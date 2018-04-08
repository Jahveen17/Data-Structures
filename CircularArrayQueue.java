
/**
 *
 * Assignment #1
 * CircularArrayQueue
 * 
 * CircularArrayQueue is a data structure which stores elements in an array in a circular queue manner.
 *
 * @author Javen Zamojcin
 * @param <E> - formal type 
 *
 */

package cs2321;

import net.datastructures.Queue;

public class CircularArrayQueue<E> implements Queue<E> {

	private int size, front, rear, capacity;
	private E[] data;
	
	@SuppressWarnings("unchecked")
	public CircularArrayQueue(int queueSize) {
		
		capacity = queueSize;
		size = front = rear = 0;
		data = (E[]) new Object[capacity];
	}
	
	/**
	 * Retrieves value of global Size variable.
	 * @return Integer value of Size
	 */
	@TimeComplexity("O(1)")
	@Override
	public int size() { return size; }
	
	/**
	 * Determines whether global Size variable is equal to 0 or not.
	 * @return Boolean value
	 */
	@TimeComplexity("O(1)")
	@Override
	public boolean isEmpty() { return size <= 0; }

	/**
	 * Adds element to the end of the array.
	 * @param e Element to be added to queue.
	 * @return void
	 * @throws IllegalStateException if the queue is already full.
	 */
	@TimeComplexity("O(1)")
	@Override
	public void enqueue(E e) throws IllegalStateException {
		/* TCJ
		 * Element e is added to rear of queue; location is known.
		 * No shifting is required
		 */
		
		if ( size > capacity ) {
			throw new IllegalStateException("Queue is full");
		}
		
		if (!isEmpty()) {
			rear++;
			data[rear] = e;
		} else {
			data[rear] = e;
		}
		
		size++;
	}

	/**
	 * Retrieves value of first element in queue.
	 * @return E value of first data element.
	 */
	@TimeComplexity("O(1)")
	@Override
	public E first() { return data[front]; }
	
	/**
	 * Retrieves value of last element in queue.
	 * @return E value of last data element.
	 */
	@TimeComplexity("O(1)")
	public E last() { return data[rear]; }

	/**
	 * Removes and returns first element in queue.
	 * @return E value of first data element.
	 */
	@TimeComplexity("O(1)")
	@Override
	public E dequeue() {
		/* TCJ
		 * Element e is removed from front of queue; location is known.
		 * No shifting of elements is required.
		 */
		
		if ( size <= 0 ) {
			return null;
		}
		
		E temp = data[front];
		data[front] = null;
		front++;
		size--;
		
		return temp;
	}

}
