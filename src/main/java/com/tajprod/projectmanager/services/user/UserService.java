package com.tajprod.projectmanager.services.user;

import com.tajprod.projectmanager.models.user.LoginUser;
import com.tajprod.projectmanager.models.user.User;
import com.tajprod.projectmanager.repositories.user.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  // === USER LOGIN SERVICE ===
  public User login(LoginUser newLogin, BindingResult result) {
    // Immediately return null for any invalid form results
    if (result.hasErrors()) {
      return null;
    }

    Optional<User> user = userRepository.findByEmail(newLogin.getLogEmail());

    // If no user is found under the submitted email:
    if (user.isEmpty()) {
      result.rejectValue(
        "logEmail",
        "EMAIL-NOT-PRESENT",
        "User not found. Check the e-mail and try again or register a new user"
      );
      return null;
    }

    User foundUser = user.get();

    // If the submitted password fails validation:
    if (!BCrypt.checkpw(newLogin.getLogPassword(), foundUser.getPassword())) {
      result.rejectValue(
        "logPassword",
        "INVALID-LOGIN-PW",
        "Incorrect password. Please try again"
      );
      return null;
    }

    // If login passes validation:
    return foundUser;
  }

  // === USER REGISTRATION SERVICE ===
  public User register(User newUser, BindingResult result) {
    // Immediately return null for any invalid form results
    if (result.hasErrors()) {
      return null;
    }

    //If passwords do not match:
    if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
      result.rejectValue(
        "confirmPassword",
        "PW-MISMATCH",
        "Passwords must match"
      );
      return null;
    }

    // If there is an existing user with the submitted email:
    if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
      result.rejectValue(
        "email",
        "EMAIL-PRESENT",
        "There is already an account with this e-mail"
      );
      return null;
    }

    // If registration passes validation:
    // Use BCrypt to hash the password before saving the new user
    String hashedPassword = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
    newUser.setPassword(hashedPassword);

    return userRepository.save(newUser);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public User getUserById(UUID id) {
    Optional<User> optional = userRepository.findById(id);

    return optional.orElse(null);
  }

  public boolean isNotValidId(UUID id) {
    return getUserById(id) == null;
  }

  public User updateUser(User user) {
    if (getUserById(user.getId()) == null) {
      return null;
    }

    return userRepository.save(user);
  }
}