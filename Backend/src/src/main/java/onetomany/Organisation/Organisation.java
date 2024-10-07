
package onetomany.Organisation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Organisation {

    @Id
    private String orgId;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private QualificationType qualification;

    public enum QualificationType {
        MULTICULTURAL, INTERNATIONAL
    }

    // Constructors, getters, and setters

    public Organisation() {
    }

    public Organisation(String orgId, String name, String email, QualificationType qualification) {
        this.orgId = orgId;
        this.name = name;
        this.email = email;
        this.qualification = qualification;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public QualificationType getQualification() {
        return qualification;
    }

    public void setQualification(QualificationType qualification) {
        this.qualification = qualification;
    }
}
