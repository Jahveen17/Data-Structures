package cs2321;

import java.util.Comparator;

import net.datastructures.*;
/**
 * A Adaptable PriorityQueue based on an heap. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author Javen Zamojcin
 */

public class HeapPQ<K,V> implements AdaptablePriorityQueue<K,V> {
	
	private ArrayList<Entry<K, V>> heap = new ArrayList<>();
	private Comparator<K> comp;
	
	//Constructors
	
	public HeapPQ() { this( new DefaultComparator<K>()); }
	
	public HeapPQ(Comparator<K> c) { comp = c; }
	
	//Helper methods
	
	private int compare( Entry<K, V> a, Entry<K, V> b ) { return comp.compare(a.getKey(), b.getKey()); }
	
	private int parent(int j) { return ( j - 1 ) / 2; }
	
	private int left(int j) { return ( 2 * j ) + 1; }
	
	private int right(int j) { return ( 2 * j ) + 2; }
	
	private boolean hasRight(int j) { return right(j) < heap.size(); }
	
	private boolean hasLeft(int j) { return left(j) < heap.size(); } 
	
	private void swap(int i, int j) {
		
		Entry<K, V> temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);	
		
		
	}
	
	private boolean checkKey( K key ) throws IllegalArgumentException {
		
		try {
			return (comp.compare(key, key) == 0 );
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Incompatible key");
		}
	}
	
	//Instance methods
	
	/**
	 * The entry should be bubbled up to its appropriate position 
	 * @param int move the entry at index j higher if necessary, to restore the heap property
	 */ 
	@TimeComplexity("O(log n)")
	public void upheap(int j){
	/** TCJ
	 * At most, the entire height of tree has to be sorted through.
	 * h = log n
	 */
		while ( j > 0 ) {
			
			int p = parent(j);
			if ( compare(heap.get(j), heap.get(p)) >= 0 ) { break; }
			swap(j, p);
			j = p;
			
		}		
	}
	
	/**
	 * The entry should be bubbled down to its appropriate position 
	 * @param int move the entry at index j lower if necessary, to restore the heap property
	 */
	@TimeComplexity("O(log n)")
	public void downheap(int j){
	/** TCJ
	 * At most, the entire height of tree has to be sorted through.
	 * h = log n
	 */	
		while (hasLeft(j)) {
			
			int leftIndex = left(j);
			int smallChildIndex = leftIndex;
			
			if ( hasRight(j) ) {
				
				int rightIndex = right(j);
				if ( compare(heap.get(leftIndex), heap.get(rightIndex)) > 0) {
					
					smallChildIndex = rightIndex;
				}
			}
			
			if ( compare(heap.get(smallChildIndex), heap.get(j)) >= 0 ) {
				
				break;
			}
			
			swap(j, smallChildIndex);
			j = smallChildIndex;
		}
	}
	
	/**
	 * Retrieves size of the heap
	 * @return value of size
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() { 
	/** TCJ
	 * Retrieving value of a variable is a one-time operation.
	 */
		return heap.size();
	}
	
	/**
	 * @return True / false if heap size is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() { 
	/** TCJ
	 * 	Determining if size is 0 is a one time operation.
	 */
		return size() == 0;
	}
	
	/**
	 * Stores new entry into heap and then restores heap order
	 * @param key and value for entry to be stored
	 * @return entry stored
	 * @throws IllegalArgumentException if key is an illegal argument
	 */
	@Override
	@TimeComplexity("O(log n)")
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
	/**TCJ
	 * 	At most, has to sort through height of entire heap
	 * height = log n
	 */
		checkKey(key);
		Entry<K, V> newest = new PQEntry<>(key, value);
		heap.add(heap.size(), newest);
		upheap(heap.size()-1);
		
		return newest;
	}
	
	/**
	 * Retrieves the minimum entry stored at the beginning of the heap
	 * @return minimum entry; null if heap is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> min() {
	/** TCJ
	 * Location of minimum entry is already known.
	 */
		if ( isEmpty()) { return null; }
		return heap.get(0);
	}

	/**
	 * Removes and returns the minimum entry of the heap
	 * by swapping with last entry and restoring the heap order
	 * @return Minimum entry; null if heap is empty
	 */
	@Override
	@TimeComplexity("O(log n)")
	public Entry<K, V> removeMin() {
	/** TCJ
	 * Potentially has to sort through height of heap
	 * height = log n
	 */
		
		if ( isEmpty()) { return null; }
		
		Entry<K, V> answer = heap.get(0);
		swap(0, heap.size() - 1);
		heap.remove(heap.size() - 1);
		downheap(0);
		
		return answer;
	}
	
	/**
	 * Removes given entry from heap by scanning through the heap and comparing entries,
	 *  then calls remove method and restores heap order.
	 *  @param entry to be removed
	 *  @throws IllegalArgumentException if heap is empty or entry not found
	 */
	@Override
	@TimeComplexity("O(log n)")
	public void remove(Entry<K, V> entry) throws IllegalArgumentException {
	/** TCJ
	 * 	Potentially has to sort through entire height of heap
	 *  Height = log n
	 */
		if ( isEmpty()) throw new IllegalArgumentException("Empty heap");
		boolean entryNonExistant = true;
		
		for ( int i = 0; i < heap.size(); i++ ) {
			
			if ( heap.get(i) == entry ) {
				
				entryNonExistant = false;
				swap(i, heap.size() - 1);
				heap.remove(heap.size() - 1);
				downheap(i);
				break;
				
			}
		}
		
		if ( entryNonExistant ) throw new IllegalArgumentException("Entry non-existant");
	}

	/**
	 * Replaces key at given entry by scanning through heap and comparing entries
	 * @param Entry to be altered; new key
	 * @throws IllegalArgumentException if heap is empty or entry is not found
	 */
	@Override
	@TimeComplexity("O(log n)")
	public void replaceKey(Entry<K, V> entry, K key) throws IllegalArgumentException {
	/** TCJ
	 * 	Potentially has to sort through entire height of heap
	 *  height = log n
	 */
		if ( isEmpty()) throw new IllegalArgumentException("empty");
		checkKey(key);
		boolean entryNonExistant = true;
		
		for ( int i = 0; i < heap.size(); i++ ) {
			
			if ( heap.get(i) == entry ) {
				
				Entry<K, V> newest = new PQEntry<>(key, heap.get(i).getValue());
				entryNonExistant = false;
				heap.set(i, newest);
				break;
				
			}
		}
		
		if ( entryNonExistant ) throw new IllegalArgumentException("Entry non-existant");
		
	}
	
	/**
	 * Replaces value at given entry by scanning through heap and comparing entries
	 * @param Entry to be altered; new value
	 * @throws IllegalArgumentException if heap is empty or entry is not found
	 */
	@Override
	@TimeComplexity("O(log n)")
	public void replaceValue(Entry<K, V> entry, V value) throws IllegalArgumentException {
	/** TCJ
	 * 	Potentially has to sort through entire height of heap
	 *  height = log n
	 */	
		if ( isEmpty()) throw new IllegalArgumentException("empty");
		boolean entryNonExistant = true;
		
		for ( int i = 0; i < heap.size(); i++ ) {
			
			if ( heap.get(i) == entry ) {
				
				Entry<K, V> newest = new PQEntry<>(heap.get(i).getKey(), value );
				entryNonExistant = false;
				heap.set(i, newest);
				break;
				
			}
		}
		
		if ( entryNonExistant ) throw new IllegalArgumentException("Entry non-existant");
		
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
