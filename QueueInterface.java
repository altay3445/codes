public interface QueueInterface<T> {
    void enqueue(T data);
    T dequeue();
    boolean isEmpty();
    int size();
}
