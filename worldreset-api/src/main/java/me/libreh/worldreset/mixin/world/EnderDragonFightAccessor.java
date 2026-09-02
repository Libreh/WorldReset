package me.libreh.worldreset.mixin.world;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EndDragonFight.class)
public interface EnderDragonFightAccessor {
    @Accessor("dragonEvent")
    ServerBossEvent worldreset$getDragonEvent();
}
