package org.skypro.homeworks.hogwarts1.repository;

import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByNameContainingIgnoreCase (String name);
    List<Faculty> findByColorContainsIgnoreCase(String color);
}

