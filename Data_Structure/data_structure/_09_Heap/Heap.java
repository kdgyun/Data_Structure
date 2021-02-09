package _09_Heap;

/**
*
* @param <E> the type of elements in this Heap
* 
* @author kdgyun (st-lab.tistory.com)
* @version 1.0
* 
*/

import java.util.Comparator;
import java.util.NoSuchElementException;

public class Heap<E> {

	private final Comparator<? super E> comparator;
	private static final int DEFAULT_CAPACITY = 10;	 

	private int size;
	Object[] array;

	public Heap() {
		this(null);
	}
	
	public Heap(Comparator<? super E> comparator) {
		this.array = new Object[DEFAULT_CAPACITY];
		this.size = 0;
		this.comparator = comparator;
	}

	public Heap(int capacity) {
		this(capacity, null);
	}
	
	public Heap(int capacity, Comparator<? super E> comparator) {
		this.array = new Object[capacity];
		this.size = 0;
		this.comparator = comparator;
	}
	

	private void resize(int newCapacity) {
		Object[] newArray = new Object[newCapacity];
		for(int i = 1; i <= size; i++) {
			newArray[i] = array[i];
		}
		this.array = null;
		this.array = newArray;
		
	}
	
	
	
	

	public void add(E value) {
		if(size + 1 == array.length) {
			resize(array.length * 2);
		}
		siftUp(value);
	}
	
	private void siftUp(E value) {
		if(comparator != null) {
			siftUpComparator(value, comparator);
		}
		else {
			siftUpComparable(value);
		}
	}

	@SuppressWarnings("unchecked")
	private void siftUpComparator(E value, Comparator<? super E> comp) {
		int idx = size + 1;
		while(idx > 1) {
			int parent = idx >>> 1;
			Object parentVal = array[parent];
			
			if(comp.compare(value, (E) parentVal) >= 0) {
				break;
			}
			array[idx] = parentVal;
			idx = parent;
		}
		array[idx] = value;
		size++;
	}
	
	@SuppressWarnings("unchecked")
	private void siftUpComparable(E value) {
		Comparable<? super E> comp = (Comparable<? super E>) value;
		int idx = size + 1;
		
		while(idx > 1) {
			int parent = idx >>> 1;
			Object parentVal = array[parent];
			
			if(comp.compareTo((E)parentVal) >= 0) {
				break;
			}
			array[idx] = parentVal;
			idx = parent;
		}
		array[idx] = comp;
		size++;
	}

	
	@SuppressWarnings("unchecked")
	public E remove() {
		if(array[1] == null) {
			throw new NoSuchElementException();
		}	
		E result = (E) array[1]; 
		array[1] = null;
		siftDown(size, (E)array[size]);	
		
		return result;
	}
	
	private void siftDown(int idx, E target) {
		if(comparator != null) {
			siftDownComparator(idx, target, comparator);
		}
		else {
			siftDownComparable(idx, target);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void siftDownComparator(int idx, E target, Comparator<? super E> comp) {

		array[idx] = null;
		size--;			
		
		int parent = 1;	
		int child;
		
		while((child = (parent << 1)) <= size) {
			int right = child + 1;
			Object childVal = array[child];	 

			if(right <= size && comp.compare((E) childVal, (E) array[right]) > 0) {
				child = right;
				childVal = array[child];
			}
			
			if(comp.compare(target ,(E) childVal) <= 0){
				break;
			}
			array[parent] = childVal;
			parent = child;

		}
		
		array[parent] = target;

		if(array.length > DEFAULT_CAPACITY && size < array.length / 4) {
			resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
		}
		
	}
	

	@SuppressWarnings("unchecked")
	private void siftDownComparable(int idx, E value) {
		
		Comparable<? super E> comp = (Comparable<? super E>) value;
		
		array[idx] = null;
		size--;
		
		int parent = 1;
		int child;

		while((child = (parent << 1)) <= size) {
			
			int right = child + 1;
			
			Object c = array[child];
			
			if(right <= size && ((Comparable<? super E>)c).compareTo((E)array[right]) > 0) {
				child = right;
				c = array[child];
			}
			
			if(comp.compareTo((E) c) <= 0){
				break;
			}
			array[parent] = c;
			parent = child;
			
		}
		array[parent] = comp;
		
		if(array.length > DEFAULT_CAPACITY && size < array.length / 4) {
			resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
		}
		
	}
	
	public int size() {
		return this.size;
	}
	
	@SuppressWarnings("unchecked")
	public E peek() {
		if(array[1] == null) {
			throw new NoSuchElementException();
		}
		
		return (E)array[1];
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
}










