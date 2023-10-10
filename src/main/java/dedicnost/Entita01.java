package dedicnost;

import entityes.MovieEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity          // díky tomuto hibernate ví, že jde o databázovou ENTITU- tedy o popis, jak vypadat tabulka
@Table(name="entita01")
// dedi sloupec id z ParentEntity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entita01 extends ParentEntity{

    @Column(name="name")
    private String name;


}
