package com.tajprod.projectmanager.services.project.task;

import com.tajprod.projectmanager.models.project.task.Task;
import com.tajprod.projectmanager.repositories.project.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  @Autowired
  private TaskRepository taskRepository;

  // =============== CREATE METHODS ===============

  // *** CREATE ***
  public Task createTask(Task task) {
    return taskRepository.save(task);
  }
}