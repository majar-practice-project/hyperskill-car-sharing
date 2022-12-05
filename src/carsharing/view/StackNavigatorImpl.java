package carsharing.view;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StackNavigatorImpl implements StackNavigator {
    private Deque<Object[]> stack = new ArrayDeque<>();

    @Override
    public void navigate(Runnable action){
        stack.push(new Object[]{action});
        action.run();
    }

    @Override
    public <T> void navigate(T arg, Consumer<T> action) {
        stack.push(new Object[]{action, arg});
        action.accept(arg);
    }

    @Override
    public void stay() {
        Object[] params = stack.peek();
        if (params == null) throw new RuntimeException("Shouldn't occur");
        if (params[0] instanceof Runnable) {
            ((Runnable) params[0]).run();
        } else if (params[0] instanceof Consumer) {
            ((Consumer) params[0]).accept(params[1]);
        } else if (params[0] instanceof BiConsumer) {
            ((BiConsumer) params[0]).accept(params[1], params[2]);
        }
    }

    @Override
    public <T1, T2> void navigate(T1 arg1, T2 arg2, BiConsumer<T1, T2> action) {
        stack.push(new Object[]{action, arg1, arg2});
        action.accept(arg1, arg2);
    }

    @Override
    public void back() {
        drop();
        stay();
    }

    @Override
    public void drop() {
        stack.pop();
    }
}
