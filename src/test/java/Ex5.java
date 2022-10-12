import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex5 {
    //распечатали чтобы посмотреть
    @Test
    public void getJsonHomework(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();
        response.print();
    }

    //выводим текст второго сообщения
    @Test
    public void getMessage(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String message = response.get("messages.message[1]");
        System.out.println(message);
    }
}
