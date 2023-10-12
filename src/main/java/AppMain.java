import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entityes.AstronautEntity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AppMain {
    public static void main(String[] args) throws IOException, InterruptedException {

    // DŮLEŽITÉ INFO: NESMÍ BÝT AKTIVNÍ PŘIPOJENÍ Z DB Browser, pokud chci mazat tabulky, nebo jiný QUERY v MySQL Workbench.
    // Jinak dojde ke KONFLIKTU, A ZABLOKUJE WORKBENCH. V TOM PŘÍPADĚ MUSÍM PRAVÝ KLIK NA CONNECTION V DB Browser a dát
    // DISCONNECT

    // ŘEŠENÍ ÚLOHY 2 ZPŮSOBY:

    // 1. ZPŮSOB: TENTO KÓD VYUŽÍVÁ PŘÍMO HTTP DOTAZ NA DANOU STRÁNKU S API, A TA NÁM VRÁTÍ POŽADOVANÉ ÚDAJE.
    //            NA TÉTO STRÁNCE UŽ JE VYTVOŘENÉ NĚJAKÉ ROZHRANÍ, KTERÉ KOMUNIKUJE S DB.
    //            VRÁTÍ NÁM ÚDAJE V SOUBORU TYPU JSON, A TEN MUSÍME ZDE V KÓDU PŘEVÉST (ROZEBRAT, PARSE)
    //            NA JSON OBJEKT - JsonObject, KTERÝ PAK POMOCÍ JsonWorker TŘÍDY NAHRAJU DO DATABÁZE

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

        // Check if the HTTP request was successful (status code 200)
        if (response.statusCode() == 200) {
            // Parse the JSON response
            String responseBody = response.body();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

            // Create an instance of JsonWorker
            JsonWorker jsonWorker = new JsonWorker();

            // Load the JSON data into the database
            jsonWorker.jsonPersonLoaderToDatabase(jsonObject);
        } else {
            System.out.println("HTTP request failed with status code: " + response.statusCode());
        }

        //**********************************************************************************************************
        //!!!!!!!!!!!!!!!!!!!!!! VOLÁM RŮZNÉ QUERY METODY ULOŽENÉ V CLASS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // 1.  Retrieve astronauts with craftname "ISS"
        VariousDbQuery variousDbQuery= new VariousDbQuery();
        List<AstronautEntity> astronautsWithCraftISS = variousDbQuery.getAstronautsWithCraftISS();
        for (AstronautEntity astronaut : astronautsWithCraftISS) {
            System.out.println("Astronaut Name: " + astronaut.getName());
            System.out.println("Craft Name: " + astronaut.getCraftname());
        }
        //----------------------------------------------------------------------------------------------
        // 2.  Retrieve .............



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

    }
}
