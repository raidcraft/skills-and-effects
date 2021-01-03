package de.raidcraft.skillsandeffects.skills;

import com.google.common.base.Strings;
import de.raidcraft.skills.AbstractSkill;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.SkillFactory;
import de.raidcraft.skills.SkillInfo;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log(topic = "RCSkills:movement-speed")
@SkillInfo("movement-speed")
public class MovementSpeed extends AbstractSkill implements Listener {

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

    private AttributeModifier attributeModifier;
    private Location lastLocation;
    private State state;
    private State oldState;

    private final List<State> states = new ArrayList<>();

    protected MovementSpeed(SkillContext context) {
        super(context);
    }

    @Override
    public void load(ConfigurationSection config) {

        states.clear();
        List<String> states = config.getStringList("states");
        if (states.isEmpty() || states.contains("ALL")) {
            this.states.addAll(Arrays.asList(State.values()));
        } else {
            for (String name : states) {
                State state = State.fromString(name);
                if (state != null) {
                    this.states.add(state);
                } else {
                    log.warning("invalid movement state \"" + name + "\" in skill config " + context().configuredSkill().alias());
                }
            }
        }

        attributeModifier = new AttributeModifier(
                context().configuredSkill().id(),
                context().configuredSkill().alias(),
                config.getDouble("modifier", 0.1),
                AttributeModifier.Operation.valueOf(config.getString("operation", AttributeModifier.Operation.MULTIPLY_SCALAR_1.name()))
        );
    }

    @Override
    public void apply() {

        context().player().ifPresent(player -> {
            updateState(player);
            addSpeed(player);
        });
    }

    @Override
    public void remove() {

        context().player().ifPresent(this::removeSpeed);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {

        if (!hasMoved(event.getTo())) {
            return;
        }
        lastLocation = event.getTo();
        updateState(event.getPlayer());

        if (stateChanged()) {
            updateMovementSpeed(event.getPlayer());
        }
    }

    private void updateMovementSpeed(Player player) {

        if (states.contains(state)) {
            addSpeed(player);
        } else {
            removeSpeed(player);
        }
    }

    private void addSpeed(Player player) {

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attribute == null) return;
        attribute.addModifier(attributeModifier);
    }

    private void removeSpeed(Player player) {

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attribute == null) return;
        attribute.removeModifier(attributeModifier);
    }

    private boolean stateChanged() {

        return oldState != state;
    }

    private void updateState(Player player) {

        oldState = state;
        if (player.isFlying()) {
            state = State.FLYING;
        } else if (player.isInWater()) {
            state = State.IN_WATER;
        } else if (player.isSprinting()) {
            state = State.SPRINTING;
        } else if (player.isGliding()) {
            state = State.GLIDING;
        } else if (player.isSneaking()) {
            state = State.SNEAKING;
        } else {
            state = State.WALKING;
        }
    }

    private boolean hasMoved(Location location) {

        return lastLocation == null ||
                location.getBlockX() != lastLocation.getBlockX()
                || location.getBlockY() != lastLocation.getBlockY()
                || location.getBlockZ() != lastLocation.getBlockZ();
    }

    enum State {
        IN_WATER,
        FLYING,
        WALKING,
        SPRINTING,
        GLIDING,
        SNEAKING;

        public static State fromString(String state) {

            if (Strings.isNullOrEmpty(state)) return null;
            for (State value : values()) {
                if (value.name().equalsIgnoreCase(state)) {
                    return value;
                }
            }
            return null;
        }
    }
}
