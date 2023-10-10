package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="a_actor")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActorEntity {

    @Id
    @Column(name="id_actor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable = false)   // pomocí nullable mohu kontrolovat v hibernate, jestli sloupec name=null
    private String name;                     // pomocí false tu kontrolu asi vypnu, tj. kontrolu nechám na MySQL DB
                                             // implicitně je nullable = TRUE
    @Column(name="age")
    private Integer age;


    // https://www.geeksforgeeks.org/hibernate-different-cascade-types/
    // Pomocí CASCADE mohu zajistit, že když SMAŽU HERCE, TAK SE SMAŽOU VŠECHNY FILMY KDE HRAJE.
    // NEBO NAOPAK, KDYŽ SMAŽU FILM, TAK SE MOHOU SMAZAT HERCI, KTEŘÍ V NĚM HRAJÍ. Oba případy jsou však docela drsné.
    // Řešit tak, že když smažu herce, tak se smaže ta vazba, ale ne ten film, ve kterém hraje.
    // This code defines a one-to-many relationship between a Actor entity and an Movie entity and specifies that the
    // detach operation should be cascaded to the associated movies. This means that when a Actor entity is detached, any associated
    // Movie entities will also be detached automatically. The Movie entities will become detached from the persistence
    // context and their state will no longer be managed by Hibernate.
    @ManyToMany(cascade = { CascadeType.DETACH })
    // https://www.baeldung.com/hibernate-many-to-many
    @JoinTable(   // Musím nadeklarovat tu VAZEBNÍ tabulku. Ta se deklaruje jen v jedné z těch dvou spojovaných tabulek.
            name = "a_movie_actor",            // Zadat jméno vazební tabulky
            joinColumns = { @JoinColumn(name = "id_actor") }, // Zde zadat sloupec, kterým se joinuje z této entity
                                                              // Touto entitou je zde ActorEntity, tj. kde jsem nyní uvnitř
            inverseJoinColumns = { @JoinColumn(name = "id_movie") }) // Zde zase spojovací sloupec co je v MovieEntity
    Set<MovieEntity> movies = new HashSet<>(); // Musím vytvořit kolekci movies, protože každý actor může hrát ve více
                                               // filmech.

}
