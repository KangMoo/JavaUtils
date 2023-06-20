package com.github.kangmoo.utils.common;

import lombok.Synchronized;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ManagedResourcePool은 스레드 안전한 클래스로, 리소스를 관리한다.
 * 리소스 중복을 방지하고 FIFO (First-In-First-Out)를 이용한 리소스 관리를 강제한다.
 * 리소스를 FIFO 순서로 관리하기 위해 ConcurrentLinkedQueue를 사용하고, 중복을 방지하기 위해 Set을 사용한다.
 *
 * @param <E> 이 풀이 관리할 리소스의 타입.
 * @author kangmoo Heo
 */
public class ManagedResourcePool<E> {
    private final ConcurrentLinkedQueue<E> resourceQueue = new ConcurrentLinkedQueue<>();
    private final Set<E> resourceSet = ConcurrentHashMap.newKeySet();

    /**
     * 새로운 ManagedResourcePool를 구성하고, 지정된 리소스를 풀에 추가한다.
     *
     * @param resources 풀에 추가될 리소스.
     */
    public ManagedResourcePool(E... resources) {
        for (E resource : resources) {
            addResource(resource);
        }
    }

    /**
     * 리소스 큐의 맨 앞에 있는 리소스를 검색하고 제거하거나, 이 풀이 비어 있을 경우 빈 Optional을 반환한다.
     *
     * @return 리소스 큐의 맨 앞에 있는 리소스, 또는 이 풀이 비어 있을 경우 빈 Optional.
     */
    @Synchronized
    public Optional<E> retain() {
        return Optional.ofNullable(resourceQueue.poll());
    }

    /**
     * 리소스를 풀에 다시 추가한다.
     * 만약 리소스가 풀의 일부가 아니거나 이미 큐에 있을 경우, 예외가 발생한다.
     *
     * @param resource 풀에 다시 추가될 리소스.
     * @throws IllegalArgumentException 리소스가 풀의 일부가 아닐 경우.
     * @throws IllegalStateException    리소스가 이미 큐에 있는 경우.
     */
    @Synchronized
    public void release(E resource) {
        if (!resourceSet.contains(resource)) throw new IllegalArgumentException("Invalid resource: " + resource);
        if (resourceQueue.contains(resource))
            throw new IllegalStateException("Resource already in the pool: " + resource);
        resourceQueue.offer(resource);
    }

    /**
     * 리소스를 풀에 추가한다.
     * 만약 리소스가 null이거나 중복될 경우, 예외가 발생한다.
     *
     * @param resource 풀에 추가될 리소스.
     * @throws NullPointerException     리소스가 null인 경우.
     * @throws IllegalStateException    리소스가 중복될 경우.
     */
    @Synchronized
    public void addResource(E resource) {
        if (resource == null) throw new NullPointerException("Resource should not be null");
        if (!resourceSet.add(resource))
            throw new IllegalStateException("Resource is duplicated. Resource must be Unique: " + resource);
        resourceQueue.add(resource);
    }

    /**
     * 리소스를 풀에서 제거한다.
     *
     * @param resource 풀에서 제거될 리소스.
     */
    @Synchronized
    public void removeResource(E resource) {
        resourceSet.remove(resource);
        resourceQueue.remove(resource);
    }
}
