package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import de.raidcraft.skills.configmapper.ConfigOption;
import lombok.NonNull;
import lombok.extern.java.Log;
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

    @ConfigOption
    float modifier = 1.0f;

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {

        event.setDamage(event.getDamage() * modifier);
    }
}
