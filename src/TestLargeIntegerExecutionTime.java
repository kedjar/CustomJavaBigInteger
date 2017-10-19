// ------------------------------------------------------------------------
// TestLargeIntegerExecutionTime.java
//
// Author: M'Hand KEDJAR
// First version: March 31, 2012
// Last Update: October 18, 2017
// Course: Data Structures and Algorithms - Winter 2012 - CCCS-315-761
// Description: This class Run some randomly generated large integers
// 				to perform the addition, subtraction, multiplication, division,
// 				exponentiation, and comparison, and compare the execution time
//				between LargeInteger class, and Java BigInteger class
// Known Bugs: None
// ------------------------------------------------------------------------

import java.math.BigInteger;
import java.util.ArrayList;

public class TestLargeIntegerExecutionTime {

    public static void main(String[] args) {
        int numberTry = 1;	// number of iterations
        int i;				// for looping
        // these ArrayList() will contain the execution time for each operation
        ArrayList<Long> timeExecLargeInt = new ArrayList<Long>(); // LargeInteger method
        ArrayList<Long> timeExecJava = new ArrayList<Long>();	  // Java BigInteger method
        // this ArrayList() will contain the number of digits of the generated numbers
        ArrayList<Long> integerLenght = new ArrayList<Long>();
        // LargeInteger method
        long timeAdd = 0;	// Execution time for addition
        long timeSub = 0;	// Execution time for subtraction
        long timeMul = 0;	// Execution time for multiplication
        long timeExp = 0;	// Execution time for exponentiation
        long timeDiv = 0;	// Execution time for division
        long timeCom = 0;	// Execution time for comparison
        // Java BigInteger method
        long timeAddJ = 0;
        long timeSubJ = 0;
        long timeMulJ = 0;
        long timeExpJ = 0;
        long timeDivJ = 0;
        long timeComJ = 0;
        // Used to calculate the execution time for each operation: timeEnd - timeBegin
        long timeBegin = 0;
        long timeEnd  = 0;
        // used to generate a LargeInteger that has a number of digits between minimum1 and maximum1
        // used to generate the first number
        int minimum1 = 200;		// minimum number of digits of the first generated LargeInteger
        int maximum1 = 220;		// maximum number of digits
        int randomNum1 = 0;		// number of digits of the first generated LargeInteger
        int loop1 = 0;			// used to loop from 0 to randomNum1 to generate random digits
        String string1 = "";	// used to store as a String the generated LargeInteger
        // used to generate the second number
        int minimum2 = 200;
        int maximum2 = 220;
        int randomNum2 = 0;
        int loop2 = 0;
        String string2 = "";
        // used to generate the exponential
        int minimum3 = 0;
        int maximum3 = 6;
        int randomNum3 = 0;
        int exp = 0;

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

            integerLenght.add((long)string1.length());
            integerLenght.add((long)string2.length());
            integerLenght.add((long)exp);
            // Creating the 2 LargeInteger
            LargeInteger firstInt = new LargeInteger(string1);
            LargeInteger secondInt = new LargeInteger(string2);

            // Creating 2 BigInteger from the Java built-in class
            BigInteger bigInt1 = new BigInteger(string1);
            BigInteger bigInt2 = new BigInteger(string2);

            // Calculating the sum
            timeBegin = System.currentTimeMillis();
            firstInt.add(secondInt);
            timeEnd = System.currentTimeMillis();
            timeExecLargeInt.add(timeEnd - timeBegin);

            // Calculating the sum using the java method
            timeBegin = System.currentTimeMillis();
            bigInt1.add(bigInt2);
            timeEnd = System.currentTimeMillis();
            timeExecJava.add(timeEnd - timeBegin);

            // Calculating the difference
            timeBegin = System.currentTimeMillis();
            firstInt.subtract(secondInt);
            timeEnd = System.currentTimeMillis();
            timeExecLargeInt.add(timeEnd - timeBegin);

            // Calculating the difference using the java method
            timeBegin = System.currentTimeMillis();
            bigInt1.subtract(bigInt2);
            timeEnd = System.currentTimeMillis();
            timeExecJava.add(timeEnd - timeBegin);

            // Calculating the product
            timeBegin = System.currentTimeMillis();
            firstInt.multiply(secondInt);
            timeEnd = System.currentTimeMillis();
            timeExecLargeInt.add(timeEnd - timeBegin);

            // Calculating the product using the java method
            timeBegin = System.currentTimeMillis();
            bigInt1.multiply(bigInt2);
            timeEnd = System.currentTimeMillis();
            timeExecJava.add(timeEnd - timeBegin);

            // Calculating the exponential
            timeBegin = System.currentTimeMillis();
            firstInt.power(exp);
            timeEnd = System.currentTimeMillis();
            timeExecLargeInt.add(timeEnd - timeBegin);

            // Calculating the exponential using the java method
            timeBegin = System.currentTimeMillis();
            bigInt1.pow(exp);
            timeEnd = System.currentTimeMillis();
            timeExecJava.add(timeEnd - timeBegin);

            // Calculating the division
            timeBegin = System.currentTimeMillis();
            firstInt.divide(secondInt);
            timeEnd = System.currentTimeMillis();
            timeExecLargeInt.add(timeEnd - timeBegin);

            // Calculating the quotient using the java method
            timeBegin = System.currentTimeMillis();
            bigInt1.divide(bigInt2);
            // Calculating the remainder using the java method
            bigInt1.mod(bigInt2);
            timeEnd = System.currentTimeMillis();
            timeExecJava.add(timeEnd - timeBegin);

            // Comparing the two numbers
            timeBegin = System.currentTimeMillis();
            firstInt.compareTo(secondInt);
            timeEnd = System.currentTimeMillis();
            timeExecLargeInt.add(timeEnd - timeBegin);

            // Comparing the two numbers using java method
            timeBegin = System.currentTimeMillis();
            bigInt1.compareTo(bigInt2);
            timeEnd = System.currentTimeMillis();
            timeExecJava.add(timeEnd - timeBegin);
        }

        System.out.println();
        System.out.println("*****************************************");
        System.out.println("************EXECUTION TIME***************");
        System.out.println("*****************************************");

        int k = 0;
        int h = 0;
        for(int j = 0; j < 6*numberTry; j = j + 6){
            if((h % 50)== 0){
                System.out.println("Iteration number: " + h);
                System.out.printf("1st Integer:    %d\t digits\n", (long)integerLenght.get(k));
                System.out.printf("2nd Integer:    %d\t digits\n", (long)integerLenght.get(k+1));
                System.out.printf("exponential:    %d\t \n", (long)integerLenght.get(k+2));

                System.out.printf("Method:         %s\t     %s\n", "LargeInt", "Java");

                System.out.printf("Addition:       %5.2f\t\t    %5.2f\n", (double)timeExecLargeInt.get(j), (double)timeExecJava.get(j));
                System.out.printf("Substraction:   %5.2f\t\t    %5.2f\n", (double)timeExecLargeInt.get(j+1) , (double)timeExecJava.get(j+1));
                System.out.printf("Multiplication: %5.2f\t\t    %5.2f\n", (double)timeExecLargeInt.get(j+2), (double)timeExecJava.get(j+2));
                System.out.printf("Exponential:    %5.2f\t\t    %5.2f\n", (double)timeExecLargeInt.get(j+3) , (double)timeExecJava.get(j+3));
                System.out.printf("Division:       %5.2f\t\t    %5.2f\n", (double)timeExecLargeInt.get(j+4), (double)timeExecJava.get(j+4));
                System.out.printf("Comparison:    %5.2f\t\t    %5.2f\n", (double)timeExecLargeInt.get(j+5), (double)timeExecJava.get(j+5));
                System.out.println("*****************************************");
            }
            k= k+3;
            h = h + 1;

            timeAdd = timeAdd + timeExecLargeInt.get(j) ;
            timeSub = timeSub + timeExecLargeInt.get(j+1) ;
            timeMul = timeMul + timeExecLargeInt.get(j+2) ;
            timeExp = timeExp + timeExecLargeInt.get(j+3) ;
            timeDiv = timeDiv + timeExecLargeInt.get(j+4) ;
            timeCom = timeCom + timeExecLargeInt.get(j+5) ;

            timeAddJ = timeAddJ + timeExecJava.get(j) ;
            timeSubJ = timeSubJ + timeExecJava.get(j+1) ;
            timeMulJ = timeMulJ + timeExecJava.get(j+2) ;
            timeExpJ = timeExpJ + timeExecJava.get(j+3) ;
            timeDivJ = timeDivJ + timeExecJava.get(j+4) ;
            timeComJ = timeComJ + timeExecJava.get(j+5) ;

        }
        // Calculate the mean execution time
        long timeAddM = timeAdd / numberTry ;
        long timeSubM = timeSub / numberTry ;
        long timeMulM = timeMul / numberTry ;
        long timeExpM = timeExp / numberTry ;
        long timeDivM = timeDiv / numberTry ;
        long timeComM = timeCom / numberTry ;

        long timeAddJM = timeAddJ / numberTry ;
        long timeSubJM = timeSubJ / numberTry ;
        long timeMulJM = timeMulJ / numberTry ;
        long timeExpJM = timeExpJ / numberTry ;
        long timeDivJM = timeDivJ / numberTry ;
        long timeComJM = timeComJ / numberTry ;


        System.out.println();
        System.out.println("****************************************");
        System.out.println("*********MEAN EXECUTION TIME************");
        System.out.println("************Millesconds*****************");
        System.out.println("****************************************");

        System.out.println("number of iterations : "+numberTry);
        System.out.printf("1st Integer: between %d and %d\t digits\n", minimum1 , maximum1);
        System.out.printf("2nd Integer: between %d and %d\t digits\n", minimum2 , maximum2);
        System.out.printf("exponetial: integer between %d and %d\t \n", minimum3 , maximum3);

        System.out.printf("Method:         %s\t     %s\n", "LargeInt", "Java");

        System.out.printf("Addition:       %5.2f\t\t    %5.2f\n", (double)timeAddM, (double)timeAddJM);
        System.out.printf("Substraction:   %5.2f\t\t    %5.2f\n", (double)timeSubM , (double)timeSubJM);
        System.out.printf("Multiplication: %5.2f\t\t    %5.2f\n", (double)timeMulM, (double)timeMulJM);
        System.out.printf("Exponential:    %5.2f\t\t    %5.2f\n", (double)timeExpM , (double)timeExpJM);
        System.out.printf("Division:       %5.2f\t\t    %5.2f\n", (double)timeDivM, (double)timeDivJM);
        System.out.printf("Comparison:    %5.2f\t\t    %5.2f\n", (double)timeComM, (double)timeComJM);
        System.out.println("*****************************************");

    }

}
