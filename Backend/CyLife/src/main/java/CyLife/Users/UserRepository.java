package CyLife.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//User Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
    boolean existsById(int id);
    void deleteById(int id);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
