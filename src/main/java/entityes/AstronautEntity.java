package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// Musím zde vytvořit ENTITU, tedy tabulku, která přesně popisuje skutečnou a_movie tabulku
@Entity                // díky tomuto hibernate ví, že jde o databázovou ENTITU- tedy o popis, jak vypadat tabulka
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
