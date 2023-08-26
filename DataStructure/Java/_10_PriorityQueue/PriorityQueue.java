package _10_PriorityQueue;



import java.util.Arrays;
import Interface.Queue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <E> the type of elements in this PriorityQueue
 * 
 * @author kdgyun (st-lab.tistory.com)
 * @version 1.1.002
 * @since 1.0.001
 * @see Queue
 * 
 */

public class PriorityQueue<E> implements Queue<E>, Cloneable, Iterable<E> {

	private final Comparator<? super E> comparator;
	private static final int DEFAULT_CAPACITY = 10;
	
    /**
     * @since 1.1.0
     * The maximum length of array to allocate.
     * 확장 가능한 용적의 한계값입니다. Java에서 인덱스는 int 정수로 인덱싱합니다.
     * 이론적으로는 Integer.MAX_VALUE(2^31 -1) 의 인덱스를 갖을 수 있지만, 
     * VM에 따라 배열 크기 제한이 상이하며, 제한 값을 초과할 경우 다음과 같은 에러가 발생합니다.
     * <p>
     * "java.lang.OutOfMemoryError: Requested array size exceeds VM limit"
     * <p>
     * 위와 같은 이유로 안정성을 위해 이론적으로 가능한 최댓값에 8을 뺀 값으로 지정하고 있습니다.
     */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

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
		if(capacity < 0) {
			throw new IllegalArgumentException();
		}
		this.array = new Object[capacity];
		this.size = 0;
		this.comparator = comparator;
	}
	
	



	private void resize(int newCapacity) {
		newCapacity = hugeRangeCheck(array.length ,newCapacity);
		Object[] newArray = new Object[newCapacity];
		
		for(int i = 1; i <= size; i++) {
			newArray[i] = array[i];
		}

		this.array = null;
		this.array = newArray;
		
	}
	
	/**
	 * resizing 할 때 overflow를 방지하기 위한 체크 함수입니다.
	 * 용적은 {@link #MAX_ARRAY_SIZE}를 초과 할 수 없습니다.
	 * 
	 * @since 1.1.0
	 * @param oldCapacity resize 하기 전의 용적
	 * @param newCapacity resize 하고자 하는 용적
	 * @return 최종 크기를 반환합니다.
	 */
	private int hugeRangeCheck(int oldCapacity, int newCapacity) {
        if (MAX_ARRAY_SIZE - size <= 0) { // fully elements in array
            throw new OutOfMemoryError("Required queue length too large");
        }
		// not overflow
		if(newCapacity >= 0) {
			if(newCapacity - MAX_ARRAY_SIZE <= 0) {
				return newCapacity;
			}
			return MAX_ARRAY_SIZE;
		}
		// newCapacity is overflow
		else {
	        int fiveFourthsSize = oldCapacity + (oldCapacity >>> 2);
	        if(fiveFourthsSize <= 0 || fiveFourthsSize >= MAX_ARRAY_SIZE) {
	        	return MAX_ARRAY_SIZE;
	        }
	        return fiveFourthsSize;
		}

	}
	
	

	public boolean offer(E value) {
		int oldCapacity = array.length;
		if(size + 1 == oldCapacity) {
			resize(oldCapacity + (oldCapacity >> 1));
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
		E target;
		if(size == 1) {
			target = null;
		}
		else {
			target = (E) array[size];
		}
		
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
		if(value == null) {
			return false;
		}
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
