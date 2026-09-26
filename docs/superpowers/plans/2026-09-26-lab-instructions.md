# Laboratory Instruction Delivery Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Deliver a buildable Java Bridge plus Adapter assignment with dynamic output selection, tests, UML, and rationale.

**Architecture:** Two instruction variants compose an output interface. Three outputs include an adapter for a status-code based legacy terminal. A dispatcher uses provider registries loaded through Java ServiceLoader.

**Tech Stack:** Java 17, Maven, JUnit 5.

**Spec:** `docs/superpowers/specs/2026-09-26-lab-instructions-design.md`

## Global Constraints

- `Instruction` may depend only on `InstructionOutput` among implementation roles.
- Exactly one of the three outputs is an adapter around an unmodified incompatible class.
- The chosen complexity module is dynamic implementor selection.
- The final rationale is at most two pages.

## Review Focus

- Unknown destination and instruction kind receive a meaningful error.
- Malformed station codes never leak parsing exceptions.
- Every terminal status, including unexpected codes, becomes `DeliveryException`.
- Selecting the legacy route actually uses the adapter.
- Adding a provider requires no edit to existing Java classes.

---

### Task 1: Bridge and Maven test setup

**Files:** `pom.xml`, `src/main/java/lab/instructions/{InstructionRequest,InstructionMessage,Priority,DeliveryException,InstructionOutput,Instruction,RoutineInstruction,EmergencyInstruction}.java`, `src/test/java/lab/instructions/InstructionTest.java`

**Interfaces:** `InstructionOutput.publish(InstructionMessage)` throws `DeliveryException`; `Instruction.issue(String stationCode, String text)` delegates and returns no value.

- [ ] Write JUnit tests using a recording output for routine and emergency formatting, priority, station, and propagation of `DeliveryException`.
- [ ] Run `mvn test` and observe compilation failure for missing production types.
- [ ] Implement the minimal Bridge classes and Maven configuration.
- [ ] Run `mvn test` and confirm green.

### Task 2: Output implementations and adapter

**Files:** `src/main/java/lab/instructions/{PanelOutput,LabelOutput,LegacyTerminal,LegacyTerminalAdapter}.java`, `src/test/java/lab/instructions/OutputTest.java`, `src/test/java/lab/instructions/LegacyTerminalAdapterTest.java`

**Interfaces:** Panel and label write to a supplied `PrintStream`; the native terminal has `sendCommand(int,byte[],char)` and integer status codes; the adapter implements `InstructionOutput`.

- [ ] Test both native outputs with captured streams and adapter success/failure with a stub terminal, including malformed station and unknown status.
- [ ] Run `mvn test` and observe failure for missing output types.
- [ ] Implement outputs and adapter with complete status translation.
- [ ] Run `mvn test` and confirm green.

### Task 3: Runtime dispatch

**Files:** `src/main/java/lab/instructions/{InstructionProvider,OutputProvider,InstructionDispatcher,Main}.java`, provider classes, `src/main/resources/META-INF/services/*`, `src/test/java/lab/instructions/InstructionDispatcherTest.java`

**Interfaces:** `InstructionDispatcher.dispatch(InstructionRequest)` chooses instruction and output providers from request keys; `Main` loads providers through `ServiceLoader`.

- [ ] Test choice of output from input, legacy route, and unknown keys using stub providers.
- [ ] Run `mvn test` and observe failure for missing dispatcher.
- [ ] Implement providers, dispatcher, descriptors, and runnable demo.
- [ ] Run `mvn test` and demo, confirming green.

### Task 4: Submission documentation

**Files:** `README.md`, `docs/design-rationale.md`, `docs/class-diagram.svg`.

**Interfaces:** `mvn test` builds and runs tests; `mvn exec:java` runs the demo.

- [ ] Write README, rationale, and diagram matching the final class names and dependencies.
- [ ] Run `mvn test` and `mvn exec:java`; inspect diagram and check rationale length.
- [ ] Verify git status and remote before preparing the repository link.
