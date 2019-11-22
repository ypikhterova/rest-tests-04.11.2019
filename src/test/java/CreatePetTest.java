import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreatePetTest {

    static long petId;

    @Test
    public void test1CreatePet() {

        String body = "{\n" +
                "  \"id\": 0,\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"string\"\n" +
                "  },\n" +
                "  \"name\": \"rat\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        ValidatableResponse response = RestAssured
                .given()
                .body(body)
                //.header("ContentType", "application/json")
                .contentType(ContentType.JSON)
                .log().all()
                .post("https://petstore.swagger.io/v2/pet")
                //.header("Content-Type: application/json")
                .then()
                .statusCode(anyOf(is(200),is(201)))
                .body("category.name", is ("string"))
                .body("category.name", is (not ("")))
                .log().all();

        petId =  response.extract().path("id");
        //response.extract().body().jsonPath().getString("id");
        System.out.println(petId);

    }


    @Test
    public void test2GetPetById() {
        System.out.println(petId);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .get("https://petstore.swagger.io/v2/pet/"+petId)
                .then()
                .statusCode(anyOf(is(200),is(201)))
                .body("category.name", is (not ("")))
                .log().all();

    }

    @Test
    public void test3DeletePetById() {
        System.out.println(petId);
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .delete("https://petstore.swagger.io/v2/pet/"+petId)
                .then()
                .statusCode(anyOf(is(200),is(201)))
                .log().all();

    }

}
