package me.libreh.worldreset.mixin.world;

import me.libreh.worldreset.api.ResetFlags;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DimensionDataStorage.class)
public class SavedDataStorageSkipSaveMixin {
    // 1.21.1 has no close()/saveAndJoin() here, save() is the only persist path,
    // so cancel it outright instead of wrapping the call inside close().
    @Inject(
        method = "save",
        at = @At("HEAD"),
        cancellable = true
    )
    private void worldreset$skipSaveDuringReset(CallbackInfo ci) {
        if (ResetFlags.skipCloseSave.get()) {
            ci.cancel();
        }
    }
}
