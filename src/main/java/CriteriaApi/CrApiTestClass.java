package CriteriaApi;

import entityes.ActorEntity;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CrApiTestClass {    // CRITERIA QUERY = Ukázka modelování QUERY pomocí OBJEKTŮ
    private Session session = null;

    // Chci zde toto namodelovat 2 výrazy (Predikáty) ve WHERE podmínkách :
    // 1. Nejprve testuji, ZDA ZADANÉ JMÉNO (přijde v argumentu) JE V SLOUPCI "name" v ActorEntity !!!!!!!!!!!!!!!!!!!!!!!
    // 2. Pak modeluji predicate "from ActorEntity where age>30", stejně jako v AppMain- metoda: addMovieAndItsActors
    // Vytvořím tedy 2 predikáty ( 2 podmínky ve WHERE příkazu)
    public CrApiTestClass(Session aSession){
        session = aSession;
    }  // Hlavní třída si v konstruktoru převezme session

    public void runItWithWhere(String name, Integer ageVetsi){  // Tato metoda vybírá data z DB

        // Skladani vyberovych podminek pomoci objektu a nikoli HQL retezcu !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Chci zde toto namodelovat 2 výrazy (Predikáty) ve WHERE podmínkách :
        // 1. Nejprve testuji, ZDA ZADANÉ JMÉNO (přijde v argumentu) JE V SLOUPCI "name" v ActorEntity !!!!!!!!!!!!!!!!!!!!!!!
        // 2. Pak modeluji predicate "from ActorEntity where age>30", stejně jako v AppMain- metoda: addMovieAndItsActors
        // Vytvořím tedy 2 predikáty ( 2 podmínky ve WHERE příkazu)
        // VYSVETLENÍ RŮZNÝCH VZTAHŮ MEZI API TŘÍDAMI A INTERFACE:
        // FIND - co se používá v klasickém HIBERNATE, je METODA v INTERFACE EntityManager (ta je v package javax.persistence),
        // ze kterého dědí naše INTERFACE Session
        // getCriteriaBuilder - platí to samé, co pro FIND. Taky tato metoda je v INTERFACE EntityManager, ze kterého dědí
        // naše Session interface
    // ------------------------------------------------------------------------------------------

        // Získání instance toho kde budeme tvořit kritéria. Metoda getCriteriaBuilder vrací objekt CriteriaBuilder.
        // Na tomto objektu pak mohu tvořit QUERY.
        // CriteriaBuilder musím vytvořit POUZE PROTO, abych z něj mohl volat metody like a greaterThan.
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // Zde vytvoříme dotaz criteriaQuery. Zde je důležité určit odkud (z jakého typu Entity) budeme brát data- zde z ActorEntity.
        // ABYCH TOTIŽ VĚDĚL, S JAKOU ENTITOU BUDE criteriaQuery PRACOVAT, a pak už na ní jen volám ty metody SELECT, WHERE,...
        // metoda createQuery create a CriteriaQuery object with the specified result type - ActorEntity.class udává typ
        // výsledného objektu. Takto definováno: <T> CriteriaQuery<T> createQuery(Class<T> resultClass), kde
        // resultClass - je type of the query result.
        // CriteriaQuery už obsahuje metody SELECT, WHERE,...
        CriteriaQuery<ActorEntity> criteriaQuery = criteriaBuilder.createQuery(ActorEntity.class);
        // Nastavit root, protože ten SELECT může brát z více tabulek. Zde nastavím tu hlavní tabulku, z které chci
        // vybírat - uložím do ROOT.
        // <X> Root<X> from(Class<X> entityClass) : Create and add a query root corresponding to the given entity,
        // forming a cartesian product with any existing roots. QUERY ROOT= entita, ze které SELEKTUJI sloupce.
        // CARTESIAN PRODUCT znamená, že daný ROOT mohu kombinovat (slučovat) s jinými ROOT.
        Root<ActorEntity> root = criteriaQuery.from(ActorEntity.class);
        // List<Predicate> : je to seznam těch jednotlivých omezujících položkek (těch výrazů) ve WHERE.
        List<Predicate> preList = new ArrayList<Predicate>(); // položky ve where

        if(name!=null)        // pokud je nenulovy vstupní argument name, tak dame do WHERE. To znamená, že když zákazník
             // ve formuláři vyplní v položce NAME, že chce jen jména herců starších 30 let, tak tento požadavek přidám do WHERE takto.
         // HLAVNÍ PRINCIP: Začínám zde vlastně skládat řetězec těch podmínek pro WHERE.
         // root.get("name"): Create a path corresponding to the referenced attribute- tj. udělá z "name" (název sloupce v actor tab.) dílčí PATH,
            // tedy nějaký Expression.
         // Vezme tedy sloupec "name" z tabulky ActorEntity, protože ROOT ukazuje na ActorEntity.
         // Důležité: ROOT je child interface od PATH, proto lze volat to get.
         // Vytvoření 1. predikátu:
         // Predicate like(Expression<String> x, String pattern): Vytvoří predicate for testing whether the Expression
         // satisfies the given pattern. TJ. ZJISTÍ, ZDA ZADANÉ JMÉNO JE V SLOUPCI "name" v ActorEntity !!!!!!!!!!!!!!!!!!!!!!!
         // JDE O STEJNÝ LIKE, JAKÝ LZE POUŽÍT V SQL PROGRAMOVÁNÍ UVNITŘ PŘÍKAZU WHERE
         // criteriaBuilder je tam jen jako objekt, ze kterého potřebuji vzít metodu LIKE
         // Vytvořený Predicate vložím do preList jako část výrazu pro WHERE. Takto vlastně postupně skládám WHERE podmínku.
            preList.add(criteriaBuilder.like(root.get("name"), name));
        if(ageVetsi!=null)                                            // pokud je nenulovy name dame do where
         // Vytvoření 2. predikátu:
         // Zde vytvořím Predicate, který bude vypadat takto: s "age" > ageVetsi , a to se přiřadí za ten předchozí predicate s "name".
         // Create a predicate for testing whether the first argument is greater than the second.
            preList.add(criteriaBuilder.greaterThan(root.get("age"), ageVetsi));

        // criteriaBuilder.equal() // DALŠÍ UKÁZKA TOHO, CO LZE DÁT DO WHERE. JE TAM SPOUSTA RŮZNÝCH PŘÍKAZŮ !!!!!!!!!!!!!!!!!!!!

        Predicate[] predicates = preList.toArray(new Predicate[0]); // Zde jen převedu preList (kolekce) na pole predicates.
                                        // Jde o pole Predikcatu. To new Predicate[0] znamená pouze nové pole o velikosti 0.
                                        // Ta metoda toArray totiž potřebuje v argumentu nějaké pole, které je stajného typu jako
                                        // to nově vytvořené pole z preList.
        if(predicates.length>0)   // Jen podmínka, pokud mám vůbec nějaké výrazy (predikáty) pro WHERE.
        // Zde opět potřebuji ten criteriaQuery objekt jen proto, abych mohl volat metodu SELECT.
        // Parametr selection v deklaraci metody SELECT znamená Entitu, ze které chci vybírat- a ta musí být shodná
        // s generickým typem objektu criteriaQuery (viz. deklarace criteriaQuery výše ).
        // SELECT opět vrací objekt typu  CriteriaQuery. Proto na měj mohu pomocí tečky zavolat WHERE.
            criteriaQuery.select(root).where(predicates);  // WHERE: Zde použiju ten vytvořený List<Predicate>
        else             // (ale převedený na pole predikates) do výrazu WHERE (zde se finálně vytvoří komplet příkaz WHERE)
            criteriaQuery.select(root); // Pokud není žádný WHERE výraz, tak udělá obyčejný SELECT
        // Vytvoříme tímto tedy výsledný objekt criteriaQuery, protože SELECT A WHERE vrací objekt typu CriteriaQuery.

        // ZDE DOJDE K FINÁLNÍMU VYTVOŘENÍ CELÉHO KOMPLETNÍHO QUERY, TJ. SELECT+WHERE  POMOCÍ createQuery.
        // createQuery VYTVOŘÍ A VRÁTÍ Query na základě parametru criteriaQuery, který jsme vytvořili o řádek výše.
        Query query = session.createQuery(criteriaQuery);
        // Query: Interface used to control query execution.

        // A zde dojde k vlastnímu PROVEDENÍ toho Select QUERY, a vrácení výsledku ve formě untyped List.
        List<ActorEntity> actList = query.getResultList(); // Execute a SELECT query and return the query results as an untyped List.
                                     // Vrátí seznam výsledných řádků ve formě kolekce
                                     // Každý řádek je vlastně taky typu ActorEntity
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        actList.forEach(ac -> {
            System.out.println("name=" + ac.getName() + " age=" + ac.getAge());
        });
    }

}
