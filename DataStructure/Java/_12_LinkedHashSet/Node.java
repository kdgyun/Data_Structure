package _12_LinkedHashSet;

class Node<E> {

	final int hash;
	final E key;
	
	Node<E> next;	// for Separate Chaining
	
	Node<E> nextLink;	// for linked list(set)
	Node<E> prevLink;	// for linked list(set)
	
	public Node(int hash, E key, Node<E> next) {
		this.hash = hash;
		this.key = key;
		this.next = next;
		
		this.nextLink = null;
		this.prevLink = null;
		
	}
}
