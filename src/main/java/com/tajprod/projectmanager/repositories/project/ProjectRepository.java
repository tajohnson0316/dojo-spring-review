package com.tajprod.projectmanager.repositories.project;

import com.tajprod.projectmanager.models.project.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
  @NonNull
  List<Project> findAll();
}