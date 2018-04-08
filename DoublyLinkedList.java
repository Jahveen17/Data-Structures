package cs2321;

import java.util.Iterator;


import net.datastructures.Position;
import net.datastructures.PositionalList;

/**
 * 
 * Assignment #1
 * DoublyLinkedList
 * 
 * DoublyLinkedList is a data structure which uses a node chain with reference values to both
 * the previous and next node in the chain.
 * 
 * @author Javen Zamojcin
 * @param <E> - Formal Type
 */

public class DoublyLinkedList<E> implements PositionalList<E> {

	private Node<E> header;
	private Node<E> trailer;
	private int size = 0;

	public DoublyLinkedList() {

		header = new Node<E>(null, null, null);
		trailer = new Node<E>(null, header, null);
		header.setNext(trailer);
	}

	/**
	 * Determines if node is within the range of the list. 
	 * Returns null if node is not.
	 * @param Node<E> node
	 * @return node; null.
	 */
	@TimeComplexity("O(1)")
	private Position<E> position(Node<E> node) {
		/* TCJ
		 * Processes only one element.
		 * Worst Case O(1), Best Case O(n).
		 */

		if ( node == header || node == trailer ) {
			return null;
		}

		return node;
	}

	/**
	 * Verifies 'p' is an instance of class Node.
	 * Checks to make sure 'p's next element isn't null.
	 * 
	 * @param Position p
	 * @return node
	 * @throws IllegalArgumentException if 'p' is not an instance of Node or if node's next element is null.
	 */
	@TimeComplexity("O(1)")
	private Node<E> validate(Position<E> p) throws IllegalArgumentException {

		if (!(p instanceof Node )) throw new IllegalArgumentException ("Illegal Position");
		Node<E> node = (Node<E>) p;
		if (node.getNext() == null ) throw new IllegalArgumentException ("Position is no longer in the list");

		return node;
	}

	/**
	 * Adds element in between two nodes.
	 * 
	 * @param Element e
	 * @param previous node
	 * @param succeeding node
	 * @return Position of new node
	 */
	private Position<E> addBetween(E e, Node<E> pred, Node<E> succ) {

		Node<E> newest = new Node<>(e, pred, succ);
		pred.setNext(newest);
		succ.setPrev(newest);
		size++;

		return newest;
	}

	/**
	 * Retrieves value of variable size.
	 * @return Integer size
	 */
	@TimeComplexity("O(1)")
	@Override
	public int size() { 
		/* TCJ
		 * Direct retrieval of size variable.
		 * Worst case O(1), Best case O(1)
		 */
		return size; 
	}

	/**
	 * Checks value of variable size.
	 * @return Boolean
	 */
	@TimeComplexity("O(1)")
	@Override
	public boolean isEmpty() { 
		/* TCJ
		 * Checks value of variable size
		 * Worst case O(1), Best case O(1)
		 */
		return size == 0;
	}
	
	/**
	 * Retrieves Position value of first node.
	 * @return Position<E>; Null if first node is empty.
	 */
	@TimeComplexity("O(1)")
	@Override
	public Position<E> first() { 
		/* TCJ
		 * Exact location of node is known; only one element is processed.
		 * Worst case O(1), Best case O(1).
		 */
		if ( isEmpty() ) 
			return null;
		
		return header.getNext();
	}
	
	public String retrieveElement(int i) {
		
		String retrieve = "header";
		
		for ( int j = 0; j < i; j++ ) {
			
			retrieve += ".getNext()";
		}
			
		return retrieve;
	}

	/**
	 * Retrieves Position value of last node.
	 * @return Position<E>; Null if last node is empty. 
	 */
	@TimeComplexity("O(1)")
	@Override
	public Position<E> last() {
		/* TCJ
		 * Exact location of node is known; only one element is processed. 
		 * Worst case O(1), Best case O(1).
		 */
		if ( isEmpty() ) 
			return null;
		return trailer.getPrev(); 
	}

	/**
	 * Returns the value of the position of the node before position p.
	 * @return Position<E> of previous node.
	 */
	@TimeComplexity("O(1)")
	@Override
	public Position<E> before(Position<E> p) throws IllegalArgumentException { 
		/* TCJ
		 * Exact location is known; only one element is processed.
		 * Worst case O(1), Best case O(1).
		 */
		Node<E> node = validate(p);
		return position(node.getPrev());
	}

	/**
	 * Returns value of position of node after position p
	 * @param Position<E> p
	 * @return Position<E> of node after p
	 * @throws IllegalArgumentException if next node of p is out of range.
	 */
	@TimeComplexity("O(1)")
	@Override
	public Position<E> after(Position<E> p) throws IllegalArgumentException { 
		/*
		 * Exact location is known; only one element is processed.
		 * Worst case O(1), Best case O(1).
		 */
		Node<E> node = validate(p);
		return position(node.getNext());
	}

	/**
	 * Element e is added to the first position in the node chain.
	 * @param Element e
	 * @return Position<E> of element e
	 */
	@TimeComplexity("O(n)")
	@Override
	public Position<E> addFirst(E e) { 
		/* TCJ
		 * Uses addBetween method, which is O(n) time; processes e + 2 elements.
		 * Worst case O(n), Best case O(n).
		 */
		return addBetween(e, header, header.getNext());
	} 

	/**
	 * Element e is added to the last position in the node chain.
	 * @param Element e
	 * @return Position<E> of element e
	 */
	@TimeComplexity("O(n)")
	@Override
	public Position<E> addLast(E e) { 
		/* TCJ
		 * Uses addBetween method, which is O(n) time; processes e + 2 elements.
		 * Worst case O(n), Best case O(n).
		 */
		return addBetween(e, trailer.getPrev(), trailer);
	}

	/**
	 * Element e is added to the position before p.
	 * @param Element e, Position<E> p
	 * @return Position<E> of element e
	 * @throws illegalArgumentException if node before p is out of range. 
	 */
	@TimeComplexity("O(1)")
	@Override
	public Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException {
		/* TCJ
		 * Exact location is known; only one element is processed.
		 * Worst case O(1), Best case O(1).
		 */
		Node<E> node = validate(p);
		return addBetween(e, node.getPrev(), node);
	}

	/**
	 * Element e is added to the position after p.
	 * @param Element e, Position<E> p
	 * @return Position<E> of element e
	 * @throws illegalArgumentException if node before p is out of range. 
	 */
	@TimeComplexity("O(1)")
	@Override
	public Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException {
		/* TCJ
		 * Exact location is known; only one element is processed.
		 * Worst case O(1), Best case O(1).
		 */
		Node<E> node = validate(p);
		return addBetween(e, node, node.getNext());
	}

	/**
	 * Element at Position<E> p is replaced by element e.
	 * @param Position<E> p, Element e
	 * @return E answer
	 * @throws IllegalArgumentException if Position<E> is out of range
	 */
	@TimeComplexity("O(1)")
	@Override
	public E set(Position<E> p, E e) throws IllegalArgumentException {
		/*
		 * Exact location is known; only one element is processed
		 * Worst case O(1), Best case O(1).
		 */
		Node<E> node = validate(p);
		E answer = node.getElement();
		node.setElement(e);

		return answer;
	}

	/**
	 * Removes node at Position<E> p
	 * Replacing it with null and making appropriate changes to reference values.
	 * @param Position<E> p
	 * @return E answer
	 * @throws IllegalArgumentException if Position<E> is out of range. 
	 */
	@TimeComplexity("O(1)")
	@Override
	public E remove(Position<E> p) throws IllegalArgumentException {

		Node<E> node = validate(p);
		Node<E> predecessor = node.getPrev();
		Node<E> successor = node.getNext();
		predecessor.setNext(successor);
		successor.setPrev(predecessor);
		size--;
		E answer = node.getElement();
		node.setElement(null);
		node.setNext(null);
		node.setPrev(null);

		return answer;
	}

	@TimeComplexity("O(1)")
	@Override
	public Iterator<E> iterator() { return new myIterator(); }

	@TimeComplexity("O(1)")
	@Override
	public Iterable<Position<E>> positions() { return new myIterable2(); }

	@TimeComplexity("O(1)")
	public E removeFirst() throws IllegalArgumentException { return remove(header.getNext()); }

	@TimeComplexity("O(1)")
	public E removeLast() throws IllegalArgumentException { return remove(trailer.getPrev()); }

	private static class Node<E> implements Position<E> {

		private E element;
		private Node<E> next;
		private Node<E> prev;

		public Node(E e, Node<E> p, Node<E> n) {

			element = e;
			next = n;
			prev = p;
		}

		@Override
		public E getElement() throws IllegalStateException { 

			if ( next == null ) {
				throw new IllegalStateException("Position is no longer available ");
			}
			return element; 
		}

		@TimeComplexity("O(1)")
		public void setElement(E element) {
			this.element = element;
		}

		@TimeComplexity("O(1)")
		public Node<E> getPrev() {return prev; }

		@TimeComplexity("O(1)")
		public Node<E> getNext() {return next; }

		@TimeComplexity("O(1)")
		public void setPrev(Node<E> e) { this.prev = e; }

		@TimeComplexity("O(1)")
		public void setNext(Node<E> e) { this.next = e; }
	}

	private class myIterator implements Iterator<E> {

		private Position<E> cursor = first();
		private Position<E> recent = null;

		@TimeComplexity("O(1)")
		@Override
		public boolean hasNext() { return (cursor != null); }

		@TimeComplexity("O(1)")
		@Override
		public E next() throws IndexOutOfBoundsException {

			if ( cursor == null ) throw new IndexOutOfBoundsException(" Nothing left ");
			recent = cursor;
			cursor = after(cursor);

			return recent.getElement();
			
		}
	}

	private class myIterable implements Iterator<Position<E>> {

		private Position<E> cursor = first();
		private Position<E> recent = null;

		@TimeComplexity("O(1)")
		public boolean hasNext() { return (cursor != null); }

		@TimeComplexity("O(1)")
		public Position<E> next() throws IndexOutOfBoundsException {

			if ( cursor == null ) throw new IndexOutOfBoundsException(" Nothing left ");
			recent = cursor;
			cursor = after(cursor);

			return recent;
		}
	}	
	
	private class myIterable2 implements Iterable<Position<E>> {

		@TimeComplexity("O(1)")
		@Override
		public Iterator<Position<E>> iterator() { return new myIterable(); }
	}
}
