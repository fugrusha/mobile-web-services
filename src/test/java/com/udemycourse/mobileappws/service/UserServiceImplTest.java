package com.udemycourse.mobileappws.service;

import com.udemycourse.mobileappws.io.entity.AddressEntity;
import com.udemycourse.mobileappws.io.entity.UserEntity;
import com.udemycourse.mobileappws.io.repository.UserRepository;
import com.udemycourse.mobileappws.service.impl.UserServiceImpl;
import com.udemycourse.mobileappws.shared.AmazonSES;
import com.udemycourse.mobileappws.shared.Utils;
import com.udemycourse.mobileappws.shared.dto.AddressDTO;
import com.udemycourse.mobileappws.shared.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Utils utils;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AmazonSES amazonSES;

    UserEntity userEntity;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Vova");
        userEntity.setLastName("Pupkin");
        userEntity.setEncryptedPassword("jdshgjksdg");
        userEntity.setUserId("456fsa");
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("454dsadsa");
        userEntity.setAddresses(getAddressesEntity());
    }

    @Test
    public void testGetUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO user = userService.getUser("test@test.com");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("Vova", user.getFirstName());
    }

    @Test
    public void testGetUserUsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser("test@test.com");
        });
    }

    @Test
    public void testCreateUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("352");
        when(utils.generateUserId(anyInt())).thenReturn("12345");
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDTO.class));

        UserDTO userDTO = createUserDTO();

        UserDTO result = userService.createUser(userDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userEntity.getFirstName(), result.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), result.getLastName());
        Assertions.assertNotNull(result.getUserId());

        Assertions.assertEquals(userEntity.getAddresses().size(), result.getAddresses().size());

        verify(utils, times(result.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("5445");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testCreateUserUserAlreadyExistsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO userDTO = createUserDTO();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.createUser(userDTO);
        });
    }

    private UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setEmail("test@test.ru");
        userDTO.setFirstName("Vova");
        userDTO.setLastName("Pupkin");
        userDTO.setPassword("5445");
        return userDTO;
    }

    private List<AddressDTO> getAddressesDTO() {
        AddressDTO billingDTO = new AddressDTO();
        billingDTO.setType("billing");
        billingDTO.setStreetName("Main street");
        billingDTO.setCity("Dnipro");
        billingDTO.setCountry("Ukraine");
        billingDTO.setPostalCode("45sa");

        AddressDTO shippingDTO = new AddressDTO();
        shippingDTO.setType("shipping");
        shippingDTO.setStreetName("Main street");
        shippingDTO.setCity("Dnipro");
        shippingDTO.setCountry("Ukraine");
        shippingDTO.setPostalCode("45sa");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(billingDTO);
        addresses.add(shippingDTO);
        return addresses;
    }

    private List<AddressEntity> getAddressesEntity() {
        List<AddressDTO> addresses = getAddressesDTO();

        Type listType = new TypeToken<List<AddressEntity>>() {
        }.getType();

        return new ModelMapper().map(addresses, listType);
    }
}
