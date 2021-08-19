package _05_ArrayQueue;

import Interface.Queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <E> the type of elements in this Queue
 * 
 * @author kdgyun (st-lab.tistory.com)
 * @version 1.1.002
 * @since 1.0.001
 * @see Queue
 * 
 */

public class ArrayQueue<E> implements Queue<E>, Cloneable, Iterable<E> {
	
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

	private static final int DEFAULT_CAPACITY = 64;

	private Object[] array;
	private int size;
	private int front;
	private int rear;

	public ArrayQueue() {
		this.array = new Object[DEFAULT_CAPACITY];
		this.size = 0;
		this.front = 0;
		this.rear = 0;
	}

	public ArrayQueue(int capacity) {
		if(capacity < 0) {
			throw new IllegalArgumentException();
		}

		this.array = new Object[capacity];
		this.size = 0;
		this.front = 0;
		this.rear = 0;
	}

	private void resize(int newCapacity) {
		
		int arrayCapacity = array.length;
		newCapacity = hugeRangeCheck(arrayCapacity, newCapacity);
		Object[] newArray = new Object[newCapacity];

		for (int i = 1, j = front + 1; i <= size; i++, j++) {
			newArray[i] = array[j % arrayCapacity];
		}

		this.array = null;
		this.array = newArray;
		this.front = 0;
		this.rear = this.size;

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

	
	@Override
	public boolean offer(E item) {
		int oldCapacity = array.length;
		if ((rear + 1) % oldCapacity == front) {
			resize(oldCapacity + (oldCapacity << 1));
		}

		rear = (rear + 1) % array.length;

		array[rear] = item;
		size++;

		return true;
	}

	@Override
	public E poll() {

		if (size == 0) { 
			return null;
		}
		front = (front + 1) % array.length;

		@SuppressWarnings("unchecked")
		E item = (E) array[front];
		array[front] = null; 
		size--; 

		if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
			resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
		}

		return item;
	}

	public E remove() {

		E item = poll();

		if (item == null) {
			throw new NoSuchElementException();
		}

		return item;
	}

	@Override
	public E peek() {
		if (size == 0) {
			return null;
		}

		@SuppressWarnings("unchecked")
		E item = (E) array[(front + 1) % array.length];
		return item;
	}

	public E element() {
		E item = peek();
		if (item == null) {
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
		int start = (front + 1) % array.length;

		for (int i = 0, idx = start; i < size; i++, idx = (idx + 1) % array.length) {
			if(value.equals(array[idx])) {
				return true;
			}
		}
		return false;
	}

	public void clear() {

		for (int i = 0; i < array.length; i++) {
			array[i] = null;
		}
		front = rear = size = 0;
	}

	public Object[] toArray() {
		return toArray(new Object[size]);
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		final T[] res;
		if (a.length < size) {

			if (front <= rear) {
				return (T[]) Arrays.copyOfRange(array, front + 1, rear + 1, a.getClass());
			}
			res = (T[]) Arrays.copyOfRange(array, 0, size, a.getClass());
			int rearlength = array.length - 1 - front; 
			if (rearlength > 0) {
				System.arraycopy(array, front + 1, res, 0, rearlength);
			}

			System.arraycopy(array, 0, res, rearlength, rear + 1);

			return res;
		}


		if (front <= rear) {
			System.arraycopy(array, front + 1, a, 0, size);
		}
		else {

			int rearlength = array.length - 1 - front; 
			if (rearlength > 0) {
				System.arraycopy(array, front + 1, a, 0, rearlength);
			}
			System.arraycopy(array, 0, a, rearlength, rear + 1);
		}
		return a;
	}

	@Override
	public Object clone() {

		try {

			@SuppressWarnings("unchecked")
			ArrayQueue<E> clone = (ArrayQueue<E>) super.clone();

			clone.array = Arrays.copyOf(array, array.length);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	public void sort() {
		sort(null);
	}

	@SuppressWarnings("unchecked")
	public void sort(Comparator<? super E> c) {
		Object[] res = toArray();
		Arrays.sort((E[]) res, 0, size, c);
		clear();
		System.arraycopy(res, 0, array, 1, res.length); 
		this.rear = this.size = res.length;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new Iter();
	}

	private class Iter implements Iterator<E> {

		private int count = 0;
		private int len = array.length;
		private int now = (front + 1) % len;

		@Override
		public boolean hasNext() {
			return count < size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			int cs = count;
			int ns = now;
			if (cs >= size) {
				throw new NoSuchElementException();
			}
			Object[] data = ArrayQueue.this.array;
			count = cs + 1;
			now = (ns + 1) % len;
			return (E) data[ns];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
