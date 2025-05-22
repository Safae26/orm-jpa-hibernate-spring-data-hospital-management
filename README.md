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

<img width="453" alt="image" src="https://github.com/user-attachments/assets/c77f7afc-ddd7-4c71-871e-7d32a9948b22" />


---

### 2. Classe `Médecin`
**Description** :  
Entité représentant les médecins du système.  

**Attributs clés** :  
- `id` : Identifiant unique (`@GeneratedValue`).  
- `nom`, `email`, `spécialité` : Informations professionnelles.  
- `rendezVous` : Liste des rendez-vous (`@OneToMany(mappedBy="médecin")`).  
  - `@JsonProperty(access=WRITE_ONLY)` : Empêche la sérialisation JSON pour éviter les cycles infinis.

<img width="525" alt="image" src="https://github.com/user-attachments/assets/59e3a3cb-c8ff-4b11-a53e-eea102a19deb" />

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

<img width="497" alt="image" src="https://github.com/user-attachments/assets/b1baeaef-0d3a-4977-8917-1221a56f4e62" />

---

### 4. Classe `Consultation`  
**Relations** :  
- `@OneToOne RendezVous` (bidirectionnelle).  

**Champs** :  
- `dateConsultation` : Date effective de la consultation.  
- `rapport` : Diagnostic médical (`@Lob` pour les textes longs).  
- `@JsonProperty(WRITE_ONLY)` sur le champ `rendezVous`.  

<img width="508" alt="image" src="https://github.com/user-attachments/assets/1beea21f-bc0d-451a-a895-7d4e6092509c" />

---

### 5. Enumération `StatusRDV`  
**Valeurs** :  
- `PENDING` (En attente)  
- `CANCELLED` (Annulé)  
- `APPROVED` (Accepté)  
**Usage** :  
Persisté en base sous forme de chaîne via `@Enumerated(EnumType.STRING)` dans `RendezVous`.

<img width="310" alt="image" src="https://github.com/user-attachments/assets/499bc821-d97a-4abd-9305-0aafe9386e06" />

## 🗂️ Repositories

- Interface ConsultationRepository : L'interface ConsultationRepository est une interface de persistance spécifique à l'entité Consultation. Elle étend JpaRepository<Consultation, Long>, ce qui lui fournit automatiquement un ensemble complet de méthodes CRUD (Create, Read, Update, Delete) sans nécessiter d'implémentation manuelle. Ici, Consultation désigne l'entité gérée, tandis que Long correspond au type de sa clé primaire (ID).
Avec Spring Data JPA, cette interface est automatiquement détectée et peut être injectée dans les services via le mécanisme d'injection de dépendances.

<img width="603" alt="image" src="https://github.com/user-attachments/assets/4cfe86f5-08b9-4076-8ac8-c7927f3afdb6" />

- Interface MedecinRepository : L’interface MedecinRepository permet d'interagir avec la base de données pour l'entité Medecin. En étendant JpaRepository<Medecin, Long>, elle bénéficie automatiquement des opérations CRUD de base. Elle inclut également une méthode personnalisée findByNom(String nom) pour rechercher un médecin par son nom.
Grâce à Spring Data JPA, l'implémentation de cette méthode est générée dynamiquement à partir de son nom, éliminant le besoin d'écrire une requête SQL manuelle.

<img width="583" alt="image" src="https://github.com/user-attachments/assets/fd3080a0-d1c2-41c8-80b0-f83de16d40f0" />

- Interface PatientRepository : L’interface PatientRepository gère l'accès aux données de l'entité Patient en étendant JpaRepository<Patient, Long>. Elle fournit ainsi les opérations CRUD standards, ainsi qu'une méthode personnalisée findByNom(String nom) pour rechercher un patient par son nom.
Spring Data JPA interprète automatiquement cette méthode et génère la requête appropriée, sans nécessiter d'implémentation manuelle.

<img width="544" alt="image" src="https://github.com/user-attachments/assets/0267e9bd-c0cf-46b7-b012-129458b93972" />

- Interface RendezVousRepository : L'interface RendezVousRepository gère la persistance des entités RendezVous en étendant JpaRepository<RendezVous, String>, indiquant ainsi que sa clé primaire est de type String.
Grâce à cette extension, elle offre automatiquement toutes les opérations CRUD de base, éliminant le besoin d'implémenter manuellement les requêtes.

<img width="593" alt="image" src="https://github.com/user-attachments/assets/0c5619e5-af4c-4b9a-9091-5de46bad9964" />


## 🛠️ Services

### Interface `IHospitalService`

L’interface `IHospitalService` définit les opérations métier principales liées à la gestion des entités médicales telles que les patients, les médecins, les rendez-vous et les consultations. Elle joue un rôle essentiel dans l’architecture de l’application en assurant une séparation claire entre la couche contrôleur (qui traite les requêtes HTTP) et la couche de persistance (repositories).

Cette interface facilite l’évolutivité, la maintenance et les tests unitaires du système en fournissant une abstraction des traitements métiers.

**Méthodes déclarées :**
- `savePatient(Patient patient)` : Enregistre un nouveau patient dans la base de données.
- `saveMedecin(Medecin medecin)` : Ajoute un médecin au système.
- `saveRDV(RendezVous rendezVous)` : Crée un rendez-vous médical. Un identifiant unique est généré automatiquement.
- `saveConsultation(Consultation consultation)` : Enregistre une consultation médicale.

<img width="559" alt="image" src="https://github.com/user-attachments/assets/8192b889-e8d6-48ac-826a-671486763301" />


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

<img width="785" alt="image" src="https://github.com/user-attachments/assets/58761269-dd8c-44c7-8da4-3a444e7f53f6" />
<img width="785" alt="image" src="https://github.com/user-attachments/assets/916ddc7d-8969-4c16-9c3a-a2ec46f5293a" />

`HospitalServiceImpl` centralise ainsi toute la logique métier liée à la gestion des entités médicales, tout en s’appuyant sur les repositories pour la persistance. Elle constitue un exemple typique de couche service dans une application Spring Boot bien structurée.

# Application de Gestion Hospitalière - API REST Spring Boot

## 🌐 Couche Web

### Classe `PatientRestController`
La classe `PatientRestController` est un contrôleur REST qui expose les données des patients via des endpoints HTTP. Grâce à l'annotation `@RestController`, Spring reconnaît automatiquement cette classe comme un composant dédié à la gestion des requêtes web.

**Fonctionnalités principales** :
- Utilise `@Autowired` pour injecter une instance de `PatientRepository` (accès aux opérations de base de données)
- La méthode `patientList()` annotée avec `@GetMapping("/patients")` est déclenchée sur les requêtes GET vers `/patients`
- Retourne les données des patients au format JSON via `patientRepository.findAll()`

<img width="528" alt="image" src="https://github.com/user-attachments/assets/b3413f96-48ce-4db7-8590-9c705142d730" />

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

<img width="779" alt="image" src="https://github.com/user-attachments/assets/13f98dd3-992f-4fb8-9a42-58225e827ea5" />

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
  
<img width="523" alt="image" src="https://github.com/user-attachments/assets/188cbef1-cfc5-4cfa-afbe-9a08405f69e9" />


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
