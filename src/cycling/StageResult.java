package cycling;

import java.time.LocalTime;

/**
 * Stores the results of stage results
 */
public class StageResult {

    private static int totalResults = 0;

    private int id;
    private Stage stage;
    private LocalTime time;

    StageResult(Stage stage, LocalTime time) {
        this.stage = stage;
        this.time = time;
        this.id = totalResults++;
    }

    public Stage getStage() {
        return stage;
    }

    public LocalTime getTime() {
        return time;
    }
}
