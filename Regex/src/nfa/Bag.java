package nfa;

import java.util.Iterator;

/**
 * Simple Data Structure to hold elements
 * @author User
 *
 * @param <Item>
 */
public class Bag <Item> implements Iterable<Item> {
	
	/**
	 * Helper class
	 * @author User
	 *
	 */
	private class Node {
		
		Item item;
		Node next;
		
	}

	private Node first;
	private int size = 0;
	
	public void put(Item item) {
		
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.next = oldFirst;
		size++;
		
	}
	
	public int getSize() { 
		
		return size;
		
	}
	
	public boolean contains(Item item) {
		
		Iterator<Item> itr = this.iterator();
		
		while(itr.hasNext())
			if(itr.next() == item)
				return true;
		
		return false;
				
		
	}
	
	/**
	 * put everythhing from another bag to this one
	 * @param everything
	 */
	public void putEverything(Bag<Item> everything) {
		
		for(Item a: everything)
			this.put(a);
		
	}
	
	/**
	 * return a Bag iterator
	 */
	public Iterator<Item> iterator() {
		return new BagIterator();
	}
	
	/**
	 * Basic iterator, no remove functions
	 * @author User
	 *
	 */
	private class BagIterator implements Iterator<Item> {
		
		private Node node = first;

		@Override
		public boolean hasNext() {
			
			return node != null;
			
		}

		@Override
		public Item next() {
			
			Item item = node.item;
			node = node.next;
			
			return item;
			
		}

		@Override
		public void remove() {
			
		}
		
		
	}
	 
}
