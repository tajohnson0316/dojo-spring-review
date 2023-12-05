package com.tajprod.projectmanager.services.project;

import com.tajprod.projectmanager.models.project.Project;
import com.tajprod.projectmanager.repositories.project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
  @Autowired
  private ProjectRepository projectRepository;


  // =============== CREATE METHODS ===============

  // *** CREATE ***
  public Project createProject(Project newProject) {
    // TODO: LOGIC TO INSTANTIATE List<User> team
    return projectRepository.save(newProject);
  }

  // =============== READ METHODS ===============

  // *** Find ALL ***
  public List<Project> allProjects() {
    return projectRepository.findAll();
  }

  // *** Find ONE BY ID ***
  public Project findOneById(Long id) {
    Optional<Project> optional = projectRepository.findById(id);

    return optional.orElse(null);
  }

  // =============== UPDATE METHODS ===============

  // =============== DELETE METHODS ===============

}