import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstTest {

    ResponseSpecification customResponseSpecification;

    @BeforeClass(enabled = true)
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://4ac3cd9b-bc5c-4c26-8431-3be472e9b042.mock.pstmn.io/").
                addHeader("x-mock-match-request-body", "true").
                setContentType("application/json;charset=utf-8").
                log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);

       customResponseSpecification = responseSpecBuilder.build();
    }

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
                baseUri("https://petstore.swagger.io/v2").
                param("status", "sold").
        when().
                get("/pet/findByStatus").
        then().
                log().all();
    }

    @Test(enabled = false)
    public void JSONArrayResponseTestAsListOfMap(){

        Response response = given().
                baseUri("https://petstore.swagger.io/v2").
                param("status", "sold").
        when().
                get("/pet/findByStatus");

        List<Map<String, Object>> JSONResponseObject = response.as(new TypeRef<List<Map<String, Object>>>() {});

        Assert.assertEquals(JSONResponseObject.get(2).get("id"), 33249514);
        System.out.println(JSONResponseObject.get(7).get("category"));
    }


    @Test(enabled = false)
    public void JSONArrayResponseTestAsJSONObject(){

        Response response = given().
                baseUri("https://petstore.swagger.io/v2").
                param("status", "sold").
                when().
                get("/pet/findByStatus");

        List<JsonObject> JSONArrayResponse = response.as(List.class);

        System.out.println(JSONArrayResponse.get(10));
    }

    @Test(enabled = false)
    public void JSONPathDemoTest(){
        String simpleJson = "{\n" +
                "   \"initAdaptiveTime\":{\n" +
                "      \"lastUpdate\":1629849600,\n" +
                "      \"metrics\":[\n" +
                "         {\n" +
                "            \"count\":11,\n" +
                "            \"total\":3296\n" +
                "         },\n" +
                "         {\n" +
                "            \"count\":0,\n" +
                "            \"total\":0\n" +
                "         }\n" +
                "      ]\n" +
                "   },\n" +
                "   \"clientProcessTime\":{\n" +
                "      \"lastUpdate\":1629849600,\n" +
                "      \"metrics\":[\n" +
                "         {\n" +
                "            \"count\":6,\n" +
                "            \"total\":2635\n" +
                "         },\n" +
                "         {\n" +
                "            \"count\":0,\n" +
                "            \"total\":0\n" +
                "         }\n" +
                "      ]\n" +
                "   }\n" +
                "}";

        JsonPath json = new JsonPath(simpleJson);

        String updateTIme = json.getString("initAdaptiveTime.lastUpdate");
        System.out.println(updateTIme);

        Object jsonPath = json.get("initAdaptiveTime");
        System.out.println(jsonPath);

        System.out.println((Object)json.get("$"));
        System.out.println(json.getString("$"));
        System.out.println((Object)json.get("initAdaptiveTime.metrics[1]"));
        System.out.println((Object)json.get());

        System.out.println(json.getString(""));
        int lastUpdate = json.getInt("initAdaptiveTime.lastUpdate");
        System.out.print(lastUpdate);

    }

    @Test(enabled = false)
    public void SendComplexJsonDemo(){

        HashMap<String, Object> mainHashmap = new HashMap<String, Object>();

        HashMap<String, String> usersParams = new HashMap<String, String>();
        usersParams.put("first_name", "Quynn");
        usersParams.put("last_name", "Contreras");
        usersParams.put("phone", "1-971-977-4681");
        usersParams.put("site", "1");

        HashMap<String, String> sitesNameList = new HashMap<String, String>();
        sitesNameList.put("name", "Edinburgh");

        mainHashmap.put("DT_RowId", "row_1");
        mainHashmap.put("users", usersParams);
        mainHashmap.put("sites", sitesNameList);

        given(requestSpecification).
                body(mainHashmap).
        when().post("post").
        then().
                spec(customResponseSpecification).
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void validate_post_request_payload_from_file_non_BDD_style(){
        File file = new File("src\\main\\resources\\CreateWorkspacePayload.json");

        given(requestSpecification).
                body(file).
        when().
                post("post").
        then().
                spec(customResponseSpecification).
                log().body().
                assertThat().
                statusCode(200).
                body("message", equalTo("Success"));

    }
}
