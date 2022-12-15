package spring.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.board.domain.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}
