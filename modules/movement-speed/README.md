# movement-speed

Erhöht den Basis Movement Speed des Spielers. [Hier ist eine Liste](https://minecraft.gamepedia.com/Attribute#Attributes_available_on_all_living_entities) der Minecraft Geschwindigkeiten.

| Option | Default | Beschreibung |
| ------ | ------- | ----------- |
| `modifier` | `0.1` | Der Attribute Modifier der auf den Movement Speed des Spielers angewendet wird. Wichtig dabei ist das `operation` setting für die Anwendung des Werts. |
| `operation` | `MULTIPLY_SCALAR_1` | Die [Operation](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/AttributeModifier.Operation.html) mit der der `modifier` angewandt wird. `ADD_NUMBER`, `ADD_SCALAR` oder `MULTIPLY_SCALAR_1`. |
| `states` | `ALL` | Die States des Spielers in denen der Speed Bonus angewendet werden soll. `IN_WATER`, `FLYING`, `WALKING`, `SPRINTING`, `GLIDING`, `SNEAKING`, `ALL` |