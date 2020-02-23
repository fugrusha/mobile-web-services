package com.udemycourse.mobileappws.shared.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private long id;
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    private UserDTO userDetails;
}
