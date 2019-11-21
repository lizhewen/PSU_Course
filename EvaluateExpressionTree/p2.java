import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class p2 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        while (input != null) {
            System.out.println("The input is: " + input);
            String[] arrOfStr = input.split(" ", 0);
            String order;
            ExpressionTree expTree;
            if (ifOperator(arrOfStr[1])) { // if input's infix
                //System.out.println("This is infix");
                order = "postorder";
                expTree = new PostfixExpressionTree();
            }
            else { // if input's postfix
                order = "inorder";
                expTree = new InfixExpressionTree();
            }
            // build expression tree from input
            treeNode result = expTree.build_expression_tree(arrOfStr);
            System.out.print("The " + order + " traversal is: ");
            // do corresponding traversal
            expTree.traversal(result);
            System.out.println();
            double eval = expTree.evaluate_expression_tree(result);
            System.out.printf("The output of the expression is: %.2f\n", eval);

            input = reader.readLine();
        }
    }

    public static Boolean ifOperator(String a) {
        if (a.equals("+") || a.equals("-") || a.equals("*") || a.equals("/")) {
            return true;
        }
        return false;
    }
}

class Stack { // implementation of stack
    sNode root;

    // type def
    static class sNode { // stack node type for stack
        treeNode value;
        sNode next;

        // constructor
        sNode(treeNode value) {
            this.value = value;
        }
    }
    
    public void push(treeNode num) {
        sNode newNode = new sNode(num);
        if (root == null) // if current stack is empty
            root = newNode;
        else {
            sNode temp = root;
            root = newNode; // insert at head
            newNode.next = temp; // link newNode with the rest of stack
        }
    }

    public treeNode pop() {
        // assuming input is valid, no case of popping from empty stack exists
        treeNode returnValue = root.value; // get returnValue
        root = root.next; // remove current head on stack
        return returnValue;
    }

}

class treeNode { // treeNode type for expression tree
    String value;
    treeNode left, right;
    
    // constructor
    treeNode(String value) {
        this.value = value;
        left = right = null;
    }
}

interface ExpressionTree {
    // all are the abstract methods
    public void traversal(treeNode a);
    public treeNode build_expression_tree(String[] input);
    public double evaluate_expression_tree(treeNode a);
}

class InfixExpressionTree implements ExpressionTree {

    public void traversal(treeNode a) { // inorder traversal
        if (a != null) {
            traversal(a.left);
            System.out.print(a.value + " ");
            traversal(a.right);
        }
    }

    public treeNode build_expression_tree(String[] input) {
        Stack myStack = new Stack();
        treeNode t, tl, tr;
        for (int i = 0; i < input.length; i++) {
            if (p2.ifOperator(input[i]) == false) { // encounter a number
                treeNode a = new treeNode(input[i]);
                myStack.push(a);
            }
            else { // encounter an operator
                t = new treeNode(input[i]); // construct a new operator treeNode
                tr = myStack.pop();
                tl = myStack.pop();
                // link both subtrees to the new treeNode
                t.right = tr;
                t.left = tl;
                // push the new node back onto stack
                myStack.push(t);
            }
        }
        return myStack.pop(); // return root node of the expression tree
    }

    public double evaluate_expression_tree(treeNode a) {
        if (a == null) { // empty tree
            return 0;
        }
        if (a.left == null && a.right == null) { // leaf node
            return Double.valueOf(a.value);
        }
        // recursively evaluate both subtrees
        double leftResult =  evaluate_expression_tree(a.left);
        double rightResult =  evaluate_expression_tree(a.right);

        switch (a.value) {
            case "+":
                return leftResult + rightResult;
            case "-":
                return leftResult - rightResult;
            case "*":
                return leftResult * rightResult;
            case "/":
                return leftResult / rightResult;
        }
        return 0; // should not run here
    }
}

class PostfixExpressionTree extends InfixExpressionTree {
    // build expression tree from infix input
    // all other methods are the same as InfixExpressionTree

    @Override
    public void traversal(treeNode a) { // postorder traversal
        if (a != null) {
            traversal(a.left);
            traversal(a.right);
            System.out.print(a.value + " ");
        }
    }

    @Override
    public treeNode build_expression_tree(String[] input) {
        Stack myStack = new Stack();
        treeNode t, tl, tr, temp;
        for (int i = 0; i < input.length; i++) {
            if (p2.ifOperator(input[i]) == false) { // encounter a number
                treeNode a = new treeNode(input[i]);
                myStack.push(a);
            }
            else { // encounter an operator
                if (input[i].equals("*") || input[i].equals("/")) { // higher priority operators
                    t = myStack.pop(); // keep the original structure
                    temp = new treeNode(input[i]);
                    if (t.left == null && t.right == null) { // if t is a leaf node
                        temp.left = t;
                        temp.right = new treeNode(input[i+1]);
                        myStack.push(temp); // push the new tree back onto stack
                    }
                    else {
                        temp.left = t.right;
                        temp.right = new treeNode(input[i+1]);
                        t.right = temp;
                        myStack.push(t); // push the new tree back onto stack
                    }
                }
                else { // no priority case
                    t = new treeNode(input[i]); // construct a new operator treeNode
                    tl = myStack.pop();
                    tr = new treeNode(input[i+1]);
                    t.right = tr;
                    t.left = tl;
                    // push the new tree back onto stack
                    myStack.push(t);
                }
                i += 1; // skip next number
            }
        }
        return myStack.pop(); // return root node of the expression tree
    }
}