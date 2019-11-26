
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lizhewen
 */
public class AsciiConverter {
    
    public int[] AsciiConverter(String msg) {
        char[] c_array = msg.toCharArray();
        int[] ascii_array = new int[c_array.length];
        for (int i = 0; i < c_array.length; i++) {
            ascii_array[i] = (int) c_array[i];
        }
        System.out.println("Ascii Convertion: " + Arrays.toString(ascii_array));
        return ascii_array;
    }
    
}
