package cs2321;

import java.util.Random;

import net.datastructures.*;

public class HashMap<K, V> extends AbstractMap<K,V> implements Map<K, V> {
	
	//Instance Variables
	private mapEntry<K, V>[] table;
	private mapEntry<K, V> DEFUNCT = new mapEntry<>(null, null);
	int capacity; 
	private int prime;
	private long scale, shift;
	private int size = 0;
	
	//Constructors
	/**
	 * Constructor that takes a hash size and a prime number
	 * Uses a default capacity of 17 and prime number of 109345121
	 * @param hashsize Table capacity: the number of buckets to initalize in the HashMap
	 * @param prime number used in equation for putting entries into map
	 */
	public HashMap(int hashsize, int prime) {
		this.prime = prime;
		capacity = hashsize;
		Random rand = new Random();
		scale = rand.nextInt(prime - 1) + 1;
		shift = rand.nextInt(prime);
		createTable();
	}
	
	/**
	 * Constructor that takes a hash size
	 * @param hashsize Table capacity: the number of buckets to initialize in the HashMap
	 */
	public HashMap(int hashsize) {
		this(hashsize, 109345121);
	}
	
	public HashMap() {
		this(17);
	}
	
	//Helper Methods
	private void resize(int cap) {
		ArrayList<Entry<K, V>> buffer = new ArrayList<>(size);
		for ( Entry<K, V> e : entrySet()) {
			buffer.addLast(e);
		}
		capacity = cap;
		createTable();
		size = 0;
		for (Entry<K, V> e : buffer) {
			put(e.getKey(), e.getValue());
		}
	}
	
	private int hashValue(K key) {
		//Math.abs(key.hashCode()) % capacity;
		return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity) ;
	}
	
	@SuppressWarnings("unchecked")
	private void createTable() {
		table = (mapEntry<K, V>[]) new mapEntry[capacity];
	}

	private boolean isAvailable(int j) {
		return (table[j] == null || table[j] == DEFUNCT);
	}
	
	private int findSlot(int h, K key) {
		int available = -1;
		int j = h;
		do {
			if (isAvailable(j)) {
				if ( available == -1 ) available = j;
				if ( table[j] == null ) break;
			} else if ( table[j].getKey().equals(key) ) {
				return j;
			}
			j = ( j + 1 ) % capacity;
		} while ( j != h );
		return -(available + 1);	
	}
	
	private V bucketGet(int h, K key) {
		int j = findSlot(h, key);
		if ( j < 0 ) return null;
		return table[j].getValue();
	}
	
	private V bucketPut(int h, K key, V value) {
		int j = findSlot(h, key);
		if ( j >= 0 ) {
			V temp = table[j].getValue();
			table[j].setValue(value);
			return temp;		
		}
		table[-(j+1)] = new mapEntry<>(key, value);
		size++;
		return null;
	}
	
	private V bucketRemove(int h, K key) {
		int j = findSlot(h, key);
		if ( j < 0 ) return null;
		V answer = table[j].getValue();
		table[j] = DEFUNCT;
		size--;
		return answer;
	}
	
	//Instance Methods
	@Override
	public int size() {
		
		return size;
	}

	@Override
	public boolean isEmpty() {
		
		return size() == 0;
	}

	@Override
	public V get(K key) {
		return bucketGet(hashValue(key), key);
	}

	@Override
	public V put(K key, V value) {
		V answer = bucketPut(hashValue(key), key, value);
		if ( size > capacity / 2) {
			resize(2 * capacity - 1);
		}
		return answer;
	}

	@Override
	public V remove(K key) {
		return bucketRemove(hashValue(key), key);
	}

	@Override
	public Iterable<Entry<K, V>> entrySet() {
		ArrayList<Entry<K, V>> buffer = new ArrayList<>();
		for ( int i = 0; i < capacity; i++ ) {
			if (!isAvailable(i)) buffer.addLast(table[i]);
		}
		return buffer;
	}
}
