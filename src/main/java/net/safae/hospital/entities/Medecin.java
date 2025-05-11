package net.safae.hospital.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity @Data
@NoArgsConstructor
@AllArgsConstructor
public class Medecin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    private String specialite;

    @OneToMany(mappedBy = "medecin", fetch = FetchType.LAZY)
    private Collection<RendezVous> rendezVous;





}


// 10 min video
// Video cours
// video 1 de tp
//    - Consulter tous les patients
//    - Consulter un patient
//    - Chercher des patients
//    - Mettre à jour un patient
//    - supprimer un patient
// Migrer de H2 Database vers MySQL