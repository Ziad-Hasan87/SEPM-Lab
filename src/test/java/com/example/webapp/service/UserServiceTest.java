package com.example.webapp.service;

import com.example.webapp.dto.RegisterDTO;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;
import com.example.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO();
        registerDTO.setUsername("teacher1");
        registerDTO.setPassword("123");
        registerDTO.setRole(Role.TEACHER);
    }

    @Test
    void testRegisterUser() {

        when(passwordEncoder.encode("123")).thenReturn("encoded123");

        userService.register(registerDTO);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("teacher1", savedUser.getUsername());
        assertEquals("encoded123", savedUser.getPassword());
        assertEquals(Role.TEACHER, savedUser.getRole());
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("teacher1");
        user.setPassword("encoded123");
        user.setRole(Role.TEACHER);

        when(userRepository.findByUsername("teacher1"))
                .thenReturn(java.util.Optional.of(user));

        var userDetails = userService.loadUserByUsername("teacher1");

        assertEquals("teacher1", userDetails.getUsername());
        assertEquals("encoded123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER")));
    }
}