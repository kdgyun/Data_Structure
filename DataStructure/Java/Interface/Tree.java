package Interface;

import java.util.Iterator;

/**
 * 
 * 자바 Tree Interface입니다. <br>
 * Tree는 BinarySearchTree에 의해 구현됩니다.
 * 
 * @author kdgyun
 * @param <E> the type of elements in this tree
 *
 * @version 1.0.003
 * @since 1.0.003
 * 
 */

public interface Tree<E> {
	/**
	 * 지정된 요소가 트리에 없는 경우 요소를 추가합니다. 
	 * 
	 * @param e 트리에 추가할 요소
	 * @return {@code true} 만약 트리에 지정 요소가 포함되지 않아 정상적으로 추가되었을 경우,
	 *         else, {@code false}
	 */
	boolean add(E e);
	
	/**
	 * 지정된 요소가 트리에 있는 경우 해당 요소를 삭제합니다.
	 * 
	 * @param o 트리에서 삭제할 특정 요소
	 * @return 만약 트리에 지정 요소가 포함되어 정상적으로 삭제되었을 경우 해당 요소를 반환
	 */
	E remove(Object o);
	
	/**
	 * 현재 트리에 특정 요소가 포함되어있는지 여부를 반환합니다.
	 * 
	 * @param o 트리에서 찾을 특정 요소
	 * @return {@code true} 트리에 지정 요소가 포함되어 있을 경우,
	 *         else, {@code false}
	 */
	boolean contains(Object o);
	
	/**
	 * 지정된 객체가 현재 트리과 같은지 여부를 반환합니다.
	 * 
	 * @param o 트리과 비교할 객체
	 * @return {@code true} 비교할 트리과 동일한 경우,
	 *         else, {@code false}
	 */
	boolean equals(Object o);
	
	/**
	 * 현재 트리가 빈 상태(요소가 없는 상태)인지 여부를 반환합니다.
	 * 
	 * @return {@code true} 트리가 비어있는 경우,
	 *         else, {@code false}
	 */
	boolean isEmpty();
	
	/**
	 * 현재 트리의 요소의 개수를 반환합니다.
	 * 
	 * @return 트리에 들어있는 요소의 개수를 반환
	 */
	int size();
	
	/**
	 * 트리의 모든 요소를 제거합니다.
	 * 이 작업을 수행하면 비어있는 트리가 됩니다.
	 */
	void clear();
	
	
	/**
	 * 트리의 모든 요소를 순회합니다.
	 */
	Iterator<E> iterator();
}
