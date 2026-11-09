package net.onebeastchris.hurricane.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.PiercingWeapon;
import net.onebeastchris.hurricane.util.BedrockUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow
    private ServerPlayer player;

    /**
     * Vanilla drops the ordinary attack packet for piercing weapons ("handleAttack" returns right after the
     * {@code DataComponents.PIERCING_WEAPON} check). A Java client never sends it for a spear - it sends
     * {@code ServerboundPlayerActionPacket.Action.STAB} with no target and the server raycasts on its own - but
     * Geyser turns a Bedrock attack into a plain entity attack, so a Bedrock player can never hit anything with a
     * spear. Run the spear's jab here instead, with the same gate the vanilla STAB branch uses.
     */
    @Inject(method = "handleAttack",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/core/component/DataComponents;PIERCING_WEAPON:Lnet/minecraft/core/component/DataComponentType;"),
            cancellable = true)
    private void hurricane$bedrockSpearStab(ServerboundAttackPacket packet, CallbackInfo ci) {
        if (!BedrockUtils.isBedrockPlayer(this.player.getUUID())) {
            return;
        }

        final ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        final PiercingWeapon piercingWeapon = stack.get(DataComponents.PIERCING_WEAPON);
        if (piercingWeapon == null) {
            return;
        }

        // Mirrors the STAB branch of ServerGamePacketListenerImpl#handlePlayerAction.
        if (!this.player.isSpectator() && !this.player.cannotAttackWithItem(stack, 5)) {
            piercingWeapon.attack(this.player, EquipmentSlot.MAINHAND);
        }
        ci.cancel();
    }
}
