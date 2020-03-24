package com.udemycourse.mobileappws.io.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "password_reset_tokens")
@Getter
@Setter
public class PasswordResetTokenEntity implements Serializable {
    private static final long serialVersionUID = 14425641439945009L;

    @Id
    @GeneratedValue
    private long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userDetails;
}
