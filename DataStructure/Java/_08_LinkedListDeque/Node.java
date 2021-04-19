package _08_LinkedListDeque;

class Node<E> {
	
	E data;  
	Node<E> next; 
	Node<E> prev; 

	Node(E data) {
		this.data = data;
		this.prev = null;
		this.next = null;
	}

}
