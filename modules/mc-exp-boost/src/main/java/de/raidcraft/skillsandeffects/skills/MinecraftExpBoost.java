package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

@Log(topic = "RCSkills:mc-exp-boost")
@SkillInfo(
        value = "mc-exp-boost"
)
public class MinecraftExpBoost extends AbstractSkill implements Listener {

    public static class Factory implements SkillFactory<MinecraftExpBoost> {

        @Override
        public Class<MinecraftExpBoost> getSkillClass() {
            return MinecraftExpBoost.class;
        }
        @Override
        public @NonNull MinecraftExpBoost create(SkillContext context) {
            return new MinecraftExpBoost(context);
        }
    }

    protected MinecraftExpBoost(SkillContext context) {
        super(context);
    }

    float factor = 1.1f;

    @Override
    public void load(ConfigurationSection config) {

        this.factor = (float) config.getDouble("factor", factor);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onExpGain(PlayerExpChangeEvent event) {

        if (notApplicable(event.getPlayer())) return;

        event.setAmount(Math.round(factor * event.getAmount()));
    }
}
