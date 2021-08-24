import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.common.mapper.TypeRef;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import io.restassured.response.Response;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class FirstTest {

    @Test(enabled = false)
    public void TestLogging(){
        System.out.println("\n****************************Before sending request ***************************\n");
        given().
                baseUri("https://reqres.in").
        when().
            log().all().
            get("/api/users?page=2");


        System.out.println("\n****************************After receiving responce ***************************\n");

        given().baseUri("https://reqres.in").
                when().
                get("/api/users?page=2").
                then().
                log().all();
    }

    @Test(enabled = false)
    public void CreateUSer(){
        given().
            body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}").
            baseUri("https://reqres.in").
        when().cookies("abc", "222").
            log().method().
            post("/api/users22").
        then().
            log().ifError();
    }

    @Test(enabled=false)
    public void CheckStatusCodeWithParamDemo(){
        given().
                baseUri("https://reqres.in").
                param("page", "2").
        when().
                get("/api/users").
        then().
//                log().all().
                assertThat().
                statusCode(200);
    }

    @Test(enabled=false)
    public void CheckStatusCodeDemo(){
        given().
                baseUri("https://reqres.in").
                param("page", "2").
        when().
                get("/api/users?page=2").
        then().
                log().body().
                assertThat().
                body("page", equalTo(2)).
                and().body("data[0].id", equalTo(7)).
                and().body("support.url", equalToIgnoringCase("https://reqres.IN/#support-heading")).
                and().body("support.url", startsWith("https://reqres.in")).
                and().body("support.url", endsWith("/#support-heading")).
                and().body("support.url", containsString("/#support")).
                statusCode(200);
    }

    @Test(enabled = false)
    public void DummyAPIDataExplorer(){
        given().
                baseUri("https://dummyapi.io").
                param("limit", 10).
        when().
                get("/data/v1/user/").
        then().
                log().all();
    }

    @Test
    public void SwagerAPIDemo(){

        Response response = given().
                baseUri("https://petstore.swagger.io/v2").
                param("status", "sold").
        when().
                get("/pet/findByStatus");

        List<Map<String, Object>> JSONResponseObject = response.as(new TypeRef<List<Map<String, Object>>>() {});

        System.out.println(JSONResponseObject.get(7).get("category"));
    }

}
