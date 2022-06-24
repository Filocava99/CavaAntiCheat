package it.filippocavallari.cavaanticheat.client;

import it.filippocavallari.cavaanticheat.common.ClientInfoPacket;
import it.filippocavallari.cavaanticheat.common.SerializationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ForgeSpigotChannel {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel SIMPLE_CHANNEL;


    public static void registerChannel(){
        SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("cava", "anticheat"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );
    }

    public static void registerMessage(){
        SIMPLE_CHANNEL.registerMessage(0,
                ClientInfoPacket.class,
                (clientInfoPacket, packetBuffer) -> {
                    try {
                        packetBuffer.writeBytes(SerializationUtils.serialize(clientInfoPacket));
                    }catch (IOException e){
                        LOGGER.error(e);
                    }
                },
                (packetBuffer) -> {
                    ClientInfoPacket clientInfoPacket = null;
                    try{
                        clientInfoPacket = (ClientInfoPacket) SerializationUtils.deserialize(packetBuffer.readByteArray());
                    }catch (IOException | ClassNotFoundException e ){
                        LOGGER.error(e);
                    }
                    return clientInfoPacket;
                },
                null
        );
    }
}
