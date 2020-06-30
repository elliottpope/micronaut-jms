package io.micronaut.jms.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractPool<T extends PooledObject<?>> {

    protected final List<T> pool;
    protected final Integer initialSize;
    protected final Integer maxSize;
    private final List<T> active;

    public AbstractPool(
            Integer initialSize,
            Integer maxSize) {
        this.pool = Collections.synchronizedList(new ArrayList<>(maxSize));
        this.active = Collections.synchronizedList(new ArrayList<>(maxSize));
        this.initialSize = initialSize;
        this.maxSize = maxSize;
    }

    /***
     *
     * Requests an object {@param <T>} from the pool. If the pool is empty then a new object is added to the pool.
     *      If the number of active connections exceeds the configured size then an {@link RuntimeException} is thrown
     *
     * @param args - the arguments to pass to the create method, or to help select an object from the pool.
     * @return a {@link PooledObject} from the pool.
     */
    public T request(Object... args) {
        if (pool.isEmpty()) {
            if (active.size() < maxSize) {
                pool.add(create(args));
            } else {
                throw new RuntimeException("Max Pool size reached.");
            }
        }
        T object = pool.remove(0);
        active.add(object);
        return object;
    }

    /***
     * Release the provided object and return it to the pool.
     *
     * @param pooledObject - the object to return to the pool
     */
    public void release(T pooledObject) {
        active.remove(pooledObject);
        reset(pooledObject);
        pool.add(pooledObject);
    }

    /***
     * Create an object for the pool.
     *
     * @param args - the arguments to be provided to the create method.
     * @return a new object of type {@param <T>} for the pool.
     */
    protected abstract T create(Object... args);

    /***
     * Reset the provided object so that it can be returned to the pool ready for reuse.
     *
     * @param pooledObject
     */
    protected abstract void reset(T pooledObject);
}