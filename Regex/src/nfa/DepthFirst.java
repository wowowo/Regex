package nfa;

/**
 * Modified version of Depth First Search to suit the NFA class
 * @author User
 *
 */
public class DepthFirst {
	
	static boolean[] marked;
	private static Bag<Integer> match;
	
	/**
	 * go to all possible locations from the start State
	 * @param G - directed graph
	 * @param startState - starting vertex
	 */
	public static void search(Digraph G, int startState) {

		marked = new boolean[G.V()];
		
		dfs(G, startState);
		
	}
	
	/**
	 * Search reachable states then return them
	 * @param G - directed graph
	 * @param matches - the current reachable states of the nfa
	 * @return
	 */
	public static Bag<Integer> searchAll(Digraph G, Bag<Integer> matches) {

		marked = new boolean[G.V()];
		match = new Bag<Integer>();
		
		/**
		 * if no reachable states exist, start from the beginning
		 */
		if(matches.getSize() == 0) 
			dfbag(G,0);
		
		else
			for(int startState: matches)
				dfbag(G, startState);
			
		return match;
	}

	/**
	 * If the vertex points to itself (this means that it should be matched)
	 * get all reachable states
	 * Do this for all adjacent vertices to the starting one
	 * @param G - Digraph
	 * @param i - starting vertex
	 */
	private static void dfbag(Digraph G, int i) {
		
		for(int o : G.adj(i))
			if(o == i)
				match.putEverything(G.returnEverything(i));
		
		for(int o : G.adj(i))
			if(!marked[o]) {
				marked[o] = true;
				dfbag(G, o);
			}
			

	
	}

	/**
	 * Go to all other adjacent vertices and mark them
	 * @param G - Digraph
	 * @param i - starting state
	 */
	private static void dfs(Digraph G, int i) {
		
		marked[i] = true;
		
		for(int o : G.adj(i))
			if(!marked[o])
				dfs(G, o);
		
	}
	
	/**
	 * 
	 * @param v - the vertex in question
	 * @return - whether the vertex has been traversed
	 */
	public static boolean marked(int v) {
		
		return marked[v];
		
	}

}
