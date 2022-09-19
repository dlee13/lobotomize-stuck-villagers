package xyz.holocons.mc.lobotomizestuckvillagers;

import org.spongepowered.configurate.CommentedConfigurationNode;

import com.google.inject.Inject;

import space.vectrix.ignite.api.Platform;
import space.vectrix.ignite.api.config.Configuration;
import space.vectrix.ignite.api.config.Configurations;
import space.vectrix.ignite.api.event.Subscribe;
import space.vectrix.ignite.api.event.platform.PlatformInitializeEvent;

public final class LobotomizeStuckVillagersMod {

    private static Configuration<Config, CommentedConfigurationNode> config;

    private final Platform platform;

    @Inject
    public LobotomizeStuckVillagersMod(final Platform platform) {
        this.platform = platform;
    }

    @Subscribe
    public void onPlatformInitialize(final PlatformInitializeEvent event) {
        final var configPath = platform.getConfigs().resolve("lobotomize-stuck-villagers.conf");
        final var configKey = Configuration.key(Config.class, configPath);
        LobotomizeStuckVillagersMod.config = Configurations.getOrCreate(Configurations.HOCON_LOADER, configKey);
        if (!configPath.toFile().isFile()) {
            try {
                LobotomizeStuckVillagersMod.config.save();
            } catch (Exception e) {
            }
        }
    }

    public static Configuration<Config, CommentedConfigurationNode> config() {
        return LobotomizeStuckVillagersMod.config;
    }
}
