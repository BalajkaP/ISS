package entityes;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class IssTimeLocation {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private double latitude;
        private double longitude;
        private LocalDateTime timestamp;

//   POZOR: Tento konstrukror netřeba uvádět, už je vytvořený pomocí @AllArgsConstructor.
//          Budeme ho pak v metodě JsonWorker používat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public IssTimeLocation(double latitude, double longitude, LocalDateTime timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

}
