package com.udemycourse.mobileappws.io.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
public class PasswordResetTokenEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userDetails;
}
