package net.safae.hospital.web;

import net.safae.hospital.entities.Patient;
import net.safae.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class PatientRestController {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PatientRepository patientRepository;
    // Controlleur qui consulte la liste des patients
    @GetMapping("/patients")
    public List<Patient> patientList() {
        return patientRepository.findAll();
    }
}
