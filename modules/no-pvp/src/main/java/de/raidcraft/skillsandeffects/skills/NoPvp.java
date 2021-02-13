package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.*;
import de.raidcraft.skills.text.text.Component;
import de.raidcraft.skills.util.TimeUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log(topic = "RCSkills:no-pvp")
@SkillInfo(
        value = "no-pvp"
)
public class NoPvp extends AbstractSkill implements Listener {

    public static class Factory implements SkillFactory<NoPvp> {

        @Override
        public Class<NoPvp> getSkillClass() {
            return NoPvp.class;
        }
        @Override
        public @NonNull NoPvp create(SkillContext context) {
            return new NoPvp(context);
        }
    }

    private final Map<UUID, Instant> lastMessages = new HashMap<>();
    private Instant lastAttack = Instant.now();

    String attackerMessage = "Du kannst {player} nicht angreifen der er den Skill \"{skill}\" hat, welcher PvP verhindert.";
    String message = "Du kannst {player} nicht angreifen da der Skill \"{skill}\" PvP verhindert.";
    long messageCooldown;

    protected NoPvp(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        attackerMessage = config.getString("attacker_message", attackerMessage);
        message = config.getString("message", message);
        messageCooldown = TimeUtil.parseTimeAsTicks(config.getString("message_cooldown", "10s"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (notApplicable((OfflinePlayer) event.getDamager())
                && notApplicable((OfflinePlayer) event.getEntity())) {
            return;
        }

        event.setCancelled(true);
        if (applicable((OfflinePlayer) event.getDamager()) && isSendMessage(event.getDamager())) {
            message = message.replace("{skill}", context().configuredSkill().name())
                    .replace("{alias}", context().configuredSkill().alias())
                    .replace("{player}", ((Player) event.getEntity()).getDisplayName());
            Messages.send(event.getDamager(), Component.text(message));
            lastAttack = Instant.now();
        } else if (notApplicable((OfflinePlayer) event.getDamager()) && isSendMessage(event.getDamager())) {
            message = message.replace("{skill}", context().configuredSkill().name())
                    .replace("{alias}", context().configuredSkill().alias())
                    .replace("{player}", ((Player) event.getEntity()).getDisplayName());
            Messages.send(event.getDamager(), Component.text(attackerMessage));
            lastMessages.put(event.getDamager().getUniqueId(), Instant.now());
        }
    }

    private boolean isSendMessage(Entity entity) {

        if (entity instanceof OfflinePlayer && applicable((OfflinePlayer) entity)) {
            return Instant.now().isAfter(lastAttack.plus(messageCooldown, ChronoUnit.MILLIS));
        } else if (entity instanceof OfflinePlayer && notApplicable((OfflinePlayer) entity)) {
            return Instant.now().isAfter(lastMessages.getOrDefault(entity.getUniqueId(),
                        Instant.now().minus(messageCooldown, ChronoUnit.MILLIS))
                    .plus(messageCooldown, ChronoUnit.MILLIS));
        }

        return false;
    }
}
