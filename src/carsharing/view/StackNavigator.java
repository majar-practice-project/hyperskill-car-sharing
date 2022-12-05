package carsharing.view;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface StackNavigator {
    void navigate(Runnable action);

    void stay();
    <T> void navigate(T arg, Consumer<T> action);

    <T1, T2> void navigate(T1 arg1, T2 arg2, BiConsumer<T1, T2> action);

//    <T1, T2, T3> void navigate(T1 arg1, T2 arg2, T3 arg3, TriConsumer<T1, T2, T3> action);
    void back();

    void drop();
}
