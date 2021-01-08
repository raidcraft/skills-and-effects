# no-pvp

Prevents the player that has this skill from engaging into PvP actions.

| Option | Default | Beschreibung |
| ------ | ------- | ----------- |
| `message_cooldown` | `10s` | Cooldown between sending notice messages to the player attacking the skill holder and vice versa. |
| `attacker_message` | `...` | The message that is sent the player attacking this player. Replacements: `{skill}`, `{player}`, `{alias}`|
| `message` | `...` | The message that is sent to the holder of this skill when he tries to attack another player. Replacements: `{skill}`, `{player}`, `{alias}`|
