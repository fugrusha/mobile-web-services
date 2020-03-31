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

    @Query(value = "select * from users u where u.first_name = ?1",
            nativeQuery = true)
    List<UserEntity> findUserByFirstName(String firstName);

    @Query(value = "select * from users u where u.last_name = :lastName",
            nativeQuery = true)
    List<UserEntity> findUserByLastName(String lastName);

    @Query(value = "select * from users u where u.first_name = %:keyword% or u.last_name = %:keyword%",
            nativeQuery = true)
    List<UserEntity> findUserByKeyword(String keyword);

    @Query(value = "select u.first_name, u.last_name from users u"
            + " where u.first_name = %:keyword% or u.last_name = %:keyword%",
            nativeQuery = true)
    List<Object[]> findUserFirstNameAndLastNameByKeyword(String keyword);

    @Transactional
    @Modifying
    @Query(value = "update users u set u.EMAIL_VERIFICATION_STATUS = :status where u.user_id = :userId",
            nativeQuery = true)
    void updateUserVerificationStatus(boolean status, String userId);

    @Query("select u from UserEntity u where u.userId = :userId")
    UserEntity findUserEntityByUserId(String userId);

    @Query("select u.firstName, u.lastName from UserEntity u where u.userId = :userId ")
    List<Object[]> findUserFullName(String userId);
}
