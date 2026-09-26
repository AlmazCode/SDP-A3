# Design rationale

## Problem and structure

A laboratory must issue routine work instructions and emergency directions to different endpoints. The wording and priority of an instruction depend on its kind, while the endpoint controls delivery. Neither axis should force a subclass for every combination. `Instruction` is the Bridge abstraction; `RoutineInstruction` and `EmergencyInstruction` refine it. Each holds only an `InstructionOutput`. `PanelOutput`, `LabelOutput`, and `LegacyTerminalAdapter` implement that interface. Either instruction kind can use any output.

Bridge alone separates the two axes but cannot make the old terminal satisfy `InstructionOutput`. Adapter alone can connect the terminal, but would leave instruction kinds coupled to endpoint classes or require classes such as `EmergencyPanelInstruction` and `RoutineLabelInstruction`. Combining the patterns places the adapter on the implementation side of the Bridge.

## Incompatible terminal and error contract

The simulated vendor class `LegacyTerminal` is kept separate in `lab.instructions.legacy` and is not modified by the adapter. Its `sendCommand(int station, byte[] payload, char urgency)` differs from `publish(InstructionMessage)` in name, argument count, types, and order. It reports failures with integer status codes (`BAD_STATION`, `QUEUE_FULL`, `OFFLINE`) rather than `DeliveryException`. The adapter parses and encodes the message, maps priority to the native urgency flag, and converts every documented status and any unknown status into a `DeliveryException.Reason`. Invalid station text and unexpected native runtime failures are also translated. The `Instruction` hierarchy never references the terminal or its constants.

## Required complexity module and extensibility

**Chosen module: dynamic implementor selection.** `InstructionRequest.destination` determines the output at runtime. `InstructionDispatcher` looks it up among `OutputProvider` instances discovered by `ServiceLoader`; the client supplies destination data, not an output class. The adapted terminal is selected by `legacy` in exactly the same path as the other outputs. The request's `kind` similarly selects an `InstructionProvider`. A new instruction variant or output needs new classes and a service descriptor entry, with no edit to existing Java classes. This is the Open/Closed Principle on both Bridge axes.

## Verification and limitation

JUnit 5 tests use recording outputs to verify both refined abstractions, a stub terminal to verify native argument conversion and every status mapping, and provider tests to verify runtime selection. Panel and label endpoints and the vendor terminal are simulated with output streams; physical device protocols and delivery acknowledgements are outside this project.
