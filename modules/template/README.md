# template

Use this template skill to quickstart the development of new skills. Add the config values of your skill in the table below and provide any additional information that is required to configure the skill.

| Option | Default | Beschreibung |
| ------ | ------- | ----------- |
| | |

## Quickstart

1. name the class to match your skill
2. adjust the log topic and skill identifier
3. add primitive config values as properties tagged with @ConfigOption and load the rest inside the load(...) method
4. (optional) implement listener if you require any bukkit events
5. (optional) implement Executable if your skill can be actively executed by the player
6. (recommended) add unit tests to test the logic of your skill

> **IMPORTANT**: always use the `de.raidcraft.skills.configmapper` package for the `@ConfigOption` or else your config will not be loaded.