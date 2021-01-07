# max-health

Erhöht die maximalen Lebenspunkte des Spielers auf den angegebenen Wert.

| Option | Default | Beschreibung |
| ------ | ------- | ----------- |
| `modifier` | `0.1` | Der Attribute Modifier der auf die maximalen Leben des Spielers angewendet wird. Wichtig dabei ist das `operation` setting für die Anwendung des Werts. |
| `operation` | `MULTIPLY_SCALAR_1` | Die [Operation](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/AttributeModifier.Operation.html) mit der der `modifier` angewandt wird. `ADD_NUMBER`, `ADD_SCALAR` oder `MULTIPLY_SCALAR_1`. |
| `base` | `20` | Setzt die maximalen Lebenspunkte des Spielers auf den Wert. |