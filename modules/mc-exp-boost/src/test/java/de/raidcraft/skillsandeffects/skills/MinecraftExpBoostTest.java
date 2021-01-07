package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.entities.DataStore;
import de.raidcraft.skills.entities.SkilledPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MinecraftExpBoostTest {

    private MinecraftExpBoost skill;
    private DataStore store;
    private SkilledPlayer player;
    private UUID playerId;

    @BeforeEach
    void setUp() {

        player = mock(SkilledPlayer.class);
        playerId = UUID.randomUUID();
        when(player.id()).thenReturn(playerId);
        store = new DataStore();

        skill = new MinecraftExpBoost(mockContext());
        skill.factor = 1.5f;
    }

    private SkillContext mockContext() {

        SkillContext context = mock(SkillContext.class);
        when(context.skilledPlayer()).thenReturn(player);
        when(context.store()).thenReturn(store);
        when(context.applicable(any())).thenAnswer(invocation -> playerId.equals(((OfflinePlayer) invocation.getArgument(0)).getUniqueId()));
        when(context.notApplicable(any())).thenAnswer(invocation -> !playerId.equals(((OfflinePlayer) invocation.getArgument(0)).getUniqueId()));

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
    @DisplayName("should increase the exp with the given factor")
    void shouldIncreaseTheReceivedExpByTheGivenFactor() {

        PlayerExpChangeEvent event = new PlayerExpChangeEvent(playerMock(), 10);
        skill.onExpGain(event);

        assertThat(event.getAmount()).isEqualTo(15);
    }

    @Test
    @DisplayName("should not increase exp of different players")
    void shouldNotIncreaseExpOfDifferentPlayers() {

        PlayerExpChangeEvent event = new PlayerExpChangeEvent(randomPlayer(), 10);
        skill.onExpGain(event);

        assertThat(event.getAmount()).isEqualTo(10);
    }
}