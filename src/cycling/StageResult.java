package cycling;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * A rider's result in a particular stage
 */
class StageResult implements Serializable {

    private static int totalResults = 0;

    private int id;
    private Stage stage;
    private LocalTime[] checkpoints;

    StageResult(Stage stage, LocalTime... checkpoints) {
        this.stage = stage;
        this.checkpoints = checkpoints;
        this.id = totalResults++;
    }

    public static void resetTotalResults() {
        totalResults = 0;
    }

    public int getId() {
        return id;
    }

    public Stage getStage() {
        return stage;
    }

    public LocalTime[] getCheckpoints() {
        return checkpoints;
    }
}
