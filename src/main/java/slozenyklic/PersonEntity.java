package slozenyklic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="person")   // Toto je skutečný název tabulky v MySQL

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonEntity {  // toto je vlastní tabulka, klasika. Ale musím zde vložit ten složený primár klíč.

    // POZOR: slozeny primární klíč (lze řešit i anotací @IdClass, ale ta je složitější)  !!!!!
    @EmbeddedId           // řeknu, že jde o složený primární klíč
    private PersonId id;

    @Column(name="name")
    private String name;

    // a dalsi sloupce

}
