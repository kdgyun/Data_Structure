package _04_Stack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import Interface.StackInterface;
import _01_ArrayList.ArrayList;

/**
*
* @param <E> the type of elements in this Stack
* 
* @author kdgyun (st-lab.tistory.com)
* @version 1.0
* @see StackInterface
* @see ArrayList (if you have implemented or imported my ArrayList)
* 
*/

public class Stack<E> implements StackInterface<E>, Cloneable, Iterable<E> {
	 
	private static final int DEFAULT_CAPACITY = 10;	
	private static final Object[] EMPTY_ARRAY = {};	
	
	private Object[] array;	
	private int size;	
	
	public Stack() {
		this.array = EMPTY_ARRAY;
		this.size = 0;
	}
	
	public Stack(int capacity) {
		this.array = new Object[capacity];
		this.size = 0;
	}
	
	
 
	private void resize() {
		if(Arrays.equals(array, EMPTY_ARRAY)) {
			array = new Object[DEFAULT_CAPACITY];
			return;
		}
		
		int arrayCapacity = array.length;	
		
		if(size == arrayCapacity) {
			int newSize = arrayCapacity * 2;
			array = Arrays.copyOf(array, newSize);
			return;
		}
		
		if(size < (arrayCapacity / 2)) {
			int newCapacity = (arrayCapacity / 2);
			array = Arrays.copyOf(array, Math.max(DEFAULT_CAPACITY, newCapacity));
			return;
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

		for(int idx = size - 1; idx >= 0; idx--) {
			if(array[idx].equals(value)) {
				return size - idx;
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