# [1.7.0](https://github.com/raidcraft/skills-and-effects/compare/v1.6.0...v1.7.0) (2021-01-07)


### Features

* **max-health:** implement max health skill ([92ef653](https://github.com/raidcraft/skills-and-effects/commit/92ef653f0811912330c93b6190fd0cbcd5cd9895))

# [1.6.0](https://github.com/raidcraft/skills-and-effects/compare/v1.5.3...v1.6.0) (2021-01-07)


### Features

* **mc-exp-boost:** implement minecraft exp booster ([bcb67fa](https://github.com/raidcraft/skills-and-effects/commit/bcb67fa848220f4b445fd9498d65f8461e6a60ee))

## [1.5.3](https://github.com/raidcraft/skills-and-effects/compare/v1.5.2...v1.5.3) (2021-01-07)


### Bug Fixes

* **mcmmo-exp:** floating point precision is lost for low exp values ([dcca819](https://github.com/raidcraft/skills-and-effects/commit/dcca81965a749c595794e957f91e0827328e3400)), closes [#8](https://github.com/raidcraft/skills-and-effects/issues/8)

## [1.5.2](https://github.com/raidcraft/skills-and-effects/compare/v1.5.1...v1.5.2) (2021-01-06)


### Bug Fixes

* **server-shop-exp:** filter out player before checking event ([8d17fad](https://github.com/raidcraft/skills-and-effects/commit/8d17fad2ba10cd2aaf8bb505b6247c314bba58d1))

## [1.5.1](https://github.com/raidcraft/skills-and-effects/compare/v1.5.0...v1.5.1) (2021-01-06)


### Bug Fixes

* **mcmmo-exp:** store timestamp as epoch in milliseconds ([155b607](https://github.com/raidcraft/skills-and-effects/commit/155b6075fc6597094275eee258c68fe3bb50c33f))
* @ConfigOption for Set does not work ([bdb16f9](https://github.com/raidcraft/skills-and-effects/commit/bdb16f9990347ce09fd86feda06ee886f8a1142a))

# [1.5.0](https://github.com/raidcraft/skills-and-effects/compare/v1.4.2...v1.5.0) (2021-01-04)


### Bug Fixes

* **FriendlyMobs:** Rename test class ([9151a44](https://github.com/raidcraft/skills-and-effects/commit/9151a44858cee388eed0c3949026e0536e963095))


### Features

* **FriendlyMobs:** Add new skill type 'FriendlyMobs' to support new Skills like 'Piglins-Friend' ([a1e9476](https://github.com/raidcraft/skills-and-effects/commit/a1e94764416acc567995d9e5f0f5e7475f262a79))
* **piglings-friend:** Add base implementation of new skill piglings-friend. ([d3d1750](https://github.com/raidcraft/skills-and-effects/commit/d3d1750f70b91bbe0541592d78a6e55c5dadfb04))

## [1.4.2](https://github.com/raidcraft/skills-and-effects/compare/v1.4.1...v1.4.2) (2021-01-04)


### Bug Fixes

* check player if player is applicable before using event ([10d1d06](https://github.com/raidcraft/skills-and-effects/commit/10d1d0656c3e8190abdb1a76eebfba25172eacc7))

## [1.4.1](https://github.com/raidcraft/skills-and-effects/compare/v1.4.0...v1.4.1) (2021-01-03)


### Bug Fixes

* **movement-speed:** do not apply attribute modifier when already applied ([a62d285](https://github.com/raidcraft/skills-and-effects/commit/a62d285480f0b832f574afda52c87e9905a7812e))

# [1.4.0](https://github.com/raidcraft/skills-and-effects/compare/v1.3.0...v1.4.0) (2021-01-03)


### Features

* **movement-speed:** add attribute modifier instead of modifying base value ([017df3f](https://github.com/raidcraft/skills-and-effects/commit/017df3f8c8d25dda3a0fecd914c0ecc844111fd6))

# [1.3.0](https://github.com/raidcraft/skills-and-effects/compare/v1.2.1...v1.3.0) (2021-01-02)


### Bug Fixes

* **look-teleport:** play sound and effect by default ([30627e3](https://github.com/raidcraft/skills-and-effects/commit/30627e30ea381280c129b5bee13120f62b91e7f2))


### Features

* add look-teleport skill ([b455cec](https://github.com/raidcraft/skills-and-effects/commit/b455cecb6ad17d44eb28c947271179ba430db030))
* add server-shop-exp skill ([73f683d](https://github.com/raidcraft/skills-and-effects/commit/73f683d080f60629ca91dfe074770e72acbb22b9))

## [1.2.1](https://github.com/raidcraft/skills-and-effects/compare/v1.2.0...v1.2.1) (2021-01-01)


### Bug Fixes

* store online and afk time seperately ([5c989f5](https://github.com/raidcraft/skills-and-effects/commit/5c989f522a4dd876cb429f55e798c6310fb636e3)), closes [#48](https://github.com/raidcraft/skills-and-effects/issues/48)

# [1.2.0](https://github.com/raidcraft/skills-and-effects/compare/v1.1.3...v1.2.0) (2020-12-25)


### Bug Fixes

* make mcmmo-exp depend on mcMMO plugin ([d593b46](https://github.com/raidcraft/skills-and-effects/commit/d593b46aee54c40f40d5890dfcd9a4da8a899b64))


### Features

* **online-time:** add essentials afk tracker to online time skill ([e87e680](https://github.com/raidcraft/skills-and-effects/commit/e87e680278f7b514d610b8f29438e70b5405b0aa))
* add new mcmmo exp gain skill ([8d0676a](https://github.com/raidcraft/skills-and-effects/commit/8d0676af31689ec32e12d0d384b30d3ddffb0476))

## [1.1.3](https://github.com/raidcraft/skills-and-effects/compare/v1.1.2...v1.1.3) (2020-12-24)


### Bug Fixes

* **online-time:** do not drop extra payout time ([737d9df](https://github.com/raidcraft/skills-and-effects/commit/737d9df22ca190b825dbc1b2b771eacd0f6b8f52))

## [1.1.2](https://github.com/raidcraft/skills-and-effects/compare/v1.1.1...v1.1.2) (2020-12-23)


### Bug Fixes

* **build:** publish all skills as one jar ([7b53afb](https://github.com/raidcraft/skills-and-effects/commit/7b53afbeb9375085358a071be5ceaec0e056484d))

## [1.1.1](https://github.com/raidcraft/skills-and-effects/compare/v1.1.0...v1.1.1) (2020-12-23)


### Bug Fixes

* **deps:** update rcskills to 1.7.2 ([292f9a3](https://github.com/raidcraft/skills-and-effects/commit/292f9a34b5e5505ecf7b8662c56bcbdecbdf1278))

# [1.1.0](https://github.com/raidcraft/skills-and-effects/compare/v1.0.1...v1.1.0) (2020-12-20)


### Features

* add new movement-speed skill ([6a8e363](https://github.com/raidcraft/skills-and-effects/commit/6a8e363f2680e41d5b3682c2871a5c77ecd3db36))

## [1.0.1](https://github.com/raidcraft/skills-and-effects/compare/v1.0.0...v1.0.1) (2020-12-20)


### Bug Fixes

* use relocated config mapper package ([9c743b9](https://github.com/raidcraft/skills-and-effects/commit/9c743b9fc4dc01c150e53444558a59c2b2bcbc64))

# 1.0.0 (2020-12-20)


### Features

* move skills and effects from main project into separate project ([ff0c4f7](https://github.com/raidcraft/skills-and-effects/commit/ff0c4f73f7b1fbb2a24b403c0a5dcbe0ebf56da3))
