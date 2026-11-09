package net.onebeastchris.hurricane.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.SpeleothemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpeleothemBlock.class)
public class SpeleothemBlockMixin {

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private void hurricane$getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
        // Since 26.3 the shape lives in SpeleothemBlock, which SulfurSpikeBlock extends as well - only cover dripstone.
        if (!((Object) this instanceof PointedDripstoneBlock)) {
            return;
        }
        cir.setReturnValue(Shapes.empty());
    }
}
