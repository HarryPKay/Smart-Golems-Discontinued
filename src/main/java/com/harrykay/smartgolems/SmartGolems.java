package com.harrykay.smartgolems;

import com.harrykay.smartgolems.common.entity.SmartGolemEntity;
import com.harrykay.smartgolems.server.command.CommandRegister;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SmartGolems.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(SmartGolems.MOD_ID)
@Mod(SmartGolems.MOD_ID)
public class SmartGolems {
    @ObjectHolder("smart_golem")
    public static final EntityType<SmartGolemEntity> SMART_GOLEM = null;
    static final String MOD_ID = "smartgolems";
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

    public static boolean createIntelGolem(PlayerEntity playerEntity) {
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

//    @SubscribeEvent
//    public static void onClientChat(ClientChatEvent event) throws CommandSyntaxException {
//        //.debug("onClientChat received: " + event.getMessage());
//        System.out.println("onClientChat received: " + event.getMessage());
//        dispatcher.execute(event.getMessage(), Minecraft.getInstance().player.getCommandSource());
//    }

    /**
     * The actual event handler that registers the custom items.
     *
     * @param event The event this event handler handles
     */
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {

    }

    @SubscribeEvent
    public static void onRegisterEntity(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(
                EntityType.Builder.create(SmartGolemEntity::new, EntityClassification.MISC)
                        .setShouldReceiveVelocityUpdates(true).setTrackingRange(24).setUpdateInterval(60)
                        .build("smart_golem").setRegistryName(MOD_ID, "smart_golem")
        );
    }
    //public Block test = new ModBlock(Block.Properties.from(Blocks.DIRT), "test");

    @SubscribeEvent
    public void onPickUpItem(EntityItemPickupEvent event) {

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
        new CommandRegister(event.getCommandDispatcher());
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


