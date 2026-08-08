# Adapter Pattern - Deep Dive

**Goal:** Convert the interface of a class into another interface that clients expect, letting incompatible types work together.

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

Sometimes a class you need already exists, but its interface does not match what your application expects.

Examples:

- A legacy payment gateway that exposes `pay()` instead of the `charge()` your app expects
- A third-party logging library with its own method names and signatures
- An external API that returns XML while your code works with arrays
- An old class from a previous project that cannot be modified

Without Adapter, developers often rewrite the third-party class, copy-paste its logic, or scatter conditional checks throughout client code to work around the mismatch.

### Real Example

```php
class LegacyPaymentGateway
{
    public function pay(string $account, int $amountInCents): string
    {
        echo "Charging {$amountInCents} cents to account {$account} via legacy gateway\n";
        return 'LEGACY_OK';
    }
}

interface PaymentProcessor
{
    public function charge(float $amount): bool;
}

class CheckoutService
{
    public function __construct(private PaymentProcessor $processor)
    {
    }

    public function completeOrder(float $amount): void
    {
        $this->processor->charge($amount);
    }
}

// LegacyPaymentGateway does not implement PaymentProcessor,
// and its method signature is completely different.
// $checkout = new CheckoutService(new LegacyPaymentGateway()); // Fatal error
```

The problem: `CheckoutService` expects a `PaymentProcessor` with a `charge(float): bool` method, but `LegacyPaymentGateway` only offers `pay(string, int): string`. The two cannot be used together directly.

Adapter solves this by wrapping the incompatible class in a new class that translates calls between the two interfaces, without modifying either side.

---

## Pattern Concept

### What is Adapter?

The Adapter Pattern wraps an existing class (the Adaptee) with a new class (the Adapter) that implements the interface the client expects (the Target). The adapter translates method calls and data formats between the two sides.

The client only ever talks to the Target interface and never knows an adaptation is happening underneath.

### Why It Matters

- Lets incompatible interfaces work together without modifying existing code
- Reuses legacy or third-party classes instead of rewriting them
- Isolates translation logic in one place instead of scattering it
- Keeps client code clean and dependent only on the interface it expects
- Makes it easy to swap one adaptee for another as long as adapters exist
- Supports gradual migration from an old API to a new one

### When to Use

Use Adapter when:
- You must use an existing class whose interface does not match what you need
- You are integrating a third-party library you cannot modify
- You want to reuse legacy code inside a modern architecture
- You need to convert data formats (XML to array, snake_case to camelCase, etc.) between two systems
- You want to support several similar libraries through one common interface

Avoid Adapter when:
- You can simply change the class itself to match the interface
- You need to simplify a whole subsystem of many classes — a Facade is the better fit
- You need to change an object's interface across an entire class hierarchy at design time — a Bridge separating abstraction from implementation may fit better
- The mismatch is trivial and adding a wrapper only adds noise

---

## Structure & Components

### Pattern Diagram

```text
Client -> Target Interface
              ^
              |
           Adapter
              |
              v
           Adaptee
```

### Key Components

| Component | Role |
|-----------|------|
| `Target` | Interface the client expects to work with |
| `Adaptee` | Existing class with an incompatible interface |
| `Adapter` | Implements `Target` and translates calls to `Adaptee` |
| `Client` | Uses the target interface without knowing an adapter is involved |

### Typical Methods

- `charge()` / `request()` - method defined by the target interface
- `convert()` or `translate()` - optional internal helper for data format conversion
- Constructor accepting the adaptee instance (composition-based adapter)

---

## PHP Implementation

### Example 1: Object Adapter for a Legacy Payment Gateway

An object adapter wraps the adaptee using composition and implements the target interface.

```php
<?php

interface PaymentProcessor
{
    public function charge(float $amount): bool;
}

class LegacyPaymentGateway
{
    public function pay(string $account, int $amountInCents): string
    {
        echo "Charging {$amountInCents} cents to account {$account} via legacy gateway\n";
        return 'LEGACY_OK';
    }
}

class LegacyPaymentAdapter implements PaymentProcessor
{
    public function __construct(
        private LegacyPaymentGateway $legacyGateway,
        private string $account
    ) {
    }

    public function charge(float $amount): bool
    {
        $amountInCents = (int) round($amount * 100);
        $result = $this->legacyGateway->pay($this->account, $amountInCents);

        return $result === 'LEGACY_OK';
    }
}

class CheckoutService
{
    public function __construct(private PaymentProcessor $processor)
    {
    }

    public function completeOrder(float $amount): void
    {
        $success = $this->processor->charge($amount);
        echo $success ? "Order completed\n" : "Order failed\n";
    }
}

$adapter = new LegacyPaymentAdapter(new LegacyPaymentGateway(), 'ACC-1001');
$checkout = new CheckoutService($adapter);
$checkout->completeOrder(49.99);
```

### Expected Output

```text
Charging 4999 cents to account ACC-1001 via legacy gateway
Order completed
```

### Example 2: Adapting an XML API for a JSON-Consuming Client

An adapter can also convert data formats, not just method signatures.

```php
<?php

interface WeatherClient
{
    public function getTemperature(string $city): array;
}

class LegacyXmlWeatherService
{
    public function fetchWeatherXml(string $city): string
    {
        return "<weather><city>{$city}</city><tempCelsius>27</tempCelsius></weather>";
    }
}

class XmlWeatherAdapter implements WeatherClient
{
    public function __construct(private LegacyXmlWeatherService $xmlService)
    {
    }

    public function getTemperature(string $city): array
    {
        $xml = $this->xmlService->fetchWeatherXml($city);
        $element = simplexml_load_string($xml);

        return [
            'city' => (string) $element->city,
            'temperature_c' => (float) $element->tempCelsius,
        ];
    }
}

function printWeather(WeatherClient $client, string $city): void
{
    $data = $client->getTemperature($city);
    echo "{$data['city']}: {$data['temperature_c']}°C\n";
}

$adapter = new XmlWeatherAdapter(new LegacyXmlWeatherService());
printWeather($adapter, 'Mumbai');
```

### Expected Output

```text
Mumbai: 27°C
```

### Example 3: Pluggable Adapter for Multiple Logging Libraries

A pluggable adapter lets several incompatible third-party libraries be used interchangeably through one common interface.

```php
<?php

interface Logger
{
    public function log(string $level, string $message): void;
}

// Third-party library #1
class MonoLoggerLibrary
{
    public function writeLog(string $message, string $severity): void
    {
        echo "[MonoLogger][{$severity}] {$message}\n";
    }
}

// Third-party library #2
class SimpleFileLogger
{
    public function append(string $entry): void
    {
        echo "[SimpleFileLogger] {$entry}\n";
    }
}

class MonoLoggerAdapter implements Logger
{
    public function __construct(private MonoLoggerLibrary $monoLogger)
    {
    }

    public function log(string $level, string $message): void
    {
        $this->monoLogger->writeLog($message, strtoupper($level));
    }
}

class SimpleFileLoggerAdapter implements Logger
{
    public function __construct(private SimpleFileLogger $fileLogger)
    {
    }

    public function log(string $level, string $message): void
    {
        $this->fileLogger->append("[" . strtoupper($level) . "] {$message}");
    }
}

function runApplication(Logger $logger): void
{
    $logger->log('info', 'Application started');
    $logger->log('error', 'Something went wrong');
}

runApplication(new MonoLoggerAdapter(new MonoLoggerLibrary()));
runApplication(new SimpleFileLoggerAdapter(new SimpleFileLogger()));
```

### Expected Output

```text
[MonoLogger][INFO] Application started
[MonoLogger][ERROR] Something went wrong
[SimpleFileLogger] [INFO] Application started
[SimpleFileLogger] [ERROR] Something went wrong
```

The application code depends only on `Logger` and can swap logging libraries without any change to `runApplication()`.

---

## Real-World Scenarios

### Scenario 1: Legacy System Integration

Older systems often expose interfaces designed years ago. An adapter lets new code call legacy services through a modern interface without rewriting the legacy system.

### Scenario 2: Third-Party Library Integration

Payment SDKs, mail libraries, and logging packages rarely match an application's internal interfaces exactly. Adapters wrap these libraries so the rest of the application stays vendor-agnostic.

### Scenario 3: Multiple Payment Gateways

An e-commerce platform may support Stripe, PayPal, and a legacy in-house gateway. Each gets its own adapter implementing a common `PaymentProcessor` interface, so the checkout flow works identically regardless of provider.

### Scenario 4: Cross-Format Data Adapters

Systems that exchange XML, CSV, or SOAP responses can use adapters to convert those formats into the arrays or DTOs the rest of the application expects, isolating parsing logic from business logic.

### Scenario 5: API Version Migration

When migrating from an old REST API to a new one, an adapter can present the new API through the old interface (or vice versa), letting teams migrate callers gradually instead of all at once.

---

## Pros & Cons

### Advantages

- Reuses existing or third-party code without modifying it
- Keeps client code decoupled from specific vendor implementations
- Centralizes translation logic in one place
- Makes it easy to swap adaptees behind a shared interface
- Supports gradual migration between old and new systems
- Follows the Open/Closed Principle by extending behavior through new adapters

### Disadvantages

- Adds an extra class and a layer of indirection for every adaptee
- Can accumulate many small adapter classes in large systems
- May hide subtle behavioral differences between adaptee implementations
- Overuse can make the codebase harder to navigate
- Adapter cannot fix a fundamentally incompatible adaptee (e.g. missing functionality)

---

## Best Practices

1. Keep the adapter thin — it should translate, not implement business logic.
2. Name adapters clearly, such as `LegacyPaymentAdapter` or `XmlWeatherAdapter`, so their purpose is obvious.
3. Prefer object adapters (composition) over class adapters (inheritance) in PHP.
4. Depend on the target interface everywhere in client code, never on the adaptee directly.
5. Group related adapters under a dedicated namespace or folder for discoverability.
6. Write unit tests for the adapter's translation logic, especially data format conversions.
7. Do not let adapters silently swallow errors from the adaptee — translate exceptions meaningfully.
8. Document any behavioral gaps between the target interface and what the adaptee can actually do.

### Good Adapter Design

```php
<?php

class LegacyPaymentAdapter implements PaymentProcessor
{
    public function __construct(
        private LegacyPaymentGateway $legacyGateway,
        private string $account
    ) {
    }

    public function charge(float $amount): bool
    {
        $amountInCents = (int) round($amount * 100);
        $result = $this->legacyGateway->pay($this->account, $amountInCents);

        return $result === 'LEGACY_OK';
    }
}
```

This adapter does one job: convert a `float` amount into cents and translate the legacy string result into a boolean, while implementing the exact interface the client expects.

---

## Common Pitfalls

### Pitfall 1: Adapter Leaks Adaptee Details

If the adapter's methods return adaptee-specific types or error codes, the client is no longer fully decoupled.

```php
// Bad: leaks the legacy string result
public function charge(float $amount): string
{
    return $this->legacyGateway->pay($this->account, (int) ($amount * 100));
}
```

The adapter should return values that match the target interface exactly, not whatever the adaptee happens to produce.

### Pitfall 2: Putting Business Logic Inside the Adapter

An adapter should translate calls, not decide discounts, validate orders, or send emails. Business logic belongs in the client or a dedicated service.

### Pitfall 3: One Adapter Trying to Wrap Too Many Adaptees

A single adapter that conditionally wraps several unrelated libraries becomes hard to maintain. Prefer one adapter per adaptee, unified behind a common interface, as shown in the pluggable logging example.

### Pitfall 4: Confusing Adapter with Facade or Decorator

Adapter changes an interface to match what the client expects, without adding new behavior. Facade simplifies access to a complex subsystem by providing a new, simpler interface over many classes. Decorator keeps the same interface but adds new behavior on top of an object.

```php
// Adapter: same behavior, different interface
class LegacyPaymentAdapter implements PaymentProcessor { /* translates calls */ }

// Decorator: same interface, added behavior
class LoggingPaymentDecorator implements PaymentProcessor { /* adds logging around charge() */ }
```

They can look structurally similar, but their intent is different: Adapter is about compatibility, Decorator is about extension, Facade is about simplification.

---

## Variants

### Object Adapter

Uses composition: the adapter holds a reference to the adaptee and implements the target interface. This is the standard and preferred approach in PHP, since it works with any adaptee, including ones without a shared parent class.

### Class Adapter

Uses inheritance to adapt an interface. Since PHP has no multiple inheritance, a true class adapter would need to extend the adaptee directly, which is restrictive and rarely practical. PHP developers sometimes approximate the idea using traits to mix in adaptee behavior, but the object adapter approach is generally preferred for flexibility.

### Two-Way Adapter

An adapter that implements both the target and the adaptee's interface, allowing objects to be used interchangeably in either context. Useful during incremental migrations where old and new code must both call the same object.

### Pluggable Adapter

An adapter designed so that multiple interchangeable third-party implementations can be swapped behind one common interface, as shown in the logging example. This is common in frameworks that support many drivers (cache, queue, logging, storage) through one contract.

---

## Practice Exercises

### Exercise 1: Legacy SMS Gateway

Create a `Notifier` interface with `send(string $message): bool`.

Adapt a `LegacySmsGateway` class that only exposes `dispatch(string $text, string $number): int` (returns `1` for success).

### Exercise 2: CSV to Array Adapter

Create a `DataSource` interface with `getRecords(): array`.

Adapt a `LegacyCsvExporter` class that only exposes `exportCsv(): string` (a raw CSV string), converting it into an array of associative rows.

### Exercise 3: Multiple Payment Gateways

Create a `PaymentProcessor` interface and adapters for two unrelated classes: `StripeSdkClient` and `PaypalSdkClient`, each with different method names and signatures.

### Exercise 4: Two-Way Adapter for API Migration

Create an `OldReportApi` and a `NewReportApi` with different method signatures. Build a two-way adapter that lets code written against either interface call the same underlying object.

### Exercise 5: Compare Adapter and Facade

Build an `Adapter` that makes a single legacy class match a `PaymentProcessor` interface, and a `Facade` that simplifies calls across three separate subsystem classes (inventory, payment, shipping).

Write down how their intent differs.

---

## Summary

The Adapter Pattern lets you reuse existing classes and third-party libraries even when their interfaces do not match what your application expects.

By wrapping the incompatible class behind a translator that implements the interface your client already understands, you avoid rewriting working code and keep the rest of the system decoupled from vendor-specific details.

Use Adapter whenever you must integrate legacy systems, third-party SDKs, or mismatched data formats, and prefer object adapters built through composition in PHP.
