package com.udemycourse.mobileappws.io.repository;

import com.udemycourse.mobileappws.io.entity.AddressEntity;
import com.udemycourse.mobileappws.io.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    static boolean recordsCreated = false;

    @BeforeEach
    void setUp() throws Exception {
        if (!recordsCreated) {
            createRecords();
        }
    }

    @Test
    public void testGetVerifiedUsers() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<UserEntity> userEntityPage = userRepository.findAllUsersWithConfirmedEmail(pageRequest);

        assertNotNull(userEntityPage);

        List<UserEntity> users = userEntityPage.getContent();
        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    public void testFindUserByFirstName() {
        String firstName = "Vova";

        List<UserEntity> users = userRepository.findUserByFirstName(firstName);

        assertNotNull(users);
        assertEquals(1, users.size());

        UserEntity user1 = users.get(0);
        assertEquals(firstName, user1.getFirstName());
    }

    @Test
    public void testFindUserByLastName() {
        String lastName = "Pupkin";

        List<UserEntity> users = userRepository.findUserByLastName(lastName);

        assertNotNull(users);
        assertEquals(1, users.size());

        UserEntity user1 = users.get(0);
        assertEquals(lastName, user1.getLastName());
    }

    @Test
    public void testFindUserByKeyword() {
        String keyword = "Vo";

        List<UserEntity> users = userRepository.findUserByKeyword(keyword);

        assertNotNull(users);
        assertEquals(1, users.size());

        UserEntity user1 = users.get(0);
        assertTrue(user1.getFirstName().contains(keyword) ||
                user1.getLastName().contains(keyword));
    }

    @Test
    public void testFindUserFirstNameAndLastNameByKeyword() {
        String keyword = "Vo";

        List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);

        assertNotNull(users);
        assertEquals(1, users.size());

        Object[] user1 = users.get(0);
        String firstName = String.valueOf(user1[0]);
        String lastName = String.valueOf(user1[1]);

        assertNotNull(firstName);
        assertNotNull(lastName);
    }

    @Test
    public void testUpdateUserEmailVerificationStatus() {
        boolean newStatus = false;

        userRepository.updateUserVerificationStatus(newStatus, "456fsa");

        UserEntity userEntity = userRepository.findByUserId("456fsa");

        boolean storedVerificationStatus = userEntity.getEmailVerificationStatus();

        assertEquals(storedVerificationStatus, newStatus);
    }

    private void createRecords() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Vova");
        userEntity.setLastName("Pupkin");
        userEntity.setEncryptedPassword("jdshgjksdg");
        userEntity.setUserId("456fsa");
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationStatus(true);


        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setType("billing");
        addressEntity.setStreetName("Main street");
        addressEntity.setCity("Dnipro");
        addressEntity.setCountry("Ukraine");
        addressEntity.setPostalCode("45sa");

        userEntity.setAddresses(List.of(addressEntity));
        userRepository.save(userEntity);

        recordsCreated = true;
    }
}
