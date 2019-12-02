import data.Pet;
import data.Status;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;

@RunWith(SerenityRunner.class)
public class PetTest {

    //private PetEndPoint petEndpoint = new PetEndPoint();

    @Steps
    private PetEndPoint petEndpoint;

    private Pet pet = new Pet (0, "string", "rat", Status.pending);

    private static long petId;

    @Before
    public void beforeMethod(){

        ValidatableResponse response = petEndpoint
                .createPet(pet)
                .statusCode(is(200))
                .body("category.name", is(not("")));
        petId = response.extract().body().path("id");
    }

    @Test
    public void createPet () {

        ValidatableResponse response =
                petEndpoint
                        .createPet(pet)
                        .statusCode(is(200))
                        .body("category.name", is(not("")));
    }

    @Test
    public void getPetById(){
        petEndpoint
                .getPet(petId)
                .statusCode(is(200))
                .body("category.name", is(not("")));
    }

    @Test
    public void deletePetById(){
        petEndpoint
                .deletePet(petId)
                .statusCode(is(200));

        petEndpoint
                .getPet(petId)
                .statusCode(is(404))
                .body("message", is("Pet not found"));
    }

    @Test
    public void getPetByStatus(){
        petEndpoint
                .getPetByStatus(Status.pending)
                .statusCode(200)
                .body("status[]", everyItem(is(Status.pending.toString())));

    }

    @Test
    public void updatePet(){
        Pet updatedPet = new Pet (petId, "pets", "rat", Status.pending);

        petEndpoint
                .updatePet(updatedPet)
                .statusCode(200)
                .body("category.name", is("pets"));
    }

    @Test
    public void updatePetById () {
        petEndpoint
                .updatePetById(petId, "dr. rat", "available")
                .statusCode(200);

        petEndpoint
                .getPet(petId)
                .statusCode(200)
                .body("name", is("dr. rat"))
                .body("status", is("available"));
    }

    @Test
    public void uploadPetImage(){
                petEndpoint
                .uploadPetImage(petId, "gray-semi-bold-decorative-rat-stock-photography_csp2501250.jpg")
                .statusCode(200)
                .body("message", containsString("uploaded to"));
    }
}
