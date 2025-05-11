package net.safae.hospital.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity @Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateConsultation;
    private String rapport;
    @OneToOne
    private RendezVous rendezVous;
}
