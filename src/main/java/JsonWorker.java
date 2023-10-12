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
import java.util.Map;

public class JsonWorker {

    public JsonElement jsonParser(String jsonFilePath) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(reader);
        reader.close();
        return jsonElement;

    }

    public void jsonPersonLoaderToDatabase(JsonElement jsonElement) {

        try {
            Session session = DbConnect.getSession();
            Transaction transaction = session.beginTransaction();

            // Test jestli je JSON Object
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                // Jde o ten celý objekt people z JSON
                // Vytvoříme pole Json objektů ze zadaného parsovaného Json souboru
                JsonArray peopleArray = jsonObject.getAsJsonArray("people");

                // Zde vytvořím kolekci s unikátními záznamy (díky HashMap), která uchovává Mapu těch Key-Value pairs
                // Map<String, SpaceshipEntity> . Jako KEY si tam ukládám tu string hodnotu "craft" field z Json
                // a jako Value je příslušný momentálně vzniklý objekt SpaceshipEntity
                Map<String, SpaceshipEntity> spacecraftMap = new HashMap<>();

                // Iterace přes JSON pole a uložení dat do databáze
                for (JsonElement personElement : peopleArray) {
                    JsonObject personObject = personElement.getAsJsonObject();
                    // Here we take precise names of object fields v každém Json objektu
                    // Tj. uložíme si do proměnné každou položku každého jednotlivého objektu
                    String name = personObject.get("name").getAsString();
                    String craft = personObject.get("craft").getAsString();

                    AstronautEntity person = new AstronautEntity();
                    person.setName(name);      // here we set the name field from AstronautEntity
                    person.setCraftname(craft);  // here we set the craftname field from AstronautEntity
                                                 // craft object is created here little bit above
               //-----------------------------------------------
                    // Toto zde přidám , abych načetl taky SpaceshipEntity
                    // Check if the spaceship entity already exists in the map - pomocí get si zjistím, zda je tam již
                    // VALUE (tj.SpaceshipEntity) pro daný unikátní KEY (string).
                    SpaceshipEntity spaceship = spacecraftMap.get(craft);
                    // Jen v případě, že daný KEY (craft) není ještě v mapě, tak provede co uvnitř if
                    if (spaceship == null) {
                        spaceship = new SpaceshipEntity();  // zde vytvoří další objekt SpaceshipEntity= další řádek v tabulce
                        spaceship.setCraftname(craft);      // a to tak, že nastaví craftname sloupec na daný "craft"
                        // Associates the specified value with the specified key in this map (optional operation).
                        // Pokud map už obsahovala daný KEY-VALUE pair (tj. mapping for the key), the old value is
                        // replaced by the specified value.
                        spacecraftMap.put(craft, spaceship);
                    }
                //------------------------------------------------------

                    session.save(person);
                    session.save(spaceship);
                }

                transaction.commit();
                session.close();
                System.out.println("Data were loaded to the database.");
            } else {
                System.out.println("JSON file is not Object.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}