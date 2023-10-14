import entityes.AstronautEntity;
import entityes.IssTimeLocation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

   // Iss speed calculation
   // Zde provedu nejprve výpočet rozdílu času. Pak výpočet location (location=(longitude+latitude)/2) pro
    //každý řádek tabulky IssTimeLocation (location1 a 2). Pak rozdíl location2- location1.
    // Nakonec výpočet: speed= timestampDifference[s] / locationdifference[m]
    public void issspeed(Session session) {
        // POZOR: NETŘEBA ZDE ŽÁDNÉ TRY-CATCH , PROTOŽE SESSION JE UŽ OTEVŘENÁ V HLAVNÍM KÓDU, KDE VOLÁM TUTO METODU!!!!!!!!!!!!!!!!!

        // Query the database to retrieve the data from the IssTimeLocation table- the first two rows ordered by the timestamp column.
        String hql = "FROM IssTimeLocation ORDER BY timestamp";
        Query query = session.createQuery(hql);  // Zde si zatím jen vytvoříme QUERY, ještě ji ale nespouštíme.
        query.setMaxResults(2); // Zde totiž ještě nastavíme tento parametr pro QUERY - chceme Retrieve the first two rows
                                 // tj. chceme použít v resultu query jen první 2 řádky, kdyby bylo více řádků v tab.

        //This line executes the Hibernate query and retrieves a list of IssTimeLocation entities from the database.
        // The query retrieves all rows from the IssTimeLocation table and orders them by the timestamp column.
        // Zde tedy PROVEDU ten QUERY.
        List<IssTimeLocation> locations = query.list(); // The result is stored in the locations list.
        //This line checks if there are at least two rows in the locations list. It's important to check this condition
        // because we need data from at least two rows to calculate the timestamp and latitude differences between them.
        if (locations.size() >= 2) {
            //If there are at least two rows in the locations list, this line retrieves the first row from the list
            // (which is the row with the earliest timestamp) and assigns it to the firstLocation variable.
            IssTimeLocation firstLocation = locations.get(0);
            //Similarly, this line retrieves the second row from the list (which is the row with the second earliest timestamp)
            // and assigns it to the secondLocation variable.
            IssTimeLocation secondLocation = locations.get(1);

            // Calculates the difference in time (in seconds) between secondTimestamp and firstTimestamp by first converting them to epoch seconds
            // and then subtracting the values. The result, timestampDifference, represents the time elapsed between the two moments in seconds.
            LocalDateTime firstTimestamp = firstLocation.getTimestamp();
            LocalDateTime secondTimestamp = secondLocation.getTimestamp();
            long timestampDifference = secondTimestamp
            // secondTimestamp.atZone(ZoneId.systemDefault()): This part of the code takes the secondTimestamp (which is a LocalDateTime object
            // representing a specific point in time) and associates it with a specific time zone using atZone. ZoneId.systemDefault()
            // is used to get the system's default time zone. This is necessary because LocalDateTime doesn't have a time zone associated with it,
            // and you need to specify one for the conversion to epoch seconds.
                    .atZone(ZoneId.systemDefault())
            // .toEpochSecond(): After associating the secondTimestamp with a time zone, this method is called to convert the LocalDateTime to
            // the number of seconds since the Unix epoch (January 1, 1970, UTC). This gives you the epoch second value for the secondTimestamp.
                    .toEpochSecond() - firstTimestamp
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond();

           // Zde VÝPOČET locationdifference mezi dvěma lokacema
           // Calculate location1 from latitude1 a longitude1
            double location1 = (firstLocation.getLatitude() + firstLocation.getLongitude())/2;
            // Calculate location2 from latitude2 a longitude2
            double location2 = (secondLocation.getLatitude() + secondLocation.getLongitude())/2;
            // Calculate locationdifference between the 2nd and 1st row.
            double locationdifference = location2 - location1;
            // SPEED calculation
            double speed= locationdifference/timestampDifference;

            System.out.println("Timestamp Difference: " + timestampDifference + " seconds");
            System.out.println("Location Difference: " + locationdifference + " meters");
            System.out.println("Speed: " + speed + " meters per second");

        } else {
            System.out.println("Not enough data in the table to perform calculations.");
        }
   }

}
