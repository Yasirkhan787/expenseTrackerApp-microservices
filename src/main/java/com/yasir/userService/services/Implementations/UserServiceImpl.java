package com.yasir.userService.services.Implementations;

import com.yasir.userService.entities.User;
import com.yasir.userService.repositories.UserRepository;
import com.yasir.userService.requests.UserRequest;
import com.yasir.userService.services.UserService;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /*
        * Explanation
        * Function<User, User> updateUser
        * Takes a User from DB, updates fields from the request, and saves it.
        * Supplier<User> createUser
        * Creates a new User object and saves it.
        * findById().map(updateUser).orElseGet(createUser)
        * If the user exists (findById returns Optional with value), map(updateUser) updates it.
        * If the user doesn’t exist, orElseGet(createUser) creates a new one.
        * Returns the saved user
        * Either newly created or updated.

     */
    @Override
    public User createOrUpdateUser(UserRequest request) {

        // Function to update an existing user
        Function<User, User> updateUser = dbUser -> {
            dbUser.setFullName(request.getFullName());
            dbUser.setEmail(request.getEmail());
            dbUser.setPhoneNumber(request.getPhoneNumber());
            return userRepository.save(dbUser);
        };

        // Supplier to create a new user
        Supplier<User> createUser = () -> {
            User user = User.builder()
                    .id(request.getId())
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .build();
            return userRepository.save(user);
        };

        // Find user by ID, update if exists, else create
        return userRepository.findById(request.getId())
                .map(updateUser)       // If found, update
                .orElseGet(createUser); // If not found, create
    }

}
