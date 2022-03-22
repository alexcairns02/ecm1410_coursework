package cycling;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * StageResult class.<br>
 * Represents a single StageResult in the cycling competition.
 *
 * @author Joey Griffiths and Alexander Cairns
 *
 */
class StageResult implements Serializable {

    /**
     * The number of instances of StageResult, automatically incremented when
     * the constructor is called.<br>
     * Used for allocation of IDs.
     */
    private static int totalResults = 0;

    /**
     * The ID of the stage result.
     */
    private final int id;

    /**
     * The Stage object that this stage result is associated with.
     */
    private final Stage stage;

    /**
     * An array of checkpoints in the stage result (time at end of each
     * segment.)
     */
    private final LocalTime[] checkpoints;

    /**
     * StageResult class constructor.<br>
     * Assigns a stage, a list of checkpoints, and an automatic ID using the
     * number of instances of stageResult.
     *
     * @param stage The stage that this stage result is associated with.
     * @param checkpoints The list of checkpoints in the stage result.
     */
    StageResult(Stage stage, LocalTime... checkpoints) {
        this.stage = stage;
        this.checkpoints = checkpoints;
        this.id = totalResults++;
    }

    /**
     * Resets the static variable totalResults.<br>
     * Used to reset the CyclingPortal so that IDs start from 0 again.
     */
    public static void resetTotalResults() {
        totalResults = 0;
    }

    /**
     * Method to get the ID of the stage result.
     *
     * @return The ID of the stage result.
     */
    public int getId() {
        return id;
    }

    /**
     * Method to get the stage that this stage result is associated with.
     *
     * @return The stage result's stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Method to get an array of the checkpoints stored in the stage result.
     *
     * @return The array of checkpoints.
     */
    public LocalTime[] getCheckpoints() {
        return checkpoints;
    }
}
