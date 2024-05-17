package committee.nova.mods.dg.net;

import committee.nova.mods.dg.Constants;
import committee.nova.mods.dg.common.net.SectionUpdatePkt;
import committee.nova.mods.dg.common.net.UpdateRequestPkt;
import committee.nova.mods.dg.net.pkt.ForgeSectionUpdatePkt;
import committee.nova.mods.dg.net.pkt.ForgeUpdateRequestPkt;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

/**
 * NetWorkDispatcher
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午11:01
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetWorkDispatcher {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(Constants.rl("main"), () -> "1.0", (s) -> true, (s) -> true);

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        CHANNEL.registerMessage(0, UpdateRequestPkt.class, UpdateRequestPkt::toBytes, UpdateRequestPkt::new, ForgeUpdateRequestPkt::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(1, SectionUpdatePkt.class, SectionUpdatePkt::toBytes, SectionUpdatePkt::new, ForgeSectionUpdatePkt::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

}