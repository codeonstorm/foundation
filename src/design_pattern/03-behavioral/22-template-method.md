# Template Method Pattern - Deep Dive

**Goal:** Define the skeleton of an algorithm in a base class, while allowing subclasses to customize specific steps.

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

Several classes follow the same workflow, but some steps differ.

Examples:

- Export data as CSV, JSON, or XML
- Process different payment providers
- Generate different report formats
- Import files from different sources
- Run validation workflows with different rules

Without Template Method, each class may duplicate the same algorithm structure.

### Real Example

```php
class CsvExporter
{
    public function export(): void
    {
        echo "Load data\n";
        echo "Validate data\n";
        echo "Format as CSV\n";
        echo "Save file\n";
    }
}

class JsonExporter
{
    public function export(): void
    {
        echo "Load data\n";
        echo "Validate data\n";
        echo "Format as JSON\n";
        echo "Save file\n";
    }
}
```

The workflow is duplicated. Only the formatting step is different.

Template Method moves the shared workflow into a base class and lets subclasses provide the variable parts.

---

## Pattern Concept

### What is Template Method?

The Template Method Pattern defines an algorithm as a sequence of steps in a parent class.

Some steps are implemented by the parent class. Other steps are abstract or optional hooks that subclasses override.

### Why It Matters

- Reuses the common workflow
- Keeps algorithm order consistent
- Lets subclasses customize specific steps
- Avoids duplication across similar classes
- Makes extension points clear

### When to Use

Use Template Method when:
- Multiple classes share the same algorithm structure
- Some steps vary between subclasses
- The order of steps must stay consistent
- You want a base class to control the workflow
- You want to expose safe customization points

Avoid Template Method when:
- The workflow changes too much between classes
- Composition with Strategy would be more flexible
- Inheritance would create a rigid hierarchy
- Subclasses need to change the algorithm order

---

## Structure & Components

### Pattern Diagram

```text
AbstractClass
  - templateMethod()
  - stepOne()
  - stepTwo()
  - hook()
        ^
        |
 ConcreteClass
```

### Key Components

| Component | Role |
|-----------|------|
| `AbstractClass` | Defines the template method and shared steps |
| `templateMethod()` | Fixed algorithm skeleton |
| `PrimitiveOperation` | Required step implemented by subclasses |
| `Hook` | Optional step subclasses may override |
| `ConcreteClass` | Implements variable steps |

### Typical Methods

- `process()` - common name for the template method
- `beforeProcess()` - optional hook
- `loadData()` - shared or abstract step
- `formatData()` - subclass-specific step
- `afterProcess()` - optional hook

---

## PHP Implementation

### Basic Export Template

```php
<?php

abstract class DataExporter
{
    final public function export(): void
    {
        $data = $this->loadData();
        $this->validate($data);
        $formatted = $this->format($data);
        $this->save($formatted);
    }

    protected function loadData(): array
    {
        return [
            ['name' => 'Alice', 'total' => 1200],
            ['name' => 'Bob', 'total' => 800],
        ];
    }

    protected function validate(array $data): void
    {
        if ($data === []) {
            throw new RuntimeException('No data to export.');
        }
    }

    abstract protected function format(array $data): string;

    protected function save(string $content): void
    {
        echo $content . "\n";
    }
}

class CsvExporter extends DataExporter
{
    protected function format(array $data): string
    {
        $lines = ['name,total'];

        foreach ($data as $row) {
            $lines[] = $row['name'] . ',' . $row['total'];
        }

        return implode("\n", $lines);
    }
}

class JsonExporter extends DataExporter
{
    protected function format(array $data): string
    {
        return json_encode($data, JSON_PRETTY_PRINT);
    }
}

$csv = new CsvExporter();
$csv->export();

$json = new JsonExporter();
$json->export();
```

The base class controls the algorithm. Subclasses only decide how to format the data.

### Proper Example: Payment Processing Template

```php
<?php

abstract class PaymentProcessor
{
    final public function process(float $amount): void
    {
        $this->validateAmount($amount);
        $this->beforeCharge($amount);
        $transactionId = $this->charge($amount);
        $this->recordTransaction($transactionId, $amount);
        $this->afterCharge($transactionId);
    }

    protected function validateAmount(float $amount): void
    {
        if ($amount <= 0) {
            throw new InvalidArgumentException('Amount must be greater than zero.');
        }
    }

    protected function beforeCharge(float $amount): void
    {
        // Optional hook.
    }

    abstract protected function charge(float $amount): string;

    protected function recordTransaction(string $transactionId, float $amount): void
    {
        echo "Recorded transaction {$transactionId} for {$amount}\n";
    }

    protected function afterCharge(string $transactionId): void
    {
        // Optional hook.
    }
}

class StripeProcessor extends PaymentProcessor
{
    protected function beforeCharge(float $amount): void
    {
        echo "Prepare Stripe request for {$amount}\n";
    }

    protected function charge(float $amount): string
    {
        echo "Charge with Stripe\n";
        return 'stripe_' . uniqid();
    }
}

class PayPalProcessor extends PaymentProcessor
{
    protected function charge(float $amount): string
    {
        echo "Charge with PayPal\n";
        return 'paypal_' . uniqid();
    }

    protected function afterCharge(string $transactionId): void
    {
        echo "Send PayPal receipt for {$transactionId}\n";
    }
}

$stripe = new StripeProcessor();
$stripe->process(1500);

$paypal = new PayPalProcessor();
$paypal->process(900);
```

The base processor guarantees validation, charging, recording, and final hooks happen in the correct order.

### Report Generator Example

```php
<?php

abstract class ReportGenerator
{
    final public function generate(): void
    {
        $this->open();
        $this->writeHeader();
        $this->writeBody();
        $this->writeFooter();
        $this->close();
    }

    protected function open(): void
    {
        echo "Open report\n";
    }

    abstract protected function writeHeader(): void;

    abstract protected function writeBody(): void;

    protected function writeFooter(): void
    {
        echo "Generated by system\n";
    }

    protected function close(): void
    {
        echo "Close report\n";
    }
}

class SalesReport extends ReportGenerator
{
    protected function writeHeader(): void
    {
        echo "Sales Report\n";
    }

    protected function writeBody(): void
    {
        echo "Sales numbers go here\n";
    }
}

class InventoryReport extends ReportGenerator
{
    protected function writeHeader(): void
    {
        echo "Inventory Report\n";
    }

    protected function writeBody(): void
    {
        echo "Stock levels go here\n";
    }
}
```

---

## Real-World Scenarios

### Scenario 1: Export Workflows

CSV, JSON, PDF, and XML exporters can share loading, validation, and saving while changing only formatting.

### Scenario 2: Payment Providers

Payment providers can share validation, transaction recording, and receipt handling while customizing charge logic.

### Scenario 3: Import Pipelines

File importers can share open, parse, validate, transform, and save steps.

### Scenario 4: Test Frameworks

Test frameworks often use setup, test, and teardown lifecycle methods.

### Scenario 5: Framework Controllers

Base controllers may define common request handling while subclasses customize specific actions.

---

## Pros & Cons

### Advantages

- Reduces duplicated workflow code
- Enforces algorithm order
- Makes extension points explicit
- Keeps shared logic in one place
- Works well for lifecycle methods
- Allows hooks before and after important steps

### Disadvantages

- Uses inheritance, which can become rigid
- Subclasses are coupled to the base class
- Too many hooks can make flow confusing
- Changing the base algorithm can affect all subclasses
- Strategy may be better when behavior must be composed dynamically

---

## Best Practices

1. Mark the template method as `final` when subclasses should not change the algorithm order.
2. Keep the base workflow clear and short.
3. Use abstract methods for required steps.
4. Use hooks for optional customization.
5. Avoid too many protected methods.
6. Keep subclass responsibilities narrow.
7. Prefer Strategy if you need runtime composition instead of inheritance.

### Good Template Method Design

```php
<?php

abstract class Importer
{
    final public function import(): void
    {
        $data = $this->read();
        $data = $this->transform($data);
        $this->save($data);
    }

    abstract protected function read(): array;

    abstract protected function transform(array $data): array;

    protected function save(array $data): void
    {
        echo "Save imported data\n";
    }
}
```

The algorithm order is fixed, while important steps remain customizable.

---

## Common Pitfalls

### Pitfall 1: Not Marking the Template Method Final

If subclasses override the main workflow, the pattern loses its value.

### Pitfall 2: Too Many Hooks

If every line of the algorithm is a hook, the base class no longer gives a useful structure.

### Pitfall 3: Base Class Knows Too Much

The base class should define the workflow, not know every detail of every subclass.

### Pitfall 4: Inheritance When Composition Is Better

If you need to swap one step at runtime, Strategy may be a better fit.

---

## Variants

### Hook Methods

Optional methods with default behavior that subclasses may override.

### Abstract Steps

Required methods that subclasses must implement.

### Template Method + Strategy

Use Template Method for the fixed workflow and Strategy for one variable step.

### Lifecycle Template

Common in frameworks: `setUp()`, `run()`, `tearDown()`.

---

## Practice Exercises

### Exercise 1: Data Exporter

Create a base exporter with steps for loading, validating, formatting, and saving. Implement CSV and JSON exporters.

### Exercise 2: Payment Template

Create payment processors for Stripe, PayPal, and bank transfer using a shared processing workflow.

### Exercise 3: Import Pipeline

Create importers for CSV and XML files. Share validation and save logic.

### Exercise 4: Report Generator

Create a report template with header, body, footer, and export steps. Implement sales and inventory reports.

### Exercise 5: Add Hooks

Add optional `beforeProcess()` and `afterProcess()` hooks to one of your examples.

---

## Summary

The Template Method Pattern defines a fixed algorithm in a base class and lets subclasses customize selected steps.

It is useful when several classes share the same workflow but differ in specific parts.

Use it when the workflow order should be controlled by the parent class. Consider Strategy when behavior should be swapped at runtime.

