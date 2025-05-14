package net.safae.hospital.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVous {
    @Id
    private String id;
    private Date date;
    @Enumerated(EnumType.STRING)
    private StatusRDV status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY)
    private Medecin medecin;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @OneToOne(mappedBy = "rendezVous")
    Consultation consultation;
}
