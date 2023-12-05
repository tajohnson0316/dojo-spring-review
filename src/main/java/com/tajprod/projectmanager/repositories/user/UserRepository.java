package com.tajprod.projectmanager.repositories.user;

import com.tajprod.projectmanager.models.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
  Optional<User> findByEmail(String email);

  List<User> findAll();
}