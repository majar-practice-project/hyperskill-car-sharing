package carsharing.database;

import java.util.List;

public interface DatabaseDAO<T> {
    List<T> getAll();

    int add(T row);

    T getById(int id);

    void update(T row);
}
