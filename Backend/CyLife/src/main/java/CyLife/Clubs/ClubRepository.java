// Source code is decompiled from a .class file using FernFlower decompiler.
package CyLife.Clubs;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRepository extends JpaRepository<Club, Integer> {
    @Query(value = """
            SELECT clubs.user_clubs.user_id
            FROM clubs.user_clubs
            WHERE clubs.user_clubs.club_id = :clubId;
            """, nativeQuery = true)
    List<Integer> clubMembers(
            @Param("clubId") int clubId);

    @Query(value = """
            SELECT clubs.club.club_id
            FROM clubs.club
            WHERE clubs.club.club_email = :clubEmail;
            """, nativeQuery = true)
    Integer getClubId(
            @Param("clubEmail") String clubEmail);
}
