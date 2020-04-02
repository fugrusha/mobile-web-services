package com.udemycourse.mobileappws.service.impl;

import com.udemycourse.mobileappws.io.entity.PasswordResetTokenEntity;
import com.udemycourse.mobileappws.io.entity.RoleEntity;
import com.udemycourse.mobileappws.io.entity.UserEntity;
import com.udemycourse.mobileappws.io.repository.PasswordResetTokenRepository;
import com.udemycourse.mobileappws.io.repository.RoleRepository;
import com.udemycourse.mobileappws.io.repository.UserRepository;
import com.udemycourse.mobileappws.security.UserPrincipal;
import com.udemycourse.mobileappws.service.UserService;
import com.udemycourse.mobileappws.shared.AmazonSES;
import com.udemycourse.mobileappws.shared.Utils;
import com.udemycourse.mobileappws.shared.dto.AddressDTO;
import com.udemycourse.mobileappws.shared.dto.UserDTO;
import com.udemycourse.mobileappws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private AmazonSES amazonSES;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UsernameNotFoundException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        for (int i = 0; i < userDTO.getAddresses().size(); i++) {
            AddressDTO addressDTO = userDTO.getAddresses().get(i);
            addressDTO.setUserDetails(userDTO);
            addressDTO.setAddressId(utils.generateAddressId(30));
            userDTO.getAddresses().set(i, addressDTO);
        }

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);

        userEntity.setEncryptedPassword(passwordEncoder.encode(userDTO.getPassword()));

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);

        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);

        // Set roles
        Collection<RoleEntity> roleEntities = new HashSet<>();

        for (String role : userDTO.getRoles()) {
            RoleEntity roleEntity = roleRepository.findByName(role);
            if (roleEntity != null ) {
                roleEntities.add(roleEntity);
            }
        }

        userEntity.setRoles(roleEntities);

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDTO returnValue = modelMapper.map(storedUserDetails, UserDTO.class);

        // send verification email
        //amazonSES.verifyEmail(returnValue);

        return returnValue;
    }

    @Override
    public UserDTO getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        UserDTO returnValue = new UserDTO();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        UserDTO returnValue = new UserDTO();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        UserDTO returnValue = new UserDTO();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());

        UserEntity updatedEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedEntity, returnValue);

        return returnValue;
    }

    @Override
    public List<UserDTO> getUsers(int page, int limit) {
        List<UserDTO> returnValue = new ArrayList<>();

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> UserEntities = usersPage.getContent();

        for (UserEntity userEntity : UserEntities) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
            returnValue.add(userDTO);
        }
        return returnValue;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean isTokenExpired = Utils.isTokenExpired(token);
            if (!isTokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userRepository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        return new UserPrincipal(userEntity);

//        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
//                userEntity.getEmailVerificationStatus(),
//                true, true, true, new ArrayList<>());

//        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public boolean requestPasswordReset(String email) {
        boolean returnValue = false;

        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            return returnValue;
        }

        String resetToken = Utils.generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity resetTokenEntity = new PasswordResetTokenEntity();
        resetTokenEntity.setToken(resetToken);
        resetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(resetTokenEntity);

        returnValue = new AmazonSES().sendPasswordResetRequest(
                userEntity.getFirstName(),
                userEntity.getEmail(),
                resetToken);

        return returnValue;
    }

    @Override
    public boolean resetPassword(String password, String token) {
        boolean returnValue = false;

        if (Utils.isTokenExpired(token)) {
            return returnValue;
        }

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }

        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return returnValue;
    }
}
