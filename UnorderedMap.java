package cs2321;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.datastructures.Entry;
import net.datastructures.Map;

/**
 * 
 * @author Javen Zamojcin
 * Assignment #6: Map
 * CS2321
 *
 * UnorderedMap (unsorted)
 *
 * @param <K>
 * @param <V>
 */

public class UnorderedMap<K,V> extends AbstractMap<K,V> {
	
	private ArrayList<mapEntry<K, V>> map = new ArrayList<>();

	//Constructor
	public UnorderedMap() { }
	
	//Helper Methods
	/**
	 * Returns index of given key
	 * @param key of type K
	 * @return Index of type integer
	 */
	private int findIndex(K key) {
		int n = map.size();
		for ( int i = 0; i < n; i++ ) {
			if ( map.get(i).getKey().equals(key))
				return i;
		}
		return -1;
	}
	
	//Instance Methods
	/**
	 * Retrieves size of map
	 * @return int: size
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() {
	/* TCJ
	 * Continous operation: O(1)
	 */
		return map.size();
	}
	
	/**
	 * Determines if map is empty or not
	 * @return boolean: map is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
	/* TCJ
	 * Continous operation: O(1)
	 */
		return map.size() == 0;
	}
	
	/**
	 * Returns value associated with given key
	 * @param K: key to be searched
	 * @return V: value associated with key
	 */
	@Override
	@TimeComplexity("O(n)")
	public V get(K key) {
	/* TCJ
	 * Has to scan through entire collection: O(n)
	 */
		int i = findIndex(key);
		if ( i == -1 ) return null;
		return map.get(i).getValue();
	}
	
	/**
	 * Inserts / replaces entry in map
	 * @param K: new entry key
	 * @param V: new entry value
	 * @return V: old entry value
	 * @return null: old entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(n)")
	public V put(K key, V value) {
	/* TCJ
	 * Has to scan through entire collection: O(n)
	 */
		V oldValue;
		int i = findIndex(key);
		if ( i == -1 ) {
			map.addFirst(new mapEntry<>(key, value));
			return null;
		} else {
			oldValue = map.get(i).getValue();
			map.get(i).setValue(value);
			return oldValue;
		}
	}
	
	/**
	 * Removes entry in map at given key
	 * @param K: key to be searched
	 * @return V: removed entry value
	 * @return null: entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(n)")
	public V remove(K key) {
	/* TCJ
	 * Has to scan through entire collection: O(n)
	 */
		int j = findIndex(key);
		int n = size();
		if ( j == -1 ) return null;
		V answer = map.get(j).getValue();
		if ( j != (n - 1))
			map.set(j, map.get(n-1));
		map.remove(n-1);
		
		return answer;
	}

	/**
	 * Retrieves an iterable set of entries from Map
	 * @return Iterable<Entry<K, V>> ArrayList
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterable<Entry<K, V>> entrySet() {
	/* TCJ
	 * Has to scan through each entry stored in map: O(n)	
	 */
		return new EntryIterable();
	}

	private class EntryIterator implements Iterator<Entry<K, V>> {

		private int j = 0;
		
		@Override
		public boolean hasNext() { return j < map.size(); }

		@Override
		public Entry<K, V> next() {
			if ( j == map.size()) throw new NoSuchElementException();
			return map.get(j++);
		}
	}
	
	private class EntryIterable implements Iterable<Entry<K, V>> {
	
		@Override
		public Iterator<Entry<K, V>> iterator() { return new EntryIterator(); } 
		
	}
}