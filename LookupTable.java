package cs2321;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.datastructures.*;

/**
 * 
 * @author Javen Zamojcin
 * Assignment #6: Map
 * CS2321
 *
 * LookupTable (sorted map)
 *
 * @param <K>
 * @param <V>
 */

public class LookupTable<K extends Comparable<K>, V> extends AbstractMap<K,V> implements SortedMap<K, V> {
	
	private ArrayList<mapEntry<K, V>> map = new ArrayList<>();
	
	//Constructors
	public LookupTable() { }
	
	public LookupTable(Comparator<K> comp) {  }
	
	//Helper Methods
	private int findIndex(K key, int low, int high ) {
		if ( high < low ) return high + 1;
		int mid = (high + low) / 2;
		int comp = key.compareTo(map.get(mid).getKey());
		if ( comp == 0 ) {
			return mid;
		} else if ( comp < 0 ) {
			return findIndex(key, low, mid - 1);
		} else {
			return findIndex(key, mid + 1, high);
		}
	}
	
	private int findIndex(K key) { return findIndex(key, 0, map.size() - 1); }
	
	private Entry<K, V> safeEntry( int j ) {
		if ( j < 0 || j >= map.size()) return null;
		return map.get(j);
	}
	
	private Iterable<Entry<K, V>> snapShot(int startIndex, K stop) {
		//ArrayList<Entry<K, V>> buffer = new ArrayList<>();
		DoublyLinkedList<Entry<K, V>> buffer = new DoublyLinkedList<>();
		int j = startIndex;
		while ( j < map.size() && (stop == null || stop.compareTo(map.get(j).getKey()) > 0 )) {
			buffer.addLast(map.get(j++));
		}
		return buffer;
	}
	
	//Instance Methods
	
	/**
	 * @return int: size of map
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() {
	/* TCJ
	 * Continuous operation time: 1	
	 */
		return map.size();
	}

	/**
	 * @return boolean: whether map is empty or not
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
	/* TCJ
	 * Continuous operation time: 1	
	 */	
		return size() == 0;
	}

	/**
	 * Retreives the value in map from given key
	 * @param K: key to be searched
	 * @return V: value of associated key
	 * @return null: map is empty
	 */
	@Override
	@TimeComplexity("O(log n)")
	public V get(K key) {
	/* TCJ
	 * Binary search operation: log n
	 */
		int j = findIndex(key);
		if ( j == size() || key.compareTo(map.get(j).getKey()) != 0 ) { return null; }
		return map.get(j).getValue();
	}

	/**
	 * Inserts / replaces a key-value entry in map
	 * @param K: key of entry
	 * @param V: value of entry
	 * @return V: value of old entry
	 * @return null: old entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(n)")
	public V put(K key, V value) {
	/* TCJ
	 * Binary search operation: log n
	 * Iterate through new collection: n
	 */
		//.getKey().compareTo(null) == 0
		V oldValue;
		int j = findIndex(key);
		if ( map.get(j) == null ) {
			oldValue = null;
		} else {
			oldValue = map.get(j).getValue();
		}
		
		if ( j < size() && key.compareTo(map.get(j).getKey()) == 0) {
			map.get(j).setValue(value);
			return oldValue;
		}
		map.add(j, new mapEntry<K, V>(key, value));
		return null;
	}
	
	/**
	 * Removes entry in map at given key
	 * @param K: key to be searched
	 * @return V: value removed
	 * @return null: entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(n)")
	public V remove(K key) {
	/* TCJ
	 * Binary search operation: log n
	 * Iterate through new collection: n
	 */
		int j = findIndex(key);
		if ( j == size() || key.compareTo(map.get(j).getKey()) != 0 ) return null;
		return map.remove(j).getValue();
	}
	
	/**
	 * Retrieves an iterable collection of all key-value entries within map
	 * @return Iterable collection of type Entry<K, V>
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterable<Entry<K, V>> entrySet() {
	/* TCJ
	 * Iterates through potentially entire collection: O(n)
	 */
		return snapShot(0, null);
	}

	/**
	 * Returns the entry with smallest key value
	 * @return Entry<K, V>: Entry with smallest value
	 * @return null: map is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> firstEntry() {
	/* TCJ
	 * Continuous operation: O(1)
	 */
		return safeEntry(0);
	}

	/**
	 * Returns the entry with largest key value
	 * @return Entry<K, V>: Entry with largest value
	 * @return null: map is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> lastEntry() {
	/* TCJ
	 * Continuous operation: 1
	 */
		return safeEntry(map.size() - 1);
	}

	/**
	 * Returns the entry with least key greater than or equal to given key
	 * @param K: key to be searched
	 * @return Entry of least greatest key to given key
	 * @return null: entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(log n)")
	public Entry<K, V> ceilingEntry(K key) {
	/* TCJ
	 * Binary search operation: log n
	 */
		return safeEntry(findIndex(key));
	}

	/**
	 * Returns the entry with least key greater than or equal to given key
	 * @param K: key to be searched
	 * @return Entry of least greatest key to given key
	 * @return null: entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(log n)")
	public Entry<K, V> floorEntry(K key) {
	/* TCJ
	 * Binary search operation: log n
	 */
		int j = findIndex(key);
		if ( j == size() || ! key.equals(map.get(j).getKey())) j--;
		return safeEntry(j);
	}
	
	/**
	 * Returns the entry with the least key value strictly
	 * less than key
	 * @param K: key to be searched
	 * @return Entry<K, V>: entry less than input entry
	 */
	@Override
	@TimeComplexity("O(log n)")
	public Entry<K, V> lowerEntry(K key) {
	/* TCJ
	 * Binary search operation: log n
	 */
		return safeEntry(findIndex(key) - 1);
	}
	
	/**
	 * Returns the entry with the least key value strictly
	 * greater than key
	 * @param K: key to be searched
	 * @return Entry<K, V>: entry greater than input entry
	 * @return null: entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(log n)")
	public Entry<K, V> higherEntry(K key) {
	/* TCJ 
	 * Binary search operation: log n
	 */
		int j = findIndex(key);
		if ( j < size() && key.equals(map.get(j).getKey())) j++;
		return safeEntry(j);
	}

	/**
	 * Returns an iteration of all entries with key greater
	 * than or equal to fromKey, but strictly less than toKey
	 * @param formKey: start of submap range
	 * @param toKey: end of submap range 
	 * @return Iterable<Entry<K, V>> ArrayList
	 */
	@Override
	@TimeComplexity("O(s + log n)")
	public Iterable<Entry<K, V>> subMap(K fromKey, K toKey){
	/* TCJ
	 * Performs a binary search: log n
	 * Scans through each index within binarysearch range: s 
	 */
		return snapShot(findIndex(fromKey), toKey);
	}
}
