package me.libreh.worldreset.predicate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

// Per-cycle accumulator for one Trigger. Each note* returns true when the event advanced this
// trigger (which gates re-evaluation); a trigger ignores the event types it does not care about.
public interface TriggerState {
    default boolean notePortal(ServerPlayer player, ResourceLocation block, ResourceKey<Level> originDimension) {
        return false;
    }

    default boolean noteDeath(ResourceLocation entityId, Entity entity) {
        return false;
    }

    default boolean noteAdvancement(ServerPlayer player, ResourceLocation advancementId) {
        return false;
    }

    boolean isSatisfied(int playerCount);
}
