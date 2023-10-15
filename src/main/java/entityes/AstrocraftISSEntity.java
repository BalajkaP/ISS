package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
// You should create an entity class that represents the astrocraftISS table. This class should include fields that
// correspond to the columns you want to store in the new table.

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AstrocraftISSEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String astronautName;
    private String craftName;

}
