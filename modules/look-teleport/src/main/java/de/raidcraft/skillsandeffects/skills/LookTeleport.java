package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.*;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log(topic = "RCSkills:look-teleport")
@SkillInfo(value = "look-teleport")
public class LookTeleport extends AbstractSkill implements Executable {

    public static class Factory implements SkillFactory<LookTeleport> {

        @Override
        public Class<LookTeleport> getSkillClass() {
            return LookTeleport.class;
        }

        @Override
        public @NonNull LookTeleport create(SkillContext context) {
            return new LookTeleport(context);
        }

    }

    boolean playSound = true;
    String sound = Sound.ENTITY_ENDERMAN_TELEPORT.name();
    private Sound soundEffect = Sound.ENTITY_ENDERMAN_TELEPORT;
    boolean playEffect = true;
    Effect effectEffect = Effect.PORTAL_TRAVEL;
    String effect = Effect.PORTAL_TRAVEL.name();

    Set<Material> transparentBlocks = new HashSet<>();

    protected LookTeleport(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        playSound = config.getBoolean("play_sound", playSound);
        try {
            soundEffect = Sound.valueOf(config.getString("sound", sound));
        } catch (IllegalArgumentException e) {
            log.warning("unknown sound effect " + sound + " in skill config " + alias());
        }

        this.playEffect = config.getBoolean("play_effect", playEffect);
        try {
            effectEffect = Effect.valueOf(config.getString("effect", effect));
        } catch (IllegalArgumentException e) {
            log.warning("unknown effect " + effect + " in skill config " + alias());
        }

        transparentBlocks.clear();
        List<String> list = config.getStringList("transparent_blocks");
        if (list.isEmpty()) {
            Arrays.stream(Material.values())
                    .filter(Material::isTransparent)
                    .forEach(transparentBlocks::add);
        } else {
            for (String block : list) {
                Material material = Material.matchMaterial(block);
                if (material == null) {
                    log.warning("the material " + block + " is not valid in the skill config of: " + alias());
                } else {
                    transparentBlocks.add(material);
                }
            }
        }
    }

    @Override
    public ExecutionResult execute(ExecutionContext context) {


        Player player = context.player();
        List<Block> lineOfSight = player.getLineOfSight(transparentBlocks, context.config().range())
                .stream().filter(block -> block.getRelative(BlockFace.UP).getType().isAir())
                .collect(Collectors.toUnmodifiableList());
        if (lineOfSight.isEmpty()) {
            if (player.isSwimming()) {
                return context.failure("Du kannst dich Unterwasser nicht teleportieren.");
            }
            return context.failure("Deine Sicht ist blockiert oder das Ziel ist zu klein für dich.");
        }

        for (int i = lineOfSight.size() - 1; i >= 0; i--) {
            Block block = lineOfSight.get(i);
            Location target = block.getLocation().add(0, 1, 0);
            if (target.getBlock().getType() == Material.AIR && target.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                // location is safe to teleport
                player.teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);
                if (playEffect) {
                    player.playEffect(target, effectEffect, null);
                }
                if (playSound) {
                    player.playSound(target, soundEffect, SoundCategory.MASTER, 10f, 1f);
                }
                return context.success();
            }
        }


        return context.failure("Es wurde kein sicheres Ziel für den Teleport gefunden.");
    }
}
