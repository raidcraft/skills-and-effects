package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;

@Log(topic = "RCSkills:max-health")
@SkillInfo(
        value = "max-health"
)
public class MaxHealth extends AbstractSkill {

    private static final int DEFAULT_MAX_HEALTH = 20;

    public static class Factory implements SkillFactory<MaxHealth> {

        @Override
        public Class<MaxHealth> getSkillClass() {
            return MaxHealth.class;
        }
        @Override
        public @NonNull MaxHealth create(SkillContext context) {
            return new MaxHealth(context);
        }
    }

    protected MaxHealth(SkillContext context) {
        super(context);
    }

    int base = 20;

    private AttributeModifier attributeModifier;

    @Override
    public void load(ConfigurationSection config) {

        this.base = config.getInt("base", base);
        attributeModifier = new AttributeModifier(
                id(),
                alias(),
                config.getDouble("modifier", 0.0),
                AttributeModifier.Operation.valueOf(config.getString("operation", AttributeModifier.Operation.MULTIPLY_SCALAR_1.name()))
        );
    }

    @Override
    public void apply() {

        context().player().ifPresent(player -> {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attribute == null) return;
            attribute.setBaseValue(base);
            attribute.addModifier(attributeModifier);
        });
    }

    @Override
    public void remove() {

        context().player().ifPresent(player -> {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attribute == null) return;
            attribute.removeModifier(attributeModifier);
            attribute.setBaseValue(DEFAULT_MAX_HEALTH);
        });
    }
}
