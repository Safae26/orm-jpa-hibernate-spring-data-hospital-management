# 🏥 Système de Gestion Hospitalière avec le framework Spring Boot et Spring Data JPA

## 📌 Objectif
Cette application permet de modéliser les principales entités d'un système hospitalier (patients, médecins, rendez-vous et consultations), ainsi que leur gestion via des services et un contrôleur REST.
## Fonctionnement clé : 
- **Gestion des entités**: Patient, Médecin, RendezVous, Consultation, StatusRDV.  
- **Operations**: D'ajout, consultation et modification des données associées à ces entités.
- **Organisation des services métiers liés aux opérations de gestion hospitalière.**
- **REST API**: Pour interagir avec le système.
- **Database**: Embedded H2 (in-memory), et après la migration vers MySQL.  

## 🧱 Structure du Projet
Le projet suit une architecture en couches typique d'une application Spring Boot :
### **📦 Packages principaux**
- **`entities`**  
  Contient les classes de domaine représentant les entités métier :  
  `Patient`, `Médecin`, `RendezVous`, `Consultation`, `StatusRDV`
- **`repositories`**  
  Interfaces JPA pour l'accès aux données :  
  `PatientRepository`, `MédecinRepository`, `RendezVousRepository`, `ConsultationRepository`
- **`service`**  
  - Interface : `IHospitalService`  
  - Implémentation : `HospitalServiceImpl`  
  (Encapsule toute la logique métier)
- **`web`**  
  Contrôleurs REST exposant les endpoints :  
  `PatientRestController`, `MédecinRestController`, etc.
- **`config`** (optionnel)  
  Fichiers de configuration Spring (si existants)
- **Point d'entrée à l'application**
`HospitalApplication` - Classe principale annotée `@SpringBootApplication`

<img width="289" alt="image" src="https://github.com/user-attachments/assets/b0fdf0db-09e9-4fde-af3b-621db0ccc597" />


## 📄 Explication détaillée des Classes

### 1. Classe `Patient`  
**Description** :  
Entité JPA représentant la table des patients dans la base de données.  

**Annotations principales** :  
- `@Entity` : Marque la classe comme persistante.  
- `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` (Lombok) : Génère automatiquement getters/setters, constructeurs, etc.  

**Attributs** :  
| Nom             | Type          | Description                                                                 |
|-----------------|---------------|-----------------------------------------------------------------------------|
| `id`            | `Long`        | Clé primaire générée automatiquement (`@GeneratedValue`).                   |
| `nom`           | `String`      | Nom complet du patient.                                                    |
| `dateNaissance` | `Date`        | Date de naissance (`@Temporal(TemporalType.DATE)`).                        |
| `malade`        | `boolean`     | Statut médical actuel.                                                     |
| `rendezVous`    | `Collection`  | Liste des rendez-vous associés (`@OneToMany(mappedBy="patient", fetch=LAZY`). |

---

### 2. Classe `Médecin`
**Description** :  
Entité représentant les médecins du système.  

**Attributs clés** :  
- `id` : Identifiant unique (`@GeneratedValue`).  
- `nom`, `email`, `spécialité` : Informations professionnelles.  
- `rendezVous` : Liste des rendez-vous (`@OneToMany(mappedBy="médecin")`).  
  - `@JsonProperty(access=WRITE_ONLY)` : Empêche la sérialisation JSON pour éviter les cycles infinis.  

---

### 3. Classe `RendezVous`  
**Relations** :  
- `@ManyToOne Patient` + `@ManyToOne Médecin`  
- `@OneToOne Consultation` (mappedBy="rendezVous")  

**Attributs** :  
| Nom       | Type         | Description                                                                 |
|-----------|--------------|-----------------------------------------------------------------------------|
| `id`      | `String`     | Identifiant unique.                                                         |
| `date`    | `LocalDateTime` | Date et heure du rendez-vous.                                              |
| `status`  | `StatusRDV`  | Statut persistant sous forme de chaîne (`@Enumerated(EnumType.STRING)`).   |

---

### 4. Classe `Consultation`  
**Relations** :  
- `@OneToOne RendezVous` (bidirectionnelle).  

**Champs** :  
- `dateConsultation` : Date effective de la consultation.  
- `rapport` : Diagnostic médical (`@Lob` pour les textes longs).  
- `@JsonProperty(WRITE_ONLY)` sur le champ `rendezVous`.  

---

### 5. Enumération `StatusRDV`  
**Valeurs** :  
- `PENDING` (En attente)  
- `CANCELLED` (Annulé)  
- `APPROVED` (Accepté)  
**Usage** :  
Persisté en base sous forme de chaîne via `@Enumerated(EnumType.STRING)` dans `RendezVous`.  

## 🛠️ Services

### Interface `IHospitalService`

L’interface `IHospitalService` définit les opérations métier principales liées à la gestion des entités médicales telles que les patients, les médecins, les rendez-vous et les consultations. Elle joue un rôle essentiel dans l’architecture de l’application en assurant une séparation claire entre la couche contrôleur (qui traite les requêtes HTTP) et la couche de persistance (repositories).

Cette interface facilite l’évolutivité, la maintenance et les tests unitaires du système en fournissant une abstraction des traitements métiers.

**Méthodes déclarées :**
- `savePatient(Patient patient)` : Enregistre un nouveau patient dans la base de données.
- `saveMedecin(Medecin medecin)` : Ajoute un médecin au système.
- `saveRDV(RendezVous rendezVous)` : Crée un rendez-vous médical. Un identifiant unique est généré automatiquement.
- `saveConsultation(Consultation consultation)` : Enregistre une consultation médicale.

Cette interface pose les fondations de la logique métier, laissant l’implémentation concrète aux classes de service.

---

### Implémentation `HospitalServiceImpl`

La classe `HospitalServiceImpl` est l’implémentation concrète de l’interface `IHospitalService`. 

**Caractéristiques :**
- Annotée avec `@Service`, elle est détectée automatiquement par Spring comme un composant métier injectable.
- L’annotation `@Transactional` garantit que chaque opération métier est exécutée dans une transaction cohérente, protégeant l’intégrité des données même en cas d’erreur.
- Les dépendances (`PatientRepository`, `MedecinRepository`, `RendezVousRepository`, `ConsultationRepository`) sont injectées via un constructeur (sans utiliser `@Autowired` directement).

**Méthodes principales :**
- `savePatient(Patient patient)` : Délègue l'enregistrement d’un patient au `PatientRepository`.
- `saveMedecin(Medecin medecin)` : Enregistre un nouveau médecin via le `MedecinRepository`.
- `saveRDV(RendezVous rendezVous)` : Génère un identifiant aléatoire (UUID) pour chaque rendez-vous avant de l’enregistrer.
- `saveConsultation(Consultation consultation)` : Persiste une nouvelle consultation médicale dans la base de données.

`HospitalServiceImpl` centralise ainsi toute la logique métier liée à la gestion des entités médicales, tout en s’appuyant sur les repositories pour la persistance. Elle constitue un exemple typique de couche service dans une application Spring Boot bien structurée.

# Application de Gestion Hospitalière - API REST Spring Boot

## 🌐 Couche Web

### Classe `PatientRestController`
La classe `PatientRestController` est un contrôleur REST qui expose les données des patients via des endpoints HTTP. Grâce à l'annotation `@RestController`, Spring reconnaît automatiquement cette classe comme un composant dédié à la gestion des requêtes web.

**Fonctionnalités principales** :
- Utilise `@Autowired` pour injecter une instance de `PatientRepository` (accès aux opérations de base de données)
- La méthode `patientList()` annotée avec `@GetMapping("/patients")` est déclenchée sur les requêtes GET vers `/patients`
- Retourne les données des patients au format JSON via `patientRepository.findAll()`

Ce contrôleur joue un rôle essentiel dans l'architecture REST en faisant le lien entre les clients (navigateurs, applications frontales) et la base de données.

### Classe Principale `HospitalApplication`
Classe d'entrée de l'application Spring Boot, annotée avec `@SpringBootApplication` pour activer :
- La configuration automatique
- Le scan des composants
- Le démarrage autonome de l'application

La méthode `main()` lance l'application via `SpringApplication.run()`.

#### Logique d'Initialisation (`CommandLineRunner`) :
La méthode `start()` (annotée `@Bean`) exécute ces opérations au démarrage :

1. **Création des Patients** :
   - Génère plusieurs patients via `IHospitalService.savePatient()`
   - Données générées dynamiquement à partir d'une liste de prénoms

2. **Création des Médecins** :
   - Crée des médecins avec des spécialités aléatoires ("Cardio" ou "Dentiste")
   - Définit nom et email via `saveMedecin()`

3. **Prise de Rendez-vous** :
   - Récupère un patient et un médecin existants (par ID ou nom)
   - Crée un `RendezVous` avec :
     - Statut : `PENDING`
     - Date courante

4. **Création de Consultation** :
   - Génère une `Consultation` liée au rendez-vous
   - Ajoute un rapport médical fictif

Cette initialisation permet de simuler un scénario clinique complet pour faciliter les tests et démonstrations.

## ⚙️ Configuration (application.properties)

Ce fichier configure les paramètres essentiels de l'application Spring Boot :

```properties
# Configuration générale
spring.application.name=Hospital
# Port d'accès
server.port=8086

# Configuration base de données H2
spring.datasource.url=jdbc:h2:mem:hospital
spring.h2.console.enabled=true
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

Explication des propriétés :

- `spring.application.name=Hospital` : définit le nom de l'application Spring Boot
- `spring.datasource.url=jdbc:h2:mem:hospital` : configuration de la base de données H2 en mémoire
- `spring.h2.console.enabled=true` : active la console web H2
- `server.port=8086` : l'application écoute sur le port 8086

## 🚀 Fonctionnalités

### Données initiales
Au lancement, l'application crée automatiquement :
- **Patients** : Mohammed, Aymane, Tassnim
- **Médecins** : Ghita, Yassmine
- **Rendez-vous** avec statut initial `PENDING`
- **Consultations** avec rapports médicaux

### Entités persistées
- Patient
- Medecin
- RendezVous
- Consultation

## 🔍 Accès à la console H2
La console H2 est accessible à :  
[http://localhost:8086/h2-console](http://localhost:8086/h2-console)

**Paramètres de connexion :**
- **JDBC URL** : `jdbc:h2:mem:hospital`
- **User Name** : `sa` (ou vide)
- **Password** : (vide)

## 📝 Conclusion
Ce projet implémente une application complète de gestion hospitalière avec :
- Modélisation des entités métier et leurs relations
- Persistance avec Spring Data JPA
- Initialisation automatique des données
- Visualisation via console H2

### Compétences développées
- Relations entre entités JPA
- Utilisation des repositories Spring
- Configuration de bases embarquées
- Développement full-stack Spring Boot

## 👩‍💻 Auteur
**Safae ERAJI**
