package com.udemycourse.mobileappws.ui.model.request;

import lombok.Data;

@Data
public class PasswordResetModel {

    private String password;

    private String token;
}
