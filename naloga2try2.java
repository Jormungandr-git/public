 import java.util.Scanner;


class bigInt{
    public static final char[] values = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j'};
    String number;

    bigInt(String number){
        this.number = number;
    }
    
    public char[] getCharArray(){
        return number.toCharArray();
    }

    public void multiply( bigInt number2, int base){

        int product10 = 0;
        int product = 0;
        char jedinice = '0';
        char desetice = '0';
        String result = "";
        String resultForOutput = "";
        ///array of results to be added up
        bigInt[] resultsArr = new bigInt[number2.number.length()]; 
        char[] num1 = this.getCharArray();
        char[] num2 = number2.getCharArray();

        for (int i = 0; i < number2.number.length(); i++) {
            for (int k = 1 + i; k < number2.number.length(); k++) {  /// adding the zeroes before the result
                result += 0;
            }
            for (int j = this.number.length() - 1; j >= 0; j--) {
                product10 = Character.getNumericValue(num2[i]) * Character.getNumericValue(num1[j]) + Character.getNumericValue(desetice);  // mnozenje cifara
                
                if ( ((product10 / base) > 9)  || ( (product10 % base) > 9) ) {  // if i have a,b,c...   ///// [POTENTIAL FOR A BUG, I HAVE TO SEPERATE JUST LIKE IN ADD]
                    desetice = values[product10/base];
                    jedinice = values[product10%base];
                     
                }else{/// if normal
                    product = (product10 / base ) * 10 + product10 % base; // iz base10 u base

                    jedinice = (char) (product % 10  + '0');
                    desetice = (char) (product / 10  + '0');
                }
            
                result += jedinice; // pisem jedinice
                resultForOutput += jedinice;
            }
            
            if (desetice != '0') {
                result += desetice;
                resultForOutput += desetice;
            }
            resultsArr[i] = new bigInt(this.reverseString(result));
            if (resultsArr[i].elementsSum() == 0) {  ///shortening the zeros
                resultForOutput = "0";
            }
            System.out.println(this.reverseString(resultForOutput));
            resultForOutput = "";
            result = "";
            desetice = '0';
            jedinice = '0';
        }
        ///adding up the results
        bigInt finalProduct = new bigInt("");
        for (int i = 0; i < resultsArr.length; i++) {
            finalProduct = finalProduct.add(resultsArr[i], base);
        }
        ///printing the output
        for (int i = 0; i < finalProduct.number.length(); i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.println(finalProduct.number);

    }

    public bigInt add(bigInt num2, int base){

        char[] number1 = reverseString(this.number).toCharArray();
        char[] number2 = reverseString(num2.number).toCharArray();
        
        int min = number1.length;
        if(number2.length < min)
            min = number2.length;
        int max = number1.length;
        if(number2.length > max)
            max = number2.length;
        
        String result = "";
        char jedinice = '0';
        char desetice = '0';
        int sum10 = 0;
        int sum = 0;
        for (int i = 0; i < min; i++) {
            sum10 = Character.getNumericValue(number1[i]) + Character.getNumericValue(number2[i]) +  Character.getNumericValue(desetice); //sabiram

            if ( ((sum10 / base) > 9)  && ( (sum10 % base) > 9) ) {  // if i have a,b,c...
                desetice = values[sum10/base];
                jedinice = values[sum10%base];
     
            }else if( (sum10 / base) > 9 ){
                desetice = values[sum10/base];
                sum = (sum10/base) * 10 + sum10 % base;   
                jedinice = (char) (sum % 10  + '0');
            }else if( (sum10 % base) > 9 ){
                jedinice = values[sum10%base];
                sum = (sum10/base) * 10 + sum10 % base;  
                if ((sum10 - Character.getNumericValue(jedinice) ) / base  > 0) {
                    desetice = (char)  ( Math.floor(  (sum10 - Character.getNumericValue(jedinice) ) / base) + '0') ; 
                }else{
                    desetice = '0';
                } 
                
            }else{/// if normal
                sum = (sum10/base) * 10 + sum10 % base;   //pretvaram iz base10 u base
                desetice = (char) (sum / 10  + '0');
                jedinice = (char) (sum % 10  + '0');
            }
            result += jedinice;
        }
        ///dodajem ono sto je ostalo od veceg broja (vrlo gadno)
        if (number2.length > number1.length){
            for (int i = min; i < max; i++) {
                String ostatak2 = (Integer.toString(Integer.parseInt(String.valueOf(number2[i]),base) + Character.getNumericValue(desetice), base));
                if (ostatak2.length() > 1){
                    result += Integer.valueOf(ostatak2) % 10;
                    desetice = (char) (Integer.valueOf(ostatak2) / 10  + '0');
                }else{
                    result += ostatak2;
                    desetice = '0';
                }
                
            }
        }else{
            for (int i = min; i < max; i++) {
                String ostatak2 = (Integer.toString(Integer.parseInt(String.valueOf(number1[i]),base) + Character.getNumericValue(desetice), base));
                if (ostatak2.length() > 1){
                    result += Integer.valueOf(ostatak2) % 10;
                    desetice = (char) (Integer.valueOf(ostatak2) / 10  + '0');
                }else{
                    result += ostatak2;
                    desetice = '0';
                }
                
            }
        }
        if(desetice != '0'){
            result += desetice;
        }

        String finalResult = reverseString(result);
        bigInt finalSum = new bigInt(finalResult);
        finalSum = clearFrontZeros(finalSum);
        return finalSum;
    }

    public bigInt sub(bigInt num2, int base){

        char[] number1 = reverseString(this.number).toCharArray();
        char[] number2 = reverseString(num2.number).toCharArray();
        
        int min = number1.length;
        if(number2.length < min)
            min = number2.length;
        int max = number1.length;
        if(number2.length > max)
            max = number2.length;
        
        String result = "";
        char jedinice = '0';
        char deseticePrev = '0';
        int carrier = 0;
        int sum10 = 0;
        int sum = 0;
        for (int i = 0; i < min; i++) {

            if (Character.getNumericValue(number1[i]) - Character.getNumericValue(deseticePrev)  < Character.getNumericValue( number2[i])){
                carrier++;
            }

            sum10 = Character.getNumericValue(number1[i]) + (carrier * base) - Character.getNumericValue(number2[i]) -   Character.getNumericValue(deseticePrev); //sabiram

            
            if (Character.getNumericValue(number1[i]) - Character.getNumericValue(deseticePrev)  < Character.getNumericValue( number2[i])){
                deseticePrev = '1';
            }else{
                deseticePrev = '0';
            }

            if ( ((sum10 / base) > 9)  && ( (sum10 % base) > 9) ) {  // if i have a,b,c...
                jedinice = values[sum10%base];
     
            }else if( (sum10 % base) > 9 ){
                jedinice = values[sum10%base];
                sum = (sum10/base) * 10 + sum10 % base;   
            }else{/// if normal
                sum = (sum10/base) * 10 + sum10 % base;   //pretvaram iz base10 u base
                jedinice = (char) (sum % 10  + '0');
            }
            result += jedinice;
            carrier = 0;
        }
        ///dodajem ono sto je ostalo od veceg broja (vrlo gadno)
        if (number2.length > number1.length){
            for (int i = min; i < max; i++) {
                String ostatak = Integer.toString(Integer.parseInt(String.valueOf(number2[i]),base) - Character.getNumericValue(deseticePrev), base);
                if(ostatak.equals("0") && i == max - 1){
                    //ne pisi zadnju nulu
                }else{
                    if(Integer.parseInt(ostatak,base) < 0){
                        deseticePrev = '1';
                        result += values[base - 1];
                    }else{
                        result += ostatak;
                        deseticePrev = '0';
                    }
                }
            }
        }else{
            for (int i = min; i < max; i++) {
                String ostatak = Integer.toString(Integer.parseInt(String.valueOf(number1[i]),base) - Character.getNumericValue(deseticePrev), base);
                if(ostatak.equals("0") && i == max - 1){
                    //ne pisi zadnju nulu
                }else{
                    if(Integer.parseInt(ostatak,base) < 0){
                        deseticePrev = '1';
                        result += values[base - 1];
                    }else{
                        result += ostatak;
                        deseticePrev = '0';
                    }
                }
                
            }
        }


        String finalResult = reverseString(result);
        bigInt finalSum = new bigInt(finalResult);
        finalSum = clearFrontZeros(finalSum);
        return finalSum;
    }

    public String reverseString(String string){
        char[] array = string.toCharArray();
        char[] result = new char[array.length];
        int counter = 0;
        for (int i = array.length-1; i >= 0 ; i--) {
            result[counter] = array[i];
            counter++;
        }
        String finalResult = String.valueOf(result);
        
        return finalResult;
    }

    public int elementsSum(){

        char[] number = this.getCharArray();
        int sum = 0;
        for (char c : number) {
            sum += Character.getNumericValue(c);
        }
        return sum;
    }

    public bigInt multiplySingle( bigInt number2, int base){

        if(number2.number.equals("0") || this.number.equals("0"))
            return new bigInt("0");

        int product10 = 0;
        int product = 0;
        char jedinice = '0';
        char desetice = '0';
        String result = "";
        String resultForOutput = "";
        ///array of results to be added up
        bigInt[] resultsArr = new bigInt[number2.number.length()]; 
        char[] num1 = this.getCharArray();
        char[] num2 = number2.getCharArray();

        for (int i = 0; i < number2.number.length(); i++) {
            for (int k = 1 + i; k < number2.number.length(); k++) {  /// adding the zeroes before the result
                result += 0;
            }
            for (int j = this.number.length() - 1; j >= 0; j--) {
                product10 = Character.getNumericValue(num2[i]) * Character.getNumericValue(num1[j]) + Character.getNumericValue(desetice);  // mnozenje cifara
                
                if ( ((product10 / base) > 9)  || ( (product10 % base) > 9) ) {  // if i have a,b,c...
                    desetice = values[product10/base];
                    jedinice = values[product10%base];
                     
                }else{/// if normal
                    product = (product10 / base ) * 10 + product10 % base; // iz base10 u base

                    jedinice = (char) (product % 10  + '0');
                    desetice = (char) (product / 10  + '0');
                }
                
                result += jedinice; // pisem jedinice
                resultForOutput += jedinice;
            }
            
            if (desetice != '0') {
                result += desetice;
                resultForOutput += desetice;
            }
            resultsArr[i] = new bigInt(this.reverseString(result));
            if (resultsArr[i].elementsSum() == 0) {  ///shortening the zeros
                resultForOutput = "0";
            }
            //System.out.println(this.reverseString(resultForOutput));
            resultForOutput = "";
            result = "";
            desetice = '0';
            jedinice = '0';
        }
        ///adding up the results
        bigInt finalProduct = new bigInt("");
        for (int i = 0; i < resultsArr.length; i++) {
            finalProduct = finalProduct.add(resultsArr[i], base);
        }
        finalProduct = clearFrontZeros(finalProduct);
        return finalProduct;

    }

    public bigInt multiplyDV(bigInt num2, int base){

        this.number = clearFrontZeros(this).number;
        num2 = clearFrontZeros(num2);
        System.out.print(this.number + " " + num2.number);
        System.out.println();

        if (num2.number.length() == 1 || this.number.length() == 1) {
            
            /// END OF RECURSION, RETURN UP
            bigInt finall = this.multiplySingle(num2, base);
            System.out.println(finall.number);
            return finall;
        }else{

            //// INIT
            bigInt a1 = new bigInt("");
            bigInt a0 = new bigInt("");
            bigInt b1 = new bigInt("");
            bigInt b0 = new bigInt("");


            int n = this.number.length();
            if (n < num2.number.length())
                n = num2.number.length();
            if( n % 2 != 0)
                n++;


            while(this.number.length() < n){
                this.number = "0" + this.number;
            }
            while(num2.number.length() < n){
                num2.number = "0" + num2.number;
            }
            char[] number1 = this.getCharArray();
            char[] number2 = num2.getCharArray();
            
            for (int i = 0; i < this.number.length()/2; i++) {
                a1.number = a1.number + number1[i];
            }
            for (int i = this.number.length()/2; i < this.number.length(); i++) {
                a0.number = a0.number + number1[i];
            }
            for (int i = 0; i < num2.number.length()/2; i++) {
                b1.number = b1.number + number2[i];
            }
            for (int i = num2.number.length()/2; i < num2.number.length(); i++) {
                b0.number = b0.number + number2[i];
            }

            a0 = clearFrontZeros(a0);
            a1 = clearFrontZeros(a1);
            b0 = clearFrontZeros(b0);
            b1 = clearFrontZeros(b1);

            ///RECURSIVE CALLS
            bigInt u = a0.multiplyDV(b0, base);
            bigInt v = a0.multiplyDV(b1, base);
            bigInt w = a1.multiplyDV(b0, base);
            bigInt z = a1.multiplyDV(b1, base);

            bigInt BtoN = new bigInt("1");
            for (int i = 0; i < n; i++) {
                BtoN.number = BtoN.number + "0";
            }
            bigInt BtoNhalf = new bigInt("1");
            for (int i = 0; i < Math.ceil(n/2.0); i++) {
                BtoNhalf.number = BtoNhalf.number + "0";
            }

            //// INPUT IN THE FUNCTION [END OF RECURSION]
            bigInt first = z.multiplySingle(BtoN, base);
            bigInt second = w.add(v, base);
            bigInt second2 = second.multiplySingle(BtoNhalf, base);
            bigInt third = first.add(second2, base);
            bigInt finall =  third.add(u, base);

            finall = clearFrontZeros(finall);
            System.out.println(finall.number);
            return finall;

        }
    }

    public bigInt multiplyKA(bigInt num2, int base){

        System.out.print(this.number + " " + num2.number);
        System.out.println();

        if (this.number.length() == 1 || num2.number.length() == 1) {
            
            /// END OF RECURSION, RETURN UP
            bigInt finall = this.multiplySingle(num2, base);
            System.out.println(finall.number);
            return finall;
        }else{

            //// INIT
            bigInt a1 = new bigInt("");
            bigInt a0 = new bigInt("");
            bigInt b1 = new bigInt("");
            bigInt b0 = new bigInt("");


            int n = this.number.length();
            if (n < num2.number.length())
                n = num2.number.length();
            if( n % 2 != 0)
                n++;

            while(this.number.length() < n){
                this.number = "0" + this.number;
            }
            while(num2.number.length() < n){
                num2.number = "0" + num2.number;
            }
            char[] number1 = this.getCharArray();
            char[] number2 = num2.getCharArray();
            
            for (int i = 0; i < this.number.length()/2; i++) {
                a1.number = a1.number + number1[i];
            }
            for (int i = this.number.length()/2; i < this.number.length(); i++) {
                a0.number = a0.number + number1[i];
            }
            for (int i = 0; i < num2.number.length()/2; i++) {
                b1.number = b1.number + number2[i];
            }
            for (int i = num2.number.length()/2; i < num2.number.length(); i++) {
                b0.number = b0.number + number2[i];
            }

            a0 = clearFrontZeros(a0);
            a1 = clearFrontZeros(a1);
            b0 = clearFrontZeros(b0);
            b1 = clearFrontZeros(b1);

            ///RECURSIVE CALLS
            bigInt u = a0.multiplyKA(b0, base);
            bigInt v = a1.multiplyKA(b1, base);
            bigInt w = a0.add(a1, base).multiplyKA(b0.add(b1, base), base);


            bigInt BtoN = new bigInt("1");
            for (int i = 0; i < n; i++) {
                BtoN.number = BtoN.number + "0";
            }
            bigInt BtoNhalf = new bigInt("1");
            for (int i = 0; i < Math.ceil(n/2.0); i++) {
                BtoNhalf.number = BtoNhalf.number + "0";
            }

            //// INPUT IN THE FUNCTION [END OF RECURSION]
            bigInt first = v.multiplySingle(BtoN, base);
            bigInt second =w.sub(v, base);
            bigInt second2 = second.sub(u, base);
            bigInt second3 = second2.multiplySingle(BtoNhalf, base);
            bigInt third = second3.add(u, base);
            bigInt finall  = first.add(third, base);

            finall = clearFrontZeros(finall);
            System.out.println(finall.number);
            return finall;

        }
    }

    public bigInt clearFrontZeros(bigInt number1){

        if (number1.number.length() != 1) {
            char[] numberArr = number1.getCharArray();
            boolean first = true;
            for (int i = 0; i < numberArr.length; i++) {
                if ((numberArr[i] == '0') && first) {
                    number1.number = number1.number.substring(1);
                    numberArr = number1.getCharArray();
                    numberArr = clearFrontZeros(numberArr);
                    break;
                }else{
                    first = false;
                }
            }
            String result = String.valueOf(numberArr);
            return new bigInt(result);
        }else{
            return number1;
        }
        
    }
    
    public char[] clearFrontZeros(char[] numberArr){
        if (numberArr.length != 1){
           String number1 = String.valueOf(numberArr);
            boolean first = true;
            for (int i = 0; i < numberArr.length; i++) {
                if ((numberArr[i] == '0') && first) {
                    number1 = number1.substring(1);
                    numberArr = number1.toCharArray();
                    numberArr = clearFrontZeros(numberArr);
                }else{
                    first = false;
                }
            }
            return numberArr; 
        }else{
            return numberArr;
        }
        
    }

}


public class naloga2try2 {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        String algorithm = sc.nextLine();
        int base = sc.nextInt();
        sc.nextLine();

        bigInt number1 = new bigInt(sc.nextLine());
        bigInt number2 = new bigInt(sc.nextLine());

        switch (algorithm) {
            case "os":
                number1.multiply(number2, base);
                break;
            case "dv":
                bigInt result1 = number1.multiplyDV(number2, base);
                break;
            case "ka":
                bigInt result2 = number1.multiplyKA(number2, base);
                break;
            case "ad":
                System.out.println(number1.add(number2, base).number);
                break;
            case "su":
                System.out.println(number1.sub(number2, base).number);
                break;
        
            default:
                break;
        }

    }
}