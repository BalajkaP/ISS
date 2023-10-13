package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity           // Díky tomuto se v MySQL vytvoří daná tabulka, i když tam vůbec není před tím!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                  // Stačí, když mám vytvořené SCHEMA v MySQL Workbench
@Table(name="spaceship")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpaceshipEntity {

    //----------------------------------------------------------------------------
    // Zde Původní verze
//    @Id
//    //@Column(name="spaceship_id")     // Netřeba zadávat. Kdybych zadal, tak se bude jmenovat tak jak zde zadáno
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name="craftname")  // původně bylo name
//    private String craftname;


    //---------------------------------------------------------------------------------
    // Zde začíná nová verze

    @Id
    @Column(name = "spaceship_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "craftname")
    private String craftname;

    // Definujeme vazbu 1:M, tj. že spaceship může obsahovat více astronauts
    // A mapujeme se přímo na proměnnou spaceship z druhé tabulky AstronautEntity pomocí mappedBy
    // Ale jelikož může být výsledkem více filmů (tj. více řádků), tak vytvořit List, tedy kolekci těch AstronautEntity
    @OneToMany(mappedBy = "spaceship", cascade = CascadeType.ALL)
    private Set<AstronautEntity> astronauts = new HashSet<>();




    // Definujeme vazbu 1:M, tj. že spaceship může obsahovat více astronauts
    // A připojujeme přímo sloupec z druhé tabulky AstronautEntity pomocí JoinColumn
    // Ale jelikož může být výsledkem více filmů (tj. více řádků), tak vytvořit List, tedy kolekci těch AstronautEntity
      //@OneToMany
//    @JoinColumn(name="director_id")
//    List<AstronautEntity> movies;

}
