package de.raidcraft.skillsandeffects.skills;

import com.gmail.nossr50.datatypes.experience.XPGainReason;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.entities.DataStore;
import de.raidcraft.skills.entities.SkilledPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class McmmoExpTest {

    private McmmoExp skill;
    private DataStore store;
    private SkilledPlayer player;
    private final PrimarySkillType skillType = null;
    private final XPGainReason reason = XPGainReason.UNKNOWN;
    private long exp;

    @BeforeEach
    void setUp() {

        SkillContext context = mock(SkillContext.class);
        skill = new McmmoExp(context);
        skill.reasons.add(XPGainReason.UNKNOWN);
        store = new DataStore();
        player = mock(SkilledPlayer.class);
        when(context.store()).thenReturn(store);
        when(context.skilledPlayer()).thenReturn(player);
        when(player.addExp(anyLong(), anyString())).then(invocation -> {
            //noinspection RedundantCast
            exp += (long) invocation.getArgument(0);
            return invocation.getMock();
        });
    }

    private McMMOPlayerXpGainEvent event(float exp) {

        McMMOPlayerXpGainEvent event = mock(McMMOPlayerXpGainEvent.class);
        when(event.getRawXpGained()).thenReturn(exp);
        when(event.getXpGainReason()).thenReturn(reason);
        when(event.getSkill()).thenReturn(skillType);

        return event;
    }

    private void assertExpGain(int times, long amount) {

        verify(player, times(times)).addExp(eq(amount), anyString());
    }

    private void assertExpGain(long amount) {

        assertExpGain(1, amount);
    }

    private void assertExp(long amount) {

        assertThat(exp).isEqualTo(amount);
    }

    @Test
    @DisplayName("should give player configured base exp")
    void shouldAwardRawBaseExp() {

        skill.base = 100;

        skill.onMcmmoExpGain(event(10));

        assertExpGain(100L);
    }

    @Test
    @DisplayName("should not exceed max exp")
    void shouldNotExceedMaxExp() {

        skill.factor = 2;
        skill.max = 60;

        skill.onMcmmoExpGain(event(50));

        assertExpGain(60L);
    }

    @Test
    @DisplayName("should not exceed max exp per hour")
    void shouldNotExceedExpPerHour() {

        skill.maxPerHour = 100;
        skill.base = 50;

        skill.onMcmmoExpGain(event(10));
        skill.onMcmmoExpGain(event(10));

        assertExpGain(2, 50);

        skill.onMcmmoExpGain(event(10));

        assertExpGain(2, 50);

        assertExp(100L);
    }

    @Test
    @DisplayName("should reset max per hour tracker after hour is over")
    void shouldResetMaxPerHourAfterHour() {

        skill.maxPerHour = 50;
        skill.base = 30;

        skill.onMcmmoExpGain(event(10));
        skill.onMcmmoExpGain(event(10));

        assertExp(50);

        skill.lastHourStart = Instant.now()
                .minus(1, ChronoUnit.HOURS)
                .minus(1, ChronoUnit.MINUTES);

        skill.onMcmmoExpGain(event(10));

        assertExp(80);
    }

    @Test
    @DisplayName("should set minimum exp gain if min > 0")
    void shouldSetMinimumExpGain() {

        skill.factor = 2;
        skill.min = 30;

        skill.onMcmmoExpGain(event(10));

        assertExp(30);
    }

    @Test
    void shouldPayoutAfterTreshholdIsReached() {

        skill.factor = 0.1f;
        skill.payoutTreshhold = 10;

        skill.onMcmmoExpGain(event(10));

        assertExp(0);
        assertThat(skill.exp).isEqualTo(1f);

        for (int i = 0; i < 10; i++) {
            skill.onMcmmoExpGain(event(10));
        }

        assertExp(10);
        assertThat(skill.exp).isEqualTo(1f);
    }
}