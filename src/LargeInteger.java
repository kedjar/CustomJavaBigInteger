// ------------------------------------------------------------------------
// LargeInteger.java
//
// Author: M'Hand KEDJAR
// First Version: March 31, 2012
// Last Update: October 18, 2017
// Course: Data Structures and Algorithms - Winter 2012 - CCCS-315-761

// Description: This class implements the arithmetic operations of two
//				decimal integers that have an arbitrary number of digits
//				Those operations are performed digit by digit by using
//				LinkedList
//				The same logic is followed in all the methods
// Known Bugs: None
// ------------------------------------------------------------------------
import java.util.LinkedList;

public class LargeInteger {

    private static final boolean PLUS = true;
    private static boolean sign = PLUS;	// Sign of the LargeInteger: PLUS: true, MINUS: false
    private LinkedList<Byte> digits;	// LinkedList of bytes
    private String string;				// String representation of the LargeInteger

    //constructor with no parameters
    public LargeInteger(){
        digits = new LinkedList<Byte>();
        sign = PLUS;
        string = "";
    }

    // constructor with one parameter as a String
    public LargeInteger(String str) {
        string = removeFirstZeros(str);		// remove the leading zeros
        digits = new LinkedList<Byte>();
        byte asciiCode = (byte)string.codePointAt(0); // first character of the string
        int index = 0;

        if(asciiCode == 45){   // if the first character is '-', the LargeInteger is negative
            index = 2;
            byte b = Byte.parseByte(string.substring(1 , 2));
            digits.addLast((byte)(-b));
        }
        else if(asciiCode == 43 || asciiCode != 45){	// first character is '+' or no sign
            if(asciiCode == 43){index = 1;}
            else if(asciiCode != 45){
                index = 0;
            }
        }
        // generating the LinkedList for the remaining characters
        for(int i = index; i < string.length(); i++){
            byte b = Byte.parseByte(string.substring(i , i+1));
            digits.addLast(b);
        }
    }// end constructor with String

    // Construct a LargeInteger from an integer
    public LargeInteger(int int1){
        digits = new LinkedList<Byte>();
        String str = Integer.toString(int1);
        for(int i = 0; i < str.length(); i++){
            byte b = Byte.parseByte(str.substring(i , i+1));
            digits.addLast(b);
        }
    }

    // method to add two LargeInteger
    public LargeInteger add(LargeInteger secondInt) {
        LargeInteger firstInt = new LargeInteger();	// first LargeInteger
        firstInt = convert(digits);					// convert the LinkedList to LargeInteger
        LargeInteger mySum = new LargeInteger();	// Sum

        LargeInteger myFirstInt = new LargeInteger();
        LargeInteger mySecondInt = new LargeInteger();
        myFirstInt = copy(firstInt);				// create a copy of the LargeInteger
        mySecondInt = copy(secondInt);

        // To add the two numbers, we are using a help method addModified()
        // which add only the absolute value of the two numbers
        // if the 2 numbers are positive
        if(myFirstInt.getDigit(0) >= 0 && mySecondInt.getDigit(0) >= 0){// both +
            mySum = addModified(absValue(myFirstInt) , absValue(mySecondInt));
            return mySum;
        }
        // if the 2 numbers are negative
        else if(myFirstInt.getDigit(0) < 0 && mySecondInt.getDigit(0) < 0){ // both -
            mySum = addModified(absValue(myFirstInt) , absValue(mySecondInt));
            setNegative(mySum);
            return mySum;
        }
        // if the first number is positive, and the second number is negative
        else if(myFirstInt.getDigit(0) > 0 && mySecondInt.getDigit(0) < 0){ // 1 + and 2 -
            if(absCompareTo(absValue(myFirstInt) , absValue(mySecondInt)) == 1 		// |1| >= |2|
                    || absCompareTo(myFirstInt , mySecondInt) == 0){
                mySum = subtractModified(absValue(myFirstInt) , absValue(mySecondInt));
                return mySum;
            }
            else{
                mySum = subtractModified(absValue(mySecondInt) , absValue(myFirstInt));
                setNegative(mySum);
                return mySum;
            }

        }
        // if the first number is negative, and the second number is positive
        else if(myFirstInt.getDigit(0) < 0 && mySecondInt.getDigit(0) > 0){ // 1 - and 2 +
            if(absCompareTo(myFirstInt , mySecondInt) == 1 		// |1| >= |2|
                    || absCompareTo(absValue(myFirstInt) , absValue(mySecondInt)) == 0){
                mySum = subtractModified(absValue(myFirstInt) , absValue(mySecondInt));
                setNegative(mySum);
                return mySum;
            }
            else{
                mySum = subtractModified(absValue(mySecondInt) ,absValue(myFirstInt));
                return mySum;
            }
        }
        // return the result
        return mySum;
    }// end add() method

    // method to add two POSITIVE LargeInteger,
    private LargeInteger addModified(LargeInteger int1, LargeInteger int2) {
        // The method used is the following
        // For example, if we want to add 12345 + 6789
        // we add digit by digit, and we propagate the carry
        // in the beginning, carry = 0
        // 9 + 5 + carry = 14 + 0 = 14 ==> result 4; carry = 1;
        // 8 + 4 + carry = 12 + 1 = 13 ==> result 3; carry = 1;
        // 7 + 3 + carry = 10 + 1 = 11 ==> result 1; carry = 1;
        // 6 + 2 + carry = 8  + 1 = 9  ==> result 9; carry = 0;
        // 0 + 1 + carry = 1  + 0 = 1  ==> result 1; carry = 0;
        // the final result: 12345 + 6789 = 19134
        LargeInteger sum = new LargeInteger();	// Sum
        int isFirstLarger = 0;					// equals 0 if the numbers have the number of digits
        // 1 if the first number is larger
        // - 1 if the second number is larger
        int smallerSize = 0;					// number of digits of the smallest number
        int largerSize = 0;						// number of digits of the largest number
        // Determine which integer has more digits
        if(int1.getSize() > int2.getSize()){	// first number is larger
            smallerSize = int2.getSize();
            largerSize = int1.getSize();
            isFirstLarger = 1;
        }
        else if(int1.getSize() <= int2.getSize()){// second number is larger
            largerSize = int2.getSize();
            smallerSize = int1.getSize();
            isFirstLarger = - 1;
        }
        else{									// same number of digits
            isFirstLarger = 0;
        }
        int diffSize = largerSize - smallerSize;// difference between the number of digits
        byte digit1 = 0;						// to store the digit of the first number
        byte digit2 = 0;						// to store the digit of the second number
        byte carry = 0;							// to store the carry
        byte temp = 0;
        // adding digit by digit
        for (int i = largerSize - 1 ; i >= largerSize - smallerSize; i--){
            if (isFirstLarger == 1) {
                digit1 = int1.getDigit(i);
                digit2 = int2.getDigit(i - diffSize);
            }
            else if (isFirstLarger == -1 || isFirstLarger == 0) {
                digit2 = int1.getDigit(i  - diffSize);
                digit1 = int2.getDigit(i);
            }
            temp = (byte)(digit1 + digit2 + carry);
            sum.addDigitFirst((byte)(temp % 10));
            carry = (byte)(temp / 10);
        }
        // completing the remaining of the digits
        for (int i = largerSize - smallerSize -1 ; i >= 0; i--){
            if(isFirstLarger == 1){
                digit1 = int1.getDigit(i);
            }
            else if(isFirstLarger == -1 || isFirstLarger == 0){
                digit1 = int2.getDigit(i);
            }
            temp = (byte)(digit1 + carry);
            sum.addDigitFirst((byte)(temp % 10));
            carry = (byte)(temp / 10);
        }
        // if the last carry is != 0
        if(carry != 0){sum.addDigitFirst(carry);}
        // returning the result
        return sum;
    }// end addModified() method

    // method to subtract two LargeInteger
    public LargeInteger subtract(LargeInteger secondInt) {
        LargeInteger firstInt = new LargeInteger();
        firstInt = convert(digits);
        LargeInteger myDiff = new LargeInteger();

        LargeInteger myFirstInt = new LargeInteger();
        LargeInteger mySecondInt = new LargeInteger();
        myFirstInt = copy(firstInt);
        mySecondInt = copy(secondInt);

        // if the two numbers are positive
        if(myFirstInt.getDigit(0) >= 0 && mySecondInt.getDigit(0) >= 0){// both +
            if(absCompareTo(absValue(myFirstInt) , absValue(mySecondInt)) == 1 		// |1| >= |2|
                    || absCompareTo(myFirstInt , mySecondInt) == 0){
                myDiff = subtractModified(absValue(myFirstInt) , absValue(mySecondInt));
                return myDiff;
            }
            else{
                myDiff = subtractModified(absValue(mySecondInt) , absValue(myFirstInt));
                setNegative(myDiff);
                return myDiff;
            }
        }// end both +
        // if the first is negative, the second is positive
        else if(myFirstInt.getDigit(0) < 0 && mySecondInt.getDigit(0) > 0){ // 1- and 2+
            myDiff = addModified(absValue(myFirstInt) , absValue(mySecondInt));
            setNegative(myDiff);
            return myDiff;
        }
        // if the two numbers are negative
        else if(myFirstInt.getDigit(0) < 0 && mySecondInt.getDigit(0) < 0){ // both -
            if(absCompareTo(absValue(myFirstInt) , absValue(mySecondInt)) == 1 		// |1| >= |2|
                    || absCompareTo(myFirstInt , mySecondInt) == 0){
                myDiff = subtractModified(absValue(myFirstInt) , absValue(mySecondInt));
                setNegative(myDiff);
                return myDiff;
            }
            else{
                myDiff = subtractModified(absValue(mySecondInt) , absValue(myFirstInt));
                return myDiff;
            }
        } // end both negative
        // if the first is positive, the second is negative
        else if(myFirstInt.getDigit(0) > 0 && mySecondInt.getDigit(0) < 0){// 1 + and 2 -
            myDiff = addModified(absValue(myFirstInt) , absValue(mySecondInt));
            return myDiff;
        }
        // returning the result
        return myDiff;
    }	// end subtract() method

    // method to subtract two POSITIVE LargeInteger
    private LargeInteger subtractModified(LargeInteger int1 , LargeInteger int2) {
        // The method used is the following
        // For example, we want to calculate : 13579 - 8642
        // we subtract digit by digit, and we propagate the borrow
        // in the beginning, borrow = 0
        // 9 - 2 - borrow = 7 - 0 = 7 ==> result = [7]; borrow = 0;
        // 7 - 4 - borrow = 3 - 0 = 3 ==> result = [3]; borrow = 0;
        // 5 - 6 - borrow = -1 - 0 = - 1 < 0 ==> result = 15 - 6 - borrow = 9 - 0 = 9
        // 							  ==> result = [9]; borrow = 1;
        // 3 - 8 - borrow = - 5 - 1 = -6 < 0 ==> result = 13 - 8 - borrow = 5 - 1 = 4
        //							  ==> result = [4]; borrow = 1;
        // 1 - 0 - borrow = 1 - 1 = 0 ==> result = 0; borrow = 0;
        // Finally the result: 13579 - 8642 = 4937
        LargeInteger diff = new LargeInteger();
        int isFirstLarger = 0;
        int smallerSize = 0;
        int largerSize = 0;

        // Determine which integer has more digits
        if(absCompareTo(int1 , int2) == 1){
            smallerSize = int2.getSize();
            largerSize = int1.getSize();
            isFirstLarger = 1;
        }
        else if(absCompareTo(int1 , int2) == -1){
            largerSize = int2.getSize();
            smallerSize = int1.getSize();
            isFirstLarger = -1;
        }
        else if(absCompareTo(int1 , int2) == 0){
            largerSize = int2.getSize();
            smallerSize = int1.getSize();
            isFirstLarger = 0;
        }

        int diffSize = largerSize - smallerSize;
        byte digit1 = 0;
        byte digit2 = 0;
        byte borrow = 0;
        byte temp = 0;

        // subtracting digit by digit
        for (int i = largerSize - 1 ; i >= largerSize - smallerSize; i--){

            if(absCompareTo(int1 , int2) == 1
                    || absCompareTo(int1, int2) == 0){
                digit1 = int1.getDigit(i);
                digit2 = int2.getDigit(i - diffSize);
            }
            else if(absCompareTo(int1 , int2) == -1){
                digit2 = int1.getDigit(i - diffSize);
                digit1 = int2.getDigit(i);
            }

            if((digit1 - digit2 - borrow) < 0){
                temp = (byte)(digit1 - digit2 - borrow + 10);
                borrow = 1;
            }
            else if((digit1 - digit2 - borrow) >= 0){
                temp = (byte)(digit1 - digit2 - borrow);
                borrow = 0;
            }
            diff.addDigitFirst((byte)(temp));
        } // end for loop

        // completing the remaining of the digits
        for (int i = largerSize - smallerSize -1 ; i >= 0; i--){
            if(isFirstLarger == 1){
                digit1 = int1.getDigit(i);
            }
            else if(isFirstLarger == -1 || isFirstLarger == 0){
                digit1 = int2.getDigit(i);
            }
            temp = (byte)(digit1 - borrow);
            diff.addDigitFirst((byte)(temp));
            borrow = 0;
        }
        //return the result
        return diff;
    }// end subtractModified() method



    // method to multiply two LargeInteger
    public LargeInteger multiply(LargeInteger secondInt) {
        LargeInteger firstInt = new LargeInteger();
        firstInt = convert(digits);
        LargeInteger myProd = new LargeInteger();
        LargeInteger zero = new LargeInteger("0");

        LargeInteger myFirstInt = new LargeInteger();
        LargeInteger mySecondInt = new LargeInteger();

        myFirstInt = copy(firstInt);
        mySecondInt = copy(secondInt);

        // if the two numbers have the same Sign
        if( (myFirstInt.getDigit(0) > 0 && mySecondInt.getDigit(0) > 0)
                || ((myFirstInt.getDigit(0) < 0 && mySecondInt.getDigit(0) < 0))){
            myProd = multiplyModified(absValue(myFirstInt) , absValue(mySecondInt));
            return myProd;
        }
        // if the two numbers have a different Sign
        else if((myFirstInt.getDigit(0) > 0 && mySecondInt.getDigit(0) < 0)
                || ((myFirstInt.getDigit(0) < 0 && mySecondInt.getDigit(0) > 0))){
            myProd = multiplyModified(absValue(myFirstInt) , absValue(mySecondInt));
            setNegative(myProd);
            return myProd;
        }
        // if at least one number equals 0
        else if(absCompareTo(myFirstInt , zero) == 0
                || absCompareTo(myFirstInt , zero) == 0){
            myProd = zero;
            return myProd;
        }
        // returns the result
        return myProd;
    }// end multiply() method

    // method to multiply two POSITIVE LargeInteger
    public LargeInteger multiplyModified(LargeInteger int1 , LargeInteger int2) {
        // The method used is the following:
        // For example, we want to multiply 12345 * 678
        // first we multiply the last digit of the second integer (8)
        // the each digit of the second integer, without forgetting to
        // propagate the carry: in the beginning; carry = 0
        // 8 * 5 + carry = 40 + 0 = 40; result = 0; carry = 4
        // 8 * 4 + carry = 32 + 4 = 36; result = 6; carry = 3 ....until the last digit
        // Finally, we will have this: 8 * 12345 = 98760 ==> store as number
        // we do the same with second digit (7); 7 * 12345 = 86415 ==> we add a 0
        // ==> 864150 ==> add it to 98760 = 962910 ==> store as number
        // ... until the last digit (6): 6 * 12345 = 74070 ==> we add two 0
        // ==> 7407000 ==> add it to 962910 = 8369910 ==> final result
        LargeInteger temporary = new LargeInteger("1");
        LargeInteger bigInt1 = new LargeInteger("1");
        byte carry = 0;
        byte digit1 = 0;
        byte digit2 = 0;
        // multiply digit by digit
        for(int j = int2.getSize() - 1; j >= 0; j--){ // main loop
            LargeInteger resultTemp = new LargeInteger();
            carry = 0;
            for(int i = int1.getSize() - 1; i >= 0 ; i--){
                digit1 = int1.getDigit(i);
                digit2 = int2.getDigit(j);
                byte b = (byte)(digit2 * digit1 + carry);

                if(b > 9){
                    carry = (byte)(b / 10);
                }
                else{
                    carry = 0;
                }

                resultTemp.addDigitFirst((byte)(b % 10));

            }
            if(carry != 0){
                resultTemp.addDigitFirst(carry);
            }

            for(int k = j ; k < int2.getSize() -1 ; k++ ){
                resultTemp.addDigitLast((byte)0);
            }
            temporary = temporary.add(resultTemp);
        }// end main loop
        temporary = subtractModified(temporary , bigInt1);
        // return the result
        return temporary;
    }// end multiplyModified() method

    // method to return the exponential of a LargeInteger
    public LargeInteger power(int exp) {
        int expInt = Math.abs(exp);
        LargeInteger temporary = convert(digits);
        LargeInteger zeroInt= new LargeInteger("0");
        int compare = absCompareTo(temporary , zeroInt);

        if(exp < 0 && compare == 0){
            //  0^( negative number) does not exist
            return null;
        }
        // for each number x != 0; x ^ 0 = 1
        LargeInteger result = new LargeInteger("1");
        if(exp != 0){
            for(int i = 0; i < expInt ; i++){
                result = temporary.multiply(result);
            }
            return result;
        }
        // return the result
        return result;
    }

    // method which return the quotient and the remainder
    // of a division of two LargeInteger
    public LargeInteger[] divide(LargeInteger secondInt) {
        // The method used is the following:
        // For example if firstInt = 12345678 and secondInt = 456
        // First, we will try to find smallest number that  divide 456, with a quotient >= 1
        // in this example, it would be 1234
        // Second, find which largest number multiplied by 456 give a result less that 1234
        // 456 * 2 = 912 < 1234  and 456 * 3 = 1368 > 1234
        // this gives a temporary quotient of 2
        // After, we subtract 912 from 1234 = 322, and we add the next digit
        // this gives us: 3225, and we do again the same process until there is no more digits
        LargeInteger quotient = new LargeInteger();
        LargeInteger remainder = new LargeInteger();
        LargeInteger[] result = new LargeInteger[2];
        LargeInteger firstInt = convert(digits);
        LargeInteger tempFirstInt = new LargeInteger();
        LargeInteger tempProduct = new LargeInteger();
        LargeInteger int1 = new LargeInteger("1");
        LargeInteger int2 = new LargeInteger("1");
        LargeInteger zeroInt = new LargeInteger("0");

        if(absCompareTo(secondInt , zeroInt) == 0){
            // Can not divide by zero
            return null;
        }

        // if the denominator is greater than the numerator ( by absolute,
        // value), the quotient will be = 0, and the remainder = numerator
        if(absCompareTo(firstInt , secondInt) == -1){
            quotient = subtractModified(int1 , int2);
            remainder = firstInt;
            result[0] = quotient;
            result[1] = remainder;
            return result;
        }
        // if the denominator and the numerator are equal, the
        // quotient will be = 1, and the remainder = 0
        else if(absCompareTo(firstInt , secondInt) == 0){
            quotient = int1;
            remainder = subtractModified(int1 , int2);
            result[0] = quotient;
            result[1] = remainder;
            return result;
        }
        // if the denominator = 1 or -1
        else if(absCompareTo(secondInt , int1) == 0){
            quotient = firstInt;
            remainder = new LargeInteger("0");
            if(secondInt.getDigit(0) < 0){
                setNegative(quotient);
            }
            result[0] = quotient;
            result[1] = remainder;
            return result;
        }


        else {	// condition5
            LargeInteger absFirstInt = absValue(firstInt);
            LargeInteger absSecondInt = absValue(secondInt);
            int firstSize = firstInt.getSize();
            int secondSize = secondInt.getSize();
            int step = 0;
            int stepIndex = 0;
            int compare =  0;
            // for example, if tempFirstInt = 123456789
            // partial(123456789 , 0 , 3) = 123
            tempFirstInt = partial(absFirstInt , step, secondSize + step );

            while(tempFirstInt.compareTo(absSecondInt) == -1
                    && (secondSize + step + stepIndex < firstSize)){
                tempFirstInt = partial(absFirstInt , step, secondSize + step +1 );
                stepIndex++;
            }

            while(absCompareTo(tempFirstInt , absSecondInt) != -1
                    && (secondSize + step + stepIndex <= firstSize)){
                int i = 1;
                do{
                    LargeInteger tempDigit = new LargeInteger(i);
                    tempProduct = tempDigit.multiply(absSecondInt);
                    compare = tempFirstInt.compareTo(tempProduct);
                    if(compare == -1 || compare == 0){
                        if(compare == 0){
                            quotient.addDigitLast((byte)(i));
                            tempDigit = new LargeInteger(i);
                        }
                        else{
                            quotient.addDigitLast((byte)(i - 1));
                            tempDigit = new LargeInteger(i - 1);
                        }
                        tempProduct = tempDigit.multiply(absSecondInt);
                        remainder = tempFirstInt.subtract(tempProduct);
                    }
                    i++;
                }while(compare == 1);

                if(secondSize + step + stepIndex < firstSize){
                    int index = 0;
                    while((absCompareTo(remainder , absSecondInt) == -1)
                            &&(secondSize + step + stepIndex <= firstSize)){
                        if(secondSize + step + stepIndex < firstSize){
                            byte b = firstInt.getDigit(secondSize + step + stepIndex);
                            remainder.addDigitLast(b);
                        }
                        if(index > 0){
                            quotient.addDigitLast((byte)(0));
                        }
                        step++;
                        index++;
                    }
                }

                tempFirstInt = remainder;
            }
        } // end condition5

        if(firstInt.getDigit(0) > 0 && secondInt.getDigit(0) < 0  ){
            if(absCompareTo(absValue(quotient) , zeroInt) != 0 ){
                setNegative(quotient);
            }
            result[0] = quotient;
            result[1] = remainder;
            // return the result
            return result;
        }
        else if(firstInt.getDigit(0) < 0 && secondInt.getDigit(0) < 0  ){
            if(absCompareTo(absValue(quotient) , zeroInt) != 0 ){
                setNegative(remainder);
            }
            result[0] = quotient;
            result[1] = remainder;
            // return the result
            return result;

        }
        else if(firstInt.getDigit(0) < 0 && secondInt.getDigit(0) > 0  ){
            if(absCompareTo(absValue(quotient) , zeroInt) != 0 ){
                setNegative(quotient);
                setNegative(remainder);
            }
            else{
                setNegative(remainder);
            }
            result[0] = quotient;
            result[1] = remainder;
            // return the result
            return result;
        }
        result[0] = quotient;
        result[1] = remainder;
        // return the result
        return result;
    }// end divide method()

    // method to compare two LargeInteger
    // returns 1 if first number is greater
    // 		   -1 if the second number is greater
    //         0 if the two numbers are equal
    public int compareTo(LargeInteger secondInt) {
        LargeInteger firstInt = new LargeInteger();
        firstInt = convert(digits);

        if(firstInt.getDigit(0) >= 0 && secondInt.getDigit(0) < 0){
            return 1;
        }
        else if(firstInt.getDigit(0) < 0 && secondInt.getDigit(0) >= 0){
            return -1;
        }
        else if(firstInt.getDigit(0) >= 0 && secondInt.getDigit(0) >= 0){
            return absCompareTo(firstInt , secondInt);
        }
        else{
            return -1 * absCompareTo(firstInt , secondInt);
        }

    }

    // Helper methods
    public boolean getSign() {return sign;}					// get the Sign
    public int getSize(){return digits.size();}				// get the number of digits
    public void addDigit(int i , byte b){digits.add(i, b);}	// append the digit b at the position i
    public void addDigit(byte b){digits.add(b);	}			// append the digit to the end
    public void addDigitLast(byte b){digits.addLast(b);}	// append the digit to the end
    public void addDigitFirst(byte b){digits.addFirst(b);}	// append the digit to the beginning
    public byte getDigit(int i){return digits.get(i);}		// return the digit at position i
    public void setDigit(int i, byte b){digits.set(i, b);}	// set the digit b at position i
    public void removeDigitFirst(){	digits.removeFirst();}	// remove the first digit

    // display method
    public void display() {
        for(int i = 0; i < digits.size(); i++){
            System.out.print(digits.get(i));
        }
    }

    // convert a LinkedList to LargeInteger
    public LargeInteger convert(LinkedList<Byte> list){
        LargeInteger largeInt = new LargeInteger();
        for(int i = 0; i < list.size(); i++){
            byte b = (byte)list.get(i);
            largeInt.addDigit(i , b);
        }
        return largeInt;
    }

    // method to create a copy of the LargeInteger
    public LargeInteger copy(LargeInteger largeinteger){
        LargeInteger temp = new LargeInteger();
        temp.addDigit(0 , (byte)(largeinteger.getDigit(0)));

        for(int i = 1; i < largeinteger.getSize(); i++){

            temp.addDigit(i , (byte)largeinteger.getDigit(i));
        }
        return temp;
    }

    // toSring() method
    public String toString(){
        String temp="";
        if(!getSign()){temp = temp + "-";}
        for(int i = 0; i < digits.size(); i++){
            temp = temp + digits.get(i);
        }
        return temp;
    }

    // method to set a number to negative
    public void setNegative(LargeInteger int1){
        int i = 0;
        while( int1.getSize() != 0 && int1.getDigit(i) == (byte)0 ){
            int1.removeDigitFirst();
            i++;
        }
        byte b = (byte)(-1 * int1.getDigit(0));

        int1.setDigit(0 , b) ;
    }

    // method to remove the first zeros from a string
    public String removeFirstZeros(String str1){
        // for example, if str1 = "00001234", the returned result would be "1234"
        String str2 = str1;
        if(str1.charAt(0) == '-'){
            str2 = str2.substring(1, str2.length());
        }
        int i = 0;
        while(str2.charAt(i) == '0' && str2.length() > 1){
            str2 = str2.substring(1, str2.length());
        }
        if(str1.charAt(0) == '-'){
            str2 = "-" + str2;
        }
        return str2;
    }

    // Method which return the absolute value of a LargeInteger
    public LargeInteger absValue(LargeInteger int1){
        LargeInteger temp = new LargeInteger();
        for(int i = 0; i < int1.getSize(); i++){
            temp.addDigit(i, (byte)Math.abs(int1.getDigit(i)));
        }
        return temp;
    }

    // Method which compare the absolute values of 2 LargeInteger
    // returns 1 if first number is greater
    // 		   -1 if the second number is greater
    //         0 if the two numbers are equal
    public int absCompareTo(LargeInteger int1 , LargeInteger int2){
        int1 = removeFirstZeros(int1);
        int2 = removeFirstZeros(int2);
        if(int1.getSize() > int2.getSize()){
            return 1;
        }
        else if(int2.getSize() > int1.getSize()){
            return -1;
        }
        else if(int1.getSize() == int2.getSize()){
            for(int i = 0; i < int1.getSize(); i++){
                if(Math.abs(int1.getDigit(i)) > Math.abs(int2.getDigit(i))){
                    return 1;
                }
                else if(Math.abs(int2.getDigit(i)) > Math.abs(int1.getDigit(i))){
                    return -1;
                }
            }
        }
        return 0;

    }

    // method to return a partial LargeInteger
    public LargeInteger partial(LargeInteger int1 ,
                                int start, int finish){
        // if int1 = 123456789, start = 3; finish = 6
        // the returned result would be: 456
        int length = finish - start;
        byte b =0;
        LargeInteger temp = new LargeInteger();
        for(int i = 0; i < length; i++){
            b = int1.getDigit(start + i);
            temp.addDigitLast(b);
        }
        return temp;
    }


    // method to remove the first zeros from a LargeInteger
    public LargeInteger removeFirstZeros(LargeInteger int1){
        // for example, if int1 = 00001234, the returned result would be 1234
        while(int1.getDigit(0) == 0 && int1.getSize() > 1){
            int1.removeDigitFirst();
        }

        return int1;
    }

}
