package com.tajprod.projectmanager.models.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginUser {
  @NotBlank(message = "Email is required!")
  @Email(message = "Please enter a valid email")
  private String logEmail;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
  private String logPassword;

  public LoginUser() {
  }

  public String getLogEmail() {
    return logEmail;
  }

  public void setLogEmail(String logEmail) {
    this.logEmail = logEmail;
  }

  public String getLogPassword() {
    return logPassword;
  }

  public void setLogPassword(String logPassword) {
    this.logPassword = logPassword;
  }
}