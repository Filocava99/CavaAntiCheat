package it.filippocavallari.cavaanticheat.client;

import it.filippocavallari.cavaanticheat.common.ClientInfoPacket;
import it.filippocavallari.cavaanticheat.common.HashingFunction;
import it.filippocavallari.cavaanticheat.common.InspectionResult;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ClientAntiCheat.MODID)
public class ClientAntiCheat {
    public static final String MODID = "cavaanticheat";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ClientAntiCheat() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        ForgeSpigotChannel.registerChannel();
        ForgeSpigotChannel.registerMessage();
    }



    @SubscribeEvent
    public void onWorldJoin(final EntityJoinWorldEvent event){
        if(event.getEntity() instanceof Player){
            ForgeSpigotChannel.SIMPLE_CHANNEL.sendToServer(inspectClient());
        }
    }

    private ClientInfoPacket inspectClient(){
        ClientInfoPacket clientInfoPacket  = new ClientInfoPacket();
        inspectMods(clientInfoPacket);
        inspectResourcePacks(clientInfoPacket);
        return clientInfoPacket;
    }

    private void inspectMods(ClientInfoPacket clientInfoPacket){
        List<ModInfo> modsInfo = FMLLoader.getLoadingModList().getMods();
        for(ModInfo modInfo : modsInfo){
            try{
                File modFile = FMLLoader.getLoadingModList().getModFileById(modInfo.getModId()).getFile().getFilePath().toFile();
                if(modFile.exists()){
                    String checksum = HashingFunction.getFileChecksum(modFile);
                    clientInfoPacket.addModChecksum(modInfo.getModId(), checksum);
                    clientInfoPacket.setModsInspectionResult(InspectionResult.NORMAL);
                    clientInfoPacket.setModsInspectionMessage("Every mod has been successfully hashed");
                }else{
                    clientInfoPacket.setModsInspectionResult(InspectionResult.FILE_NOT_FOUND);
                    clientInfoPacket.setModsInspectionMessage(modFile.getName() + " not found");
                    break;
                }
            }catch (IOException e){
                clientInfoPacket.setModsInspectionResult(InspectionResult.IO_EXCEPTION);
                clientInfoPacket.setModsInspectionMessage(e.getMessage());
                LOGGER.error(e);
                break;
            }catch (NoSuchAlgorithmException e){
                clientInfoPacket.setModsInspectionResult(InspectionResult.HASH_FUNCTION_NOT_FOUND);
                clientInfoPacket.setModsInspectionMessage(e.getMessage());
                LOGGER.error(e);
                break;
            }
        }
    }

    private void inspectResourcePacks(ClientInfoPacket clientInfoPacket){
        File resourcePacksFolder = Minecraft.getInstance().getResourcePackDirectory();
        for(File file : Objects.requireNonNull(resourcePacksFolder.listFiles())){
            if(file.isDirectory()){
                clientInfoPacket.setTextureInspectionResult(InspectionResult.INVALID_TEXTURE_FORMAT);
                clientInfoPacket.setTextureInspectionMessage("Texture called " + file.getName() + " as directory found");
                break;
            }else{
                try{
                    clientInfoPacket.addTextureChecksum(file.getName(), HashingFunction.getFileChecksum(file));
                    clientInfoPacket.setTextureInspectionResult(InspectionResult.NORMAL);
                    clientInfoPacket.setTextureInspectionMessage("Every texture has been successfully hashed");
                } catch (NoSuchAlgorithmException e) {
                    clientInfoPacket.setTextureInspectionResult(InspectionResult.HASH_FUNCTION_NOT_FOUND);
                    clientInfoPacket.setTextureInspectionMessage(e.getMessage());
                    LOGGER.error(e);
                    break;
                } catch (IOException e) {
                    clientInfoPacket.setTextureInspectionResult(InspectionResult.IO_EXCEPTION);
                    clientInfoPacket.setTextureInspectionMessage(e.getMessage());
                    LOGGER.error(e);
                    break;
                }
            }
        }
    }


}
