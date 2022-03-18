import cycling.CyclingPortal;
import cycling.CyclingPortalInterface;
import cycling.IllegalNameException;
import cycling.InvalidNameException;
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
			System.out.println(e);
		} catch (InvalidNameException e) {
			System.out.println(e);
		} finally {
			assert (portal.getRaceIds().length == 3);
		}

		// Testing Stage Creation
		try {
			portal.addStageToRace(1, "First stage", "The first stage", 10, LocalDateTime.now(), StageType.FLAT);
			portal.addStageToRace(0, "First stage different race", "The first stage", 10, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN);
			portal.addStageToRace(1, "Second stage", "The second stage", 10, LocalDateTime.now(), StageType.FLAT);
			int team1 = portal.createTeam("team1", "the first team");
			portal.createRider(team1, "rider1", 1994);
			portal.registerRiderResultsInStage(0, 0, LocalTime.of(1, 2, 15), LocalTime.of(1, 15, 23));
			assert (portal.getRaceStages(0).length == 1);
			assert (portal.getRaceStages(1).length == 2);
			assert (portal.getTeams().length == 1);
			assert (portal.getTeamRiders(0).length == 1);
		} catch (Exception e) {
			System.out.println(e);
		}

		// Testing serialisation methods
		CyclingPortalInterface portal2 = new CyclingPortal();
		try {
			portal.saveCyclingPortal("portal.ser");
			portal2.loadCyclingPortal("portal.ser");
			assert (Arrays.toString(portal.getRaceIds()) == Arrays.toString(portal2.getRaceIds()));
		} catch (Exception e) {
			System.out.println(e);
		}

		try {
			System.out.println(Arrays.toString(portal.getGeneralClassificationTimesInRace(1)));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
