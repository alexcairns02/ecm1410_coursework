package cycling;

import java.time.LocalTime;

/**
 * Stores the results of stage results
 */
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
