package xyz.holocons.mc.lobotomizestuckvillagers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.WallBlock;
import xyz.holocons.mc.lobotomizestuckvillagers.LobotomizeStuckVillagersMod;

@Mixin(Villager.class)
abstract class VillagerMixin extends AbstractVillager {

    private boolean isLobotomized = false;
    private int notLobotomizedCount = 0;

    private VillagerMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    private boolean checkLobotomized() {
        final var interval = this.notLobotomizedCount > 3
                ? LobotomizeStuckVillagersMod.config().instance().villagerActiveCheckInterval()
                : LobotomizeStuckVillagersMod.config().instance().villagerLobotomizedCheckInterval();
        if (this.tickCount % interval == 0) {
            this.isLobotomized = !canTravelFrom(new BlockPos(getX(), getY() + 0.0625D, getZ()));

            this.notLobotomizedCount = this.isLobotomized ? 0 : this.notLobotomizedCount + 1;
        }
        return this.isLobotomized;
    }

    private boolean canTravelFrom(BlockPos pos) {
        return canTravelTo(pos.east()) || canTravelTo(pos.west())
                || canTravelTo(pos.north()) || canTravelTo(pos.south());
    }

    private boolean canTravelTo(BlockPos pos) {
        final var state = this.level.getBlockStateIfLoaded(pos);
        if (state == null) {
            return false;
        }
        final var bottom = state.getBlock();
        if (bottom instanceof FenceBlock || bottom instanceof FenceGateBlock || bottom instanceof WallBlock) {
            return false;
        }
        final var top = this.level.getBlockState(pos.above()).getBlock();
        return !bottom.hasCollision && !top.hasCollision;
    }

    @ModifyVariable(method = "mobTick", at = @At("HEAD"))
    private boolean injectLobotomizedCheck(boolean inactive) {
        return inactive || checkLobotomized();
    }

    @Inject(method = "mobTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
    private void injectLobotomizedRestock(boolean inactive, CallbackInfo info) {
        if (this.isLobotomized && ((Villager) (Object) this).shouldRestock()) {
            ((Villager) (Object) this).restock();
        }
    }
}
