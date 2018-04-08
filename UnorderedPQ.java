package cs2321;

import java.util.Comparator;

import net.datastructures.*;
/**
 * A PriorityQueue based on an Unordered Doubly Linked List. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author Javen Zamojcin
 */

public class UnorderedPQ<K,V> implements PriorityQueue<K,V> {

	private PositionalList<Entry<K, V>> list = new DoublyLinkedList<>();
	private Comparator< K > comp;
	
	//Constructors
	
	public UnorderedPQ() { this( new DefaultComparator<K>()); }
	
	public UnorderedPQ(Comparator<K> c) { comp = c; } 
	
	//Helper methods
	
	private int compare( Entry<K, V> a, Entry<K, V> b ) { return comp.compare(a.getKey(), b.getKey()); }
	
	private boolean checkKey( K key ) throws IllegalArgumentException {
		
		try {
			return (comp.compare(key, key) == 0 );
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Incompatible key");
		}
	}
	
	private Position<Entry<K, V>> findMin() {
		
		Position<Entry<K, V>> min = list.first();
		
		for ( Position<Entry<K, V>> walk : list.positions()) {
			
			if ( compare(walk.getElement(), min.getElement()) < 0 ) {
				
				min = walk;
			}
		}
		
		return min;
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
	 * Creates and inserts entry in an unordered fashion
	 * @param Key and value for new entry
	 * @return Entry created
	 * @throws IllegalArgumentException if key is an illegal arguments
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
	/** TCJ
	 *  Position is known for where to put entry at 
	 */
		checkKey(key);
		Entry<K, V> newest = new PQEntry<>(key, value);
		list.addLast(newest);
		
		return newest;
	}

	/**
	 * Scans through PQ and finds minimum entry by comparing entries
	 * @return Minimum Entry; null if PQ is empty
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> min() {
	/** TCJ
	 *  Potentially has to scan through entire PQ	
	 */
		if (isEmpty()) { return null; }
		
		return findMin().getElement();
	}
	
	/**
	 * Finds minimum entry in PQ and removes it
	 * @return Minimum entry; null if PQ is empty
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> removeMin() {
	/** TCJ
	 *  Potentially has to scan through entire PQ
	 */
		if (isEmpty()) { return null; }
		
		return list.remove(findMin());
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
