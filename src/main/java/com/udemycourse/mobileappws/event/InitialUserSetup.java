package com.udemycourse.mobileappws.event;

import com.udemycourse.mobileappws.io.entity.AuthorityEntity;
import com.udemycourse.mobileappws.io.entity.RoleEntity;
import com.udemycourse.mobileappws.io.entity.UserEntity;
import com.udemycourse.mobileappws.io.repository.AuthorityRepository;
import com.udemycourse.mobileappws.io.repository.RoleRepository;
import com.udemycourse.mobileappws.io.repository.UserRepository;
import com.udemycourse.mobileappws.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.udemycourse.mobileappws.io.entity.RoleType.ROLE_ADMIN;
import static com.udemycourse.mobileappws.io.entity.RoleType.ROLE_USER;

@Component
public class InitialUserSetup {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        createRole(ROLE_USER.name(), List.of(readAuthority));
        RoleEntity roleAdmin = createRole(ROLE_ADMIN.name(), List.of(readAuthority, writeAuthority, deleteAuthority));

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("admin");
        adminUser.setLastName("admin");
        adminUser.setEmail("test@test.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(30));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("1234567890"));
        adminUser.setRoles(List.of(roleAdmin));
        userRepository.save(adminUser);
    }

    private AuthorityEntity createAuthority(String name) {
        AuthorityEntity authority = authorityRepository.findByName(name);

        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
        RoleEntity roleEntity = roleRepository.findByName(name);

        if (roleEntity == null) {
            roleEntity = new RoleEntity(name);
            roleEntity.setAuthorities(authorities);
            roleRepository.save(roleEntity);
        }

        return roleEntity;
    }
}
