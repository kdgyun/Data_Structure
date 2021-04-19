package _11_HashSet;

class Node<E> {
	/*
	 * hash와 key값은 변하지 않으므로
	 * final로 선언해준다.
	 */
	final int hash;	
	final E key;
	Node<E> next;
	
	public Node(int hash, E key, Node<E> next) {
		this.hash = hash;
		this.key = key;
		this.next = next;
	}
	
}
