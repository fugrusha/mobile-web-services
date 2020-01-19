package com.udemycourse.mobileappws.ui.model.response;

import lombok.Data;

@Data
public class UserRest {

    public String userId;

    private String firstName;

    private String lastName;

    private String email;
}
