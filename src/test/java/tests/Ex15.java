package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class Ex15 extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    // Создание пользователя с некорректным email - без символа @
    @Test
    public  void testCreateUserWithWrongEmail(){
        String wrongEmail = DataGenerator.getRandomWrongEmail();
        Map<String, String> userData =  new HashMap<>();
        userData.put("email", wrongEmail);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePOSTRequest("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }


    //Создание пользователя без указания одного из полей - с помощью @ParameterizedTest необходимо проверить, что отсутствие любого параметра не дает зарегистрировать пользователя

@ParameterizedTest
@ValueSource (strings = {"email","password", "username", "firstName", "lastName"})
    public  void testCreateUserWithoutParameter(String parameter){
    Map<String, String> userData =DataGenerator.getRegistrationDataNotAll(parameter);

        Response responseCreateAuth = apiCoreRequests
                .makePOSTRequest("https://playground.learnqa.ru/api/user", userData);

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: "+parameter);
    }


    //Здесь 2 теста. Создание пользователя с очень коротким именем в один символ + Создание пользователя с очень длинным именем - длиннее 250 символов
    @ParameterizedTest
    @ValueSource(ints = {1, 251})
    public  void testCreateUserWithShortAndLongUsername(int usernameLength){
        String username = DataGenerator.getRandomString(usernameLength);
        System.out.println(username);

        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePOSTRequest("https://playground.learnqa.ru/api/user", userData);

        if(usernameLength==1){
            System.out.println("username: " + username);
            Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
            Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
        }else  if(usernameLength==251){
            System.out.println("username: " + username);
            Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
            Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
        }
    }
}
