# MockMvc

```java
@Test
@WithUserDetails()
public void whenUserAccessUserSecuredEndpoint_thenOk() throws Exception {
    mvc.perform(get("/user"))
      .andExpect(status().isOk());
}
```

L'oggetto `mvc` che viene utilizzato nel codice di test è fornito da Spring Framework attraverso il modulo `spring-test`. Questo modulo fornisce una serie di utilità per facilitare il testing delle applicazioni Spring, inclusa la classe `MockMvc`.


`MockMvc` è una classe che simula il comportamento di un servlet container, consentendo di eseguire le richieste HTTP contro i controller Spring e verificare il comportamento del sistema in modo controllato e isolato dall'ambiente di produzione. Questo è particolarmente utile nei test di integrazione e nei test dell'unità per le applicazioni web.

---

### Utilizzo di MockMvc nel Codice di Test

Nel codice di test che hai mostrato, `mvc` è un oggetto `MockMvc` che viene inizializzato nel contesto del test grazie all'integrazione con `spring-test`. Ecco come viene utilizzato nel contesto dei tuoi test:

```java
@Test
@WithUserDetails()
public void whenUserAccessUserSecuredEndpoint_thenOk() throws Exception {
    mvc.perform(get("/user"))
      .andExpect(status().isOk());
}
```

- `mvc.perform(get("/user"))`: Questa istruzione simula una richiesta HTTP GET all'endpoint `/user` utilizzando `MockMvc`.
- `.andExpect(status().isOk())`: Questo metodo verifica che la risposta HTTP ricevuta abbia uno stato 200 (OK). È un modo per assicurarsi che l'endpoint risponda correttamente senza errori.

---

### Configurazione di MockMvc

Normalmente, `MockMvc` viene configurato e iniettato nel test attraverso l'annotazione `@AutoConfigureMockMvc` insieme ad altre annotazioni come `@SpringBootTest` per eseguire test di integrazione. Ecco un esempio di configurazione tipica:

```java
@SpringBootTest
@AutoConfigureMockMvc
public class MyControllerTests {

    @Autowired
    private MockMvc mvc;

    // Metodi di test qui...
}
```

---

### Conclusione

Quindi, `mvc` è un oggetto `MockMvc` fornito da Spring Test Framework per eseguire e verificare le richieste HTTP nei test delle applicazioni Spring. Utilizzando `MockMvc`, è possibile simulare il comportamento delle richieste HTTP e testare il funzionamento dei controller e della sicurezza delle API in modo controllato e automatizzato.
