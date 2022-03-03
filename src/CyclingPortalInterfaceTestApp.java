import cycling.CyclingPortal;
import cycling.CyclingPortalInterface;
import cycling.IllegalNameException;
import cycling.InvalidNameException;
import cycling.StageType;

import java.time.LocalDateTime;
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

		try {
			portal.createRace("Firstrace", "A description");
			portal.createRace("Secondrace", "Another description");
			portal.createRace("Thirdrace", "No description");
		} catch (IllegalNameException e) {
			System.out.println(e);
		} catch (InvalidNameException e) {
			System.out.println(e);
		} finally {
			System.out.println(""+portal.getRaceIds().length);
			System.out.println(Arrays.toString(portal.getRaceIds()));
		}
		try {
			portal.addStageToRace(1, "First stage", "The first stage", 10, LocalDateTime.now(), StageType.FLAT);
			portal.addStageToRace(0, "First stage different race", "The first stage", 10, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN);
			portal.addStageToRace(1, "Second stage", "The second stage", 10, LocalDateTime.now(), StageType.FLAT);
			System.out.println(portal.viewRaceDetails(1));
			System.out.println(Arrays.toString(portal.getRaceStages(1)));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
