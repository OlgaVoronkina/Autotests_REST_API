import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex7 {
    @Test
    public void longRedirect() {
        int redirects=0;
        String url= "https://playground.learnqa.ru/api/long_redirect";
        do {
          url=this.returnHeaderLocation(url);
          redirects++;
            System.out.println("Количество редиректов = "+redirects);
        }
        while (this.returnCode(url)==301);
    }

        public String returnHeaderLocation(String url){
            System.out.println("Base URL is " +url);
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();
            url = response.getHeader("location");
            System.out.println("Redirect URL is " +url);

            return url;
        }

        public int returnCode(String url){
            int code;
            Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(url)
                .andReturn();
            code = response.getStatusCode();
            System.out.println(code);
            return code;
    }

}