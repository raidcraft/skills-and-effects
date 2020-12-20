# Raid-Craft Skills and Effects

[![Build Status](https://github.com/raidcraft/skills-and-effects/workflows/Build/badge.svg)](../../actions?query=workflow%3ABuild)
[![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/raidcraft/skills-and-effects?include_prereleases&label=release)](../../releases)
[![codecov](https://codecov.io/gh/raidcraft/skills-and-effects/branch/master/graph/badge.svg)](https://codecov.io/gh/raidcraft/skills-and-effects)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)
[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)

Dieses Projekt enthält alle Skills und Effekte die auf [Raid-Craft](https://raid-craft.de) genutzt werden.

Alle Skills befinden sich im [modules](modules) Ordner und haben dort eine Beschreibung ihrer Config Optionen.

## Skill Programmieren

Um einen neuen Skill oder Effekt hinzuzufügen einfach analog der anderen Klassen hinzufügen. Die Klasse wird dann automatisch vom [RCSkills](https://github.com/raidcraft/rcskills) Plugin erkannt.

> **WICHTIG**: Nicht die `@SkillInfo` oder `@EffectInfo` annotation vergessen, sonst kann die Klasse nicht registriert werden.
> Außerdem muss `@ConfigOption` aus dem `de.raidcraft.skills.configmapper.*` package stammen nicht aus `net.silthus.*`.