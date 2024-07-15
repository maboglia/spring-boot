# Spring Security: Upgrading the Deprecated WebSecurityConfigurerAdapter

*Last updated: January 15, 2024*

*Written by: baeldung Reviewed by: Eric Martin*

- [article](https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter)
- [github code](https://github.com/eugenp/tutorials/tree/master/spring-security-modules/spring-security-web-boot-4)

### Panoramica

Spring Security consente di personalizzare la sicurezza HTTP per funzionalità come l'autorizzazione degli endpoint o la configurazione del gestore di autenticazione, estendendo una classe WebSecurityConfigurerAdapter. Tuttavia, nelle versioni recenti, Spring depreca questo approccio e incoraggia una configurazione della sicurezza basata su componenti.
In questo tutorial, impareremo come possiamo sostituire questa deprecazione in un'applicazione Spring Boot ed eseguire alcuni test MVC.

### 2. Spring Security Senza WebSecurityConfigurerAdapter

Comunemente vediamo classi di configurazione della sicurezza HTTP di Spring che estendono una classe WebSecurityConfigurerAdapter. Tuttavia, a partire dalla versione 5.7.0-M2, Spring depreca l'uso di WebSecurityConfigurerAdapter e suggerisce di creare configurazioni senza di essa.

Creiamo un esempio di applicazione Spring Boot utilizzando l'autenticazione in memoria per mostrare questo nuovo tipo di configurazione.

Per prima cosa, definiremo la nostra classe di configurazione:

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    // configurazione

}
```

Aggiungeremo annotazioni di sicurezza sui metodi per abilitare l'elaborazione basata su diversi ruoli.

### 2.1. Configurare l'Autenticazione

Con il WebSecurityConfigurerAdapter, utilizzeremmo un AuthenticationManagerBuilder per impostare il nostro contesto di autenticazione. Ora, se vogliamo evitare la deprecazione, possiamo definire un componente UserDetailsManager o UserDetailsService:

```java
@Bean
public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(User.withUsername("user")
      .password(bCryptPasswordEncoder.encode("userPass"))
      .roles("USER")
      .build());
    manager.createUser(User.withUsername("admin")
      .password(bCryptPasswordEncoder.encode("adminPass"))
      .roles("USER", "ADMIN")
      .build());
    return manager;
}
```

Oppure, dato il nostro `UserDetailsService`, possiamo anche impostare un `AuthenticationManager`:

```java
@Bean
public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) 
  throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
      .userDetailsService(userDetailsService)
      .passwordEncoder(bCryptPasswordEncoder)
      .and()
      .build();
}
```

Allo stesso modo, questo funzionerà se utilizziamo l'autenticazione **JDBC** o **LDAP**.

### 2.2. Configurare la Sicurezza HTTP

Più importante, se vogliamo evitare la deprecazione per la sicurezza HTTP, possiamo creare un bean `SecurityFilterChain`. Ad esempio, supponiamo di voler proteggere gli endpoint a seconda dei ruoli, lasciando un punto di ingresso anonimo solo per il login. Restringeremo anche qualsiasi richiesta di eliminazione al ruolo di amministratore. Utilizzeremo l'**Autenticazione Basic**:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
              authorizationManagerRequestMatcherRegistry
                      .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                      .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                      .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                      .requestMatchers("/login/**").permitAll()
                      .anyRequest().authenticated())
      .httpBasic(Customizer.withDefaults())
      .sessionManagement(httpSecuritySessionManagementConfigurer -> 
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
}
```

La sicurezza HTTP costruirà un oggetto `DefaultSecurityFilterChain` per caricare i request matchers e i filtri.

[Spiegazione](./104_spring_secuirty_http.md)

### 2.3. Configurare la Sicurezza Web

Per la sicurezza Web, ora possiamo utilizzare l'interfaccia di callback `WebSecurityCustomizer`. Aggiungeremo un livello di debug e ignoreremo alcuni percorsi, come immagini o script:

```java
@Bean
public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.debug(securityDebug)
                    .ignoring()
                    .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
}
```

In questo modo, configuriamo la sicurezza Web per ignorare i percorsi specificati, consentendo di caricare le risorse statiche senza richiedere autenticazione.


### 3. Endpoints Controller

Ora definiremo una semplice classe controller REST per la nostra applicazione:

```java
@RestController
public class ResourceController {

    @GetMapping("/login")
    public String loginEndpoint() {
        return "Login!";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Admin!";
    }

    @GetMapping("/user")
    public String userEndpoint() {
        return "User!";
    }

    @GetMapping("/all")
    public String allRolesEndpoint() {
        return "All Roles!";
    }

    @DeleteMapping("/delete")
    public String deleteEndpoint(@RequestBody String s) {
        return "I am deleting " + s;
    }
}
```

Come abbiamo menzionato in precedenza durante la definizione della sicurezza HTTP, aggiungeremo un endpoint generico `/login` accessibile da chiunque, endpoint specifici per admin e user, e un endpoint `/all` non protetto da un ruolo specifico ma che richiede comunque l'autenticazione.

### 4. Testare gli Endpoints

Aggiungiamo la nostra nuova configurazione a un test Spring Boot utilizzando un MVC mock per testare i nostri endpoint.

#### 4.1. Testare gli Utenti Anonimi

Gli utenti anonimi possono accedere all'endpoint `/login`. Se provano ad accedere a qualcos'altro, non saranno autorizzati (401):

```java
@Test
@WithAnonymousUser
public void whenAnonymousAccessLogin_thenOk() throws Exception {
    mvc.perform(get("/login"))
      .andExpect(status().isOk());
}

@Test
@WithAnonymousUser
public void whenAnonymousAccessRestrictedEndpoint_thenIsUnauthorized() throws Exception {
    mvc.perform(get("/all"))
      .andExpect(status().isUnauthorized());
}
```

Inoltre, per tutti gli endpoint tranne `/login`, richiediamo sempre l'autenticazione, come per l'endpoint `/all`.

#### 4.2. Testare il Ruolo di Utente

Un ruolo di utente può accedere agli endpoint generici e a tutti gli altri percorsi che abbiamo concesso per questo ruolo:

```java
@Test
@WithUserDetails()
public void whenUserAccessUserSecuredEndpoint_thenOk() throws Exception {
    mvc.perform(get("/user"))
      .andExpect(status().isOk());
}

@Test
@WithUserDetails()
public void whenUserAccessRestrictedEndpoint_thenOk() throws Exception {
    mvc.perform(get("/all"))
      .andExpect(status().isOk());
}

@Test
@WithUserDetails()
public void whenUserAccessAdminSecuredEndpoint_thenIsForbidden() throws Exception {
    mvc.perform(get("/admin"))
      .andExpect(status().isForbidden());
}

@Test
@WithUserDetails()
public void whenUserAccessDeleteSecuredEndpoint_thenIsForbidden() throws Exception {
    mvc.perform(delete("/delete"))
      .andExpect(status().isForbidden());
}
```

È importante notare che se un utente tenta di accedere a un endpoint protetto per admin, l'utente riceverà un errore "forbidden" (403). Al contrario, qualcuno senza credenziali, come un anonimo nell'esempio precedente, otterrà un errore "unauthorized" (401).

#### 4.3. Testare il Ruolo di Admin

Come possiamo vedere, qualcuno con un ruolo di admin può accedere a qualsiasi endpoint:

```java
@Test
@WithUserDetails(value = "admin")
public void whenAdminAccessUserEndpoint_thenOk() throws Exception {
    mvc.perform(get("/user"))
      .andExpect(status().isOk());
}

@Test
@WithUserDetails(value = "admin")
public void whenAdminAccessAdminSecuredEndpoint_thenIsOk() throws Exception {
    mvc.perform(get("/admin"))
      .andExpect(status().isOk());
}

@Test
@WithUserDetails(value = "admin")
public void whenAdminAccessDeleteSecuredEndpoint_thenIsOk() throws Exception {
    mvc.perform(delete("/delete").content("{}"))
      .andExpect(status().isOk());
}
```

In questo modo, possiamo verificare che il ruolo di admin abbia l'accesso previsto agli endpoint specificati.



### 5. Conclusione

In questo articolo, abbiamo imparato a creare una configurazione di Spring Security senza utilizzare `WebSecurityConfigureAdapter` e a sostituirla creando componenti per l'autenticazione, la sicurezza HTTP e la sicurezza Web.
