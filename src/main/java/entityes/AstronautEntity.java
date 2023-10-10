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
@Table(name="astronaut")  // takto zadáme přesně jméno naší tabulky

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AstronautEntity {    // zde už vlastní definice naší ENTITY= tabulky

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "craft")
    private String craft;



}
