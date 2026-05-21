# Factory Method Pattern - Deep Dive

**Goal:** Define an interface for creating an object, but let subclasses decide which class to instantiate.

---

## Table of Contents

1. [Problem Statement](#problem-statement)
2. [Pattern Concept](#pattern-concept)
3. [Structure & Components](#structure--components)
4. [PHP Implementation](#php-implementation)
5. [Real-World Scenarios](#real-world-scenarios)
6. [Pros & Cons](#pros--cons)
7. [Best Practices](#best-practices)
8. [Common Pitfalls](#common-pitfalls)
9. [Variants](#variants)
10. [Practice Exercises](#practice-exercises)

---

## Problem Statement

### The Challenge

In many systems, you need to create objects, but the concrete type depends on runtime data, configuration, or environment.

Without Factory Method:
- Client code is coupled to concrete classes
- Object creation logic is scattered across the system
- Adding new product types requires changes in many places
- Code becomes harder to maintain and extend

### Real Example

```php
$paymentType = 'paypal';

if ($paymentType === 'stripe') {
    $processor = new StripePayment();
} elseif ($paymentType === 'paypal') {
    $processor = new PayPalPayment();
} else {
    throw new Exception('Unsupported payment type');
}
```

This logic should be centralized so the client depends on an abstraction instead of concrete constructors.

---

## Pattern Concept

### What is Factory Method?

The Factory Method Pattern defines an interface for creating an object, but lets subclasses decide which class to instantiate.

### Why It Matters

- Decouples client code from concrete classes
- Centralizes object creation logic
- Supports open/closed principle: add new types without changing client code
- Makes code easier to extend and maintain

### When to Use

✅ Use Factory Method when:
- You need to create objects from a family of related types
- Object type depends on configuration or runtime state
- You want to isolate creation logic from business logic
- You need to support extensible product types

❌ Avoid when:
- You only need one concrete type
- Object construction is simple and static
- Factory adds unnecessary complexity

---

## Structure & Components

### Pattern Diagram

```
Creator -> Product
  |         ^
  |         |
  v      ConcreteProduct
ConcreteCreator
```

### Key Components

| Component | Role |
|-----------|------|
| `Creator` | Declares factory method and may contain default behavior |
| `ConcreteCreator` | Overrides factory method to create specific products |
| `Product` | Interface or abstract class for product objects |
| `ConcreteProduct` | Implements product interface |

### Typical Methods

- `createProduct()` — Factory method in creator
- `someOperation()` — Business operation using product via abstraction

---

## PHP Implementation

### Basic Factory Method Example

```php
<?php
interface Button
{
    public function render(): string;
}

class HTMLButton implements Button
{
    public function render(): string
    {
        return '<button>HTML Button</button>';
    }
}

class WindowsButton implements Button
{
    public function render(): string
    {
        return '<button>Windows Button</button>';
    }
}

abstract class Dialog
{
    abstract public function createButton(): Button;

    public function render(): string
    {
        $button = $this->createButton();
        return $button->render();
    }
}

class HTMLDialog extends Dialog
{
    public function createButton(): Button
    {
        return new HTMLButton();
    }
}

class WindowsDialog extends Dialog
{
    public function createButton(): Button
    {
        return new WindowsButton();
    }
}

function clientCode(Dialog $dialog): void
{
    echo $dialog->render();
}

clientCode(new HTMLDialog());
clientCode(new WindowsDialog());
```

### Factory Method with Runtime Selection

```php
<?php
interface Logger
{
    public function log(string $message): void;
}

class FileLogger implements Logger
{
    public function log(string $message): void
    {
        echo "Write to file: {$message}\n";
    }
}

class DatabaseLogger implements Logger
{
    public function log(string $message): void
    {
        echo "Write to database: {$message}\n";
    }
}

abstract class LoggerFactory
{
    abstract public function createLogger(): Logger;
}

class FileLoggerFactory extends LoggerFactory
{
    public function createLogger(): Logger
    {
        return new FileLogger();
    }
}

class DatabaseLoggerFactory extends LoggerFactory
{
    public function createLogger(): Logger
    {
        return new DatabaseLogger();
    }
}

function logMessage(LoggerFactory $factory, string $message): void
{
    $logger = $factory->createLogger();
    $logger->log($message);
}

logMessage(new FileLoggerFactory(), 'File log event');
logMessage(new DatabaseLoggerFactory(), 'Database log event');
```

### Factory Method Using Configuration

```php
<?php
interface PaymentMethod
{
    public function pay(float $amount): string;
}

class StripePayment implements PaymentMethod
{
    public function pay(float $amount): string
    {
        return "Paid ".$amount." with Stripe";
    }
}

class PayPalPayment implements PaymentMethod
{
    public function pay(float $amount): string
    {
        return "Paid ".$amount." with PayPal";
    }
}

abstract class PaymentFactory
{
    abstract public function createPaymentMethod(): PaymentMethod;
}

class StripeFactory extends PaymentFactory
{
    public function createPaymentMethod(): PaymentMethod
    {
        return new StripePayment();
    }
}

class PayPalFactory extends PaymentFactory
{
    public function createPaymentMethod(): PaymentMethod
    {
        return new PayPalPayment();
    }
}

function processPayment(PaymentFactory $factory, float $amount): void
{
    $payment = $factory->createPaymentMethod();
    echo $payment->pay($amount)."\n";
}

$config = 'stripe';
$factory = $config === 'stripe' ? new StripeFactory() : new PayPalFactory();
processPayment($factory, 100.00);
```

---

## Real-World Scenarios

### Scenario 1: UI Component Factory

Create buttons, menus, dialogs, and controls for different platforms while keeping client code platform-agnostic.

### Scenario 2: Message Parsers

Use the factory method to instantiate parsers for JSON, XML, CSV, or other formats.

### Scenario 3: Data Source Connectors

Produce concrete connectors for MySQL, PostgreSQL, SQLite, or external APIs based on config.

### Scenario 4: Reporting Engines

Create reports in PDF, HTML, or Excel from a common interface.

---

## Pros & Cons

### Advantages ✅

- Decouples product creation from usage
- Compatible with open/closed principle
- Centralizes object creation logic
- Easy to add new product types
- Improves testability by using interfaces

### Disadvantages ❌

- More classes and boilerplate
- Can overcomplicate simple object creation
- Subclass proliferation
- Indirection makes code harder to follow if overused

---

## Best Practices

1. Keep factory methods small and focused.
2. Use interfaces or abstract products for type safety.
3. Keep client code dependent on abstractions.
4. Prefer composition over inheritance if subclassing is not required.
5. Use configuration or dependency injection to select concrete factories.
6. Document the purpose of each factory and product.

---

## Common Pitfalls

### Pitfall 1: Overuse

Using Factory Method for trivial object creation adds unnecessary complexity.

### Pitfall 2: Tight Coupling to Creator

If client code depends on concrete creators, the pattern loses its benefit.

### Pitfall 3: Hidden Logic

Avoid putting too much business logic inside the factory method itself.

### Pitfall 4: Too Many Subclasses

Avoid creating a subclass for every tiny case; use configuration or simpler factories when appropriate.

---

## Variants

### Simple Factory

A simple factory is not a formal pattern but centralizes object creation in one class. It is often used as a stepping stone before a full Factory Method.

### Abstract Factory

Abstract Factory creates families of related objects, while Factory Method focuses on creating a single product.

### Dependency Injection

Using a DI container can replace some use cases of Factory Method with configuration-based object creation.

---

## Practice Exercises

### Exercise 1: Payment Processor

Implement a `PaymentFactory` that creates either `StripePayment`, `PayPalPayment`, or `BankTransferPayment` based on runtime input.

### Exercise 2: Parser Factory

Build a `ParserFactory` that returns a parser for JSON, XML, or CSV input.

### Exercise 3: Notification Factory

Create a `NotificationFactory` that produces email, SMS, or push notification senders.

### Exercise 4: UI Factory

Create a `DialogFactory` that returns platform-specific dialog objects for `HTML`, `Windows`, or `Mac`.

### Exercise 5: Config-Driven Factory

Use a simple configuration file or array to select the factory implementation at runtime.

---

## Summary

The Factory Method Pattern helps you write extensible PHP code by decoupling object creation from object usage. It is ideal when the concrete type may vary over time and you want to keep client code dependent on abstractions rather than implementations.

Use it for plugin-style systems, platform-specific components, or when adding new product types should not require changing client code.

Happy building! 🚀
