# Laboratory Instruction Delivery

Assignment 3: Bridge and Adapter in one laboratory instruction workflow.

## Build

Requires Java 17+ and Maven. Build with one command:

```bash
mvn package
```

## Run

The first two arguments are data used for runtime selection. The application discovers instruction and output providers with Java `ServiceLoader`; `Main` does not choose concrete output classes.

```bash
mvn -q compile exec:java -Dexec.args='emergency legacy 7 Close fume hood'
```

Other examples:

```bash
mvn -q compile exec:java -Dexec.args='routine panel 12 Calibrate pipette'
mvn -q compile exec:java -Dexec.args='routine label 12 Calibrate pipette'
```

Kinds: `routine`, `emergency`. Destinations: `panel`, `label`, `legacy`. The legacy route requires a positive numeric station code. Panel and label outputs, and the simulated vendor terminal, write to standard output in this demo.