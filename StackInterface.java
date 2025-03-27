public interface StackInterface<T> {
    void push(T data);
    T pop();
    T peek();
    boolean isEmpty();
    int size();
}
