package onetomany.Organisation;

import org.springframework.data.jpa.repository.JpaRepository;

//Organisation Repository
public interface OrganisationRepository extends JpaRepository<Organisation, String> {
}