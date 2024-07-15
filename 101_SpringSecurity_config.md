### Introduzione alla Configurazione Java per Spring Security

- [link ref](https://www.baeldung.com/java-config-spring-security)

#### 1. Panoramica

Questo articolo è un'introduzione alla configurazione Java per Spring Security, che permette agli utenti di configurare facilmente Spring Security senza l'uso di XML. La configurazione Java è stata aggiunta al framework Spring in Spring 3.1 ed estesa a Spring Security in Spring 3.2, ed è definita in una classe annotata con `@Configuration`.

#### 2. Configurazione Maven

Per utilizzare Spring Security in un progetto Maven, dobbiamo prima aggiungere la dipendenza `spring-security-core` nel file `pom.xml` del progetto:

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-core</artifactId>
    <version>5.3.3.RELEASE</version>
</dependency>
```

L'ultima versione può essere trovata sempre qui.

#### 3. Sicurezza Web con Configurazione Java

Iniziamo con un esempio base di configurazione Java per Spring Security:

```java
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
      throws Exception {
        auth.inMemoryAuthentication().withUser("user")
          .password(passwordEncoder().encode("password")).roles("USER");
    }
}
```

Come avrete notato, la configurazione imposta una configurazione di autenticazione base in memoria. Inoltre, a partire da Spring 5, abbiamo bisogno di un bean `PasswordEncoder`:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

#### 4. Sicurezza HTTP

Per abilitare la Sicurezza HTTP in Spring, dobbiamo creare un bean `SecurityFilterChain`:

```java
@Bean 
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .anyRequest().authenticated()
      .and().httpBasic();
    return http.build();
}
```

La configurazione sopra assicura che qualsiasi richiesta all'applicazione sia autenticata tramite login basato su form o autenticazione HTTP basic. Inoltre, è esattamente simile alla seguente configurazione XML:

```xml
<http>
    <intercept-url pattern="/**" access="isAuthenticated()"/>
    <form-login />
    <http-basic />
</http>
```

#### 5. Login con Form

Interessantemente, Spring Security genera automaticamente una pagina di login basata sulle funzionalità abilitate e utilizzando valori standard per l'URL che elabora il login inviato:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .anyRequest().authenticated()
      .and().formLogin()
      .loginPage("/login").permitAll();
    return http.build();
}
```

Qui, la pagina di login generata automaticamente è comoda per iniziare rapidamente.

#### 6. Autorizzazione con Ruoli

Configuriamo ora alcune semplici autorizzazioni su ogni URL utilizzando i ruoli:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers("/", "/home").access("hasRole('USER')")
      .antMatchers("/admin/**").hasRole("ADMIN")
      .and()
      // altre chiamate a metodi
      .formLogin();
    return http.build();
}
```

Notate come stiamo utilizzando sia l'API type-safe – `hasRole` – sia l'API basata su espressioni, tramite `access`.

#### 7. Logout

Come molti altri aspetti di Spring Security, il logout ha alcune ottime impostazioni predefinite fornite dal framework. Per impostazione predefinita, una richiesta di logout invalida la sessione, cancella tutte le cache di autenticazione, cancella il `SecurityContextHolder` e reindirizza alla pagina di login. Ecco una semplice configurazione di logout:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.logout();
    return http.build();
}
```

Tuttavia, se si desidera avere un maggiore controllo sui gestori disponibili, ecco come potrebbe apparire una implementazione più completa:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.logout().logoutUrl("/my/logout")
      .logoutSuccessUrl("/my/index")
      .logoutSuccessHandler(logoutSuccessHandler) 
      .invalidateHttpSession(true)
      .addLogoutHandler(logoutHandler)
      .deleteCookies(cookieNamesToClear)
      .and()
      // altre chiamate a metodi
    return http.build();
}
```

#### 8. Autenticazione

Vediamo un altro modo per permettere l'autenticazione con Spring Security.

##### 8.1. Autenticazione in Memoria

Iniziamo con una semplice configurazione in memoria:

```java
@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) 
  throws Exception {
    auth.inMemoryAuthentication()
      .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
      .and()
      .withUser("admin").password(passwordEncoder().encode("password")).roles("USER", "ADMIN");
}
```

##### 8.2. Autenticazione JDBC

Per passare a JDBC, tutto ciò che devi fare è definire un data source all'interno dell'applicazione e usarlo direttamente:

```java
@Autowired
private DataSource dataSource;

@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) 
  throws Exception {
    auth.jdbcAuthentication().dataSource(dataSource)
      .withDefaultSchema()
      .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
      .and()
      .withUser("admin").password(passwordEncoder().encode("password")).roles("USER", "ADMIN");
}
```

Naturalmente, con entrambi gli esempi sopra, dobbiamo anche definire il bean `PasswordEncoder` come descritto nella Sezione 3.

#### 9. Conclusione

In questo rapido tutorial, abbiamo esaminato le basi della configurazione Java per Spring Security e ci siamo concentrati su esempi di codice che illustrano gli scenari di configurazione più semplici.