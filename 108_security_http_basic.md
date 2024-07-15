# Http Basic (NON sicura!)

HTTP Basic è uno dei metodi di autenticazione più semplici e fondamentali supportati dal protocollo HTTP. È stato definito nella specifica HTTP/1.0 come un modo per accedere a risorse protette su un server utilizzando una coppia di credenziali (solitamente username e password).

---

### Funzionamento di HTTP Basic Authentication

1. **Richiesta di Accesso Protetta**
   - Quando un client HTTP (come un browser o un'applicazione) tenta di accedere a una risorsa protetta su un server che utilizza l'autenticazione HTTP Basic, il server risponde con uno stato di "Unauthorized" (401) insieme a un'intestazione `WWW-Authenticate`.

2. **Credenziali Utente**
   - Il client deve fornire le credenziali dell'utente (username e password) nel formato Base64 codificato all'interno dell'intestazione `Authorization` nella richiesta HTTP successiva.

3. **Intestazione `Authorization`**
   - L'intestazione `Authorization` ha la seguente struttura:

     ```
     Authorization: Basic <base64-encoded-credentials>
     ```

     Dove `<base64-encoded-credentials>` è il valore Base64 delle credenziali `username:password`.

4. **Decodifica delle Credenziali**
   - Il server decodifica le credenziali ricevute e verifica se corrispondono a un utente valido nel sistema.

5. **Accesso Consentito o Rifiutato**
   - Se le credenziali sono valide, il server consente l'accesso alla risorsa protetta. In caso contrario, il server risponde con uno stato di "Forbidden" (403) o un altro stato di errore appropriato.

---

### Utilizzo di HTTP Basic in Spring Security

In un'applicazione Spring Boot, l'autenticazione HTTP Basic può essere configurata facilmente utilizzando Spring Security. Ecco un esempio di configurazione in un'applicazione Spring Boot:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Utilizzato solo per scopi didattici o di test, NON utilizzare in produzione
        return NoOpPasswordEncoder.getInstance();
    }
}
```

---

### Considerazioni Importanti

- **Sicurezza**: L'autenticazione HTTP Basic è vulnerabile agli attacchi di tipo "Man-in-the-Middle" a meno che non sia utilizzato in congiunzione con HTTPS (HTTP Secure).
  
- **Base64 Encoding**: Anche se le credenziali sono codificate in Base64, questo non costituisce un meccanismo di crittografia sicuro. Non dovrebbero essere inviate password sensibili senza l'utilizzo di HTTPS.

- **Usabilità**: HTTP Basic è utile per scenari semplici di autenticazione, ma per applicazioni più complesse è consigliabile considerare meccanismi più sicuri e flessibili come OAuth 2.0.

In sintesi, HTTP Basic è un metodo semplice e comune per proteggere le risorse su un server utilizzando un sistema di autenticazione basato su username e password. Tuttavia, è fondamentale utilizzarlo in modo sicuro e considerare alternative più robuste per applicazioni sensibili.

---

## Autenticazione HTTP Basic senza estendere direttamente `WebSecurityConfigurerAdapter`

[Deprecato in spring 5.7,vedi qui](./100_SpringSecurity.md)

Se desideri configurare l'autenticazione HTTP Basic senza estendere direttamente `WebSecurityConfigurerAdapter`, è possibile configurarla utilizzando le funzionalità di configurazione di base fornite da Spring Security. Ecco come puoi farlo:

### Configurazione di base senza WebSecurityConfigurerAdapter

1. **Aggiungi le dipendenze necessarie**

   Assicurati di avere le dipendenze di Spring Security nel tuo progetto. Se stai usando Maven, puoi aggiungerle nel tuo file `pom.xml`:

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-security</artifactId>
   </dependency>
   ```

2. **Configura l'autenticazione HTTP Basic**

   Puoi configurare l'autenticazione HTTP Basic direttamente in un bean di configurazione `SecurityConfigurerAdapter`. Ecco un esempio:

   ```java
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.security.config.annotation.web.builders.HttpSecurity;
   import org.springframework.security.core.userdetails.User;
   import org.springframework.security.core.userdetails.UserDetailsService;
   import org.springframework.security.crypto.password.NoOpPasswordEncoder;
   import org.springframework.security.crypto.password.PasswordEncoder;
   import org.springframework.security.provisioning.InMemoryUserDetailsManager;
   import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

   @Configuration
   public class SecurityConfig {

       @Bean
       public UserDetailsService userDetailsService() {
           // Definizione degli utenti e delle loro password
           InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
           manager.createUser(User.withUsername("user").password("password").roles("USER").build());
           manager.createUser(User.withUsername("admin").password("admin").roles("ADMIN").build());
           return manager;
       }

       @Bean
       public PasswordEncoder passwordEncoder() {
           // Utilizzato solo per scopi didattici o di test, NON utilizzare in produzione
           return NoOpPasswordEncoder.getInstance();
       }

       @Bean
       public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
           BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
           entryPoint.setRealmName("My Realm");
           return entryPoint;
       }

       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
               .authorizeRequests()
                   .anyRequest().authenticated()
                   .and()
               .httpBasic()
                   .authenticationEntryPoint(basicAuthenticationEntryPoint());
   
           return http.build();
       }
   }
   ```

   - **`userDetailsService()`**: Definisce gli utenti e le loro credenziali. In questo esempio, gli utenti `user` e `admin` sono definiti con le rispettive password e ruoli.

   - **`passwordEncoder()`**: Configura un `PasswordEncoder`. Nel caso di esempio, viene utilizzato `NoOpPasswordEncoder` per scopi didattici, ma in un ambiente di produzione è consigliabile utilizzare un `PasswordEncoder` sicuro.

   - **`basicAuthenticationEntryPoint()`**: Configura un `BasicAuthenticationEntryPoint`, che definisce il nome del realm per l'autenticazione HTTP Basic.

   - **`securityFilterChain()`**: Configura la catena di filtri di sicurezza HTTP. Qui viene configurato `httpBasic()` per abilitare l'autenticazione HTTP Basic con il punto di ingresso specificato.

Questo approccio configura l'autenticazione HTTP Basic senza l'uso diretto di `WebSecurityConfigurerAdapter`, utilizzando invece le funzionalità di configurazione di base fornite da Spring Security. Assicurati di personalizzare la configurazione in base alle esigenze specifiche del tuo progetto e di seguire le migliori pratiche di sicurezza per la gestione delle password e delle credenziali utente.

---

Nel contesto di Spring Boot e Spring Security, è possibile configurare le credenziali degli utenti direttamente nel file `application.properties` o `application.yml`. Questo è particolarmente utile per le configurazioni di test o per ambienti di sviluppo dove si desidera mantenere le credenziali separate dal codice sorgente.

Ecco come puoi configurare le credenziali degli utenti in `application.properties`:

### Configurazione in application.properties

Supponiamo di voler definire due utenti di esempio con ruoli diversi (`USER` e `ADMIN`).

1. **Definizione delle Credenziali**

   Aggiungi le seguenti proprietà nel tuo file `application.properties`:

   ```properties
   # Credenziali utente "user" con ruolo "USER"
   spring.security.user.name=user
   spring.security.user.password=password
   spring.security.user.roles=USER

   # Credenziali utente "admin" con ruolo "ADMIN"
   spring.security.user.name=admin
   spring.security.user.password=admin
   spring.security.user.roles=ADMIN
   ```

   In questo esempio:
   - L'utente `user` ha la password `password` e il ruolo `USER`.
   - L'utente `admin` ha la password `admin` e il ruolo `ADMIN`.

2. **Utilizzo in Configurazione Spring Security**

   Quando Spring Boot avvia l'applicazione, Spring Security automaticamente utilizza queste proprietà per configurare gli utenti e i loro ruoli. Non è necessario aggiungere ulteriori configurazioni a meno che non sia necessario personalizzare ulteriormente il comportamento di Spring Security.

3. **Attenzione**

   - **Sicurezza**: Utilizzare credenziali e password sicure anche in fase di sviluppo. Evitare di utilizzare password deboli o informazioni sensibili.

   - **Produzione**: Non utilizzare questa metodologia per le credenziali in un ambiente di produzione. È consigliabile utilizzare un meccanismo di autenticazione più sicuro, come un database sicuro o un provider di identità esterno come OAuth 2.0.

Utilizzare `application.properties` per definire le credenziali degli utenti è un modo conveniente per configurare rapidamente l'autenticazione di base senza dover scrivere codice aggiuntivo per la gestione degli utenti. Assicurati di considerare le migliori pratiche di sicurezza e di mantenere sempre aggiornate le credenziali e le configurazioni di sicurezza del tuo sistema.
