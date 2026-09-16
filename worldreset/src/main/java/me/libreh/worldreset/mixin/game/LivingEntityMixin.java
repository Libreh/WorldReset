package me.libreh.worldreset.mixin.game;

import me.libreh.worldreset.WorldReset;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void worldreset$blockDamageDuringReset(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        MinecraftServer server = ((LivingEntity) (Object) this).level().getServer();
        if (server == null) {
            return;
        }
        var worlds = WorldReset.worlds(server);
        if (worlds != null && worlds.isResetting()) {
            cir.setReturnValue(false);
        }
    }
}
