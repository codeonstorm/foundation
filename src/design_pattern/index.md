# Design Patterns in PHP - Deep Learning Roadmap

A comprehensive guide to master design patterns with PHP implementations and real-world examples.

---

## Table of Contents

1. [Introduction](#introduction)
2. [Creational Patterns](#creational-patterns)
3. [Structural Patterns](#structural-patterns)
4. [Behavioral Patterns](#behavioral-patterns)
5. [Architectural Patterns](#architectural-patterns)
6. [Learning Path](#learning-path)

---

## Introduction

### What are Design Patterns?

Design patterns are reusable solutions to common problems in software design. They provide templates for writing maintainable, scalable, and efficient code.

### Why Learn Design Patterns?

- Write cleaner, more maintainable code
- Communicate solutions effectively with team members
- Solve problems faster with proven solutions
- Understand framework architecture better (Laravel, Symfony, etc.)

### Pattern Categories

Design patterns are typically grouped into three main categories based on their purpose.

---

## Creational Patterns

Creational patterns deal with object creation mechanisms.

### 1. Singleton Pattern
**File:** `01-creational/01-singleton.md`

- Ensures a class has only one instance
- Provides global access to that instance
- Use cases: Database connections, Logger, Configuration managers
- **PHP Focus:** Static properties, private constructors, getInstance()

### 2. Factory Method Pattern
**File:** `01-creational/02-factory-method.md`

- Creates objects without specifying their exact classes
- Delegates object creation to subclasses
- Use cases: Database drivers, Payment processors, File handlers
- **PHP Focus:** Interface implementation, polymorphism

### 3. Abstract Factory Pattern
**File:** `01-creational/03-abstract-factory.md`

- Creates families of related or dependent objects
- Provides interface for creating object families
- Use cases: UI theme engines, Cross-platform applications
- **PHP Focus:** Multiple related interfaces, factory classes

### 4. Builder Pattern
**File:** `01-creational/04-builder.md`

- Constructs complex objects step by step
- Separates construction from representation
- Use cases: Query builders, HTML/XML builders, Configuration objects
- **PHP Focus:** Fluent interfaces, method chaining

### 5. Prototype Pattern
**File:** `01-creational/05-prototype.md`

- Creates new objects by cloning existing ones
- Avoids expensive object creation
- Use cases: Cache systems, Document cloning
- **PHP Focus:** `clone` keyword, deep vs shallow copying

### 6. Object Pool Pattern
**File:** `01-creational/06-object-pool.md`

- Reuses objects that are expensive to create
- Manages pool of reusable instances
- Use cases: Database connections, Thread pools
- **PHP Focus:** Resource management, initialization

---

## Structural Patterns

Structural patterns deal with object composition and relationships.

### 7. Adapter Pattern
**File:** `02-structural/07-adapter.md`

- Makes incompatible interfaces work together
- Converts interface to another clients expect
- Use cases: Legacy code integration, Third-party library wrapping
- **PHP Focus:** Interface mapping, wrapper classes

### 8. Bridge Pattern
**File:** `02-structural/08-bridge.md`

- Decouples abstraction from implementation
- Allows varying independently
- Use cases: Database abstraction, Cross-platform systems
- **PHP Focus:** Composition over inheritance, abstraction

### 9. Composite Pattern
**File:** `02-structural/09-composite.md`

- Composes objects into tree structures
- Clients treat individual objects and compositions uniformly
- Use cases: File systems, DOM trees, Menu systems
- **PHP Focus:** Recursive structures, tree traversal

### 10. Decorator Pattern
**File:** `02-structural/10-decorator.md`

- Attaches additional responsibilities to objects dynamically
- Provides flexible alternative to subclassing
- Use cases: Middleware, Feature enhancement, Component wrapping
- **PHP Focus:** Wrapping objects, preserving interfaces

### 11. Facade Pattern
**File:** `02-structural/11-facade.md`

- Provides unified interface to complex subsystem
- Simplifies complex interactions
- Use cases: Framework APIs, Service aggregation
- **PHP Focus:** Simplification, encapsulation

### 12. Flyweight Pattern
**File:** `02-structural/12-flyweight.md`

- Shares common state to minimize memory usage
- Uses object pooling efficiently
- Use cases: Text editors (character objects), Game rendering
- **PHP Focus:** Intrinsic vs extrinsic state, memory optimization

### 13. Proxy Pattern
**File:** `02-structural/13-proxy.md`

- Provides placeholder/surrogate for another object
- Controls access to real object
- Use cases: Lazy loading, Access control, Caching
- **PHP Focus:** Virtual proxies, protection proxies, logging proxies

---

## Behavioral Patterns

Behavioral patterns deal with object collaboration and responsibility distribution.

### 14. Chain of Responsibility Pattern
**File:** `03-behavioral/14-chain-of-responsibility.md`

- Passes request along chain of handlers
- Each handler decides to process or pass
- Use cases: Event handling, Approval workflows, Logging levels
- **PHP Focus:** Handler interfaces, request objects

### 15. Command Pattern
**File:** `03-behavioral/15-command.md`

- Encapsulates request as object
- Parameterizes objects with operations
- Use cases: Undo/Redo systems, Macro recording, Task queuing
- **PHP Focus:** Command interfaces, receiver objects, invoker

### 16. Iterator Pattern
**File:** `03-behavioral/16-iterator.md`

- Provides sequential access to collection elements
- Hides underlying representation
- Use cases: Traversing custom collections, Database results
- **PHP Focus:** `Iterator` interface, `foreach` compatibility

### 17. Mediator Pattern
**File:** `03-behavioral/17-mediator.md`

- Defines object that encapsulates interactions
- Reduces coupling between communicating objects
- Use cases: Dialog boxes, Chat room, Game event coordination
- **PHP Focus:** Central coordinator, message passing

### 18. Memento Pattern
**File:** `03-behavioral/18-memento.md`

- Captures and externalizes object state
- Allows restoration to previous state
- Use cases: Undo/Redo, Snapshots, Save states
- **PHP Focus:** State serialization, caretaker objects

### 19. Observer Pattern
**File:** `03-behavioral/19-observer.md`

- Defines one-to-many dependency
- Notifies observers of state changes
- Use cases: Event systems, MVC architectures, Real-time updates
- **PHP Focus:** Event listeners, publish-subscribe

### 20. State Pattern
**File:** `03-behavioral/20-state.md`

- Allows object to alter behavior when state changes
- Encapsulates state-specific behavior
- Use cases: State machines, Workflow engines, Context switching
- **PHP Focus:** State interfaces, behavior encapsulation

### 21. Strategy Pattern
**File:** `03-behavioral/21-strategy.md`

- Defines family of algorithms
- Encapsulates each algorithm
- Use cases: Payment methods, Sorting algorithms, Validation rules
- **PHP Focus:** Algorithm encapsulation, runtime selection

### 22. Template Method Pattern
**File:** `03-behavioral/22-template-method.md`

- Defines algorithm skeleton in base class
- Subclasses implement specific steps
- Use cases: Framework templates, Processing pipelines
- **PHP Focus:** Abstract classes, method overriding

### 23. Visitor Pattern
**File:** `03-behavioral/23-visitor.md`

- Represents operation to perform on object structure
- Separates algorithm from objects
- Use cases: Compilers, Report generators, Tree traversal
- **PHP Focus:** Double dispatch, element acceptance

### 24. Interpreter Pattern
**File:** `03-behavioral/24-interpreter.md`

- Defines grammar representation
- Interprets sentences in language
- Use cases: SQL parsers, Configuration DSLs, Expression evaluation
- **PHP Focus:** Expression trees, recursive evaluation

---

## Architectural Patterns

High-level patterns for overall application structure.

### 25. Model-View-Controller (MVC)
**File:** `04-architectural/25-mvc.md`

- Separates data, presentation, logic
- Used by: Laravel, Symfony, WordPress
- PHP Focus: Controller classes, view templating, models

### 26. Model-View-ViewModel (MVVM)
**File:** `04-architectural/26-mvvm.md`

- Separates UI from business logic
- Two-way data binding
- PHP Focus: View models, property binding

### 27. Model-View-Presenter (MVP)
**File:** `04-architectural/27-mvp.md`

- Passive view receives updates from presenter
- Complete separation of concerns
- PHP Focus: View interfaces, presenter logic

### 28. Dependency Injection Pattern
**File:** `04-architectural/28-dependency-injection.md`

- Provides dependencies to classes
- Types: Constructor, Property, Method injection
- Use cases: Loose coupling, Testing, Service containers
- PHP Focus: Service containers, autowiring, PHP-DI, Laravel container

### 29. Repository Pattern
**File:** `04-architectural/29-repository.md`

- Abstracts data access logic
- Provides collection-like interface
- Use cases: Switching databases, Testing with mocks
- PHP Focus: Eloquent models, query builders

### 30. Service Locator Pattern
**File:** `04-architectural/30-service-locator.md`

- Provides instances of services
- Central registry for services
- Use cases: Laravel's app() helper, Service container
- PHP Focus: Service container, centralized access

---

## Learning Path

### Phase 1: Foundations (Weeks 1-2)
Start with these core patterns:

1. **Singleton** - Simple, fundamental concept
2. **Factory Method** - Essential for object creation
3. **Observer** - Understanding event systems
4. **Dependency Injection** - Critical for modern PHP

**Time Investment:** 2-3 hours per pattern
**Practice:** Build a mini-logger system using Singleton and Observer

---

### Phase 2: Essential Patterns (Weeks 3-4)
Build on foundations:

5. **Strategy** - Algorithm selection
6. **Adapter** - Making incompatible things work
7. **Decorator** - Dynamic enhancement
8. **Repository** - Data access abstraction

**Time Investment:** 2-3 hours per pattern
**Practice:** Build a payment processor supporting multiple payment methods

---

### Phase 3: Intermediate Patterns (Weeks 5-7)
Deepen understanding:

9. **Builder** - Complex object construction
10. **Facade** - Simplifying complex systems
11. **Command** - Encapsulating requests
12. **Chain of Responsibility** - Sequential processing
13. **Template Method** - Algorithm templates

**Time Investment:** 2-3 hours per pattern
**Practice:** Build a query builder like Laravel's Eloquent

---

### Phase 4: Advanced Patterns (Weeks 8-10)
Master complex patterns:

14. **Visitor** - Operating on object structures
15. **State** - State machine implementation
16. **Mediator** - Complex object communication
17. **Proxy** - Controlling object access
18. **Composite** - Tree structures

**Time Investment:** 3-4 hours per pattern
**Practice:** Build an expression parser or document generator

---

### Phase 5: Real-World Integration (Weeks 11-12)
Apply everything:

19. **MVC Architecture** - Full framework understanding
20. **Creational Patterns Review** - Singleton, Abstract Factory, Prototype
21. **Structural Patterns Review** - Bridge, Flyweight, Composite
22. **Behavioral Patterns Review** - Observer, State, Strategy

**Time Investment:** 4-5 hours per pattern
**Practice:** Build a complete mini-framework or refactor existing code

---

## Learning Strategy

### For Each Pattern, Cover:

1. **Concept** - What and why
2. **Problem** - Real-world scenario where it solves
3. **Solution** - Pattern structure and components
4. **PHP Implementation** - Code examples
5. **Use Cases** - Real applications (Laravel, WordPress, etc.)
6. **Pros & Cons** - When to use and avoid
7. **Real Example** - Laravel/Symfony/popular library usage
8. **Practice Exercise** - Build something with the pattern

### PHP-Specific Considerations:

- Magic methods (`__call`, `__get`, etc.)
- Traits and mixins
- Namespaces and autoloading
- Interfaces and abstract classes
- Type hints and return types
- Anonymous classes
- SPL (Standard PHP Library) interfaces

---

## Resources

### PHP Framework Examples:
- **Laravel**: Service container, facades, eloquent (Repository pattern)
- **Symfony**: Service container, event dispatcher, commands
- **WordPress**: Hooks system (Observer pattern)
- **Yii**: Active Record pattern, behaviors

### Books to Reference:
- "Design Patterns: Elements of Reusable Object-Oriented Software" (Gang of Four)
- "PHP Design Patterns" by Aaron Saray
- "Clean Code" by Robert C. Martin

### Online Resources:
- refactoring.guru (patterns with code examples)
- PHP official documentation
- Framework documentation

---

## Study Tips

1. **Code Along** - Type examples, don't copy-paste
2. **Modify Examples** - Change conditions, add features
3. **Combine Patterns** - See how patterns work together
4. **Review Framework Code** - See patterns in Laravel/Symfony
5. **Refactor Old Code** - Apply patterns to existing projects
6. **Teach Others** - Explain patterns to solidify understanding
7. **Build Projects** - Apply multiple patterns in real applications

---

## Progress Tracking

As you complete each pattern:

- [ ] Understand concept
- [ ] Read PHP implementation
- [ ] Run example code
- [ ] Modify and experiment
- [ ] Write own implementation
- [ ] Identify real-world use case
- [ ] Complete practice exercise

---

## Next Steps

Start with **Phase 1** patterns. Create a folder for each pattern with:
- `README.md` - Pattern explanation
- `example.php` - Basic implementation
- `advanced.php` - Complex example
- `exercise.md` - Practice challenge
- `references.md` - Links and resources

Good luck on your design patterns journey! 🚀
