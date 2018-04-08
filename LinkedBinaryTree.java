package cs2321;
import java.util.Iterator;

import net.datastructures.*;
	
/**
 * LinkedBinaryTree
 * 
 * Course: CS2321 Data Structures
 * Assignment #5
 * @author Javen Zamojcin
 *
 * @param <E>
 */

public class LinkedBinaryTree<E> implements BinaryTree<E>{
	
	//Instance variables
	private Node<E> root = null;
	private int size = 0;
	
	//Constructor
	public LinkedBinaryTree( ) { }
	
	//Helper Methods
	private void inorderSubtree(Position<E> p, List<Position<E>> snapshot) {
		if (left(p) != null) {
			inorderSubtree(left(p), snapshot);
		}
		snapshot.add(0, p);
		if ( right(p) != null ) {
			inorderSubtree(right(p), snapshot);
		}
	}
	
	private Node<E> createNode(E element, Node<E> parent, Node<E> left, Node<E> right ) {
		
		return new Node<E>(element, parent, left, right);
	}
	
	private Node<E> validate(Position<E> p) throws IllegalArgumentException {
		
		if ( !(p instanceof Node) )
			throw new IllegalArgumentException("Not valid position type");
		Node<E> node = (Node<E>) p;
		if ( node.getParent() == node )
			throw new IllegalArgumentException("p is no longer in the tree");
		return node;
	}
	
	private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) {
		snapshot.add(0, p);
		for ( Position<E> c : children(p))
			preorderSubtree(c, snapshot);
	}
	
	private Iterable<Position<E>> preorder() {
		
		List<Position<E>> snapshot = new ArrayList<>();
		if ( !isEmpty() )
			preorderSubtree(root(), snapshot);
		return snapshot;
	}
	
	//Instance Methods
	public Iterable<Position<E>> inorder() {
		List<Position<E>> snapshot = new ArrayList<>();
		if ( !isEmpty() ) {
			inorderSubtree(root(), snapshot);
		}
		return snapshot;
	}
	
	public E set(Position<E> p, E e) throws IllegalArgumentException {
		Node<E> node = validate(p);
		E temp = node.getElement();
		node.setElement(e);
		return temp;
	}
	
	public E remove(Position<E> p) throws IllegalArgumentException {
		
		Node<E> node = validate(p);
		
		if ( numChildren(p) == 2 ) {
			throw new IllegalArgumentException("position has two children");
		}
		
		Node<E> child = (node.getLeft() != null ? node.getLeft() : node.getRight());
		if ( child != null )
			child.setParent(node.getParent());
		if ( node == root )
			root = child;
		else { 
			Node<E> parent = node.getParent();
			if ( node == parent.getLeft())
				parent.setLeft(child);
			else {
				parent.setRight(child);
			}
		}
		size--;
		E temp = node.getElement();
		node.setElement(null);
		node.setLeft(null);
		node.setRight(null);
		node.setParent(node);
		return temp;
		
	}
	
	/** 
	 * @return Value of root of type Position<E>
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> root() {
	/* TCJ
	 * Returns known value of a variable: O(1)
	 */
		return root;
	}
	
	/**
	 * Returns the value of the position of the parent node
	 * @param Position<E> p of node
	 * @return Parent node
	 * @throws IllegalArgumentException if position is not available
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
	/** TCJ
	 * 	Returns known value of a variable: O(1)
	 */
		Node<E> node = validate(p);
		return node.getParent();
	}

	/**
	 * Retreives the child of a node
	 * @param Position<E> 'p' of parent node
	 * @return Positional node of child
	 * @throws IllegalArgumentEception if position is not available
	 */
	@Override
	@TimeComplexity("O(c(p)+1)")
	public Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException {
		/* TCJ
		 * Depends on amount of children at P
		 */
		List<Position<E>> snapshot = new ArrayList<>(2);
		if ( left(p) != null )
			snapshot.add(0, left(p));
		if ( right(p) != null )
			snapshot.add(1, right(p));
		return snapshot;
	}
	
	/**
	 * Determines the amount of children at a Position
	 * @param Position<E> 'p' of parent node
	 * @return int of amount of children nodes
	 * @throws IllegalArgumentException if position is not available
	 */
	@Override
	@TimeComplexity("O(1)")
	public int numChildren(Position<E> p) throws IllegalArgumentException {
		/* TCJ
		 * Simply tests two known variables: O(1)
		 */
		int count = 0;
		if ( left(p) != null)
			count++;
		if ( right(p) != null)
			count++;
	
		return count;
	}

	/**
	 * Determines whether a position node is internal or not
	 * @param Position<P> 'p' to check
	 * @return boolean of whether 'p' is internal
	 * @throws IllegalArgumentException if 'p' is not available.
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isInternal(Position<E> p) throws IllegalArgumentException {
	/* TCJ
	 * Checks a boolean: O(1)
	 * 	
	 */
		return numChildren(p) > 0;
	}

	/**
	 * Determines whether a position node is external or not
	 * @param Position<P> 'p' to check
	 * @return boolean of whether 'p' is external
	 * @throws IllegalArgumentException if 'p' is not available.
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isExternal(Position<E> p) throws IllegalArgumentException {
		/* TCJ
		 * Checks a boolean: O(1)
		 * 	
		 */
		return numChildren(p) == 0;
	}
	
	/**
	 * Determines if a position is the root of tree
	 * @param Position<E> 'p' to be checked
	 * @return boolean of whether 'p' is root
	 * @throws IllegalArgumentException if 'p' is not available
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isRoot(Position<E> p) throws IllegalArgumentException {
		/* TCJ
		 * Checks a boolean: O(1)
		 * 	
		 */
		return p == root();
	}
	
	/**
	 * @return size of tree
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() {
	/* TCJ
	* Size is a known variable: O(1)
	*/
		return size;
	}

	/**
	 * @return Whether the tree is empty or not
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
		/* TCJ
		 * Size is a known variable: O(1)
		 */
		return size == 0;
	}

	/**
	 * Creates an instance of the element iterator
	 * @return New Iterator
	 */
	@Override
	@TimeComplexity("O(1)")
	public Iterator<E> iterator() {
	/* TCJ
	 * Simply creates new instance of ElementIterator object: O(1)
	 */
		return new ElementIterator();
	}
	
	/**
	 * Retrieves the iterable position of a preorder traversal
	 * @return Iterable<Position<E>> of preorder traversal
	 */
	@Override
	@TimeComplexity("O(1)")
	public Iterable<Position<E>> positions() {
		
		return preorder();
	}

	/**
	 * Retrieves left child node of a parent node
	 * @param Parent Position<E> 'p'
	 * @return Position<E> of left child node
	 * @throws IllegalArgumentException if position is not available
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> left(Position<E> p) throws IllegalArgumentException {
		/* TCJ
		 * Returns value of known variable: O(1)
		 */
		Node<E> node = validate(p);
		return node.getLeft();
	}

	/**
	 * Retrieves left child node of a parent node
	 * @param Parent Position<E> 'p'
	 * @return Position<E> of left child node
	 * @throws IllegalArgumentException if position is not available
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> right(Position<E> p) throws IllegalArgumentException {
		
		Node<E> node = validate(p);
		return node.getRight();
	}

	/**
	 * Returns the sibling of a Position
	 * @param Position<E> 'p' of node
	 * @return Position<E> of sibling node
	 * @return null if Position<E> is root
	 * @throws IllegalArgumentException if position 'p' is not available
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> sibling(Position<E> p) throws IllegalArgumentException {
		
		Position<E> parent = parent(p);
		if ( parent == null ) return null;
		if ( p == left(parent))
			return right(parent);
		else
			return left(parent);
		
	}
	
	/** creates a root for an empty tree, storing e as element, and returns the 
	 * position of that root. An error occurs if tree is not empty. 
	 * @param Element e to be added at root
	 * @return Position of root
	 * @throws IllegalStateException if tree is not empty
	 */
	@TimeComplexity("O(1)")
	public Position<E> addRoot(E e) throws IllegalStateException {
		
		if ( !isEmpty() ) throw new IllegalStateException("Tree is not empty");
		root = createNode(e, null, null, null);
		size++;
		return root;
	}
	
	/** creates a new left child of Position p storing element e, return the left child's position.
	 * If p has a left child already, throw exception IllegalArgumentExeption. 
	 * @param Element 'e' to be added to the left of Position<E> p
	 * @throws IllegalArgumentException
	 */
	@TimeComplexity("O(1)")
	public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {
		
		Node<E> parent = validate(p);
		if ( parent.getLeft() != null)
			throw new IllegalArgumentException("p already has a left child");
		Node<E> child = createNode(e, parent, null, null);
		parent.setLeft(child);
		size++;
		return child;
	}

	/** creates a new right child of Position p storing element e, return the right child's position.
	 * If p has a right child already, throw exception IllegalArgumentExeption. 
	 * @param Element 'e' to be added to the right of Position<E> 'p'
	 * @throws IllegalArgumentException
	 */
	@TimeComplexity("O(1)")
	public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {
		
		Node<E> parent = validate(p);
		if ( parent.getRight() != null)
			throw new IllegalArgumentException("p already has a right child");
		Node<E> child = createNode(e, parent, null, null);
		parent.setRight(child);
		size++;
		return child;
	}
	
	/** Attach trees t1 and t2 as left and right subtrees of external Position. 
	 * if p is not external, throw IllegalArgumentExeption.
	 * @param Position<E> p, LinkedBinaryTree<E> tree1, LinkedBinaryTree<E> tree2
	 * @throws IllegalArgumentException
	 */
	@TimeComplexity("O(1)")
	public void attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2)
			throws IllegalArgumentException {
		
		Node<E> node = validate(p);
		if ( isInternal(p) ) throw new IllegalArgumentException("p must be a leaf");
		size += t1.size() + t2.size();
		
		if ( !t1.isEmpty() ) {
			t1.root.setParent(node);
			node.setLeft(t1.root);
			t1.root = null;
			t1.size = 0;
		}

		if ( !t2.isEmpty() ) {
			t2.root.setParent(node);
			node.setRight(t2.root);
			t2.root = null;
			t2.size = 0;
		}
	}
	
	public class Node<E> implements Position<E> {

		private E element;
		private Node<E> parent;
		private Node<E> left;
		private Node<E> right;
		
		@SuppressWarnings("unused")
		public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild) {
			
			element = e;
			parent = above;
			left = leftChild;
			right = rightChild;	
		}
		
		//Getter methods
		@Override
		public E getElement() throws IllegalStateException { return element; }
		
		public Node<E> getParent() { return parent; }
		
		public Node<E> getLeft() { return left; }
		
		public Node<E> getRight() { return right; }
		
		//Setter methods
		public void setElement(E e) { element = e; }
		
		public void setParent(Node<E> e) { parent = e; }
		
		public void setLeft(Node<E> e) { left = e; }
		
		public void setRight(Node<E> e) { right = e; }
		
	}
	
	private class ElementIterator implements Iterator<E> {

		Iterator<Position<E>> posIterator = positions().iterator();
		
		@Override
		public boolean hasNext() { return posIterator.hasNext(); }

		@Override
		public E next() { return posIterator.next().getElement(); }
		
		public void remove() { posIterator.remove(); }
		
	}
}
