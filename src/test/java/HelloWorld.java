import  io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWorld {
    @Test
    public void testHelloWorld(){
        System.out.println("Hello world");
    }

    @Test
    public void testHelloWorld2(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();

    }
}
