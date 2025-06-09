# 📌 **Cos’è Spring Data JPA**

**Spring Data JPA** è un modulo di Spring che **semplifica l’accesso al database** utilizzando **Java Persistence API (JPA)**, astratti e automatizzati grazie a interfacce e annotazioni.

📌 Obiettivo: **ridurre il boilerplate code** necessario per interagire con un database relazionale.

---

## 📦 **Dipendenza Maven**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

👉 Include: Hibernate (come implementazione JPA), Spring ORM, JPA API.

---

## 🔄 **Configurazione base (`application.properties`)**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/testdb
spring.datasource.username=utente
spring.datasource.password=pass
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

---

## 🧩 **Struttura tipica di un progetto Spring Data JPA**

```
com.example.demo
├── entity         → classi JPA (@Entity)
├── repository     → interfacce JPA (estendono JpaRepository)
├── service        → logica di business
├── controller     → gestione richieste web
```

---

## 🧱 **Esempio completo**

### 📌 Entity

```java
@Entity
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // costruttori, getter/setter
}
```

### 📌 Repository

```java
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    List<Utente> findByNome(String nome);
}
```

> ⚙️ `JpaRepository<T, ID>` fornisce CRUD + metodi avanzati (es: paginazione, sorting).

### 📌 Service

```java
@Service
public class UtenteService {
    @Autowired
    private UtenteRepository repository;

    public List<Utente> trovaPerNome(String nome) {
        return repository.findByNome(nome);
    }

    public Utente salva(Utente utente) {
        return repository.save(utente);
    }
}
```

### 📌 Controller

```java
@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    private UtenteService service;

    @GetMapping("/{nome}")
    public List<Utente> getByNome(@PathVariable String nome) {
        return service.trovaPerNome(nome);
    }
}
```

---

## 🔍 **Query con Spring Data JPA**

Puoi scrivere query **personalizzate**:

### 📌 Per nome metodo

```java
List<Utente> findByNomeContaining(String parteNome);
```

### 📌 Con `@Query`

```java
@Query("SELECT u FROM Utente u WHERE u.nome = :nome")
List<Utente> trovaPerNome(@Param("nome") String nome);
```

---

## 🧪 **Funzionalità avanzate**

| Funzione                     | Descrizione                        |
| ---------------------------- | ---------------------------------- |
| `@Query`                     | Query HQL personalizzata           |
| `@Modifying`                 | Per `UPDATE` o `DELETE`            |
| `@Transactional`             | Per gestire transazioni            |
| `PagingAndSortingRepository` | Ordinamento e paginazione          |
| `Specification<T>`           | Filtri complessi dinamici          |
| `@EntityGraph`               | Ottimizzazione caricamento (fetch) |

---

## ✅ **Vantaggi**

* CRUD automatico
* Supporto nativo per paginazione, ordinamento
* Facilità d’uso con JPA
* Query dinamiche basate su nomi dei metodi
* Integrazione con Spring Boot (zero config extra)

---

## 🧠 **Buone pratiche**

* Usa DTO per esporre dati al controller
* Non usare direttamente l’Entity nei controller
* Evita logica business dentro il repository
* Abilita `spring.jpa.show-sql=true` solo in sviluppo

---

Vediamo **perché si usa il DTO (Data Transfer Object)** per esporre dati al controller **e perché non è consigliabile usare direttamente le Entity** nei controller in un'applicazione Spring (o in generale MVC).

---

## 🎯 1. **Cos’è un DTO?**

Un **DTO (Data Transfer Object)** è un **oggetto semplice** (POJO) che viene usato per **trasferire dati** tra i livelli dell’applicazione, specialmente tra il backend e il frontend (controller ↔ client web/mobile).

👉 Un DTO:

* Contiene **solo i dati necessari** (niente logica).
* È spesso una **rappresentazione semplificata** dell’entity.
* Non è legato al database né ha annotazioni JPA (`@Entity`, `@Id`, ecc).

---

## 🧱 2. **Differenza tra Entity e DTO**

| Caratteristica     | `@Entity`               | `DTO`                 |
| ------------------ | ----------------------- | --------------------- |
| Legato al DB       | ✅ Sì                    | ❌ No                  |
| Ha annotazioni JPA | ✅ (es. `@Table`, `@Id`) | ❌                     |
| Scopo              | Persistenza             | Trasferimento dati    |
| Logica di business | Possibile               | Nessuna               |
| Dati esposti       | Tutti                   | Solo quelli necessari |

---

## 🚫 3. **Perché **NON** usare le Entity nei controller**

Ecco i principali motivi:

### 🔐 **Sicurezza e controllo**

* L’Entity potrebbe contenere **informazioni sensibili** (es: password, ruoli, dati interni).
* Usando un DTO puoi esporre **solo i campi necessari**.

### 💥 **Disaccoppiamento**

* Il controller non dovrebbe **conoscere i dettagli del database**.
* Se la struttura dell’Entity cambia (es: rename, join, relazione), **il frontend non deve rompersi** → grazie al DTO che funge da interfaccia stabile.

### ⚠️ **Problemi con la serializzazione**

* Le Entity spesso hanno **relazioni bidirezionali** (`@OneToMany`, `@ManyToOne`) che possono causare cicli infiniti nella serializzazione JSON.
* I DTO evitano questo problema perché **non contengono relazioni JPA**.

### 🧪 **Maggiore testabilità**

* I DTO sono **più semplici da creare nei test**, senza dover accedere al database.

---

## ✅ 4. **Quando usare i DTO (best practice)**

* Per tutte le **response dei controller REST**
* Per le **request di creazione/modifica** (es. `CreateUserDTO`, `UpdateProductDTO`)
* Per **form personalizzate** o aggregazioni (es. esami con docente e studente)

---

## 🧩 5. **Esempio pratico**

### Entity

```java
@Entity
public class Utente {
    @Id
    private Long id;
    private String nome;
    private String password;
    private String ruolo; // es. ADMIN, USER
}
```

### DTO

```java
public class UtenteDTO {
    private String nome;
}
```

### Mapper (in un service o classe dedicata)

```java
private UtenteDTO toDTO(Utente u) {
    return UtenteDTO.builder()
        .nome(u.getNome())
        .build();
}
```

### Controller

```java
@GetMapping("/utenti")
public List<UtenteDTO> getUtenti() {
    return utenteService.getAllUtenti(); // ritorna solo DTO
}
```

---

## 🔄 Riassunto

| 🟢 Da fare                    | 🔴 Da evitare                        |
| ----------------------------- | ------------------------------------ |
| Usare DTO per le API          | Esporre Entity direttamente          |
| Mappare DTO in modo esplicito | Ritornare `List<Entity>` dal service |
| Controllare cosa esponi       | Serializzare tutto automaticamente   |

---

**esempio completo** di utilizzo dei **DTO in un contesto Spring Boot**, includendo:

* DTO per visualizzazione (`UtenteDTO`)
* DTO per creazione (`UtenteCreateDTO`)
* Entity JPA (`Utente`)
* Mapper manuale nel service
* Controller che usa solo DTO

Immagina un sistema di gestione utenti dove vogliamo esporre solo nome e email ma mai la password.

---

## 🔸 1. Entity – `Utente`

```java
@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String password;
}
```

---

## 🔹 2. DTO per visualizzazione – `UtenteDTO`

```java
@Data
@Builder
public class UtenteDTO {
    private Long id;
    private String nome;
    private String email;
}
```

---

## 🔹 3. DTO per creazione – `UtenteCreateDTO`

```java
@Data
public class UtenteCreateDTO {
    private String nome;
    private String email;
    private String password;
}
```

---

## 🔸 4. Repository – `UtenteRepository`

```java
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    // metodi CRUD standard
}
```

---

## 🔸 5. Service – `UtenteService`

```java
@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public List<UtenteDTO> getAllUtenti() {
        return utenteRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public UtenteDTO createUtente(UtenteCreateDTO dto) {
        Utente utente = Utente.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .password(dto.getPassword()) // salvala hashata nel mondo reale!
                .build();

        Utente salvato = utenteRepository.save(utente);
        return toDTO(salvato);
    }

    // 🔒 Mapper privato
    private UtenteDTO toDTO(Utente u) {
        return UtenteDTO.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .build();
    }
}
```

---

## 🔸 6. Controller – `UtenteController`

```java
@RestController
@RequestMapping("/api/utenti")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService utenteService;

    @GetMapping
    public List<UtenteDTO> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    @PostMapping
    public ResponseEntity<UtenteDTO> creaUtente(@RequestBody UtenteCreateDTO dto) {
        UtenteDTO creato = utenteService.createUtente(dto);
        return new ResponseEntity<>(creato, HttpStatus.CREATED);
    }
}
```

---

## ✅ Vantaggi di questa struttura

* **Non esponi la password** né altre proprietà sensibili.
* Puoi **validare facilmente** gli input usando annotazioni su `UtenteCreateDTO`.
* Puoi cambiare la struttura interna (`Entity`) senza rompere le API.
* Facilita la scrittura di **test e mock**.

---

### MapStruct

 esempio **completo** usando **MapStruct** in un'app Spring Boot per mappare tra:

* `Utente` (Entity)
* `UtenteDTO` (DTO di output)
* `UtenteCreateDTO` (DTO di input)

---

## ✅ 1. Aggiungi la dipendenza in `pom.xml`

```xml
<dependencies>
    <!-- MapStruct -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>1.5.5.Final</version>
    </dependency>

    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>1.5.5.Final</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Necessario per generare il codice MapStruct -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>17</source>
                <target>17</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>1.5.5.Final</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## 📦 2. Entity – `Utente`

```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String password;
}
```

---

## 📦 3. DTO – `UtenteDTO`

```java
@Data
@Builder
public class UtenteDTO {
    private Long id;
    private String nome;
    private String email;
}
```

## 📦 4. DTO di input – `UtenteCreateDTO`

```java
@Data
public class UtenteCreateDTO {
    private String nome;
    private String email;
    private String password;
}
```

---

## 🔁 5. Mapper – `UtenteMapper`

```java
@Mapper(componentModel = "spring")
public interface UtenteMapper {

    UtenteDTO toDTO(Utente utente);

    List<UtenteDTO> toDTOList(List<Utente> utenti);

    Utente fromCreateDTO(UtenteCreateDTO dto);
}
```

---

## 🔧 6. Repository – `UtenteRepository`

```java
public interface UtenteRepository extends JpaRepository<Utente, Long> {
}
```

---

## 🧠 7. Service – `UtenteService`

```java
@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;

    public List<UtenteDTO> getAllUtenti() {
        return utenteMapper.toDTOList(utenteRepository.findAll());
    }

    public UtenteDTO creaUtente(UtenteCreateDTO dto) {
        Utente utente = utenteMapper.fromCreateDTO(dto);
        // 🔐 nel mondo reale qui si dovrebbe criptare la password
        return utenteMapper.toDTO(utenteRepository.save(utente));
    }
}
```

---

## 🌐 8. Controller – `UtenteController`

```java
@RestController
@RequestMapping("/api/utenti")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService utenteService;

    @GetMapping
    public List<UtenteDTO> getAll() {
        return utenteService.getAllUtenti();
    }

    @PostMapping
    public ResponseEntity<UtenteDTO> create(@RequestBody UtenteCreateDTO dto) {
        UtenteDTO creato = utenteService.creaUtente(dto);
        return new ResponseEntity<>(creato, HttpStatus.CREATED);
    }
}
```

---

## 📌 Conclusione

Con MapStruct:

* Eviti codice di mapping manuale.
* Il compilatore genera implementazioni **efficienti e sicure**.
* Puoi **personalizzare** i mapping con annotazioni (`@Mapping`, `@AfterMapping`, ecc.).


