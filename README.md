# Kitchen Expo Game

A small proof-of-concept game about being the **expeditor** in a busy restaurant.

The player coordinates the flow of food from the kitchen to the dining room during service, ensuring timely delivery, correctness of orders, and keeping dishes synchronised so that plates get to the table together.

In case you're wondering, all this is inspired by the TV series "The Bear".

## Building and running

**Prerequisites:** JDK 21+. Gradle wrapper is included.

**Run the app:**
```
.\gradlew.bat run
```

**Compile:**
```
.\gradlew.bat compileKotlin
```

**Package a native Windows executable:**
```
.\gradlew.bat packageExe
```
The `.exe` will be placed under `build/compose/binaries/`.
