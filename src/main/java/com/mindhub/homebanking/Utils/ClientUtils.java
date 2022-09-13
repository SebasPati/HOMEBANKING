package com.mindhub.homebanking.Utils;

import java.util.List;
import java.util.stream.Collectors;

public final class ClientUtils {
    private ClientUtils(){}

    static int min =00000000;
    static int max =99999999;

    public static int GetNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static String getString() {
        int randomNumber = GetNumber(min, max);
        return String.valueOf(randomNumber);
    }

    public static String getNumber(List<String> accountNumbers) {
        int tamaño = 0;

        String numeroCuenta;

        do {
            numeroCuenta = "VIN" + getString();
            String finalNumeroCuenta = numeroCuenta;
            List<String> compare = accountNumbers.stream().filter(numero -> numero.equals(finalNumeroCuenta)).collect(Collectors.toList());
            tamaño = compare.size();
        } while (tamaño != 0);
        return numeroCuenta;
    }
}
