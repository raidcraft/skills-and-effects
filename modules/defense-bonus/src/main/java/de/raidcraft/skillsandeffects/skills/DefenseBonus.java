package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@Log(topic = "RCSkills:defense-bonus")
@SkillInfo(
        value = "defense-bonus"
)
public class DefenseBonus extends AbstractSkill implements Listener {

    public static class Factory implements SkillFactory<DefenseBonus> {

        @Override
        public Class<DefenseBonus> getSkillClass() {
            return DefenseBonus.class;
        }
        @Override
        public @NonNull DefenseBonus create(SkillContext context) {
            return new DefenseBonus(context);
        }
    }

    protected DefenseBonus(SkillContext context) {
        super(context);
    }

    float modifier = 1.0f;

    @Override
    public void load(ConfigurationSection config) {

        this.modifier = (float) config.getDouble("modifier", 1.0);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (notApplicable((OfflinePlayer) event.getEntity())) return;

        event.setDamage(event.getDamage() * modifier);
    }
}
