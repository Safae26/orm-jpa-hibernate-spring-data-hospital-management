package net.safae.hospital.service;

import net.safae.hospital.entities.Consultation;
import net.safae.hospital.entities.Medecin;
import net.safae.hospital.entities.Patient;
import net.safae.hospital.entities.RendezVous;

public interface IHospitalService {
    Patient savePatient(Patient patient);
    Medecin saveMedecin(Medecin medecin);
    RendezVous saveRDV(RendezVous rendezVous);
    Consultation saveConsultation(Consultation consultation);

    void deletePatient(Long id);
}

