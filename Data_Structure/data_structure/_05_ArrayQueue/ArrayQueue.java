package _05_ArrayQueue;

import Interface.Queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *
 * @param <E> the type of elements in this Queue
 * 
 * @author kdgyun (st-lab.tistory.com)
 * @version 1.0
 * @see Queue
 * 
 */

public class ArrayQueue<E> implements Queue<E>, Cloneable {

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
		this.array = new Object[capacity];
		this.size = 0;
		this.front = 0;
		this.rear = 0;
	}

	private void resize(int newCapacity) {

		int arrayCapacity = array.length;

		Object[] newArray = new Object[newCapacity];

		for (int i = 1, j = front + 1; i <= size; i++, j++) {
			newArray[i] = array[j % arrayCapacity];
		}

		this.array = null;
		this.array = newArray;
		this.front = 0;
		this.rear = this.size;

	}

	@Override
	public boolean offer(E item) {

		if ((rear + 1) % array.length == front) {
			resize(array.length * 2);
		}

		rear = (rear + 1) % array.length;

		array[rear] = item;
		size++;

		return true;
	}

	@Override
	public E poll() {

		if (size == 0) { // 삭제할 요소가 없을 경우 null 반환
			return null;
		}

		front = (front + 1) % array.length; // front 를 한 칸 옮긴다.

		@SuppressWarnings("unchecked")
		E item = (E) array[front]; // 반환할 데이터 임시 저장

		array[front] = null; // 해당 front의 데이터 삭제
		size--; // 사이즈 감소

		// 용적이 최소 크기(64)보다 크고 요소 개수가 1/4 미만일 경우
		if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {

			// 아무리 작아도 최소용적 미만으로 줄이지는 않도록 한다.
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

		int start = (front + 1) % array.length;

		for (int i = 0, idx = start; i < size; i++, idx = (idx + 1) % array.length) {
			if (array[idx].equals(value)) {
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
}