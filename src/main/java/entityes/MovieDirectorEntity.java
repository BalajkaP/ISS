package entityes;

import javax.persistence.*;

// TOTO UKÁZKA JAK NAMODELOVAT ENTITU V HIBERNATE KTERÁ SLOUČÍ HODNOTY ZE 2 TABULEK
// Tj. vytvořím NOVOU FYZICKOU VÝSLEDKOVOU TABULKU,  a ne jen abstraktní TABULKU JAKO VÝSLEDEK query
// JAK TEDY VYBÍRÁM ZE 2 TABULEK
// entita reprezentuje přístup ke dvoum tabulkám - dodělat obsluhu.
@Entity
@Table(name="a_movie")             // Vybíráme z této tabulky a_movie
@SecondaryTable(name="a_director") // ale taky z této tabulky  a_director
public class MovieDirectorEntity {

    @Id
    @Column(name="id_movie")        // 1. sloupec je movie ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")            // 2. sloupec je movie NAME
    private String name;

    @Column(name="name", table = "a_director")  // 3. sloupec bude director NAME - z tabulky a_director
    private String dirctorname;

}

/*   // ZDE UKÁZKA JAK TO UDĚLAT V SQL
SELECT m.name film, d.name rejza
FROM mydb.a_movie m, mydb.a_director d
WHERE m.director_id=d.director_id;

*/
