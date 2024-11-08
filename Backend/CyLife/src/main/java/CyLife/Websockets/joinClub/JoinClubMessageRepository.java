package CyLife.Websockets.joinClub;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JoinClubMessageRepository extends JpaRepository<JoinClubMessage, Long> {
    List<JoinClubMessage> findByClubId(String clubId);
}
