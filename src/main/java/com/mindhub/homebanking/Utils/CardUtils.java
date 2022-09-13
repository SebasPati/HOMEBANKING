package com.mindhub.homebanking.Utils;

public final class CardUtils {
    private CardUtils(){}

    static int min2 = 1000;
    static int max2 = 9999;


    static int min1 = 100;
    static int max1 = 999;

    public static int GetRandomNumber(int min2, int max2) {
        return (int) ((Math.random() * (max2 - min2)) + min2);
    }

    public static String getString() {
        int randomCardNumber = GetRandomNumber(min2, max2);
        return String.valueOf(randomCardNumber);
    }
    public static String getStrings() {
        String cardNumber = "";
        for (int i = 0; i < 4; i++) {
            String serie = getString();
            cardNumber += ("-" + serie);
        }
        return cardNumber.substring(1);
    }
    public static int getCvv() {
        return (int) ((Math.random() * (max1 - min1)) + min1);
    }
}
