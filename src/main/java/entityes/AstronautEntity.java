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

    //----------------------------------------------------------------------------
    // Zde Původní verze
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    // Zde je to jméno astronauta
//    @Column(name = "name")        // název sloupce v tabulce
//    private String name;          // název proměnné pro daný sloupec - je to field v  AstronautEntity
//    // Zde je to jméno spaceship - je to jen klasické vytvoření sloupce
//    @Column(name = "craftname")
//    private String craftname;

    //---------------------------------------------------------------------------
    // Zde začíná nová verze

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    // ZDE ZADÁM TEN 3. SLOUPEC , TJ. PŘEVEZMU CELOU DEFINICI SLOUPCE Z SpaceshipEntity
    // Definuji relaci 1:M a taky jak chci název 3. sloupce
    // To jestli zvolím spaceship_id nebo craftname, NEMÁ VLIV NA JINÝ KÓD. NIC SE NEROZBIJE.
    @ManyToOne   // POZOR: Na rozdíl od HIBERNATE1_NEW je zde použito ManyToOne (kdežto v MovieEntity je OneToOne)
    // Zde Join přes spaceship_id v SpaceshipEntity
//    @JoinColumn(name = "spaceship_id")  // A připojujeme přímo sloupec z druhé tabulky SpaceshipEntity pomocí JoinColumn.
                                        // Náš 3. sloupec zde v AstronautEntity se pak bude jmenovat úplně stejně
    // Zde Join přes craftname v SpaceshipEntity. Je ÚPLNĚ JEDNO PŘES CO JOIN. JDE JEN O TO , CO CHCI VIDĚT VE 3. TABULCE
    // V AstronautEntity si pro tento sloupec vytvořím PROMĚNNOU spaceshift.
    // SUPER DŮLEŽITÉ: In this code, we're using @JoinColumn with name and referencedColumnName to specify that the
    // craftname column in the AstronautEntity should be related to the craftname column in the SpaceshipEntity.
    // This will ensure that you have string values in the craftname column of the AstronautEntity table, and it will be
    // related to the craftname column in the SpaceshipEntity table.
    // To referencedColumnName musím použít, pokud nejdu přes PK sloupec z té druhé tabulky- tj. kdybych chtěl jako výše použít
    // spaceship_id , tak NETŘEBA POUŽÍT referencedColumnName.
      @JoinColumn(name = "craftname")
      //@JoinColumn(name = "craftname", referencedColumnName = "craftname")
      private SpaceshipEntity spaceship;  // A pro tento připojený sloupec si deklarujeme jednoduchou proměnnou spaceship,
                                        // protože každý Astronaut má jen 1 ship


// Zde zadám jméno Craftu, ve kterém jsou astronauti
// NOVÁ VERZE: ZDE ZADÁM TEN 3. SLOUPEC S NÁZVEM craft, TJ. PŘEVEZMU CELOU DEFINICI SLOUPCE Z SpaceshipEntity
    // Zde nová varianta
//    @OneToOne
//    @JoinColumn(name = "craftname")    // Zde PŘEVEZMU CELOU DEFINICI SLOUPCE Z SpaceshipEntity
//    SpaceshipEntity craftname;         // Na tuto proměnnou se pak budu v programu odkazovat pro tento sloupec



}
