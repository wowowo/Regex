package nfa;

/**
 *  A regex engine that macthes characters against the text based on a transitional digraph.
 *  
 *  <p>
 *  
 *   '*' - matches zero or more occurences of the preciding char, or block
 *   '|' - Inclusive or, matches whats on one or both sides of the symbol, works for blocks and chars
 *   '^' - matches beggining
 *   '(',')' - define a block withing the regex
 *   '?' - preceding char or block can be skipped
 *   '/' - escapes the next metachar
 *   'Z' - matches any digit
 *  
 *  </p>
 * @author User
 *
 */
public class Pattern {
	
	private char[] regex;
	private Digraph G;
	private int L;
	
	/**
	 * Add all the chars of the regex to an array
	 * and create a the transition graph
	 * @param regex - the regular expression to be matched against any text
	 */
	public Pattern(String regex) {
				
		this.L = regex.length();
		this.regex = regex.toCharArray();
		this.G = buildDigraph();
		
	}
	
	/**
	 * Build a transition graph based on the principal that all 
	 * characters that have to be matched have an edge pointing to themselves
	 * and all the metachars - '(','?'... have an edge pointing to somewhere else
	 * depending on their function
	 * @return  - the transitional graph
	 */
	private Digraph buildDigraph() {
		
		G = new Digraph(L + 1);
		//add end state
		G.addEdge(new Edge(regex.length, regex.length));
		
		//crete stacks for the bracket transitions
		Stack<Integer> Obrackets = new Stack<Integer>();
		Stack<Integer> Cbrackets = new Stack<Integer>();
		Stack<Integer> brackets = new Stack<Integer>();
		Stack<Integer> waiting = new Stack<Integer>();

		//add the closing bracekts to Cbrackets and all the opening ones to brackets
		for(int i = L-1; i < -1; i--)
			
			if(regex[i] == ')')
				Cbrackets.push(i);
		
			else if(regex[i] == '(')
				brackets.push(i);
			

		//for each character in the regex
		for(int i = 0; i < L; i++) {
			
			//if there exists a char class create the appropriate links
			if(regex[i] == 'Z') 
					populate(G,i);

			//escape the next character character and go on to the one after that
			if(regex[i] == '/') {
				G.addEdge(new Edge(i, i + 1));
				G.addEdge(new Edge(i + 1, i + 1));
				i += 1;
				continue;
			}
			
			// if there is an opening bracket in the course of the 
			// graph building put it in Obrackets.
			if(regex[i]=='(')
				Obrackets.push(i);
			
			// if the current char is a closing bracket 
			if(regex[i]==')') {
				
				//pop the last added opening bracket
				int popped = Obrackets.pop();
				
				// if there is a graph in the waiting stack
				// make the OR transition
				if(waiting.getSize() > 0 && waiting.peek() == popped) {
					
					int check = waiting.pop();
					check = waiting.pop();
					
					G.addEdge(new Edge(popped - 1, i + 1));
					G.addEdge(new Edge(check, popped));
					
				}

				
				// make transitions between the brackets
				G.addEdge(new Edge(i, popped));
				G.addEdge(new Edge(i, i + 1));
				int p = i + 1;
				
				//look ahead
				if(p < L)
					// if the whole section between the brackets can be skipped
					// add the transitions
					if(regex[p] == '?' || regex[p] == '*')
						G.addEdge(new Edge(popped, i));
				
					// if there are multiple choices
					else if(regex[p] == '|') {
						
						//add an edge to the state after '|'						
						G.addEdge(new Edge(popped, p + 1));
						
						// if its not an opening bracket, but a char class or a simple char
						// add that transition
						if(regex[p + 1 ] != '(')
							G.addEdge(new Edge(p, p + 2));
						
						// otherwise send the brackets on the waiting stack
						else {
							
								waiting.push(popped);
								waiting.push(p + 1);
						
						}
						
						i++;
						continue;
						
					}

			}
			
				
				
			//look ahead
			if(i + 1 < L) 
				
				// if the next char is '?' make it possible to skip this char
				if(regex[i + 1] == '?') 
					G.addEdge(new Edge(i, i + 2));
			
				// if the next char is | make the transitions 
				// so that either one can advance the matching
				else if (regex[i + 1] == '|') {
					
						G.addEdge(new Edge(i, i));
						G.addEdge(new Edge(i, i + 2));
						G.addEdge(new Edge(i + 1, i + 3));
						i++;
						continue;	
						
				}
			
			// if char is * add transitions so that 
			// you can go to the same char, and to the one after the '*'
				else if(regex[i + 1] == '*') {
					G.addEdge(new Edge(i + 1, i));
					G.addEdge(new Edge(i, i + 2));
				}
					
				
					
			//define as char that needs to be matched
			//needs extension for numbers and capitals and others
			if(regex[i] > 96 && regex[i] < 129)
				G.addEdge(new Edge(i, i));
				
			//define as metachar
			if(regex[i] == '^' || regex[i] == '?' || regex[i] == '(')
				G.addEdge(new Edge(i, i + 1));
			
		}
		
		return G;
	}
	


	/**
	 * 
	 * @param G - the digraph
	 * @param p - the vertex that will point to all the numbers
	 */
	private void populate(Digraph G, int p) {
		
		// create a new larger di graph and copy over from the old one
		char[] regex2 = new char[regex.length + 21];
		
		for(int i = 0; i < regex.length; i++) 
			regex2[i] = regex[i];

		int beggining = regex.length + 1;
		int ending  = regex.length + 21;
		
		
		// set the edges so that they point to concrete characters that have to be matched
		// and if they are set the next stage to point to the next state in the regex and digraph
		
		for(int i = beggining, j = 48; i < ending; i+=2)
			regex2[i] = (char) j++;
			
		regex = regex2;
		
		for(int i = beggining,j = i + 1; i < ending; i+=2) {
			j = i + 1;
			G.addEdge(new Edge(p, i));
			G.addEdge(new Edge(i, i));
			G.addEdge(new Edge(j, p+1));
			
		}
	}

	public boolean match(String txt) {
	
		// create a bag of reachable states
		Bag<Integer> rs = new Bag<Integer>();
		rs = DepthFirst.searchAll(G, rs);

		boolean matching = false;
		
		// for every character in the text
		for(int i = 0; i < txt.length(); i++) {
			
			Bag<Integer> matches = new Bag<Integer>();
			
			// for every reachable state
			for(int v : rs) {
				
				//if the end is reached return true
				if(v == L)
					 return true;
				
				// else if the reachable state matches the text char 
				else if(regex[v] == txt.charAt(i) || regex[v]=='.') {
					
					// if the next state is not in the states that match
					// add it and set a maching flag, so that the algorith knows
					// there have been matches on this text character
					if(!matches.contains(v + 1)) {
					matching = true;
					matches.put(v + 1);
					}
					
					
				}
			}
			
			// if there were no matches 
			if(matches.getSize() == 0)
				//and the 
				// machine had to match the beginning
				// return false
				if(regex[0] == '^')
					
					return false;
				//if the algorith was maching 
				// turn back one text character, to start from the beggining 
				// of the regex
				else {
					if(matching)
							i--;
					matching = false;
					rs = DepthFirst.searchAll(G, matches);
				}
			//if there were matches 
			// find the adjacent vertices
			else
				rs = DepthFirst.searchAll(G, matches);
			
		}
		

		// if the end of the regex has been reached
		// return true
		for(int v: rs)
					
			if(v == L)
				return true;
		
		return false;
	}
	

	/**
	 * Test
	 * @param args
	 */
	public static void main(String[] args) {
		
		Pattern pat = new Pattern("(a*)|(xe)zah");
		//						   0123456789
		System.out.println(pat.match("zah"));

	}
	 		
}
