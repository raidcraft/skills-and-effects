package de.raidcraft.skillsandeffects.skills;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.raidcraft.skills.ExecutionConfig;
import de.raidcraft.skills.ExecutionContext;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.entities.SkilledPlayer;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LookTeleportTest {

    private ServerMock server;
    private FriendlyMobs skill;
    private PlayerMock player;
    private SkilledPlayer skilledPlayer;

    protected SkillContext mockContext() {

        SkillContext context = mock(SkillContext.class);
        when(context.skilledPlayer()).thenReturn(skilledPlayer);
        when(context.player()).thenReturn(Optional.of(player));

        return context;
    }

    protected ExecutionContext mockExecution() {

        ExecutionContext context = mock(ExecutionContext.class);
        when(context.config()).thenReturn(new ExecutionConfig(new MemoryConfiguration(), 10, 0, 0, 0));
        when(context.player()).thenReturn(player);
        return context;
    }

    @BeforeEach
    void setUp() {

        server = MockBukkit.mock();
        player = server.addPlayer();

        skilledPlayer = mock(SkilledPlayer.class);

        skill = new FriendlyMobs(mockContext());
    }

    @AfterEach
    void tearDown() {

        MockBukkit.unmock();
    }
}