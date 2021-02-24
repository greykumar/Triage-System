/* 

* Grey Kumar 

* CPSC 5002, Seattle University 

* This is free and unencumbered software released into the public domain. 

*/
package gkumar_p2;

import java.util.ArrayList;
import java.util.EmptyStackException;

/**
 * Hospital triage system implemented using a heap.
 * 
 * @author bc3soln
 */
public class PatientPriorityQueue {
	private ArrayList<Patient> patients; // heap property is always satisfied
	private int nextPatientNumber; // num assigned to next added patient

	/**
	 * Creates an empty triage system with no patients.
	 */
	public PatientPriorityQueue() {
		this.patients = new ArrayList<Patient>();
		this.nextPatientNumber = 1;
	}

	/**
	 * Gets the list of patients currently in the waiting room
	 * 
	 * @return the list of patients that have not been called
	 */
	public ArrayList<Patient> getPatientList() {
		return patients;
	}

	/**
	 * Adds a patient to the heap based of their priority code. Along with the
	 * code, the name of the patient and the number of the patient are added
	 * 
	 * @param priorityCode the number used to sort the heap
	 * @param patientName  the name of the patient
	 */
	public void addPatient(int priorityCode, String patientName) {
		Patient patObj = new Patient(priorityCode, nextPatientNumber,
				patientName);
		patients.add(patObj);
		nextPatientNumber++;
		percolateUp(patients.size() - 1);
	}

	/**
	 * This method returns the next patient at the top of the heap
	 * 
	 * @return the highest priority patient
	 */
	public Patient peek() {
		if (patients.isEmpty()) {
			throw new EmptyStackException();
		}
		return patients.get(0);
	}

	/**
	 * This method removes the highest priority patient from the heap
	 * 
	 * @return the patient at the top of the heap
	 */
	public Patient dequeue() {
		Patient val;
		if (patients.isEmpty()) {
			throw new EmptyStackException();
		} else {
			val = patients.get(0);
			patients.set(0, patients.get(patients.size() - 1));
			patients.remove(patients.size() - 1);
			if (!patients.isEmpty()) {
				percolateDown(0);
			}
		}
		return val;
	}

	/**
	 * displays the number of patients
	 * 
	 * @return the size of the array
	 */
	public int size() {
		return patients.size();
	}

	/**
	 * percolates down the heap and rearranges the data into the correct order
	 * 
	 * @param index the starting index
	 */
	private void percolateDown(int index) {
		if (index < size()) {
			Patient left;
			Patient right;
			Patient min = patients.get(index);
			int minIndex = index;

			//check left child
			if (hasLeft(index)) {
				left = patients.get(left(index));
				if (left.getPriorityCode() < min.getPriorityCode()) {
					minIndex = left(index);
					min = patients.get(minIndex);
				}
			}
			
			//check right child
			if (hasRight(index)) {
				right = patients.get(right(index));
				if (right.getPriorityCode() < min.getPriorityCode()) {
					minIndex = right(index);
					min = patients.get(minIndex);
				}
			}

			//swap
			if (minIndex != index) {
				Patient temp = patients.get(index);
				patients.set(index, patients.get(minIndex));
				patients.set(minIndex, temp);
				percolateDown(minIndex);
			}
		}
	}

	/**
	 * helper method to percolate up through the heap and organize the data based
	 * on the correct structure
	 * 
	 * @param index the index the percolation starts at
	 */
	private void percolateUp(int index) {
		Patient curr = patients.get(index);
		Patient parent = patients.get(parent(index));
		if (parent.getPriorityCode() > curr.getPriorityCode()) {
			Patient temp = parent;
			patients.set(parent(index), curr);
			patients.set(index, temp);
			percolateUp(parent(index));
		}
	}

	/**
	 * helper method to determine whether of not a parent index has a left child
	 * 
	 * @param parent the parent index
	 * @return true if has a left child
	 */
	private boolean hasLeft(int parent) {
		return 2 * parent + 1 < patients.size();
	}

	/**
	 * helper method to determine whether or not a parent index has a right child
	 * 
	 * @param parent
	 * @return true if it has a right child
	 */
	private boolean hasRight(int parent) {
		return parent * 2 + 2 < patients.size();
	}

	/**
	 * helper method to determine the left child of a parent index
	 * 
	 * @param parent the parent index
	 * @return the left child of the parent index
	 */
	private int left(int parent) {
		return (2 * parent) + 1;
	}

	/**
	 * a helper method to determine the right child of a parent index
	 * 
	 * @param parent the parent index
	 * @return the right child of the parent index
	 */
	private int right(int parent) {
		return (2 * parent) + 2;
	}

	/**
	 * helper method to determine parent of an index
	 * 
	 * @param child the child index
	 * @return the parent of the child index
	 */
	private int parent(int child) {
		return (child - 1) / 2;
	}

}
