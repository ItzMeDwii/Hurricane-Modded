Hurricane 2.2.1:
- Updated to Minecraft 26.3, Fabric Loader 0.20.1, Fabric API 0.165.2 and NeoForge 26.3.0.101
- Requires Java 25 (Minecraft 26.3 class files)
- Fixed the pointed dripstone collision fix not applying on 26.3, where `getShape` moved to `SpeleothemBlock`
- Fixed the "Mismatch in destroy block pos" warning suppression not applying on 26.3, where the warning moved to `ServerPlayerGameMode.abortDestroyBlock`
- Added a workaround so Bedrock players can hit entities with spears: their attacks now run the spear's jab instead of being dropped as a piercing-weapon attack

Hurricane 2.1:
- Added config version
- Removed the item steerables workaround due to it being implemented natively in Geyser