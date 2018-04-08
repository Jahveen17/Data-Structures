package cs2321;


import java.util.Iterator;

import net.datastructures.List;

/**
 * 
 * Assignment #1
 * ArrayList
 * 
 * ArrayList is a data structure which stores elements in an array. 
 * An initial capacity is set, but is doubled if size exceeds capacity.
 * 
 * @author Javen Zamojcin
 *
 * @param <E> - formal type.
 */

public class ArrayList<E> implements List<E> {
	
	private E[] data;
	private int size = 0;
	private int capacity = 0;

	@SuppressWarnings("unchecked")
	public ArrayList() {
		
		capacity = 16;
		data = (E[]) new Object[capacity];
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList(int capacity) {
		
		this.capacity = capacity;
		data = (E[]) new Object[capacity];
	}

	/**
	 * Checks whether size is greater than capacity.
	 * If size is greater, capacity is doubled and the contents of array are copied into bigger array.
	 * @param size the value of size wanted to be checked.
	 */
	@TimeComplexity("O(n)")
	@SuppressWarnings("unchecked")
	public void checkCapacity(int size) {
		/* TCJ
		 * Each element in array is processed and copied, requiring n time. 
		 * Worst case O(n), best case O(1).
		 */
		if ( size >= capacity ) {
			
			E[] tempData = (E[]) new Object[capacity];
			
			for ( int i = 0; i < capacity; i++ ) {
				tempData[i] = data[i];
			}
			
			data = (E[]) new Object[capacity * 2];
			
			for ( int i = 0; i < capacity; i++ ) {
				data[i] = tempData[i];
			}
			
			capacity = capacity * 2;
		}
	}
	
	/**
	 * Retrieves value of Size variable
	 * @return Integer value of size
	 */
	@TimeComplexity("O(1)")
	@Override
	public int size() { return size; }

	/**
	 * Determines whether Size variable is equal to 0 or not.
	 * @return boolean value of size
	 */
	@TimeComplexity("O(1)")
	@Override
	public boolean isEmpty() { return size() == 0; }

	/**
	 * Retrieves value of element at index i
	 * @param Index i
	 * @return E element value
	 * @throws IndexOutOfBoundsException if index i is out of range of size.
	 */
	@TimeComplexity("O(1)")
	@Override
	public E get(int i) throws IndexOutOfBoundsException {
		/* TCJ
		 * Element is retrieved at known index.
		 * Best time O(1), worst time O(1).
		 */
		
		if ( i > size ) throw new IndexOutOfBoundsException (" Array too small ");
		
		return data[i];
	}

	/**
	 * Element e replaces value at index i.
	 * @param index i, element e
	 * @return value of old element at index i.
	 * @throws IndexOutOfBoundsException if index i is out of range of size.
	 */
	@TimeComplexity("O(1)")
	@Override
	public E set(int i, E e) throws IndexOutOfBoundsException {
		/* TCJ
		 * Array is accessed with known index.
		 * Worst case O(1), Best case O(1).
		 * 
		 */
		
		if ( i > size ) throw new IndexOutOfBoundsException (" Array too small ");
		
		if ( data[i] == null ) {
			size++;
		}
		
		E temp = data[i];
		data[i] = e;
		return temp;
	}
	
	/**
	 * Element e is added at index i, shifting all elements at index i and beyond shift one position to the right.
	 * @param index i, element e
	 * @throws IndexOutOfBoundsException if index i is out of range of size.
	 */
	@TimeComplexity("O(n)")
	@Override
	public void add(int i, E e) throws IndexOutOfBoundsException {
		/* TCJ
		 * Each element at index i and beyond has to be processed and shifted over.
		 * Worst case O(n), best case O(1).
		 */
		
		if ( i > capacity ) throw new IndexOutOfBoundsException (" Array too small ");
		
		checkCapacity(size + 1);
		
		
		E temp = data[i];
		data[i] = e;
		size++;
		
		for ( int j = i + 1; j < size(); j++ ) {
			
			E temp2 = data[j]; 
			data[j] = temp;	
			temp = temp2;
		}
	}
	
	/**
	 * Element located at index i is removed, shifting all values right of the element one 
	 * position to the left.
	 * @param index i
	 * @return E element
	 * @throws IndexOutOfBoundsException if index i is out of range of size.
	 */
	@TimeComplexity("O(n)")
	@Override
	public E remove(int i) throws IndexOutOfBoundsException {
		/* TCJ
		 * Each element at index i and to the right has to be processed and
		 * shifted one position to the left.
		 * Worst case O(n), Best case O(1).
		 */
		
		if ( i > size ) throw new IndexOutOfBoundsException (" Array too small ");
		
		E temp1 = data[i];
		data[i] = null;
		
		if ( data[i + 1] != null ) {
			
			for ( int j = i; j < size - 1; j++ ) {
				
				data[j] = data[j + 1];
			}
		}
		
		size--;
		return temp1;
	}

	/**
	 * Element e is added at first index
	 * @param Element e
	 * @throws IndexOutOfBoundsException if index i is out of range of size.
	 */
	@TimeComplexity("O(n)")
	public void addFirst(E e) throws IndexOutOfBoundsException { 
		/* TCJ
		 * Each element at index i and beyond has to be processed and shifted over.
		 * Worst case O(n), best case O(1).
		 */
		add(0, e);
	}
	
	/**
	 * Element e is added at last index
	 * @param e
	 * @throws IndexOutOfBoundsException if element is out of range of capacity. 
	 */
	@TimeComplexity("O(n)")
	public void addLast(E e) throws IndexOutOfBoundsException { 
		/* TCJ
		 * Each element at index i and beyond has to be processed and shifted over.
		 * Worst case O(n), best case O(1).
		 */
		add(capacity - 1, e); 
	}
	
	@TimeComplexity("O(n)")
	public E removeFirst() throws IndexOutOfBoundsException { return remove(0); }
	
	@TimeComplexity("O(n)")
	public E removeLast() throws IndexOutOfBoundsException { return remove(data.length - 1); }
	
	@TimeComplexity("O(1)")
	@Override
	public Iterator<E> iterator() { 
		
		Iterator<E> iterator = new myIterator();
		return iterator;
	}

	private class myIterator implements Iterator<E> {

		int cursor = 0;
		
		@Override
		public boolean hasNext() { return cursor < size; }

		@Override
		public E next() {
			
			E temp = data[cursor];
			cursor++;
			
			return temp;
		}
	}
}
