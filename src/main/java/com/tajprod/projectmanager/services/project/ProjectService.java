package com.tajprod.projectmanager.services.project;

import com.tajprod.projectmanager.models.project.Project;
import com.tajprod.projectmanager.models.user.User;
import com.tajprod.projectmanager.repositories.project.ProjectRepository;
import com.tajprod.projectmanager.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private UserService userService;


  // =============== CREATE METHODS ===============

  // *** CREATE ***
  public Project createProject(Project project) {
    // On project creation, instantiate the team List and add the lead to the team
    project.addUserToTeam(project.getLead());

    return projectRepository.save(project);
  }

  // =============== READ METHODS ===============

  // *** Find ALL ***
  public List<Project> allProjects() {
    return projectRepository.findAll();
  }

  // *** Find ONE BY ID ***
  public Project getProjectById(Long id) {
    Optional<Project> optional = projectRepository.findById(id);

    return optional.orElse(null);
  }

  // =============== UPDATE METHODS ===============

  // *** ADD USER to Team ***
  public void addUserToTeam(Project project, User user) {
    project.addUserToTeam(user);

    projectRepository.save(project);
  }

  // *** REMOVE USER to Team ***
  public void removeUserFromTeam(Project project, User user) {
    project.removeUserFromTeam(user);

    projectRepository.save(project);
  }

  public void updateProject(Project project) {
    projectRepository.save(project);
  }

  // =============== DELETE METHODS ===============

  // *** Delete ONE BY ID ***
  public void deleteProjectById(Long id) {
    projectRepository.deleteById(id);
  }

}