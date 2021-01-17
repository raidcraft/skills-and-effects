package de.raidcraft.skillsandeffects.skills;

import com.google.common.base.Strings;
import de.raidcraft.skills.*;
import de.raidcraft.skills.configmapper.ConfigOption;
import de.raidcraft.skills.text.text.format.NamedTextColor;
import de.raidcraft.skills.util.PseudoRandomGenerator;
import de.raidcraft.skills.util.TimeUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.raidcraft.skills.text.text.Component.text;

@Log(topic = "RCSkills:rebirth")
@SkillInfo(
        value = "rebirth"
)
public class Rebirth extends AbstractSkill implements Listener {

    public static class Factory implements SkillFactory<Rebirth> {

        @Override
        public Class<Rebirth> getSkillClass() {
            return Rebirth.class;
        }
        @Override
        public @NonNull Rebirth create(SkillContext context) {
            return new Rebirth(context);
        }
    }

    @ConfigOption
    String message = "Dein Skill {skill} hat tödlichen Schaden verhindert und du wurdest um {heal} Leben geheilt. Cooldown: {cooldown}";
    @ConfigOption
    double heal = 20;
    @ConfigOption
    boolean healInPercent = false;
    private Set<EntityDamageEvent.DamageCause> ignoredCauses = new HashSet<>();

    private PseudoRandomGenerator random;

    protected Rebirth(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        this.random = PseudoRandomGenerator.create((float) config.getDouble("chance", 0.1));
        List<String> stringList = config.getStringList("ignored-causes");
        if (stringList.isEmpty()) {
            ignoredCauses.add(EntityDamageEvent.DamageCause.VOID);
            ignoredCauses.add(EntityDamageEvent.DamageCause.SUFFOCATION);
            ignoredCauses.add(EntityDamageEvent.DamageCause.SUICIDE);
            ignoredCauses.add(EntityDamageEvent.DamageCause.CUSTOM);
        } else {
            for (String cause : stringList) {
                try {
                    ignoredCauses.add(EntityDamageEvent.DamageCause.valueOf(cause.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    log.severe("invalid ignored damage cause " + cause + " in config of " + alias() + " (" + id() + ")");
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (ignoredCauses.contains(event.getCause())) return;
        if (notApplicable((OfflinePlayer) event.getEntity())) return;

        Player player = (Player) event.getEntity();

        double damage = event.getFinalDamage();
        double health = player.getHealth() - damage;
        boolean isDeadly = health <= 0;

        if (!isDeadly) return;
        if (context().isOnCooldown()) return;
        if (health < 0) health = 0;

        if (random.hit()) {
            double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (healInPercent) {
                heal = maxHealth * heal;
            }

            health += heal;
            if (health > maxHealth) health = maxHealth;

            player.setHealth(health);
            lastUsed(Instant.now());
            event.setCancelled(true);

            if (!Strings.isNullOrEmpty(message)) {
                message = message.replace("{skill}", context().configuredSkill().name())
                        .replace("{heal}", heal + "")
                        .replace("{cooldown}", TimeUtil.formatTime(getRemainingCooldown()))
                        .replace("{alias}", context().configuredSkill().alias());
                Messages.send(player, text(message, NamedTextColor.GREEN));
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
