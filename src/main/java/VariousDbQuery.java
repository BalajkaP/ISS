import entityes.AstronautEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class VariousDbQuery {

    // Zde získáme jen astronauty, kteří jsou momentálně přítomni na ISS
    public List<AstronautEntity> getAstronautsWithCraftISS(Session session) {
        try {
//            Session session = DbConnect.getSession();
//            Transaction transaction = session.beginTransaction();

 //     TOTO PLATÍ PRO PŮVODNÍ VERZI
 //     List<AstronautEntity> astronauts = session.createQuery("FROM AstronautEntity WHERE craftname = 'ISS'", AstronautEntity.class).list();

//      ODSUD UŽ PLATÍ PRO NOVOU VERZI
            List<AstronautEntity> astronauts = session.createQuery("FROM AstronautEntity ae WHERE ae.spaceship = 'ISS'").list();

//            transaction.commit();
//            session.close();

            return astronauts;

        } catch (Exception e) {
            e.printStackTrace();
            return null;              // Metoda musí něco vrátit - tj. v případě neúspěchu vrátí NULL
        }
    }

   // DELETE DANÝ ASTRONAUT DLE ZADANÉHO JMÉNA
   // Do argumentu pošlu jméno astronauta= astronautName, kterého chci vymazat. V HQL to jméno použiji a taky natvrdo řeknu, že má být z ISS.
   // PRINCIP:!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   // Delete an astronaut with a specified name and craft ISS.
   //
   //We begin a transaction using session.beginTransaction() to ensure that the delete operation is atomic.
   //
   //Create a query to delete the astronaut with the specified name and with the craft ISS. We use the spaceship.craftname
   // attribute to specify the craft name related to the astronaut using the ManyToOne mapping.
   //
   // We use a parameter (:name) for the astronaut's name, which we set using deleteQuery.setParameter("name", astronautName).
   //We execute the delete query using deleteQuery.executeUpdate() and store the number of rows affected.
   //
   //After the delete operation is complete, we commit the transaction using transaction.commit().
   //
   //We return true if one or more rows were affected, indicating that an astronaut was deleted successfully. If an error occurs, we return false.
   public boolean deleteAstronautByNameWithCraftISS(Session session, String astronautName) {
       try {
           // Begin a transaction. V našem případě není možné, protože už máme spuštěnou transakci v AppMain!!!!!!!!!!!!!!!!!!
           // Transaction transaction = session.beginTransaction();

           // Create a query to delete the astronaut with the specified name and craft ISS
           Query deleteQuery = session.createQuery("DELETE FROM AstronautEntity ae WHERE ae.name = :name AND ae.spaceship.craftname = 'ISS'");
           deleteQuery.setParameter("name", astronautName);

           // Execute the delete query
           int rowsAffected = deleteQuery.executeUpdate();

           // Commit the transaction
           // transaction.commit();

           // Check if any rows were affected (an astronaut was deleted)
           return rowsAffected > 0;
       } catch (Exception e) {
           e.printStackTrace();
           return false; // Return false in case of an error
       }
   }


}
