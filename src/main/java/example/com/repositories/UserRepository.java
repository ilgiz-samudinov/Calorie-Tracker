package example.com.repositories;

import example.com.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
