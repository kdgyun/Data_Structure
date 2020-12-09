package _06_LinkedListQueue;

class Node<E> {
	
	E data;
	Node<E> next;
	
	Node(E data) {
		this.data = data;
		this.next = null;
	}
}