package slozenyklic;

import javax.persistence.Column;
import javax.persistence.Embeddable;

// definice slozeneho primárního klíče
@Embeddable    // tímto řeknu že má složený primární klíč
public class PersonId {    // nějaká tabulka se složeným primár klíčem
                           // žádné zvláštní anotace, jen jména sloupců
    @Column(name = "id_firmy")   // První primár klíč
    private int idFirmy;

    @Column(name = "id_oddeleni")   // Druhý primár klíč
    private int idOddeeleni;

}
