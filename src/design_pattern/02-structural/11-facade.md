**Facade Pattern**

**Intent:** Provides a unified interface to a set of interfaces in a subsystem.

**Also called:** Facade

**Motivation:** Large systems often expose many classes with complex interactions. A Facade simplifies usage by offering a higher-level interface that delegates to the appropriate subsystem objects.

**When to use:**
- When you want to provide a simple API over a complex subsystem.
- To decouple clients from the subsystem and reduce compilation/usage dependencies.
- When designing framework or library entry points, or aggregating services.

**Structure / Participants:**
- **Facade:** `Facade` class that provides simple methods for common tasks and coordinates subsystem objects.
- **Subsystem classes:** Complex classes that implement the actual functionality. Clients should not need to use these directly.
- **Client:** Uses the `Facade` to perform tasks.

**Consequences / Benefits:**
- Simplifies common tasks for clients.
- Limits dependencies between the client and many subsystem classes.
- Improves readability and maintenance of client code.

**Drawbacks / Trade-offs:**
- Can become a god object if it exposes too much responsibility.
- Clients still can (and sometimes must) use subsystem classes for advanced operations—Facade doesn't prevent that.

**Use cases:** Framework APIs, service aggregation, simplifying library entry points, testing stubs/mocks.

**PHP Focus:** Favor small, focused facade classes that encapsulate initialization and common workflows. Use dependency injection when the subsystem objects need to be testable or configurable.

**Quick example:** See the playable example at [src/design_pattern/02-structural/11-facade/example.php](src/design_pattern/02-structural/11-facade/example.php#L1).
