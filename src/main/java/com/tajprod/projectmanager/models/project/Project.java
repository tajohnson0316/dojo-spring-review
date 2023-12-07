package com.tajprod.projectmanager.models.project;

import com.tajprod.projectmanager.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank(message = "Required field. Please provide a name for the project")
  @Size(min = 2, max = 32, message = "Project name limited to 2 to 32 characters")
  private String projectName;

  @NotBlank(message = "Required field. Please provide a description of the project")
  @Size(min = 3, message = "Project description limited to a minimum of 3 characters")
  private String description;

  @NotNull(message = "Required field. Please provide a due date for the project")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @FutureOrPresent(message = "Due date must be today or some time in the future")
  private LocalDateTime dueDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User lead;

  @ManyToMany(mappedBy = "projects")
  private Set<User> team = new HashSet<>();

  @Column(updatable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdAt;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date updatedAt;

  public Project() {

  }

  // === UTILITY METHODS ===

  public void addUserToTeam(User user) {
    this.team.add(user);
    user.getProjects().add(this);
  }

  public void removeUserFromTeam(User user) {
    this.team.remove(user);
    user.getProjects().remove(this);
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = new Date();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = new Date();
  }

  // *** GETTERS & SETTERS ***

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDateTime dueDate) {
    this.dueDate = dueDate;
  }

  public User getLead() {
    return lead;
  }

  public void setLead(User lead) {
    this.lead = lead;
  }

  public Set<User> getTeam() {
    return team;
  }

  public void setTeam(Set<User> team) {
    this.team = team;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}