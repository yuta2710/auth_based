package com.sepm.authbased.modules.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
@RequiredArgsConstructor
public class UserJpaAccessService implements UserDAO{
    private final UserRepository userRepository;
    @Override
    public List<User> selectAllUsers() {
        Page<User> page = userRepository.findAll(Pageable.ofSize(1000));
        return page.getContent();
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> selectUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existUserByGivenEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existUserByGivenId(Integer userId) {
        return userRepository.existsUserById(userId);
    }

    @Override
    public void deleteUserByGivenId(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUser(User newData) {
        userRepository.save(newData);
    }

    @Override
    public void updateCustomerProfileImageId(String profileImageId, Integer customerId) {
        userRepository.updateProfileImageId(profileImageId, customerId);
    }
}
