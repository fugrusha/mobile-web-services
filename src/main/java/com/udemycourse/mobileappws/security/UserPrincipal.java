package com.udemycourse.mobileappws.security;

import com.udemycourse.mobileappws.io.entity.AuthorityEntity;
import com.udemycourse.mobileappws.io.entity.RoleEntity;
import com.udemycourse.mobileappws.io.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 7205592845252317995L;

    private UserEntity userEntity;

    @Getter
    @Setter
    private String userId;

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.userId = userEntity.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<AuthorityEntity> authorityEntities = new HashSet<>();

        Collection<RoleEntity> roles = userEntity.getRoles();

        if (roles == null) {
            return authorities;
        }

        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorityEntities.addAll(role.getAuthorities()); // for each role get authorities
        });

        authorityEntities.forEach(authorityEntity -> {
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.userEntity.getEmailVerificationStatus();
    }
}
