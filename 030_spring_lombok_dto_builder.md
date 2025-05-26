
![images/programmer/DTO1.png](https://github.com/maboglia/ProgrammingResources/blob/master/images/programmer/DTO2.png?raw=true)


Il **progetto Lombok** è una libreria Java open-source che mira a ridurre il **boilerplate code**, cioè il codice ripetitivo e verboso tipico della programmazione Java. Lombok permette di scrivere classi Java più concise e leggibili grazie a **annotazioni** che generano automaticamente metodi comuni come `getters`, `setters`, `equals`, `hashCode`, `toString`, costruttori, builder, e altro ancora.

---

### ✅ **Caratteristiche principali di Lombok**

1. **@Getter e @Setter**
   Generano automaticamente i metodi getter e setter per i campi della classe.

   ```java
   @Getter @Setter
   private String nome;
   ```

2. **@ToString**
   Crea automaticamente il metodo `toString()`.

3. **@EqualsAndHashCode**
   Crea i metodi `equals()` e `hashCode()` basati sui campi della classe.

4. **@NoArgsConstructor, @AllArgsConstructor, @RequiredArgsConstructor**
   Generano costruttori con 0, tutti, o solo i campi `final` / `@NonNull`.

5. **@Data**
   Una scorciatoia per `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode` e `@RequiredArgsConstructor`.

   ```java
   @Data
   public class Persona {
       private String nome;
       private int età;
   }
   ```

6. **@Builder**
   Implementa il pattern builder per creare oggetti complessi in modo fluido.

   ```java
   @Builder
   public class Utente {
       private String nome;
       private String email;
   }
   ```

7. **@Slf4j**
   Crea automaticamente un logger SLF4J nella classe, senza doverlo dichiarare manualmente.

---

### 🛠️ **Integrazione**

* **Maven**:

  ```xml
  <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.30</version>
      <scope>provided</scope>
  </dependency>
  ```

* **IDE**:
  Lombok richiede il supporto dell’IDE (come IntelliJ IDEA o Eclipse). È necessario installare il plugin Lombok e abilitare l'annotation processing.

---

### ⚠️ **Vantaggi e svantaggi**

**Vantaggi**:

* Riduce la quantità di codice ripetitivo.
* Migliora la leggibilità delle classi.
* Accelera lo sviluppo.

**Svantaggi**:

* Aggiunge una dipendenza esterna al progetto.
* Può essere difficile da capire per chi non conosce Lombok.
* Alcuni tool di analisi statica o IDE potrebbero non supportarlo completamente senza plugin.

---

### 📌 Quando usare Lombok?

È utile in **progetti enterprise, Spring Boot**, o quando si lavora con molte entità o DTO, dove il codice getter/setter/constructor è ripetitivo. Tuttavia, in contesti dove la chiarezza o il controllo del codice è essenziale (per esempio, librerie pubbliche), va usato con cautela.

---

Ecco un esempio completo e realistico di un'applicazione Java con **Lombok** in cui abbiamo:

* Una classe **`Utente` (Entity/Model)**
* Un **DTO (`UtenteDto`)** per il trasferimento dati
* Un **Mapper (`UtenteMapper`)** per la conversione tra i due

---

### 🧩 1. `Utente.java` – Model (Entity)

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {
    private Long id;
    private String nome;
    private String email;
    private int età;
}
```

---

### 📦 2. `UtenteDto.java` – Data Transfer Object

```java
import lombok.Data;

@Data
public class UtenteDto {
    private String nome;
    private String email;
}
```

> In questo DTO non includiamo `id` ed `età`, per simulare una situazione in cui questi dati non devono essere esposti (es. registrazione).

---

### 🔁 3. `UtenteMapper.java` – Mapper manuale

```java
public class UtenteMapper {

    public static UtenteDto toDto(Utente utente) {
        if (utente == null) return null;

        UtenteDto dto = new UtenteDto();
        dto.setNome(utente.getNome());
        dto.setEmail(utente.getEmail());
        return dto;
    }

    public static Utente toEntity(UtenteDto dto) {
        if (dto == null) return null;

        Utente utente = new Utente();
        utente.setNome(dto.getNome());
        utente.setEmail(dto.getEmail());
        return utente;
    }
}
```

---

### 🧪 4. Uso del Mapper in un contesto applicativo

```java
public class Main {
    public static void main(String[] args) {
        // Model -> DTO
        Utente utente = new Utente(1L, "Mario Rossi", "mario@esempio.com", 30);
        UtenteDto dto = UtenteMapper.toDto(utente);
        System.out.println("DTO: " + dto);

        // DTO -> Model
        Utente nuovoUtente = UtenteMapper.toEntity(dto);
        System.out.println("Model da DTO: " + nuovoUtente);
    }
}
```

---

### 🛠️ Alternativa con MapStruct

Se vuoi automatizzare il mapping, puoi usare **[MapStruct](https://mapstruct.org/)**, un'altra libreria Java per la generazione automatica dei mapper.

---

Ecco un esempio aggiornato con:

* **Lombok `@Builder`** per creare gli oggetti in modo fluido
* Il **mapper** incluso come metodo **`private`** all'interno di un **`UtenteService`**
* Separazione tra DTO e Entity

---

### 📦 1. `Utente.java` – Entity con `@Builder`

```java
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utente {
    private Long id;
    private String nome;
    private String email;
    private int eta;
}
```

---

### 📦 2. `UtenteDto.java` – DTO con `@Builder`

```java
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtenteDto {
    private String nome;
    private String email;
}
```

---

### 🛠️ 3. `UtenteService.java` – Contiene logica e mapping

```java
public class UtenteService {

    public UtenteDto getDettaglioUtente(Long id) {
        // Simulazione: recupera un utente (in realtà lo prenderesti da un repository)
        Utente utente = Utente.builder()
                .id(id)
                .nome("Mario Bianchi")
                .email("mario.bianchi@example.com")
                .eta(35)
                .build();

        return toDto(utente);
    }

    public Utente creaUtente(UtenteDto dto) {
        // Simula la creazione di un utente da DTO
        Utente utente = toEntity(dto);
        // Qui lo salveresti in un database, ad esempio
        return utente;
    }

    // MAPPING

    private UtenteDto toDto(Utente utente) {
        if (utente == null) return null;

        return UtenteDto.builder()
                .nome(utente.getNome())
                .email(utente.getEmail())
                .build();
    }

    private Utente toEntity(UtenteDto dto) {
        if (dto == null) return null;

        return Utente.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
    }
}
```

---

### ✅ 4. Esempio di utilizzo (`Main.java`)

```java
public class Main {
    public static void main(String[] args) {
        UtenteService service = new UtenteService();

        // Creazione da DTO
        UtenteDto dto = UtenteDto.builder()
                .nome("Lucia Verdi")
                .email("lucia.verdi@example.com")
                .build();

        Utente nuovoUtente = service.creaUtente(dto);
        System.out.println("Utente creato: " + nuovoUtente);

        // Ottenimento DTO da entity simulata
        UtenteDto dettaglio = service.getDettaglioUtente(1L);
        System.out.println("DTO Dettaglio: " + dettaglio);
    }
}
```

---

### ✨ Output atteso

```
Utente creato: Utente(id=null, nome=Lucia Verdi, email=lucia.verdi@example.com, eta=0)
DTO Dettaglio: UtenteDto(nome=Mario Bianchi, email=mario.bianchi@example.com)
```

---

Nel contesto dell’esempio con **Lombok**, l’annotazione `@Builder` fornisce un modo elegante e sicuro per **costruire oggetti immutabili o semi-immutabili** – come `Utente` e `UtenteDto` – **senza dover scrivere costruttori complessi** o chiamare più volte i `set`.

---

### 🔧 Cos'è `@Builder` in pratica?

Il **Builder Pattern** è una tecnica creazionale per costruire oggetti complessi passo-passo. Lombok lo semplifica automaticamente.

#### ✅ Vantaggi in questo caso

1. **Leggibilità**

   ```java
   UtenteDto.builder()
       .nome("Mario")
       .email("mario@example.com")
       .build();
   ```

   È immediatamente chiaro **quali campi vengono valorizzati**, senza bisogno di ricordare l’ordine di un costruttore con più parametri.

2. **Evita errori**
   Nessun rischio di confondere l’ordine dei parametri (ad es. `new Utente("Mario", 35, "mario@example.com")`).

3. **Facile da mantenere**
   Se aggiungi un campo opzionale (es. `telefono`), puoi iniziare ad usarlo nel builder senza rompere codice esistente.

---

### 🧱 Perché usarlo con `DTO` e `Entity`

Nel nostro caso:

* **DTO** contiene solo una parte dei dati: per esempio, non ha `id` o `eta`, ma solo `nome` e `email`.
* **Entity** è il modello completo con tutti i campi, che potresti salvare in un database.

Con il builder:

* Creare un `Utente` da un `UtenteDto` è chiaro e conciso
* Costruire una risposta `UtenteDto` a partire da una `Entity` è semplice

---

### 🔍 Differenze rispetto a setter e costruttori

| Approccio       | Pro                                                 | Contro                                   |
| --------------- | --------------------------------------------------- | ---------------------------------------- |
| **Builder**     | + Chiarezza, flessibilità, evita errori sull’ordine | - Più verboso (ma Lombok lo nasconde)    |
| **Setter**      | + Flessibile                                        | - Permette oggetti incompleti e mutabili |
| **Costruttore** | + Immutabilità                                      | - Difficile gestire molti parametri      |

---

### 📌 In breve

Usare `@Builder` in questo contesto:

* **rende il codice più pulito**
* **riduce i bug da parametri fuori ordine**
* **rende la trasformazione DTO <-> Entity elegante e leggibile**

---

Il **Builder Pattern** è un *design pattern creazionale* che **semplifica la creazione di oggetti complessi**, specialmente quando ci sono molti parametri opzionali o combinazioni di configurazioni.

---

### 📌 **Obiettivo**

Separare **la costruzione di un oggetto** dalla **rappresentazione finale** in modo che lo stesso processo possa creare rappresentazioni diverse.

---

## 🔧 Problema che risolve

Supponiamo di avere una classe con tanti parametri:

```java
public class Computer {
    private String CPU;
    private String RAM;
    private String storage;
    private boolean hasGPU;
}
```

Costruire oggetti con molti costruttori o con tanti `set` può diventare:

* Poco chiaro
* Soggetto a errori
* Difficile da leggere

### ❌ Costruttore lungo

```java
Computer pc = new Computer("Intel", "16GB", "1TB", true);
```

> Cosa significa `true`? Cosa succede se dimentico un parametro?

---

## ✅ Soluzione con **Builder Pattern**

```java
Computer pc = Computer.builder()
    .CPU("Intel")
    .RAM("16GB")
    .storage("1TB")
    .hasGPU(true)
    .build();
```

> ✅ Molto più leggibile e sicuro.

---

### 🔍 Caratteristiche principali

* Ogni metodo del builder **restituisce lo stesso oggetto builder**, permettendo la **chaining**.
* Il metodo `build()` restituisce l’oggetto finale costruito.
* Può essere implementato manualmente o generato automaticamente (es. con **Lombok**).

---

## 🛠️ Implementazione manuale (esempio semplificato)

```java
public class Computer {
    private String CPU;
    private String RAM;
    private String storage;
    private boolean hasGPU;

    private Computer(Builder builder) {
        this.CPU = builder.CPU;
        this.RAM = builder.RAM;
        this.storage = builder.storage;
        this.hasGPU = builder.hasGPU;
    }

    public static class Builder {
        private String CPU;
        private String RAM;
        private String storage;
        private boolean hasGPU;

        public Builder CPU(String cpu) {
            this.CPU = cpu;
            return this;
        }

        public Builder RAM(String ram) {
            this.RAM = ram;
            return this;
        }

        public Builder storage(String storage) {
            this.storage = storage;
            return this;
        }

        public Builder hasGPU(boolean hasGPU) {
            this.hasGPU = hasGPU;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}
```

Uso:

```java
Computer myPc = new Computer.Builder()
    .CPU("Intel i9")
    .RAM("32GB")
    .storage("1TB SSD")
    .hasGPU(true)
    .build();
```

---

## ⚡ Con Lombok

Lombok semplifica tutto con un’annotazione:

```java
@Builder
public class Computer {
    private String CPU;
    private String RAM;
    private String storage;
    private boolean hasGPU;
}
```

Uso identico al manuale, ma senza boilerplate.

---

## ✅ Vantaggi

* Leggibilità e chiarezza
* Costruzione **immutabile** (oggetti finali non modificabili)
* Evita costruttori con molti parametri
* Supporta **valori opzionali** senza mille overload

---

## ❌ Svantaggi

* Più codice da scrivere (se non si usa Lombok)
* Più passaggi per costruire un oggetto (ma più sicuro)

---

### 📦 Dove si usa il Builder Pattern

* API fluide (es. Java Stream API, JPA CriteriaBuilder)
* Framework come Spring, Hibernate
* Creazione di DTO, entity, richieste HTTP complesse
* Quando si vuole evitare setter e oggetti mutabili

---

Per capire bene il contesto in cui si usano **DTO** e **DAO**, è utile partire da un’architettura a livelli comune in applicazioni Java (come in un'app Spring Boot, ad esempio):

---

## 🏗️ Architettura a livelli

```
Client (API REST / UI)
   ↓
Controller
   ↓
Service
   ↓
DTO ↔ Mapper ↔ Entity
   ↓
DAO (Repository)
   ↓
Database
```

---

## 📦 DTO - Data Transfer Object

### 🔹 **Cos'è**

Un oggetto semplificato usato per **trasferire dati tra i livelli dell'applicazione**, soprattutto tra il *controller* e il *service*, o verso l’esterno (API).

### 🔹 **A cosa serve**

* Isolare il *domain model* (Entity) dall’esposizione esterna
* Evitare di esporre **tutti i dati** dell’entità
* Migliorare la **sicurezza** e la **manutenibilità**
* Facilitare la **serializzazione/deserializzazione** (es. JSON ↔ oggetto)

### 🔹 **Esempio**

```java
public class UtenteDTO {
    private String nome;
    private String email;
}
```

---

## 🗄️ DAO - Data Access Object

### 🔹 **Cos'è**

Una **classe o interfaccia** che fornisce un’interfaccia astratta per l’accesso ai dati (tipicamente il database).

In Java moderno (es. con Spring Data JPA), il DAO è spesso un’interfaccia chiamata **Repository**.

### 🔹 **A cosa serve**

* Isolare la logica di accesso ai dati dal resto dell'applicazione
* Rende il codice più testabile
* Incapsula le query e l’accesso al database

### 🔹 **Esempio**

```java
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByEmail(String email);
}
```

---

## 🧠 Differenza tra DTO e DAO

| Aspetto     | DTO                                          | DAO                                              |
| ----------- | -------------------------------------------- | ------------------------------------------------ |
| Scopo       | Trasferire dati tra i livelli dell'app       | Accedere e gestire dati da una fonte persistente |
| Rappresenta | Dati (spesso parziali o aggregati)           | Logica di accesso al DB                          |
| Livello     | Tra controller ↔ service, o service ↔ client | Tra service ↔ database                           |
| Esempio     | `UtenteDTO` (nome, email)                    | `UtenteRepository` (findByEmail, save)           |

---

## 🧩 Insieme a un Mapper

Spesso tra **DTO** e **Entity** c'è un **mapper**, manuale o automatico (es. con MapStruct), per convertire tra i due.

```java
public class UtenteMapper {
    public static UtenteDTO toDto(Utente entity) {
        return UtenteDTO.builder()
                .nome(entity.getNome())
                .email(entity.getEmail())
                .build();
    }

    public static Utente toEntity(UtenteDTO dto) {
        return Utente.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
    }
}
```

---

## ✅ Vantaggi di separare DTO, Entity e DAO

* **Disaccoppiamento** tra livelli (es. cambiare il DB o il front-end senza impattare tutto)
* **Testabilità** maggiore (puoi testare service con DTO senza DB)
* **Controllo** su cosa viene esposto o modificato
* **Sicurezza** (eviti di esporre dati sensibili)

---

Ecco un esempio completo basato su Spring Boot e Lombok, modellando un sistema semplificato per la gestione di **Materie**, **Docenti**, **Studenti** ed **Esami** in un corso post-diploma di Informatica. L’esempio include:

* **DTO (Data Transfer Object)**
* **Entity (JPA)**
* **Mapper (manuale o con MapStruct)**
* **DAO (Repository)**
* **Service**
* **Controller** (facoltativo se vuoi usarlo come API REST)

Iniziamo con la **Materia** come esempio. Possiamo poi replicare per gli altri.

---

### 📁 `Materia.java` – Entity

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    private Docente docente;
}
```

---

### 📁 `MateriaDTO.java` – DTO

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaDTO {
    private Long id;
    private String nome;
    private Long docenteId;
}
```

---

### 📁 `MateriaRepository.java` – DAO

```java
public interface MateriaRepository extends JpaRepository<Materia, Long> {
}
```

---

### 📁 `MateriaService.java` – Service + Mapper

```java
@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final DocenteRepository docenteRepository;

    public MateriaDTO createMateria(MateriaDTO dto) {
        Materia materia = toEntity(dto);
        materia = materiaRepository.save(materia);
        return toDTO(materia);
    }

    public List<MateriaDTO> findAll() {
        return materiaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private Materia toEntity(MateriaDTO dto) {
        return Materia.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .docente(docenteRepository.findById(dto.getDocenteId()).orElse(null))
                .build();
    }

    private MateriaDTO toDTO(Materia materia) {
        return MateriaDTO.builder()
                .id(materia.getId())
                .nome(materia.getNome())
                .docenteId(materia.getDocente() != null ? materia.getDocente().getId() : null)
                .build();
    }
}
```

---

### 📁 `Docente.java` – Entity (base per l’esempio)

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
}
```

---

### Aggiunte per completare l’esempio

Replicare la stessa logica per:

* `Studente.java` e `StudenteDTO.java`
* `Esame.java` e `EsameDTO.java` (con riferimenti a Studente, Materia, Voto)

---

```java
package com.example.demo;

// --- ENTITY ---
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {
    @Id @GeneratedValue
    private Long id;
    private String nome;
}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Docente {
    @Id @GeneratedValue
    private Long id;
    private String nome;
    private String cognome;

    @ManyToMany
    private List<Materia> materie;
}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Studente {
    @Id @GeneratedValue
    private Long id;
    private String nome;
    private String cognome;
}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Esame {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Studente studente;

    @ManyToOne
    private Materia materia;

    private int voto;
    private String data;
}

// --- DTO ---
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsameDTO {
    private Long id;
    private Long studenteId;
    private Long materiaId;
    private int voto;
    private String data;
}

// --- DAO ---
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository<Materia, Long> {}
public interface DocenteRepository extends JpaRepository<Docente, Long> {}
public interface StudenteRepository extends JpaRepository<Studente, Long> {}
public interface EsameRepository extends JpaRepository<Esame, Long> {}

// --- SERVICE ---
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EsameService {

    @Autowired
    private EsameRepository esameRepository;

    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    public EsameDTO salvaEsame(EsameDTO dto) {
        Esame esame = fromDTO(dto);
        return toDTO(esameRepository.save(esame));
    }

    private Esame fromDTO(EsameDTO dto) {
        return Esame.builder()
            .id(dto.getId())
            .voto(dto.getVoto())
            .data(dto.getData())
            .studente(studenteRepository.findById(dto.getStudenteId()).orElseThrow())
            .materia(materiaRepository.findById(dto.getMateriaId()).orElseThrow())
            .build();
    }

    private EsameDTO toDTO(Esame esame) {
        return EsameDTO.builder()
            .id(esame.getId())
            .voto(esame.getVoto())
            .data(esame.getData())
            .studenteId(esame.getStudente().getId())
            .materiaId(esame.getMateria().getId())
            .build();
    }
}

// --- CONTROLLER (esempio) ---
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/esami")
public class EsameController {

    @Autowired
    private EsameService esameService;

    @PostMapping
    public EsameDTO creaEsame(@RequestBody EsameDTO dto) {
        return esameService.salvaEsame(dto);
    }
}
```

---

![images/programmer/DTO2.png](https://github.com/maboglia/ProgrammingResources/blob/master/images/programmer/DTO2.png?raw=true)

Non **devi** sempre usare i DTO, ma in molti casi **è fortemente consigliato**. Vediamo **quando usarli**, **perché**, e **quando puoi evitarli**:

---

### ✅ **Quando usare i DTO (Data Transfer Object)**

Usali quando:

1. **Esponi dati tramite API REST**
   – Eviti di mostrare direttamente le entità del database (Entity) all’esterno.
   – Nascondi campi sensibili o interni (`password`, `createdAt`, ecc.).

2. **Vuoi controllare i dati in input/output**
   – DTO separati per `Create`, `Update` o `Response` ti permettono validazioni e conversioni specifiche.

3. **Hai mapping complessi o aggregati**
   – Un DTO può aggregare più entità (`StudenteDTO` che include anche le `Materie` frequentate).

4. **Mantieni un'architettura pulita**
   – Aiuta a separare il dominio (Entity) dalla logica di presentazione o API.

---

### ❌ **Quando puoi evitare i DTO**

1. **Progetti piccoli o prototipi**
   – Se l’app è semplice e interna, puoi usare direttamente le Entity.

2. **Lettura diretta senza logica complessa**
   – Se esponi solo pochi dati letti dal database senza logica o manipolazione, un DTO può essere superfluo.

3. **Non hai bisogno di nascondere o trasformare dati**
   – Se il tuo `Entity` è già una rappresentazione fedele del dato da esporre.

---

### 🔍 Esempio pratico

Se hai questa Entity:

```java
@Entity
public class Studente {
    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String password; // NON vuoi esporla!
}
```

Evita di esporla direttamente. Usa un DTO:

```java
public class StudenteDTO {
    private Long id;
    private String nomeCompleto;
    private String email;
}
```

E un mapper:

```java
StudenteDTO studenteToDTO(Studente s) {
    return StudenteDTO.builder()
        .id(s.getId())
        .nomeCompleto(s.getNome() + " " + s.getCognome())
        .email(s.getEmail())
        .build();
}
```

---

### ✅ In sintesi

| Scenario                            | Uso DTO?               |
| ----------------------------------- | ---------------------- |
| API pubblica                        | **Sì**                 |
| Validazioni input                   | **Sì**                 |
| Applicazione interna molto semplice | **No (puoi evitarlo)** |
| Entity con dati sensibili           | **Sì**                 |
| Progetto grande o scalabile         | **Sì**                 |

---

Perfetto! Inserire il **pattern Facade** nel contesto della tua applicazione Spring Boot per la gestione delle **views (ad esempio pagine HTML con Thymeleaf o frontend)** è un’ottima idea per **semplificare l’accesso alla logica di business** da parte dei controller o componenti di presentazione.

---

## 🔷 Cos’è il Pattern Facade?

Il **Facade Pattern** fornisce un’interfaccia semplificata a un insieme complesso di classi, librerie o servizi.
In un'app MVC, può essere usato per:

* semplificare la chiamata di più servizi o DAO,
* nascondere la logica complessa del dominio,
* mantenere i controller puliti e focalizzati sulla presentazione.

---

## 🧩 Contesto della tua App

Hai un dominio didattico con:

* `Studente`
* `Docente`
* `Materia`
* `Esame`

Un controller può dover mostrare nella view, ad esempio:

* l’elenco degli esami di uno studente,
* i dettagli di una materia e i docenti che la insegnano.

👉 È qui che entra in gioco la **Facade**.

---

## 📦 Esempio di Facade: `CorsoInformaticaFacade`

```java
@Service
@RequiredArgsConstructor
public class CorsoInformaticaFacade {

    private final StudenteService studenteService;
    private final EsameService esameService;
    private final DocenteService docenteService;
    private final MateriaService materiaService;

    public StudenteDTO getStudenteConEsami(Long idStudente) {
        var studente = studenteService.findById(idStudente);
        var esami = esameService.findByStudenteId(idStudente);
        return mapToDTO(studente, esami);
    }

    public List<MateriaDTO> getMaterieConDocenti() {
        var materie = materiaService.findAll();
        return materie.stream()
            .map(materia -> {
                List<DocenteDTO> docenti = docenteService.findByMateria(materia.getId());
                return mapToDTO(materia, docenti);
            })
            .toList();
    }

    // mapping privato
    private StudenteDTO mapToDTO(Studente s, List<Esame> esami) {
        return StudenteDTO.builder()
            .id(s.getId())
            .nomeCompleto(s.getNome() + " " + s.getCognome())
            .email(s.getEmail())
            .esami(esami.stream().map(this::mapEsame).toList())
            .build();
    }

    private EsameDTO mapEsame(Esame e) {
        return EsameDTO.builder()
            .id(e.getId())
            .voto(e.getVoto())
            .materia(e.getMateria().getNome())
            .data(e.getData())
            .build();
    }

    private MateriaDTO mapToDTO(Materia m, List<DocenteDTO> docenti) {
        return MateriaDTO.builder()
            .id(m.getId())
            .nome(m.getNome())
            .docenti(docenti)
            .build();
    }
}
```

---

## 🧭 E il Controller?

```java
@Controller
@RequiredArgsConstructor
public class StudenteViewController {

    private final CorsoInformaticaFacade corsoFacade;

    @GetMapping("/studenti/{id}/esami")
    public String viewEsamiStudente(@PathVariable Long id, Model model) {
        StudenteDTO studenteDTO = corsoFacade.getStudenteConEsami(id);
        model.addAttribute("studente", studenteDTO);
        return "esami-studente"; // nome del template Thymeleaf
    }
}
```

---

## 🎯 Vantaggi

| Facade                           | Beneficio                                                  |
| -------------------------------- | ---------------------------------------------------------- |
| ✅ Incapsula logica complessa     | Il controller non deve sapere come combinare i dati        |
| ✅ Rende il controller più pulito | Un solo metodo al posto di 3-4 chiamate a service/mapper   |
| ✅ Favorisce riuso                | La Facade può essere usata anche da altri controller o API |
| ✅ Più testabile                  | Puoi testare la Facade in isolamento                       |

---

## 🧪 Esempio completo con la Facade implementata per  `Esame`

Ecco un esempio completo della **vista Esame** nel contesto di una web app per la gestione di un corso di informatica post-diploma, utilizzando:

* DTO
* Mapper
* Service
* DAO (Repository)
* Pattern Facade
* Controller (per la view)

---

### ✅ `EsameDto.java`

```java
@Data
@Builder
public class EsameDto {
    private Long id;
    private LocalDate data;
    private Long idStudente;
    private Long idMateria;
    private Integer voto;
}
```

---

### ✅ `Esame.java` (Entity)

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Esame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @ManyToOne
    private Studente studente;

    @ManyToOne
    private Materia materia;

    private Integer voto;
}
```

---

### ✅ `EsameRepository.java`

```java
public interface EsameRepository extends JpaRepository<Esame, Long> {
    List<Esame> findByStudenteId(Long idStudente);
}
```

---

### ✅ `EsameService.java`

```java
@Service
@RequiredArgsConstructor
public class EsameService {

    private final EsameRepository esameRepository;

    public List<Esame> getEsami() {
        return esameRepository.findAll();
    }

    public Optional<Esame> getEsameById(Long id) {
        return esameRepository.findById(id);
    }

    public Esame salvaEsame(Esame esame) {
        return esameRepository.save(esame);
    }

    public void eliminaEsame(Long id) {
        esameRepository.deleteById(id);
    }
}
```

---

### ✅ `EsameFacade.java`

```java
@Component
@RequiredArgsConstructor
public class EsameFacade {

    private final EsameService esameService;

    public List<EsameDto> getAllEsami() {
        return esameService.getEsami().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public EsameDto getEsame(Long id) {
        return esameService.getEsameById(id)
            .map(this::toDto)
            .orElse(null);
    }

    public EsameDto creaEsame(EsameDto dto) {
        Esame esame = toEntity(dto);
        return toDto(esameService.salvaEsame(esame));
    }

    private EsameDto toDto(Esame e) {
        return EsameDto.builder()
            .id(e.getId())
            .data(e.getData())
            .idStudente(e.getStudente().getId())
            .idMateria(e.getMateria().getId())
            .voto(e.getVoto())
            .build();
    }

    private Esame toEntity(EsameDto dto) {
        Studente s = new Studente(); s.setId(dto.getIdStudente());
        Materia m = new Materia(); m.setId(dto.getIdMateria());

        return Esame.builder()
            .id(dto.getId())
            .data(dto.getData())
            .studente(s)
            .materia(m)
            .voto(dto.getVoto())
            .build();
    }
}
```

---

### ✅ `EsameController.java`

```java
@Controller
@RequiredArgsConstructor
public class EsameController {

    private final EsameFacade esameFacade;

    @GetMapping("/esami")
    public String listaEsami(Model model) {
        model.addAttribute("esami", esameFacade.getAllEsami());
        return "esami/lista"; // esami/lista.html
    }

    @GetMapping("/esami/nuovo")
    public String nuovoEsameForm(Model model) {
        model.addAttribute("esame", new EsameDto());
        return "esami/form";
    }

    @PostMapping("/esami")
    public String salvaEsame(@ModelAttribute EsameDto esame) {
        esameFacade.creaEsame(esame);
        return "redirect:/esami";
    }
}
```

---

### ✅ `esami/lista.html` (Thymeleaf)

```html
<h2>Lista Esami</h2>
<a href="/esami/nuovo">Aggiungi Esame</a>
<table>
    <tr><th>ID</th><th>Data</th><th>Studente</th><th>Materia</th><th>Voto</th></tr>
    <tr th:each="esame : ${esami}">
        <td th:text="${esame.id}"></td>
        <td th:text="${esame.data}"></td>
        <td th:text="${esame.idStudente}"></td>
        <td th:text="${esame.idMateria}"></td>
        <td th:text="${esame.voto}"></td>
    </tr>
</table>
```

---

### ✅ `esami/form.html`

```html
<h2>Nuovo Esame</h2>
<form method="post" action="/esami">
    <label>Data:</label>
    <input type="date" name="data"><br>
    <label>Studente ID:</label>
    <input type="number" name="idStudente"><br>
    <label>Materia ID:</label>
    <input type="number" name="idMateria"><br>
    <label>Voto:</label>
    <input type="number" name="voto"><br>
    <button type="submit">Salva</button>
</form>
```

---

[progetto scaricabile](./workspace-spring/spring-corsi/src/main)

---

## Configurazione application.properties e database

Ecco un esempio completo di `application.properties` per il tuo progetto Spring Boot che gestisce Materie, Docenti, Studenti ed Esami. Questo file è pensato per un'applicazione che usa **H2 Database** in memoria per semplificare lo sviluppo e i test:

```properties
# Server
server.port=8080

# Datasource H2
spring.datasource.url=jdbc:h2:mem:esamidb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false
spring.thymeleaf.mode=HTML
```

---

### Opzioni aggiuntive

Se invece usi un database reale (come MySQL o PostgreSQL), ecco la configurazione equivalente. 

Perfetto! Ecco come configurare il file `application.properties` per un'applicazione Spring Boot con **MySQL** usando come utente e password `ITS_2025`.

### ✅ `application.properties` per MySQL

```properties
# Server port
server.port=8080

# MySQL DataSource Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/esamidb?useSSL=false&serverTimezone=UTC
spring.datasource.username=ITS_2025
spring.datasource.password=ITS_2025
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate & JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# Thymeleaf (optional, for views)
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false

# Optional: To prevent timezone issues
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

---

### 📌 Assicurati che:

1. **MySQL sia in esecuzione** e accessibile su `localhost:3306`.
2. **Esista un database chiamato `esamidb`** (`CREATE DATABASE esamidb;`).
3. L'utente `ITS_2025` abbia i permessi su quel database:

   ```sql
   CREATE USER 'ITS_2025'@'localhost' IDENTIFIED BY 'ITS_2025';
   GRANT ALL PRIVILEGES ON esamidb.* TO 'ITS_2025'@'localhost';
   FLUSH PRIVILEGES;
   ```

---

### script SQL per popolare le tabelle iniziali

Ecco uno **script SQL completo** per creare le tabelle principali (`materia`, `docente`, `studente`, `esame`) e popolarle con **dati di prova** per il progetto Spring Boot su **MySQL**.

---

### 📄 `init_esamidb.sql`

```sql
-- Crea il database (se non esiste)
CREATE DATABASE IF NOT EXISTS esamidb;
USE esamidb;

-- Tabella: materia
CREATE TABLE IF NOT EXISTS materia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Tabella: docente
CREATE TABLE IF NOT EXISTS docente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL
);

-- Tabella: studente
CREATE TABLE IF NOT EXISTS studente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL
);

-- Tabella: esame
CREATE TABLE IF NOT EXISTS esame (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    voto INT NOT NULL,
    id_materia BIGINT NOT NULL,
    id_docente BIGINT NOT NULL,
    id_studente BIGINT NOT NULL,
    FOREIGN KEY (id_materia) REFERENCES materia(id),
    FOREIGN KEY (id_docente) REFERENCES docente(id),
    FOREIGN KEY (id_studente) REFERENCES studente(id)
);

-- Dati di prova

-- Materie
INSERT INTO materia (nome) VALUES
('Programmazione Java'),
('Database'),
('Sistemi Operativi');

-- Docenti
INSERT INTO docente (nome, cognome) VALUES
('Luca', 'Rossi'),
('Marco', 'Bianchi'),
('Elena', 'Verdi');

-- Studenti
INSERT INTO studente (nome, cognome) VALUES
('Giulia', 'Neri'),
('Andrea', 'Ferrari'),
('Sara', 'Russo');

-- Esami
INSERT INTO esame (data, voto, id_materia, id_docente, id_studente) VALUES
('2025-05-10', 28, 1, 1, 1),
('2025-05-11', 30, 2, 2, 2),
('2025-05-12', 27, 3, 3, 3);
```

---

### ✅ Come usarlo

1. Apri **MySQL Workbench**, **DBeaver** o terminale.
2. Esegui lo script sul tuo server MySQL:

   ```bash
   mysql -u ITS_2 -p < init_esamidb.sql
   ```
3. Assicurati che `spring.datasource.url` punti a `esamidb`.

---

## Puoi far eseguire a Spring le istruzioni DDL e DML

---

## ✅ 1. File `schema.sql`

Per creare le tabelle:

```sql
CREATE TABLE IF NOT EXISTS materia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS docente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS studente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS esame (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    voto INT NOT NULL,
    id_materia BIGINT NOT NULL,
    id_docente BIGINT NOT NULL,
    id_studente BIGINT NOT NULL,
    FOREIGN KEY (id_materia) REFERENCES materia(id),
    FOREIGN KEY (id_docente) REFERENCES docente(id),
    FOREIGN KEY (id_studente) REFERENCES studente(id)
);
```

---

## ✅ 2. File `data.sql`

Per popolare le tabelle con dati di esempio:

```sql
-- Materie
INSERT INTO materia (nome) VALUES
('Programmazione Java'),
('Database'),
('Sistemi Operativi');

-- Docenti
INSERT INTO docente (nome, cognome) VALUES
('Luca', 'Rossi'),
('Marco', 'Bianchi'),
('Elena', 'Verdi');

-- Studenti
INSERT INTO studente (nome, cognome) VALUES
('Giulia', 'Neri'),
('Andrea', 'Ferrari'),
('Sara', 'Russo');

-- Esami
INSERT INTO esame (data, voto, id_materia, id_docente, id_studente) VALUES
('2025-05-10', 28, 1, 1, 1),
('2025-05-11', 30, 2, 2, 2),
('2025-05-12', 27, 3, 3, 3);
```

---

## ✅ 3. Modifica `application.properties`

```properties
# Connessione MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/esamidb
spring.datasource.username=ITS_2
spring.datasource.password=ITS_2

# Driver e JPA
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Inizializzazione automatica con schema.sql e data.sql
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

---

## 📁 Dove posizionare i file

Metti entrambi i file in:

```
src/main/resources/schema.sql  
src/main/resources/data.sql
```
