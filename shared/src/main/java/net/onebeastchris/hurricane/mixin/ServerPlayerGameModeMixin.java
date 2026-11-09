package net.onebeastchris.hurricane.mixin;

import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    // 26.3 moved the mismatch warning out of handleBlockBreakAction into this private method.
    @Redirect(method = "abortDestroyBlock",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
    private void hurricane$onMismatchInDestroyBlockPos(org.slf4j.Logger logger, String format, Object arg1, Object arg2) {
        // do nothing
    }
}
