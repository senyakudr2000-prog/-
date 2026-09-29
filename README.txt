Real Water (NeoForge 1.21.1, Java 21)

1) Возьми официальный MDK 1.21.1 (ModDevGradle): https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle
2) В его gradle.properties поставь: mod_id=realwater, mod_group_id=com.example.realwater
3) Замени папку src/main целиком на src/main из этого архива
   (уберите из MDK пример-класс и свой neoforge.mods.toml - тут свой).
4) ./gradlew build  -> jar в build/libs/
5) Закинь jar в mods (Java 21 в лаунчере, рендерер LTW).

Блок: Creative -> Functional Blocks -> Water Emitter. Ставишь - сверху течёт вода.
Сломал - вылитая вода остаётся.
