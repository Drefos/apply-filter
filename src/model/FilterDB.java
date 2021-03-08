package model;

import java.util.HashMap;
import java.util.Map;

public class FilterDB {
    private Map<String, Filter> filterMap;

    public FilterDB() {
        filterMap = new HashMap<>();
    }

    public Filter getByName(String name) {
        return filterMap.get(name);
    }

    public boolean contains(String name) {
        return filterMap.containsKey(name);
    }

    public void addFilter(String name, Filter filter) {
        filterMap.put(name, filter);
    }

    public void updateFilter(String name, Filter filter) {
        filterMap.replace(name, filter);
    }
}
