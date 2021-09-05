import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import DecomposableShape.PointNode;

public class DecomposableShapeBackup {
	
	

	
	private int initNumPoints = 1;
	private int currNumPoints;
	private PointNode firstNode;
	private StackADT<PointNode> stack;

	public DecomposableShape(Scanner input) {

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

		firstNode.prev = current;
		current.next = firstNode;
		
// debugging code
//		System.out.println(firstNode);
//		System.out.println(firstNode.next);
//		System.out.println(firstNode.prev);

		firstNode.calculateImportance();
		current = firstNode.next;

		while (current != firstNode) {
			current.calculateImportance();
			current = current.next;

		}

	}

	public Polygon toPolygon() {

		int[] xArr = new int[initNumPoints];
		int[] yArr = new int[initNumPoints];
		PointNode current = firstNode;
//		PointNode prev = firstNode;
		int count = 0;
		while (current != firstNode) {
			xArr[count] = current.xCoor;
			yArr[count] = current.yCoor;
			current = current.next;
// 			prev = current;
			count++;
		}

		Polygon polygon = new Polygon(xArr, yArr, initNumPoints);
		return polygon;

	}

	public void setToSize(int target) {

		currNumPoints = (int) (initNumPoints * (double) (100 - target) / 100);
		int numPointsToRemove = currNumPoints;

		for (int i = 0; i < numPointsToRemove; i++) {

			PointNode current = firstNode.next;
			PointNode leastImportant = firstNode;
			while (current != firstNode) {

				if (leastImportant.importance > current.importance) {
					leastImportant = current;
				}

				current = current.next;

			}

			leastImportant.prev.next = leastImportant.next;
			leastImportant.next.prev = leastImportant.prev;

			(leastImportant.prev).calculateImportance();
			(leastImportant.next).calculateImportance();

		}

	}

	public String toString() {
		String tag = "";
		PointNode current = firstNode.next;
		while (current != firstNode) {
			tag += current.toString() + "\n";
			current = current.next;
		}
		return tag;

	}



	public static void main(String[] args) throws FileNotFoundException {
//		Scanner infile = new Scanner(new File("box.txt"));
//		DecomposableShape trial = new DecomposableShape(infile);
//		
//		Polygon trialPolygon = trial.toPolygon();
//		System.out.println(Arrays.toString(trialPolygon.xpoints));
//		System.out.println(Arrays.toString(trialPolygon.ypoints));
//		System.out.println(trialPolygon.npoints);

//		trial.setToSize(40);

//		Scanner input = new Scanner(new File("box.txt"));
//		DecomposableShape shape1 = new DecomposableShape(input);
//		System.out.println(shape1);
		
		try{
			   Scanner input = new Scanner(new File("box.txt"));
			   DecomposableShape shape1 = new DecomposableShape(input);
			   System.out.println(shape1);
			} catch (FileNotFoundException e){
			   System.out.println(e);
			}

		

	}
	
	
	
	
	
	

	public static void main(String[] args) {
		
//		Scanner infile = new Scanner(new File("box.txt"));
//		DecomposableShape trial = new DecomposableShape(infile);
//		
//		Polygon trialPolygon = trial.toPolygon();
//		System.out.println(Arrays.toString(trialPolygon.xpoints));
//		System.out.println(Arrays.toString(trialPolygon.ypoints));
//		System.out.println(trialPolygon.npoints);

//		trial.setToSize(40);

//		Scanner input = new Scanner(new File("box.txt"));
//		DecomposableShape shape1 = new DecomposableShape(input);
//		System.out.println(shape1);
		
		try{
			   Scanner input = new Scanner(new File("box.txt"));
			   DecomposableShape shape1 = new DecomposableShape(input);
			   System.out.println(shape1);
			} catch (FileNotFoundException e){
			   System.out.println(e);
			}

		
		
		
		
		
		
		
		
		
		// TODO Auto-generated method stub

	}

}
