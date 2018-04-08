package cs2321;

import java.util.Comparator;

import net.datastructures.Entry;
import net.datastructures.Position;
import net.datastructures.SortedMap;

/**
 * 
 * @author Javen Zamojcin
 * Assignment #6: Map
 * CS2321
 * 
 * BinarySearchTree
 *
 * @param <K>
 * @param <V>
 */

public class BinarySearchTree<K extends Comparable<K>,V> extends AbstractMap<K,V> implements SortedMap<K,V> {
	
	private LinkedBinaryTree<Entry<K,V>> tree = new LinkedBinaryTree<>();
	
	//Constructors
	public BinarySearchTree() { }
	
	//Helper Methods
	private void subMapRecurse(K fromKey, K toKey, Position<Entry<K, V>> p, ArrayList<Entry<K, V>> buffer) {
		
		if (tree.isInternal(p)) {
			if ( p.getElement().getKey().compareTo(fromKey) < 0) {
				subMapRecurse(fromKey, toKey, tree.right(p), buffer);
			} else {
				subMapRecurse(fromKey, toKey, tree.left(p), buffer);
				if ( p.getElement().getKey().compareTo(toKey) < 0) {
					buffer.addFirst(p.getElement());
					subMapRecurse(fromKey, toKey, tree.right(p), buffer);
				}
			}
		}
	}
	
	private Position<Entry<K, V>> treeMax(Position<Entry<K, V>> p) {
		
		Position<Entry<K, V>> walk = p;
		while ( tree.isInternal(walk) ) {
			walk = tree.right(walk);
		}
		return tree.parent(walk);
	}
	
	private Position<Entry<K, V>> treeMin(Position<Entry<K, V>> p) {
		
		Position<Entry<K, V>> walk = p;
		while ( tree.isInternal(walk) ) {
			walk = tree.left(walk);
		}
		return tree.parent(walk);
	}
	
	private void expandExternal(Position<Entry<K, V>> p, Entry<K,V> entry) {
		
		tree.set(p, entry);
		tree.addLeft(p, null);
		tree.addRight(p, null);
	}
	
	private Position<Entry<K, V>> root() { return tree.root(); }
	
	private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
		
		if (tree.isExternal(p)) { return p; }
		int comp = key.compareTo(p.getElement().getKey());
		if ( comp == 0 ) {
			return p;
		} else if ( comp < 0 ) {
			return treeSearch(tree.left(p), key);
		} else {
			return treeSearch(tree.right(p), key);
		}
	}
	
	//Hook Methods
	private void rebalanceInsert(Position<Entry<K, V>> p) { }
	
	private void rebalanceDelete(Position<Entry<K, V>> p) { }
	
	private void rebalanceAccess(Position<Entry<K, V>> p) { }
	
	//Instance Methods
	/**
	 * Returns tree for testing purposes
	 * @return tree
	 */
	public LinkedBinaryTree<Entry<K,V>> getTree() {
		return tree;
	}
	
	/**
	 * @return size of tree
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size(){
	/* TCJ
	 * Continous operation: O(1)
	 */
		return (tree.size() - 1 ) / 2;
	}
	
	/**
	 * Returns the value associated with the specified key
	 * @return V: value associated with key
	 * @return null: no entry exists
	 */
	@Override
	@TimeComplexity("O(h)")
	public V get(K key) {
	/* TCJ
	 * Potentially has to traverse entire height of tree: O(h)
	 */
		if ( key == null ) throw new IllegalArgumentException("key is null");
		if ( isEmpty() ) return null;
		
		Position<Entry<K, V>> p = treeSearch(root(), key);
		System.out.println(p);
		rebalanceAccess(p);
		if (tree.isExternal(p)) return null;
		
		return p.getElement().getValue();
	}

	/**
	 * Associates the given value with the given key, returning any overridden value
	 * @param K: new entry key
	 * @param V: new entry value
	 * @return V: old entry value
	 * @return null: old entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(h)")
	public V put(K key, V value) {
	/* TCJ
	 * Potentially has to traverse through entire height of tree: O(h)
	 */
		if ( key == null ) throw new IllegalArgumentException("key is null");
		Entry<K, V> entry = new mapEntry<>(key, value);
		
		if ( isEmpty() ) {
			tree.addRoot(entry);
			expandExternal(root(), entry);
			return null;
		}

		Position<Entry<K, V>> p = treeSearch(root(), key);
		if (tree.isExternal(p)) {
			expandExternal(p, entry);
			rebalanceInsert(p);
			return null;
		} else { 
			V oldValue = p.getElement().getValue();
			tree.set(p, entry);
			rebalanceAccess(p);
			return oldValue;
		}
	}

	/**
	 * Removes the entry associated with input key
	 * @param key to be searched
	 * @return value of old entry
	 * @return null: entry doesn't exist
	 */
	@Override
	@TimeComplexity("O(h)")
	public V remove(K key) {
	/* TCJ
	 * Potentially has to traverse through entire height of tree: O(h)
	 */
		if ( key == null ) throw new IllegalArgumentException("key is null");
		Position<Entry<K, V>> p = treeSearch(root(), key);
		
		if ( tree.isExternal(p) ) {
			rebalanceAccess(p);
			return null;
		} else {
			V oldValue = p.getElement().getValue();
			if ( tree.isInternal(tree.left(p)) && tree.isInternal(tree.right(p))) {
				Position<Entry<K, V>> replacement = treeMin(tree.right(p));
				tree.set(p, replacement.getElement());
				p = replacement;
			}
			
			Position<Entry<K, V>> leaf = (tree.isExternal(tree.left(p)) ? tree.left(p) : tree.right(p));
			Position<Entry<K, V>> sibling = tree.sibling(leaf);
			tree.remove(leaf);
			tree.remove(p);
			rebalanceDelete(sibling);
			return oldValue;	
		}
	}

	/**
	 * Returns an iterable collection of all key-value entries of tree
	 * @return Iterable<Entry<K, V>>: iterable collection of entries
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterable<Entry<K, V>> entrySet() {
	/* TCJ
	 * Iterates through entire collection of entries	
	 */
		ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
		for ( Position<Entry<K, V>> p : tree.inorder()) {
			if ( tree.isInternal(p) ) buffer.addFirst(p.getElement());
		}
		return buffer;
	}

	/**
	 * Returns the entry having the least greatest key
	 * @return Entry<K, V>: entry
	 * @return null: tree is empty
	 */
	@Override
	@TimeComplexity("O(h)")
	public Entry<K, V> firstEntry() {
	/* TCJ
	 * Potentially has to sort through entire height of tree: O(h)
	 */
		if ( isEmpty() ) return null;
		Position<Entry<K, V>> p = root();
		
		while ( tree.isInternal(p) ) {
			p = tree.left(p);
		}
		return tree.parent(p).getElement();
	}
	
	/**
	 * Returns the entry having the greatest key
	 * @return Entry<K, V>: entry
	 * @return null: tree is empty
	 */
	@Override
	@TimeComplexity("O(h)")
	public Entry<K, V> lastEntry() {
	/* TCJ
	 * Potentially has to sort through entire height of tree: O(h)
	 */
		if (isEmpty()) return null;
		return treeMax(root()).getElement();
	}
	
	/**
	 * Returns the entry with least greatest key less than or equal to given key
	 * @param key to be searched
	 * @return Entry<K, V>: entry
	 * @return null: tree is empty
	 */
	@Override
	@TimeComplexity("O(h)")
	public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException {
	/* TCJ
	 * Potentially has to sort through entire height of tree: O(h)
	 */	
		if ( key == null ) throw new IllegalArgumentException("key is null");
		if ( isEmpty() ) return null;
		
		Position<Entry<K ,V>> p = treeSearch(root(), key);
		if ( tree.isInternal(p) ) return p.getElement();
		while (p != root()) {
			if ( p == tree.left(tree.parent(p))) {
				return tree.parent(p).getElement();
			} else {
				p = tree.parent(p);
			}
		}
		return null;
	}

	/**
	 * Returns the entry with greatest key less than or equal to given key
	 * @param key to be searched
	 * @return Entry<K, V>: entry
	 * @return null: tree is empty
	 */
	@Override
	@TimeComplexity("O(h)")
	public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
	/* TCJ
	 * Potentially has to sort through entire height of tree: O(h)
	 */			
		if ( key == null ) throw new IllegalArgumentException("key is null");
		if ( isEmpty() ) return null;
		
		Position<Entry<K ,V>> p = treeSearch(root(), key);
		if ( tree.isInternal(p) ) return p.getElement();
		while (p != root()) {
			if ( p == tree.right(tree.parent(p))) {
				return tree.parent(p).getElement();
			} else {
				p = tree.parent(p);
			}
		}
		return null;
	}
	
	/**
	 * Returns the entry with greatest key strictly less than given key
	 * @param K: key to be searched
	 * @return Entry<K, V>: entry
	 * @return null: tree is empty
	 */
	@Override
	@TimeComplexity("O(h)")
	public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
	/* TCJ
	* Potentially has to sort through entire height of tree: O(h)
	*/			
		if ( key == null ) throw new IllegalArgumentException("key is null");
		if ( isEmpty() ) return null;
		
		Position<Entry<K, V>> p = treeSearch(root(), key);
		if ( tree.isInternal(p) && tree.isInternal(tree.left(p))) return treeMax(tree.left(p)).getElement();
		while (p != root()) {
			if ( p == tree.right(tree.parent(p))) {
				return tree.parent(p).getElement();
			} else {
				p = tree.parent(p);
			}
		}
		return null;
	}
	
	/**
	 * Returns the entry with least greatest key strictly higher than given key
	 * @param K: key to be searched
	 * @return Entry<K, V>: entry
	 * @return null: tree is empty
	 */
	@Override
	@TimeComplexity("O(h)")
	public Entry<K, V> higherEntry(K key) throws IllegalArgumentException {
	/* TCJ
	 * Potentially has to sort through entire height of tree: O(h)
	*/		
		if ( key == null ) throw new IllegalArgumentException("key is null");
		if ( isEmpty() ) return null;
		
		Position<Entry<K, V>> p = treeSearch(root(), key);
		if ( tree.isInternal(p) && tree.isInternal(tree.right(p))) return treeMax(tree.right(p)).getElement();
		
		while (p != root()) {
			if ( p == tree.left(tree.parent(p))) {
				return tree.parent(p).getElement();
			} else {
				p = tree.parent(p);
			}
		}
		return null;
	}

	/**
	 * Returns an iterable of entries with keys in range
	 * @param fromKey: beginning of range
	 * @param toKey: end of range
	 * @return Iterable<Entry<K, V>>: iterable entries
	 */
	@Override
	@TimeComplexity("O(s + h)")
	public Iterable<Entry<K, V>> subMap(K fromKey, K toKey)
			throws IllegalArgumentException {
		
		ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
		if ( fromKey.compareTo(toKey) < 0 ) {
			subMapRecurse(fromKey, toKey, root(), buffer);
		}
		return buffer;
	}

	/**
	 * @return boolean: tree is empty or not
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
	/* TCJ
	 * Continous operation: O(1)
	 */
		return tree.size() == 0;
	}
}
