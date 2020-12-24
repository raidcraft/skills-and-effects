package de.raidcraft.skillsandeffects.skills;

import de.raidcraft.skills.SkillContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static de.raidcraft.skillsandeffects.skills.OnlineTimeExpSkill.calcLastPayout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class OnlineTimeExpSkillTest {

    private OnlineTimeExpSkill skill;

    @BeforeEach
    void setUp() {
        skill = new OnlineTimeExpSkill(mock(SkillContext.class));
        skill.exp = 10;
        skill.interval = 10;
    }

    private void assertResult(long count, long exp, long lastPayout) {

        assertThat(calcLastPayout(skill))
                .extracting(
                        OnlineTimeExpSkill.Result::getCount,
                        OnlineTimeExpSkill.Result::getExp,
                        OnlineTimeExpSkill.Result::getLastPayout
                ).contains(
                count,
                exp,
                lastPayout
        );
    }

    @Test
    @DisplayName("should calculate correct exp payout amount")
    void shouldCalculateCorrectPayoutAmount() {

        skill.onlineTime = 30;
        assertResult(3, 30, 30);
    }

    @Test
    @DisplayName("should not payout first online time until interval is reached")
    void shouldNotPayoutFirstOnlineTimeUntilInterval() {

        skill.onlineTime = 9;
        assertResult(0, 0, 0);
    }

    @Test
    @DisplayName("should payout single exp if online time is an exact match")
    void shouldPayoutSingleExpIfExactMatch() {

        skill.onlineTime = 10;
        assertResult(1, 10, 10);
    }

    @Test
    @DisplayName("should keep rest online time and payout")
    void shouldKeepRestOnlineTime() {

        skill.onlineTime = 15;
        assertResult(1, 10, 10);
    }

    @Test
    @DisplayName("should keep rest online time if multi payout")
    void shouldKeepResultOnlineTimeIfMultiple() {

        skill.onlineTime = 54;
        assertResult(5, 50, 50);
    }
}