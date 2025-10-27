package task1;

import java.util.Random;
import java.util.Scanner;

    public class Main {
        public static void main(String args[]) {
            Random random;

            Scanner scan = new Scanner(System.in);
            boolean flag = true;

            while(flag) {
                final int num = (new Random().nextInt(1000));
                int digit1 = num / 100;           // Hundreds place
                int digit2 = (num / 10) % 10;     // Tens place
                int digit3 = num % 10;            // Units place

                // Find the largest digit
                int largestDigit = digit1;
                if (digit2 > largestDigit) {
                    largestDigit = digit2;
                }
                if (digit3 > largestDigit) {
                    largestDigit = digit3;
                }

                System.out.println(num);
                System.out.println(largestDigit);
                System.out.println("wanna end?");

                if(scan.nextLine().toUpperCase().equals("YES")) {
                    flag = false;
                }
            }
        }

    }
