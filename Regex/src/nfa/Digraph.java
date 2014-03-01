package nfa;

/**+
 * Basic class than acts as a Digraph of linked lists of edges
 * @author User
 *
 */
public class Digraph {
	
	private int V;
	private Bag<Integer>[] ver;
	
	/**
	 * Create an array of bags of size V
	 * @param V - number of vertices
	 */
	public Digraph(int V) {
		
		this.V = V;
		ver = (Bag<Integer>[]) new Bag[V];
		for(int i = 0; i < V; i++)
			ver[i] = new Bag<Integer>();
		
	}

	/**
	 * add a new edge, tail is the beginning, head is the vertex the edge pints to
	 * @param e
	 */
	public void addEdge(Edge e) {
		
		ver[e.getTail()].put(e.getHead());
		
	}
	
	/**
	 * 
	 * @param v - the vertex
	 * @return - a bag with all the adjacent vertices
	 */
	public Iterable<Integer> adj(int v) {
		
		return ver[v];
		
	}
	
	/**
	 * delete the loop of the vertex
	 * @param p - a vertex with a  loop
	 */
	public void wipeout(int p) {
		
		Bag<Integer> replacement = new Bag<Integer>();
		
		for(int i : ver[p])
			if(i != p)
				replacement.put(i);
		
		ver[p] = replacement;
		
			
		
	}
	/**
	 * can be used instead of iterable
	 * @param i
	 * @return
	 */
	public Bag<Integer> returnEverything(int i) {
		
		Bag<Integer> returnee = new Bag<Integer>();
		for(int o : ver[i])
			returnee.put(o);
		return returnee;
	}
	
	/**
	 * 
	 * @return size of the graph
	 */
	public int V() {
		
		return V;
		
	}
	
	/**
	 * print method for debugging
	 */
	public void print() {
		
		for(int i = 0; i < V; i++) {
			System.out.print("edge " + i + " :");
			for(int a: this.adj(i)) 
				System.out.print(a + ", ");
			
				
			System.out.println("");
		}
	}
	
}
