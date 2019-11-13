package io.pivotal.pal.tracker;


import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> entryMap = new HashMap<>(0);
    private long sequence;

    public TimeEntry create(TimeEntry entry) {
        //TODO: Implement sequence
        entry.setId(++sequence);
        entryMap.put(entry.getId(), entry);
        return entry;
    }

    public TimeEntry find(long id) {
        return entryMap.get(id);
    }

    public List<TimeEntry> list() {
        List<TimeEntry> list = new ArrayList<>();
        list.addAll(entryMap.values());
        return list;
    }

    public TimeEntry update(long id, TimeEntry newVal) {
        TimeEntry oldVal = find(id);
        if(null == oldVal) {
            return oldVal;
        }
        newVal.setId(id);
        entryMap.put(id, newVal);
        return newVal;
    }

    public void delete(long id) {
        entryMap.remove(id);
    }
}
