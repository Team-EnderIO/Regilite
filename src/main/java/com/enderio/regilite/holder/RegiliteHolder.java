package com.enderio.regilite.holder;

import java.util.function.Consumer;

public interface RegiliteHolder<T extends RegiliteHolder<T>> {

    /**
     * Mutate this object using the provided applicator.
     * This can be used to collect common combinations of modifications together to reduce repetition.
     * @param applicator An applicator that will modify the object.
     * @return The object.
     */
    default T with(Consumer<T> applicator) {
        //noinspection unchecked
        T self = (T)this;
        applicator.accept(self);
        return self;
    }
}
