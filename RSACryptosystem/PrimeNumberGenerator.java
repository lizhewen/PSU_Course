
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lizhewen
 */
public class PrimeNumberGenerator {
    
    public static int getNum() {
        int num = 0;
        Random rand = new Random(); // generate a random number
        num = rand.nextInt(500) + 1;

        while (!isPrime(num)) {          
            num = rand.nextInt(500) + 1;
        }
        return num;
    }

    /**
     * Checks to see if the requested value is prime.
     */
    static boolean isPrime(int inputNum){
        if (inputNum < 0) {
            return false;
        }
        if (inputNum <= 3 || inputNum % 2 == 0) 
            return inputNum == 2 || inputNum == 3; //this returns false if number is <=1 & true if number = 2 or 3
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0)) 
            divisor += 2; //iterates through all possible divisors
        return inputNum % divisor != 0; //returns true/false
    }
}
