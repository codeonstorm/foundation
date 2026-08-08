# Builder Pattern - Deep Dive

**Goal:** Construct complex objects step by step, separating construction logic from the final object representation.

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

Some objects need many fields, options, nested parts, or construction steps.

Examples:

- Building a SQL query with selected columns, joins, filters, sorting, and limits
- Creating an email with recipients, subject, body, attachments, and headers
- Creating a report with title, sections, filters, format, and delivery settings
- Configuring an HTTP request with method, URL, headers, body, timeout, and authentication
- Building a product configuration with size, color, warranty, shipping, and discounts

Without Builder, construction code often becomes hard to read.

### Real Example

```php
$email = new Email(
    'alice@example.com',
    ['bob@example.com', 'carol@example.com'],
    'Monthly Report',
    'Please review the attached report.',
    ['report.pdf'],
    ['X-Priority' => 'High'],
    true
);
```

This constructor has too many arguments. It is easy to pass values in the wrong order, and optional settings make the constructor noisy.

Builder solves this by moving construction into a readable step-by-step API.

---

## Pattern Concept

### What is Builder?

The Builder Pattern separates the construction of a complex object from the object itself.

Instead of passing many constructor arguments, the client calls meaningful builder methods. The builder stores intermediate configuration and creates the final object when `build()` is called.

### Why It Matters

- Makes complex object creation readable
- Avoids long constructors
- Handles optional settings cleanly
- Allows validation before object creation
- Can reuse construction steps
- Can create different representations from the same process

### When to Use

Use Builder when:
- An object has many optional parameters
- Construction requires multiple ordered steps
- You want a fluent, readable creation API
- You need validation before creating the final object
- You want to hide complex setup from client code
- You need to create different variants of a complex object

Avoid Builder when:
- The object has only a few required fields
- A constructor or named constructor is clearer
- The builder only mirrors setters without adding clarity
- The object can be represented with a simple value object
- Construction does not vary

---

## Structure & Components

### Pattern Diagram

```text
Client -> Builder -> Product
            ^
            |
        Director
```

### Key Components

| Component | Role |
|-----------|------|
| `Product` | Complex object being created |
| `Builder` | Interface or class that defines construction steps |
| `ConcreteBuilder` | Stores configuration and builds the product |
| `Director` | Optional object that runs a predefined build sequence |
| `Client` | Uses the builder to create the product |

### Typical Methods

- `setTitle()` - configure one property
- `addSection()` - add one part
- `withHeader()` - fluent method returning the builder
- `reset()` - clear builder state for another product
- `build()` - validate and return the final object

---

## PHP Implementation

### Basic Email Builder

```php
<?php

class Email
{
    public function __construct(
        public string $from,
        public array $to,
        public string $subject,
        public string $body,
        public array $attachments = [],
        public array $headers = [],
        public bool $html = false
    ) {
    }

    public function send(): void
    {
        echo "From: {$this->from}\n";
        echo "To: " . implode(', ', $this->to) . "\n";
        echo "Subject: {$this->subject}\n";
        echo "Body: {$this->body}\n";
    }
}

class EmailBuilder
{
    private string $from = '';
    private array $to = [];
    private string $subject = '';
    private string $body = '';
    private array $attachments = [];
    private array $headers = [];
    private bool $html = false;

    public function from(string $email): self
    {
        $this->from = $email;
        return $this;
    }

    public function to(string $email): self
    {
        $this->to[] = $email;
        return $this;
    }

    public function subject(string $subject): self
    {
        $this->subject = $subject;
        return $this;
    }

    public function body(string $body): self
    {
        $this->body = $body;
        return $this;
    }

    public function attach(string $file): self
    {
        $this->attachments[] = $file;
        return $this;
    }

    public function header(string $name, string $value): self
    {
        $this->headers[$name] = $value;
        return $this;
    }

    public function asHtml(): self
    {
        $this->html = true;
        return $this;
    }

    public function build(): Email
    {
        if ($this->from === '') {
            throw new RuntimeException('Sender is required.');
        }

        if ($this->to === []) {
            throw new RuntimeException('At least one recipient is required.');
        }

        if ($this->subject === '') {
            throw new RuntimeException('Subject is required.');
        }

        return new Email(
            $this->from,
            $this->to,
            $this->subject,
            $this->body,
            $this->attachments,
            $this->headers,
            $this->html
        );
    }
}

$email = (new EmailBuilder())
    ->from('reports@example.com')
    ->to('alice@example.com')
    ->to('bob@example.com')
    ->subject('Monthly Report')
    ->body('Please review the attached report.')
    ->attach('report.pdf')
    ->header('X-Priority', 'High')
    ->asHtml()
    ->build();

$email->send();
```

The builder makes the construction readable. It also validates required fields before returning the final `Email`.

### Proper Example: SQL Query Builder

```php
<?php

class SelectQuery
{
    public function __construct(
        private array $columns,
        private string $table,
        private array $joins,
        private array $where,
        private array $orderBy,
        private ?int $limit
    ) {
    }

    public function toSql(): string
    {
        $sql = 'SELECT ' . implode(', ', $this->columns);
        $sql .= ' FROM ' . $this->table;

        foreach ($this->joins as $join) {
            $sql .= ' ' . $join;
        }

        if ($this->where !== []) {
            $sql .= ' WHERE ' . implode(' AND ', $this->where);
        }

        if ($this->orderBy !== []) {
            $sql .= ' ORDER BY ' . implode(', ', $this->orderBy);
        }

        if ($this->limit !== null) {
            $sql .= ' LIMIT ' . $this->limit;
        }

        return $sql;
    }
}

class SelectQueryBuilder
{
    private array $columns = ['*'];
    private ?string $table = null;
    private array $joins = [];
    private array $where = [];
    private array $orderBy = [];
    private ?int $limit = null;

    public function select(string ...$columns): self
    {
        $this->columns = $columns === [] ? ['*'] : $columns;
        return $this;
    }

    public function from(string $table): self
    {
        $this->table = $table;
        return $this;
    }

    public function join(string $table, string $condition): self
    {
        $this->joins[] = "JOIN {$table} ON {$condition}";
        return $this;
    }

    public function where(string $condition): self
    {
        $this->where[] = $condition;
        return $this;
    }

    public function orderBy(string $column, string $direction = 'ASC'): self
    {
        $direction = strtoupper($direction);

        if (!in_array($direction, ['ASC', 'DESC'], true)) {
            throw new InvalidArgumentException('Sort direction must be ASC or DESC.');
        }

        $this->orderBy[] = "{$column} {$direction}";
        return $this;
    }

    public function limit(int $limit): self
    {
        if ($limit < 1) {
            throw new InvalidArgumentException('Limit must be greater than zero.');
        }

        $this->limit = $limit;
        return $this;
    }

    public function build(): SelectQuery
    {
        if ($this->table === null) {
            throw new RuntimeException('Table is required.');
        }

        return new SelectQuery(
            $this->columns,
            $this->table,
            $this->joins,
            $this->where,
            $this->orderBy,
            $this->limit
        );
    }
}

$query = (new SelectQueryBuilder())
    ->select('users.id', 'users.name', 'profiles.city')
    ->from('users')
    ->join('profiles', 'profiles.user_id = users.id')
    ->where('users.active = 1')
    ->orderBy('users.name')
    ->limit(20)
    ->build();

echo $query->toSql() . "\n";
```

Query builders are one of the most common Builder examples in PHP frameworks.

### Builder with Director

The director is optional. It is useful when you have common build recipes.

```php
<?php

class Report
{
    private string $title = '';
    private array $sections = [];
    private string $format = 'html';

    public function setTitle(string $title): void
    {
        $this->title = $title;
    }

    public function addSection(string $heading, string $content): void
    {
        $this->sections[] = [
            'heading' => $heading,
            'content' => $content,
        ];
    }

    public function setFormat(string $format): void
    {
        $this->format = $format;
    }

    public function render(): void
    {
        echo "Report: {$this->title}\n";
        echo "Format: {$this->format}\n";

        foreach ($this->sections as $section) {
            echo "{$section['heading']}: {$section['content']}\n";
        }
    }
}

interface ReportBuilder
{
    public function reset(): void;

    public function setTitle(string $title): void;

    public function addSummary(): void;

    public function addDetails(): void;

    public function setFormat(string $format): void;

    public function getReport(): Report;
}

class SalesReportBuilder implements ReportBuilder
{
    private Report $report;

    public function __construct()
    {
        $this->reset();
    }

    public function reset(): void
    {
        $this->report = new Report();
    }

    public function setTitle(string $title): void
    {
        $this->report->setTitle($title);
    }

    public function addSummary(): void
    {
        $this->report->addSection('Summary', 'Sales summary goes here.');
    }

    public function addDetails(): void
    {
        $this->report->addSection('Details', 'Sales details go here.');
    }

    public function setFormat(string $format): void
    {
        $this->report->setFormat($format);
    }

    public function getReport(): Report
    {
        $result = $this->report;
        $this->reset();

        return $result;
    }
}

class ReportDirector
{
    public function buildExecutiveReport(ReportBuilder $builder): Report
    {
        $builder->setTitle('Executive Sales Report');
        $builder->addSummary();
        $builder->setFormat('pdf');

        return $builder->getReport();
    }

    public function buildDetailedReport(ReportBuilder $builder): Report
    {
        $builder->setTitle('Detailed Sales Report');
        $builder->addSummary();
        $builder->addDetails();
        $builder->setFormat('xlsx');

        return $builder->getReport();
    }
}

$director = new ReportDirector();
$builder = new SalesReportBuilder();

$report = $director->buildDetailedReport($builder);
$report->render();
```

The director knows common construction sequences. The builder knows how to create the actual report.

### Immutable Product Builder

Builders work especially well when the final object should be immutable.

```php
<?php

final class HttpRequest
{
    public function __construct(
        public readonly string $method,
        public readonly string $url,
        public readonly array $headers,
        public readonly ?string $body,
        public readonly int $timeout
    ) {
    }
}

class HttpRequestBuilder
{
    private string $method = 'GET';
    private ?string $url = null;
    private array $headers = [];
    private ?string $body = null;
    private int $timeout = 30;

    public function method(string $method): self
    {
        $this->method = strtoupper($method);
        return $this;
    }

    public function url(string $url): self
    {
        $this->url = $url;
        return $this;
    }

    public function header(string $name, string $value): self
    {
        $this->headers[$name] = $value;
        return $this;
    }

    public function json(array $payload): self
    {
        $this->body = json_encode($payload);
        $this->headers['Content-Type'] = 'application/json';
        return $this;
    }

    public function timeout(int $seconds): self
    {
        $this->timeout = $seconds;
        return $this;
    }

    public function build(): HttpRequest
    {
        if ($this->url === null) {
            throw new RuntimeException('URL is required.');
        }

        return new HttpRequest(
            $this->method,
            $this->url,
            $this->headers,
            $this->body,
            $this->timeout
        );
    }
}

$request = (new HttpRequestBuilder())
    ->method('post')
    ->url('https://api.example.com/users')
    ->json(['name' => 'Alice'])
    ->timeout(10)
    ->build();
```

The `HttpRequest` object cannot be changed after construction, but the builder keeps creation flexible.

---

## Real-World Scenarios

### Scenario 1: Query Builders

Laravel, Doctrine, and many database libraries use builder-style APIs for constructing queries step by step.

### Scenario 2: HTTP Requests

HTTP clients often use builders for method, URL, headers, body, authentication, retries, and timeout.

### Scenario 3: Email and Notification Messages

Emails and notifications often have optional recipients, attachments, headers, templates, variables, and delivery settings.

### Scenario 4: Reports and Documents

Reports can be built from reusable steps such as title, filters, charts, tables, sections, and output format.

### Scenario 5: Test Data Builders

Tests often use builders to create valid domain objects while overriding only the fields relevant to a test.

---

## Pros & Cons

### Advantages

- Replaces long constructors with readable steps
- Handles optional parameters cleanly
- Can validate before object creation
- Makes complex construction reusable
- Works well with immutable objects
- Improves test setup readability
- Supports fluent interfaces

### Disadvantages

- Adds another class
- Can be overkill for simple objects
- Builder state can accidentally leak between builds
- Poorly named builder methods can hide required fields
- Mutable builders are not always safe to reuse
- Can duplicate validation already present in the product

---

## Best Practices

1. Use Builder for objects with real construction complexity.
2. Keep builder methods meaningful and domain-specific.
3. Validate required fields in `build()`.
4. Make the final product immutable when possible.
5. Reset builder state after building if the builder is reusable.
6. Avoid making builders global mutable objects.
7. Keep product invariants inside the product when they are essential.
8. Use fluent methods only when they improve readability.

### Good Builder Design

```php
<?php

$invoice = (new InvoiceBuilder())
    ->forCustomer($customer)
    ->addItem('Keyboard', 2, 1200)
    ->addItem('Mouse', 1, 500)
    ->withTaxRate(18)
    ->build();
```

The construction reads like the business concept instead of a list of anonymous constructor arguments.

---

## Common Pitfalls

### Pitfall 1: Builder Mirrors Every Setter

If the builder only repeats simple setters, it may not add value.

### Pitfall 2: No Validation

```php
$query = (new SelectQueryBuilder())->where('active = 1')->build();
```

If `from()` is required, the builder should fail clearly instead of creating an invalid product.

### Pitfall 3: Reusing Dirty Builder State

If a builder keeps old values, a second product may accidentally include settings from the first product.

Use `reset()` or create a new builder.

### Pitfall 4: Putting Business Workflow in the Builder

A builder should construct an object. It should not process payment, send email, or save to the database.

### Pitfall 5: Hiding Required Fields

If required fields are easy to miss, consider constructor arguments for required fields and builder methods for optional parts.

---

## Variants

### Fluent Builder

Methods return `$this`, allowing chained calls.

```php
$email = $builder->from('a@example.com')->to('b@example.com')->build();
```

### Director-Based Builder

A director contains standard build recipes and calls builder steps in a known order.

### Test Data Builder

Used in tests to create valid objects with sensible defaults.

```php
$user = UserBuilder::aUser()->withRole('admin')->build();
```

### Immutable Builder

Each builder method returns a cloned builder instead of mutating the same builder.

### Step Builder

A stricter builder design where interfaces enforce the order of required steps.

### Builder + Factory

A factory can choose the correct builder, and the builder handles detailed construction.

---

## Practice Exercises

### Exercise 1: Email Builder

Create an `EmailBuilder` that supports sender, recipients, subject, body, attachments, headers, and HTML mode.

### Exercise 2: Query Builder

Build a `SelectQueryBuilder` that supports `select`, `from`, `where`, `join`, `orderBy`, and `limit`.

### Exercise 3: Invoice Builder

Create an invoice builder with customer details, items, tax, discount, payment terms, and due date.

### Exercise 4: HTTP Request Builder

Create an immutable `HttpRequest` object and a builder for method, URL, headers, JSON body, timeout, and authentication.

### Exercise 5: Test Data Builder

Build a `UserBuilder` for tests with default valid values and methods like `asAdmin()`, `withEmail()`, and `inactive()`.

---

## Summary

The Builder Pattern constructs complex objects step by step.

It is useful when constructors become long, optional parameters are common, or object creation needs validation and readable setup.

In PHP, Builder is often seen in query builders, HTTP clients, email builders, report builders, and test data builders. Use it when it makes construction clearer, not just because an object has setters.
