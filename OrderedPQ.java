package cs2321;

import java.util.Comparator;

import net.datastructures.*;
/**
 * A PriorityQueue based on an ordered Doubly Linked List. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author Javen Zamojcin
 * 
 */

public class OrderedPQ<K,V> implements PriorityQueue<K,V> {

	private PositionalList<Entry<K, V>> list = new DoublyLinkedList<>();
	private Comparator< K > comp;
	
	//Constructors
	
	public OrderedPQ() { this( new DefaultComparator<K>()); }
	
	public OrderedPQ(Comparator<K> c) { comp = c; } 
	
	//Helper methods
	
	private int compare( Entry<K, V> a, Entry<K, V> b ) { return comp.compare(a.getKey(), b.getKey()); }
	
	private boolean checkKey( K key ) throws IllegalArgumentException {
		
		try {
			return (comp.compare(key, key) == 0 );
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Incompatible key");
		}
	}
	
	//Instance methods
	
	/**
	 * Retrieves size of PQ
	 * @return value of size
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() { 
	/** TCJ
	 * 	Retrieving value of a variable is a one-time operation
	 */
		return list.size(); 
	}

	/**
	 * Checks if PQ is empty or not
	 * @return True / false if PQ is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
	/** TCJ
	 * Retrieving value of a variable is a one-time operation
	 */
		return size() == 0; 
	}

	/**
	 * Creates and inserts new entry into the PQ in a sorted fashion
	 * @param Key and value for new entry
	 * @return New entry
	 * @throws IllegalArgumentException if key is an illegal argument
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
	/** TCJ
	 * 	Potentially has to scan through entire PQ
	 */
		checkKey(key);
		Entry<K, V> newest = new PQEntry<>(key, value);
		Position<Entry<K, V>> walk = list.last();
		
		while ( walk != null && compare(newest, walk.getElement()) < 0 ) {
			walk = list.before(walk);
		}
		
		if ( walk == null ) {
			list.addFirst(newest);
		} else {
			list.addAfter(walk, newest);
		}
		
		return newest;
	}
	
	/**
	 * Retrieves the minimum entry of PQ
	 * @return Minimum entry; null if PQ is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> min() {
	/** TCJ
	 *  Node reference position is known
	 */
		if ( isEmpty() ) { return null; }
		return list.first().getElement();
	}

	/**
	 * Removes minimum entry
	 * @return Entry removed; null if PQ is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> removeMin() {
	/** TCJ
	 * 	Node reference position is known
	 */
		if ( isEmpty() ) { return null; }
		return list.remove(list.first());
	}

	//Helper class
	
	@SuppressWarnings("hiding")
	private class PQEntry<K, V> implements Entry<K, V>{

		private K key;
		private V value;

		public PQEntry(K k, V v) {
			key = k;
			value = v;
		}
		
		@Override
		public K getKey() { return key; }

		@Override
		public V getValue() { return value; }
		
		@SuppressWarnings("unused")
		public void setKey( K k ) { key = k; }
		
		@SuppressWarnings("unused")
		public void setValue( V v ) { value = v; }
		
	}
}
