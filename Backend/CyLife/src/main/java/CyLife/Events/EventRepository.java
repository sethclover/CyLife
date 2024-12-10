package CyLife.Events;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(value = """
            SELECT e.event_id, e.club_id, e.event_name, e.description,
                   e.event_location, e.date
            FROM clubs.event e
            JOIN clubs.user_clubs uc ON e.club_id = uc.club_id
            JOIN clubs.user u ON uc.user_id = u.user_id
            WHERE u.user_id = :userId
            AND e.date >= :currentDate
            AND e.date <= :sevenDaysFromNow
            ORDER BY e.date
            """, nativeQuery = true)
    List<Event> studentsEventsThisWeek(
            @Param("userId") int userId,
            @Param("currentDate") LocalDate currentDate,
            @Param("sevenDaysFromNow") LocalDate sevenDaysFromNow);
}
