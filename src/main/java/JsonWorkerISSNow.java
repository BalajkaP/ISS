import com.google.gson.JsonObject;
import entityes.IssTimeLocation;
import org.hibernate.Session;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JsonWorkerISSNow {
    //The timestamp in the JSON response is not in a format that can be directly parsed by LocalDateTime. The timestamp is likely
    // in epoch seconds, and you need to convert it to a LocalDateTime object correctly.
    //In this code, we use Instant.ofEpochSecond to convert the epoch seconds to an Instant, and then we use LocalDateTime.ofInstant
    // to convert the Instant to a LocalDateTime.
    public void jsonTimeLocationLoaderToDatabase(Session session, JsonObject jsonObject) {
        if (jsonObject.has("iss_position") && jsonObject.has("timestamp")) {
            JsonObject issPosition = jsonObject.getAsJsonObject("iss_position");
            double latitude = issPosition.get("latitude").getAsDouble();
            double longitude = issPosition.get("longitude").getAsDouble();
            long epochSeconds = jsonObject.get("timestamp").getAsLong();

            // Convert epoch seconds to LocalDateTime
            //ofInstant: Obtains an instance of LocalDateTime from an Instant and zone ID.
            //This creates a local date-time based on the specified instant. First, the offset from UTC/Greenwich is
            // obtained using the zone ID and instant, which is simple as there is only one valid offset for each instant.
            // Then, the instant and offset are used to calculate the local date-time.
            //Params: instant – the instant to create the date-time from, not null zone – the time-zone, which may be an offset
            //Returns: the local date-time
            // ofEpochSecond: Obtains an instance of Instant using seconds from the epoch of 1970-01-01T00:00:00Z.
            //The nanosecond field is set to zero.
            //Params: epochSecond – the number of seconds from 1970-01-01T00:00:00Z
            //Returns: an instant
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
