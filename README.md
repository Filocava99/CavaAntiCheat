# Cava's Anti Cheat
Cava's AntiCheat is composed by a client-side Forge mod and a server-side Spigot plugin that form a powerful client-server anticheat for Minecraft. It detects unauthorized loaded mods, textures and tampering using hashes.

## Installation

### Client side
To install the anticheat on the client just place the CavaAntiCheatClient.jar in the mods folder.

### Server side
Place the CavaAntiCheatServer.jar in the plugins folder, then after the first startup you have to configure the whitelist.  
You have to calculate the SHA-256 hashes for your Minecraft version jar, your forge version jar and for CavaAntiCheatClient jar.  
In case you want to whitelist more mods just add a new line and compute the relative hashes.  
I raccomend to use [this online tool](https://emn178.github.io/online-tools/sha256_checksum.html) for calculating the hashes fast.  
Example:
```
whitelisted-mods:
  minecraft: ffd4fb3037d4110085b4d3d2dc47b760695528eb14292c3e5f7c758983c6d764
  forge: ffd4fb3037d4110085b4d3d2dc47b760695528eb14292c3e5f7c758983c6d764
  cavaanticheat: 5d7fdb3fa948864930064f7f1d46d535b58fee1306793d7c63da5d37f877c965
#List of the enabled resource packs on the server with their checksums.
whitelisted-textures:
#  exampleTexture: exampleChecksum
```

### Commonly asked questions
Q: What if the user doesn't install the client mod? Can he still join with cheats?  
A: Absolutely not. After a delay that you can set in the config (defaults to 5 seconds), the user will be kicked because the client mod hasn't been detected from the server.  
  
Q: What if the user deletes the mods file after the client startup? Can he avoid the protection mechanism?  
A: Nope, if the client detects a loaded mod that isn't not present in the mods folder it inform the server and will be kicked.  
  
Q: Are only jar files checked?  
A: No, every file in the mods folder will be checked.  
  
Q: What if a user changes the modID of a cheat to the modID of a whitelisted mod?  
A: That's not a problem. The hashes of the mod will not match, thus preventing the user from joining.  
  
Disclaimer: as every client-server anticheat not running at kernel level, a skilled user can still find a way to bypass the protection. Other layers of protections server-side only are raccomended.
