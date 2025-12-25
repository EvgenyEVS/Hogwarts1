package org.skypro.homeworks.hogwarts1.repository;

import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository <Student, Long> {
    List<Student> findByNameContainingIgnoreCase (String name);
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int ageMin, int ageMax);
}
