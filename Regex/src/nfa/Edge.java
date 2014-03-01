package nfa;

/**
 * Basic edge class
 * @author User
 *
 */
public class Edge implements Comparable<Edge> {
	
	private int v;
	private int w;
	private double weight;
	
	public Edge(int v, int w) {
		
		this.v = v;
		this.w = w;
		
	}
	
	public int getTail() {
		
		return v;
				
	}
	
	public int getHead() {
		
		return w;
		
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public int compareTo(Edge that) {
		if(this.weight > that.weight) return 1;
		else if (this.weight == that.weight) return 0;
		return -1;
	}
	

}
