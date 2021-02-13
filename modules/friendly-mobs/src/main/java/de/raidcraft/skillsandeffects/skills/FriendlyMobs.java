package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Log(topic = "RCSkills:friendly-mobs")
@SkillInfo(value = "friendly-mobs")
public class FriendlyMobs extends AbstractSkill implements Listener {

    public static class Factory implements SkillFactory<FriendlyMobs> {

        @Override
        public Class<FriendlyMobs> getSkillClass() {
            return FriendlyMobs.class;
        }

        @Override
        public @NonNull FriendlyMobs create(SkillContext context) {
            return new FriendlyMobs(context);
        }

    }

    // Track last time the player attacks a mob
    private Instant lastAttack = Instant.MIN;

    // Time after attacked mob will stop re-targeting player
    private long timeout = 10;
    List<EntityType> mobs = new ArrayList<>();

    FriendlyMobs(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        this.timeout = config.getLong("timeout", 10);

        mobs.clear();
        List<String> configuredMobs = config.getStringList("mobs");
        for (String name : configuredMobs) {
            try {
                EntityType mob = EntityType.valueOf(name);
                this.mobs.add(mob);
            } catch(IllegalArgumentException e) {
                log.warning("invalid mobs type \"" + name + "\" in skill config " + alias());
            }
        }
    }

    @EventHandler
    public void onLivingEntityTarget(EntityTargetLivingEntityEvent event) {

        // Ignore Entity <-> Entity events
        if(!(event.getTarget() instanceof Player)) return;

        // Skip if damaging entity is not in mob list
        if(!mobs.contains(event.getEntity().getType())) return;

        Player targetPlayer = (Player)event.getTarget();

        // Skip if skill is not active for target player
        if(context().notApplicable(targetPlayer)) return;

        // Check if player attacked within timeout
        if(Instant.now().isBefore(lastAttack.plus(timeout, ChronoUnit.SECONDS))) {
            return;
        }

        // Prevent mob from attacking player
        event.setCancelled(true);
    }

    @EventHandler
    public void onLivingEntityDamage(EntityDamageByEntityEvent event) {

        // Ignore Entity <-> Entity events
        if(!(event.getDamager() instanceof Player)) return;

        // Skip if damaged entity is not in mob list
        if(!mobs.contains(event.getEntity().getType())) return;

        Player targetPlayer = (Player)event.getDamager();

        // Skip if skill is not active for target player
        if(context().notApplicable(targetPlayer)) return;

        // Save time of last attack
        lastAttack = Instant.now();
    }
}
