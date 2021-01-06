package de.raidcraft.skillsandeffects.skills;

import com.gmail.nossr50.datatypes.experience.XPGainReason;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import de.raidcraft.skills.configmapper.ConfigOption;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Log(topic = "RCSkills:mmcmmo-exp")
@SkillInfo(value = "mcmmo-exp", depends = {"mcMMO"})
public class McmmoExp extends AbstractSkill implements Listener {

    private static final String LAST_HOUR_START = "last-hour-start";
    private static final String LAST_HOUR_EXP_GAIN = "last-hour-exp-gain";
    private static final String TOTAL_EXP_GAIN = "total-exp-gain";
    private static final String TOTAL_MCMMO_EXP = "total-mcmmo-exp-gain";
    private static final String LAST_HOUR_MCMMO_EXP_GAIN = "last-hour-mcmmo-exp-gain";

    public static class Factory implements SkillFactory<McmmoExp> {
        @Override
        public Class<McmmoExp> getSkillClass() {
            return McmmoExp.class;
        }

        @Override
        public @NonNull McmmoExp create(SkillContext context) {
            return new McmmoExp(context);
        }
    }

    @ConfigOption
    String reason = "mcMMO EXP";
    @ConfigOption
    int base = 0;
    @ConfigOption
    double factor = 0;
    @ConfigOption
    int min = 0;
    @ConfigOption
    int max = -1;
    @ConfigOption
    int maxPerHour = -1;
    Set<XPGainReason> reasons = new HashSet<>();
    Set<PrimarySkillType> skills = new HashSet<>();
    Instant lastHourStart = Instant.now();
    float lastHourMcmmoExp;
    float totalMcmmoExp;
    long totalGainedExp;
    long lastHourGainedExp;

    McmmoExp(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        if (config.isSet("reasons")) {
            for (String key : config.getStringList("reasons")) {
                XPGainReason reason = XPGainReason.getXPGainReason(key);
                if (reason != null) {
                    reasons.add(reason);
                } else {
                    log.warning("invalid mcMMO EXP gain reason " + key + " in skill config of " + context().configuredSkill().alias());
                }
            }
        } else {
            reasons.addAll(Arrays.asList(XPGainReason.values()));
        }

        if (config.isSet("skills")) {
            for (String key : config.getStringList("skills")) {
                PrimarySkillType skill = PrimarySkillType.getSkill(key);
                if (skill != null) {
                    skills.add(skill);
                } else {
                    log.warning("invalid mcMMO skill " + key + " in skill config of " + context().configuredSkill().alias());
                }
            }
        } else {
            skills.addAll(Arrays.asList(PrimarySkillType.values()));
        }
    }

    @Override
    public void apply() {

        lastHourStart = context().store().get(LAST_HOUR_START, Instant.class, Instant.now());
        totalGainedExp = context().store().get(TOTAL_EXP_GAIN, long.class, 0L);
        lastHourGainedExp = context().store().get(LAST_HOUR_EXP_GAIN, long.class, 0L);
        lastHourMcmmoExp = context().store().get(LAST_HOUR_MCMMO_EXP_GAIN, float.class, 0f);
        totalMcmmoExp = context().store().get(TOTAL_MCMMO_EXP, float.class, 0f);
    }

    @Override
    public void remove() {

        context().store()
                .set(LAST_HOUR_START, lastHourStart)
                .set(TOTAL_EXP_GAIN, totalGainedExp)
                .set(LAST_HOUR_EXP_GAIN, lastHourGainedExp)
                .set(LAST_HOUR_MCMMO_EXP_GAIN, lastHourMcmmoExp)
                .set(TOTAL_MCMMO_EXP, totalMcmmoExp)
                .save();
    }

    @EventHandler(ignoreCancelled = true)
    public void onMcmmoExpGain(McMMOPlayerXpGainEvent event) {

        if (context().notApplicable(event.getPlayer())) return;

        if (event.getXpGainReason() != null && !reasons.contains(event.getXpGainReason())) {
            return;
        }

        if (event.getSkill() != null && !skills.contains(event.getSkill())) {
            return;
        }

        if (event.getRawXpGained() <= 0f) return;

        if (Instant.now().isAfter(lastHourStart.plus(1, ChronoUnit.HOURS))) {
            lastHourStart = Instant.now();
            lastHourMcmmoExp = 0f;
            lastHourGainedExp = 0L;
        }

        float mcmmoExp = event.getRawXpGained();
        totalMcmmoExp += mcmmoExp;
        lastHourMcmmoExp += mcmmoExp;

        long exp = calculateExpGain(mcmmoExp);
        if (exp > 0) {
            totalGainedExp += exp;
            lastHourGainedExp += exp;
            context().skilledPlayer()
                    .addExp(exp, reason + " (" + event.getXpGainReason().name().toLowerCase() + ")");
        }
    }

    long calculateExpGain(float mcMmoExp) {

        long exp = base;
        exp += mcMmoExp * factor;
        if (min > 0 && exp < min) {
            exp = min;
        } else if (max > 0 && exp > max) {
            exp = max;
        }

        if (maxPerHour < 0) {
            return exp;
        }

        if (lastHourGainedExp + exp > maxPerHour) {
            return maxPerHour - lastHourGainedExp;
        }

        return exp;
    }
}
