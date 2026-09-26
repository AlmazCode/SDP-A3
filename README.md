# Laboratory Instruction Delivery

Assignment 3: Bridge and Adapter in one laboratory instruction workflow.

## Build and test

Requires Java 17+ and Maven. One command compiles the project and runs all JUnit 5 tests:

```bash
mvn test
```

## Run

The first two arguments are data used for runtime selection. The application discovers instruction and output providers with Java `ServiceLoader`; `Main` does not choose concrete output classes.

```bash
mvn -q exec:java -Dexec.args='emergency legacy 7 Close fume hood'
```

Other examples:

```bash
mvn -q exec:java -Dexec.args='routine panel 12 Calibrate pipette'
mvn -q exec:java -Dexec.args='routine label 12 Calibrate pipette'
```

Kinds: `routine`, `emergency`. Destinations: `panel`, `label`, `legacy`. The legacy route requires a positive numeric station code. Panel and label outputs, and the simulated vendor terminal, write to standard output in this demo.

## Design and submission files

- [Design rationale](docs/design-rationale.md) explains both patterns, the chosen complexity module, and a limitation.
- [UML class diagram](docs/class-diagram.svg) shows the code's classes and relationships.
- `src/test/java` contains JUnit 5 tests for delegation, runtime selection, and adapter failures.

To add an instruction kind, add a subclass of `Instruction` and an `InstructionProvider`, then register the provider in `META-INF/services/lab.instructions.InstructionProvider`. To add an output, implement `InstructionOutput` and `OutputProvider` and register it in the corresponding service descriptor. Existing Java classes need no changes.
