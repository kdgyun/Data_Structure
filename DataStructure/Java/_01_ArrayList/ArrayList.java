package _01_ArrayList;

import Interface.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <E> the type of elements in this list
 * 
 * @author kdgyun (st-lab.tistory.com)
 * @version 1.1.0
 * @since 1.0.0
 * @see List
 * 
 */

public class ArrayList<E> implements List<E>, Cloneable, Iterable<E> {

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

	private static final Object[] EMPTY_ARRAY = {};
	private int size;

	Object[] array;

	public ArrayList() {
		this.array = EMPTY_ARRAY;
		this.size = 0;

	}

	public ArrayList(int capacity) {
		array = new Object[capacity];
		this.size = 0;

	}


	private void resize() {
		int array_capacity = array.length;

		// array's capacity = 0
		if (Arrays.equals(array, EMPTY_ARRAY)) {
			array = new Object[DEFAULT_CAPACITY];
			return;
		}

		// if array is full
		if (size == array_capacity) {
			// default growing 1.5x
			int new_capacity = hugeRangeCheck(array_capacity, array_capacity + (array_capacity << 1));

			// copy
			array = Arrays.copyOf(array, new_capacity);
			return;
		}

		// if array is less than half full
		if (size < (array_capacity >>> 1)) {
			int new_capacity = array_capacity >>> 1;

			// copy
			array = Arrays.copyOf(array, Math.max(new_capacity, DEFAULT_CAPACITY));
			return;
		}
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
            throw new OutOfMemoryError("Required array length too large");
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
	public boolean add(E value) {
		addLast(value);
		return true;
	}

	public void addLast(E value) {
		if (size == array.length) {
			resize();
		}
		array[size] = value;
		size++;

	}

	public void addFirst(E value) {
		add(0, value);
	}

	@Override
	public void add(int index, E value) {
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index == size) {
			addLast(value);
			return;
		}

		if (size == array.length) {
			resize();
		}

		for (int i = size; i > index; i--) {
			array[i] = array[i - 1];
		}
		array[index] = value;
		size++;

	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return (E) array[index];
	}

	@Override
	public void set(int index, E value) {
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException();
		} else {
			array[index] = value;
		}
	}

	@Override
	public int indexOf(Object value) {
		for (int i = 0; i < size; i++) {
			if (array[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}
	
	public int lastIndexOf(Object value) {
		for(int i = size - 1; i >= 0; i--) {
			if(array[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean contains(Object value) {
		return indexOf(value) >= 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove(int index) {

		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException();
		}

		E element = (E) array[index];
		array[index] = null;
		for (int i = index; i < size; i++) {
			array[i] = array[i + 1];
			array[i + 1] = null;
		}
		size--;
		resize();
		return element;
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index == -1) {
			return false;
		}

		remove(index);
		return true;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			array[i] = null;
		}
		size = 0;
		resize();
	}

	@Override
	public Object clone() {

		try {
			ArrayList<?> cloneList = (ArrayList<?>) super.clone();
			cloneList.array = new Object[size];

			System.arraycopy(array, 0, cloneList.array, 0, size);

			return cloneList;
			
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}

	}

	public void sort() {
		sort(null);
	}

	@SuppressWarnings("unchecked")
	public void sort(Comparator<? super E> c) {
		Arrays.sort((E[]) array, 0, size, c);
	}

	public Object[] toArray() {
		return Arrays.copyOf(array, size);
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			return (T[]) Arrays.copyOf(array, size, a.getClass());

		/**
		 * arraycopry param 
		 * 1) original array 
		 * 2) start potision in original array 
		 * 3) array to copy 
		 * 4) start position in array to copy 
		 * 5) number of elements to copy
		 */
		System.arraycopy(array, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;

	}

	@Override
	public Iterator<E> iterator() {
		return new Iter();
	}

	private class Iter implements Iterator<E> {

		private int now = 0;

		@Override
		public boolean hasNext() {
			return now < size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			int cs = now;
			if (cs >= size) {
				throw new NoSuchElementException();
			}
			Object[] data = ArrayList.this.array;
			now = cs + 1;
			return (E) data[cs];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
