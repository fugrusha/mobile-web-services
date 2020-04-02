package com.udemycourse.mobileappws.io.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
public class AuthorityEntity implements Serializable {
    private static final long serialVersionUID = 5219296471727560009L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Collection<RoleEntity> roles;

    public AuthorityEntity(String name) {
        this.name = name;
    }
}
