package com.udemycourse.mobileappws.io.repository;

import com.udemycourse.mobileappws.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findUserByEmailVerificationToken(String token);

    @Query(value = "select * from USERS u where u.EMAIL_VERIFICATION_STATUS = 'true'",
            countQuery = "select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
            nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmail(Pageable pageable);

    @Query(value = "select * from USERS u where u.firstName = ?1",
            nativeQuery = true)
    List<UserEntity> findUserByFirstName(String firstName);

    @Query(value = "select * from USERS u where u.lastName = :lastName",
            nativeQuery = true)
    List<UserEntity> findUserByLastName(String lastName);

    @Query(value = "select * from USERS u where u.firstName = %:keyword% or u.lastName = %:keyword%",
            nativeQuery = true)
    List<UserEntity> findUserByKeyword(String keyword);

    @Query(value = "select u.firstName, u.lastName from USERS u"
            + " where u.firstName = %:keyword% or u.lastName = %:keyword%",
            nativeQuery = true)
    List<Object[]> findUserFirstNameAndLastNameByKeyword(String keyword);

    @Transactional
    @Modifying
    @Query(value = "update USERS u set u.EMAIL_VERIFICATION_STATUS = :status where u.userId = :userId",
            nativeQuery = true)
    void updateUserVerificationStatus(boolean status, String userId);
}
