package com.harrykay.smartgolems;

import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.harrykay.smartgolems.server.command.CommandSmartGolems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.harrykay.smartgolems.init.ModEntities.SMART_GOLEM;

@Mod.EventBusSubscriber(modid = SmartGolems.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
//@ObjectHolder(SmartGolems.MOD_ID)
@Mod(SmartGolems.MOD_ID)
public class SmartGolems {

    public static final String MOD_ID = "smartgolems";
    private static final Logger LOGGER = LogManager.getLogger();
    public static List<SmartGolemEntity> golems = new ArrayList<>();

    public SmartGolems() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean assignGolemToPlayer(SmartGolemEntity golem, PlayerEntity player) {
        if (player == null || golem == null) {
            return false;
        }

        if (player.world.isRemote) {
            return false;
        }

        golems.add(golem);
        return true;
    }

    public static boolean createSmartGolem(PlayerEntity playerEntity) {
        if (playerEntity.world.isRemote) {
            return false;
        }
        SmartGolemEntity golem = new SmartGolemEntity(SMART_GOLEM, playerEntity.world);

        golem.setLocationAndAngles(playerEntity.posX, playerEntity.posY, playerEntity.posZ, playerEntity.rotationYaw, 0.0F);
        golem.setCustomName(new StringTextComponent("" + golems.size()));
        golem.setCustomNameVisible(true);

        if (playerEntity.world.addEntity(golem)) {
            LOGGER.debug("Golem created.");
            golems.add(golem);

            return true;
        }
        LOGGER.debug("Golem could not be created.");
        return false;
    }

    public static boolean removeGolem(PlayerEntity playerEntity, String customName) {
        if (playerEntity.world.isRemote) {
            return false;
        }

        for (SmartGolemEntity golem : golems) {
            if (golem.getDisplayName().getString().equals(customName)) {
                golem.remove(false);
                golems.remove(golem);
                return true;
            }
        }

        return false;
    }

    public static SmartGolemEntity getGolem(PlayerEntity playerEntity, String customName) {
        if (playerEntity.world.isRemote) {
            return null;
        }

        for (SmartGolemEntity golem : golems) {
            if (golem.getDisplayName().getString().equals(customName)) {
                return golem;
            }
        }

        return null;
    }

    public static boolean removeAllGolems(PlayerEntity playerEntity) {
        if (playerEntity.world.isRemote) {
            return false;
        }


        for (SmartGolemEntity golem : golems) {
            golem.remove(false);
        }

        return true;
    }


    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        new CommandSmartGolems(event.getCommandDispatcher());
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

        }
    }
}


