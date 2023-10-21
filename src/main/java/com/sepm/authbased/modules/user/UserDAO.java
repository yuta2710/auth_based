package com.sepm.authbased.modules.user;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> selectAllUsers();
    Optional<User> selectUserByEmail(String email);
    Optional<User> selectUserById(Integer userId);
    void insertUser(User user);
    boolean existUserByGivenEmail(String email);
    boolean existUserByGivenId(Integer userId);
    void deleteUserByGivenId(Integer userId);
    void updateUser(User newData);
    void updateCustomerProfileImageId(String profileImageId, Integer customerId);
}
