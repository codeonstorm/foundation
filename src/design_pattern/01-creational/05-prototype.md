# Prototype Pattern - Deep Dive

**Goal:** Create new objects by cloning existing objects instead of building them from scratch.

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

Sometimes creating an object is expensive, repetitive, or complicated.

Examples:

- A report object has many default settings
- A document template has predefined sections
- A product configuration has nested options
- A game object has default stats and assets
- A query object has a long base configuration

Without Prototype, client code often repeats the same setup again and again.

### Real Example

```php
$invoice = new Document();
$invoice->setType('invoice');
$invoice->setFont('Arial');
$invoice->setPageSize('A4');
$invoice->setHeader('Company Invoice');
$invoice->addSection('Customer Details');
$invoice->addSection('Line Items');
$invoice->addSection('Payment Terms');

$anotherInvoice = new Document();
$anotherInvoice->setType('invoice');
$anotherInvoice->setFont('Arial');
$anotherInvoice->setPageSize('A4');
$anotherInvoice->setHeader('Company Invoice');
$anotherInvoice->addSection('Customer Details');
$anotherInvoice->addSection('Line Items');
$anotherInvoice->addSection('Payment Terms');
```

The setup is duplicated. If the invoice template changes, every object creation block must be updated.

Prototype solves this by preparing one object as a template, then cloning it whenever a similar object is needed.

---

## Pattern Concept

### What is Prototype?

The Prototype Pattern creates new objects by copying existing objects.

Instead of asking a class to build a new object from zero, you keep a ready-made prototype object and call `clone` when you need a copy.

### Why It Matters

- Avoids repeated setup code
- Reduces expensive initialization
- Makes object creation more flexible
- Allows runtime object templates
- Helps create similar objects with small differences

### When to Use

Use Prototype when:
- Object creation is expensive
- Objects share a lot of default configuration
- You need many similar objects
- The exact class is chosen at runtime
- You want to avoid subclassing just to create configured objects
- You need template-style creation, such as documents or product presets

Avoid Prototype when:
- Objects are simple to create
- Object state is small and obvious
- Cloning creates confusing shared references
- A factory or constructor is clearer
- Object identity is important and should not be copied accidentally

---

## Structure & Components

### Pattern Diagram

```text
Client -> Prototype
             |
             | clone
             v
        New Object Copy
```

### Key Components

| Component | Role |
|-----------|------|
| `Prototype` | Interface or class that supports cloning |
| `ConcretePrototype` | Real object that can be copied |
| `Client` | Clones the prototype and customizes the copy |
| `PrototypeRegistry` | Optional storage for named prototypes |

### PHP Cloning Basics

In PHP, objects are cloned with the `clone` keyword.

```php
$copy = clone $original;
```

PHP performs a shallow copy by default. That means the top-level object is copied, but nested objects are still shared unless you clone them manually inside `__clone()`.

---

## PHP Implementation

### Basic Prototype Example

This example creates a report template and clones it for monthly reports.

```php
<?php

class Report
{
    private string $title;
    private string $format;
    private array $sections = [];

    public function __construct(string $title, string $format)
    {
        $this->title = $title;
        $this->format = $format;
    }

    public function setTitle(string $title): void
    {
        $this->title = $title;
    }

    public function addSection(string $section): void
    {
        $this->sections[] = $section;
    }

    public function render(): void
    {
        echo "Report: {$this->title}\n";
        echo "Format: {$this->format}\n";
        echo "Sections: " . implode(', ', $this->sections) . "\n";
    }
}

$monthlyTemplate = new Report('Monthly Report Template', 'PDF');
$monthlyTemplate->addSection('Summary');
$monthlyTemplate->addSection('Sales');
$monthlyTemplate->addSection('Expenses');
$monthlyTemplate->addSection('Profit');

$januaryReport = clone $monthlyTemplate;
$januaryReport->setTitle('January Report');

$februaryReport = clone $monthlyTemplate;
$februaryReport->setTitle('February Report');

$januaryReport->render();
$februaryReport->render();
```

The client does not rebuild every section. It clones the prepared template and changes only what is different.

### Proper Example: Document Template with Deep Clone

This example shows a more realistic Prototype implementation. A document has nested `Section` objects. If we do not clone the sections, the original and copied documents will share the same section objects.

```php
<?php

class Section
{
    public function __construct(
        private string $heading,
        private string $content
    ) {
    }

    public function setContent(string $content): void
    {
        $this->content = $content;
    }

    public function render(): string
    {
        return "{$this->heading}: {$this->content}";
    }
}

class Document
{
    private string $id;
    private string $title;
    private array $sections = [];

    public function __construct(string $title)
    {
        $this->id = uniqid('doc_', true);
        $this->title = $title;
    }

    public function addSection(Section $section): void
    {
        $this->sections[] = $section;
    }

    public function setTitle(string $title): void
    {
        $this->title = $title;
    }

    public function updateSection(int $index, string $content): void
    {
        if (!isset($this->sections[$index])) {
            throw new InvalidArgumentException('Section does not exist.');
        }

        $this->sections[$index]->setContent($content);
    }

    public function __clone()
    {
        $this->id = uniqid('doc_', true);

        foreach ($this->sections as $index => $section) {
            $this->sections[$index] = clone $section;
        }
    }

    public function render(): void
    {
        echo "Document ID: {$this->id}\n";
        echo "Title: {$this->title}\n";

        foreach ($this->sections as $section) {
            echo "- " . $section->render() . "\n";
        }
    }
}

$invoiceTemplate = new Document('Invoice Template');
$invoiceTemplate->addSection(new Section('Customer', 'Customer details go here'));
$invoiceTemplate->addSection(new Section('Items', 'Line items go here'));
$invoiceTemplate->addSection(new Section('Terms', 'Payment terms go here'));

$invoiceForAlice = clone $invoiceTemplate;
$invoiceForAlice->setTitle('Invoice for Alice');
$invoiceForAlice->updateSection(0, 'Alice - alice@example.com');

$invoiceForBob = clone $invoiceTemplate;
$invoiceForBob->setTitle('Invoice for Bob');
$invoiceForBob->updateSection(0, 'Bob - bob@example.com');

$invoiceTemplate->render();
$invoiceForAlice->render();
$invoiceForBob->render();
```

### Why `__clone()` Matters

The `__clone()` method runs automatically after an object is cloned.

Use it to:

- Generate a new ID
- Reset timestamps
- Clone nested objects
- Clear temporary state
- Detach database identity
- Reset cached values

Without `__clone()`, nested objects may be shared between the original and the clone.

### Shallow Copy Problem

This example shows the common mistake.

```php
<?php

class Address
{
    public function __construct(public string $city)
    {
    }
}

class Customer
{
    public function __construct(
        public string $name,
        public Address $address
    ) {
    }
}

$customer1 = new Customer('Alice', new Address('Delhi'));
$customer2 = clone $customer1;

$customer2->name = 'Bob';
$customer2->address->city = 'Mumbai';

echo $customer1->address->city . "\n"; // Mumbai
echo $customer2->address->city . "\n"; // Mumbai
```

The top-level customer object was copied, but both customers still reference the same `Address` object.

### Deep Copy Fix

```php
<?php

class Address
{
    public function __construct(public string $city)
    {
    }
}

class Customer
{
    public function __construct(
        public string $name,
        public Address $address
    ) {
    }

    public function __clone()
    {
        $this->address = clone $this->address;
    }
}

$customer1 = new Customer('Alice', new Address('Delhi'));
$customer2 = clone $customer1;

$customer2->name = 'Bob';
$customer2->address->city = 'Mumbai';

echo $customer1->address->city . "\n"; // Delhi
echo $customer2->address->city . "\n"; // Mumbai
```

Now each customer has its own address object.

### Prototype Registry Example

A prototype registry stores named prototypes and returns clones when requested.

```php
<?php

interface EmailTemplate
{
    public function setRecipient(string $email): void;

    public function setBody(string $body): void;

    public function send(): void;
}

class MarketingEmail implements EmailTemplate
{
    private string $recipient = '';
    private string $subject;
    private string $body;

    public function __construct(string $subject, string $body)
    {
        $this->subject = $subject;
        $this->body = $body;
    }

    public function setRecipient(string $email): void
    {
        $this->recipient = $email;
    }

    public function setBody(string $body): void
    {
        $this->body = $body;
    }

    public function send(): void
    {
        echo "To: {$this->recipient}\n";
        echo "Subject: {$this->subject}\n";
        echo "Body: {$this->body}\n";
    }
}

class EmailTemplateRegistry
{
    private array $templates = [];

    public function register(string $name, EmailTemplate $template): void
    {
        $this->templates[$name] = $template;
    }

    public function create(string $name): EmailTemplate
    {
        if (!isset($this->templates[$name])) {
            throw new InvalidArgumentException("Template {$name} not found.");
        }

        return clone $this->templates[$name];
    }
}

$registry = new EmailTemplateRegistry();
$registry->register(
    'welcome',
    new MarketingEmail('Welcome to our app', 'Thanks for joining us.')
);

$aliceEmail = $registry->create('welcome');
$aliceEmail->setRecipient('alice@example.com');
$aliceEmail->send();

$bobEmail = $registry->create('welcome');
$bobEmail->setRecipient('bob@example.com');
$bobEmail->setBody('Welcome Bob, your account is ready.');
$bobEmail->send();
```

The registry hides the original template and gives each caller a fresh clone.

---

## Real-World Scenarios

### Scenario 1: Document Templates

Invoices, contracts, proposals, reports, and receipts often share structure. Clone a template and customize the copy.

### Scenario 2: Product Configurations

An ecommerce system may clone a base product configuration and adjust price, size, color, tax rules, or shipping options.

### Scenario 3: Game Objects

Games often clone enemy prototypes, weapon prototypes, or item prototypes, then adjust health, position, or behavior.

### Scenario 4: Query Builders

A base query can be cloned and extended for different filters without rebuilding all joins and common conditions.

### Scenario 5: Form Builders

A form template can be cloned for different pages while keeping common fields, validation rules, and layout.

---

## Pros & Cons

### Advantages

- Reduces repeated setup code
- Can avoid expensive initialization
- Creates objects at runtime without hard-coding concrete classes
- Useful for template-based object creation
- Can be simpler than many specialized factory classes
- Keeps object configuration close to the prototype

### Disadvantages

- Cloning nested objects can be tricky
- Shared references can cause surprising bugs
- Object identity fields must be reset manually
- Deep cloning complex object graphs can be expensive
- Cloning may bypass important constructor logic
- Not every object is safe or meaningful to clone

---

## Best Practices

1. Use `__clone()` to reset identity fields like IDs and timestamps.
2. Deep clone nested mutable objects.
3. Keep prototypes in a registry when you need named templates.
4. Avoid cloning live database entities unless you clearly detach identity.
5. Prefer immutable value objects when possible.
6. Document whether a prototype performs shallow or deep copying.
7. Do not use Prototype when a simple constructor is clearer.
8. Write tests proving the original and clone do not accidentally share mutable state.

### Good Prototype Design

```php
<?php

class InvoicePrototype
{
    private string $invoiceNumber;
    private array $items = [];

    public function __clone()
    {
        $this->invoiceNumber = 'INV-' . uniqid();

        foreach ($this->items as $index => $item) {
            $this->items[$index] = clone $item;
        }
    }
}
```

This clone resets identity and copies nested items, so the clone can be safely customized.

---

## Common Pitfalls

### Pitfall 1: Forgetting Deep Clone

If the original and copy share nested objects, changing the clone may accidentally change the original.

### Pitfall 2: Copying IDs

```php
$newOrder = clone $existingOrder;
```

If the order ID is copied, saving the clone could overwrite the original order. Reset IDs in `__clone()`.

### Pitfall 3: Cloning Services

Services like database connections, mailers, loggers, and API clients usually should not be cloned. They are dependencies, not prototype state.

### Pitfall 4: Bypassing Constructor Validation

Cloning does not call the constructor. If the constructor enforces important rules, make sure clones remain valid.

### Pitfall 5: Too Much Magic

If `__clone()` performs too much hidden work, developers may not understand what happens when they use `clone`.

Keep clone behavior predictable.

---

## Variants

### Simple Prototype

Clone an existing object directly and change a few fields.

```php
$copy = clone $prototype;
$copy->setName('Custom name');
```

### Prototype Registry

Store prepared prototypes by name and clone them on request.

```php
$invoice = $registry->create('invoice');
```

### Deep Prototype

The prototype clones its nested mutable objects inside `__clone()`.

### Immutable Prototype

Use immutable objects and methods like `withTitle()` that return modified copies.

```php
$newReport = $report->withTitle('March Report');
```

This is not PHP's `clone` style directly, but it serves a similar purpose: creating a modified copy.

### Factory + Prototype

A factory can choose which prototype to clone based on runtime input.

```php
$template = $factory->createTemplate('invoice');
```

---

## Practice Exercises

### Exercise 1: Invoice Template

Create an invoice template with:
- Company header
- Customer section
- Item section
- Payment terms section

Clone it for three customers and customize each copy.

### Exercise 2: Deep Clone Customer

Create a `Customer` class with an `Address` object.

Clone the customer and change the cloned address. Make sure the original address does not change.

### Exercise 3: Email Template Registry

Build a registry with templates for:
- Welcome email
- Password reset email
- Discount offer email

Each call to `create()` should return a clone.

### Exercise 4: Product Presets

Create product prototypes for:
- Basic laptop
- Gaming laptop
- Office laptop

Clone a prototype and customize RAM, storage, price, and color.

### Exercise 5: Query Prototype

Create a base query object with common filters. Clone it to create separate queries for active users, inactive users, and premium users.

---

## Summary

The Prototype Pattern creates new objects by cloning existing objects.

It is useful when objects are expensive to build, share a lot of configuration, or act like templates.

In PHP, Prototype is built around the `clone` keyword and the `__clone()` magic method. The most important thing to understand is shallow vs deep copying: clone copies the top-level object, but nested mutable objects must be cloned manually when they should not be shared.

