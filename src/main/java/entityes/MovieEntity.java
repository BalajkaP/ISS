package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Musím zde vytvořit ENTITU, tedy tabulku, která přesně popisuje skutečnou a_movie tabulku
@Entity                // díky tomuto hibernate ví, že jde o databázovou ENTITU- tedy o popis, jak vypadat tabulka
@Table(name="a_movie")  // takto zadáme přesně jméno naší tabulky

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {    // zde už vlastní definice naší ENTITY= tabulky

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
                                    // v DirectorEntity. Zde tedy přímo zmíním sloupec z té druhé tab DirectorEntity.
                                    // A vybírám režiséra přímo z 2. tabulky.
    DirectorEntity director;   // musím taky zadat typ toho sloupce dle reality v MySQL databázi. Zde jde však přímo o
                               // sloupec z DirectorEntity který chci propojit
                               // fungovalo by to ale i takto: private Integer directorId

    @ManyToMany(mappedBy = "movies") // nazev vlastnosti (field) z ActorEntity (Set<MovieEntity> movies = new HashSet<>())
                                     // Musím říct, jaká položka v té druhé  entitě je ta mapovaná.
    // DŮLEŽITÉ: Stejně jako JoinTable a Cascade, tak i mappedBy stačí uvést jen u jedné z těch spojovaných entit.
                                     // Je to taky kolekce, jako zde actors. Musí to být kolekce, protože pro každý prvek
                                     // z dané Entity existuje více prvků z té 2. entity
    private Set<ActorEntity> actors = new HashSet<>(); // Musím vytvořit kolekci herců, protože každý film může mít více
                                                       // herců. Set je interface, HashSet je class
}
