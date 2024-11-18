package CyLife.Events;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(value = "SELECT * FROM clubs.event e WHERE e.date >= :currentDate AND e.date <= :sevenDaysFromNow", nativeQuery = true)
    List<Event> findByDateBetween(@Param("currentDate") Date currentDate, @Param("sevenDaysFromNow") Date sevenDaysFromNow);
}
