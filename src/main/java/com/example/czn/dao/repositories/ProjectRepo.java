package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository <Projects,Long> {
    Projects findByName(String projectName);
    List<Projects> findAllByCategory(String category);
    List<Projects> findAll();
}
