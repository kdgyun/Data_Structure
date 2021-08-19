package _08_LinkedListDeque;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import Interface.Deque;


/**
*
* @param <E> the type of elements in this Deque
* 
* @author kdgyun (st-lab.tistory.com)
* @version 1.1.002
* @since 1.0.001
* @see Queue
* 
*/

public class LinkedListDeque<E> implements Deque<E>, Cloneable, Iterable<E> {
	
	private Node<E> head;	
	private Node<E> tail;
	private int size;

	public LinkedListDeque() {
		head = null;
		tail = null;
		size = 0;
	}
	
	public boolean offerFirst(E value) {	
		Node<E> newNode = new Node<E>(value);
		newNode.next = head;	
		if (head != null) {
			head.prev = newNode;
		}
		
		head = newNode;
		size++;
		if (head.next == null) {
			tail = head;
		}
		return true;
	}
	
	
	@Override
	public boolean offer(E value) {
		return offerLast(value);
	}

	public boolean offerLast(E value) {
		if (size == 0) {
			return offerFirst(value);
		}

		Node<E> newNode = new Node<E>(value);		
		tail.next = newNode; 
		newNode.prev = tail; 
		tail = newNode; 
		size++;
		return true;
	}

	@Override
	public E poll() {
		return pollFirst();
	}

	public E pollFirst() {
		if (size == 0) {
			return null;
		}
		E element = head.data;
		Node<E> nextNode = head.next;

		head.data = null;
		head.next = null;
		
		if (nextNode != null) {
			nextNode.prev = null;
		}
		head = null;
		head = nextNode;
		size--;
		if(size == 0) {
			tail = null;
		}
		
		return element;
	}

	public E remove() {
		return removeFirst();
	}
	
	public E removeFirst() {
		E element = poll();
		
		if(element == null) {
			throw new NoSuchElementException();
		}
		return element;
	}
	
	public E pollLast() {
		if (size == 0) {
			return null;
		}
		
		E element = tail.data;	
		Node<E> prevNode = tail.prev;
		
		tail.data = null;
		tail.prev = null;
		
		if (prevNode != null) {
			prevNode.next = null;
		}
		
		tail = null;
		tail = prevNode;
		size--;
		if(size == 0) {
			head = null;
		}
		return element;
	}

	public E removeLast() {
		E element = pollLast();
		
		if(element == null) {
			throw new NoSuchElementException();
		}
		return element;
	}
	
	@Override
	public E peek() {
		return peekFirst();
	}
	
	public E peekFirst() {
		if(size == 0) {
			return null;
		}
		return head.data;
	}
	
	public E peekLast() {
		if(size == 0) {
			return null;
		}
		return tail.data;
	}
	
	
	public E element() {
		return getFirst();
	}
	
	public E getFirst() {
		E item = peek(); 
		if(item == null) {
			throw new NoSuchElementException();
		}
		return item;	
	}
	
	public E getLast() {
		E item = peekLast();
		
		if(item == null) {
			throw new NoSuchElementException();
		}
		return item;
	}
	
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean contains(Object value) {
		if(value == null) {
			return false;
		}
		for(Node<E> x = head; x != null; x = x.next) {
			if(value.equals(x.data)) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		for (Node<E> x = head; x != null;) {
			Node<E> next = x.next;

			x.data = null;
			x.next = null;
			x.prev = null;
			x = next;
		}
		size = 0;
		head = tail = null;
	}
	
	public Object[] toArray() {
		Object[] array = new Object[size];
		int idx = 0;
		for (Node<E> x = head; x != null; x = x.next) {
			array[idx++] = (E) x.data;
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size) {
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		}
		int i = 0;
		Object[] result = a;
		for (Node<E> x = head; x != null; x = x.next)
			result[i++] = x.data;


		return a;
	}
	
	@Override
	public Object clone() {
		try {
			@SuppressWarnings("unchecked")
			LinkedListDeque<E> clone = (LinkedListDeque<E>) super.clone();
			clone.head = null;
			clone.tail = null;
			clone.size = 0;

			for(Node<E> x = head; x != null; x = x.next) {
				clone.offerLast(x.data);
			}
			return clone;
			
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	public void sort() {
		sort(null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sort(Comparator<? super E> c) {
		Object[] a = this.toArray();
		Arrays.sort(a, (Comparator) c);

		Iter it = (LinkedListDeque<E>.Iter) this.iterator();
		for(Object e : a) {
			it.next();
			it.set((E) e);
		}
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iter();
	}

	private class Iter implements Iterator<E> {

		private int nowIndex = 0;
		private Node<E> nextNode = head;
		private Node<E> nowNode;

		@Override
		public boolean hasNext() {
			return nowIndex < size;
		}

		@Override
		public E next() {
			int cs = nowIndex;
			if (cs >= size) {
				throw new NoSuchElementException();
			}
			nowNode = nextNode;
			nextNode = nextNode.next;
			nowIndex = cs + 1;
			return (E) nowNode.data;
		}
		
		public void set(E e) {
			if(nowNode == null) {
				throw new IllegalStateException();
			}
			nowNode.data = e;
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
