package com.udemycourse.mobileappws.shared.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 2745893206179650625L;
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private List<AddressDTO> addresses;
    private Collection<String> roles;
}
