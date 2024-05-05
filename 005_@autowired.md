# @Autowired

Questa annotazione viene applicata su campi, metodi setter e costruttori. L'annotazione` @Autowired `inietta implicitamente la dipendenza dell'oggetto.

Quando usi `@Autowired` sui campi e passi i valori per i campi usando il nome della proprietà, Spring assegnerà automaticamente i campi con i valori passati.

Puoi anche usare `@Autowired` su proprietà private, come mostrato di seguito. (Non è una buona idea!)

```java
    public class Customer {
       ` @Autowired `                              
        private Person person;                   
        private int type;
    }
```

Quando usi `@Autowired` sui metodi setter, Spring prova a eseguire l'autowiring per tipo sul metodo. Stai dicendo a Spring che dovrebbe iniziare questa proprietà usando il metodo setter in cui puoi aggiungere il tuo codice personalizzato, come inizializzare qualsiasi altra proprietà con questa proprietà.

```java

    public class Customer {                                                                                         
        private Person person;
       ` @Autowired `                                                                                                     
        public void setPerson (Person person) {
         this.person=person;
        }
    }
```

---

Prendi in considerazione uno scenario in cui hai bisogno dell'istanza della classe A, ma non memorizzi A nel campo della classe. Basta usare A per ottenere l'istanza di B e si sta memorizzando B in questo campo. In questo caso il metodo setter autowiring ti farà meglio. Non avrai campi inutilizzati a livello di classe.

Quando usi `@Autowired` su un costruttore, l'iniezione del costruttore avviene al momento della creazione dell'oggetto. Indica il costruttore da autowire quando usato come bean. Una cosa da notare qui è che solo un costruttore di qualsiasi classe bean può portare l'annotazione `@Autowired`.

```java

    @Component
    public class Customer {
        private Person person;
        @Autowired
        public Customer (Person person) {          
          this.person=person;
        }
    }
```

NOTA: A partire dalla spring 4.3, `@Autowired` è diventato facoltativo per le classi con un solo costruttore. Nell'esempio sopra, Spring avrebbe comunque iniettato un'istanza della classe Person se avessi omesso l'annotazione `@Autowired`.


---

## @Qualifier

Questa annotazione viene utilizzata insieme all'annotazione `@Autowired`. Quando è necessario un maggiore controllo del processo di iniezione delle dipendenze, è possibile utilizzare `@Qualifier`. @Qualifier `può essere specificato su singoli argomenti del costruttore o parametri del metodo. Questa annotazione viene utilizzata per evitare confusione che si verifica quando si crea più di un bean dello stesso tipo e si desidera cablare solo uno di essi con una proprietà.

Si consideri un esempio in cui un'interfaccia BeanInterface è implementata da due bean BeanB1 e BeanB2.

```java
    @Component
    public class BeanB1 implements BeanInterface {
      //
    }
    @Component
    public class BeanB2 implements BeanInterface {
      //
    }
```

---

Ora se BeanA autorizza questa interfaccia, Spring non saprà quale delle due implementazioni iniettare.
Una soluzione a questo problema è l'uso dell'annotazione `@Qualifier`.

```java
    @Component
    public class BeanA {
      @Autowired
      @Qualifier("beanB2")
      private BeanInterface dependency;
      ...
    }
```

Con l'annotazione `@Qualifier` aggiunta, Spring ora saprà quale bean autowire dove beanB2 è il nome di BeanB2.
