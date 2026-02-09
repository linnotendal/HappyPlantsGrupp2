package com.happyplants2.plantapp.service;

import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpSession session;

    @Test
    void registerUserWithValidInput() {
        String email = "test@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        User result= userService.registerUser(email, "test", "test");
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void registerAnExistingUser() {
        String email = "test@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, "test", "test");
        });
        assertEquals("Email already exists",exception.getMessage() );
    }
    @Test
    void testLoginUserWithValidAccount() {
        String email = "test@test.com";
        User mockUser = new User(email, "test", "Correct_password");
        userService.registerUser(mockUser.getEmail(), mockUser.getUsername(), mockUser.getPassword());
        when(userRepository.findByEmail(email)).thenReturn(mockUser);
        User result = userService.loginUser(email, mockUser.getPassword());
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testLogInUserWithInvalidEmail() {
        String email = "test@test.com";
        User mockUser = new User(email, "test", "correct_password");
        when(userRepository.findByEmail(email)).thenReturn(mockUser);
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser(email, "wrong_password");
        });
        assertEquals("Wrong password",exception.getMessage() );
    }

    @Test
    void testLogInUserWithInvalidPassword() {
        String Email = "test@test.com";
        User mockUser = new User(Email, "test", "correct_password");
        when(userRepository.findByEmail(Email)).thenReturn(mockUser);
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser(Email, "wrong_password");
        });
        assertEquals("Wrong password",exception.getMessage() );
    }
    @Test
    void testLogOutUser() {
        userService.logOutUser(session);
        verify(session, times(1)).invalidate();
    }
    @Test
    void testDeleteUser() {

    }

}