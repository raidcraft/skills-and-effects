package de.raidcraft.skillsandeffects.skills;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import de.raidcraft.skills.*;
import io.ebean.annotation.Transactional;
import lombok.NonNull;
import lombok.Value;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.time.Instant;

@SkillInfo(value = "online-time-exp")
public class OnlineTimeExpSkill extends AbstractSkill implements Periodic {

    public static class Factory implements SkillFactory<OnlineTimeExpSkill> {

        @Override
        public Class<OnlineTimeExpSkill> getSkillClass() {
            return OnlineTimeExpSkill.class;
        }

        @Override
        public @NonNull OnlineTimeExpSkill create(SkillContext context) {
            return new OnlineTimeExpSkill(context);
        }

    }

    static final String STORAGE_KEY_ONLINE_TIME = "online-time";
    static final String STORAGE_KEY_AFK_TIME = "afk-time";
    static final String STORAGE_KEY_LAST_ONLINE = "last-online";
    static final String STORAGE_KEY_LAST_PAYOUT = "last-payout";

    long interval = 900;
    int exp = 25;
    private String reason = "EXP für Onlinezeit";

    Instant onlineSince;
    Instant afkSince;
    long onlineTime;
    long afkTime;
    long lastPayOut;
    Essentials essentials;

    protected OnlineTimeExpSkill(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        this.interval = config.getLong("interval", interval);
        this.exp = config.getInt("exp", exp);
        this.reason = config.getString("reason", reason);

        Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentials != null) {
            this.essentials = (Essentials) essentials;
        }
    }

    @Override
    public void apply() {

        onlineSince = Instant.now();
        lastPayOut = context().store().get(STORAGE_KEY_LAST_PAYOUT, long.class, 0L);
        onlineTime = context().store().get(STORAGE_KEY_ONLINE_TIME, long.class, 0L);
        afkTime = context().store().get(STORAGE_KEY_AFK_TIME, long.class, 0L);
    }

    @Override
    public void remove() {

        updateOnlineTime();
        context().store()
                .set(STORAGE_KEY_ONLINE_TIME, onlineTime)
                .set(STORAGE_KEY_LAST_ONLINE, Instant.now())
                .set(STORAGE_KEY_LAST_PAYOUT, lastPayOut)
                .set(STORAGE_KEY_AFK_TIME, afkTime)
                .save();
    }

    @Override
    @Transactional
    public void tick() {

        if (essentials != null) {
            User user = essentials.getUser(context().skilledPlayer().id());
            if (user.isAfk() && afkSince == null) {
                afkSince = Instant.ofEpochMilli(user.getAfkSince());
            } else if (!user.isAfk() && afkSince != null) {
                afkTime += Instant.now().getEpochSecond() - afkSince.getEpochSecond();
                afkSince = null;
            }
        }

        updateOnlineTime();
        Result payout = calcLastPayout(this);
        if (payout.count > 0) {
            context().skilledPlayer().addExp(payout.exp, reason).save();
            setLastPayout(payout.lastPayout);
        }
    }

    private void setLastPayout(long time) {

        lastPayOut = time;
    }

    private void updateOnlineTime() {

        Instant now = Instant.now();
        if (afkSince != null) {
            afkTime += now.getEpochSecond() - afkSince.getEpochSecond();
            afkSince = now;
        }

        onlineTime += now.getEpochSecond() - onlineSince.getEpochSecond();
        onlineSince = now;
    }

    static Result calcLastPayout(OnlineTimeExpSkill skill) {

        long onlineTime = skill.onlineTime - skill.afkTime;
        if (skill.lastPayOut == 0 && onlineTime < skill.interval) {
            return new Result(0, 0, 0);
        }

        long onlineTimeSince = onlineTime - skill.lastPayOut;
        long count = (onlineTimeSince / skill.interval);
        long rest = onlineTimeSince % skill.interval;
        return new Result(count, skill.exp * count, onlineTime - rest);
    }

    @Value
    static class Result {

        long count;
        long exp;
        long lastPayout;
    }
}
