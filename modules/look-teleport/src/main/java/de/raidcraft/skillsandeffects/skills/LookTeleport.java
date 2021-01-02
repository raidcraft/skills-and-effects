package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.*;
import de.raidcraft.skills.configmapper.ConfigOption;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;
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

    @ConfigOption
    boolean playSound = true;
    @ConfigOption
    String sound = Sound.ENTITY_ENDERMAN_TELEPORT.name();
    private Sound soundEffect = Sound.ENTITY_ENDERMAN_TELEPORT;
    @ConfigOption
    boolean playEffect = true;
    Effect effectEffect = Effect.PORTAL_TRAVEL;
    @ConfigOption
    String effect = Effect.PORTAL_TRAVEL.name();

    Set<Material> transparentBlocks = new HashSet<>();

    protected LookTeleport(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        try {
            soundEffect = Sound.valueOf(sound);
        } catch (IllegalArgumentException e) {
            log.warning("unknown sound effect " + sound + " in skill config " + context().configuredSkill().alias());
        }

        try {
            effectEffect = Effect.valueOf(effect);
        } catch (IllegalArgumentException e) {
            log.warning("unknown effect " + effect + " in skill config " + context().configuredSkill().alias());
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
                    log.warning("the material " + block + " is not valid in the skill config of: " + context().configuredSkill().alias());
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

        Block target = lineOfSight.get(lineOfSight.size() - 1);
        player.teleport(target.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        if (playEffect) {
            player.playEffect(target.getLocation(), effectEffect, null);
        }
        if (playSound) {
            player.playSound(target.getLocation(), soundEffect, SoundCategory.MASTER, 10f, 1f);
        }

        return context.success();
    }
}
