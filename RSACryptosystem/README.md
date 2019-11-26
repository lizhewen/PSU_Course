# RSA Cryptosystem Program
###### Author: Eric Zhewen Li
###### Contact: eric@zhewenli.com
This is a RSA Cryptosystem Project. It was designed for CMPSC 360 course as the final project (Penn State University, Spring 2018).
The following intro are taken from my project report for the course. It introduces what is RSA and how it works.

## RSA Intro:
RSA cryptosystem is a very important algorithm of Computer Science and it is really important for us to understand how it works as well as how to implement it into real-life programming. The general idea why RSA is difficult to be cracked is because of the difficulty of factorization of the sum of two large prime numbers. In addition, RSA is an asymmetric cryptosystem, meaning that its public key (used for encryption) is different than its private key (used for decryption) and this enables the possibility of sharing the public key without fearing ciphertexts being cracked – hackers have to have the corresponding private key to decrypt messages. And by using p and q that are large enough makes the refactorization process really hard and almost impossible.
#### This program can both encrypt and decrypt and it first prompts user for which mode to use.
## Encryption Mode:
User can decide whether to specify the values of p and q (i.e specify public key to encrypt). If not, the program will general those values for user randomly. User can also specify the value of e. If not, the program will find the smallest possible e value. Then user can input messages to be encrypted - strings will be transferred to ASCII numbers and then encrypted to new numbers by RSA.
## Decryption Mode:
When user has the private key and ciphertext, they can decrypt by inputting the ciphertext (as in ASCII numbers, divided by comma) and the program will decrypt the message back to the original string.
