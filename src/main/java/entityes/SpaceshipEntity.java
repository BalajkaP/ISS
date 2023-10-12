package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity           // Díky tomuto se v MySQL vytvoří daná tabulka, i když tam vůbec není před tím!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                  // Stačí, když mám vytvořené SCHEMA v MySQL Workbench
@Table(name="spaceship")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpaceshipEntity {

    @Id
    //@Column(name="director_id")     // Netřeba zadávat
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="craftname")  // původně bylo name
    private String craftname;

    // Definujeme vazbu 1:M, tj. že spaceship může obsahovat více astronauts
    // A připojujeme přímo sloupec z druhé tabulky AstronautEntity pomocí JoinColumn
    // Ale jelikož může být výsledkem více filmů (tj. více řádků), tak vytvořit List, tedy kolekci těch AstronautEntity
      //@OneToMany
//    @JoinColumn(name="director_id")
//    List<AstronautEntity> movies;

}
