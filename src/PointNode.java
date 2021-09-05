
public class PointNode {
		public int xCoor;
		public int yCoor;
		public double importance;
		public PointNode prev;
		public PointNode next;
		
		public PointNode(int xCoor, int yCoor) {
			this.xCoor = xCoor;
			this.yCoor = yCoor;
			importance = 0;
			prev = null;
			next = null;
		}
		public double euclidDis(PointNode other) {
			double distance = Math.sqrt(Math.pow(xCoor-other.xCoor,2) + Math.pow(yCoor-other.yCoor,2));
			return distance;
		}
		
		public void calculateImportance() {
			//   [L]....[P]....[R]
			double LP = euclidDis(prev);
			double PR = euclidDis(next);
			double LR = prev.euclidDis(next);
			
			importance = LP + PR - LR;
			
			
		}
		
		public String toString() {
			String tag = "x = " + xCoor + ", y = " + yCoor + ", importance = " + importance;
			return tag;
		}
		

	public static void main(String[] args) {
		PointNode first = new PointNode(100, 150);
		PointNode second = new PointNode(200, 25);
		PointNode third = new PointNode(350, 100);
		second.prev = first;
		first.next = second;
		second.next = third;
		third.prev = second;
		
		second.calculateImportance();
		System.out.println(second);
		// TODO Auto-generated method stub

	}

}
