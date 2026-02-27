package com.happyplants2.plantapp;

import com.happyplants2.plantapp.model.Plant;
import com.happyplants2.plantapp.repository.PlantRepository;
import com.happyplants2.plantapp.service.UserService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Disabled
class PlantappApplicationTests {
//TODO We need to check the class MyPLantsLibraryController också, den används inte i tests just nu.
    /** Den här testklassen provar API-calls genom att använda mockmvc.
     * Transactional betyder att det görs en rollback efter testen är klara.
     * DVS ska inget sparas i databaserna.
     * 
    */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
/**
 A user shall be able to log in by entering
 their email address and password.
 */
    public void testANV01F_shouldLoginWithValidEmailAndPassword() throws Exception {
        userService.registerUser("test@test.com", "testuser", "123456");
        String loginJson = """
                {
                    "email": "test@test.com",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("test@test.com"))
                .andExpect(jsonPath("$.plants").isArray());
    }


    @Disabled("ANV02F not implemented yet")
    @Test
/**
 A user shall be able to log out of the application.
 */
    public void testANV02F_shouldLogoutUserSuccessfully() {
    }


    @Test
/**
 A user shall be able to create a new account by
 entering their email address, name, and password.
 */
    public void testANV03F_shouldCreateAccountWithValidInput() throws Exception {
        String json = """
                {
                    "email": "test@test.com",
                    "username": "testuser",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
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
    void testBIB01F_shouldAddPlantToLibrary() throws Exception {
        long before = plantRepository.count();

        mockMvc.perform(post("/api/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "TestPlant",
                                  "speciesId": "1337",
                                  "lastWatered": "2026-02-20",
                                  "waterInterval": 1,
                                  "imageUrl": ""
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("TestPlant"));

        long after = plantRepository.count();
        assertEquals(before + 1, after);
    }

    @Test
/**
 A user shall be able to view their library containing all plants,
 with a visual representation of time since last watering.
 */
    void testBIB02F_shouldDisplayLibraryWithWateringStatus() throws Exception {
        Plant plant = new Plant("LibraryPlant", "123", LocalDate.now(), 3, "");
        plantRepository.save(plant);
        mockMvc.perform(get("/api/library"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nickname").value(hasItem("LibraryPlant")));
    }


    @Test
/**
 A user shall be able to search
 for plants in their library based on plant name or nickname.
 */
    void testBIB03F_shouldSearchPlantsByNameOrNickname() throws Exception {
        Plant plant = new Plant("LibraryPlant", "123", LocalDate.now(), 3, "");
        plant = plantRepository.save(plant);

        mockMvc.perform(get("/api/library/" + plant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("LibraryPlant"));
    }


    @Test
/**
 A user shall be able to remove a plant from their library.
 */
    public void testBIB07F_shouldRemovePlantFromLibrary() throws Exception {
        Plant plant = new Plant("LibraryPlant", "123", LocalDate.now(), 3, "");
        plant = plantRepository.save(plant);

        mockMvc.perform(delete("/api/library/" + plant.getId()))
                .andExpect(status().isOk());

        assertFalse(plantRepository.existsById(plant.getId()));
    }


    @Test
/**
 A user shall be able to see information about different plants including name,
 scientific name, family name, water needs, sunlight requirements, hardiness zones,
 and care level.
 */
    public void testINF01F_shouldDisplayDetailedPlantInformation() throws Exception {
        Plant plant = new Plant(
                "MyPlant",
                "PL123",
                LocalDate.now(),
                5,
                "image.jpg"
        );

        plant = plantRepository.save(plant);

        mockMvc.perform(get("/api/library/" + plant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("MyPlant"))
                .andExpect(jsonPath("$.plantId").value("PL123"))
                .andExpect(jsonPath("$.waterFrequencyDays").value(5))
                .andExpect(jsonPath("$.imageURL").value("image.jpg"));
    }

    @Disabled("Not implemented yet")
    @Test
/**
 When a user logs in, the watering status of already added plants shall be
 updated and displayed based on the date the plant was last watered and the
 species' water needs.
 */
    public void testINF02F_shouldUpdateWateringStatusOnLogin() {
    }


    @Disabled("not implemented yet")
    @Test
/**
 User data, associated plants, email address, username, password, and
 settings shall be stored. Data shall be accessible even if the application
 has been closed or used by another user.
 */
    public void testLA01F_shouldPersistUserDataAcrossSessions() {
    }

    @Disabled("Not implemented yet")
    @Test
/**
 A user shall be able to see suggestions for different plants and search
 for plants based on plant name.
 */
    public void testSOK01F_shouldProvidePlantSuggestionsAndSearch() {
    }

    @Disabled("should probably be checked in frontend?")
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
    public void testSK02F_shouldAllowUserToMarkPlantsAsWatered() throws Exception {
        Plant plant = new Plant("LibraryPlant", "123", LocalDate.now(), 3, "");
        plant = plantRepository.save(plant);

        mockMvc.perform(put("/api/library/" + plant.getId() + "/water"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastWatered").value(LocalDate.now().toString()));
    }


}
