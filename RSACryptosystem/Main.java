/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Scanner;
import java.math.*;

/**
 *
 * @author lizhewen
 */
public class Main {
    
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        
        while(true) {
            System.out.print("1 for encryption and 2 for decryption: ");
            int option = scan.nextInt();
            if (option == 0) {
                break;
            }
            else if (option == 1) {
                Encryption obj = new Encryption();
                continue;
            }
            else if (option == 2) {
                Decryption obj = new Decryption();
                continue;
            }
            else {
                System.out.println("Error. Pls try again.");
                continue;
            }
        }
        System.out.println("*** End of program ***");
    }
    
}
