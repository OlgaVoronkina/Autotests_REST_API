package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataGenerator {
    public static  String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa"+timestamp+"@example.com";
    }

    public static  String getRandomWrongEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa"+timestamp+"example.com";
    }

    public static  String getRandomString(int length) {
        final String characters = "abcdefghijklmnopqrstuvwxyz";
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

    public static Map<String, String> getRegistrationData() {
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", "123");
        data.put("username", "learnqa");
        data.put("firstName", "learnqa");
        data.put("lastName", "learnqa");

        return  data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for(String key :keys){
            if(nonDefaultValues.containsKey(key)){
                userData.put(key, nonDefaultValues.get(key));
            }else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    public static Map<String, String> getRegistrationDataNotAll(String parameter) {
        Map<String, String> data = new HashMap<>();
        switch (parameter){
            case "email":
                data.put("password", "123");
                data.put("username", "learnqa");
                data.put("firstName", "learnqa");
                data.put("lastName", "learnqa");
                break;
            case "password":
                data.put("email", DataGenerator.getRandomEmail());
                data.put("username", "learnqa");
                data.put("firstName", "learnqa");
                data.put("lastName", "learnqa");
                break;
            case "username":
                data.put("email", DataGenerator.getRandomEmail());
                data.put("password", "123");
                data.put("firstName", "learnqa");
                data.put("lastName", "learnqa");
                break;
            case "firstName":
                data.put("email", DataGenerator.getRandomEmail());
                data.put("password", "123");
                data.put("username", "learnqa");
                data.put("lastName", "learnqa");
                break;
            case "lastName":
                data.put("email", DataGenerator.getRandomEmail());
                data.put("password", "123");
                data.put("username", "learnqa");
                data.put("firstName", "learnqa");
                break;
        }
        return data;
    }

}
