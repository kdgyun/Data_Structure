package _04_Stack;

import java.util.EmptyStackException;

import Interface.StackInterface;
import _01_ArrayList.ArrayList;

/**
*
* @param <E> the type of elements in this Stack
* 
* @author kdgyun (st-lab.tistory.com)
* @version 1.0
* @see StackInterface
* @see ArrayList
* 
*/


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
