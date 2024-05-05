# Annotazioni stereotipate di Spring Framework

---

## @Component

Questa annotazione viene utilizzata sulle classi per indicare un componente Spring. L'annotazione` @Component` segna la classe Java come bean o dice componente in modo che il meccanismo di scansione dei componenti di Spring possa essere aggiunto al contesto dell'applicazione.

---

## @Controller

L'annotazione `@Controller` viene utilizzata per indicare che la classe è un controller Spring. Questa annotazione può essere utilizzata per identificare i controller per Spring MVC o Spring WebFlux.

---

## @Service

Questa annotazione viene utilizzata su una classe. `@Service` segna una classe Java che esegue alcuni servizi, come eseguire la logica aziendale, eseguire calcoli e chiamare API esterne. Questa annotazione è una forma specializzata dell'annotazione `@Component` destinata ad essere utilizzata nel livello di servizio.

---

## @Repository

Questa annotazione viene utilizzata su classi Java che accedono direttamente al database. L'annotazione `@Repository` funziona come marker per qualsiasi classe che svolge il ruolo di repository o Data Access Object.

Questa annotazione ha una funzione di traduzione automatica. Ad esempio, quando si verifica un'eccezione in `@Repository` c'è un gestore per quell'eccezione e non è necessario aggiungere un blocco try catch.

