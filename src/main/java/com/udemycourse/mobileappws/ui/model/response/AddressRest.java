package com.udemycourse.mobileappws.ui.model.response;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class AddressRest extends RepresentationModel<AddressRest> {
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
