package CyLife.Websockets.chat;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByClubId(int clubId);

    // Updated method to use a parameterized date instead of INTERVAL
    @Query("SELECT m FROM Message m WHERE m.clubId = :clubId AND m.sent >= :startDate")
    List<Message> findRecentMessagesByClubId(@Param("clubId") int clubId, @Param("startDate") Date startDate);

}
