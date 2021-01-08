# rebirth

Der Skill hat eine Chance tödlichen Schaden zu verhindern und dann den Spieler zu heilen. Der Skill ist mit dem cooldown der `execution` Config kombinierbar.

| Option | Default | Beschreibung |
| ------ | ------- | ----------- |
| `chance` | `0.1` | Die Chance in % die der Spieler hat tödlichen Schaden zu verhindern und den Skill auszulösen. |
| `heal` | `20` | Die Leben oder der Prozentsatz an Leben die der Spieler zurück bekommt. |
| `heal_in_percent` | `false` | Wenn auf `true` zählt der Wert von `heal` als Prozentsatz, z.B. `heal: 0.1` ist 10%. |
| `message` | `Dein Skill {skill} hat tödlichen Schaden verhindert und du wurdest um {heal} Leben geheilt. Cooldown: {cooldown}` | Nachricht die kommt wenn der Skill ausgelöst wird. Platzhalter: `{skill}`, `{heal}`, `{cooldown}`, `{alias}` |