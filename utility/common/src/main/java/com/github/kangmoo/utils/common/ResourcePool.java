package com.github.kangmoo.utils.common;

import lombok.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author kangmoo Heo
 */
public class ResourcePool<E> {
    private final Set<E> resources = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final ConcurrentLinkedQueue<E> usableResources = new ConcurrentLinkedQueue<>();

    public boolean add(@NonNull E e) {
        if (resources.add(e)) {
            usableResources.add(e);
            return true;
        }
        return false;
    }

    public boolean remove(@NonNull E e) {
        if (resources.remove(e)) {
            usableResources.remove(e);
            return true;
        }
        return false;
    }

    public Optional<E> alloc() {
        return Optional.ofNullable(usableResources.poll());
    }

    public boolean dealloc(@NonNull E e) {
        if (resources.contains(e)) {
            usableResources.add(e);
            return true;
        }
        return false;
    }

    public Set<E> getAllResources(){
        return new HashSet<>(resources);
    }

    public void clear(){
        resources.clear();
        usableResources.clear();
    }

    public int size() {
        return resources.size();
    }

    public boolean isEmpty() {
        return resources.isEmpty();
    }
}
