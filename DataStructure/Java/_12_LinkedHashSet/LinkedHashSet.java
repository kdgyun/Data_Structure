package _12_LinkedHashSet;

import java.util.Iterator;
import Interface.Set;

public class LinkedHashSet<E> implements Set<E> {

	private final static int DEFAULT_CAPACITY = 1 << 4;
	private static final float LOAD_FACTOR = 0.75f;
	
	Node<E>[] table;
	private int size;
	
	private Node<E> head;
	private Node<E> tail;
	
	@SuppressWarnings("unchecked")
	public LinkedHashSet() {
		table = (Node<E>[])new Node[DEFAULT_CAPACITY];
		size = 0;
		head = null;
		tail = null;
	}
	
	
	private static final int hash(Object key) {
		int hash;
		if(key == null) {
			return 0;
		}
		else {
			return (hash = key.hashCode()) ^ (hash >>> 16); 
		}
	}
	
	private void resize() {
		
		int oldCapacity = table.length;
		int newCapacity = oldCapacity << 1;
		
		@SuppressWarnings("unchecked")
		final Node<E>[] newTable = 	(Node<E>[]) new Node[newCapacity];
		
		for(int i = 0; i < oldCapacity; i++) {
			
			Node<E> data = table[i];
			if(data == null) {
				continue;
			}
			
			table[i] = null;
			if(data.next == null) {
				newTable[data.hash & (newCapacity - 1)] = data;
				continue;
			}
			
			Node<E> lowHead = null;
			Node<E> lowTail = null;
			Node<E> highHead = null;
			Node<E> highTail = null;
			
			
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
	
	
	private void linkedLastNode(Node<E> o) {
        Node<E> last = tail;
        tail = o;
        if (last == null)
            head = o;
        else {
            o.prevLink = last;
            last.nextLink = o;
        }
		
	}
	
	private void removeNode(Node<E> o) {
		Node<E> prevNode = o.prevLink;
		Node<E> nextNode = o.nextLink;
		
		/*
		 * need implementation to headdata remove
		 */
		
		prevNode.next = null;
		o.next = null;
		o.nextLink = null;
		o.prevLink = null;
		
		if(nextNode != null) {
			nextNode.prevLink = null;
			
			nextNode.prevLink = prevNode;
			prevNode.nextLink = nextNode;
		}
		else {
			tail = prevNode;
		}
		
		
		// 다음 노드가 없을 경우
		
	}
	
	@Override
	public boolean add(E e) {
		return add(hash(e), e) == null;
	}
	
	
	private E add(int hash, E key) {
		int idx = hash & (table.length -1);
		Node<E> newNode = new Node<E>(hash, key, null);
		if(table[idx] == null) {
			table[idx] = newNode;
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

			prev.next = newNode;
		}
		size++;
		
		linkedLastNode(newNode);	// table에 저장이 끝났으면 해당 노드를 linked로 연결해준다.

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
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
