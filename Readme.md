# Raid-Craft Skills and Effects

[![Build Status](https://github.com/raidcraft/skills-and-effects/workflows/Build/badge.svg)](../../actions?query=workflow%3ABuild)
[![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/raidcraft/skills-and-effects?include_prereleases&label=release)](../../releases)
[![codecov](https://codecov.io/gh/raidcraft/skills-and-effects/branch/master/graph/badge.svg)](https://codecov.io/gh/raidcraft/skills-and-effects)
[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)
[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)

Dieses Projekt enthält alle Skills und Effekte die auf [Raid-Craft](https://raid-craft.de) genutzt werden.

Alle Skills befinden sich im [modules](modules) Ordner und haben dort eine Beschreibung ihrer Config Optionen.

## Liste der Skills

- [look-teleport](modules/look-teleport): *Teleportiert den Spieler zu dem Block den er anschaut.*
- [mcmmo-exp](modules/mcmmo-exp): *Gibt RC-EXP für jede erhaltene mcMMO EXP.*
- [movement-speed](modules/movement-speed): *Erhöht den movement speed des Spielers auf Land, in der Luft oder im Wasser.*
- [online-time-exp](modules/online-time-exp): *Gibt dem Spieler RC-EXP für die Zeit die er aktiv spielt.*
- [server-shop-exp](modules/server-shop-exp): *Der Spieler erhält EXP für Gegenstände die er an den Server Shop verkauft.*
- [friendly-mobs](modules/friendly-mobs): *Feindliche Mobs greifen den Spieler nicht mehr grundlos an*
- [mc-exp-boost](modules/mc-exp-boost): *Erhöht die erhaltenen Minecraft EXP des Spielers.*

## Skill Development

There is a [quickstart template](modules/template) to get you started with developing new skills. Simply copy and paste it and then follow the steps in the template description.