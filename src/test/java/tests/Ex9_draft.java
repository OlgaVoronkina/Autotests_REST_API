package tests;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.*;
import java.util.stream.Stream;

public class Ex9_draft {
    @Test
    public void ex9_draft() throws IOException {
//        Map<String, String> body = new HashMap<>();
//        body.put("login", "value1");
//        body.put("password", "value2");


        //работает
//        ArrayList<String> pp = new ArrayList<>();
//        File file = new File("passwords");
//        Scanner scanner = new Scanner(file);
//        while (scanner.hasNextLine()){
//            String line = scanner.nextLine();
//            pp.add(line);
//        }
//        System.out.println(pp);

//        Path path = Paths.get("passwords");
//        Stream stream = Files.lines(path);
//        stream.forEach((System.out::println));


//        Path path = Paths.get("passwords");
//        ArrayList<String> pass = new ArrayList<>(Files.readAllLines(Paths.get("passwords")));
//        System.out.println(pass);


        //работает
//        //определяем пустой массив изначально
//        String[] text = new String[0];
//        try {
//            String str = null;
//            BufferedReader br = new BufferedReader(new FileReader("passwords"));
//            while ((str = br.readLine()) != null) {
//                //получаем новые слова
//                String[] newWords = str.split(" ");
//                //создаем расширенный массив
//                String[] result = new String[text.length + newWords.length];
//                //копируем элементы в массив
//                System.arraycopy(text, 0, result, 0, text.length);
//                System.arraycopy(newWords, 0, result, text.length, newWords.length);
//                //присваиваем результирующий массив текущему
//                text = result;
//            }
//            br.close();
//        } catch (IOException exc) {
//            System.out.println("IO error!" + exc);
//        }
//        System.out.println(Arrays.toString(text));
//        String uu = text[0];
//        System.out.println(uu);

        this.checkCookie();
    }

    public String checkCookie() {
        Map cookie = getCookie();
        System.out.println("Кука после получения в чек куки ---"+cookie);
        Response responseForCheck = RestAssured
                .given()
                .cookies(cookie)
                .when()
                .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                .andReturn();

        String answer = responseForCheck.asString();

        System.out.println("проверка куки = " + answer);
        return answer;
    }

    public Map getCookie() {
        Map<String, String> body = new HashMap<>();
        body.put("login", "super_admin");
        body.put("password", "ninja");
        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                .andReturn();

        System.out.println(response.getStatusCode());

        String responseCookies = response.getCookie("auth_cookie");
        Map<String, String> cookies = new HashMap<>();
        if (responseCookies!=null){
            cookies.put("auth_cookie", responseCookies);
        }
        System.out.println("Кука в ГЕТ - "+responseCookies);
        return cookies;
    }

}
