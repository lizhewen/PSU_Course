/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lizhewen
 */
public class EuclidAlg {
    
    public static int gcd(int p, int q) {
        if (p == 0) {
            return q;
        }
        return gcd(q % p, p);
    }
    
    
    // px + qy = gcd(p, q)
    public static int modInverse(int p, int q, int x, int y)
    {
        // Base Case
        if (p == 0)
        {
            x = 0;
            y = 1;
            return q;
        }
 
        int x1=1, y1=1; // To store results of recursive call
        int gcd = modInverse(q % p, p, x1, y1);
 
        // Update x and y using results of recursive call
        x = y1 - (q / p) * x1;
        y = x1;
        
        // return the modular inverse, aka coefficient of p
        return x;
    }
    
}
