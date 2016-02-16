package obberton13.Util;

import java.util.LinkedList;

/**
 * Created by Obberton on 2/14/2016.
 */
public class Queue<T> {
	private LinkedList<T> _queue;

	public Queue()
	{
		_queue = new LinkedList<>();
	}

	synchronized public void enqueue(T item)
	{
		_queue.addLast(item);
	}

	synchronized public T dequeue()
	{
		return _queue.removeFirst();
	}

	synchronized public T peek()
	{
		return _queue.peekFirst();
	}

	synchronized public int size()
	{
		return _queue.size();
	}

	synchronized public boolean isEmpty()
	{
		return _queue.isEmpty();
	}
}
