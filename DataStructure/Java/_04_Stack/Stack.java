package _04_Stack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import Interface.StackInterface;

/**
 *
 * @param <E> the type of elements in this Stack
 * 
 * @author kdgyun (st-lab.tistory.com)
 * @version 1.1.002
 * @since 1.0.001
 * @see StackInterface
 * @see ArrayList (if you have implemented or imported my ArrayList)
 * 
 */

public class Stack<E> implements StackInterface<E>, Cloneable, Iterable<E> {
	
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

	private static final int DEFAULT_CAPACITY = 10;	
	private static final Object[] EMPTY_ARRAY = {};	
	
	private Object[] array;	
	private int size;	
	
	public Stack() {
		this.array = EMPTY_ARRAY;
		this.size = 0;
	}
	
	public Stack(int capacity) {
		if(capacity < 0) {
			throw new IllegalArgumentException();
		}
		if(capacity == 0) {
			array = EMPTY_ARRAY;
		} else {
			array = new Object[capacity];
		}
		this.size = 0;
	}
	
	
 
	private void resize() {
		if(Arrays.equals(array, EMPTY_ARRAY)) {
			array = new Object[DEFAULT_CAPACITY];
			return;
		}
		
		int arrayCapacity = array.length;	
		
		if(size == arrayCapacity) {
			// default growing 1.5x
			int newSize = hugeRangeCheck(arrayCapacity, arrayCapacity + arrayCapacity << 1);
			array = Arrays.copyOf(array, newSize);
			return;
		}
		
		if(size < (arrayCapacity / 2)) {
			int newCapacity = (arrayCapacity / 2);
			array = Arrays.copyOf(array, Math.max(DEFAULT_CAPACITY, newCapacity));
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
            throw new OutOfMemoryError("Required stack size too large");
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
	public E push(E item) {
		if (size == array.length) {
			resize();
		}
		array[size] = item;	
		size++;	
		
		return item;
	}
 
	@Override
	public E pop() {
		if(size == 0) {
			throw new EmptyStackException();
		}
		
		@SuppressWarnings("unchecked")
		E obj = (E) array[size - 1];
		array[size - 1] = null;
		size--;	
		resize();
		
		return obj;
	}
 
 
	@SuppressWarnings("unchecked")
	@Override
	public E peek() {
		if(size == 0) {
			throw new EmptyStackException();
		}
		return (E) array[size - 1];
	}
 
	@Override
	public int search(Object value) {

		if(value == null) {
			for(int idx = size - 1; idx >= 0; idx--) {
				if(array[idx] == null) {
					return size - idx;
				}
			}
		} else {
			for(int idx = size - 1; idx >= 0; idx--) {
				if(array[idx].equals(value)) {
					return size - idx;
				}
			}
		}

		return -1;
	}
 
	@Override
	public int size() {
		return size;
	}
 
	@Override
	public void clear() {
		for(int i = 0; i < size; i++) {
			array[i] = null;
		}
		size = 0;
		resize();
	}
	
	@Override
	public boolean empty() {
		return size == 0;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Stack<?> cloneStack = (Stack<?>) super.clone();

		cloneStack.array = new Object[size];
		System.arraycopy(array, 0, cloneStack.array, 0, size);
		return cloneStack;
	}
 
	
	public Object[] toArray() {
		return Arrays.copyOf(array, size);
	}
	
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(array, size, a.getClass());
 
        System.arraycopy(array, 0, a, 0, size);
 
        return a;
    }
    
    
	public void sort() {
		sort(null);
	}
 
	@SuppressWarnings("unchecked")
	public void sort(Comparator<? super E> c) {
		Arrays.sort((E[]) array, 0, size, c);
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
			Object[] data = Stack.this.array;
			now = cs + 1;
			return (E) data[cs];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
    
}




/* 
 * ArrayList가 구현되어 있을경우 아래 주석 구현부를 이용
 * Use annotated implementation if ArrayList is implemented
 */

/**
import _01_ArrayList.ArrayList;
public class Stack<E> extends ArrayList<E> implements StackInterface<E> {

    public Stack() {
    	super();
    }
    
    public Stack(int capacity) {
    	super(capacity);
    }

	@Override
	public E push(E item) {
		addLast(item);
		return item;
	}

	@Override
	public E pop() {
		int length = size();
		E obj = remove(length - 1);	
		
		return obj;
	}

	@Override
	public E peek() {
		
		int length = size();
        if (length == 0)
            throw new EmptyStackException();
        
		E obj = get(length - 1);
		
		return obj;
	}

	@Override
	public int search(Object value) {
		int idx = lastIndexOf(value);
		
		if(idx >= 0) {
			return size() - idx;
		}
		return -1;
	}

	@Override
	public boolean empty() {
		return size() == 0;
	}
}
*/