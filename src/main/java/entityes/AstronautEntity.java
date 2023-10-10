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

    @Id      // tímto oznámíme hibernatu, že jde o primární klíč
    @Column(name="id_movie")   // zde definuji vlastní sloupce tabulky tak jak jsou vytvořeny v MySQL
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;        // musím taky zadat typ toho sloupce dle reality v MySQL databázi
                               // best practice je, aby se tato proměnná jmenovala podobně jako sloupec v DB

    @Column(name="name")       // zde definuji vlastní sloupce tabulky tak jak jsou vytvořeny v MySQL
    private String name;       // musím taky zadat typ toho sloupce dle reality v MySQL databázi
                               // best practice je, aby se tato proměnná jmenovala podobně jako sloupec v DB

    @OneToOne  // Definujeme vazbu 1:1, tj. že film může mít jen 1 režiséra, a tak spojíme tabulky pomocí JOIN přes director_id sloupec
    // Tj. místo ID (jako Integer) režiséra budeme rovnou vybírat ENTITu ze sousední tabulky, která má vazbu přes ten identifikátor.
    @JoinColumn(name="director_id") // zde tedy řeknu, že mám v movie joinovací sloupec přímo na odpovídající sloupec
                                    // v SpaceshipEntity. Zde tedy přímo zmíním sloupec z té druhé tab SpaceshipEntity.
                                    // A vybírám režiséra přímo z 2. tabulky.
    SpaceshipEntity director;   // musím taky zadat typ toho sloupce dle reality v MySQL databázi. Zde jde však přímo o
                               // sloupec z SpaceshipEntity který chci propojit
                               // fungovalo by to ale i takto: private Integer directorId



}
