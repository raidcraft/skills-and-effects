package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.entities.DataStore;
import de.raidcraft.skills.entities.SkilledPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefenseBonusTest {

    private DefenseBonus skill;
    private DataStore store;
    private SkilledPlayer player;
    private UUID playerId;

    @BeforeEach
    void setUp() {

        player = mock(SkilledPlayer.class);
        playerId = UUID.randomUUID();
        when(player.id()).thenReturn(playerId);
        store = new DataStore();

        MemoryConfiguration cfg = new MemoryConfiguration();
        // TODO: set your skill config here
        // cfg.set("my-cfg-property", 10);
        skill = new DefenseBonus(mockContext());
        skill.load(cfg);
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
}