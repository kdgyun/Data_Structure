package _10_PriorityQueue;


/**
*
* @param <E> the type of elements in this PriorityQueue
* 
* @author kdgyun (st-lab.tistory.com)
* @version 1.0.1
* @see Queue
* 
*/

import java.util.Arrays;
import Interface.Queue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class PriorityQueue<E> implements Queue<E>, Cloneable, Iterable<E> {

	private final Comparator<? super E> comparator;
	private static final int DEFAULT_CAPACITY = 10;

	private int size;
	private Object[] array;

	public PriorityQueue() {
		this(null);
	}
	
	public PriorityQueue(Comparator<? super E> comparator) {
		this.array = new Object[DEFAULT_CAPACITY];
		this.size = 0;
		this.comparator = comparator;
	}

	public PriorityQueue(int capacity) {
		this(capacity, null);
	}
	
	public PriorityQueue(int capacity, Comparator<? super E> comparator) {
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
	
	

	public boolean offer(E value) {
		if(size + 1 == array.length) {
			resize(array.length * 2);
		}
		
		siftUp(size + 1, value);
		size++;	
		return true;
	}
	
	/**
	 * 상향 선별
	 * 
	 * @param idx		추가할 노드의 인덱스
	 * @param target	재배치 할 노드
	 */
	private void siftUp(int idx, E target) {
		if(comparator != null) {
			siftUpComparator(idx, target, comparator);
		}
		else {
			siftUpComparable(idx, target);
		}
	}

	// Comparator을 이용한 sift-up 
	@SuppressWarnings("unchecked")
	private void siftUpComparator(int idx, E target, Comparator<? super E> comp) {
		
		while(idx > 1) {
			int parent = idx >>> 1;
			Object parentVal = array[parent];
			
			if(comp.compare(target, (E) parentVal) >= 0) {
				break;
			}

			array[idx] = parentVal;
			idx = parent;
		}
		array[idx] = target;
	}
	
	
	// 삽입 할 객체의 Comparable을 이용한 sift-up
	@SuppressWarnings("unchecked")
	private void siftUpComparable(int idx, E target) {
		
		Comparable<? super E> comp = (Comparable<? super E>) target;
		
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
	}

	
	
	public E poll() {
		if(array[1] == null) {
			return null;
		}
		return remove();
	}
	
	@SuppressWarnings("unchecked")
	public E remove() {
		if(array[1] == null) {
			throw new NoSuchElementException();
		}	
		
		E result = (E) array[1];
		E target = (E) array[size];
		
		array[size] = null;
		size--;	
		siftDown(1, target);	
		
		return result;
	}
	
	
	/**
	 * 하향 선별
	 * 
	 * @param idx		삭제할 노드의 인덱스
	 * @param target	재배치 할 노드
	 */
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
		
		int parent = idx;	
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

		int len = array.length;
		if(len > DEFAULT_CAPACITY && size < (len >>> 2)) {
			resize(Math.max(DEFAULT_CAPACITY, len >>> 1));
		}
		
	}
	

	@SuppressWarnings("unchecked")
	private void siftDownComparable(int idx, E target) {
		
		Comparable<? super E> comp = (Comparable<? super E>) target;
		
		array[idx] = null;
		
		int parent = idx;
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
		
		int len = array.length;
		if(len > DEFAULT_CAPACITY && size < (len >>> 2)) {
			resize(Math.max(DEFAULT_CAPACITY, len >>> 1));
		}
		
	}
	
	public int size() {
		return this.size;
	}
	
	public E element() {
		return peek();
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
	
	public boolean contains(Object value) {
		for(Object v : this) {
			if(v.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		for(int i = 0; i < array.length; i++) {
			array[i] = null;
		}
		
		size = 0;
	}
	
	public Object[] toArray() {
		return toArray(new Object[size]);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if(a.length <= size) {
			return (T[]) Arrays.copyOfRange(array, 1, size + 1, a.getClass());
		}
		System.arraycopy(array, 1, a, 0, size);
		return a;
	}
	
	@Override
	public Object clone() {
		try {
			PriorityQueue<?> cloneHeap = (PriorityQueue<?>) super.clone();
		
			cloneHeap.array = new Object[size + 1];
		
			System.arraycopy(array, 0, cloneHeap.array, 0, size + 1);
			return cloneHeap;
		} 
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
		
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iter();
	}
	
	private class Iter implements Iterator<E> {

		private int now = 1;

		@Override
		public boolean hasNext() {
			return now <= size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			int cs = now;
			if (cs > size) {
				throw new NoSuchElementException();
			}
			Object[] data = PriorityQueue.this.array;
			now = cs + 1;
			return (E) data[cs];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
	
}
