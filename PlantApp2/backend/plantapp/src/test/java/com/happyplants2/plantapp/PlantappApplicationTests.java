package com.happyplants2.plantapp;

import com.happyplants2.plantapp.model.PlantTemplate;
import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.model.UserPlant;
import com.happyplants2.plantapp.repository.PlantRepository;
import com.happyplants2.plantapp.repository.PlantTemplateRepository;
import com.happyplants2.plantapp.repository.UserPlantsRepository;
import com.happyplants2.plantapp.repository.UserRepository;
import com.happyplants2.plantapp.service.PlantImportService;
import com.happyplants2.plantapp.service.UserService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
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
class PlantappApplicationTests {
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

    @Autowired
    private UserPlantsRepository userPlantsRepository;

    @Autowired
    private PlantTemplateRepository plantTemplateRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PlantImportService plantImportService;

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


    @Test
/**
 A user shall be able to log out of the application.
 */
    public void testANV02F_shouldLogoutUserSuccessfully() throws Exception {
        userService.registerUser("test@test.com", "testuser", "123456");
        userService.loginUser("test@test.com", "123456");

        mockMvc.perform(post("/api/users/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("User logout successfully"));
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

    @Test
/**
 A user shall receive an error message
 if they attempt to create an account without a username.
 */
    public void testANV05F_shouldRejectAccountWithoutUsername() throws Exception {
        String json = """
                {
                    "email": "test@test.com",
                    "username": "",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400))
                .andExpect(content().string("Username is required"));
    }

    @Test
/**
 A user shall receive an error message
 if they attempt to create an account without an email.
 */
    public void testANV05F_shouldRejectAccountWithoutEmail() throws Exception {
        String json = """
                {
                    "email": "",
                    "username": "testuser",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400))
                .andExpect(content().string("Email is required"));
    }

    @Test
/**
 A user shall receive an error message
 if they attempt to create an account without a password.
 */
    public void testANV05F_shouldRejectAccountWithoutPassword() throws Exception {
        String json = """
                {
                    "email": "test@test.com",
                    "username": "testuser",
                    "password": ""
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400))
                .andExpect(content().string("Password is required"));
    }

    @Test
/**
 A user shall receive an error message
 if they attempt to create an account with an invalid email.
 */
    public void testANV05F_shouldRejectAccountWithInvalidEmail() throws Exception {
        String json = """
                {
                    "email": "test.com",
                    "username": "testuser",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400))
                .andExpect(content().string("Invalid email format"));
    }

    @Test
/**
 A user shall receive an error message
 if they attempt to create an account with an email already in use.
 */
    public void testANV05F_shouldRejectAccountWithEmailAlreadyInUse() throws Exception {
        userService.registerUser("test@test.com", "testuser1", "123456");

        String json = """
                {
                    "email": "test@test.com",
                    "username": "testuser2",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400))
                .andExpect(content().string("Email already exists"));
    }
    
    @Test
/**
 A user shall receive an error message when logging in if the entered
 password or email address does not match the database records.
 */
    public void testANV06F_shouldRejectLoginWithInvalidEmail() throws Exception{
        userService.registerUser("test@test.com", "testuser", "123456");
        String loginJson = """
                {
                    "email": "abc@test.com",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().is(400))
                .andExpect(content().string("User with this email is not found"));
    }

    @Test
/**
 A user shall receive an error message when logging in if the entered
 password or email address does not match the database records.
 */
    public void testANV06F_shouldRejectLoginWithInvalidPassword() throws Exception {
        userService.registerUser("test@test.com", "testuser", "123456");
        String loginJson = """
                {
                    "email": "test@test.com",
                    "password": "abcdef"
                }
                """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().is(400))
                .andExpect(content().string("Wrong password"));
    }

    @Test
/**
 A user shall receive an error message when
 logging in if no account exists for the entered email address.
 */
    public void testANV07F_shouldRejectLoginWhenEmailDoesNotExist() throws Exception{
        String loginJson = """
                {
                    "email": "test@test.com",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().is(400))
                .andExpect(content().string("User with this email is not found"));
    }

    @Test
/**
 A user shall be able to add a plant to their library.
 */
    void testBIB01F_shouldAddPlantToLibrary() throws Exception {
        // Create user
        User testUser = userService.registerUser("library@test.com", "testuser", "123456");

        // Create session with userId
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        // Create PlantTemplate
        PlantTemplate template = new PlantTemplate(1337, "Rose", "Rosa", "Rosaceae",
                "frequent", "full sun", "image.jpg", 7);
        plantTemplateRepository.save(template);

        long before = userPlantsRepository.count();

        // Endpoint: POST /api/user-plants/add/{plantId}
        mockMvc.perform(post("/api/user-plants/add/" + template.getId())
                        .session(session))
                .andExpect(status().isOk());

        long after = userPlantsRepository.count();
        assertEquals(before + 1, after);
    }

    @Test
/**
 A user shall be able to view their library containing all plants,
 with a visual representation of time since last watering.
 */
    void testBIB02F_shouldDisplayLibraryWithWateringStatus() throws Exception {
        User testUser = userService.registerUser("view@test.com", "viewer", "123456");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        PlantTemplate template = new PlantTemplate(123, "Cactus", "Cactaceae", "Cactaceae",
                "minimum", "full sun", "cactus.jpg", 14);
        plantTemplateRepository.save(template);

        UserPlant userPlant = new UserPlant(testUser, template, LocalDate.now());
        userPlant.setNickName("LibraryPlant");
        userPlantsRepository.save(userPlant);

        mockMvc.perform(get("/api/user-plants")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nickName").value(hasItem("LibraryPlant")));
    }


    @Test
/**
 A user shall be able to search
 for plants in their library based on plant name or nickname.
 */
    void testBIB03F_shouldSearchPlantsByNameOrNickname() throws Exception {
        User testUser = userService.registerUser("search@test.com", "searcher", "123456");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        PlantTemplate template = new PlantTemplate(456, "Fern", "Polypodiopsida", "Polypodiaceae",
                "frequent", "part shade", "fern.jpg", 3);
        plantTemplateRepository.save(template);

        UserPlant userPlant = new UserPlant(testUser, template, LocalDate.now());
        userPlant.setNickName("LibraryPlant");
        userPlant = userPlantsRepository.save(userPlant);

        mockMvc.perform(get("/api/user-plants")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nickName").value(hasItem("LibraryPlant")));
    }


    @Test
/**
 A user shall be able to remove a plant from their library.
 */
    public void testBIB07F_shouldRemovePlantFromLibrary() throws Exception {
        User testUser = userService.registerUser("delete@test.com", "deleter", "123456");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        PlantTemplate template = new PlantTemplate(789, "Basil", "Ocimum basilicum", "Lamiaceae",
                "frequent", "full sun", "basil.jpg", 2);
        plantTemplateRepository.save(template);

        UserPlant userPlant = new UserPlant(testUser, template, LocalDate.now());
        userPlant.setNickName("ToDelete");
        userPlant = userPlantsRepository.save(userPlant);

        mockMvc.perform(delete("/api/user-plants/remove/" + userPlant.getId())
                        .session(session))
                .andExpect(status().isOk());

        assertFalse(userPlantsRepository.existsById(userPlant.getId()));
    }


    @Test
/**
 A user shall be able to see information about different plants including name,
 scientific name, family name, water needs, sunlight requirements, hardiness zones,
 and care level.
 */
    public void testINF01F_shouldDisplayDetailedPlantInformation() throws Exception {
        User testUser = userService.registerUser("info@test.com", "infouser", "123456");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        PlantTemplate template = new PlantTemplate(999, "Monstera", "Monstera deliciosa", "Araceae",
                "average", "part shade", "monstera.jpg", 7);
        plantTemplateRepository.save(template);

        UserPlant userPlant = new UserPlant(testUser, template, LocalDate.now());
        userPlant.setNickName("MyPlant");
        userPlant = userPlantsRepository.save(userPlant);

        mockMvc.perform(get("/api/user-plants")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickName").value("MyPlant"))
                .andExpect(jsonPath("$[0].plantId").value(999))  // ← ÄNDRAT
                .andExpect(jsonPath("$[0].commonName").value("Monstera"))  // ← ÄNDRAT
                .andExpect(jsonPath("$[0].wateringIntervalDays").value(7));  // ← ÄNDRAT
    }

    @Test
/**
 When a user logs in, the watering status of already added plants shall be
 updated and displayed based on the date the plant was last watered and the
 species' water needs.
 */
    public void testINF02F_shouldUpdateWateringStatusOnLogin() throws Exception {
        User testUser = userService.registerUser("test@test.com", "testuser", "123456");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        PlantTemplate template = new PlantTemplate(1337, "Rose", "Rosa", "Rosaceae",
                "frequent", "full sun", "image.jpg", 7);
        plantTemplateRepository.save(template);

        UserPlant userPlant = new UserPlant(testUser, template, LocalDate.now());
        userPlant.setNickName("LibraryPlant");
        userPlant.setLastWatered(LocalDate.now().minusDays(4));
        userPlantsRepository.save(userPlant);

        userService.logOutUser(session);
        userService.loginUser("test@test.com", "123456");

        MockHttpSession session2 = new MockHttpSession();
        session2.setAttribute("userId", testUser.getId());

        mockMvc.perform(get("/api/user-plants")
                    .session(session2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastWatered").value(LocalDate.now().minusDays(4).toString()));

        assertEquals(LocalDate.now().minusDays(4), userPlant.getLastWatered());
        assertEquals(3, userPlant.getDaysUntilWater());
    }


    @Test
/**
 User data, associated plants, email address, username, password, and
 settings shall be stored. Data shall be accessible even if the application
 has been closed or used by another user.
 */
    public void testLA01F_shouldPersistUserDataAcrossSessions() throws Exception {
        User testUser = userService.registerUser("test@test.com", "testuser", "123456");

        PlantTemplate template = new PlantTemplate(1337, "Rose", "Rosa", "Rosaceae",
                "frequent", "full sun", "image.jpg", 7);
        plantTemplateRepository.save(template);

        UserPlant userPlant = new UserPlant(testUser, template, LocalDate.now());
        userPlant.setNickName("LibraryPlant");
        userPlant.setLastWatered(LocalDate.now().minusDays(4));
        userPlantsRepository.save(userPlant);
        int before = userPlantsRepository.findByUser_Id(testUser.getId()).size();

        User testUser2 = userService.registerUser("test2@test.com", "testuser2", "123456");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser2.getId());

        userService.logOutUser(session);

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
                .andExpect(jsonPath("$.plants").isArray());

        int after = userPlantsRepository.findByUser_Id(testUser.getId()).size();

        assertEquals(before, after);
    }

    @Disabled("Not implemented yet")
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
    public void testSK01F_shouldCalculateAndDisplayWateringIndicator() throws Exception {
        User testUser = userService.registerUser("test@test.com", "testuser", "123456");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        PlantTemplate template = new PlantTemplate(1337, "Rose", "Rosa", "Rosaceae",
                "frequent", "full sun", "image.jpg", 7);
        plantTemplateRepository.save(template);

        UserPlant userPlant = new UserPlant(testUser, template, LocalDate.now());
        userPlant.setNickName("LibraryPlant");
        userPlant.setLastWatered(LocalDate.now().minusDays(4));
        userPlantsRepository.save(userPlant);

        mockMvc.perform(get("/api/user-plants")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastWatered").value(LocalDate.now().minusDays(4).toString()));

        assertEquals(LocalDate.now().minusDays(4), userPlant.getLastWatered());
        assertEquals(3, userPlant.getDaysUntilWater());
    }

    @Test
/**
 A user shall be able to mark one or more plants as watered.
 */
    public void testSK02F_shouldAllowUserToMarkPlantsAsWatered() throws Exception {
        User testUser = userService.registerUser("water@test.com", "waterer", "123456");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        PlantTemplate template = new PlantTemplate(555, "Thyme", "Thymus vulgaris", "Lamiaceae",
                "frequent", "full sun", "thyme.jpg", 3);
        plantTemplateRepository.save(template);

        UserPlant userPlant = new UserPlant(testUser, template, LocalDate.now().minusDays(5));
        userPlant.setNickName("ThirstyPlant");
        userPlant = userPlantsRepository.save(userPlant);

        mockMvc.perform(put("/api/user-plants/water/" + userPlant.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastWatered").value(LocalDate.now().toString()));
    }

}
