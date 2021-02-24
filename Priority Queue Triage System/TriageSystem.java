/* 

* Grey Kumar 

* CPSC 5002, Seattle University 

* This is free and unencumbered software released into the public domain. 

*/
package gkumar_p2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Scanner;

/**
 * This class sets up the triage interface. It allows the user to see all the 
 * commands necessary to run the program, and allows the user to input data
 * and manipulate the contents of the heap.
 * 
 * @author Grey Kumar
 */
public class TriageSystem {
	private static final String MSG_WELCOME = "Welcome to the triage priority "
			+ "system! Enter 'help' for list of commands.";
	private static final String MSG_GOODBYE = "Thanks for using the triage "
			+ "priority system.";
	private static final String MSG_HELP = "add <priority-code> <patient-name>"
			+ "\r\n" + "            Adds the patient to the triage system.\r\n"
			+ "            <priority-code> must be one of the 4 accepted priority "
			+ "codes:\r\n"
			+ "                1. immediate 2. emergency 3. urgent 4. minimal\r\n"
			+ "            <patient-name>: patient's full legal name (may contain "
			+ "spaces)\r\n"
			+ "next        Announces the patient to be seen next. Takes into account"
			+ " the\r\n"
			+ "            type of emergency and the patient's arrival order.\r\n"
			+ "peek        Displays the patient that is next in line, but keeps in"
			+ " queue\r\n"
			+ "list        Displays the list of all patients that are still waiting"
			+ "\r\n" + "            in the order that they have arrived.\r\n"
			+ "load <file> Reads the file and executes the command on each line"
			+ "\r\n" + "help        Displays this menu\r\n"
			+ "quit        Exits the program";
	private static boolean keepAsking = true;

	/**
	 * Entry point of the program
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println(MSG_WELCOME);

		Scanner console = new Scanner(System.in);
		PatientPriorityQueue priQueue = new PatientPriorityQueue();
		while (keepAsking) {
			System.out.print("\ntriage> ");
			String line = console.nextLine();
			processLine(line, priQueue);
		}

		System.out.println(MSG_GOODBYE);
	}

	/**
	 * Process the line entered from the user or read from the file
	 * 
	 * @param line     String command to execute
	 * @param priQueue Priority Queue to operate on
	 */
	private static void processLine(String line, PatientPriorityQueue priQueue) {
		
		try {
			Scanner lineScanner = new Scanner(line); // Scanner to extract words
			String cmd = lineScanner.next(); // The first is user's command

		
			// A switch statement could be used on strings, but not all have JDK7
			if (cmd.equals("help")) {
				System.out.println(MSG_HELP);
			} else if (cmd.equals("add")) {
				addPatient(lineScanner, priQueue);
			} else if (cmd.equals("peek")) {
				peekNextPatient(priQueue);
			} else if (cmd.equals("next")) {
				dequeueNextPatient(priQueue);
			} else if (cmd.equals("list")) {
				showPatientList(priQueue);
			} else if (cmd.equals("load")) {
				executeCommandsFromFile(lineScanner, priQueue);
			} else if (cmd.equals("debug")) {
				System.out.println(priQueue.toString());
			} else if (cmd.equals("quit")) {
				keepAsking = false;
			} else {
				System.out.println("Error: unrecognized command: " + line);
			}
		} catch (Exception e) {
			System.out.println("Error: unrecognized command: " + line);
		}
	}

	/**
	 * Reads a text file with each command on a separate line and executes the
	 * lines as if they were typed into the command prompt.
	 * 
	 * @param lineScanner Scanner remaining characters after the command `load`
	 * @param priQueue    priority queue to operate on
	 */
	private static void executeCommandsFromFile(Scanner lineScanner,
			PatientPriorityQueue priQueue) {
		// read the rest of the line into a single string
		String fileName = lineScanner.nextLine().trim();

		try {
			Scanner file = new Scanner(new File(fileName));
			while (file.hasNext()) {
				final String line = file.nextLine();
				System.out.println("\ntriage> " + line);
				processLine(line, priQueue);
			}
			file.close();
		} catch (FileNotFoundException e) {
			System.out.printf("File %s was not found.%n", fileName);
		}
	}

	/**
	 * Displays the next patient in the waiting room that will be called.
	 * 
	 * @param priQueue priority queue to operate on
	 */
	private static void peekNextPatient(PatientPriorityQueue priQueue) {
		try {
			System.out.println("Highest priority patient to be called next: " +
					priQueue.peek().getName());
		}catch (EmptyStackException e){
			System.out.println("There are no patients in the waiting area.");
		}
	}

	/**
	 * Displays the list of patients in the waiting room.
	 * 
	 * @param priQueue priority queue to operate on
	 */
	private static void showPatientList(PatientPriorityQueue priQueue) {
		System.out.println("# patients waiting: " + priQueue.size() + "\n");
		System.out.println("  Arrival #   Priority Code   Patient Name\n"
				+ "+-----------+---------------+--------------+");
		ArrayList<Patient> list = priQueue.getPatientList();

		for (Patient pat : list) {
			System.out.printf("%-5s", "     ");
			System.out.printf("%-9d", pat.getArrivalOrder());
			if (pat.getPriorityCode() == 1) {
				System.out.printf("%-16s", "immediate");
			} else if (pat.getPriorityCode() == 2) {
				System.out.printf("%-16s", "emergency");
			} else if (pat.getPriorityCode() == 3) {
				System.out.printf("%-16s", "urgent");
			} else if (pat.getPriorityCode() == 4) {
				System.out.printf("%-16s", "minimal");
			}
			System.out.printf("%-10s", pat.getName());
			System.out.println();
		}

	}

	/**
	 * Removes a patient from the waiting room and displays the name on the
	 * screen.
	 * 
	 * @param priQueue priority queue to operate on
	 */
	private static void dequeueNextPatient(PatientPriorityQueue priQueue) {	
		try {
			System.out.println("This patient will now be seen: " + 
					priQueue.dequeue().getName());
		} catch(EmptyStackException e){
			System.out.println("There are no patients in the waiting area.");
		}
	}

	/**
	 * Adds the patient to the waiting room.
	 * 
	 * @param lineScanner Scanner with remaining chars after the command
	 * @param priQueue    priority queue to operate on
	 */
	private static void addPatient(Scanner lineScanner,
			PatientPriorityQueue priQueue) {
		String str = lineScanner.nextLine();
		String[] arr = str.split(" ", 3);

		if (arr[1].equals("immediate")) {
			priQueue.addPatient(1, arr[2]);
			System.out.println(
					"Added patient \"" + arr[2] + "\" to the priority system");
		} else if (arr[1].equals("emergency")) {
			priQueue.addPatient(2, arr[2]);
			System.out.println(
					"Added patient \"" + arr[2] + "\" to the priority system");
		} else if (arr[1].equals("urgent")) {
			priQueue.addPatient(3, arr[2]);
			System.out.println(
					"Added patient \"" + arr[2] + "\" to the priority system");
		} else if (arr[1].equals("minimal")) {
			priQueue.addPatient(4, arr[2]);
			System.out.println(
					"Added patient \"" + arr[2] + "\" to the priority system");
		}
	}

}
