package me.libreh.worldreset.api;

import net.fabricmc.api.ModInitializer;

public final class WorldResetApiInit implements ModInitializer {
    @Override
    public void onInitialize() {
        // WorldPreloader uses ServerChunkCache#getChunkFuture on 1.21.1, so no custom
        // ticket type registration is needed.
    }
}
