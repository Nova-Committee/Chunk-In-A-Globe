package committee.nova.mods.dg;

import committee.nova.mods.dg.client.render.GlobeBlockEntityRenderer;
import committee.nova.mods.dg.common.net.ForgeGlobeSectionManagerServer;
import committee.nova.mods.dg.utils.ForgeDimensionHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static committee.nova.mods.dg.CommonClass.*;
import static committee.nova.mods.dg.ModRegistries.*;

@Mod(Constants.MOD_ID)
public class DimensionGlobeForge {
    public DimensionGlobeForge() {
        CommonClass.managerServer = new ForgeGlobeSectionManagerServer();
        CommonClass.dimensionHelper= new ForgeDimensionHelper();

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetUp);
        bus.addListener(this::clientSetUp);

        ModRegistries.BLOCKS.register(bus);
        ModRegistries.ITEMS.register(bus);
        ModRegistries.BLOCK_ENTITIES.register(bus);
        ModRegistries.TABS.register(bus);
        ModRegistries.SERIALIZERS.register(bus);
        ModRegistries.CHUNK_GENERATOR.register(bus);
    }

    public void commonSetUp(FMLCommonSetupEvent event){
        globeBlock = globeBlock1.get();
        globeBlockItem = globeBlockItem1.get();
        globeBlockEntityType = globeBlockEntityType1.get();
        //globeCrafting = globeCrafting1.get();
        globeItemGroup = globeItemGroup1.get();
    }

    public void clientSetUp(FMLClientSetupEvent event){
        BlockEntityRenderers.register(CommonClass.globeBlockEntityType, GlobeBlockEntityRenderer::new);
    }
}