package com.udemycourse.mobileappws.service.impl;

import com.udemycourse.mobileappws.io.entity.UserEntity;
import com.udemycourse.mobileappws.repository.UserRepository;
import com.udemycourse.mobileappws.service.UserService;
import com.udemycourse.mobileappws.shared.Utils;
import com.udemycourse.mobileappws.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("User already exists");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);

        userEntity.setEncryptedPassword("test");

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDTO returnValue = new UserDTO();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }
}
