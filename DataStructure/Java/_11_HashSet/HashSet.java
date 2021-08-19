package _11_HashSet;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import Interface.Set;

/**
*
* @param <E> the type of elements in this HashSet
* 
* @author kdgyun (st-lab.tistory.com)
* @version 1.1.002
* @since 1.0.001
* @see Set
* 
*/

public class HashSet<E> implements Set<E>, Cloneable {

	private final static int DEFAULT_CAPACITY = 1 << 4;
	
    /**
     * @since 1.1.0
     * The maximum length of array to allocate.
     * 확장 가능한 용적의 한계값입니다. Java에서 인덱스는 int 정수로 인덱싱합니다.
     * 이론적으로는 Integer.MAX_VALUE(2^31 -1) 의 인덱스를 갖을 수 있지만, 
     * VM에 따라 배열 크기 제한이 상이하며, 제한 값을 초과할 경우 다음과 같은 에러가 발생합니다.
     * <p>
     * "java.lang.OutOfMemoryError: Requested array size exceeds VM limit"
     * <p>
     * 또한 HashSet은 2의 승수로 용적을 관리하기 때문에 HashSet 내부의 배열의 용적이
     * 최대로 가질 수 있는 길이는 2^30 입니다.
     */
	private static final int MAX_ARRAY_SIZE = 1 << 30;
	private final static float LOAD_FACTOR = 0.75f;

	Node<E>[] table;
	private int size;

	
	@SuppressWarnings("unchecked")
	public HashSet() {
		table = (Node<E>[]) new Node[DEFAULT_CAPACITY];	
		size = 0;
	}

	private static final int hash(Object key) {
		int hash;
		if (key == null) {
			return 0;
		} else {
			return (hash = key.hashCode()) ^ (hash >>> 16);
		}
	}

	@SuppressWarnings("unchecked")
	private void resize() {


		int oldCapacity = table.length;
		int newCapacity = hugeRangeCheck(oldCapacity, (oldCapacity << 1));	
		
		if(oldCapacity == newCapacity) {
			return;
		}
		final Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
		
		for(int i = 0; i < oldCapacity; i++) {
			
			Node<E> data = table[i];
			if(data == null) {
				continue;
			}
			
			table[i] = null;	// gc
			if(data.next == null) {
				newTable[data.hash & (newCapacity - 1)] = data;	
				continue;
			}

			Node<E> lowHead = null;
			Node<E> lowTail = null;

			Node<E> highHead = null;
			Node<E> highTail = null;
			
			/*
			 * 재배치 되는 노드는 원래 자리에 그대로 있거나
			 * 원래자리에 새로 늘어난 크기만큼의 자리에 배치 되거나 둘 중 하나다.
			 * 
			 * ex)
			 * oldCapacity = 4, newCapacity = 8
			 * 
			 * 만약 데이터가 index2에 위치했다고 가정했을 때,
			 * oldSet -> index_of_data = 2 
			 * 
			 * 사이즈가 두 배 늘어 새로운 set에 배치 될 경우 두 가지 중 하나임
			 * newSet -> index_of_data = 2 or 6 (2 + oldCapacity)
			 * 
			 */
			
			Node<E> next;
			do {
				next = data.next;
				
				if((data.hash & oldCapacity) == 0) {
					if(lowHead == null) {
						lowHead = data;
					}
					else {
						lowTail.next = data;
					}
					lowTail = data;
				}
				/*
				 * data.hash & oldCapacity != 0
				 * 
				 * oldCapacity = 4, newCapacity = 8 일 때,
				 * 재배치하려는 원소의 인덱스가 2라면
				 * 
				 * 2 또는 6에 위치하게 된다.
				 * 
				 * 이 때, 6에 위치하는 경우라는 의미는 재배치 하기 이전의 사이즈였던 4에 대하여
				 * n % 4 = 2 이었을 때, n % 8 = 6이 된다는 말,
				 * 즉, 8의 나머지에 대하여 4의 몫이 1이 된다는 말이다.
				 * 쉽게 말하자면 4로 나눈 몫이 홀 수 일 경우다.
				 * 
				 * 비트로 생각하면 4에 대응하는 비트가 1인 경우다.
				 * ex)
				 * hash = 45  ->  0010 1101(2)
				 * oldCap = 4  ->  0000 0100(2)
				 * newCap = 8  ->  0000 1000(2)
				 * 
				 * 재배치 이전 index = (hash & (oldCap - 1))
				 * 0010 1101(2) - 0000 0011(2) = 0000 0001(2) = index 1
				 * 
				 * 재배치 이후 index = (hash & (newCap - 1))
				 * 0010 1101(2) - 0000 0111(2) = 0000 0101(2) = index 5
				 * 
				 * 2진법으로 볼 때 새로운 위치에 배치되는 경우는 
				 * hash & oldCap = 0010 1101(2) & 0000 0100(2)
				 * = 0000 0100(2) 로 0이 아닌 경우다.
				 */
				else {
					if(highHead == null) {
						highHead = data;
					}
					else {
						highTail.next = data;
					}
					highTail = data;
				}
				data = next;
			} while(data != null);
			
			if(lowTail != null) {
				lowTail.next = null;
				newTable[i] = lowHead;
			}
			if(highTail != null) {
				highTail.next = null;
				newTable[i + oldCapacity] = highHead;
			}
		}
		
		table = newTable;
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

		// not overflow
		if(newCapacity >= 0) {
			if(newCapacity - MAX_ARRAY_SIZE <= 0) {
				return newCapacity;
			}
			return MAX_ARRAY_SIZE;
		}
		// newCapacity is overflow
		else {
	        return MAX_ARRAY_SIZE;
		}

	}

	@Override
	public boolean add(E e) {
		return add(hash(e), e) == null;
	}

	private E add(int hash, E key) {

	
		int idx = hash & (table.length - 1);

		if (table[idx] == null) {
			table[idx] = new Node<E>(hash, key, null);
		}
		else {

			Node<E> temp = table[idx];
			Node<E> prev = null;
			
			
			while (temp != null) {
				if ((temp.hash == hash) && (temp.key == key || temp.key.equals(key))) {
					return key;
				}
				prev = temp;
				temp = temp.next;
			}

			prev.next = new Node<E>(hash, key, null);
		}
		size++;

		// 데이터의 개수가 현재 table 용적의 75%을 넘어가는 경우 용적을 늘려준다.  
		if (size >= LOAD_FACTOR * table.length) {
			resize();
		}
		return null;
	}
	
	@Override
	public boolean remove(Object o) {
		return remove(hash(o), o) != null;
	}
	
	private Object remove(int hash, Object key) {

		int idx = hash & (table.length - 1);

		Node<E> node = table[idx];
		Node<E> removedNode = null;
		Node<E> prev = null;

		if (node == null) {
			return null;
		}

		while (node != null) {
			// 같은 노드를 찾았다면
			if(node.hash == hash && (node.key == key || node.key.equals(key))) {
				
				removedNode = node; //삭제되는 노드를 반환하기 위해 담아둔다.
				
				// 해당노드의 이전 노드가 존재하지 않는 경우 (= head노드인 경우)
				if (prev == null) {
					table[idx] = node.next;
					node = null;
				} 
				// 그 외엔 이전 노드의 next를 삭제할 노드의 다음노드와 연결해준다.
				else {
					prev.next = node.next;
					node = null;
				}
				size--;
				break;
			}
			prev = node;
			node = node.next;
		}

		return removedNode;
	}

	@Override
	public void clear() {
		if (table != null && size > 0) {
			for (int i = 0; i < table.length; i++) {
				table[i] = null;
			}
			size = 0;
		}
	}

	@Override
	public boolean contains(Object o) {
		if(o == null) {
			return false;
		}
		int idx = hash(o) & (table.length - 1);
		Node<E> temp = table[idx];

		while (temp != null) {
			if ( o == temp.key || (o != null && temp.key.equals(o))) {
				return true;
			}
			temp = temp.next;
		}

		return false;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}


	@Override
	public int size() {
		return size;
	}

	public Object[] toArray() {

		if (table == null) {
			return null;
		}
		Object[] ret = new Object[size];
		int index = 0;

		for(Object v : this) {
			ret[index++] = v;
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {

		Object[] copy = toArray();
		if (a.length < size)
			return (T[]) Arrays.copyOf(copy, size, a.getClass());

		System.arraycopy(copy, 0, a, 0, size);

		return a;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(o == this) {
			return true;
		}
		if(!(o instanceof Set)) {
			return false;
		}
		
		Set<?> oSet = (Set<?>) o;

		try {
			
			if(oSet.size() != size) {
				return false;
			}
			
			for(Object v : oSet) {
				if(!contains(v)) {
					return false;
				}
			}
			
		} catch(ClassCastException e) {
			return false;
		}
		return true;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		HashSet<E> newSet;
		try {
			newSet = (HashSet<E>)super.clone();
			newSet.table = null;
			newSet.size = 0;
		}
		catch(CloneNotSupportedException e) {
			throw new Error(e);
		}
		newSet.PutSet(this);
		return newSet;
	}
	
	
	@SuppressWarnings("unchecked")
	final void PutSet(HashSet<? extends E> set) {
		int sSize = set.size();
		
		if(sSize <= 0) { 
			return;
		}
		
		if(table == null) {
			table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
		}
		for (E key : set) {
			add(hash(key), key);
		}
		
	}

	@Override
	public Iterator<E> iterator() {
		return new Iter();
	}

	private class Iter implements Iterator<E> {

		private int count = 0;
		private int nowIndex;
		private Node<E> next;

		Iter() {
			Node<E>[] tempTable = table;
			nowIndex = 0;
			if (tempTable != null && size > 0) {
				while (nowIndex < tempTable.length && (next = tempTable[nowIndex++]) == null)
					;
			}
		}

		@Override
		public boolean hasNext() {
			return count < size;
		}

		@Override
		public E next() {
			Node<E>[] tempTable;
			Node<E> now = next;
			if (count > size) {
				throw new NoSuchElementException();
			}
			if (now == null) {
				throw new NoSuchElementException();
			}
			next = (now).next;
			count++;
			if (next == null && (tempTable = table) != null) {
				while (nowIndex < tempTable.length && (next = tempTable[nowIndex++]) == null)
					;
			}
			return now.key;
		}

	}
	

}

