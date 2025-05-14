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
			//ConsultationRepository consultationRepository
			IHospitalService iHospitalService) {
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
		};
	}
}
