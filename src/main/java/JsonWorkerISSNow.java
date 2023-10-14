import com.google.gson.JsonObject;
import entityes.IssTimeLocation;
import org.hibernate.Session;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JsonWorkerISSNow {
    public void jsonTimeLocationLoaderToDatabase(Session session, JsonObject jsonObject) {
        if (jsonObject.has("iss_position") && jsonObject.has("timestamp")) {
            JsonObject issPosition = jsonObject.getAsJsonObject("iss_position");
            double latitude = issPosition.get("latitude").getAsDouble();
            double longitude = issPosition.get("longitude").getAsDouble();
            long epochSeconds = jsonObject.get("timestamp").getAsLong();

            // Convert epoch seconds to LocalDateTime
            LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.of("UTC"));


            // Create a new IssTimeLocation entity and set its properties
            IssTimeLocation timeLocation = new IssTimeLocation(latitude, longitude, timestamp);

            // Save the timeLocation entity to the database
            session.save(timeLocation);
        } else {
            System.out.println("JSON response does not contain the expected fields.");
        }
    }
}
