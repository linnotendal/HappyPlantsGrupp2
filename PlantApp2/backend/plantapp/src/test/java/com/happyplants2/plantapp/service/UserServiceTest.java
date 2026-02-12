package com.happyplants2.plantapp.service;

import com.happyplants2.plantapp.model.User;
import com.happyplants2.plantapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
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
    void registerAnExistingEmail() {
        String email = "test@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, "test", "test");
        });
        assertEquals("Email already exists",exception.getMessage() );
    }
    @Test
    void registerUserWithInvalidEmailFormat() {
        String email = "test.com";
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, "test", "test");
        });
        assertEquals("Invalid email format",exception.getMessage() );
    }
    @Test
    void registerWithBlankEmail() {
        String email = " ";
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, "test", "test");
        });
        assertEquals("Email is required",exception.getMessage() );
    }
    @Test
    void registerWithNullEmail() {
        String email = null;
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, "test", "test");
        });
        assertEquals("Email is required",exception.getMessage() );
    }

    @Test
    void registerWithNullUsername() {
        String email = "test@test.com";
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, null, "test");
        });
        assertEquals("Username is required",exception.getMessage() );
    }
    @Test
    void registerWithBlankUsername() {
        String email = "test@test.com";
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, " ", "test");
        });
        assertEquals("Username is required",exception.getMessage() );
    }
    @Test
    void registerWithNullPassword() {
        String email = "test@test.com";
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, "test", null);
        });
        assertEquals("Password is required",exception.getMessage() );
    }
    @Test
    void registerWithBlankPassword() {
        String email = "test@test.com";
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(email, "test", "  ");
        });
        assertEquals("Password is required",exception.getMessage() );
    }

    @Test
    void testLoginUserWithValidAccount() {
        String email = "test@test.com";
        User mockUser = new User(email, "test", "correct_password");
        when(userRepository.findByEmail(email)).thenReturn(mockUser);
        User result = userService.loginUser(email, mockUser.getPassword());
        assertNotNull(result);
        assertEquals(email, result.getEmail());
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
    void testLogInUserWithNotExistingEmail() {
        String Email = "test@test.com";
        when(userRepository.findByEmail(Email)).thenReturn(null);
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser(Email, "wrong_password");
        });
        assertEquals("User with this email is not found",exception.getMessage() );
    }

    @Test
    void testLogOutUser() {
        userService.logOutUser(session);
        verify(session, times(1)).invalidate();
    }
    @Test
    void testDeleteUser() {
        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        boolean result = userService.deleteUser(userId);
        assertTrue(result);
        verify(userRepository, times(1)).deleteById(userId);
    }
    @Test
    void deleteNonExistingUser() {
        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);
        Exception exception= assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(userId);
        });
        assertEquals("User with this id is not found",exception.getMessage() );
    }
}