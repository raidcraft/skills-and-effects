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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;
import java.util.stream.Collectors;

@Log(topic = "RCSkills:piglings-friend")
@SkillInfo(value = "piglings-friend")
public class PiglingsFriend extends AbstractSkill implements Listener {

    public static class Factory implements SkillFactory<PiglingsFriend> {

        @Override
        public Class<PiglingsFriend> getSkillClass() {
            return PiglingsFriend.class;
        }

        @Override
        public @NonNull PiglingsFriend create(SkillContext context) {
            return new PiglingsFriend(context);
        }

    }

    PiglingsFriend(SkillContext context) {
        super(context);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {

        // TODO
    }
}
