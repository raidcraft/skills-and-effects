# server-shop-exp

Der Spieler erhält jedes Mal EXP wenn er etwas über den [Server Shop](https://github.com/raidcraft/server-shop) verkauft.

| Option | Default | Beschreibung |
| ------ | ------- | ----------- |
| reason | `EXP für Item Verkauf` | Die Details die im EXP Log des Spielers auftauchen. |
| factor | `1.0` | Der Faktor mit dem die finalen EXP multipliziert werden. Ist vor allem für mehrere Skill Stufen hilfreich. |
| items | `{}` | Eine Map aus Items und EXP pro Verkauf. Siehe Beispiel unten. |

```yaml
reason: ...
factor: 1.0
items:
  # eine liste mit minecraft items und den exp pro stück die der Spieler erhält
  diamond: 20
  gold: 10
  'minecraft:iron_ore': 5
```