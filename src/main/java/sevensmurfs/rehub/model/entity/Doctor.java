package sevensmurfs.rehub.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uc_doctor_pin", columnNames = "pin"),
                            @UniqueConstraint(name = "uc_doctor_medical_no", columnNames = "medicalNo")})
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

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
    private String medicalNo;

    @Column(nullable = false)
    private String speciality;

    private LocalDate dateOfEmployment;

    private LocalDate dateOfResignation;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

}
