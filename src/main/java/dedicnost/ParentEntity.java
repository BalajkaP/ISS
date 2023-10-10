package dedicnost;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass   // predek pro jine entity, tj. tímto řeknu, že tato třída může být děděna
@Getter
@Setter
public class ParentEntity {

    @Id
    @Column(name="id")
    private Integer id;
}
