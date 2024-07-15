# 2.3. Configurare la Sicurezza Web

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

---

Il codice fornito configura la sicurezza web in modo da ignorare determinati percorsi e abilitare il debug della sicurezza utilizzando l'interfaccia di callback `WebSecurityCustomizer`. Vediamo nel dettaglio cosa fa ogni parte del codice:

### Definizione del Metodo `webSecurityCustomizer`

```java
@Bean
public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.debug(securityDebug)
                    .ignoring()
                    .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
}
```

- Il metodo `webSecurityCustomizer` è annotato con `@Bean`, quindi Spring gestirà il ciclo di vita di questo bean e lo renderà disponibile per l'iniezione in altre parti dell'applicazione.
- Restituisce un'implementazione di `WebSecurityCustomizer` utilizzando un'espressione lambda.

### Configurazione del Debug della Sicurezza

```java
web.debug(securityDebug)
```

- Il metodo `debug` abilita o disabilita il debug della sicurezza a seconda del valore di `securityDebug`.
- `securityDebug` è presumibilmente una variabile booleana che deve essere definita altrove nel codice.
- Quando il debug della sicurezza è abilitato, vengono registrati dettagli aggiuntivi sul funzionamento del sistema di sicurezza, utili per il debug e la risoluzione dei problemi.

### Ignorare Percorsi Specifici

```java
.ignoring()
.requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
```

- Il metodo `ignoring` indica a Spring Security di ignorare determinate richieste e di non applicare alcuna misura di sicurezza a esse.
- `requestMatchers` specifica i percorsi che devono essere ignorati. In questo caso:
  - `/css/**` ignora tutti i file CSS.
  - `/js/**` ignora tutti i file JavaScript.
  - `/img/**` ignora tutte le immagini.
  - `/lib/**` ignora tutte le librerie (presumibilmente JavaScript o CSS).
  - `/favicon.ico` ignora il file della favicon.

### Perché Ignorare Certi Percorsi?

Ignorare percorsi come quelli delle risorse statiche (CSS, JS, immagini) è una pratica comune perché:

- Queste risorse non contengono informazioni sensibili.
- Non richiedono autenticazione per essere visualizzate.
- Migliora le prestazioni riducendo il carico sul sistema di autenticazione.

### Riassunto

Il metodo `webSecurityCustomizer` configura la sicurezza web per:

1. Abilitare il debug della sicurezza se `securityDebug` è vero.
2. Ignorare le richieste a percorsi specifici (CSS, JavaScript, immagini, librerie e favicon) in modo che queste risorse statiche possano essere caricate senza richiedere autenticazione.

Questa configurazione è utile per assicurarsi che le risorse statiche siano facilmente accessibili e che il sistema di sicurezza non aggiunga overhead non necessario per queste risorse.
