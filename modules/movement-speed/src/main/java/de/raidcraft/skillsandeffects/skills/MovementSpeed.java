package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import de.raidcraft.skills.configmapper.ConfigOption;
import lombok.NonNull;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

@SkillInfo("movement-speed")
public class MovementSpeed extends AbstractSkill {

    public static class Factory implements SkillFactory<MovementSpeed> {

        @Override
        public Class<MovementSpeed> getSkillClass() {
            return MovementSpeed.class;
        }

        @Override
        public @NonNull MovementSpeed create(SkillContext context) {
            return new MovementSpeed(context);
        }

    }

    @ConfigOption
    private double modifier = 0.1;
    private double previousModifier = 0.1;

    protected MovementSpeed(SkillContext context) {
        super(context);
    }

    @Override
    public void apply() {

        context().player().ifPresent(player -> {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if (attribute == null) return;
            previousModifier = attribute.getBaseValue();
            attribute.setBaseValue(modifier);
        });
    }

    @Override
    public void remove() {

        context().player().ifPresent(player -> {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if (attribute == null) return;
            attribute.setBaseValue(previousModifier);
        });
    }
}
