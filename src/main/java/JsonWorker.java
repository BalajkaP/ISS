import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entityes.AstronautEntity;
import entityes.SpaceshipEntity;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonWorker {

    public JsonElement jsonParser(String jsonFilePath) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(reader);
        reader.close();
        return jsonElement;

    }

//  ZDE ZAČÍNÁ PŮVODNÍ VERZE metody jsonPersonLoaderToDatabase
//  public void jsonPersonLoaderToDatabase(Session session, JsonElement jsonElement) {

//        try {
//            Session session = DbConnect.getSession();
//            Transaction transaction = session.beginTransaction();

    // Test jestli je JSON Object
//            if (jsonElement.isJsonObject()) {
//                JsonObject jsonObject = jsonElement.getAsJsonObject();
//                // Jde o ten celý objekt people z JSON
//                // Vytvoříme pole Json objektů ze zadaného parsovaného Json souboru
//                JsonArray peopleArray = jsonObject.getAsJsonArray("people");
//
//                // Zde vytvořím kolekci s unikátními záznamy (díky HashMap), která uchovává Mapu těch Key-Value pairs
//                // Map<String, SpaceshipEntity> . Jako KEY si tam ukládám tu string hodnotu "craft" field z Json
//                // a jako Value je příslušný momentálně vzniklý objekt SpaceshipEntity
//                Map<String, SpaceshipEntity> spacecraftMap = new HashMap<>();
//
//                // Iterace přes JSON pole a uložení dat do databáze
//                for (JsonElement personElement : peopleArray) {
//                    JsonObject personObject = personElement.getAsJsonObject();
//                    // Here we take precise names of object fields v každém Json objektu
//                    // Tj. uložíme si do proměnné každou položku každého jednotlivého objektu
//                    String name = personObject.get("name").getAsString();
//                    String craft = personObject.get("craft").getAsString();
//
//                // Vytvořím AstronautEntity a načtu data
//                    AstronautEntity person = new AstronautEntity();
//                    person.setName(name);      // here we set the name field from AstronautEntity
//                    person.setCraftname(craft);  // here we set the craftname field from AstronautEntity
//                                                 // craft object is created here little bit above
//               //--------------------------------------------------------------------------------------------
//                    // Toto zde přidám , abych načetl taky SpaceshipEntity
//                    // Check if the spaceship entity already exists in the map - pomocí get si zjistím, zda je tam již
//                    // VALUE (tj.SpaceshipEntity) pro daný unikátní KEY (string).
//                    SpaceshipEntity spaceship = spacecraftMap.get(craft);
//                    // Jen v případě, že daný KEY (craft) není ještě v mapě, tak provede co uvnitř if
//                    if (spaceship == null) {
//                        spaceship = new SpaceshipEntity();  // zde vytvoří další objekt SpaceshipEntity= další řádek v tabulce
//                        spaceship.setCraftname(craft);      // a to tak, že nastaví craftname sloupec na daný "craft"
//                        // Associates the specified value with the specified key in this map (optional operation).
//                        // Pokud map už obsahovala daný KEY-VALUE pair (tj. mapping for the key), the old value is
//                        // replaced by the specified value.
//                        spacecraftMap.put(craft, spaceship);
//                    }
//                //------------------------------------------------------
//
//                    session.save(person);
//                    session.save(spaceship);
//                }

//                transaction.commit();
//                session.close();
//                System.out.println("Data were loaded to the database.");
//            } else {
//                System.out.println("JSON file is not Object.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//  ZDE KONČÍ PŮVODNÍ VERZE

    //------------------------------------------------------------------------------------------------
// ZDE  ZAČÍNÁ NOVÁ VERZE metody jsonPersonLoaderToDatabase
// In this code, the jsonPersonLoaderToDatabase method processes a JSON object containing astronaut names and craft names.
// For each astronaut, it creates an AstronautEntity and a SpaceshipEntity, and then it calls the
// saveSpaceshipAndAstronautsWithRelationship method to establish a relationship between the astronaut and the spaceship.
// The saveSpaceshipAndAstronautsWithRelationship method sets the astronaut as part of the spaceship's crew and establishes
// the relationship. Finally, both the spaceship and the astronaut are saved in the database.

    public void jsonPersonLoaderToDatabase(Session session, JsonElement jsonElement) {
        // Check if the JSON element is an object
        if (jsonElement.isJsonObject()) {
            // Convert the JSON element to a JSON object
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // Extract the "people" array from the JSON object
            JsonArray peopleArray = jsonObject.getAsJsonArray("people");

            // DŮLEŽITÝ KÓD PRO ZAJIŠTĚNÍ UNIKÁTNÍHO VSTUPU
            // Zde vytvořím kolekci s unikátními záznamy (díky HashMap), která uchovává Mapu těch Key-Value pairs
            // Map<String, SpaceshipEntity> . Jako KEY si tam ukládám tu string hodnotu "craft" field z Json
            // a jako Value je příslušný momentálně vzniklý objekt SpaceshipEntity
            Map<String, SpaceshipEntity> spacecraftMap = new HashMap<>();

            // Iterate over the "people" array to extract astronaut names and craft names
            for (JsonElement personElement : peopleArray) {
                JsonObject personObject = personElement.getAsJsonObject();
                String name = personObject.get("name").getAsString();
                String craft = personObject.get("craft").getAsString();

                // Create a new AstronautEntity and set its name
                AstronautEntity astronaut = new AstronautEntity();
                astronaut.setName(name); //  DŮLEŽITÉ: Zde netřeba zadávat astronaut.setCraftname(craft), jako
                // v původní verzi, protože se tento 3.sloupec vytvoří automaticky v
                // AstronautEntity převzetím z SpaceshipEntity, pomocí @JoinColumn!!!!!!!!!!!!!!!!!!!!!!!!!
                // Create a new SpaceshipEntity and set its craft name. Toto zde přidám , abych načetl taky SpaceshipEntity
                //POZOR: VELMI DŮLEŽITÉ: KÓD PRO ZAJIŠTĚNÍ UNIKÁTNÍHO VSTUPU - TJ. ŽE SE NAČTE z Json JEN 1 TYP CRAFTu DO TABULKY!!!!!!!!!!!!!!!!!!!
//                    // Check if the spaceship entity already exists in the map - pomocí get si zjistím, zda je tam již
//                    // VALUE (tj.SpaceshipEntity) pro daný unikátní KEY (string).
                SpaceshipEntity spaceship = spacecraftMap.get(craft);
//                    // Jen v případě, že daný KEY (craft) není ještě v mapě, tak provede co uvnitř if
                if (spaceship == null) {
                    spaceship = new SpaceshipEntity();  // zde vytvoří další objekt SpaceshipEntity= další řádek v tabulce
                    spaceship.setCraftname(craft);      // a to tak, že nastaví craftname sloupec na daný "craft"
//                        // PUT: Associates (začlení) the specified value with the specified key in this map (optional operation).
//                        // Pokud map už obsahovala daný KEY-VALUE pair (tj. mapping for the key), the old value is
//                        // replaced by the specified value.
                    spacecraftMap.put(craft, spaceship);
                }
                //       SpaceshipEntity spaceship = new SpaceshipEntity(); // Toto stačí když nechci zajistit UNIKÁTNÍ VSTUP
                //       spaceship.setCraftname(craft);                     // Toto stačí když nechci zajistit UNIKÁTNÍ VSTUP

                // Call the saveSpaceshipAndAstronautsWithRelationship method to establish the relationship
                saveSpaceshipAndAstronautsWithRelationship(session, spaceship, astronaut);
            }

        } else {
            // Print a message if the JSON element is not an object
            System.out.println("JSON file is not Object.");
        }
    }


    public void saveSpaceshipAndAstronautsWithRelationship(Session session, SpaceshipEntity spaceship, AstronautEntity astronaut) {
        // Add the astronaut to the spaceship's set of astronauts
        spaceship.getAstronauts().add(astronaut);
        // Set the spaceship for the astronaut
        astronaut.setSpaceship(spaceship);
        // Save both the spaceship and the astronaut
        session.save(spaceship);
        session.save(astronaut);
    }
}