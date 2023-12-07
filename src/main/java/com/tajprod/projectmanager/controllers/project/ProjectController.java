package com.tajprod.projectmanager.controllers.project;

import com.tajprod.projectmanager.models.project.Project;
import com.tajprod.projectmanager.models.project.task.Task;
import com.tajprod.projectmanager.models.user.User;
import com.tajprod.projectmanager.services.project.ProjectService;
import com.tajprod.projectmanager.services.project.task.TaskService;
import com.tajprod.projectmanager.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class ProjectController {
  @Autowired
  private UserService userService;

  @Autowired
  private ProjectService projectService;

  @Autowired
  private TaskService taskService;

  //  =============== GET ROUTES ===============

  // *** DISPLAY DASHBOARD ***
  // TODO: Order the lists of projects
  @GetMapping("/dashboard")
  public String dashboard(Model model, HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userService.isNotValidId(userId)) {
      return "redirect:/logout";
    }

    User user = userService.getUserById(userId);

    model.addAttribute("username", user.getUsername());
    model.addAttribute("userId", userId);
    model.addAttribute("usersTotalProjects", user.getProjects().size());

    // Get all projects and remove any the current user is a part of
    List<Project> unjoinedProjects = projectService.allProjects();
    unjoinedProjects.removeIf(project -> user.getProjects().contains(project));
    model.addAttribute("unjoinedProjects", unjoinedProjects);
    model.addAttribute("joinedProjects", user.getProjects());

    return "/project/dashboard.jsp";
  }

  // *** DISPLAY PROJECT FORM ***
  @GetMapping("/projects/new/form")
  public String displayProjectForm(
    @ModelAttribute("project") Project project,
    HttpSession session,
    Model model
  ) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userService.isNotValidId(userId)) {
      return "redirect:/logout";
    }

    model.addAttribute("userId", userId);

    return "project/projectForm.jsp";
  }

  // *** VIEW PROJECT ***
  @GetMapping("/projects/{id}")
  public String viewProject(
    @PathVariable("id") Long id,
    HttpSession session,
    Model model
  ) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userService.isNotValidId(userId)) {
      return "redirect:/logout";
    }

    model.addAttribute("project", projectService.getProjectById(id));
    model.addAttribute("userId", userId);

    return "project/viewProject.jsp";
  }

  // *** DISPLAY EDIT FORM ***
  @GetMapping("/projects/edit/{id}")
  public String displayEditProjectForm(
    @PathVariable("id") Long id,
    HttpSession session,
    Model model
  ) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userService.isNotValidId(userId)) {
      return "redirect:/logout";
    }

    Project project = projectService.getProjectById(id);
    model.addAttribute("project", project);
    model.addAttribute("teamSize", project.getTeam().size());
    model.addAttribute("userId", userId);

    return "project/projectEditForm.jsp";
  }

  // *** VIEW TASKS ***
  // TODO: Remove or dynamically render tasks that were created by users that have left the project's team
  @GetMapping("/projects/{id}/tasks")
  public String viewTasks(
    @PathVariable("id") Long id,
    @ModelAttribute("task") Task task,
    HttpSession session,
    Model model
  ) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userService.isNotValidId(userId)) {
      return "redirect:/logout";
    }

    model.addAttribute("project", projectService.getProjectById(id));
    model.addAttribute("user", userService.getUserById(userId));

    return "/project/task/viewTasks.jsp";
  }

  //  =============== POST ROUTES ===============

  // *** CREATE NEW PROJECT ***
  @PostMapping("/projects/new/create")
  public String createProject(
    @Valid @ModelAttribute("project") Project project,
    BindingResult result,
    HttpSession session,
    Model model
  ) {
    UUID userId = (UUID) session.getAttribute("userId");

    if (result.hasErrors()) {
      model.addAttribute("project", project);
      model.addAttribute("userId", userId);

      return "project/projectForm.jsp";
    }

    Project newProject = projectService.createProject(project);

    return String.format("redirect:/projects/%d", newProject.getId());
  }

  // *** CREATE TASK ***
  @PostMapping("/projects/{projectId}/tasks/create")
  public String createTask(
    @PathVariable("projectId") Long id,
    @Valid @ModelAttribute("task") Task task,
    BindingResult result,
    HttpSession session,
    Model model
  ) {
    if (result.hasErrors()) {
      UUID userId = (UUID) session.getAttribute("userId");
      model.addAttribute("project", projectService.getProjectById(id));
      model.addAttribute("user", userService.getUserById(userId));
      model.addAttribute("task", task);

      return "project/task/viewTasks.jsp";
    }
    Project project = projectService.getProjectById(id);
    Task newTask = taskService.createTask(task);
    project.getTasks().add(newTask);
    projectService.updateProject(project);

    return String.format("redirect:/projects/%d/tasks", project.getId());
  }

  //  =============== PUT ROUTES ===============

  // *** UPDATE PROJECT ***
  @PutMapping("/projects/update/{id}")
  public String updateProject(
    @Valid @ModelAttribute("project") Project project,
    BindingResult result,
    HttpSession session,
    Model model
  ) {
    UUID userId = (UUID) session.getAttribute("userId");

    if (result.hasErrors()) {
      model.addAttribute("project", project);
      model.addAttribute("teamSize",
        projectService.getProjectById(project.getId()).getTeam().size());
      model.addAttribute("userId", userId);
      return "project/projectEditForm.jsp";
    }

    projectService.updateProject(project);

    return String.format("redirect:/projects/%d", project.getId());
  }

  // *** ADD USER TO PROJECT TEAM ***
  @PostMapping("/projects/{projectId}/join")
  public String addUserToProject(
    HttpSession session,
    @PathVariable("projectId") Long id
  ) {
    User user = userService.getUserById((UUID) session.getAttribute("userId"));
    Project project = projectService.getProjectById(id);

    projectService.addUserToTeam(project, user);

    return "redirect:/dashboard";
  }

  // *** REMOVE USER FROM PROJECT TEAM ***
  @PostMapping("/projects/{projectId}/leave")
  public String removeUserFromProject(
    HttpSession session,
    @PathVariable("projectId") Long id
  ) {
    User user = userService.getUserById((UUID) session.getAttribute("userId"));
    Project project = projectService.getProjectById(id);

    projectService.removeUserFromTeam(project, user);

    return "redirect:/dashboard";
  }

  //  =============== DELETE ROUTES ===============

  // *** DELETE PROJECT ***
  @DeleteMapping("/projects/delete/{id}")
  public String deleteProject(@PathVariable("id") Long id) {
    Project project = projectService.getProjectById(id);
    for (User user : project.getTeam()) {
      user.removeProject(project);
      userService.updateUser(user);
    }
    projectService.deleteProjectById(id);

    return "redirect:/dashboard";
  }
}