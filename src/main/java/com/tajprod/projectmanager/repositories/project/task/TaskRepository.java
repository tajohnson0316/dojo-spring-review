package com.tajprod.projectmanager.repositories.project.task;

import com.tajprod.projectmanager.models.project.task.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
  @NonNull
  List<Task> findAll();
}