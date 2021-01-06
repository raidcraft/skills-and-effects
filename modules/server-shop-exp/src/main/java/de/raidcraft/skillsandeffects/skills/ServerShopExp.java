package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.servershop.SoldtemsEvent;
import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import de.raidcraft.skills.configmapper.ConfigOption;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

@Log(topic = "RCSkills:server-shop-exp")
@SkillInfo(value = "server-shop-exp", depends = {"RCServerShop"})
public class ServerShopExp extends AbstractSkill implements Listener {

    public static class Factory implements SkillFactory<ServerShopExp> {

        @Override
        public Class<ServerShopExp> getSkillClass() {
            return ServerShopExp.class;
        }
        @Override
        public @NonNull ServerShopExp create(SkillContext context) {
            return new ServerShopExp(context);
        }
    }

    @ConfigOption
    String reason = "EXP für Item Verkauf";
    @ConfigOption
    double factor = 1.0d;
    Map<Material, Long> itemExpMap = new HashMap<>();

    protected ServerShopExp(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        itemExpMap.clear();

        ConfigurationSection items = config.getConfigurationSection("items");
        if (items == null) {
            items = config.createSection("items");
        }
        for (String key : items.getKeys(false)) {
            Material material = Material.matchMaterial(key);
            if (material == null) {
                log.warning("unknown material " + key + " in skill config of " + context().configuredSkill().alias());
            } else {
                itemExpMap.put(material, items.getLong(key, 0L));
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onServerShopSell(SoldtemsEvent event) {

        if (context().notApplicable(event.getPlayer())) return;

        long exp = itemExpMap.getOrDefault(event.getItem(), 0L);
        exp = Math.round((exp * event.getAmount()) * factor);

        if (exp > 0) {
            context().skilledPlayer().addExp(exp, reason);
        }
    }
}
