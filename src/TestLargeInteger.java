// ------------------------------------------------------------------------
// TestLargeInteger.java
//
// Author: M'Hand KEDJAR
// First Version: March 31, 2012
// Last Update: October 18, 2017
// Course: Data Structures and Algorithms - Winter 2012 - CCCS-315-761
// Description: This class Run some randomly generated large integers
// 				to perform the addition, subtraction, multiplication, division,
// 				exponentiation, and comparison
// Known Bugs: None
// ------------------------------------------------------------------------

import java.math.BigInteger;

public class TestLargeInteger {

    public static void main(String[] args) {
        int numberTry = 10;
        int i = 0;
        // used to generate a LargeInteger that has a number of digits between minimum1 and maximum1
        // used to generate the first number
        int minimum1 = 30;		// minimum number of digits of the first generated LargeInteger
        int maximum1 = 35;		// maximum number of digits
        int randomNum1 = 0;		// number of digits of the first generated LargeInteger
        int loop1 = 0;			// used to loop from 0 to randomNum1 to generate random digits
        String string1 = "";	// used to store as a String the generated LargeInteger
        // used to generate the second number
        int minimum2 = 30;
        int maximum2 = 35;
        int randomNum2 = 0;
        int loop2 = 0;
        String string2 = "";
        // used to generate the exponential
        int minimum3 = 0;
        int maximum3 = 5;
        int randomNum3 = 0;
        int exp = 0;
        // used to generate negative numbers
        boolean isNegativeNumber = false;

        for( i = 0; i < numberTry; i++){
            // generate an integer between minimum and maximum
            randomNum1 = minimum1 + (int)(Math.random() * ((maximum1 - minimum1) + 1));
            randomNum2 = minimum2 + (int)(Math.random() * ((maximum2 - minimum2) + 1));
            randomNum3 = minimum3 + (int)(Math.random() * ((maximum3 - minimum3) + 1));
            // Initialisation of the variables
            string1 = "";
            string2 = "";
            loop1 = 0;
            loop2 = 0;

            // Generating the first string
            while (loop1 < randomNum1 ) {
                String random1 = Double.toString(Math.random());
                string1 = string1 + random1.substring(2 , 3);
                loop1++;
            }
            // Generating the second string
            while (loop2 < randomNum2) {
                String random2 = Double.toString(Math.random());
                string2 = string2 + random2.substring(2 , 3);
                loop2++;
            }
            // Generating the exponential
            exp = randomNum3;

            // randomly generate positive and negative numbers
            isNegativeNumber = (((int)(Math.random() * 10) + 1) > 5) ? false : true;
            if(isNegativeNumber){string1 = "-" + string1;}
            isNegativeNumber = (((int)(Math.random() * 10) + 1) > 5) ? false : true;
            if(isNegativeNumber){string2 = "-" + string2;}
            isNegativeNumber = (((int)(Math.random() * 10) + 1) > 5) ? false : true;
            if(isNegativeNumber){ exp = -exp;}

            System.out.println("Iteration number: " +(i+1));
            System.out.println("string1: "+string1);
            System.out.println("string2: "+string2);

            // Creating the 2 LargeInteger
            LargeInteger firstInt = new LargeInteger(string1);
            LargeInteger secondInt = new LargeInteger(string2);

            // Creating 2 BigInteger from the Java built-in class
            BigInteger bigInt1 = new BigInteger(string1);
            BigInteger bigInt2 = new BigInteger(string2);

            // Displaying the 2 Integers
            System.out.print("First integer: "); firstInt.display(); System.out.println();
            System.out.printf("%d digits\n", firstInt.getSize());

            System.out.print ("Second integer: "); secondInt.display(); System.out.println();
            System.out.printf("%d digits\n", secondInt.getSize());

            System.out.println("exp: "+exp);

            // Calculating the sum
            System.out.println("ADDITION:");
            System.out.println("*********");
            LargeInteger sum = firstInt.add(secondInt);
            System.out.print("sum:      "); sum.display();
            System.out.println();
            // Calculating the sum using the java method
            System.out.print("sum Java: "+bigInt1.add(bigInt2));
            System.out.println();

            // Calculating the difference
            System.out.println("SUBTRACTION: ");
            System.out.println("************");
            LargeInteger difference = firstInt.subtract(secondInt);
            System.out.print("diff:      "); difference.display();
            System.out.println();
            // Calculating the difference using the java method
            System.out.print("diff Java: "+bigInt1.subtract(bigInt2));
            System.out.println();

            // Calculating the product
            System.out.println("MULTIPLICATION: ");
            System.out.println("***************");
            LargeInteger product = firstInt.multiply(secondInt);
            System.out.print("product:      "); product.display();
            System.out.println();
            // Calculating the product using the java method
            System.out.print("product Java: "+bigInt1.multiply(bigInt2));
            System.out.println();

            // Calculating the exponential
            System.out.println("EXPONENATION: ");
            System.out.println("*************");
            LargeInteger exponential = firstInt.power(exp);
            if(exponential != null){
                System.out.print("exponential:      ");
                if(exp >= 0){
                    exponential.display();
                    System.out.println();
                }
                else{
                    System.out.println("1/"+exponential.toString());
                }
            }
            else{
                System.out.println("The result is not a number");

            }

            // Calculating the exponential using the java method
            if(exp >= 0){
                System.out.println("exponential Java: "+bigInt1.pow(Math.abs(exp)));
            }
            else{
                System.out.println("exponential Java: 1/"+bigInt1.pow(Math.abs(exp)));
            }

            // Calculating the division
            System.out.println("DIVISION: ");
            System.out.println("*********");

            // Calculating the quotient
            LargeInteger divrem[] = firstInt.divide(secondInt);
            if(divrem != null){
                System.out.print("Quotient  = 	"); divrem[0].display();
                System.out.println();
                // Calculating the remainder
                System.out.print("Remainder = 	"); divrem[1].display();
                System.out.println();
            }
            else{
                System.out.println("Can not divide by zero");
            }

            // Calculating the quotient using the java method
            System.out.println("Quotient Java:  "+bigInt1.divide(bigInt2));
            // Calculating the remainder using the java method
            System.out.println("Remainder Java: "+bigInt1.remainder(bigInt2));

            System.out.println("*COMPARISON: ");
            System.out.println("************* ");
            // Comparing the two numbers
            int compareTwoLargeIntegers = firstInt.compareTo(secondInt);
            System.out.print("Compare:      ");
            if(compareTwoLargeIntegers == 1){
                firstInt.display(); System.out.print(" is greater than ");secondInt.display();
            }
            else if(compareTwoLargeIntegers == -1){
                secondInt.display(); System.out.print(" is greater than ");firstInt.display();
            }
            else{
                secondInt.display(); System.out.print(" and ");firstInt.display();System.out.print(" are equals ");
            }
            System.out.println();

            // Comparing the two numbers using java method
            int compareTwoLargeIntegersJava = bigInt1.compareTo(bigInt2);
            System.out.print("Compare Java: ");
            if(compareTwoLargeIntegersJava == 1){
                firstInt.display(); System.out.print(" is greater than ");secondInt.display();
            }
            else if(compareTwoLargeIntegersJava == -1){
                secondInt.display(); System.out.print(" is greater than ");firstInt.display();
            }
            else{
                secondInt.display(); System.out.print(" and ");firstInt.display();System.out.print(" are equals ");
            }
            System.out.println();
            System.out.println("------------------------------------------------------------");
        }// end for loop
    }// end main
}// end TestLargeInteger class
