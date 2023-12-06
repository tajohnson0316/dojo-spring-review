package com.tajprod.projectmanager.services.project;

import com.tajprod.projectmanager.models.project.Project;
import com.tajprod.projectmanager.models.user.User;
import com.tajprod.projectmanager.repositories.project.ProjectRepository;
import com.tajprod.projectmanager.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {
  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private UserService userService;


  // =============== CREATE METHODS ===============

  // *** CREATE ***
  public Project createProject(Project project) {
    project.setTeam(new ArrayList<>());

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

  public List<Project> getUnjoinedProjects(UUID userId) {
    User user = userService.getUserById(userId);
    List<Project> unjoinedProjects = allProjects();

    // Loop through all projects and remove any that have the current user on the team
    unjoinedProjects.removeIf(project -> project.getLead().equals(user) || project.getTeam().contains(user));

    return unjoinedProjects;
  }

  // =============== UPDATE METHODS ===============

  public Project addUserToTeam(Long projectId, UUID userId) {
    User user = userService.getUserById(userId);
    Project project = getProjectById(projectId);

    project.getTeam().add(user);

    return projectRepository.save(project);
  }

  public Project removeUserFromTeam(Long projectId, UUID userId) {
    User user = userService.getUserById(userId);
    Project project = getProjectById(projectId);

    project.getTeam().remove(user);

    return projectRepository.save(project);
  }

  public Project updateProject(Project project) {
    List<User> allUsers = userService.findAll();
    List<User> usersInTeam = new ArrayList<>();

    for (User user : allUsers) {
      if (user.getProjects().contains(project)) {
        usersInTeam.add(user);
      }
    }
    project.setTeam(usersInTeam);
    return projectRepository.save(project);
  }

  // =============== DELETE METHODS ===============

}