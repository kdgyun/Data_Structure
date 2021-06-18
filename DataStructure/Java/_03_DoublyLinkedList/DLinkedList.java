package _03_DoublyLinkedList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import Interface.List;

/**
*
* @param <E> the type of elements in this list
* 
* @author kdgyun (st-lab.tistory.com)
* @version 1.0
* @see List
* 
*/

public class DLinkedList<E> implements List<E>, Iterable<E>, Cloneable {

	private Node<E> head;
	private Node<E> tail;
	private int size;
	
	public DLinkedList() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}

	private Node<E> search(int index) {

		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		if (index > size / 2) {
			Node<E> x = tail;
			for (int i = size - 1; i > index; i--) {
				x = x.prev;
			}
			return x;
		}

		else {
			Node<E> x = head;
			for (int i = 0; i < index; i++) {
				x = x.next;
			}
			return x;
		}
	}
	
	public void addFirst(E value) {
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

	}
	
	@Override
	public boolean add(E value) {
		addLast(value);
		return true;
	}


	public void addLast(E value) {
		Node<E> newNode = new Node<E>(value);

		if (size == 0) {
			addFirst(value);
			return;
		}

		tail.next = newNode;
		newNode.prev = tail;
		tail = newNode;
		size++;
	}

	@Override
	public void add(int index, E value) {

		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			addFirst(value);
			return;
		}
		if (index == size) {
			addLast(value);
			return;
		}
		Node<E> prev_Node = search(index - 1);
		Node<E> next_Node = prev_Node.next;
		Node<E> newNode = new Node<E>(value);
		
		prev_Node.next = null;
		next_Node.prev = null;
		
		prev_Node.next = newNode;
		
		newNode.prev = prev_Node;
		newNode.next = next_Node;
		
		next_Node.prev = newNode;
		size++;
	}
	
	

	public E remove() {

		Node<E> headNode = head;
		if (headNode == null) {
			throw new NoSuchElementException();
		}
		
		E element = headNode.data;
		Node<E> nextNode = head.next;
		
		head.data = null;
		head.next = null;
		
		if(nextNode != null) {
			nextNode.prev = null;
		}
		
		head = nextNode;
		size--;
		
		if(size == 0) {
			tail = null;
		}

		return element;
	}
	
	@Override
	public E remove(int index) {

		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		
		if (index == 0) {
			E element = head.data;
			remove();
			return element;
		}

		Node<E> prevNode = search(index - 1);
		Node<E> removedNode = prevNode.next;
		Node<E> nextNode = removedNode.next;

		E element = removedNode.data;	
		
		prevNode.next = null;
		removedNode.next = null;
		removedNode.prev = null;
		removedNode.data = null;
		
		if(nextNode != null) {
			nextNode.prev = null;
			
			nextNode.prev = prevNode;
			prevNode.next = nextNode;
		}

		else {	 
			tail = prevNode;
		}

		size--;

		return element;
	}

	@Override
	public boolean remove(Object value) {

		Node<E> prevNode = head;
		Node<E> x = head;
		for (; x != null; x = x.next) {
			if (value.equals(x.data)) {
				break;
			}
			prevNode = x;
		}

		if(x == null) {
			return false;
		}
		
		if (x.equals(head)) {
			remove();
			return true;
		}

		else {
			Node<E> nextNode = x.next;
			
			prevNode.next = null;
			x.data = null;
			x.next = null;
			x.prev = null;
			
			if(nextNode != null) {
				nextNode.prev = null;
				
				nextNode.prev = prevNode;
				prevNode.next = nextNode;
			}
			else {
				tail = prevNode;
			}

			size--;
			return true;
		}

	}

	@Override
	public E get(int index) {
		return search(index).data;
	}
	
	@Override
	public void set(int index, E value) {

		Node<E> prev = search(index);
		prev.data = null;
		prev.data = value;
	}


	@Override
	public boolean contains(Object item) {
		return indexOf(item) >= 0;
	}

	@Override
	public int indexOf(Object o) {
		int index = 0;
		if (o == null) {
			for (Node<E> x = head; x != null; x = x.next) {
				if (x.data == null)
					return index;
				index++;
			}
		} else {
			for (Node<E> x = head; x != null; x = x.next) {
				if (o.equals(x.data))
					return index;
				index++;
			}
		}
		return -1;
	}

	public int lastIndexOf(Object o) {
		int index = size;
		if (o == null) {
			for (Node<E> x = tail; x != null; x = x.prev) {
				index--;
				if (x.data == null)
					return index;
			}
		} else {
			for (Node<E> x = tail; x != null; x = x.prev) {
				index--;
				if (o.equals(x.data))
					return index;
			}
		}
		return -1;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public void clear() {
		for (Node<E> x = head; x != null;) {
			Node<E> next = x.next;
			x.data = null;
			x.next = null;
			x.prev = null;
			x = next;
		}
		head = tail = null;
		size = 0;
	}
	
	
	public Object clone() {
		try {
			@SuppressWarnings("unchecked")
			DLinkedList<? super E> clone = (DLinkedList<? super E>) super.clone();

			clone.head = null;
			clone.tail = null;
			clone.size = 0;

			for (Node<E> x = head; x != null; x = x.next) {
				clone.addLast(x.data);
			}

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
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
			// Arrya.newInstance(컴포넌트 타입, 생성할 크기) 
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		}
		int i = 0;
		Object[] result = a;
		for (Node<E> x = head; x != null; x = x.next) {
			result[i++] = x.data;
		}
		if (a.length > size) {
			a[size] = null;
		}
		return a;
	}
	
	public void sort() {
		sort(null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sort(Comparator<? super E> c) {
		Object[] a = this.toArray();
		Arrays.sort(a, (Comparator) c);

		Iter it = (DLinkedList<E>.Iter) this.iterator();
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
