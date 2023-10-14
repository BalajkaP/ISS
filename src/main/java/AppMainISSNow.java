
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AppMainISSNow {
    // To modify the main and jsonPersonLoaderToDatabase methods to read data from the "http://api.open-notify.org/iss-now.json" URL
    // and put the acquired data into a database table called IssTimeLocation, you need to follow these steps:
    //
    //We created a IssTimeLocation Hibernate entity to map the database table.
    //The main method sends an HTTP request to "http://api.open-notify.org/iss-now.json" and parses the JSON response.
    //The jsonTimeLocationLoaderToDatabase method extracts latitude, longitude, and timestamp data from the JSON response and inserts it into the IssTimeLocation table
    //timestamp in the JSON response is not in a format that can be directly parsed by LocalDateTime. The timestamp is likely
    // in epoch seconds, and you need to convert it to a LocalDateTime object correctly.
    //In this code, we use Instant.ofEpochSecond to convert the epoch seconds to an Instant, and then we use LocalDateTime.ofInstant
    // to convert the Instant to a LocalDateTime. This should handle the timestamp correctly.
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        // Define the URL for the API
        String apiUrl = "http://api.open-notify.org/iss-now.json";

        // Create an HTTP GET request to the API
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        // Send the HTTP request and get the response
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        try {
            Session session = DbConnect.getSession();
            Transaction transaction = session.beginTransaction(); // Begin a new transaction to group database operations together.

            // Check if the HTTP request was successful (status code 200)
            if (response.statusCode() == 200) {
                // Parse the JSON response
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

                // Create an instance of JsonWorker
                JsonWorkerISSNow jsonWorker = new JsonWorkerISSNow();

                // Load the JSON data into the database
                jsonWorker.jsonTimeLocationLoaderToDatabase(session, jsonObject);

            } else {
                System.out.println("HTTP request failed with status code: " + response.statusCode());
            }

            transaction.commit(); // Commit the transaction, which applies the changes to the database.
            System.out.println("Data were loaded to the database.");
            session.close(); // Close the JPA session to release resources and end the database connection.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
