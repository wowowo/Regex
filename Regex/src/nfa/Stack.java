package nfa;

import java.util.Iterator;

/**
 * Basically a bag with an remove method
 * @author User
 *
 * @param <Item>
 */
public class Stack <Item> implements Iterable<Item> {
	
	private class Node {
		
		Item item;
		Node next;
		
	}

	private Node first;
	private int size = 0;
	
	public void push(Item item) {
		
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.next = oldFirst;
		size++;
		
	}
	
	public Item pop() {
		Item a = first.item;
		first = first.next;
		size--;
		return a;
	}
	
	public Item peek() {

		return first.item;
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
	
	public void putEverything(Bag<Item> everything) {
		
		for(Item a: everything)
			this.push(a);
		
	}
	
	public Iterator<Item> iterator() {
		return new BagIterator();
	}
	
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
