# keep-inventory

The player has a chance to keep his inventory when he dies. Use the `execution.cooldown` config to configure the cooldown of this skill.

| Option | Default | Description |
| ------ | ------- | ----------- |
| `chance` | `0.1` | The chance of keeping the inventory in percent, e.g. `0.1` is 10%. |
| `keep_inventory` | `true` | Set to false to drop the inventory on death. |
| `keep_level` | `false` | Set to true to keep the Minecraft level of the player. |
| `message` | `...` | The message to display when the skill was activated. Placeholder: `{skill}`, `{cooldown}`, `{alias}`|
