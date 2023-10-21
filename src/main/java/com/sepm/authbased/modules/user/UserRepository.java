package com.sepm.authbased.modules.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByEmail(String email);
    boolean existsUserById(Integer userId);
    Optional<User> findUserByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.profileImageId = ?1 WHERE u.id = ?2")
    void updateProfileImageId(String profileImageId, Integer userId);
}
