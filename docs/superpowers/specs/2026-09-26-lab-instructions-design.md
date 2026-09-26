# Laboratory instruction delivery design

## Purpose

Build an individual Java assignment that combines Bridge and Adapter in one laboratory instruction workflow. It must be easy to run and explain at the oral defense. The domain differs from the user's earlier program launch configuration and game world assignments.

## Model

An instruction request carries an instruction kind, destination, station code, and text. `Instruction` is the Bridge abstraction and holds only an `InstructionOutput`. `RoutineInstruction` and `EmergencyInstruction` independently prepare normal and urgent messages. `InstructionOutput.publish(InstructionMessage)` is the Implementor contract. `PanelOutput`, `LabelOutput`, and `LegacyTerminalAdapter` are its three concrete implementations.

The unmodified `LegacyTerminal` uses `sendCommand(int station, byte[] payload, char urgency)` and returns status codes instead of throwing the contract's `DeliveryException`. The adapter parses the station, encodes the text, maps urgency, and translates every documented or unknown status to a contract-level error. No instruction class imports the terminal or its constants.

## Runtime selection and extensibility

The chosen complexity module is dynamic implementor selection. A dispatcher reads the destination from each request and selects an `InstructionOutputProvider` discovered with Java `ServiceLoader`. The client does not choose an output class. An analogous provider for instruction kinds allows new abstraction variants without changing existing classes. Registering a new provider may add a service descriptor entry but does not modify existing Java classes.

## Validation and proof

Use Maven and JUnit 5. Tests stub `InstructionOutput` to check both refined abstractions, stub `LegacyTerminal` to check success and all failure translations, and exercise runtime selection including the adapted route. Provide an accurate UML class diagram, a design rationale of at most two pages, a README with one-command build and test instructions, and a runnable demo.

## Limits

Panel and label outputs simulate their devices with supplied output streams. The legacy terminal is a local representative of an immutable vendor API. No physical laboratory hardware is required.
