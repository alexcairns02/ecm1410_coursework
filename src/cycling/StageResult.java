package cycling;

import java.time.LocalTime;

/**
 * Stores the results of stage results
 */
public class StageResult {

    private static int totalResults = 0;

    private int id;
    private Stage stage;
    private LocalTime[] checkpoints;

    StageResult(Stage stage, LocalTime... checkpoints ) {
        this.stage = stage;
        this.checkpoints = checkpoints;
        this.id = totalResults++;
    }

    public Stage getStage() {
        return stage;
    }

    public LocalTime[] getCheckpoints() {
        return checkpoints;
    }
}
