# look-teleport (AKTIV)

Teleportiert den Spieler an die Stelle die er schaut. Distanz und Cooldown über die `execution` config einstellbar.

| Option | Default | Beschreibung |
| ------ | ------- | ----------- |
| `play_effect` | `true` | Zeigt einen Enderman Partikel Effekt beim Teleport an. |
| `play_sound` | `true` | Spielt einen Sound beim Teleport ab. |
| `sound` | `ENTITY_ENDERMAN_TELEPORT` | Der Sound Effekt der beim Teleport abgespielt werden soll. Muss ein valider Sound aus [dieser Liste](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html) sein. |
| `effect` | `PORTAL_TRAVEL` | Der Partikel Effekt der beim Teleport abgespielt werden soll. Muss ein Partikel Effekt aus [dieser Liste](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Effect.html) sein.
| `transparent_blocks | `Material#isTransparent()` | Eine Liste aus Minecraft Materials die als transparent angesehen werden sollen. Nimmt die default Liste des Servers wenn leer. |