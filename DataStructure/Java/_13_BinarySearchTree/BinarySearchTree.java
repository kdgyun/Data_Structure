package _13_BinarySearchTree;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import Interface.Tree;
/**
*
* @param <E> the type of elements in this tree
* 
* @author kdgyun (st-lab.tistory.com)
* @version 1.1.003
* @since 1.0.003
* 
*/

public class BinarySearchTree<E> implements Iterable<E>, Tree<E> {
	
	private Node<E> root;
	private int size;
	
	private final Comparator<? super E> comparator;
	StringBuffer sb;	// 순회 결과를 String으로 반환하기 위한 것입니다.
	
	public BinarySearchTree() {
		this(null);
	}
	
	
	public BinarySearchTree(Comparator<? super E> comparator) {
		this.comparator = comparator;
		this.root = null;
		this.size = 0;
	}
	
	@Override
	public boolean add(E value) {
		if(comparator == null) {
			return addUsingComparable(value) == null;
		}
		return addUsingComparator(value, comparator) == null;
	}
	
	private E addUsingComparable(E value) {
		
		Node<E> current = root;
		
		if(current == null) {
			root = new Node<E>(value);
			size++;
			return null;
		}
		
		Node<E> currentParent;
		
		@SuppressWarnings("unchecked")
		Comparable<? super E> compValue = (Comparable<? super E>) value;
		
		int compResult;
		
		do {
			currentParent = current;
			compResult = compValue.compareTo(current.value);

			if(compResult < 0) {
				current = current.left;
			} else if(compResult > 0) {
				current = current.right;
			}
			else {
				return value;
			}
			
		} while(current != null);
		
		
		Node<E> newNode = new Node<E>(value, currentParent);
		
		if(compResult < 0) {
			currentParent.left = newNode;
		}
		else {
			currentParent.right = newNode;
		}
		
		size++;
		return null;
	}
	
	private E addUsingComparator(E value, Comparator<? super E> comp) {
		
		Node<E> current = root;
		if(current == null) {
			root = new Node<E>(value, null);
			size++;
			return null;
		}
		
		Node<E> currentParent;
		int compResult;
		do {
			currentParent = current;
			compResult = comp.compare(value, current.value);
			if(compResult < 0) {
				current = current.left;
			}
			else if(compResult > 0) {
				current = current.right;
			}
			else {
				return value;
			}
		} while(current != null);
		
		Node<E> newNode = new Node<E>(value, currentParent);
		if(compResult < 0) {
			currentParent.left = newNode;
		}
		else {
			currentParent.right = newNode;
		}
		size++;
		return null;
	}

	@Override
	public E remove(Object o) {

		if (root == null) {
			return null;
		}
		if (comparator == null) {
			return removeUsingComparable(o);
		} else {
			return removeUsingComparator(o, comparator);
		}
	}


	private E removeUsingComparable(Object value) {
		@SuppressWarnings("unchecked")
		E oldVal = (E) value;
		Node<E> parent = null, current = root;
		boolean hasLeft = false;

		if (root == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		Comparable<? super E> compValue = (Comparable<? super E>) value;

		do {
			int resComp = compValue.compareTo(current.value);
			if (resComp == 0) {
				break;
			}

			parent = current;
			if (resComp < 0) {
				hasLeft = true;
				current = current.left;
			} else {
				hasLeft = false;
				current = current.right;
			}
		} while (current != null);

		if (current == null) {
			return null;
		}

		if (parent == null) {
			deleteNode(current);
			size--;
			return oldVal;
		}

		if (hasLeft) {
			parent.left = deleteNode(current);
			if (parent.left != null) {
				parent.left.parent = parent;
			}
		} else {
			parent.right = deleteNode(current);
			if (parent.right != null) {
				parent.right.parent = parent;
			}
		}
		size--;
		return oldVal;
	}

	private E removeUsingComparator(Object value, Comparator<? super E> comp) {
		
		@SuppressWarnings("unchecked")
		E oldVal = (E) value;
		Node<E> parent = null, current = root;
		boolean hasLeft = false;

		if (root == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		E compValue = (E) value;

		do {
			int resComp = comp.compare(compValue, current.value);
			if (resComp == 0) {
				break;
			}

			parent = current;
			if (resComp < 0) {
				hasLeft = true;
				current = current.left;
			} else {
				hasLeft = false;
				current = current.right;
			}
		} while (current != null);

		if (current == null) {
			return null;
		}

		if (parent == null) {
			deleteNode(current);
			size--;
			return oldVal;
		}

		if (hasLeft) {
			parent.left = deleteNode(current);
			if (parent.left != null) {
				parent.left.parent = parent;
			}
		} else {
			parent.right = deleteNode(current);
			if (parent.right != null) {
				parent.right.parent = parent;
			}
		}
		size--;
		return oldVal;
	}


	private Node<E> deleteNode(Node<E> node) {

		if (node != null) {
			if (node.left == null && node.right == null) {
				if (node == root) {
					root = null;
				} else {
					node = null;
				}
				return null;
			}

			if (node.left != null && node.right != null) {
				Node<E> replacement = getSuccessorAndUnlink(node);
				node.value = replacement.value;
			}
			else if (node.left != null) {
				if (node == root) {
					node = node.left;
					root = node;
					root.parent = null;
				} else {
					node = node.left;
				}
			}
			else {
				if (node == root) {
					node = node.right;
					root = node;
					root.parent = null;
				} else {
					node = node.right;
				}
			}
		}

		return node;
	}

	private Node<E> getSuccessorAndUnlink(Node<E> node) {

		Node<E> currentParent = node;
		Node<E> current = node.right;

		/**
		 * 처음 탐색하게되는 오른쪽 자식 노드(current)에서 
		 * current의 왼쪽 자식이 없다는 것은 current노드, 
		 * 즉 오른쪽 첫 자식노드가 대체되는 노드가 된다는 것이 됩니다.
		 * 
		 * 그렇기 때문에 대체해야하는 노드는 삭제되는 노드의 오른쪽 자식이 되며 
		 * 이에 대체되는 노드 자리(currentParent)의 오른쪽 자식은
		 * current의 오른쪽 자식을 가리키고, currentParent는 이후 
		 * current의 값이 반환되고, 상위 메소드에서 currentParent자리에 
		 * 값이 대체되게 됩니다.
		 */
		if (current.left == null) {
			currentParent.right = current.right;
			if (currentParent.right != null) {
				currentParent.right.parent = currentParent;
			}
			current.right = null;
			return current;

		}

		while (current.left != null) {
			currentParent = current;
			current = current.left;
		}

		/*
		 * 만약 후계자가 될 노드(가장 작은 노드)의 오른쪽 노드가 존재한다면
		 * currentParent의 왼쪽 자식노드는 오른쪽 자식노드와 연결되어야 합니다.
		 * 
		 * 만약 current.right = null 이라면 
		 * 후계자가 될 노드의 자식노드는 존재하지 않으므로 자연스럽게 
		 * 후계자 노드의 부모노드는 후계자가 다른노드로 대체되러 가기 때문에 
		 * 후계자의 부모노드의 왼쪽자식노드는 자연스럽게 null을 가리키게 됩니다.
		 */
		currentParent.left = current.right;
		if (currentParent.left != null) {
			currentParent.left.parent = currentParent;
		}

		current.right = null;
		return current;
	}
	
	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		if (comparator == null) {
			return containsUsingComparable(o);
		}
		return containsUsingComparator(o, comparator);
	}

	private boolean containsUsingComparable(Object o) {
		
		@SuppressWarnings("unchecked")
		Comparable<? super E> value = (Comparable<? super E>) o;
		
		Node<E> node = root;
		while (node != null) {
			int res = value.compareTo(node.value);
			if (res < 0) {
				node = node.left;
			} else if (res > 0) {
				node = node.right;
			} 
			else {
				return true;
			}
		}
		return false;
	}

	private boolean containsUsingComparator(Object o, Comparator<? super E> comparator) {
		@SuppressWarnings("unchecked")
		E value = (E) o;

		Node<E> node = root;
		while (node != null) {
			int res = comparator.compare(value, node.value);
			if (res < 0) {
				node = node.left;
			} else if (res > 0) {
				node = node.right;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		size = 0;
		root = null;
	}
	
	
	public String toString() {
		return inOrder();
	}
	
	public String preOrder() {
		sb = new StringBuffer();
		if(this.root == null) {
			return "";
		}
		preOrder(this.root);
		String ret = sb.toString();
		sb = null;
		return ret;
	}
	
	private void preOrder(Node<E> o) {
		if(o != null) {
			sb.append(o.value + " ");
			preOrder(o.left);
			preOrder(o.right);
		}
	}
	
	public String inOrder() {
		sb = new StringBuffer();
		if(this.root == null) {
			return "";
		}
		inOrder(this.root);
		String ret = sb.toString();
		sb = null;
		return ret;
	}
	
	private void inOrder(Node<E> o) {
		if(o != null) {
			inOrder(o.left);
			sb.append(o.value + " ");
			inOrder(o.right);
		}
	}
	
	public String postOrder() {
		sb = new StringBuffer();
		if(this.root == null) {
			return "";
		}
		postOrder(this.root);
		String ret = sb.toString();
		sb = null;
		return ret;
	}
	
	private void postOrder(Node<E> o) {
		if(o != null) {
			postOrder(o.left);
			postOrder(o.right);
			sb.append(o.value + " ");
		}
	}
	
	
	public Iterator<E> iterator() {
		return new Iter(getFirstNode());
	}
	private class Iter implements Iterator<E> {

		private Node<E> last;
		private Node<E> next;

		
		Iter(Node<E> first) {
			last = null;
			next = first;
		}

        public final boolean hasNext() {
            return next != null;
        }

        public E next() {
        	Node<E> node = next;
        	if (node == null) {
        		throw new NoSuchElementException();
        	}
        	next = getSuccessor(node);
        	last = node;
        	return node.value;
        }

		public void remove() {
			if (last == null) {
				throw new IllegalStateException();
			}
            if (last.left != null && last.right != null)
                next = last;
            removeNode(last);
			last = null;
		}

	}
	
	final static <E> Node<E> getSuccessor(Node<E> p) {
		if (p == null) {
			return null;
		}
		if(p.right != null) {
			Node<E> node = p.right;
			while(node.left != null) {
				node = node.left;
			}
			return node;
		} else {
			Node<E> parent = p.parent;
			Node<E> current = p;
			while(parent != null && current == parent.right) {
				current = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}
	
	final Node<E> getFirstNode() {
		Node<E> node = root;
		if(node != null) {
			while(node.left != null) {
				node = node.left;
			}
		}
		return node;
	}
	
	final Node<E> getLastNode() {
		Node<E> node = root;
		if(node != null) {
			while(node.right != null) {
				node = node.right;
			}
		}
		return node;
	}
	
	final private E removeNode(Node<E> o) {
		return remove(o.value);
	}
}
