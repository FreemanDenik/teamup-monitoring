package ru.team.up.sup.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team.up.sup.core.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);

    User getUserById(Long id);

    User findByEmail(String email);

    User findUserByName(String name);
}
