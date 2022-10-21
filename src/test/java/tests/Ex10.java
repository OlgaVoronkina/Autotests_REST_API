package tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10 {

    @ParameterizedTest
    @ValueSource(ints = {10, 16})
    public void checkLength(int condition){
        String randomString = generateRandomString(condition); //генерация переменной типа String с заданной длинной
        System.out.println(randomString);
        int lengthString=getLengthString(randomString); //gjkex
        System.out.println(lengthString);

        assertTrue(lengthString>15, "String length less than 15");
    }

    //вычисляет длинну строки
    public  static int getLengthString(String str){
                int lengthString = 0;
                for(int i = 0; i<str.length(); i++) {
                    lengthString++;
                }
        return lengthString;
    }

    //генерация переменной типа String с заданной длинной
    public static String generateRandomString(int length) {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        if (length <= 0) {
            throw new IllegalArgumentException("String length must be a positive integer");
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
