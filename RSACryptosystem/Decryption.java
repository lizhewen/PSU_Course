
import java.util.Arrays;
import java.util.Scanner;
import java.math.*;
import java.math.BigInteger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lizhewen
 */
public class Decryption {
    Scanner scan = new Scanner(System.in);
    String n, d;
    BigInteger base, power, k;
    
    public Decryption() {
        System.out.print("Pls input n: ");
        n = scan.next();
        System.out.print("Pls input d: ");
        d = scan.next();
        System.out.println("Here's your private key: (" + n + ", " + d + ")");
        System.out.print("Input message (separeted by comma): ");
        String message = scan.next();
        power = new BigInteger(d);
        k = new BigInteger(n);
        String[] values = message.split(",");
//        String[] values = new String[chars.length];
//        for (int k = 0; k < chars.length; k++) {
//            char c = chars[k].charAt(0);
//            values[k] = (int) c;
//        }
        String[] ascii_decipher = new String[values.length];
        String[] decipher = new String[values.length];
        for(int i = 0; i < values.length; i++) {
            base = new BigInteger(values[i]);
            BigInteger ans = base.modPow(power, k);
            ascii_decipher[i] = ans.toString();
            decipher[i] = Character.toString((char)ans.intValue());
        }
        
        System.out.print("Ascii Numbers: ");
        System.out.println(Arrays.toString(ascii_decipher));
        System.out.print("The message is: ");
        System.out.println(Arrays.toString(decipher));
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
    
}
