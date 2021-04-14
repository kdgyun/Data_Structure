package _11_HashSet;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import Interface.Set;

public class HashSet<E> implements Set<E>, Iterable<E> {

	private final static int DEFAULT_CAPACITY = 1 << 4;

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
		int newCapacity = oldCapacity << 1;	
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

	@Override
	public boolean add(E key) {
		return add(hash(key), key) == null;
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
	public boolean remove(Object key) {
		return remove(hash(key), key) != null;
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
	public boolean contains(Object key) {
		int idx = hash(key) & (table.length - 1);
		Node<E> temp = table[idx];

		while (temp != null) {
			if ( key == temp.key || (key != null && temp.key.equals(key))) {
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

		for (int i = 0; i < table.length; i++) {

			Node<E> node = table[i];

			while (node != null) {
				ret[index] = node.key;
				index++;
				node = node.next;
			}
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