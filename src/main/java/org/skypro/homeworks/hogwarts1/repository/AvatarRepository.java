package org.skypro.homeworks.hogwarts1.repository;

import org.mockito.internal.matchers.Find;
import org.skypro.homeworks.hogwarts1.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudentId(Long studentId);
}
