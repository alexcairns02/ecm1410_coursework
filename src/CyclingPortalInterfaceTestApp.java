import cycling.CyclingPortal;
import cycling.CyclingPortalInterface;
import cycling.SegmentType;
import cycling.StageType;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortalInterface interface -- note you
 * will want to increase these checks, and run it on your CyclingPortal class
 * (not the BadCyclingPortal class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class CyclingPortalInterfaceTestApp {

	/**
	 * Test method.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the execution...");

		CyclingPortalInterface portal = new CyclingPortal();

		assert (portal.getRaceIds().length == 0)
				: "Initial SocialMediaPlatform not empty as required or not returning an empty array.";

		// Testing Race Creation
		try {
			portal.createRace("Firstrace", "A description");
			portal.createRace("Secondrace", "Another description");
			portal.createRace("Thirdrace", "No description");

			assert (portal.getRaceIds().length == 3);
			assert (portal.getNumberOfStages(1) == 0);
			assert (portal.getRaceIds()[0] == 0);

			portal.removeRaceById(0);

			assert (portal.getRaceIds().length == 2);
			assert (portal.getRaceIds()[0] == 1);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		// Testing Stage Creation
		try {
			portal.addStageToRace(1, "Firststage", "The first stage", 10, LocalDateTime.now(), StageType.FLAT);
			portal.addStageToRace(1, "Secondstage", "The second stage", 15, LocalDateTime.now(), StageType.HIGH_MOUNTAIN);
			portal.addCategorizedClimbToStage(1, 3.0, SegmentType.C1, 43.0, 2.0);
			portal.addIntermediateSprintToStage(1, 2.0);
			portal.addIntermediateSprintToStage(1, 6.0);
			portal.removeSegment(2);
			portal.concludeStagePreparation(0);
			portal.concludeStagePreparation(1);

			assert (portal.getNumberOfStages(1) == 2);
			assert (portal.getRaceStages(1).length == 2);
			assert (portal.getStageSegments(1)[0] == 1);
			assert (portal.getStageLength(1) == 15);

			portal.addStageToRace(1, "Thirdstage", "The third stage", 5, LocalDateTime.now(), StageType.TT);
			portal.concludeStagePreparation(2);
			portal.removeStageById(2);
			assert (portal.getNumberOfStages(1) == 2);

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		// Testing rider creation
		try {
			int team1 = portal.createTeam("team1", "the first team");
			portal.createTeam("team2", "the second team");
			portal.removeTeam(1);

			portal.createRider(team1, "rider1", 1994);
			portal.registerRiderResultsInStage(0, 0, LocalTime.of(1, 2, 15), LocalTime.of(1, 15, 23));
			portal.registerRiderResultsInStage(1, 0, LocalTime.of(0, 0, 0), LocalTime.of(0, 15, 30), LocalTime.of(0, 20, 15), LocalTime.of(0, 45, 13));
			portal.createRider(team1, "rider2", 1994);
			portal.registerRiderResultsInStage(0, 1, LocalTime.of(1, 2, 15), LocalTime.of(1, 14, 01));
			portal.registerRiderResultsInStage(1, 1, LocalTime.of(0, 0, 0), LocalTime.of(0, 18, 13), LocalTime.of(0, 23, 43), LocalTime.of(0, 52, 24));
			portal.createRider(team1, "rider3", 1994);
			portal.registerRiderResultsInStage(0, 2, LocalTime.of(1, 2, 15), LocalTime.of(1, 14, 25));
			portal.registerRiderResultsInStage(1, 2, LocalTime.of(0, 0, 0), LocalTime.of(0, 14, 30), LocalTime.of(0, 17, 36), LocalTime.of(0, 49, 2));
			
			portal.createRider(team1, "rider4", 1920);
			portal.removeRider(3);

			assert (portal.getTeams().length == 1);
			assert (portal.getTeamRiders(0).length == 3);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		// Testing getAdjustedElapsedTimes()
		try {
			int team2 = portal.createTeam("team2", "the second team");
			assert (team2 == 2);

			int id1 = portal.createRider(team2, "team2rider1", 1994);
			portal.registerRiderResultsInStage(0, id1, LocalTime.of(1, 2, 15), LocalTime.of(1, 15, 22, 500000000));
			int id2 = portal.createRider(team2, "team2rider2", 1994);
			portal.registerRiderResultsInStage(0, id2, LocalTime.of(1, 2, 15), LocalTime.of(1, 15, 23, 800000000));

			assert (portal.getRiderAdjustedElapsedTimeInStage(0, id1).equals(portal.getRiderAdjustedElapsedTimeInStage(0, id2)));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		// Testing race results
		try {
			System.out.println(portal.viewRaceDetails(1));
			System.out.println(Arrays.toString(portal.getGeneralClassificationTimesInRace(1)));
			System.out.println(Arrays.toString(portal.getRidersGeneralClassificationRank(1)));
			System.out.println(Arrays.toString(portal.getRidersPointsInRace(1)));
			System.out.println(Arrays.toString(portal.getRidersPointClassificationRank(1)));
			System.out.println(Arrays.toString(portal.getRidersMountainPointsInRace(1)));
			System.out.println(Arrays.toString(portal.getRidersMountainPointClassificationRank(1)));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		// Testing serialisation methods
		CyclingPortalInterface portal2 = new CyclingPortal();
		try {
			portal.saveCyclingPortal("portal.ser");
			portal2.loadCyclingPortal("portal.ser");
			assert (Arrays.toString(portal.getRaceIds()).equals(Arrays.toString(portal2.getRaceIds())));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		// Testing eraseCyclingPortal()
		try {
			portal.eraseCyclingPortal();
			assert (portal.getRaceIds().length == 0);
			assert (portal.getTeams().length == 0);
			
			portal.createRace("Firstrace", "the first race");
			assert (portal.getRaceIds()[0] == 0);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
