#include <stdio.h>
#include "csapp.h"

/* Recommended max cache and object sizes */
#define MAX_CACHE_SIZE 16777216 
#define MAX_OBJECT_SIZE 8388608
#define MAX_BLOCK_SIZE 2 /* = MAX_CACHE_SIZE / MAX_OBJECT_SIZE */

/* You won't lose style points for including this long line in your code */
static const char *user_agent_hdr = "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:10.0.3) Gecko/20120305 Firefox/10.0.3\r\n";

/* listNode structure for cached blocks */
typedef struct listNode {
  char data[MAX_OBJECT_SIZE];
  char uri[MAXLINE]; 
  struct listNode *next;
  struct listNode *prev;
} listNode;

/* use doublyLinkedList to keep track of cached contents */
typedef struct doublyLinkedList {
  listNode *head;
  listNode *tail;
} doublyLinkedList;

/* initialize cache */
static doublyLinkedList list = {NULL, NULL};

int if_hit(doublyLinkedList *listPtr, char *uri);
void update_cache(doublyLinkedList *listPtr, int index);
void add_cache(char *uri, char *buf, doublyLinkedList *list);
void insertHead(doublyLinkedList *listPtr, char *uri, char *buf);
void removeTail(doublyLinkedList *listPtr);


void doit(int fd, doublyLinkedList *listPtr);
void parse_uri(char *uri, char *host, char *path, char *port);


int main(int argc, char **argv)
{
  int listenfd, connfd;
  char hostname[MAXLINE], port[MAXLINE];
  socklen_t clientlen;
  struct sockaddr_storage clientaddr;

  if (argc != 2) { /* make sure user is specifying port number */
    fprintf(stderr, "usage: %s <port>\n", argv[0]);
    exit(1); 
  }

  Signal(SIGPIPE, SIG_IGN);

  listenfd = Open_listenfd(argv[1]); /* start listening on the user-specified port  */
  while (1) {
    clientlen = sizeof(clientaddr);
    connfd = Accept(listenfd, (SA *)&clientaddr, &clientlen);
    Getnameinfo((SA *) &clientaddr, clientlen, hostname, MAXLINE, port, MAXLINE, 0);
    printf("Accepted connection from (%s, %s)\n", hostname, port);
    doit(connfd, &list); /* get client's address to send the message back to */
    Close(connfd); /* close this connection */
  }
  return 0;
}

/*
 * doit - handle one HTTP request/response transaction
 *        and cache implementation
 */
void doit(int fd, doublyLinkedList *listPtr) { 
  char buf[MAXLINE], method[MAXLINE], uri[MAXLINE], version[MAXLINE];
  char cache_buf[MAX_OBJECT_SIZE];
  char host[MAXLINE], path[MAXLINE], port[5];

  int total_size, connfd;
  rio_t userInput; /* use Robust I/O package to save user's input */
  rio_readinitb(&userInput, fd); /* Read request line and headers */
  if (!rio_readlineb(&userInput, buf, MAXLINE)) {
    fprintf(stderr, "Read line error!\n");
    return;
  }
  sscanf(buf, "%s %s %s", method, uri, version); 
  if (strcasecmp(method, "GET")) { /* error handling: if user input method other than GET */
    fprintf(stderr, "proxy only accepts GET method!\n");
    return;
  }

  if (listPtr->head != NULL) { /* if cache is not empty */
    int blockLocation = if_hit(listPtr, uri);
    if (blockLocation != -1) { /* if cache hits */
      int i;
      listNode *temp = listPtr->head;
      for(i = 0; i < blockLocation; i++) { /* iterate till blockLocation */
        temp = temp->next;
      }
      if (rio_writen(fd, temp->data, strlen(temp->data)) < 0) {
        fprintf(stderr, "rio_writen error!\n");
        return;
      }
      update_cache(listPtr, blockLocation); /* update cache to reflect its usage */
    }
  }
  
  parse_uri(uri, host, path, port);

  int socket_fd = Open_clientfd(host, port); /* connect to server and get the socket descriptor */
  if (socket_fd < 0) {
    fprintf(stderr, "Connection Error\n");
    return;
  }

  char const *connection = "Connection: close\r\nProxy-Connection: close\r\n";

  /* write request to connect to server */
  sprintf(buf, "GET %s HTTP/1.0\r\n", path);
  rio_writen(socket_fd, buf, strlen(buf)); /* use buffer and rio to write in path */
  sprintf(buf, "Host: %s\r\n", host);
  rio_writen(socket_fd, buf, strlen(buf)); /* use buffer and rio to write in host */
  rio_writen(socket_fd, user_agent_hdr, strlen(user_agent_hdr)); /* write in browser info */
  /* end of request to close the connection with server */
  rio_writen(socket_fd, connection, strlen(connection));
  rio_writen(socket_fd, "\r\n", strlen("\r\n"));

  cache_buf[0] = '\0'; /* clear cache buffer each time doit is called */

  int n;
  rio_readinitb(&userInput, socket_fd); /* read server's response */
  /* give it back to client */
  while ((n = rio_readlineb(&userInput, buf, MAXLINE))) {
    rio_writen(fd, buf, n);
    strcat(cache_buf, buf);
    strcpy(cache_buf + total_size, buf);
    total_size += n;
  }

  if (total_size < MAX_OBJECT_SIZE) {
    add_cache(uri, cache_buf, listPtr); /* cache the most-recently-used site */
  }

  Close(socket_fd);
}

/*
 * parse_uri - parse uri to the format of host, path, and port
 */
void parse_uri(char *uri, char *host, char *path, char *port) {
  if (strstr(uri, "//") != NULL) {
    uri += strlen("http://"); /* get rid of "http://" */
  }
  char *portPtr = strstr(uri, ":");
  char *pathPtr = strstr(uri, "/");
  int intPort;
  if (portPtr == NULL) { /* if no port is specified */
    intPort = 80; /* use default port 80 */
    sprintf(port, "%d", intPort);
    if (pathPtr == NULL) { /* if no path is specified */
      strcpy(host, uri);
    }
    else { /* if a path is specified */
      int i;
      for (i = 0; ; i++) {
        host[i] = uri[i];
        if (uri[i] == '/') { /* get to the path part */
          host[i]='\0';
          break;
        }
      }
      strcpy(path, pathPtr);
    }
  }
  else { /* if a port is specified */
    int j;
    for (j = 0; ; j++) {
      host[j] = uri[j];
      if (uri[j] == ':') { /* get to the port part */
        host[j]='\0';
        break;
      }
    }
    if (pathPtr == NULL) { /* if no path is specified */
      path = '\0';
    }
    else {
      strcpy(path, pathPtr);
    }
    sscanf(portPtr+1, "%d", &intPort); /* port number is the only int between : and / */
    sprintf(port, "%d", intPort);
  }
}

/*
 * init_hit - return location pointer if it's a hit and -1 otherwise
 */
int if_hit(doublyLinkedList *listPtr, char *uri) {
  int i;
  listNode *temp = listPtr->head;
  for(i = 0; temp != NULL; i++) { /* iterate thru all blocks */
    if (strcmp(temp->uri, uri) == 0) {
        return i; /* cache hit */
    }
    temp = temp->next;
  }
  return -1; /* cache miss */
}

/*
 * update_cache - if a cache hit, relink it to be the head
 */
void update_cache(doublyLinkedList *listPtr, int index) {
  int i;
  listNode *blockLocation = listPtr->head;
  for (i = 0; i < index; i++) {
    blockLocation = blockLocation->next;
  }
  listNode *prevNode = blockLocation->prev;
  if (prevNode == NULL) { /* blockLocation is already head */
    return;
  }
  listNode *nextNode = blockLocation->next;
  listNode *oldHead = listPtr->head;
  nextNode->prev = prevNode;
  prevNode->next = nextNode;
  oldHead->prev = blockLocation;
  blockLocation->next = oldHead;
  blockLocation->prev = NULL;
  listPtr->head = blockLocation;
  return;
}

/*
 * add_cache - add a missed uri to cache, remove tail if current cache is full
 */
void add_cache(char *uri, char *buf, doublyLinkedList *list) { 
  int i;
  listNode *temp = list->head;
  for(i = 0; (i < MAX_BLOCK_SIZE) && (temp != NULL); i++) { /* move temp to tail */
    temp = temp->next;
  }
  if (temp != NULL) { /* if cache is currently full */
    removeTail(list);
  }
  insertHead(list, uri, buf); /* add most recently-used cache to head */
}

void insertHead(doublyLinkedList *listPtr, char *uri, char *buf) {
  listNode *newNodePtr = (listNode *)malloc(sizeof(listNode));
  listNode *head = listPtr->head;
  strcpy(newNodePtr->data, buf);
  strcpy(newNodePtr->uri, uri);
  newNodePtr->prev = NULL;
  if (head == NULL) { /* if the list is empty */
    newNodePtr->next = NULL;
    /* set both head and tail pointer to newly-inserted node */
    listPtr->head = newNodePtr;
    listPtr->tail = newNodePtr;
  }
  else { /* if the list is not empty */
    newNodePtr->next = head; /* link this node to the rest of list */
    head->prev = newNodePtr; /* original head is linked to new head */
    listPtr->head = newNodePtr; /* set head pointer to newly-inserted node */
  }
}

void removeTail(doublyLinkedList *listPtr) {
  listNode *oldTail = listPtr->tail;
  if (oldTail->prev == NULL) { /* if the list only has one node */
    free(oldTail); /* free up the node's space */
    /* empty the head and tail pointer */
    listPtr->head = NULL;
    listPtr->tail = NULL;
  }
  else { /* if the list is not empty */
    listNode *temp = oldTail->prev;
    temp->next = NULL; /* new tail's next is NULL */
    free(oldTail); /* free up the node's space */
    listPtr->tail = temp;
  }
}
