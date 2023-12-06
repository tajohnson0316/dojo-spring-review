package com.tajprod.projectmanager.controllers.project;

import com.tajprod.projectmanager.models.project.Project;
import com.tajprod.projectmanager.models.user.User;
import com.tajprod.projectmanager.services.project.ProjectService;
import com.tajprod.projectmanager.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class ProjectController {
  @Autowired
  private UserService userService;

  @Autowired
  private ProjectService projectService;

  //  =============== GET ROUTES ===============

  // *** DISPLAY DASHBOARD ***
  @GetMapping("/dashboard")
  public String dashboard(Model model, HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (!userService.isValidId(userId)) {
      return "redirect:/logout";
    }

    User user = userService.getUserById(userId);

    model.addAttribute("username", user.getUsername());
    model.addAttribute("userId", userId);
    model.addAttribute("unjoinedProjects", projectService.getUnjoinedProjects(userId));
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
    if (session.getAttribute("userId") == null) {
      return "redirect:/logout";
    }

    UUID userId = (UUID) session.getAttribute("userId");
    if (!userService.isValidId(userId)) {
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
    if (session.getAttribute("userId") == null) {
      return "redirect:/logout";
    }

    UUID userId = (UUID) session.getAttribute("userId");
    if (!userService.isValidId(userId)) {
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
    if (session.getAttribute("userId") == null) {
      return "redirect:/logout";
    }

    UUID userId = (UUID) session.getAttribute("userId");
    if (!userService.isValidId(userId)) {
      return "redirect:/logout";
    }

    Project project = projectService.getProjectById(id);
    model.addAttribute("project", project);
    model.addAttribute("userId", userId);

    return "project/projectEditForm.jsp";
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

  //  =============== PUT ROUTES ===============

  // *** ADD USER TO PROJECT TEAM ***
  @PostMapping("/projects/{projectId}/join")
  public String addUserToProject(
    HttpSession session,
    @PathVariable("projectId") Long id
  ) {
    UUID userId = (UUID) session.getAttribute("userId");
    projectService.addUserToTeam(id, userId);

    return "redirect:/dashboard";
  }

  //  =============== DELETE ROUTES ===============
}