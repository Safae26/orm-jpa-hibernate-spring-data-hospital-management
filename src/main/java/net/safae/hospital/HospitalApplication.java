package net.safae.hospital;

import net.safae.hospital.entities.*;
import net.safae.hospital.repository.ConsultationRepository;
import net.safae.hospital.repository.PatientRepository;
import net.safae.hospital.repository.MedecinRepository;
import net.safae.hospital.repository.RendezVousRepository;
import net.safae.hospital.service.IHospitalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class HospitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);
	}

	@Bean
	CommandLineRunner start(
            PatientRepository patientRepository,
            MedecinRepository medecinRepository,
            RendezVousRepository rendezVousRepository,
            IHospitalService iHospitalService, ConsultationRepository consultationRepository) {
		// Retourne un objet qui est toujours un composant Spring
		return args -> {
			// Opérations de gestion :

			// Ajout des patients
			Stream.of("Mohammed", "Asmae", "Israe", "Lina", "Maryam", "Tassnim")
					.forEach(name->{Patient patient = new Patient();
						patient.setNom(name);
						patient.setDateNaissance(new Date());
						patient.setMalade(false);
						//patientRepository.save(patient);
						iHospitalService.savePatient(patient);
					});

			// Ajout des médecins
            Stream.of("Ahmed", "Aymane", "Ghita")
                    .forEach(name->{Medecin medecin = new Medecin();
                        medecin.setNom(name);
						medecin.setEmail(name+"@gmail.com");
                        medecin.setSpecialite(Math.random()>0.5?"Cardio":"Neurologue");
                        //medecinRepository.save(medecin);
						iHospitalService.saveMedecin(medecin);
						});


			Patient patient = patientRepository.findById(1L).orElse(null);
			Patient patient1 = patientRepository.findByNom("Mohammed");

			Medecin medecin = medecinRepository.findByNom("Ghita");


			RendezVous rendezVous = new RendezVous();
			rendezVous.setDate(new Date());
			rendezVous.setStatus(StatusRDV.PENDING);
			rendezVous.setMedecin(medecin);
			rendezVous.setPatient(patient);
			iHospitalService.saveRDV(rendezVous);

			RendezVous rendezVous1 = rendezVousRepository.findAll().get(0);
			Consultation consultation = new Consultation();
			consultation.setDateConsultation(new Date());
			consultation.setRendezVous(rendezVous1);
			consultation.setRapport("Rapport de la consultation ...");
			iHospitalService.saveConsultation(consultation);

            // Chercher tous des patients
			List<Patient> patients = patientRepository.findAll();
			patients.forEach(
					p->{
						p.getNom();
						p.getDateNaissance();
					});

			// Chercher un patient
            patientRepository.findById(1L);

            // Mettre à jour un patient
            if (patient1 != null) {
                patient1.setNom("Mohammed Benali");
                patient1.setMalade(true);
                iHospitalService.savePatient(patient1);
            }

            // Supprimer un patient
            Patient patientToDelete = patientRepository.findByNom("Lina");
            if (patientToDelete != null) {
                System.out.println("\nSuppression du patient: " + patientToDelete.getNom());
                iHospitalService.deletePatient(patientToDelete.getId());

                // Vérification de la suppression
                if (patientRepository.findById(patientToDelete.getId()).isEmpty()) {
                    System.out.println("Patient supprimé avec succès");
                } else {
                    System.out.println("Échec de la suppression du patient");
                }
            }
		};
	}
}
