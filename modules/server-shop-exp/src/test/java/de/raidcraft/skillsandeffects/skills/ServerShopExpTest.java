package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.servershop.SellItemEvent;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.entities.SkilledPlayer;
import org.bukkit.Material;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ServerShopExpTest {

    private ServerShopExp skill;
    private SkilledPlayer player;

    protected SkillContext mockContext() {

        SkillContext context = mock(SkillContext.class);
        when(context.skilledPlayer()).thenReturn(player);

        return context;
    }

    @BeforeEach
    void setUp() {

        player = mock(SkilledPlayer.class);

        MemoryConfiguration cfg = new MemoryConfiguration();
        cfg.set("items.diamond", 10);
        skill = new ServerShopExp(mockContext());
        skill.load(cfg);
    }

    @Test
    @DisplayName("should add correct exp per item to player")
    void shouldAddCorrectExpToPlayer() {

        skill.onServerShopSell(new SellItemEvent(Material.DIAMOND, 10));

        verify(player, times(1)).addExp(eq(100L), anyString());
    }

    @Test
    @DisplayName("should not add exp for items that are not configured")
    void shouldNotAddExpForNonConfiguredItems() {

        skill.onServerShopSell(new SellItemEvent(Material.DIRT, 100));

        verify(player, never()).addExp(anyLong(), anyString());
    }

    @Test
    @DisplayName("should add in factor when adding exp")
    void shouldCalculateFactor() {

        skill.factor = 2.0D;

        skill.onServerShopSell(new SellItemEvent(Material.DIAMOND, 10));

        verify(player, times(1)).addExp(eq(200L), anyString());
    }

    @Test
    @DisplayName("should not add exp if the amount is below zero")
    void shouldNotAddExpWhenExpIsBelowZero() {

        skill.onServerShopSell(new SellItemEvent(Material.DIAMOND, 0));

        verify(player, never()).addExp(anyLong(), anyString());
    }
}