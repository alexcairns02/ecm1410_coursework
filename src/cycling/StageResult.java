package cycling;

import java.time.LocalTime;

/**
 * A rider's result in a particular stage
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
