package com.tajprod.projectmanager.models.user;

import com.tajprod.projectmanager.models.project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank(message = "Required field. Please provide a first name")
  @Size(min = 2, max = 24, message = "First name must be between 2 and 24 characters")
  private String firstName;

  @NotBlank(message = "Required field. Please provide a last name")
  @Size(min = 2, max = 24, message = "Last name must be between 2 and 24 characters")
  private String lastName;

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
  private String username;

  @Column(unique = true)
  @NotBlank(message = "Email is required")
  @Email(message = "Please enter a valid email")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
  private String password;

  @Transient
  @NotBlank(message = "Password confirmation is required")
  @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
  private String confirmPassword;

  @OneToMany(mappedBy = "lead", fetch = FetchType.LAZY)
  private List<Project> leadProjects;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "users_projects",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "project_id")
  )
  private Set<Project> projects = new HashSet<>();

  public User() {

  }

  // === UTILITY METHODS ===

  public void addProject(Project project) {
    this.projects.add(project);
    project.getTeam().add(this);
  }

  public void removeProject(Project project) {
    this.projects.remove(project);
    project.getTeam().remove(this);
  }

  // === GETTERS & SETTERS ===

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public List<Project> getLeadProjects() {
    return leadProjects;
  }

  public void setLeadProjects(List<Project> leadProjects) {
    this.leadProjects = leadProjects;
  }

  public Set<Project> getProjects() {
    return projects;
  }

  public void setProjects(Set<Project> projects) {
    this.projects = projects;
  }
}