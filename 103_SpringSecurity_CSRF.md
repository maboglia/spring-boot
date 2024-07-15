# CSRF

CSRF, o Cross-Site Request Forgery, è un tipo di attacco informatico che coinvolge la contraffazione di richieste da un sito web malevolo verso un altro sito in cui l'utente è autenticato. L'attaccante sfrutta la sessione attiva dell'utente su un'applicazione web per eseguire azioni indesiderate a suo nome senza che l'utente ne sia a conoscenza.

### Come Funziona un Attacco CSRF

1. **Preparazione**:
   - Un utente visita un sito web malevolo controllato dall'attaccante.
   - Il sito web malevolo contiene un link o un modulo nascosto che punta all'applicazione web bersaglio.

2. **Esecuzione**:
   - L'utente, autenticato sull'applicazione web bersaglio (es. banca online), interagisce con il sito web malevolo.
   - Il sito malevolo invia una richiesta all'applicazione bersaglio utilizzando le credenziali dell'utente attualmente autenticato (ad esempio, cookie di sessione).

3. **Conseguenze**:
   - L'applicazione web bersaglio esegue l'azione richiesta dall'attaccante, credendo che la richiesta provenga dall'utente autenticato.
   - Questo può comportare trasferimenti di denaro, cambiamenti di impostazioni, pubblicazione di contenuti o altre azioni indesiderate.

### Esempio di Attacco CSRF

Immaginiamo un'applicazione web di banca online con una funzionalità per trasferire denaro:

```html
<form action="https://www.bancaonline.com/transfer" method="POST">
    <input type="hidden" name="toAccount" value="attackerAccount">
    <input type="hidden" name="amount" value="1000">
    <input type="submit" value="Transfer Money">
</form>
```

Se un utente autenticato su bancaonline.com visita una pagina malevola contenente questo modulo, il modulo potrebbe essere inviato automaticamente, trasferendo denaro all'account dell'attaccante.

### Protezione Contro CSRF

Per proteggersi contro attacchi CSRF, è importante implementare misure di sicurezza che verificano l'origine delle richieste. Spring Security fornisce un supporto integrato per la protezione CSRF. Ecco alcune delle tecniche comunemente usate:

1. **Token CSRF**:
   - Ogni richiesta che modifica lo stato (POST, PUT, DELETE) deve includere un token CSRF univoco e imprevedibile.
   - Questo token viene generato dal server e inviato al client (tipicamente come campo nascosto in un modulo).
   - Il server verifica la presenza e la validità del token nelle richieste in entrata.

2. **SameSite Cookie Attribute**:
   - L'attributo `SameSite` dei cookie può essere impostato per prevenire l'invio dei cookie nelle richieste cross-site.
   - `SameSite=Strict` previene l'invio del cookie in tutte le richieste cross-site.
   - `SameSite=Lax` permette l'invio dei cookie solo per alcune richieste di navigazione sicure.

### Esempio di Implementazione di CSRF in Spring Security

Spring Security rende la protezione CSRF semplice da implementare. Ecco un esempio di configurazione:

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Abilita protezione CSRF con token nel cookie
            .and()
            .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }
}
```

In questo esempio, `CookieCsrfTokenRepository` viene utilizzato per archiviare il token CSRF in un cookie. Questo token sarà verificato dal server per ogni richiesta di modifica dello stato.

### Conclusione

CSRF è una minaccia significativa per le applicazioni web, ma con le giuste misure di sicurezza, come i token CSRF e gli attributi `SameSite` dei cookie, è possibile mitigare efficacemente questo rischio. Spring Security fornisce strumenti potenti e facili da usare per proteggere le applicazioni web da attacchi CSRF, garantendo che le richieste provenienti da origini non autorizzate non possano compromettere la sicurezza delle applicazioni.
