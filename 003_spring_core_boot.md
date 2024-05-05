# Core Spring Framework Annotations

---


Le annotazioni sono uno strumento potente in Spring e Spring Boot per semplificare la configurazione e il comportamento delle classi.

## @SpringBootApplication

Questa annotazione viene utilizzata sulla classe dell'applicazione durante l'impostazione di un progetto Spring Boot. La classe che è annotata con `@SpringBootApplication` deve essere mantenuta nel pacchetto base. L'unica cosa che fa `@SpringBootApplication` è una scansione dei componenti. Ma scansionerà solo i suoi sotto-pacchetti. Ad esempio, se metti la classe annotata con` @SpringBootApplication `in com.example, allora` @SpringBootApplication `scansionerà tutti i suoi sotto-pacchetti, come com.example.a, com.example.b e com.example .ascia

`@SpringBootApplication` è una comoda annotazione che sostituisce le annotazioni:

```java
    @Configuration
    @EnableAutoConfiguration
    @ComponentScan
```


---

Di seguito, una panoramica delle annotazioni più comuni in entrambi i framework:

## Annotazioni in Spring

1. **`@Component` / `@Service` / `@Repository` / `@Controller`:**
   - Indicano che una classe è un componente, un servizio, un repository o un controller rispettivamente.
   - Consentono la scansione automatica delle classi e la loro registrazione nel contesto di Spring.

2. **`@Autowired`:**
   - Utilizzata per l'iniezione automatica delle dipendenze, riducendo la necessità di configurare manualmente le dipendenze nelle classi.

3. **`@Qualifier`:**
   - Specifica il nome o l'ID di un bean quando ci sono più implementazioni dello stesso tipo.

4. **`@Configuration` / `@Bean`:**
   - `@Configuration` indica che una classe contiene metodi con definizioni di bean.
   - `@Bean` viene utilizzata per dichiarare un metodo come produttore di bean, restituendo un oggetto che verrà gestito dal container IoC.

5. **`@Value`:**
   - Utilizzata per l'iniezione di valori da properties o da espressioni di Spring.

6. **`@Qualifier`:**
   - Viene utilizzata in combinazione con `@Autowired` per specificare quale bean deve essere iniettato quando ci sono più implementazioni dello stesso tipo.

7. **`@Scope`:**
   - Specifica il ciclo di vita di un bean (singleton, prototype, etc.).

8. **`@ComponentScan`:**
   - Indica a Spring di eseguire la scansione delle classi nel package specificato per individuare e registrare i bean.

9. **`@Conditional`:**
   - Consente di condizionare la registrazione di un bean in base a determinate condizioni.

## Annotazioni in Spring Boot

1. **`@SpringBootApplication`:**
   - Raggruppa diverse annotazioni come `@Configuration`, `@EnableAutoConfiguration`, e `@ComponentScan` in un'unica annotazione per l'applicazione principale.

2. **`@RestController` / `@RequestMapping`:**
   - `@RestController` combina `@Controller` e `@ResponseBody`, semplificando la creazione di API RESTful.
   - `@RequestMapping` specifica il percorso delle richieste HTTP gestite dal controller.

3. **`@SpringBootTest`:**
   - Indica che la classe è una classe di test per un'applicazione Spring Boot e consente di configurare l'ambiente di test.

4. **`@ConfigurationProperties`:**
   - Lega i valori delle proprietà dell'applicazione a un bean.

5. **`@EnableAutoConfiguration`:**
   - Consente all'applicazione di configurarsi automaticamente in base alle dipendenze presenti nel classpath.

6. **`@EnableConfigurationProperties`:**
   - Abilita la configurazione di proprietà definite dall'utente con `@ConfigurationProperties`.

7. **`@SpringBootTest`:**
   - Segnala a Spring Boot di inizializzare un contesto dell'applicazione completo per il test.

8. **`@EnableScheduling`:**
   - Abilita la pianificazione delle attività nel contesto dell'applicazione.

9. **`@EnableTransactionManagement`:**
   - Abilita la gestione delle transazioni.

10. **`@SpringBootApplication` / `@EntityScan` / `@EnableJpaRepositories`:**

- Utilizzate in combinazione per configurare e scansionare le entità JPA e i repository.

Queste annotazioni semplificano la configurazione e forniscono funzionalità avanzate in Spring e Spring Boot. Puoi scegliere le annotazioni più appropriate in base alle esigenze specifiche della tua applicazione.

---


## @Required

Questa annotazione viene applicata ai metodi del bean setter. Considerare uno scenario in cui è necessario applicare una proprietà richiesta. L'annotazione`@Required` indica che il bean interessato deve essere popolato al momento della configurazione con la proprietà richiesta. Altrimenti viene generata un'eccezione di tipo BeanInitializationException.

---

## @EnableAutoConfiguration

Questa annotazione viene generalmente posizionata sulla classe di applicazione principale. L'annotazione `@EnableAutoConfiguration` definisce implicitamente un "pacchetto di ricerca" di base. Questa annotazione dice a Spring Boot di iniziare ad aggiungere bean in base alle impostazioni del percorso di classe, altri bean e varie impostazioni delle proprietà.

---

## @Lazy

Questa annotazione viene utilizzata sulle classi dei componenti. Per impostazione predefinita, tutte le dipendenze autowired vengono create e configurate all'avvio. Ma se vuoi inizializzare un bean pigramente, puoi usare l'annotazione `@Lazy` sulla classe. Ciò significa che il bean verrà creato e inizializzato solo quando viene richiesto per la prima volta. Puoi anche usare questa annotazione sulle classi `@Configuration`. Questo indica che tutti i metodi `@Bean` all'interno di quel `@Configuration` dovrebbero essere inizializzati pigramente.

---

## @Value

Questa annotazione viene utilizzata a livello di campo, parametro del costruttore e parametro del metodo. L'annotazione` @Value` indica un'espressione di valore predefinita per il campo o il parametro con cui inizializzare la proprietà. Dato che l'annotazione `@Autowired` dice a Spring di iniettare l'oggetto in un altro quando carica il contesto dell'applicazione, puoi anche usare l'annotazione` @Value `per iniettare valori da un file di proprietà nell'attributo di un bean. Supporta segnaposto # {...} e $ {...}.

---
