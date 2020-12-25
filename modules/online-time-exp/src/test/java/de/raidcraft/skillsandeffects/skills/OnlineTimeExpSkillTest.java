package de.raidcraft.skillsandeffects.skills;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import de.raidcraft.skills.SkillContext;
import de.raidcraft.skills.entities.DataStore;
import de.raidcraft.skills.entities.SkilledPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static de.raidcraft.skillsandeffects.skills.OnlineTimeExpSkill.calcLastPayout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OnlineTimeExpSkillTest {

    private OnlineTimeExpSkill skill;
    private SkilledPlayer player;
    private DataStore store;
    private User user;
    private boolean isAfk;
    private long afkSince;
    private long exp;

    @BeforeEach
    void setUp() {
        SkillContext context = mock(SkillContext.class);
        store = new DataStore();
        when(context.store()).thenReturn(store);
        skill = new OnlineTimeExpSkill(context);
        skill.exp = 10;
        skill.interval = 10;
        user = mock(User.class);
        Essentials essentials = mock(Essentials.class);
        when(essentials.getUser((UUID) null)).thenReturn(user);
        skill.essentials = essentials;

        player = mock(SkilledPlayer.class);
        when(context.skilledPlayer()).thenReturn(player);
        when(player.addExp(anyLong(), anyString())).then(invocation -> {
            exp += (long) invocation.getArgument(0);
            return invocation.getMock();
        });
    }

    private OnlineTimeExpSkill setup(long onlineTime) {

        skill.onlineSince = Instant.now().minus(onlineTime, ChronoUnit.SECONDS);

        return skill;
    }

    private void assertResult(long exp, long lastPayout) {

        assertExp(exp);
        assertThat(calcLastPayout(skill))
                .extracting(
                        OnlineTimeExpSkill.Result::getLastPayout
                ).isEqualTo(
                lastPayout
        );
    }

    private void assertExp(long amount) {

        assertThat(exp).isEqualTo(amount);
    }

    @Test
    @DisplayName("should calculate correct exp payout amount")
    void shouldCalculateCorrectPayoutAmount() {

        setup(30);

        skill.tick();

        assertResult(30, 30);
        assertExp(30);
    }

    @Test
    @DisplayName("should not payout first online time until interval is reached")
    void shouldNotPayoutFirstOnlineTimeUntilInterval() {

        setup(9);

        skill.tick();

        assertResult(0, 0);
        assertExp(0);
    }

    @Test
    @DisplayName("should payout single exp if online time is an exact match")
    void shouldPayoutSingleExpIfExactMatch() {

        setup(10);

        skill.tick();

        assertResult(10, 10);
        assertExp(10);
    }

    @Test
    @DisplayName("should keep rest online time and payout")
    void shouldKeepRestOnlineTime() {

        setup(15);

        skill.tick();

        assertResult(10, 10);
        assertExp(10);
    }

    @Test
    @DisplayName("should keep rest online time if multi payout")
    void shouldKeepResultOnlineTimeIfMultiple() {

        setup(54);

        skill.tick();

        assertResult(50, 50);
        assertExp(50);
    }

    @Test
    @DisplayName("should subtract afk time from online time")
    void shouldSubtractAfkTimeFromOnlineTime() {

        setup(30);
        when(user.isAfk()).thenReturn(true);
        when(user.getAfkSince()).thenReturn(Instant.now().minus(20, ChronoUnit.SECONDS).toEpochMilli());

        skill.tick();

        assertResult(10, 10);
        assertExp(10);
    }
}