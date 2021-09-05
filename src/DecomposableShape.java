/*
 * Name: Leoul Gezu
 * Date: April 21 - April 29 2021
 * CSC 202
 * Project 3-DecomposableShape.java
 * 
 * Description:
 * 		A class used by the decomposer frame class to parse the file with the points stored in it, form a polygon and remove/restore as needed based on their importance.
 * 
 * 
 * 
 * Cite Assistance: (who and describe what; if no assistance, declare that fact)
 * 		Dr. Mueller helped me extensively with the setToSize method (specifically with removing and restoring nodes).
 * 		Shared strategy for removing and restoring nodes with Alvaro Martin Grande
 * 
 */

import java.util.Arrays;
import java.util.Scanner;
import java.awt.Polygon;
import java.io.*;

public class DecomposableShape {

	private class PointNode {
		private int xCoor;
		private int yCoor;
		private double importance;
		private PointNode prev;
		private PointNode next;

		/**
		 * Creates a PointNode object based on the x and y coordinates defined in the
		 * parameters
		 * 
		 * @param xCoor: x-coordinate
		 * @param yCoor: y-coordinate
		 */
		public PointNode(int xCoor, int yCoor) {
			this.xCoor = xCoor;
			this.yCoor = yCoor;
			importance = 0;
			prev = null;
			next = null;
		}

		/**
		 * Calculates the Euclidean distance between two point nodes object. (Used in
		 * calculateImportance() to determine node importance)
		 * 
		 * @param other: the other PointNode object
		 * @return: Euclidean distance
		 */
		public double euclidDis(PointNode other) {
			double distance = Math.sqrt(Math.pow(xCoor - other.xCoor, 2) + Math.pow(yCoor - other.yCoor, 2));
			return distance;
		}

		/**
		 * Calculates the importance of a PointNode object and sets the datafield to
		 * this value
		 */
		public void calculateImportance() {
			// [L]....[P]....[R]
			double LP = euclidDis(prev);
			double PR = euclidDis(next);
			double LR = prev.euclidDis(next);

			importance = LP + PR - LR;

		}

		/**
		 * Returns a clean tag for a point node, including its x-coordinate,
		 * y-coordinate and importance
		 */
		public String toString() {
			String tag = "x = " + xCoor + ", y = " + yCoor + ", importance = " + importance;
			return tag;
		}

	}

	private int initNumPoints = 1;
	private int currNumPoints;
	private PointNode firstNode;
	private StackADT<PointNode> stack;

	/**
	 * Creates a DecomposableShape object based on the entries in the Scanner object
	 * connected to a file. Points extracted from the file are put into a doubly
	 * linked circular list
	 * 
	 * @param input: Scanner object connected to a file
	 */
	public DecomposableShape(Scanner input) {

		PointNode current = doublyLinkedList(input);
		stack = new ArrayStack<>();
		currNumPoints = initNumPoints;

		firstNode.prev = current;
		current.next = firstNode;

		firstNode.calculateImportance();
		current = firstNode.next;

		while (current != firstNode) {
			current.calculateImportance();
			current = current.next;

		}

	}

	/**
	 * Sets the initNumPoints data-field, creates the doubly linked list and sets
	 * the firstNode data-field and returns the last place the PointNode variable
	 * current was pointing
	 */
	public PointNode doublyLinkedList(Scanner input) {
		String firstLine = input.nextLine();
		String[] firstLineArray = firstLine.split(",");

		firstNode = new PointNode(Integer.parseInt(firstLineArray[0]), Integer.parseInt(firstLineArray[1]));

		PointNode current = firstNode;

		while (input.hasNextLine()) {
			String currentLine = input.nextLine();
			String[] currentLineArray = currentLine.split(",");

			PointNode currentNode = new PointNode(Integer.parseInt(currentLineArray[0]),
					Integer.parseInt(currentLineArray[1]));

			currentNode.prev = current;
			current.next = currentNode;
			current = currentNode;

			initNumPoints++;
		}
		return current;

	}

	/**
	 * Creates a polygon object based on the x and y points in the file
	 * 
	 * @return: polygon object
	 */
	public Polygon toPolygon() {

		int[] xArr = new int[initNumPoints];
		int[] yArr = new int[initNumPoints];
		PointNode current = firstNode;

		int count = 0;
		for (int i = 0; i < initNumPoints; i++) {
			xArr[count] = current.xCoor;
			yArr[count] = current.yCoor;
			count++;
			current = current.next;
		}

		Polygon polygon = new Polygon(xArr, yArr, initNumPoints);
		return polygon;

	}

	/**
	 * Removes all the least important points based on the parameter
	 * 
	 * @param numPointsToRemove
	 */
	public void removeLeastImportantNodes(int numPointsToRemove) {

		for (int i = 0; i < numPointsToRemove; i++) {
			PointNode current = firstNode.next;
			PointNode leastImportant = firstNode;
			while (current != firstNode) {

				if (leastImportant.importance > current.importance) {
					leastImportant = current;
				}
				current = current.next;
			}

			if (leastImportant == firstNode) {
				firstNode = firstNode.next;
			}
			stack.push(leastImportant);
			leastImportant.prev.next = leastImportant.next;
			leastImportant.next.prev = leastImportant.prev;

			(leastImportant.prev).calculateImportance();
			(leastImportant.next).calculateImportance();

		}

	}

	/**
	 * Restores nodes on the top of the stack based on the parameter
	 * 
	 * @param numPointsToRestore
	 */
	public void restoreNodes(int numPointsToRestore) {
		for (int i = 0; i < numPointsToRestore; i++) {
			PointNode node2restore = stack.pop();

			PointNode prevOfN2R = node2restore.prev;
			PointNode nextOfN2R = node2restore.next;

			prevOfN2R.next = node2restore;
			nextOfN2R.prev = node2restore;

			prevOfN2R.calculateImportance();
			nextOfN2R.calculateImportance();
		}

	}

	/**
	 * Restores or removes nodes as needed depending on the target and their
	 * importance
	 * 
	 * @param target: percentage of points to retain
	 */
	public void setToSize(int target) {

		int targetPoints = (int) (initNumPoints * (double) target / 100);

		if (currNumPoints > targetPoints) {
			int numPointsToRemove = currNumPoints - targetPoints;
			removeLeastImportantNodes(numPointsToRemove);
			currNumPoints = targetPoints;

		} else if (currNumPoints < targetPoints) {
			int numPointsToRestore = targetPoints - currNumPoints;
			restoreNodes(numPointsToRestore);
			currNumPoints = targetPoints;
		}
	}

	/**
	 * Returns a clean tag including all the nodes in the linked list, their x and y
	 * values and their importance
	 */
	public String toString() {
		String tag = firstNode.toString() + "\n";
		PointNode current = firstNode.next;
		while (current != firstNode) {
			tag += current.toString() + "\n";
			current = current.next;
		}
		return tag;

	}

}
