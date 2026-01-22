package org.skypro.homeworks.hogwarts1.repository;

import org.skypro.homeworks.hogwarts1.model.Faculty;
import org.skypro.homeworks.hogwarts1.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int ageMin, int ageMax);

    @Query(value = "SELECT COUNT(id) FROM student", nativeQuery = true)
    long getStudentQuantity();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Optional<Double> getAvgStudentAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLast_5_StudentById();
}
