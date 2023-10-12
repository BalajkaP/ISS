package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// Musím zde vytvořit ENTITU, tedy tabulku, která přesně popisuje skutečnou a_movie tabulku
// Stačí, když mám vytvořené SCHEMA v MySQL Workbench
@Entity                // díky tomuto hibernate ví, že jde o databázovou ENTITU- tedy o popis, jak vypadat tabulka
// Díky tomuto se v MySQL vytvoří daná tabulka, i když tam vůbec není před tím!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
@Table(name = "astronaut")  // takto zadáme přesně jméno naší tabulky

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AstronautEntity {    // zde už vlastní definice naší ENTITY= tabulky

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // Zde je to jméno astronauta
    @Column(name = "name")        // název sloupce v tabulce
    private String name;          // název proměnné pro daný sloupec - je to field v  AstronautEntity

// Zde zadám jméno Craftu, ve kterém jsou astronauti
// NOVÁ VERZE: ZDE ZADÁM TEN 3. SLOUPEC S NÁZVEM craft, TJ. PŘEVEZMU CELOU DEFINICI SLOUPCE Z SpaceshipEntity
    // Zde nová varianta
//    @OneToOne
//    @JoinColumn(name = "craftname")    // Zde PŘEVEZMU CELOU DEFINICI SLOUPCE Z SpaceshipEntity
//    SpaceshipEntity craftname;         // Na tuto proměnnou se pak budu v programu odkazovat pro tento sloupec

// ZDE ZADÁM TEN 3. SLOUPEC S NÁZVEM craft - je to jen klasické vytvoření sloupce
//    Toto je původní varianta
        @Column(name = "craftname")
        private String craftname;

}
