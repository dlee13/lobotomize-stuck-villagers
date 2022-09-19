package xyz.holocons.mc.lobotomizestuckvillagers;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class Config {

    @Setting(value = "villagerActiveCheckInterval")
    @Comment(value = "Interval, in ticks, between trapped checks for active villagers")
    private int villagerActiveCheckInterval = 200;

    @Setting(value = "villagerLobotomizedCheckInterval")
    @Comment(value = "Interval, in ticks, between trapped checks for lobotomized villagers")
    private int villagerLobotomizedCheckInterval = 100;

    public int villagerActiveCheckInterval() {
        return this.villagerActiveCheckInterval;
    }

    public void villagerActiveCheckInterval(final int villagerActiveCheckInterval) {
        this.villagerActiveCheckInterval = Math.max(villagerActiveCheckInterval, 100);
    }

    public int villagerLobotomizedCheckInterval() {
        return this.villagerLobotomizedCheckInterval;
    }

    public void villagerLobotomizedCheckInterval(final int villagerLobotomizedCheckInterval) {
        this.villagerLobotomizedCheckInterval = Math.max(villagerLobotomizedCheckInterval, 100);
    }
}
