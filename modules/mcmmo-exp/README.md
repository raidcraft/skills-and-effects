# mcmmo-exp

Der Spieler erhält jedes Mal EXP wenn er mcMMO EXP erhält.

| Option | Default | Beschreibung |
| ------ | ------- | ----------- |
| `base` | `0` | EXP die der Spieler jedes Mal erhält wenn er mcMMO EXP sammelt. |
| `factor` | `0.25` | Der Spieler erhält die mcMMO EXP multipliziert mit dem Faktor als EXP. |
| `min` | `0` | Das Minimum an EXP was der Spieler jedes Mal erhält wenn er mcMMO EXP erhält. |
| `max` | `20` | Das Maximum an EXP was der Spieler pro mcMMO EXP erhalten kann. |
| `max-per-hour` | `100` | Das Maximum an mcMMO -> RC-EXP was der Spieler pro Stunde erhalten kann. `-1` für unendlich EXP/Stunde. |
| `skills` | alle mcMMO Skills | Eine Liste von [mcMMO Skills](https://github.com/mcMMO-Dev/mcMMO/blob/master/src/main/java/com/gmail/nossr50/datatypes/skills/PrimarySkillType.java#L37) in Kleinbuchstaben die diesen Skill auslösen sollen. |
| `reasons` | alle mCMMO EXP Reasons | Eine Liste von [mcMMO EXP Reasons](https://github.com/mcMMO-Dev/mcMMO/blob/master/src/main/java/com/gmail/nossr50/datatypes/experience/XPGainReason.java) in Kleinbuchstaben die diesen Skill auslösen sollen. |