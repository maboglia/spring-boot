# 2.2. Configurare la Sicurezza HTTP

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

---

Questo codice configura la sicurezza per un'applicazione web utilizzando Spring Security. Vediamo passo per passo cosa fa ogni parte del codice.

### Definizione del Metodo e Disabilitazione di CSRF

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
```

- Il metodo `filterChain` è annotato con `@Bean`, il che significa che Spring gestirà il ciclo di vita di questo bean e lo renderà disponibile per l'iniezione in altre parti dell'applicazione.
- Viene passato un oggetto `HttpSecurity` che permette di configurare la sicurezza HTTP.
- La protezione CSRF (Cross-Site Request Forgery) viene disabilitata. Questo potrebbe essere necessario per alcune API RESTful, specialmente se non utilizzano sessioni basate su cookie.

### Configurazione delle Regole di Autorizzazione

```java
.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
    authorizationManagerRequestMatcherRegistry
        .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
        .requestMatchers("/login/**").permitAll()
        .anyRequest().authenticated())
```

- Si inizia a configurare le regole di autorizzazione per le richieste HTTP.
- `requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")`: Solo gli utenti con il ruolo `ADMIN` possono effettuare richieste DELETE.
- `requestMatchers("/admin/**").hasAnyRole("ADMIN")`: Solo gli utenti con il ruolo `ADMIN` possono accedere a URL che iniziano con `/admin/`.
- `requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")`: Gli utenti con i ruoli `USER` o `ADMIN` possono accedere a URL che iniziano con `/user/`.
- `requestMatchers("/login/**").permitAll()`: Tutti possono accedere ai URL che iniziano con `/login/`, senza autenticazione.
- `anyRequest().authenticated()`: Qualsiasi altra richiesta deve essere autenticata.

### Configurazione dell'Autenticazione HTTP Basic

```java
.httpBasic(Customizer.withDefaults())
```

- Viene configurata l'autenticazione HTTP Basic, che richiede un nome utente e una password per ogni richiesta. `Customizer.withDefaults()` utilizza la configurazione predefinita di Spring Security per HTTP Basic.

### Configurazione della Gestione della Sessione

```java
.sessionManagement(httpSecuritySessionManagementConfigurer -> 
    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

- La gestione della sessione viene configurata per essere senza stato (`STATELESS`), il che significa che non vengono create sessioni server-side. Questo è tipico per le API RESTful dove ogni richiesta contiene tutte le informazioni necessarie per l'autenticazione e l'autorizzazione (ad esempio, utilizzando token JWT).

### Restituzione della Configurazione di Sicurezza

```java
return http.build();
}
```

- Alla fine, la configurazione di sicurezza viene costruita e restituita come un bean `SecurityFilterChain`.

### Riassunto

Questo metodo configura un filtro di sicurezza per un'applicazione web con Spring Security, impostando le seguenti regole:

- Disabilita la protezione CSRF.
- Definisce regole di autorizzazione per diversi endpoint e metodi HTTP.
- Abilita l'autenticazione HTTP Basic.
- Configura la gestione delle sessioni per essere senza stato.

Questo tipo di configurazione è tipico per applicazioni RESTful dove le sessioni server-side non sono utilizzate e ogni richiesta deve essere autenticata individualmente.
