package cs2321;

import net.datastructures.*;

/**
 * 
 * @author Javen Zamojcin
 * Assignment #6: Map
 * CS2321
 *
 * HashMap implemented with chain-addressing collision handling
 * 
 * @param <K>
 * @param <V>
 */
public class HashMap<K, V> extends AbstractMap<K,V> implements Map<K, V> {
	
	//Instance Variables
	private UnorderedMap<K, V>[] table;
	int capacity; 
	private int size = 0;
	
	//Constructors
	/**
	 * Constructor that takes a hash size
	 * Uses a hash size of 17 by default
	 * @param hashsize Table capacity: the number of buckets to initialize in the HashMap
	 */
	public HashMap(int hashsize) {
		capacity = hashsize;
		createTable();
	}
	
	public HashMap() {
		this(17);
	}
	
	//Helper Methods
	private int hashValue(K key) {
		return Math.abs(key.hashCode()) % capacity;
	}
	
	@SuppressWarnings("unchecked")
	private void createTable() {
		table = (UnorderedMap<K, V>[]) new UnorderedMap[capacity];
	}
	
	private V bucketGet(int h, K key) {
		UnorderedMap<K, V> bucket = table[h];
		if ( bucket == null ) return null;
		return bucket.get(key);
	}
	
	private V bucketPut(int h, K key, V value) {
		UnorderedMap<K, V> bucket = table[h];
		if ( bucket == null )
			bucket = table[h] = new UnorderedMap<>();
		int oldSize = bucket.size();
		V answer = bucket.put(key, value);
		size += (bucket.size() - oldSize);
		return answer;
	}
	
	private V bucketRemove(int h, K key) {
		UnorderedMap<K, V> bucket = table[h];
		if ( bucket == null ) return null;
		int oldSize = bucket.size();
		V answer = bucket.remove(key);
		size -= (oldSize - bucket.size());
		return answer;
	}
	
	//Instance Methods
	/**
	 * Retreives size of table
	 * @return int: size
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() {
	/* TCJ
	 * Continuous operation: O(1)	
	 */
		return size;
	}
	
	/**
	 * Determines if table is empty
	 * @return boolean: table is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
	/* TCJ
	 * Continous operation: O(1)
	 */
		return size() == 0;
	}

	/**
	 * Returns value associated with input key in table
	 * @param K: key to be searched
	 * @return V: value associated with key
	 */
	@Override
	@TimeComplexity("O(n)")
	@TimeComplexityExpected("O(1)")
	public V get(K key) {
	/** TCJ
	 * 	O(1): No collisions
	 * 	O(n): Worst case; collisions for each entry
	 */
		return bucketGet(hashValue(key), key);
	}
	
	/**
	 * Inserts / replaces entry into table
	 * @param K: new entry key
	 * @param V: new entry value
	 * @return V: old entry value
	 * @return null: old entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(n)")
	@TimeComplexityExpected("O(1)")
	public V put(K key, V value) {
	/* TCJ
	 * O(1): No collisions
	 * O(n): Worst case; collisions for each entry
	 */
		V answer = bucketPut(hashValue(key), key, value);

		return answer;
	}

	/**
	 * Removes entry in table at given key
	 * @param K: key to be searched
	 * @return V: old entry value
	 * @return null: old entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(n)")
	@TimeComplexityExpected("O(1)")
	public V remove(K key) {
	/* TCJ
	 * O(1): Only one entry per bucket
	 * O(n): Each entry is in one bucket
	 */
		return bucketRemove(hashValue(key), key);
	}

	/**
	 * Returns an ArrayList of iterable entries of table
	 * @return Iterable<Entry<K, V>>: ArrayList
	 */
	@Override
	@TimeComplexity("O(n)")
	@TimeComplexityExpected("O(n)")
	public Iterable<Entry<K, V>> entrySet() {
	/* TCJ
	 * Scans through entire collection of entries: O(n)
	 */
		DoublyLinkedList<Entry<K, V>> buffer = new DoublyLinkedList<>();
		for ( int i = 0; i < capacity; i++ ) {
			if ( table[i] != null ) {
				for ( Entry<K, V> entry : table[i].entrySet())
					buffer.addLast(entry);
			}
		}
		return buffer;
	}
}
