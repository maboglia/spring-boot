# 🔍 **Cos’è H2 Database?**

H2 è un **database relazionale leggero, in-memory o file-based**, scritto in Java, ideale per:

* **Test e sviluppo**
* Progetti **embedded**
* Simulazione di comportamenti reali senza dipendere da un DB esterno

Può funzionare in due modalità:

1. **In-Memory** (i dati si perdono al riavvio)
2. **Persistente su file** (i dati si salvano su disco)

---

## ⚙️ **Configurazione in `application.properties`**

### ✅ **1. Database In-Memory**

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.platform=h2
spring.h2.console.enabled=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

* `jdbc:h2:mem:testdb` → database solo in memoria, nome `testdb`
* `spring.h2.console.enabled=true` → abilita la **console web H2**
* `spring.jpa.hibernate.ddl-auto=update` → crea/aggiorna automaticamente le tabelle

---

### ✅ **2. Database Persistente su File**

```properties
spring.datasource.url=jdbc:h2:file:./data/dbdemo
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

* I file `.mv.db` e `.trace.db` verranno salvati nella directory `./data` con nome `dbdemo`.

---

## 🖥️ **H2 Console Web**

Quando `spring.h2.console.enabled=true`, Spring Boot abilita una **console web interattiva** per interrogare e gestire il DB.

### ✳️ **Accesso:**

Vai a:

```
http://localhost:8080/h2-console
```

### ✳️ **Impostazioni per accedere:**

* **JDBC URL:** deve combaciare con il valore in `application.properties`
  Es: `jdbc:h2:mem:testdb` o `jdbc:h2:file:./data/dbdemo`
* **Username:** `sa` (default)
* **Password:** vuota (di default)

---

## 🔄 **DDL Auto (Creazione Tabelle)**

```properties
spring.jpa.hibernate.ddl-auto=update
```

| Valore        | Descrizione                                                                 |
| ------------- | --------------------------------------------------------------------------- |
| `none`        | Non fa nulla                                                                |
| `create`      | Crea lo schema ogni volta, **cancella tutto**                               |
| `update`      | Aggiorna lo schema senza perdere i dati                                     |
| `create-drop` | Come `create` ma elimina il DB alla chiusura dell’app                       |
| `validate`    | Controlla che lo schema esista e sia compatibile, ma **non modifica nulla** |

---

## 🧪 **Script SQL (opzionale)**

Spring Boot esegue **automaticamente script SQL** se presenti in `src/main/resources`:

### ⚙️ `schema.sql`

Contiene **DDL** per creare le tabelle.

### ⚙️ `data.sql`

Contiene **DML** per inserire dati iniziali.

Esempio:

```sql
-- schema.sql
CREATE TABLE utenti (
  id BIGINT PRIMARY KEY,
  nome VARCHAR(100)
);

-- data.sql
INSERT INTO utenti (id, nome) VALUES (1, 'Mario');
```

---

## ✅ **Vantaggi di H2 in Spring Boot**

* **Zero setup** per lo sviluppo
* Console web molto comoda
* Ottimo per **test automatici**
* Può essere sostituito facilmente con MySQL, PostgreSQL ecc. solo cambiando `application.properties`

---

## 🧩 Esempio completo

```properties
spring.datasource.url=jdbc:h2:file:./data/myappdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

