package de.raidcraft.skillsandeffects.skills;

import com.google.common.base.Strings;
import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.Messages;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import de.raidcraft.skills.configmapper.ConfigOption;
import de.raidcraft.skills.text.text.Component;
import de.raidcraft.skills.text.text.format.NamedTextColor;
import de.raidcraft.skills.util.PseudoRandomGenerator;
import de.raidcraft.skills.util.TimeUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.Instant;

@Log(topic = "RCSkills:keep-inventory")
@SkillInfo(
        value = "keep-inventory"
)
public class KeepInventory extends AbstractSkill implements Listener {

    public static class Factory implements SkillFactory<KeepInventory> {

        @Override
        public Class<KeepInventory> getSkillClass() {
            return KeepInventory.class;
        }
        @Override
        public @NonNull KeepInventory create(SkillContext context) {
            return new KeepInventory(context);
        }
    }

    @ConfigOption
    String message = "Der Skill {skill} hat verhindert dass du dein Inventar verlierst. Cooldown: {cooldown}.";
    @ConfigOption
    boolean keepInventory = true;
    @ConfigOption
    boolean keepLevel = false;
    private PseudoRandomGenerator random;

    protected KeepInventory(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        random = PseudoRandomGenerator.create((float) config.getDouble("chance", 0.1));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (notApplicable(event.getEntity())) return;
        if (isOnCooldown()) return;

        if (random.hit()) {
            event.setKeepInventory(keepInventory);
            event.setKeepLevel(keepLevel);
            lastUsed(Instant.now());

            if (!Strings.isNullOrEmpty(message)) {
                message = message.replace("{skill}", context().configuredSkill().name())
                        .replace("{cooldown}", TimeUtil.formatTime(getRemainingCooldown()))
                        .replace("{alias}", context().configuredSkill().alias());
                Messages.send(event.getEntity(), Component.text(message, NamedTextColor.GREEN));
            }
        }
    }

    @Override
    public void apply() {

        random.reset();
    }

    @Override
    public void remove() {

        random.reset();
    }
}
