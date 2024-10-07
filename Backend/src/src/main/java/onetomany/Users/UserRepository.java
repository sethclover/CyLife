package onetomany.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
    List<User> findByOrganisation_OrgId(String orgId);
    List<User> findByClub_ClubId(int clubId);
}
