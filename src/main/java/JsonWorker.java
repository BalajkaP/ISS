import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entityes.AstronautEntity;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
                JsonArray peopleArray = jsonObject.getAsJsonArray("people");

                // Iterace přes JSON pole a uložení dat do databáze
                for (JsonElement personElement : peopleArray) {
                    JsonObject personObject = personElement.getAsJsonObject();
                    String name = personObject.get("name").getAsString();
                    String craft = personObject.get("craft").getAsString();

                    AstronautEntity person = new AstronautEntity();
                    person.setName(name);
                    person.setCraft(craft);

                    session.save(person);
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