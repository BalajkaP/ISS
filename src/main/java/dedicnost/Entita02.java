package dedicnost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="entita02")
// dedi sloupec id z ParentEntity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entita02 extends ParentEntity{

    @Column(name="country")
    private String country;


}
