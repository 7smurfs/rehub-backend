package sevensmurfs.rehub.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uc_patient_pin", columnNames = "pin"),
@UniqueConstraint(name = "uc_patient_phone_number", columnNames = "phoneNumber")})
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String pin;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String gender;

    @Column(nullable = false)
    private LocalDateTime dateOfBirth;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private RehubUser user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private Set<Therapy> therapies = new HashSet<>();

}
