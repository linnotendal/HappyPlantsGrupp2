package com.happyplants2.plantapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happyplants2.plantapp.model.Plant;
import com.happyplants2.plantapp.repository.PlantRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PlantappApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void contextLoads() {
    }

    @Disabled("ANV01F not implemented yet")
    @Test
/**
 A user shall be able to log in by entering
 their email address and password.
 */
    public void testANV01F_shouldLoginWithValidEmailAndPassword() {
    }

    @Disabled("ANV02F not implemented yet")
    @Test
/**
 A user shall be able to log out of the application.
 */
    public void testANV02F_shouldLogoutUserSuccessfully() {
    }

    @Disabled("ANV03F not implemented yet")
    @Test
/**
 A user shall be able to create a new account by
 entering their email address, name, and password.
 */
    public void testANV03F_shouldCreateAccountWithValidInput() {
    }

    @Disabled("ANV05F not implemented yet")
    @Test
/**
 A user shall receive an error message
 if they attempt to create an account without a username.
 */
    public void testANV05F_shouldRejectAccountWithoutUsername() {
    }

    @Disabled("ANV06F not implemented yet")
    @Test
/**
 A user shall receive an error message when logging in if the entered
 password or email address does not match the database records.
 */
    public void testANV06F_shouldRejectLoginWithInvalidCredentials() {
    }

    @Disabled("ANV07F not implemented yet")
    @Test
/**
 A user shall receive an error message when
 logging in if no account exists for the entered email address.
 */
    public void testANV07F_shouldRejectLoginWhenEmailDoesNotExist() {
    }

    @Test
/**
 A user shall be able to add a plant to their library.
 */
    public void testBIB01F_shouldAddPlantToLibrary() throws Exception {
        plantRepository.deleteAll();

        Plant testPlant = new Plant(
                "Planticus Maximus",
                "1337",
                LocalDate.now(),
                1,
                ""
        );

        mockMvc.perform(post("/api/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "Planticus Maximus",
                                  "speciesId": "1337",
                                  "lastWatered": "2024-05-20",
                                  "waterInterval": 1,
                                  "imageUrl": ""
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("Planticus Maximus"));
        ;


        assertEquals(1, plantRepository.count());
    }

    @Test
/**
 A user shall be able to view their library containing all plants,
 with a visual representation of time since last watering.
 */
    public void testBIB02F_shouldDisplayLibraryWithWateringStatus() {
    }

    @Test
/**
 A user shall be able to search
 for plants in their library based on plant name or nickname.
 */
    public void testBIB03F_shouldSearchPlantsByNameOrNickname() {
    }

    @Test
/**
 A user shall be able to remove a plant from their library.
 */
    public void testBIB07F_shouldRemovePlantFromLibrary() {
    }

    @Test
/**
 A user shall be able to see information about different plants including name,
 scientific name, family name, water needs, sunlight requirements, hardiness zones,
 and care level.
 */
    public void testINF01F_shouldDisplayDetailedPlantInformation() {
    }

    @Test
/**
 When a user logs in, the watering status of already added plants shall be
 updated and displayed based on the date the plant was last watered and the
 species' water needs.
 */
    public void testINF02F_shouldUpdateWateringStatusOnLogin() {
    }

    @Test
/**
 User data, associated plants, email address, username, password, and
 settings shall be stored. Data shall be accessible even if the application
 has been closed or used by another user.
 */
    public void testLA01F_shouldPersistUserDataAcrossSessions() {
    }

    @Test
/**
 A user shall be able to see suggestions for different plants and search
 for plants based on plant name.
 */
    public void testSOK01F_shouldProvidePlantSuggestionsAndSearch() {
    }

    @Test
/**
 The application shall calculate and display when a plant needs watering through a visual representation.
 */
    public void testSK01F_shouldCalculateAndDisplayWateringIndicator() {
    }

    @Test
/**
 A user shall be able to mark one or more plants as watered.
 */
    public void testSK02F_shouldAllowUserToMarkPlantsAsWatered() {
    }


}
