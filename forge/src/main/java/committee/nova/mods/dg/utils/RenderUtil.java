package committee.nova.mods.dg.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.Iterator;
import java.util.Optional;

/**
 * RenderUtil
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/19 上午3:37
 */
public class RenderUtil {
//    public static BlockState readBlockState(HolderGetter<Block> $$0, CompoundTag $$1) {
//        if (!$$1.contains("Name", 8)) {
//            return Blocks.AIR.defaultBlockState();
//        } else {
//            ResourceLocation $$2 = new ResourceLocation($$1.getString("Name"));
//            Optional<? extends Holder<Block>> $$3 = $$0.get(ResourceKey.create(Registries.BLOCK, $$2));
//            if ($$3.isEmpty()) {
//                return Blocks.AIR.defaultBlockState();
//            } else {
//                Block $$4 = (Block)((Holder<?>)$$3.get()).value();
//                BlockState $$5 = $$4.defaultBlockState();
//                if ($$1.contains("Properties", 10)) {
//                    CompoundTag $$6 = $$1.getCompound("Properties");
//                    StateDefinition<Block, BlockState> $$7 = $$4.getStateDefinition();
//
//                    for (String $$8 : $$6.getAllKeys()) {
//                        net.minecraft.world.level.block.state.properties.Property<?> $$9 = $$7.getProperty($$8);
//                        if ($$9 != null) {
//                            $$5 = (BlockState) setValueHelper($$5, $$9, $$8, $$6, $$1);
//                        }
//                    }
//                }
//
//                return $$5;
//            }
//        }
//    }
}
