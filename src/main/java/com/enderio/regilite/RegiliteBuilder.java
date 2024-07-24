package com.enderio.regilite;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.UnaryOperator;

/**
 * Regilite builders contain metadata useful for data-generation and linked objects.
 * This pattern is enforced to ensure that after construction, all builders go out of scope, and thus not consume memory.
 * Otherwise, we're holding a ton of useless information at runtime.
 */
public abstract class RegiliteBuilder<B extends RegiliteBuilder<B, R, T, H>, R, T extends R, H extends DeferredHolder<R, T>> {

    private final H holder;

    protected RegiliteBuilder(H holder) {
        this.holder = holder;
    }

    /**
     * Once you have finished setting up the object, call this to get its holder.
     * This will allow all the builder data to be flushed after initialization.
     * @return The holder.
     */
    public final H finishHolder() {
        return holder;
    }

    @ApiStatus.Internal
    public final T get() {
        return holder.value();
    }

    protected final ResourceLocation getId() {
        return holder.getId();
    }

    public final B with(UnaryOperator<B> applicator) {
        return applicator.apply(selfCast());
    }

    private B selfCast() {
        //noinspection unchecked
        return (B)this;
    }
}
