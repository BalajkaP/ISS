import entityes.AstronautEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class VariousDbQuery {

    // Zde získáme jen astronauty, kteří jsou momentálně přítomni na ISS
    public List<AstronautEntity> getAstronautsWithCraftISS() {
        try {
            Session session = DbConnect.getSession();
            Transaction transaction = session.beginTransaction();
 //     TOTO PLATÍ PRO PŮVODNÍ VERZI
 //     List<AstronautEntity> astronauts = session.createQuery("FROM AstronautEntity WHERE craftname = 'ISS'", AstronautEntity.class).list();

//      ODSUD UŽ PLATÍ PRO NOVOU VERZI
            List<AstronautEntity> astronauts = session.createQuery("FROM AstronautEntity WHERE Spaceship.getCraftname() = 'ISS'", AstronautEntity.class).list();

            transaction.commit();
            session.close();

            return astronauts;

        } catch (Exception e) {
            e.printStackTrace();
            return null;              // Metoda musí něco vrátit - tj. v případě neúspěchu vrátí NULL
        }
    }



}
