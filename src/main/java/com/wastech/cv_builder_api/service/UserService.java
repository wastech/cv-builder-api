package com.wastech.cv_builder_api.service;
//import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.wastech.cv_builder_api.dto.UserDTO;
import com.wastech.cv_builder_api.model.Role;
import com.wastech.cv_builder_api.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public interface UserService {
    void updateUserRole(UUID userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(UUID id);

    User findByUsername(String username);

    void updateAccountLockStatus(UUID userId, boolean lock);

    List<Role> getAllRoles();

    void updateAccountExpiryStatus(UUID userId, boolean expire);

    void updateAccountEnabledStatus(UUID userId, boolean enabled);

    void updateCredentialsExpiryStatus(UUID userId, boolean expire);

    void updatePassword(UUID userId, String password);

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword);

    Optional<User> findByEmail(String email);

    User registerUser(User user);

//    GoogleAuthenticatorKey generate2FASecret(Long userId);

//    boolean validate2FACode(Long userId, int code);

    void enable2FA(UUID userId);

    void disable2FA(UUID userId);
}
