package net.safae.hospital.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import net.safae.hospital.entities.RendezVous;

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
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Collection<RendezVous> rendezVous;





}


//    - Consulter tous les patients
//    - Consulter un patient
//    - Chercher des patients
//    - Mettre à jour un patient
//    - supprimer un patient
// Migrer de H2 Database vers MySQL
// readme, screenshots