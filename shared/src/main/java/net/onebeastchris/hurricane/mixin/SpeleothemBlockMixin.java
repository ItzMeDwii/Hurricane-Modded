package net.onebeastchris.hurricane.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SpeleothemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.onebeastchris.hurricane.util.BedrockUtils;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpeleothemBlock.class)
public abstract class SpeleothemBlockMixin {

	/**
	 * Pointed dripstone and sulfur spikes share one block class since 26.3, and the Bedrock client collides with
	 * their edges differently than the Java server does (MCPE-240854), which rubber-bands Bedrock players a few
	 * blocks back. Ignore the server-side collision for Bedrock players so the server accepts the movement their
	 * client predicted; Java players and mobs keep the vanilla shape.
	 * <p>
	 * This has to be {@code getCollisionShape} and not {@code getShape}: the entity's collision context is dropped
	 * by {@link net.minecraft.world.level.block.state.BlockBehaviour}'s default implementation before it reaches
	 * {@code getShape}, so this is the only place where the player is known. Added to {@code SpeleothemBlock} by
	 * the mixin, overriding the inherited default.
	 */
	protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
	                                       CollisionContext collisionContext) {
		if (!collisionContext.isPlacement()
				&& collisionContext instanceof EntityCollisionContext entityCollisionContext
				&& entityCollisionContext.getEntity() instanceof Player player
				&& BedrockUtils.isBedrockPlayer(player.getUUID())) {
			return Shapes.empty();
		}

		// What the overridden default does for a block with collision (both speleothems have it).
		return blockState.getShape(blockGetter, blockPos);
	}
}
