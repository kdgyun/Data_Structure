package Interface;

/**
 * 
 * 자바 Deque Interface입니다. <br>
 * Deque는 ArrayDeque, LinkedListDeque 에 의해 구현됩니다.
 * 
 * @author st_lab
 * @param <E> the type of elements in this Deque
 *
 * @version 1.0
 * 
 */

public interface Deque<E> extends Queue<E> {


    /**
     * 덱의 전방(head)에 요소를 추가합니다.
     *
     * @param e 덱에 추가할 요소
     * @return {@code true} 덱에 정상적으로 요소가 추가 될 경우, else
     *         {@code false}
	 */
    boolean offerFirst(E e);

    /**
     * 덱의 가장 마지막(tail)에 요소를 추가합니다.
     *
     * @param e 덱에 추가할 요소
     * @return {@code true} 덱에 정상적으로 요소가 추가 될 경우, else
     *         {@code false}
	 */
    boolean offerLast(E e);

    /**
     * 덱의 첫 번째(head) 요소를 삭제하고 삭제된 요소를 반환합니다.
     * 만약 덱이 빈 상태(empty)일 경우 예외를 던집니다. 
     *
     * @return 덱의 첫 번째 요소를 반환
     * @throws NoSuchElementException 덱이 비었을 경우
     */
    E removeFirst();

    /**
     * 덱의 마지막(tail) 요소를 삭제하고 삭제된 요소를 반환합니다.
     * 만약 덱이 빈 상태(empty)일 경우 예외를 던집니다. 
     *
     * @return 덱의 마지막 요소를 반환
     * @throws NoSuchElementException 덱이 비었을 경우
     */
    E removeLast();

    /**
     * 덱의 첫 번째(head) 요소를 삭제하고 삭제된 요소를 반환합니다.
     * 만약 덱이 빈 상태(empty)일 경우 {@code null}을 반환합니다. 
     *
     * @return 덱의 첫 번쨰 요소 또는 덱이 비었을(empty) 경우 {@code null}을 반환
     */
    E pollFirst();

    /**
     * 덱의 마지막(tail) 요소를 삭제하고 삭제된 요소를 반환합니다.
     * 만약 덱이 빈 상태(empty)일 경우 {@code null}을 반환합니다. 
     *
     * @return 덱의 마지막 요소 또는 덱이 비었을(empty) 경우 {@code null}을 반환
     */
    E pollLast();

    /**
     * 덱의 첫 번째(head) 요소를 반환하되 삭제하진 않습니다.
     * 만약 덱이 비었을 경우 예외를 던집니다.
     *
     * @return 덱의 첫 번째 요소를 반환
     * @throws NoSuchElementException 덱이 비었을 경우
     */
    E getFirst();

    /**
     * 덱의 마지막(tail) 요소를 반환하되 삭제하진 않습니다.
     * 만약 덱이 비었을 경우 예외를 던집니다.
     *
     * @return 덱의 마지막 요소를 반환
     * @throws NoSuchElementException 덱이 비었을 경우
     */
    E getLast();

    /**
     * 덱의 첫 번째(head) 요소를 반환하되 삭제하진 않습니다.
     * 만약 덱이 비었을 경우 {@code null}을 반환합니다.
     *
     * @return 덱의 첫 번째 요소 또는 덱이 비었을 경우 {@code null}을 반환
     */
    E peekFirst();

    /**
     * 덱의 마지막(tail) 요소를 반환하되 삭제하진 않습니다.
     * 만약 덱이 비었을 경우 {@code null}을 반환합니다.
     *
     * @return 덱의 마지막 요소 또는 덱이 비었을 경우 {@code null}을 반환
     */
    E peekLast();
	
    
    
    
    // **** [Queue Methods] ****
    
    /**
     * 큐의 마지막에 요소를 추가하는 것이며 큐를 확장한 덱의 경우
     * 덱의 가장 마지막(tail)에 요소를 추가하는 것과 같습니다.
     * 
     * 이 메소드는 {@link #offerLast}과 같습니다.
     *
     * @param e 덱에 추가할 요소
     * @return {@code true} 덱에 정상적으로 요소가 추가 될 경우, else
     *         {@code false}
     *         
	 */
    boolean offer(E e);

    /**
     * 큐의 첫 번째(head) 요소를 삭제하고 삭제된 요소를 반환합니다.
     * 큐를 확장한 덱의 경우 덱의 첫 번째 요소를 삭제하고 이를 반환하는 것과 같습니다.
     * 
     * 이 메소드는 {@link #removeFirst()} 과 같습니다.
     * 
     * 만약 덱이 빈 상태(empty)일 경우 예외를 던집니다. 
     *
     * @return 덱의 첫 번째 요소를 반환
     * @throws NoSuchElementException 덱이 비었을 경우
     */
    E remove();

    /**
     * 큐의 첫 번째(head) 요소를 삭제하고 삭제된 요소를 반환합니다.
     * 큐를 확장한 덱의 경우 덱의 첫 번째 요소를 삭제하고 이를 반환하는 것과 같습니다.
     * 
     * 이 메소드는 {@link #pollFirst()} 과 같습니다.
     * 
     * 만약 덱이 빈 상태(empty)일 경우 {@code null}을 반환합니다. 
     *
     * @return 덱의 첫 번째 요소 또는 덱이 비었을 경우 {@code null}을 반환
     */
    E poll();

    /**
     * 큐의 첫 번째(head) 요소를 반환하되 삭제하진 않습니다.
     * 큐를 확장한 덱의 경우 덱의 첫 번째 요소를 반환하되 삭제하지 않는 것과 같습니다.
     * 만약 덱이 비었을 경우 예외를 던집니다.
     *
     * 이 메소드는 {@link #getFirst()} 과 같습니다.
     *     
     * @return 덱의 첫 번째 요소를 반환
     * @throws NoSuchElementException 덱이 비었을 경우
     */
    E element();

    /**
     * 큐의 첫 번째(head) 요소를 반환하되 삭제하진 않습니다.
     * 큐를 확장한 덱의 경우 덱의 첫 번째 요소를 반환하되 삭제하지 않는 것과 같습니다.
     * 만약 덱이 비었을 경우 {@code null}을 반환합니다.
     *
     * 이 메소드는 {@link #peekFirst()} 과 같습니다.
     *     
     * @return 덱의 첫 번째 요소 또는 덱이 비었을 경우 {@code null}을 반환 
     */
    E peek();

}
