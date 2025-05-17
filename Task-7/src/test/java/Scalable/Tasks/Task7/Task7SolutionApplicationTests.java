package Scalable.Tasks.Task7;

import Scalable.Tasks.Task7.Repositories.UserCoreRepository1;
import Scalable.Tasks.Task7.Repositories.UserCoreRepository2;
import Scalable.Tasks.Task7.Repositories.UserCoreRepository3;
import Scalable.Tasks.Task7.Repositories.UserExtraRepository;
import Scalable.Tasks.Task7.Repositories.UserPreferencesRepository;
import Scalable.Tasks.Task7.Services.FullUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class Task7SolutionApplicationTests {
    @Value("${ID}")
    private String ID;
    @Autowired private MockMvc mockMvc;
    @Autowired private FullUserService service;
    @Autowired private UserCoreRepository1 repo1;
    @Autowired private UserCoreRepository2 repo2;
    @Autowired private UserCoreRepository3 repo3;
    @Autowired private UserExtraRepository extraRepo;
    @Autowired private UserPreferencesRepository prefsRepo;
    @Autowired private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clean() {
        // clear all tables before each test
        prefsRepo.deleteAll();
        extraRepo.deleteAll();
        jdbcTemplate.execute("TRUNCATE TABLE user_core RESTART IDENTITY CASCADE");
    }

    @Test
    void firstThousand_users_go_to_repo1_next_to_repo2() {
        int total = 1500;
        for (int i = 1; i <= total; i++) {
            service.createFullUser("user" + i, "pass" + i,
                    "First" + i, "Last" + i,
                    "u" + i + "@ex.com",
                    "0100000" + i,
                    "Addr" + i,
                    "light", true);
        }

        // Verify distribution by ID ranges since repos share the same table
        // IDs 1-1000 go to repo1
        Integer count1 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_core WHERE id <= 1000", Integer.class);
        assertThat(count1).isEqualTo(1000);
        // IDs 1001-2000 go to repo2
        Integer count2 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_core WHERE id > 1000 AND id <= 2000", Integer.class);
        assertThat(count2).isEqualTo(500);
        // IDs above 2000 go to repo3
        Integer count3 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_core WHERE id > 2000", Integer.class);
        assertThat(count3).isZero();
        // Extras and prefs still total
        assertThat(extraRepo.count()).isEqualTo(total);
        assertThat(prefsRepo.count()).isEqualTo(total);

        // extra and prefs should total 1500 across
        assertThat(extraRepo.count()).isEqualTo(total);
        assertThat(prefsRepo.count()).isEqualTo(total);
    }

    @Test
    void after_two_thousand_users_remaining_go_to_repo3() {
        int total = 2500;
        for (int i = 1; i <= total; i++) {
            service.createFullUser("u" + i, "p" + i,
                    "F" + i, "L" + i,
                    "u" + i + "@ex.com",
                    "011" + i,
                    "Addr" + i,
                    i % 2 == 0 ? "dark" : "light",
                    i % 2 == 0);
        }

        // IDs 1-1000 go to repo1
        Integer count1 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_core WHERE id <= 1000", Integer.class);
        assertThat(count1).isEqualTo(1000);
        // IDs 1001-2000 go to repo2
        Integer count2 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_core WHERE id > 1000 AND id <= 2000", Integer.class);
        assertThat(count2).isEqualTo(1000);
        // IDs above 2000 go to repo3
        Integer count3 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_core WHERE id > 2000", Integer.class);
        assertThat(count3).isEqualTo(500);
        assertThat(extraRepo.count()).isEqualTo(total);
        assertThat(prefsRepo.count()).isEqualTo(total);
    }

    @Test
    void createAndGetFullUser_withValidData_returnsExpected() throws Exception {
        mockMvc.perform(post("/"+ID)
                        .param("username", "testuser")
                        .param("password", "pass123")
                        .param("firstName", "Test")
                        .param("lastName", "User")
                        .param("email", "test@example.com")
                        .param("phone", "0123456789")
                        .param("address", "123 Main St")
                        .param("theme", "dark")
                        .param("notificationsEnabled", "true")
                )
                .andExpect(status().isOk());

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_core WHERE username = 'testuser'", Integer.class);
        assert count != null && count == 1;
    }

    @Test
    void whenGetFullUser_thenReturnsCorrectJson() throws Exception {
        // First create via params
        mockMvc.perform(post("/"+ID)
                        .param("username", "getuser")
                        .param("password", "pwd456")
                        .param("firstName", "Get")
                        .param("lastName", "User")
                        .param("email", "get@example.com")
                        .param("phone", "0987654321")
                        .param("address", "456 Elm St")
                        .param("theme", "light")
                        .param("notificationsEnabled", "false")
                )
                .andExpect(status().isOk());

        mockMvc.perform(get("/"+ID+"/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userCore.username").value("getuser"))
                .andExpect(jsonPath("$.userExtra.email").value("get@example.com"))
                .andExpect(jsonPath("$.userPreferences.theme").value("light"));
    }
}

