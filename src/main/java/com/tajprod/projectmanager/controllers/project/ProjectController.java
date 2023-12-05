package com.tajprod.projectmanager.controllers.project;

import com.tajprod.projectmanager.services.project.ProjectService;
import com.tajprod.projectmanager.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class ProjectController {
  @Autowired
  private UserService userService;

  @Autowired
  private ProjectService projectService;

  //  =============== GET ROUTES ===============

  // *** DASHBOARD ***
  @GetMapping("/dashboard")
  public String dashboard(Model model, HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (!userService.isValidId(userId)) {
      return "redirect:/logout";
    }

    model.addAttribute("allProjects", projectService.allProjects());

    return "/project/dashboard.jsp";
  }

  //  =============== POST ROUTES ===============

  //  =============== UPDATE ROUTES ===============

  //  =============== DELETE ROUTES ===============
}