# Application properties per i database

---

Per collegare un database a un'applicazione Spring Boot, è necessario configurare alcune proprietà nel file `application.properties` o `application.yml`. Ecco le principali proprietà di configurazione per collegare un database:

### Proprietà per il collegamento al Database

1. **DataSource URL**
   - **Descrizione**: URL di connessione al database.
   - **Esempio (H2 Database)**:

     ```properties
     spring.datasource.url=jdbc:h2:mem:testdb
     ```

2. **DataSource Username**
   - **Descrizione**: Nome utente per accedere al database.
   - **Esempio**:

     ```properties
     spring.datasource.username=yourUsername
     ```

3. **DataSource Password**
   - **Descrizione**: Password per accedere al database.
   - **Esempio**:

     ```properties
     spring.datasource.password=yourPassword
     ```

4. **DataSource Driver Class Name**
   - **Descrizione**: Classe del driver JDBC per il database.
   - **Esempio (H2 Database)**:

     ```properties
     spring.datasource.driver-class-name=org.h2.Driver
     ```

### Configurazioni Opzionali

5. **Hibernate DDL Auto**
   - **Descrizione**: Modalità di gestione dello schema del database da parte di Hibernate.
   - **Esempio**:

     ```properties
     spring.jpa.hibernate.ddl-auto=update
     ```

6. **Hibernate Dialect**
   - **Descrizione**: Dialetto Hibernate per il database specificato.
   - **Esempio (H2 Database)**:

     ```properties
     spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
     ```

7. **Pooling DataSource**
   - **Descrizione**: Configurazione per un pool di connessioni al database.
   - **Esempio (HikariCP)**:

     ```properties
     spring.datasource.hikari.connection-timeout=20000
     spring.datasource.hikari.maximum-pool-size=5
     ```

### Esempio Completo di Configurazione (H2 Database)

```properties
# DataSource configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
# Hibernate configuration
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
# HikariCP configuration (connection pooling)
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.maximum-pool-size=5
```

Queste proprietà possono variare a seconda del tipo di database (MySQL, PostgreSQL, Oracle, etc.) e del driver JDBC utilizzato. È importante configurare correttamente le proprietà per consentire a Spring Boot di connettersi al database correttamente e di utilizzare Hibernate per la gestione dell'ORM e dello schema del database.

---

## spring.jpa.hibernate.ddl-auto

La proprietà `spring.jpa.hibernate.ddl-auto` è utilizzata in applicazioni Spring Boot per controllare il comportamento di Hibernate nella gestione del database relativo alle operazioni di DDL (Data Definition Language), come la creazione, l'aggiornamento o la validazione dello schema del database. Questa proprietà è particolarmente utile durante lo sviluppo dell'applicazione e può essere configurata nel file `application.properties` o `application.yml`.

### Opzioni di Configurazione

Ecco le opzioni principali per `spring.jpa.hibernate.ddl-auto`:

1. **none**
   - **Descrizione**: Hibernate non esegue operazioni DDL automaticamente.
   - **Utilizzo tipico**: Utile in produzione dove lo schema del database è gestito manualmente e non deve essere modificato da Hibernate.

2. **create**
   - **Descrizione**: Hibernate elimina e ricrea lo schema del database ad ogni avvio dell'applicazione.
   - **Utilizzo tipico**: Sviluppo iniziale dell'applicazione dove si desidera che Hibernate crei lo schema del database automaticamente.

3. **update**
   - **Descrizione**: Hibernate aggiorna lo schema del database in base alle modifiche nelle entità (tabelle) del modello dati.
   - **Utilizzo tipico**: Sviluppo in cui è necessario modificare lo schema del database durante lo sviluppo dell'applicazione senza eliminare i dati esistenti.

4. **validate**
   - **Descrizione**: Hibernate valida lo schema del database con quello definito nelle entità, ma non apporta modifiche allo schema del database.
   - **Utilizzo tipico**: Produzione, dove lo schema del database è già stato creato e non deve essere modificato automaticamente da Hibernate.

5. **create-drop**
   - **Descrizione**: Hibernate crea lo schema del database al momento dell'avvio e lo elimina al momento della chiusura dell'applicazione.
   - **Utilizzo tipico**: Sviluppo o test in cui è necessario creare e eliminare il database frequentemente.

### Configurazione in application.properties

Ecco come è possibile configurare `spring.jpa.hibernate.ddl-auto` nel file `application.properties`:

```properties
# Hibernate non esegue operazioni DDL automaticamente
spring.jpa.hibernate.ddl-auto=none

# Hibernate elimina e ricrea lo schema del database ad ogni avvio
spring.jpa.hibernate.ddl-auto=create

# Hibernate aggiorna lo schema del database in base alle modifiche nelle entità
spring.jpa.hibernate.ddl-auto=update

# Hibernate valida lo schema del database con quello definito nelle entità
spring.jpa.hibernate.ddl-auto=validate

# Hibernate crea e poi elimina lo schema del database ad ogni avvio e chiusura dell'applicazione
spring.jpa.hibernate.ddl-auto=create-drop
```

### Considerazioni aggiuntive

- **Ambiente di Sviluppo e Produzione**: Le opzioni `create` e `update` sono comunemente utilizzate durante lo sviluppo per facilitare la gestione dello schema del database. Tuttavia, in produzione, è consigliabile configurare `spring.jpa.hibernate.ddl-auto=validate` o `spring.jpa.hibernate.ddl-auto=none` per evitare modifiche non controllate allo schema del database.

- **Script SQL Personalizzati**: Se è necessario eseguire script SQL personalizzati per la creazione o l'aggiornamento dello schema, è possibile utilizzare il meccanismo di inizializzazione SQL di Spring Boot (`spring.sql.init.*`) insieme alla configurazione di Hibernate.

Questo permette di gestire efficacemente la creazione e l'aggiornamento dello schema del database nelle applicazioni Spring Boot in base alle esigenze specifiche dell'ambiente di sviluppo, test o produzione.

---

## **spring.sql.init.mode**

L'istruzione `spring.sql.init.mode=embedded` nel file `application.properties` configura il modo in cui Spring Boot inizializza e gestisce gli script SQL al momento dell'avvio dell'applicazione.

### Spiegazione dettagliata

1. **spring.sql.init.mode**: Questa proprietà definisce la modalità di inizializzazione degli script SQL. Nel caso specifico:

2. **embedded**: Indica che gli script SQL saranno eseguiti in modalità embedded, cioè all'interno dell'applicazione stessa.

### Cosa significa "embedded" in questo contesto?

- Quando si imposta `spring.sql.init.mode=embedded`, Spring Boot cerca automaticamente file di script SQL (come `schema.sql`, `data.sql`, ecc.) all'interno delle risorse dell'applicazione (ad esempio, nella cartella `src/main/resources` del progetto).

- Durante il processo di avvio dell'applicazione, Spring Boot eseguirà automaticamente questi script SQL embedded per inizializzare o aggiornare lo schema del database e/o inserire dati iniziali.

### Vantaggi dell'embedded mode

- **Semplicità di configurazione**: Non è necessario configurare manualmente connessioni a database esterni o altri servizi per l'inizializzazione dei dati.
  
- **Portabilità**: Gli script SQL sono incorporati nell'applicazione stessa, rendendo l'applicazione più portatile e meno dipendente da risorse esterne.

- **Facilità di sviluppo**: Facilita lo sviluppo locale e il testing dell'applicazione senza la necessità di un database esterno completamente configurato.

### Considerazioni aggiuntive

- Se si utilizzano script SQL personalizzati con nomi diversi o in posizioni diverse dalle impostazioni predefinite, è possibile configurare manualmente la posizione e il nome degli script usando altre proprietà di configurazione di Spring Boot, come ad esempio `spring.sql.init.schema-locations`.

- È importante tenere presente che l'uso di `embedded` mode può essere limitato in applicazioni che richiedono configurazioni più complesse o l'utilizzo di più database.

In sintesi, `spring.sql.init.mode=embedded` configura Spring Boot per eseguire script SQL incorporati durante l'avvio dell'applicazione, facilitando l'inizializzazione e la gestione del database senza dipendenze esterne complesse.

---

Spring Boot offre diverse modalità per l'inizializzazione degli script SQL attraverso la proprietà `spring.sql.init.mode`. Oltre alla modalità `embedded`, esistono altre opzioni che è possibile utilizzare a seconda delle esigenze dell'applicazione. Ecco un elenco delle principali modalità disponibili:

### Modalità di Inizializzazione degli Script SQL in Spring Boot

1. **embedded**
   - **Descrizione**: Esegue gli script SQL incorporati nell'applicazione durante il processo di avvio.
   - **Uso tipico**: Semplicità di sviluppo locale e testing senza dipendenze esterne.

2. **always**
   - **Descrizione**: Esegue gli script SQL ogni volta che l'applicazione viene avviata.
   - **Uso tipico**: Utile in ambienti di sviluppo e test per garantire che il database sia sempre aggiornato con lo schema più recente.

3. **never**
   - **Descrizione**: Non esegue gli script SQL durante l'avvio dell'applicazione.
   - **Uso tipico**: Utilizzato in produzione quando la gestione dello schema del database è gestita manualmente o tramite altri processi automatizzati.

4. **fallback**
   - **Descrizione**: Esegue gli script SQL solo se non esiste uno schema nel database. È una modalità di fallback nel caso in cui l'applicazione debba gestire sia la creazione iniziale che l'aggiornamento dello schema.
   - **Uso tipico**: Applicazioni che devono supportare sia l'installazione iniziale che gli aggiornamenti successivi dello schema del database.

### Configurazione in application.properties

Ecco come è possibile configurare queste modalità nel file `application.properties`:

```properties
# Esegue gli script SQL incorporati nell'applicazione durante l'avvio
spring.sql.init.mode=embedded

# Esegue gli script SQL ogni volta che l'applicazione viene avviata
spring.sql.init.mode=always

# Non esegue gli script SQL durante l'avvio dell'applicazione
spring.sql.init.mode=never

# Esegue gli script SQL solo se non esiste uno schema nel database
spring.sql.init.mode=fallback
```

### Considerazioni aggiuntive

- **Personalizzazione degli Script**: È possibile specificare i nomi e le posizioni degli script SQL utilizzando altre proprietà come `spring.sql.init.schema-locations` se gli script non si trovano nelle posizioni predefinite.

- **Multi-database**: Se si sta utilizzando più di un database, è possibile configurare modalità diverse per ciascuno dei database utilizzando le rispettive proprietà di configurazione.

- **Ambienti di Sviluppo e Produzione**: Le modalità possono essere adattate in base agli ambienti di sviluppo, test e produzione per ottimizzare la gestione e l'inizializzazione dello schema del database.

Queste opzioni offrono flessibilità nella gestione degli script SQL nell'ambiente di sviluppo e di produzione delle applicazioni Spring Boot.

---

