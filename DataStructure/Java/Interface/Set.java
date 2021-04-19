package Interface;

/**
 * 
 * 자바 Set Interface입니다. <br>
 * Set은 HashSet, LinkedHashSet, 
 * TreeSet에 의해 구현됩니다.
 * 
 * @author st_lab
 * @param <E> the type of elements in this Set
 *
 * @version 1.0
 * 
 */

public interface Set<E> {

	/**
	 * 지정된 요소가 Set에 없는 경우 요소를 추가합니다. 
	 * 
	 * @param e 집합에 추가할 요소
	 * @return {@code true} 만약 Set에 지정 요소가 포함되지 않아 정상적으로 추가되었을 경우,
	 *         else, {@code false}
	 */
	boolean add(E e);
	
	/**
	 * 지정된 요소가 Set에 있는 경우 해당 요소를 삭제합니다.
	 * 
	 * @param o 집합에서 삭제할 특정 요소
	 * @return {@code true} 만약 Set에 지정 요소가 포함되어 정상적으로 삭제되었을 경우,
	 *         else, {@code false}
	 */
	boolean remove(Object o);
	
	/**
	 * 현재 집합에 특정 요소가 포함되어있는지 여부를 반환합니다.
	 * 
	 * @param o 집합에서 찾을 특정 요소
	 * @return {@code true} Set에 지정 요소가 포함되어 있을 경우,
	 *         else, {@code false}
	 */
	boolean contains(Object o);
	
	/**
	 * 지정된 객체가 현재 집합과 같은지 여부를 반환합니다.
	 * 
	 * @param o 집합과 비교할 객체
	 * @return {@code true} 비교할 집합과 동일한 경우,
	 *         else, {@code false}
	 */
	boolean equals(Object o);
	
	/**
	 * 현재 집합이 빈 상태(요소가 없는 상태)인지 여부를 반환합니다.
	 * 
	 * @return {@code true} 집합이 비어있는 경우,
	 *         else, {@code false}
	 */
	boolean isEmpty();
	
	/**
	 * 현재 집합의 요소의 개수를 반환합니다. 만약 들어있는 요소가
	 * {@code Integer.MAX_VALUE}를 넘을 경우 {@code Integer.MAX_VALUE}를 반환합니다.
	 * 
	 * @return 집합에 들어있는 요소의 개수를 반환
	 */
	int size();
	
	/**
	 * 집합의 모든 요소를 제거합니다.
	 * 이 작업을 수행하면 비어있는 집합이 됩니다.
	 */
	void clear();
}
