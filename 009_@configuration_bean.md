# @Configuration

Questa annotazione viene utilizzata su classi che definiscono bean. `@Configuration`è un analogo per il file di configurazione XML - è la configurazione che utilizza la classe Java. La classe Java annotata con `@Configuration` è una configurazione a sé stante e avrà metodi per creare un'istanza e configurare le dipendenze.

Ecco un esempio:

```java
    @Configuration
    public class DataConfig{ 
      @Bean
      public DataSource source(){
        DataSource source = new OracleDataSource();
        source.setURL();
        source.setUser();
        return source;
      }
      @Bean
      public PlatformTransactionManager manager(){
        PlatformTransactionManager manager = new BasicDataSourceTransactionManager();
        manager.setDataSource(source());
        return manager;
      }
    }
```

---

## @ComponentScan

Questa annotazione viene utilizzata con l'annotazione `@Configuration` per consentire a Spring di conoscere i pacchetti per scansionare i componenti annotati. `@ComponentScan` viene anche usato per specificare i pacchetti base usando gli attributi basePackageClasses o basePackage per scansionare. Se non vengono definiti pacchetti specifici, la scansione verrà eseguita dal pacchetto della classe che dichiara questa annotazione.

---

## @Bean

Questa annotazione è usata a livello di metodo.` @Bean `annotation funziona con `@Configuration` per creare Spring bean. Come accennato in precedenza, `@Configuration` avrà metodi per istanziare e configurare le dipendenze. Tali metodi verranno annotati con `@Bean`.

Il metodo annotato con questa annotazione funziona come ID bean e crea e restituisce il bean effettivo.

Per esempio:

```java

    @Configuration
    public class AppConfig{
      @Bean
      public Person person(){
        return new Person(address());
      }
      @Bean
      public Address address(){
        return new Address();
      }
    }
```

---
