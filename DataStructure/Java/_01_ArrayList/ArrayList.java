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
 * @version 1.0
 * @see List
 * 
 */

public class ArrayList<E> implements List<E>, Cloneable, Iterable<E> {

	private static final int DEFAULT_CAPACITY = 10;
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
			int new_capacity = array_capacity << 1;

			// copy
			array = Arrays.copyOf(array, new_capacity);
			return;
		}

		// if array is less than half full
		if (size < (array_capacity / 2)) {
			int new_capacity = array_capacity >>> 1;

			// copy
			array = Arrays.copyOf(array, Math.max(new_capacity, DEFAULT_CAPACITY));
			return;
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
