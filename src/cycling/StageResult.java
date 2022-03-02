package cycling;

// Stores the results of stage results

import java.time.LocalTime;

public class StageResult {
    private Stage stage;
    private Rider rider;
    private LocalTime time;

    StageResult(Stage stage, Rider rider, LocalTime time) {
        this.stage = stage;
        this.rider = rider;
        this.time = time;
    }

    public LocalTime getTime() {
        return time;
    }
}
