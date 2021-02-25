package de.raidcraft.skillsandeffects.skills;

import com.google.common.base.Strings;
import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import de.raidcraft.skills.util.PseudoRandomGenerator;
import de.raidcraft.skills.util.TimeUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.ChatColor;
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

    String message = "Der Skill {skill} hat verhindert dass du dein Inventar verlierst. Cooldown: {cooldown}.";
    boolean keepInventory = true;
    boolean keepLevel = false;
    private PseudoRandomGenerator random;

    protected KeepInventory(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        message = config.getString("message", message);
        keepInventory = config.getBoolean("keep_inventory", keepInventory);
        keepLevel = config.getBoolean("keep_level", keepLevel);
        random = PseudoRandomGenerator.create((float) config.getDouble("chance", 0.1));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (notApplicable(event.getEntity())) return;
        if (isOnCooldown()) return;

        if (random.hit()) {
            event.setKeepInventory(keepInventory);
            event.setKeepLevel(keepLevel);
            if (keepInventory) event.getDrops().clear();
            if (keepLevel) event.setDroppedExp(0);
            lastUsed(Instant.now());

            if (!Strings.isNullOrEmpty(message)) {
                message = message.replace("{skill}", name())
                        .replace("{cooldown}", TimeUtil.formatTime(getRemainingCooldown()))
                        .replace("{alias}", alias());
                event.getEntity().sendMessage(ChatColor.GREEN + message);
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
