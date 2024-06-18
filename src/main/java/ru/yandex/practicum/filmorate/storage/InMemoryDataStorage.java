package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDataStorage<D> {
    protected Map<Integer, D> data = new HashMap<>();

    public List<D> findAll() {
        return new ArrayList<>(data.values());
    }

    public int generateNewId() {
        int result = 0;
        for (int i = 1; i <= (data.size() + 1); i++) {
            if (!data.containsKey(i)) {
                result = i;
                break;
            }
        }
        return result;
    }
}
