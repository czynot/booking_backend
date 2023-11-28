package com.booking.users.view;

import com.booking.App;
import com.booking.passwordHistory.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.users.Admin;
import com.booking.users.AdminRepository;
import com.booking.users.User;
import com.booking.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;


    @BeforeEach
    public void before() {
        passwordHistoryRepository.deleteAll();
        adminRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        passwordHistoryRepository.deleteAll();
        adminRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void shouldLoginSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "password"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowErrorMessageForInvalidCredentials() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFetchAdminProfileSuccessfully() throws Exception {
        User user = userRepository.save(new User("test-user", "password"));
        Admin admin = adminRepository.save(new Admin(user, "Admin name", 1));

        MvcResult result = mockMvc.perform(get("/profile")
                        .with(httpBasic("test-user", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseText = result.getResponse().getContentAsString();
        final String EXPECTED_TEXT = "{\"username\":\""+ admin.getUsername() + "\",\"name\":\"" + admin.getName() + "\",\"counter\":" + admin.getCounterNo() + "}";
        assertEquals(EXPECTED_TEXT, responseText);
    }

    @Test
    public void shouldReturnUnauthorisedWithoutCredentials() throws Exception {
        mockMvc.perform(get("/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void shouldReturnInvalidRequestOnProvidingWeakPassword() throws Exception {
        userRepository.save(new User("test-user", "password"));
        final String requestJson = "{" +
                "\"oldPassword\": \"Password@1\"," +
                "\"newPassword\": \"Password\""+
                "}";


        mockMvc.perform(put("/change-password")
                        .with(httpBasic("test-user", "password"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnInvalidRequestOnProvidingSameOldAndNewPassword() throws Exception {
        userRepository.save(new User("test-user", "password"));
        final String requestJson = "{" +
                "\"oldPassword\": \"Password@1\"," +
                "\"newPassword\": \"Password@1\""+
                "}";


        mockMvc.perform(put("/change-password")
                        .with(httpBasic("test-user", "password"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateTheNewPasswordAndSaveOldPassword() throws Exception {
        User user = userRepository.save(new User("test-user", "password"));
        adminRepository.save(new Admin(user,"test-user",1));
        final String requestJson = "{" +
                "\"oldPassword\": \"password\"," +
                "\"newPassword\": \"Password@2\""+
                "}";


        mockMvc.perform(put("/change-password")
                        .with(httpBasic("test-user", "password"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk());

        User updatedUser = userRepository.getByUsername("test-user");
        assertThat(updatedUser.getPassword(),is(equalTo("Password@2")));
        assertThat(passwordHistoryRepository.findAll().size(),is(1));
;
    }

    @Test
    public void shouldUpdateTheNewPasswordAndSaveOldPasswordAndDeleteVeryOldPassword() throws Exception {
        User user = userRepository.save(new User("test-user", "password"));
        adminRepository.save(new Admin(user,"test-user",1));
        passwordHistoryRepository.save(new PasswordHistory(user, "Password@1"));
        passwordHistoryRepository.save(new PasswordHistory(user, "Password@2"));
        passwordHistoryRepository.save(new PasswordHistory(user, "Password@3"));
        final String requestJson = "{" +
                "\"oldPassword\": \"password\"," +
                "\"newPassword\": \"Password@4\""+
                "}";


        mockMvc.perform(put("/change-password")
                        .with(httpBasic("test-user", "password"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk());

        User updatedUser = userRepository.getByUsername("test-user");
        assertThat(updatedUser.getPassword(),is(equalTo("Password@4")));
        assertThat(passwordHistoryRepository.findAll().size(),is(3));
        ;
    }



}
