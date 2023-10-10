import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AppMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Create an instance of HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Define the URL for the API
        String apiUrl = "http://api.open-notify.org/astros.json";

        // Create an HTTP request to the API
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
    }
}
