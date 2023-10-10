package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="a_director")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectorEntity {

    @Id
    @Column(name="director_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    // Definujeme vazbu 1:M, tj. že režisér může režírovat více filmů
    // A připojujeme přímo sloupec z druhé tabulky MovieEntity pomocí JoinColumn
    // Ale jelikož může být výsledkem více filmů (tj. více řádků), tak vytvořit List, tedy kolekci těch MovieEntity
    @OneToMany
    @JoinColumn(name="director_id")
    List<MovieEntity> movies;

}
