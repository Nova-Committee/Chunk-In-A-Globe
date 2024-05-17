package committee.nova.mods.dg.mixin;

import committee.nova.mods.dg.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

	@Shadow
    private Level level;

	@Inject(method = "shouldBlockExplode", at = @At("HEAD"), cancellable = true)
	private void canExplosionDestroyBlock(Explosion $$0, BlockGetter world, BlockPos pos, BlockState state, float $$4, CallbackInfoReturnable<Boolean> cir) {
		if (level.dimension() == CommonClass.globeDimension && state.getBlock() == CommonClass.globeBlock) {
			cir.setReturnValue(false);
		}
	}
}
