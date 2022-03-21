import cycling.CyclingPortal;
import cycling.CyclingPortalInterface;
import cycling.IllegalNameException;
import cycling.InvalidNameException;
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
		} catch (IllegalNameException e) {
			e.printStackTrace(System.out);
		} catch (InvalidNameException e) {
			e.printStackTrace(System.out);
		} finally {
			assert (portal.getRaceIds().length == 3);
		}

		// Testing Stage Creation
		try {
			portal.addStageToRace(1, "Firststage", "The first stage", 10, LocalDateTime.now(), StageType.FLAT);
			portal.addStageToRace(1, "Secondstage", "The second stage", 15, LocalDateTime.now(), StageType.HIGH_MOUNTAIN);
			portal.addCategorizedClimbToStage(1, 3.0, SegmentType.C1, 43.0, 2.0);

			int team1 = portal.createTeam("team1", "the first team");
			portal.createRider(team1, "rider1", 1994);
			portal.registerRiderResultsInStage(0, 0, LocalTime.of(1, 2, 15), LocalTime.of(1, 15, 23));
			portal.createRider(team1, "rider2", 1994);
			portal.registerRiderResultsInStage(0, 1, LocalTime.of(1, 2, 15), LocalTime.of(1, 14, 01));
			portal.registerRiderResultsInStage(1, 1, LocalTime.of(1, 2, 15), LocalTime.of(1, 14, 01), LocalTime.of(1, 18, 45));
			portal.createRider(team1, "rider3", 1994);
			portal.registerRiderResultsInStage(0, 2, LocalTime.of(1, 2, 15), LocalTime.of(1, 14, 25));
			portal.registerRiderResultsInStage(1, 2, LocalTime.of(1, 2, 15), LocalTime.of(1, 14, 25), LocalTime.of(1, 17, 54));

			assert (portal.getRaceStages(0).length == 0);
			assert (portal.getRaceStages(1).length == 2);
			assert (portal.getTeams().length == 1);
			assert (portal.getTeamRiders(0).length == 3);
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

		// Testing getAdjustedElapsedTimes()
		try {
			int team2 = portal.createTeam("team2", "the second team");
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
			System.out.println(Arrays.toString(portal.getGeneralClassificationTimesInRace(1)));
			System.out.println(Arrays.toString(portal.getRidersGeneralClassificationRank(1)));
			System.out.println(Arrays.toString(portal.getRidersPointsInRace(1)));
			System.out.println(Arrays.toString(portal.getRidersPointClassificationRank(1)));
			System.out.println(Arrays.toString(portal.getRidersMountainPointsInRace(1)));
			System.out.println(Arrays.toString(portal.getRidersMountainPointClassificationRank(1)));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
