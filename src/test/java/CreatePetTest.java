import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreatePetTest {

    static long petId;

    public RequestSpecification given() {
        return RestAssured
                .given()
                .baseUri("https://petstore.swagger.io/v2")
                .log().all()
                .contentType(ContentType.JSON);

    }

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

        ValidatableResponse response = given()
                .body(body)
                //.header("ContentType", "application/json")
                .post(PetEndPoint.CREATE_PET)
                //.header("Content-Type: application/json")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("category.name", is("string"))
                .body("category.name", is(not("")))
                .log().all();

        petId = response.extract().path("id");
        //response.extract().body().jsonPath().getString("id");
        System.out.println(petId);

    }


    @Test
    public void test2GetPetById() {
        System.out.println(petId);
        given()
                .get(PetEndPoint.GET_PET, petId)
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("category.name", is(not("")))
                .log().all();

    }

    @Test
    public void test3DeletePetById() {
        System.out.println(petId);
        given()
                .delete(PetEndPoint.DELETE_PET,petId)
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .log().all();

    }

}
