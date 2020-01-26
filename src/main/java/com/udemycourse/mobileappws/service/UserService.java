package com.udemycourse.mobileappws.service;

import com.udemycourse.mobileappws.shared.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO user);

    UserDTO getUser(String email);

    UserDTO getUserByUserId(String userId);

    UserDTO updateUser(String userId, UserDTO userDTO);

    void deleteUser(String userId);
}
