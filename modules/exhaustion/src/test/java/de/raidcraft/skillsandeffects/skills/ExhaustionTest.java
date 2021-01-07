package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.entities.DataStore;
import de.raidcraft.skills.entities.SkilledPlayer;
import lombok.Data;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExhaustionTest {

    private Exhaustion skill;
    private DataStore store;
    private SkilledPlayer player;
    private Player bukkitPlayer;
    private UUID playerId;

    private Wrapper wrapper;

    @BeforeEach
    void setUp() {

        wrapper = new Wrapper();

        player = mock(SkilledPlayer.class);
        playerId = UUID.randomUUID();
        when(player.id()).thenReturn(playerId);

        bukkitPlayer = playerMock();
        when(bukkitPlayer.getExhaustion()).thenAnswer(invocation -> wrapper.getExhaustion());
        doAnswer(invocation -> {
            wrapper.setExhaustion(invocation.getArgument(0));
            return invocation;
        }).when(bukkitPlayer).setExhaustion(anyFloat());

        when(player.bukkitPlayer()).thenReturn(Optional.of(bukkitPlayer));

        store = new DataStore();

        skill = new Exhaustion(mockContext());
    }

    private SkillContext mockContext() {

        SkillContext context = mock(SkillContext.class);
        when(context.skilledPlayer()).thenReturn(player);
        when(context.store()).thenReturn(store);
        when(context.applicable(any())).thenAnswer(invocation -> playerId.equals(((OfflinePlayer) invocation.getArgument(0)).getUniqueId()));
        when(context.notApplicable(any())).thenAnswer(invocation -> !playerId.equals(((OfflinePlayer) invocation.getArgument(0)).getUniqueId()));
        when(context.player()).thenReturn(Optional.of(bukkitPlayer));

        return context;
    }

    private Player playerMock() {

        Player mock = mock(Player.class);
        when(mock.getUniqueId()).thenReturn(playerId);
        return mock;
    }

    private Player randomPlayer() {

        Player mock = mock(Player.class);
        when(mock.getUniqueId()).thenReturn(UUID.randomUUID());
        return mock;
    }

    @Test
    @DisplayName("should slow down player exhaustion by the defined amount each tick")
    void shouldModifyThePlayerExhaustionLevel() {

        skill.modifier = 0.5f;

        wrapper.setExhaustion(2f);
        skill.tick();

        assertThat(wrapper.getExhaustion()).isEqualTo(1f);
    }

    @Test
    @DisplayName("should calculate exhaustion switch from 4 -> 0 correctly")
    void shouldIgnoreExhaustionSwitchToZero() {

        skill.modifier = 0.5f;

        wrapper.setExhaustion(3.5f);
        skill.tick();
        wrapper.setExhaustion(0.2f);
        skill.tick();

        assertThat(wrapper.getExhaustion()).isEqualTo(0.2f);
        skill.lastExhaustionLevel = 0.2f;
    }

    @Data
    static class Wrapper {

        private float exhaustion = 0f;
    }
}