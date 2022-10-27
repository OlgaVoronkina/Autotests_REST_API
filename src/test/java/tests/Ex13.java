package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex13 {
    @ParameterizedTest
    @CsvSource({
            "'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30', Mobile, No, Android",
            "'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1', Mobile, Chrome, iOS",
            "'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)', Googlebot, Unknown, Unknown",
            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0', Web, Chrome, No",
            "'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1', Mobile, No, iPhone"
    })

    public void Ex13(String user_agent, String expectedPlatform, String expectedBrowser, String expectedDevice){
        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent", user_agent);
        Response response = RestAssured
                .given()
                .headers(headers)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();


        assertEquals(expectedPlatform, response.path("platform"),"Platform is not equal to expected value for user_agent - "+user_agent);
        assertEquals(expectedBrowser, response.path("browser"), "Browser is not equal to expected value for user_agent - "+user_agent);
        assertEquals(expectedDevice, response.path("device"), "Device is not equal to expected value for user_agent - "+user_agent);

//        if(!response.path("platform").equals(expectedPlatform)){
//            System.out.println("Platform is not equal to expected value for user_agent - "+user_agent);
//        }
//        else if (!response.path("browser").equals(expectedBrowser)){
//            System.out.println("Browser is not equal to expected value for user_agent - "+user_agent);
//        }
//        else if(!response.path("device").equals(expectedDevice)){
//            System.out.println("Device is not equal to expected value for user_agent - "+user_agent);
//        }

//        assertTrue(response.path("platform").equals(expectedPlatform), "pl");
//        assertTrue(response.getString("browser").equals(expectedBrowser));
//        assertTrue(response.getString("device").equals(expectedDevice));



//        String actualPlatform = response.getString("platform");
//        System.out.println("actualPlatform = "+actualPlatform);
//        System.out.println("expectedPlatform = "+expectedPlatform);
//
//        String actualBrowser = response.getString("browser");
//        System.out.println("actualBrowser = "+actualBrowser);
//        System.out.println("expectedBrowser = "+expectedBrowser);
//
//        String actualDevice = response.getString("device");
//        System.out.println("actualDevice = "+actualDevice);
//        System.out.println("expectedDevice = "+expectedDevice);

    }

}
