package Interface;

import java.util.NoSuchElementException;

/**
 * 
 * 자바 Queue Interface입니다. <br>
 * Queue는 ArrayQueue, LinkedListQueue,
 * PriorityQueue 에 의해 구현됩니다.
 * 
 * @author st_lab
 * @param <E> the type of elements in this Queue
 *
 * @version 1.0.001
 * @since 1.0.001
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
	
    /**
     * 큐의 첫 번째(head) 요소를 삭제하고 삭제된 요소를 반환합니다.
     * 만약 큐거 빈 상태(empty)일 경우 예외를 던집니다. 
     *
     * @return 큐의 첫 번째 요소를 반환
     * @throws NoSuchElementException 큐거 비었을 경우
     */
    E remove();
    
    /**
     * 큐의 첫 번째(head) 요소를 반환하되 삭제하진 않습니다.
     * 만약 큐가 비었을 경우 예외를 던집니다.
     *
     * @return 큐의 첫 번째 요소를 반환
     * @throws NoSuchElementException 큐가 비었을 경우
     */
    E element();
	
}
