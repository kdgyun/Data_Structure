package Interface;

/**
 * 
 * 자바 Queue Interface입니다. <br>
 * Queue는 ArrayQueue, LinkedListQueue,
 * Deque, PriorityQueue 에 의해 구현됩니다.
 * 
 * @author st_lab
 * @param <E> the type of elements in this Queue
 *
 * @version 1.0
 * 
 */

public interface Queue<E> {
	
	/**
	 * 큐의 가장 마지막에 요소를 추가합니다.
	 * 
	 * @param e 큐에 추가할 요소 
	 * @return 큐에 요소가 정상적으로 추가되었을 경우 true를 반환 
	 */
	boolean offer(E e);
	
	/**
	 * 큐의 첫 번째 요소를 삭제하고 삭제 된 요소를 반환합니다.
	 * 
	 * @return 큐의 삭제 된 요소 반환 
	 */
	E poll();
	
	/**
	 * 큐의 첫 번째 요소를 반환합니다.
	 * 
	 * @return 큐의 첫 번째 요소 반환 
	 */
	E peek();
	
	
}
