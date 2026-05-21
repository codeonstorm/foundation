# SOLID Principles in PHP - Deep Learning Roadmap

A comprehensive guide to master SOLID principles with PHP implementations and real-world examples.

---

## Table of Contents

1. [Introduction](#introduction)
2. [SOLID Principles Overview](#solid-principles-overview)
3. [Single Responsibility Principle](#single-responsibility-principle)
4. [Open/Closed Principle](#openclosed-principle)
5. [Liskov Substitution Principle](#liskov-substitution-principle)
6. [Interface Segregation Principle](#interface-segregation-principle)
7. [Dependency Inversion Principle](#dependency-inversion-principle)
8. [Combining Principles](#combining-principles)
9. [Learning Path](#learning-path)

---

## Introduction

### What are SOLID Principles?

SOLID principles are five design principles intended to make software more understandable, flexible, and maintainable. They guide developers in writing clean code that's easier to extend and modify.

### Why Learn SOLID?

- Write maintainable code that's easier to understand
- Reduce coupling between components
- Increase code reusability
- Make testing easier
- Facilitate future modifications and extensions
- Align with industry best practices
- Write code like professional frameworks (Laravel, Symfony)

### SOLID Acronym

- **S**ingle Responsibility Principle (SRP)
- **O**pen/Closed Principle (OCP)
- **L**iskov Substitution Principle (LSP)
- **I**nterface Segregation Principle (ISP)
- **D**ependency Inversion Principle (DIP)

---

## SOLID Principles Overview

### The Five Principles

SOLID principles work together to create maintainable, scalable systems. Understanding each principle individually and how they interact is key.

```
SRP → Single Responsibility (Do one thing)
OCP → Open/Closed (Extend, don't modify)
LSP → Liskov Substitution (Substitute without breaking)
ISP → Interface Segregation (Small, focused interfaces)
DIP → Dependency Inversion (Depend on abstractions)
```

---

## Single Responsibility Principle

**A class should have one, and only one, reason to change.**

### File: `01-srp.md`

### Core Concept

Each class should have a single responsibility or reason to change. When a class has multiple responsibilities, it becomes harder to change and test.

### Why It Matters

- **Easier to Understand** - Class has clear purpose
- **Easier to Test** - Fewer dependencies to mock
- **Easier to Maintain** - Changes affect only one responsibility
- **More Reusable** - Single-purpose classes are more reusable

### Problems It Solves

❌ **God Classes** - Classes doing too much
❌ **Hidden Dependencies** - Unclear what a class really needs
❌ **Difficult Testing** - Hard to test due to multiple concerns
❌ **Side Effects** - Changes break unexpected parts

### PHP Example Structure

```php
// ❌ BAD: Multiple responsibilities
class User {
    public function createUser($data) { }
    public function sendWelcomeEmail($user) { }
    public function validateEmail($email) { }
    public function logActivity($activity) { }
}

// ✅ GOOD: Single responsibility
class User { }
class UserValidator { }
class EmailService { }
class ActivityLogger { }
```

### Key Components

1. **Identifying Responsibilities** - What does this class do?
2. **Extracting Methods** - Move to separate classes
3. **Clear Naming** - Class name reflects single purpose
4. **Testing** - Each class tested independently

### Real-World Examples

- **Laravel Models** - Represent data, relationships
- **Service Classes** - Handle business logic separately
- **Validators** - Dedicated validation classes
- **Repositories** - Dedicated data access

### Use Cases in PHP

- Creating Service classes from Models
- Extracting validation logic
- Separating concerns in controllers
- Building middleware components

### Common Violations

- User class handling auth, email, logging, validation
- Controller handling business logic, validation, response
- Model class handling database, validation, formatting

### Refactoring Strategy

1. **Identify multiple concerns** in a class
2. **Create separate classes** for each concern
3. **Inject dependencies** into original class
4. **Update tests** to reflect new structure

### Practice Exercise

**Refactor a User class with:**
- Email sending
- Password validation
- Database operations
- Activity logging
- Permission checking

**Into separate classes** with SRP

---

## Open/Closed Principle

**Software entities (classes, functions, modules) should be open for extension but closed for modification.**

### File: `02-ocp.md`

### Core Concept

You should be able to add new functionality without changing existing code. This is achieved through abstraction and inheritance/composition.

### Why It Matters

- **Safer Extensions** - Add features without breaking existing code
- **Backward Compatible** - Existing code still works
- **Reduced Risk** - Fewer places to introduce bugs
- **Easy Testing** - New code tested separately

### Problems It Solves

❌ **Fragile Code** - Small changes break multiple places
❌ **Risky Deployments** - Changes affect too many areas
❌ **Difficult Maintenance** - Existing code modified frequently
❌ **Code Duplication** - Similar logic in multiple places

### PHP Example Structure

```php
// ❌ BAD: Closed to extension
class PaymentProcessor {
    public function process($paymentType, $amount) {
        if ($paymentType === 'credit_card') {
            // process credit card
        } elseif ($paymentType === 'paypal') {
            // process paypal
        }
    }
}

// ✅ GOOD: Open for extension
interface PaymentMethod {
    public function process($amount);
}

class CreditCardPayment implements PaymentMethod {
    public function process($amount) { }
}

class PayPalPayment implements PaymentMethod {
    public function process($amount) { }
}

class PaymentProcessor {
    public function process(PaymentMethod $method, $amount) {
        return $method->process($amount);
    }
}
```

### Key Components

1. **Abstraction** - Create interfaces for extensibility
2. **Inheritance/Composition** - Extend via new classes, not modification
3. **Strategy Pattern** - Implement different strategies
4. **Template Method** - Define extension points in base class

### Real-World Examples

- **Payment Gateways** - Add new payment method without changing core
- **Database Drivers** - Switch drivers without changing queries
- **Report Generators** - Add report types without modifying existing
- **Notification Channels** - Add notification types (SMS, Email, Push)

### Use Cases in PHP

- Creating interfaces for different implementations
- Using Strategy pattern for algorithms
- Extending frameworks without modifying core
- Building plugin systems

### Common Violations

- `if/else` chains checking types
- Modifying existing methods to add features
- Large case statements for different behavior
- Hardcoding business logic

### Implementation Techniques

1. **Use Interfaces** - Define contracts
2. **Use Abstract Classes** - Define extension points
3. **Use Inheritance** - Override specific methods
4. **Use Composition** - Inject different implementations
5. **Use Design Patterns** - Strategy, Decorator, Template Method

### Practice Exercise

**Build a notification system:**
- Support Email, SMS, Push notifications
- Add new notification types without modifying existing code
- Use interfaces to define contract

---

## Liskov Substitution Principle

**Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.**

### File: `03-lsp.md`

### Core Concept

Derived classes must be substitutable for their base classes. If a function expects a base class, it should work correctly with any derived class.

### Why It Matters

- **Correct Inheritance** - Ensures proper class hierarchies
- **Polymorphism Works** - Objects can be used interchangeably
- **Unexpected Behavior** - Prevents breaking substitution
- **Code Reliability** - Base class contracts honored

### Problems It Solves

❌ **Broken Substitution** - Subclass breaks when substituted
❌ **Unexpected Behavior** - Derived class violates contract
❌ **Hidden Bugs** - Code works with parent but not child
❌ **Fragile Hierarchies** - Inheritance doesn't work as expected

### PHP Example Structure

```php
// ❌ BAD: Bird-Penguin problem
class Bird {
    public function fly() {
        return "Flying";
    }
}

class Penguin extends Bird {
    public function fly() {
        throw new Exception("Penguins cannot fly");
    }
}

// ✅ GOOD: Correct abstraction
interface Animal {
    public function move();
}

class Bird implements Animal {
    public function move() {
        return "Flying";
    }
}

class Penguin implements Animal {
    public function move() {
        return "Swimming";
    }
}
```

### Key Components

1. **Contract Honoring** - Subclass respects parent contract
2. **Behavioral Compatibility** - Expected behavior maintained
3. **Correct Abstraction** - Base class represents true parent
4. **Method Overriding** - Override correctly, don't violate

### Real-World Examples

- **Database Drivers** - All implement same interface behavior
- **Validation Rules** - Different validators work same way
- **Payment Methods** - All accept same interface
- **Storage Engines** - Different storage behave identically

### Use Cases in PHP

- Implementing concrete classes from interfaces
- Extending base classes without violating contracts
- Creating polymorphic collections
- Building plugin systems with interchangeable components

### Common Violations

- Throwing exceptions in overridden methods
- Changing method signatures in subclasses
- Removing functionality in subclasses
- Returning incompatible types from overrides

### Rules for Correct Substitution

1. **Honor Preconditions** - Don't add new requirements
2. **Honor Postconditions** - Deliver expected results
3. **Maintain Invariants** - Class properties remain valid
4. **Don't Throw New Exceptions** - Except those declared
5. **Return Compatible Types** - Same or covariant types

### Practice Exercise

**Create a Shape hierarchy:**
- Define Shape interface with area() method
- Implement Circle, Rectangle, Triangle
- Ensure all are substitutable for Shape
- Calculate total area from mixed Shape array

---

## Interface Segregation Principle

**Many client-specific interfaces are better than one general-purpose interface.**

### File: `04-isp.md`

### Core Concept

Clients should not be forced to depend on interfaces they don't use. Create focused, specific interfaces instead of one large "god interface".

### Why It Matters

- **Loose Coupling** - Classes depend only on needed methods
- **Easier to Test** - Mock only what's needed
- **Clear Contracts** - Interface shows exactly what's needed
- **Flexible Implementation** - Classes implement only relevant methods

### Problems It Solves

❌ **Fat Interfaces** - Interfaces with too many methods
❌ **Forced Implementation** - Must implement unused methods
❌ **Tight Coupling** - Depend on more than needed
❌ **Difficult Testing** - Must mock entire interface

### PHP Example Structure

```php
// ❌ BAD: Fat interface
interface Worker {
    public function work();
    public function eat();
    public function sleep();
}

class Robot implements Worker {
    public function work() { }
    public function eat() { }  // Robots don't eat!
    public function sleep() { } // Robots don't sleep!
}

// ✅ GOOD: Segregated interfaces
interface Workable {
    public function work();
}

interface Eatable {
    public function eat();
}

interface Sleepable {
    public function sleep();
}

class Robot implements Workable {
    public function work() { }
}

class Human implements Workable, Eatable, Sleepable {
    public function work() { }
    public function eat() { }
    public function sleep() { }
}
```

### Key Components

1. **Small Interfaces** - One responsibility per interface
2. **Focused Methods** - Only related methods grouped
3. **Client-Specific** - Interface for specific client needs
4. **Flexibility** - Implement only needed interfaces

### Real-World Examples

- **Laravel Contracts** - Small, focused interfaces
- **PSR Standards** - Segregated interfaces (PSR-3, PSR-6)
- **Payment Processors** - Specific payment interfaces
- **Storage Interfaces** - Different storage capabilities

### Use Cases in PHP

- Creating small, focused interfaces
- Implementing multiple interfaces on classes
- Defining role-based interfaces
- Building flexible component systems

### Common Violations

- One huge interface with 20+ methods
- Forcing implementation of unrelated methods
- Classes implementing methods they don't need
- Tight coupling to large interfaces

### Interface Design Rules

1. **One Responsibility** - Each interface has single purpose
2. **Client-Centric** - Design for client needs
3. **Cohesive Methods** - Related methods grouped
4. **Meaningful Names** - Interface name reflects purpose
5. **Composable** - Combine multiple small interfaces

### Practice Exercise

**Refactor a large interface:**
- Start with large, fat interface
- Identify different types of clients
- Create segregated interfaces for each client type
- Implement classes using appropriate interfaces

---

## Dependency Inversion Principle

**Depend upon abstractions, not concretions. High-level modules should not depend on low-level modules; both should depend on abstractions.**

### File: `05-dip.md`

### Core Concept

Classes should depend on abstractions (interfaces) rather than concrete implementations. This inverts the typical dependency flow where high-level code depends on low-level code.

### Why It Matters

- **Loose Coupling** - Easy to swap implementations
- **Testability** - Mock interfaces, not concrete classes
- **Flexibility** - Change implementation without changing dependents
- **Scalability** - Add new implementations easily

### Problems It Solves

❌ **Tight Coupling** - Hard to swap dependencies
❌ **Difficult Testing** - Can't mock concrete classes easily
❌ **Fragile Code** - Changes break dependents
❌ **Limited Flexibility** - Hard to use different implementations

### PHP Example Structure

```php
// ❌ BAD: Depend on concrete class
class UserService {
    private $database;
    
    public function __construct() {
        $this->database = new MySQLDatabase(); // Concrete!
    }
}

// ✅ GOOD: Depend on abstraction
interface Database {
    public function save($data);
    public function find($id);
}

class MySQLDatabase implements Database {
    public function save($data) { }
    public function find($id) { }
}

class UserService {
    private $database;
    
    public function __construct(Database $database) {
        $this->database = $database;
    }
}
```

### Key Components

1. **Abstractions** - Create interfaces for dependencies
2. **Dependency Injection** - Pass dependencies in
3. **Constructor Injection** - Most common form
4. **Service Container** - Manage dependency creation

### Real-World Examples

- **Laravel Service Container** - Dependency injection
- **Database Abstraction** - Eloquent over PDO
- **Logging** - Logger interface over concrete logger
- **Cache** - Cache interface over Redis/Memcached
- **Email** - Mail interface over specific provider

### Use Cases in PHP

- Using Dependency Injection containers
- Constructor injection in classes
- Property injection where appropriate
- Method injection for specific cases

### Types of Dependency Injection

1. **Constructor Injection** - Dependencies in constructor
2. **Property Injection** - Dependencies as properties
3. **Method Injection** - Dependencies passed to methods
4. **Service Locator** - Request from container (anti-pattern)

### Common Violations

- Creating dependencies inside constructor
- Using `new` keyword to instantiate dependencies
- Depending directly on concrete classes
- Hard-coded implementation selection

### Implementation with Laravel

```php
// In service container (app/Providers/AppServiceProvider.php)
public function register() {
    $this->app->bind(Database::class, MySQLDatabase::class);
}

// In controller or service
public function __construct(Database $database) {
    $this->database = $database;
}
```

### Practice Exercise

**Build a notification system:**
- Create notification interface
- Implement different notification types
- Use DIP in NotificationService
- Use Laravel container for dependency injection
- Easy to add new notification types

---

## Combining Principles

### How SOLID Principles Work Together

The five SOLID principles are interconnected and reinforce each other.

### SRP + OCP

```
SRP: UserValidator validates only users
OCP: Add new validation without modifying UserValidator
Together: Clean, extensible validation
```

### OCP + LSP

```
OCP: Extend with PaymentMethod interface
LSP: All payment methods work identically
Together: Safe extensions that don't break code
```

### ISP + DIP

```
ISP: Small, focused PaymentMethod interface
DIP: Depend on PaymentMethod interface
Together: Flexible, testable payment processing
```

### Real-World: Complete Example

```php
// SRP: Each class has single responsibility
interface PaymentMethod {
    public function charge($amount);
}

class StripePayment implements PaymentMethod {
    public function charge($amount) { }
}

class PayPalPayment implements PaymentMethod {
    public function charge($amount) { }
}

// OCP: Add new payment without modifying existing
class SquarePayment implements PaymentMethod {
    public function charge($amount) { }
}

// ISP: Small, focused interface
// LSP: All payments work the same way
// DIP: Service depends on interface, not concrete class
class PaymentService {
    private $paymentMethod;
    
    public function __construct(PaymentMethod $paymentMethod) {
        $this->paymentMethod = $paymentMethod;
    }
    
    public function process($amount) {
        return $this->paymentMethod->charge($amount);
    }
}

// Usage
$payment = new PaymentService(new StripePayment());
$payment->process(100);

// Easy to switch
$payment = new PaymentService(new PayPalPayment());
$payment->process(100);
```

### Benefits of Combined SOLID

- **Maintainability** - Easy to understand and modify
- **Testability** - Components tested independently
- **Extensibility** - Add features without breaking existing
- **Reusability** - Components used in different contexts
- **Scalability** - Grows without becoming unwieldy

---

## Learning Path

### Phase 1: Understanding (Week 1)
**Goal:** Understand what each principle means

1. **Single Responsibility** - One reason to change
2. **Open/Closed** - Extend without modifying
3. **Read & Understand** - No coding yet

**Time:** 2 hours
**Activity:** Read each principle, understand concepts

---

### Phase 2: Single Responsibility (Week 2)
**Goal:** Master SRP with practical examples

1. **Concept** - One responsibility per class
2. **Identify violations** - In existing code
3. **Refactoring** - Extract responsibilities
4. **Practice** - Refactor User class

**Time:** 3-4 hours
**Exercise:** Extract validation, email, logging from User class

---

### Phase 3: Open/Closed (Week 3)
**Goal:** Master OCP, build extensible code

1. **Concept** - Open for extension, closed for modification
2. **Abstraction** - Use interfaces effectively
3. **Patterns** - Strategy, Template Method, Decorator
4. **Practice** - Payment gateway system

**Time:** 3-4 hours
**Exercise:** Build notification system (Email, SMS, Push)

---

### Phase 4: Liskov Substitution (Week 4)
**Goal:** Ensure proper inheritance and substitution

1. **Concept** - Substitutability requirement
2. **Common Mistakes** - Bird-Penguin problem
3. **Correct Hierarchies** - When to use inheritance
4. **Practice** - Shape hierarchy

**Time:** 2-3 hours
**Exercise:** Implement Shape classes with correct hierarchy

---

### Phase 5: Interface Segregation (Week 5)
**Goal:** Design small, focused interfaces

1. **Concept** - Many specific vs one fat interface
2. **Fat Interfaces** - Identify and refactor
3. **Composable Interfaces** - Small, reusable interfaces
4. **Practice** - Worker interface refactoring

**Time:** 2-3 hours
**Exercise:** Break down large interface into smaller ones

---

### Phase 6: Dependency Inversion (Week 6)
**Goal:** Master DIP and Dependency Injection

1. **Concept** - Depend on abstractions
2. **Dependency Injection** - Constructor, property, method
3. **Service Containers** - Laravel example
4. **Practice** - Build with DI

**Time:** 3-4 hours
**Exercise:** Refactor to use constructor injection and interfaces

---

### Phase 7: Integration (Week 7)
**Goal:** Apply all principles together

1. **Combined Example** - All principles in one project
2. **Real Application** - Refactor existing code
3. **Framework Patterns** - See SOLID in Laravel
4. **Code Review** - Check your code against SOLID

**Time:** 4-5 hours
**Project:** Refactor an existing application using all principles

---

## Study Strategy

### For Each Principle, Cover:

1. **Concept** - What it means
2. **Why It Matters** - Benefits and purpose
3. **Problems** - What it solves
4. **PHP Examples** - ❌ Bad vs ✅ Good code
5. **Real-World Use** - Laravel, frameworks, patterns
6. **Refactoring Techniques** - How to apply
7. **Practice Exercise** - Build something
8. **Code Review** - Check existing code

### Tips for Learning

1. **Read Bad Code** - Understand violations first
2. **Refactor Progressively** - Step by step
3. **See Framework Code** - Laravel applies SOLID
4. **Practice Constantly** - Each principle via exercises
5. **Code Review** - Review others' code
6. **Combine Principles** - See how they work together
7. **Refactor Own Code** - Apply to your projects

### Anti-Patterns to Avoid

1. **Over-Engineering** - Don't apply prematurely
2. **Ignoring SOLID** - Don't write bad code
3. **One Wrong Principle** - All five work together
4. **Blind Application** - Understand the "why"

---

## Real-World PHP Applications

### Laravel Framework

**SRP:**
- Models handle data
- Services handle business logic
- Controllers handle requests

**OCP:**
- Extend services without modifying core
- Add middleware without changing framework

**LSP:**
- All models extend Eloquent Model
- All controllers can be substituted

**ISP:**
- Small contracts for specific functionality
- Implement only what you need

**DIP:**
- Service container handles dependencies
- Inject interfaces, not concrete classes

### Symfony Framework

Similar principles applied:
- Service container for DIP
- Event dispatcher (Observer pattern)
- Middleware stack
- Repository pattern for data access

### WordPress Plugins

- Hooks system (Observer pattern)
- Separate concerns in plugins
- Plugin interfaces for compatibility

---

## Common Mistakes & Solutions

### Mistake 1: Too Many Responsibilities
**Problem:** Class does everything
**Solution:** Extract to separate classes (SRP)

### Mistake 2: Hard to Extend
**Problem:** Modify existing code for new features
**Solution:** Use interfaces and abstraction (OCP)

### Mistake 3: Broken Inheritance
**Problem:** Subclass breaks when substituted
**Solution:** Ensure behavioral compatibility (LSP)

### Mistake 4: Fat Interfaces
**Problem:** Forced to implement unused methods
**Solution:** Create smaller, focused interfaces (ISP)

### Mistake 5: Tightly Coupled
**Problem:** Hard to test, hard to change
**Solution:** Inject dependencies via interface (DIP)

---

## Resources

### Official & Standards
- PSR-1, PSR-12 - PHP Standards Recommendations
- PHP Manual - Classes and Objects
- Design Patterns: Gang of Four book

### PHP-Specific
- Laravel Contracts (SOLID in action)
- Symfony Best Practices
- Clean Code by Robert C. Martin

### Online Learning
- refactoring.guru/design-patterns
- Martin Fowler's design pattern articles
- SOLID Principles resources

---

## Assessment & Checklist

### Understanding Phase
- [ ] Can explain each principle in own words
- [ ] Understand why SOLID matters
- [ ] Know problems each principle solves

### Application Phase
- [ ] Can identify SRP violations
- [ ] Can design extensible (OCP) code
- [ ] Can verify substitutability (LSP)
- [ ] Can segregate interfaces (ISP)
- [ ] Can apply dependency injection (DIP)

### Integration Phase
- [ ] Can apply multiple principles together
- [ ] Can refactor code to be SOLID
- [ ] Can review others' code for SOLID
- [ ] Can explain trade-offs and context

### Professional Phase
- [ ] Write SOLID code naturally
- [ ] Teach others about SOLID
- [ ] Apply to real projects
- [ ] Know when to break rules (and why)

---

## Next Steps

1. Start with **Phase 1** - Read and understand all principles
2. Work through **Phase 2-6** - One principle per week
3. Complete **Phase 7** - Integrate and apply all together
4. **Refactor your code** - Apply SOLID to real projects
5. **Code reviews** - Check code against SOLID
6. **Teach others** - Solidify your understanding

---

## Conclusion

SOLID principles are foundational to professional PHP development. They work together to create code that is:

- **Maintainable** - Easy to understand and modify
- **Testable** - Easy to test in isolation
- **Extensible** - Easy to add new features
- **Reliable** - Less likely to break
- **Professional** - Industry best practices

Master these principles, and your code will be significantly better.

Good luck on your SOLID journey! 🚀
