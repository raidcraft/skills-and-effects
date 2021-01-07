package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.Periodic;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import de.raidcraft.skills.configmapper.ConfigOption;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.event.Listener;

@Log(topic = "RCSkills:exhaustion")
@SkillInfo(
        value = "exhaustion",
        taskInterval = 1L
)
public class Exhaustion extends AbstractSkill implements Listener, Periodic {

    public static class Factory implements SkillFactory<Exhaustion> {

        @Override
        public Class<Exhaustion> getSkillClass() {
            return Exhaustion.class;
        }

        @Override
        public @NonNull Exhaustion create(SkillContext context) {
            return new Exhaustion(context);
        }
    }

    // taken from the minecraft wiki: https://minecraft.gamepedia.com/Hunger#Mechanics
    //
    // foodExhaustionLevel: The player's current exhaustion level, which determines how fast the saturation level depletes.
    // Its value is increased by the player's actions (see Exhaustion level increase for specific values).
    // The initial value is zero. When it reaches the maximum value of 4, it resets to zero and one point is subtracted from foodSaturationLevel.
    //
    // foodSaturationLevel: The player's current saturation level, which determines how fast the hunger level depletes and is controlled by the kinds of food the player has eaten.
    // Its maximum value always equals foodLevel's value and decreases with the hunger level. Its initial value on world creation is 5.
    //
    // this means we need to keep track of the difference in exhaustion levels and adjust the changes based on our modifier
    // the best is if we do this every tick, which means we need to take high care of performance

    @ConfigOption
    float modifier = 0.9f;
    float lastExhaustionLevel = 0f;

    protected Exhaustion(SkillContext context) {
        super(context);
    }

    @Override
    public void apply() {

        context().player().ifPresent(player -> lastExhaustionLevel = player.getExhaustion());
    }

    @Override
    public void tick() {

        context().player().ifPresent(player -> {
            float exhaustion = player.getExhaustion();

            if (exhaustion == lastExhaustionLevel) return;

            // we intentionally ignore the switch from 4 -> 0 because then
            // we would need modify the saturation and the hunger level as well
            // the players won't really notice this as long as the task interval is very low (one tick at best)
            //
            // setting a value to higher than 4f is also not a problem:
            // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/net/minecraft/server/FoodMetaData.java?at=a49a35834968612e3c2cc30495459a0d1f04b6cf#34
            if (exhaustion > lastExhaustionLevel) {
                float diff = exhaustion - lastExhaustionLevel;
                exhaustion = lastExhaustionLevel + (diff * modifier);
                player.setExhaustion(exhaustion);
            }

            lastExhaustionLevel = exhaustion;
        });
    }
}
