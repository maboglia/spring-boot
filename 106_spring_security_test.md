# 4.2. Testare il Ruolo di Utente

Un ruolo di utente può accedere agli endpoint generici e a tutti gli altri percorsi che abbiamo concesso per questo ruolo:

[MockMvc](./107_spring_mockmvc.md)

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

---

Il codice fornito è un esempio di test per verificare la sicurezza di un'applicazione web utilizzando Spring Security e JUnit. Vediamo nel dettaglio ogni parte del codice e il suo significato.

### Annotazioni e Setup

```java
@Test
@WithUserDetails()
public void whenUserAccessUserSecuredEndpoint_thenOk() throws Exception {
    mvc.perform(get("/user"))
      .andExpect(status().isOk());
}
```

- `@Test`: Indica che il metodo è un test JUnit.
- `@WithUserDetails()`: Configura il contesto di sicurezza per il test, impostando un utente con dettagli predefiniti. Se non viene specificato nulla, utilizza l'utente di default configurato nel contesto di sicurezza.
- `mvc.perform(get("/user"))`: Utilizza `MockMvc` per simulare una richiesta HTTP GET all'endpoint `/user`.
- `andExpect(status().isOk())`: Verifica che la risposta HTTP abbia lo stato 200 OK.

### Test degli Endpoint Protetti

#### Accesso ad Endpoint Sicuri per Utenti

```java
@Test
@WithUserDetails()
public void whenUserAccessUserSecuredEndpoint_thenOk() throws Exception {
    mvc.perform(get("/user"))
      .andExpect(status().isOk());
}
```

- Questo test verifica che un utente autenticato possa accedere all'endpoint `/user` e ricevere uno stato HTTP 200 OK.

#### Accesso a Endpoint Non Protetti

```java
@Test
@WithUserDetails()
public void whenUserAccessRestrictedEndpoint_thenOk() throws Exception {
    mvc.perform(get("/all"))
      .andExpect(status().isOk());
}
```

- Questo test verifica che un utente autenticato possa accedere all'endpoint `/all`, che si presume sia un endpoint non protetto, e ricevere uno stato HTTP 200 OK.

#### Accesso a Endpoint Protetti per Admin

```java
@Test
@WithUserDetails()
public void whenUserAccessAdminSecuredEndpoint_thenIsForbidden() throws Exception {
    mvc.perform(get("/admin"))
      .andExpect(status().isForbidden());
}
```

- Questo test verifica che un utente autenticato, ma non con ruolo di admin, tenti di accedere all'endpoint `/admin` e riceva uno stato HTTP 403 Forbidden. Questo significa che l'endpoint è protetto e accessibile solo agli admin.

#### Accesso a Endpoint Protetti per Delete

```java
@Test
@WithUserDetails()
public void whenUserAccessDeleteSecuredEndpoint_thenIsForbidden() throws Exception {
    mvc.perform(delete("/delete"))
      .andExpect(status().isForbidden());
}
```

- Questo test verifica che un utente autenticato tenti di effettuare una richiesta DELETE all'endpoint `/delete` e riceva uno stato HTTP 403 Forbidden. Questo indica che l'endpoint è protetto e non accessibile per l'utente corrente.

### Differenza tra Forbidden e Unauthorized

- **403 Forbidden**: L'utente è autenticato ma non ha i permessi necessari per accedere alla risorsa.
- **401 Unauthorized**: L'utente non è autenticato. In altre parole, non ha fornito le credenziali richieste o le credenziali fornite non sono valide.

### Importanza del Test

Questi test sono cruciali per assicurarsi che le regole di sicurezza definite nell'applicazione funzionino correttamente. Essi verificano che:

- Gli utenti abbiano accesso solo agli endpoint ai quali sono autorizzati.
- Gli endpoint protetti siano inaccessibili agli utenti non autorizzati.

Questa suite di test aiuta a mantenere la sicurezza dell'applicazione e a prevenire accessi non autorizzati a risorse sensibili.
