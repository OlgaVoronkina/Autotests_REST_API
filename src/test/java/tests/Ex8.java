package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.core.IsEqual.equalTo;


public class Ex8 {
    @Test
    public void longRedirect() throws InterruptedException {
//1. создает задачу, получает token и seconds
        JsonPath createTask = RestAssured
                .get("https://playground.learnqa.ru/api/longtime_job")
                .jsonPath();
        String token = createTask.get("token");
        int seconds = createTask.get("seconds");

//2. делал один запрос с token ДО того, как задача готова, убеждался в правильности поля status
                given()
                .log().all()
                .queryParam("token", (token))
                .when()
                .get("https://playground.learnqa.ru/api/longtime_job")
                .then()
                .log().all()
                .body("status", equalTo("Job is NOT ready"));

//3. ждал нужное количество секунд с помощью функции time.sleep() - для этого надо сделать import time
        System.out.println("Тест не завис  - мы ждем");
        sleep(seconds*1000);

//4. делал бы один запрос c token ПОСЛЕ того, как задача готова, убеждался в правильности поля status и наличии поля result
                given()
                .log().all()
                .queryParam("token", (token))
                .when()
                .get("https://playground.learnqa.ru/api/longtime_job")
                .then()
                .log().all()
                .body("status", equalTo("Job is ready"))
                .body("$", hasKey("result"));

    }
}
