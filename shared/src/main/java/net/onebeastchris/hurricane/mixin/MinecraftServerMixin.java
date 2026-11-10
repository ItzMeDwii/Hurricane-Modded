package net.onebeastchris.hurricane.mixin;

import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.clock.ServerClockManager;
import net.onebeastchris.hurricane.util.BedrockUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

	@Shadow
	public abstract PlayerList getPlayerList();

	@Shadow
	public abstract ServerClockManager clockManager();

	@Shadow
	public abstract int getTickCount();

	/**
	 * 26.3 only sends the world clock on join and on dimension changes; clients are expected to advance it
	 * themselves from the rate in that packet. Geyser advances its copy once per Geyser session tick, which runs
	 * on wall-clock time (the TickingState tick rate, default 20) instead of the server's actual tick count, so
	 * a lagging server lets the Bedrock sky run ahead of the world - it gets dark while Java players still see
	 * day, even though gameplay follows the server's clock. Re-send the server's clock to Bedrock players once a
	 * second so their sky is snapped back to the authoritative time of day.
	 */
	@Inject(method = "tickServer(Ljava/util/function/BooleanSupplier;)V", at = @At("TAIL"))
	private void hurricane$syncBedrockClock(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
		if (this.getTickCount() % 20 != 0) {
			return;
		}

		ClientboundSetTimePacket packet = null;
		for (final ServerPlayer player : this.getPlayerList().getPlayers()) {
			if (!BedrockUtils.isBedrockPlayer(player.getUUID())) {
				continue;
			}
			if (packet == null) {
				packet = this.clockManager().createFullSyncPacket();
			}
			player.connection.send(packet);
		}
	}
}
