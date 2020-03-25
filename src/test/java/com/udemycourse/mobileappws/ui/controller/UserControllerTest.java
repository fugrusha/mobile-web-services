package com.udemycourse.mobileappws.ui.controller;

import com.udemycourse.mobileappws.service.impl.UserServiceImpl;
import com.udemycourse.mobileappws.shared.dto.AddressDTO;
import com.udemycourse.mobileappws.shared.dto.UserDTO;
import com.udemycourse.mobileappws.ui.model.response.UserRest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    UserDTO userDTO;

    final String USER_ID = "54654321";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userDTO = new UserDTO();
        userDTO.setUserId(USER_ID);
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setEmail("test@test.ru");
        userDTO.setFirstName("Vova");
        userDTO.setLastName("Pupkin");
        userDTO.setPassword("5445");
    }

    @Test
    public void testGetUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDTO);

        UserRest userRest = userController.getUser(USER_ID);

        Assertions.assertNotNull(userRest);
        Assertions.assertEquals(USER_ID, userRest.getUserId());
        Assertions.assertEquals(userDTO.getFirstName(), userRest.getFirstName());

        Assertions.assertEquals(userDTO.getAddresses().size(), userRest.getAddresses().size());
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
}
