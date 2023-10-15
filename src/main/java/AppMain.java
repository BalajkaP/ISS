import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entityes.AstrocraftISSEntity;
import entityes.AstronautEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AppMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        // POZOR: Projekt má 2 verze (nejedná se o ty 2 způsoby řešení). JDE O PŮVODNÍ VERZI A NOVOU VERZI.
        // PŮVODNÍ VERZE je založená na jednoduché definici sloupců v entitách pomocí @Column- žádné vztahy 1:M.
        // NOVÁ VERZE je založená na implementaci 1:M vztahu pomocí @OneToMany(mappedBy....),@JoinColumn(name = "craftname",...)
        // anotací.

        // DŮLEŽITÉ INFO: NESMÍ BÝT AKTIVNÍ PŘIPOJENÍ Z DB Browser, pokud chci mazat tabulky, nebo jiný QUERY v MySQL Workbench.
        // Jinak dojde ke KONFLIKTU, A ZABLOKUJE WORKBENCH. V TOM PŘÍPADĚ MUSÍM PRAVÝ KLIK NA CONNECTION V DB Browser a dát
        // DISCONNECT

        // ŘEŠENÍ ÚLOHY 2 ZPŮSOBY:

        // 1. ZPŮSOB: TENTO KÓD VYUŽÍVÁ PŘÍMO HTTP DOTAZ NA DANOU URL (STRÁNKU S API), A TA NÁM VRÁTÍ POŽADOVANÉ ÚDAJE.
        //            NA TÉTO STRÁNCE UŽ JE VYTVOŘENÉ NĚJAKÉ ROZHRANÍ, KTERÉ KOMUNIKUJE S nějakou eterní DB.
        //            VRÁTÍ NÁM ÚDAJE TYPU JSON - ale ty jsou jako String v responseBody (viz. klasický zápis JSON
        //            pomocí {} ). TO responseBody MUSÍME ZDE V KÓDU PŘEVÉST (ROZEBRAT, PARSE) pomocí getAsJsonObject NA
        //            JSON OBJEKT (JsonObject), KTERÝ PAK POMOCÍ JsonWorker TŘÍDY ZPRACUJI A NAHRAJU DO DATABÁZE
        //            V JsonWorker extrahujeme jednotlivé fields (prvky) z našeho JsonObject (který je zde uložen v poli).
        // DŮLEŽITÉ: TEN 2.ZPŮSOB (VIZ. NÍŽE) FUNGUJE VELICE PODOBNĚ, JEN ZÍSKÁNÍ JsonObject JE JINÉ - TEDY TA PRVNÍ ČÁST.
        //           V TOMTO PŘÍPADĚ, Z LOKÁLNÍHO ZKOPÍROVANÉHO JSON SOUBORU, POMOCÍ jsonParser() PŘEVEDE SOUBOR TYPU
        //           JSON NA JsonObject= JsonElement. ZPRACOVÁNÍ PAK STEJNĚ JAKO U 1.ZPŮSOBU POMOCÍ METODY jsonPersonLoaderToDatabase
        //           Z JsonWorker.

        // Create an instance of HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Define the URL for the API
        String apiUrl = "http://api.open-notify.org/astros.json";

        // Create an HTTP GET request to the API
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        // Send the HTTP request and get the response
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        try {
            Session session = DbConnect.getSession();
            Transaction transaction = session.beginTransaction(); //Begin a new transaction to group database operations together.
            // Check if the HTTP request was successful (status code 200)
            if (response.statusCode() == 200) {
                // Parse the JSON response . Z response.body zde získám přímo ten JSON soubor (JsonObject).
                String responseBody = response.body();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                // Create an instance of JsonWorker
                JsonWorker jsonWorker = new JsonWorker();


                // Load the JSON data into the database
                jsonWorker.jsonPersonLoaderToDatabase(session, jsonObject);

            } else {
                System.out.println("HTTP request failed with status code: " + response.statusCode());
            }
            transaction.commit(); // Commit the transaction, which applies the changes to the database.
            System.out.println("Data were loaded to the database.");


            //**********************************************************************************************************
            //!!!!!!!!!!!!!!!!!!!!!! VOLÁM RŮZNÉ QUERY METODY ULOŽENÉ V CLASS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            //Begin a new transaction to group database operations together.
            transaction.begin();                         // Spustím druhou transakci té naší session

            // 1.  Retrieve astronauts with craftname "ISS" . POUŽITÍ PŘÍKAZU WHERE. VÝSTUPY NA OBRAZOVKU, DO DATABASE , DO SOUBORU
            //       Využívám metodu: getAstronautsWithCraftISS
            VariousDbQuery variousDbQuery = new VariousDbQuery();
            // Zde je náš seznam astronautů získaný pomocí HQL
            List<AstronautEntity> astronautsWithCraftISS = variousDbQuery.getAstronautsWithCraftISS(session);
            // TOTO DŮLEŽITÉ PRO ULOŽENÍ DO DATABASE. Create a new list to store instances of AstrocraftISSEntity.
            List<AstrocraftISSEntity> astrocraftISSList = new ArrayList<>();

            // ZDE PROVEDU VÝSTUPY NA OBRAZOVKU, DO DATABASE , DO SOUBORU
            for (AstronautEntity astronaut : astronautsWithCraftISS) {
                // a) Výstup na obrazovku
                System.out.println("Astronaut Name: " + astronaut.getName());
                //System.out.println("Craft Name: " + astronaut.getCraftname());  // Toto platí pro původní verzi
                System.out.println("Craft Name: " + astronaut.getSpaceship().getCraftname()); // Toto platí pro NOVOU verzi

                // b) Výstup do databáze a souboru - zde jen příprava finálního seznamu pro uložení
                // Create a new instance of AstrocraftISSEntity for each astronaut.
                AstrocraftISSEntity astrocraftISSEntity = new AstrocraftISSEntity();

                // Set the astronaut's name in the AstrocraftISSEntity instance.
                astrocraftISSEntity.setAstronautName(astronaut.getName());

                // Set the craft name by accessing it through the associated spaceship in AstronautEntity.
                astrocraftISSEntity.setCraftName(astronaut.getSpaceship().getCraftname());

                // Add the populated AstrocraftISSEntity instance to the list.
                astrocraftISSList.add(astrocraftISSEntity);

            }
            // Pokračování pro b) - Platí pro Výstup do databáze - viz. b)
            // Iterate through the list of AstrocraftISSEntity objects.
            for (AstrocraftISSEntity astrocraftISSEntity : astrocraftISSList) {
                // Save the current AstrocraftISSEntity instance to the database.
                session.save(astrocraftISSEntity);
            }
            // c) Výstup do souboru
            // Pokračování z b)
            // Specify the ABSOLUTE path to the output text file
            //String filePath = "C:\\PROGRAMOVANI_SKOLENI\\SKOLENI SDA\\PROGRAMY\\ISS\\src\\main\\java\\files\\astrocraftoutput.txt";
            // Specify the RELATIVE path to the output text file
            String filePath = "astrocraftoutput.txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Astronaut-Craft Entry: \n" + "\n");
                //todo Zde se mazání děje automaticky díky funkci writer.write , protože ta zapisuje od začátku souboru a ne append
                for (AstrocraftISSEntity astrocraftISSEntity : astrocraftISSList) {
                    // Write the data to the text file
                    writer.write("Astronaut Name: " + astrocraftISSEntity.getAstronautName());
                    writer.newLine(); // Add a new line
                    writer.write("Craft Name: " + astrocraftISSEntity.getCraftName());
                    writer.newLine(); // Add a new line
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //----------------------------------------------------------------------------------------------
            // 2. DELETE astronaut with specified name.
            // POUŽITÍ PŘÍKAZU: session.createQuery("DELETE FROM AstronautEntity ae WHERE ae.astronautName = :name AND ae.craftName = 'ISS'")
    //      boolean resstate = variousDbQuery.deleteAstronautByNameWithCraftISS(session, "Jasmin Moghbeli");

//********************************************************************************************************************
            // 2. ZPŮSOB: TENTO KÓD POUŽÍVÁ JIŽ ZTAŽENÝ SOUBOR iss.json, který mám uložený buď někde na disku,
            //            nebo si ho vytvořím přímo zde v resources. Mohu si ho vytvořit tak, že klik na odkaz
            //            http://api.open-notify.org/astros.json , a zkopíruju jeho obsah.
            //            Pak v resources vytvořím po pravý klik na main -NEW FILE - iss.json, a do něj zkopíruju obsah
            //            souboru astros.json. Abych tedy mohl zadat relativní cestu, tak raději ten soubor dát přímo pod main
            //

//        JsonWorker jsonWorker = new JsonWorker();
//    // Do jsonParser zadám cestu k mojemu iss.json. Dát pravý klik na iss.json vlevo v TREE v INTELLIJ, a
//    // COPY PATH/REFERENCE - Path from content Root zvolit.
//        JsonElement jsonElementPeople = jsonWorker.jsonParser("src/main/iss.json");
//    // Zde volám metodu jsonPersonLoaderToDatabase , která hodí objekty z JSON souboru do DB.
//    // Tyto objekty jsou již pomocí metody jsonParser převedeny do objektu  jsonElementPeople, s kterým pak
//    // může metoda jsonPersonLoaderToDatabase pracovat. Toto řešení navrhl CHAT GPT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!§
//        jsonWorker.jsonPersonLoaderToDatabase(jsonElementPeople);
// *****************************************************************************************************************
            transaction.commit();
            session.close(); // Close the JPA session to release resources and end the database connection.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
