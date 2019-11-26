
import java.util.Arrays;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lizhewen
 */
public class Encryption {
    
    // constructor
    public Encryption() {
        Scanner scan = new Scanner(System.in);
        int p, q, e = 2, d = 0;
        
        while (true) {
            System.out.print("Would you like to specify p and q? (y/n): ");
            String pqOpt = scan.next();
            if (pqOpt.equals("y")) {
                while (true) {
                    System.out.print("What's your p: ");
                    p = scan.nextInt();
                    if (PrimeNumberGenerator.isPrime(p)) {
                        break;
                    }
                    System.out.println("Error. p is not prime.");
                    continue;
                }
                
                while (true) {
                    System.out.print("What's your q: ");
                    q = scan.nextInt();
                    if (PrimeNumberGenerator.isPrime(q)) {
                        break;
                    }
                    System.out.println("Error. q is not prime.");
                    continue;
                }
                break;
            }
            else if (pqOpt.equals("n")) {
                p = PrimeNumberGenerator.getNum();
                q = PrimeNumberGenerator.getNum();
                break;
            }
            else {
                System.out.println("Error. Pls try again.");
                continue;
            }
        } // end while loop
        
        System.out.println("Your p: " + p + " and your q: " + q);
        int n = p * q;
        int k = (p-1) * (q-1);
        System.out.println("Your n: " + n + " and your k: " + k);
        
        while (true) {
            System.out.print("Would you like to specify e (y/n): ");
            String pqOpt = scan.next();
            if (pqOpt.equals("y")) {
                while(true) {
                    System.out.print("What is your e: ");
                    int input = scan.nextInt();
                    // check if it is co-prime
                    if (gcd(input, k) == 1) {
                        e = input;
                        break;
                    }
                    else{
                        System.out.println("Error. e has to be co-prime with k. Pls try again.");
                        continue;
                    }
                }
            }
            else if (pqOpt.equals("n")) {
                // generate e that is co-prime
                for(int i = 2; i < k; i++) {
                    if (gcd(k, i) == 1) {
                        e = i;
                        break;
                    }
                }
            }
            else {
                System.out.println("Error. Pls try again.");
                continue;
            }
            break;
        } // end while loop
        
        gcdProcess(k, e);
        System.out.println("--> Your e is co-prime with k.");
        
        System.out.println("Your e is " + e);
        System.out.println("Your public key is (n, e) = " + n + ", " + e);
        
        d = modInverse(e, k);
        System.out.println("Your d is " + d);
        System.out.println("Your private key is (n, d) = " + n + ", " + d);
        
        System.out.print("Input message: ");
        String message = scan.next();
        int[] asciiArray = asciiConvert(message);
        int[] ciphertext = new int[asciiArray.length];
        for(int i = 0; i < asciiArray.length; i++) {
            int c = (int) (Math.pow(asciiArray[i], e) % n);
            ciphertext[i] = c;
        }
        
        System.out.print("Your Ciphertext is: ");
        System.out.println(Arrays.toString(ciphertext));
    }
    
    public int[] asciiConvert(String msg) {
        char[] c_array = msg.toCharArray();
        int[] ascii_array = new int[c_array.length];
        for (int i = 0; i < c_array.length; i++) {
            ascii_array[i] = (int) c_array[i];
        }
        System.out.println("Ascii Convertion: " + Arrays.toString(ascii_array));
        return ascii_array;
    }
    
    public static int gcd(int p, int q) {
        if (q == 0) {
            return p;
        }
        return gcd(q, p % q);
    }
    
    public static int gcdProcess(int p, int q) {
        if (q == 0) {
            return p;
        }
        System.out.println("--> " + p + " = " + q + " * " + (p/q) + " + " + (p%q));
        return gcdProcess(q, p % q);
    }
    
    static int modInverse(int a, int m) {
        int m0 = m;
        int y = 0, x = 1;
 
        if (m == 1) {
            return 0;
        }
 
        while (a > 1) {
            int q = a / m;
            int t = m;
            m = a % m;
            a = t;
            t = y;
            y = x - q * y;
            x = t;
        }
        
        if (x < 0)
            x += m0;
        
        return x;
    }
    
}
