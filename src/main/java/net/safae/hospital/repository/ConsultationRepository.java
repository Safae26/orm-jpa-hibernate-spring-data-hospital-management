package net.safae.hospital.repository;

import net.safae.hospital.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

// Interface JPA Repository basée sur Spring data
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
}
