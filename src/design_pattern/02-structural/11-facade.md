# Facade Pattern - Deep Dive

**Goal:** Provide a single, simplified interface to a complex set of subsystem classes.

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

Real systems are rarely made of one class. A single business action, like placing an order, may require coordinating several independent subsystems:

- Checking and reserving stock in an `Inventory` service
- Charging a card through a `Payment` gateway
- Booking a courier with a `Shipping` service
- Sending a confirmation through a `Notification` service

Without a unifying layer, the client code that wants to "place an order" must know about all of these subsystems, call them in the right order, handle their individual return values, and undo work if something fails partway through. Every place that needs to place an order repeats this coordination logic.

### Real Example

```php
class Inventory
{
    public function reserve(string $sku, int $qty): bool
    {
        echo "Reserving {$qty} x {$sku}\n";
        return true;
    }
}

class Payment
{
    public function charge(float $amount): bool
    {
        echo "Charging \${$amount}\n";
        return true;
    }
}

class Shipping
{
    public function bookCourier(string $sku, int $qty): string
    {
        echo "Booking courier for {$qty} x {$sku}\n";
        return 'TRACK-12345';
    }
}

class Notification
{
    public function sendConfirmation(string $tracking): void
    {
        echo "Sending confirmation for tracking {$tracking}\n";
    }
}

// Client code has to know every subsystem and the correct order of calls.
$inventory = new Inventory();
$payment = new Payment();
$shipping = new Shipping();
$notification = new Notification();

if ($inventory->reserve('SKU-1', 2)) {
    if ($payment->charge(59.98)) {
        $tracking = $shipping->bookCourier('SKU-1', 2);
        $notification->sendConfirmation($tracking);
    }
}
```

The problem: the client is doing the job of an order-processing engine. It must understand every subsystem, call them in the correct sequence, and repeat this whenever an order needs to be placed elsewhere in the codebase.

Facade solves this by introducing one simple object that hides the subsystems behind a single method, such as `placeOrder()`. The client no longer needs to know how many subsystems exist or in what order they must run.

---

## Pattern Concept

### What is Facade?

The Facade Pattern provides a unified, higher-level interface that wraps a set of interfaces in a subsystem. The facade does not replace the subsystem classes; it simply coordinates them so the client can perform a task through one simple call.

### Why It Matters

- Reduces the number of objects the client needs to know about
- Hides the correct call order and internal coordination logic
- Provides a stable, simple entry point even if subsystems change internally
- Makes subsystems easier to reuse without leaking their complexity
- Improves readability by expressing intent (`placeOrder()`) instead of low-level steps
- Decouples client code from subsystem implementation details

### When to Use

Use Facade when:
- A subsystem has many classes with complex interactions
- Clients only need a simple, common set of operations
- You want to layer a simple API on top of a complex library or framework
- You want to reduce coupling between client code and subsystem internals

Avoid Facade when:
- The subsystem is already simple and a wrapper adds no value
- Clients genuinely need fine-grained control over subsystem classes
- A single rigid facade would force every client into the same workflow

Facade does not forbid direct access to the subsystem. Advanced clients can still reach into `Inventory`, `Payment`, or `Shipping` directly when they need more control; the facade is a convenience, not a wall.

Facade is often confused with Adapter. Facade simplifies an interface by combining and coordinating existing classes. Adapter converts one interface into another so incompatible classes can work together. Facade adds convenience; Adapter adds compatibility.

---

## Structure & Components

### Pattern Diagram

```text
Client -> Facade
              |
      +-------+-------+-------+
      |               |       |
  SubsystemA      SubsystemB  SubsystemC
```

### Key Components

| Component | Role |
|-----------|------|
| `Facade` | Exposes simple, high-level methods and coordinates the subsystems |
| `Subsystem classes` | Perform the real, detailed work; unaware the facade exists |
| `Client` | Calls the facade instead of talking to each subsystem directly |

### Typical Methods

- `placeOrder()` - facade method combining several subsystem calls into one action
- `convert()` - facade method wrapping multi-step processing logic
- `send()` - facade method hiding transport, formatting, and queuing details

---

## PHP Implementation

### Example 1: Order Facade

```php
<?php

class Inventory
{
    public function reserve(string $sku, int $qty): bool
    {
        echo "Inventory: reserved {$qty} x {$sku}\n";
        return true;
    }
}

class Payment
{
    public function charge(float $amount): bool
    {
        echo "Payment: charged \${$amount}\n";
        return true;
    }
}

class Shipping
{
    public function bookCourier(string $sku, int $qty): string
    {
        echo "Shipping: courier booked for {$qty} x {$sku}\n";
        return 'TRACK-98765';
    }
}

class Notification
{
    public function sendConfirmation(string $tracking): void
    {
        echo "Notification: confirmation sent for {$tracking}\n";
    }
}

class OrderFacade
{
    public function __construct(
        private Inventory $inventory,
        private Payment $payment,
        private Shipping $shipping,
        private Notification $notification
    ) {
    }

    public function placeOrder(string $sku, int $qty, float $amount): string
    {
        if (!$this->inventory->reserve($sku, $qty)) {
            throw new RuntimeException('Item out of stock.');
        }

        if (!$this->payment->charge($amount)) {
            throw new RuntimeException('Payment failed.');
        }

        $tracking = $this->shipping->bookCourier($sku, $qty);
        $this->notification->sendConfirmation($tracking);

        return $tracking;
    }
}

$orderFacade = new OrderFacade(
    new Inventory(),
    new Payment(),
    new Shipping(),
    new Notification()
);

$tracking = $orderFacade->placeOrder('SKU-1', 2, 59.98);
echo "Order complete. Tracking: {$tracking}\n";
```

### Expected Output

```text
Inventory: reserved 2 x SKU-1
Payment: charged $59.98
Shipping: courier booked for 2 x SKU-1
Notification: confirmation sent for TRACK-98765
Order complete. Tracking: TRACK-98765
```

### Example 2: Video Conversion Facade

```php
<?php

class Codec
{
    public function detect(string $filename): string
    {
        echo "Codec: detected format for {$filename}\n";
        return 'h264';
    }
}

class Compressor
{
    public function compress(string $filename, string $codec): string
    {
        echo "Compressor: compressing {$filename} using {$codec}\n";
        return "{$filename}.compressed";
    }
}

class FileWriter
{
    public function save(string $filename): void
    {
        echo "FileWriter: saved {$filename}\n";
    }
}

class VideoConversionFacade
{
    public function __construct(
        private Codec $codec,
        private Compressor $compressor,
        private FileWriter $writer
    ) {
    }

    public function convert(string $filename): string
    {
        $codec = $this->codec->detect($filename);
        $compressed = $this->compressor->compress($filename, $codec);
        $this->writer->save($compressed);

        return $compressed;
    }
}

$converter = new VideoConversionFacade(
    new Codec(),
    new Compressor(),
    new FileWriter()
);

$output = $converter->convert('holiday.mov');
echo "Conversion finished: {$output}\n";
```

### Expected Output

```text
Codec: detected format for holiday.mov
Compressor: compressing holiday.mov using h264
FileWriter: saved holiday.mov.compressed
Conversion finished: holiday.mov.compressed
```

### Example 3: Email Facade

```php
<?php

class SmtpClient
{
    public function connect(): void
    {
        echo "SmtpClient: connected to mail server\n";
    }

    public function send(string $to, string $body): void
    {
        echo "SmtpClient: sent email to {$to}\n";
    }
}

class TemplateEngine
{
    public function render(string $template, array $data): string
    {
        echo "TemplateEngine: rendered '{$template}'\n";
        return "Hello {$data['name']}, welcome!";
    }
}

class EmailQueue
{
    public function enqueue(string $to, string $body): void
    {
        echo "EmailQueue: queued email for {$to}\n";
    }
}

class EmailFacade
{
    public function __construct(
        private SmtpClient $smtp,
        private TemplateEngine $templates,
        private EmailQueue $queue
    ) {
    }

    public function sendWelcomeEmail(string $to, string $name): void
    {
        $body = $this->templates->render('welcome', ['name' => $name]);
        $this->queue->enqueue($to, $body);
        $this->smtp->connect();
        $this->smtp->send($to, $body);
    }
}

$emailFacade = new EmailFacade(
    new SmtpClient(),
    new TemplateEngine(),
    new EmailQueue()
);

$emailFacade->sendWelcomeEmail('bob@example.com', 'Bob');
```

### Expected Output

```text
TemplateEngine: rendered 'welcome'
EmailQueue: queued email for bob@example.com
SmtpClient: connected to mail server
SmtpClient: sent email to bob@example.com
```

---

## Real-World Scenarios

### Scenario 1: E-Commerce Checkout

A `CheckoutFacade` can combine cart validation, tax calculation, payment processing, and order creation into a single `checkout()` call used by web, mobile, and admin clients alike.

### Scenario 2: Media Conversion Libraries

Video and image processing libraries often expose a facade over codecs, compression, and format-specific writers, so consumers can call `convert()` without learning the internals of each format.

### Scenario 3: Framework Service Facades

Frameworks like Laravel expose static-looking facades (`Cache::get()`, `Mail::send()`) that hide container resolution and the real service object behind a friendly, memorable call.

### Scenario 4: Third-Party SDK Wrapping

When integrating a cloud storage or payment SDK with many classes and configuration objects, teams often write a thin facade exposing only `upload()`, `download()`, or `charge()` methods relevant to their application.

---

## Pros & Cons

### Advantages

- Simplifies interaction with complex subsystems
- Reduces coupling between client code and subsystem classes
- Centralizes coordination logic in one place
- Makes subsystems easier to swap or refactor without breaking clients
- Improves readability of client code
- Provides a natural entry point for new developers exploring a subsystem

### Disadvantages

- Can turn into a "god object" if it accumulates too many responsibilities
- May hide flexibility that advanced clients occasionally need
- Adds another layer that must be kept in sync with subsystem changes
- Risk of clients depending only on the facade and never learning the subsystem correctly
- Poorly designed facades can become a bottleneck for every feature

---

## Best Practices

1. Keep the facade's methods focused on common, high-level use cases.
2. Do not force every possible subsystem operation through the facade.
3. Let advanced clients bypass the facade and use subsystem classes directly when needed.
4. Inject subsystem dependencies into the facade rather than constructing them internally.
5. Keep business logic in the subsystems; the facade should coordinate, not compute.
6. Name facade methods after the business action they perform (`placeOrder`, not `doStuff`).
7. Split a growing facade into smaller, purpose-specific facades instead of one giant class.
8. Document what the facade simplifies so new developers understand the subsystem underneath.

### Good Facade Design

```php
<?php

class OrderFacade
{
    public function __construct(
        private Inventory $inventory,
        private Payment $payment,
        private Shipping $shipping,
        private Notification $notification
    ) {
    }

    public function placeOrder(string $sku, int $qty, float $amount): string
    {
        $this->inventory->reserve($sku, $qty);
        $this->payment->charge($amount);
        $tracking = $this->shipping->bookCourier($sku, $qty);
        $this->notification->sendConfirmation($tracking);

        return $tracking;
    }
}
```

This facade coordinates four subsystems through one clear method, accepts its dependencies through the constructor, and leaves the actual work to the subsystem classes.

---

## Common Pitfalls

### Pitfall 1: The Facade Becomes a God Object

```php
class SuperFacade
{
    public function placeOrder() { /* ... */ }
    public function refundOrder() { /* ... */ }
    public function generateReports() { /* ... */ }
    public function manageUsers() { /* ... */ }
    public function sendMarketingCampaigns() { /* ... */ }
}
```

When a facade absorbs unrelated responsibilities, it stops simplifying anything and instead becomes a monolith that every part of the codebase depends on.

### Pitfall 2: Hiding Necessary Flexibility

```php
class ReportFacade
{
    public function generate(): string
    {
        // Always PDF, no way to request CSV or JSON.
        return $this->pdfExporter->export();
    }
}
```

If the facade only exposes one rigid path, clients that need a small variation are forced to bypass it entirely or duplicate logic.

### Pitfall 3: Facade vs Adapter vs Mediator Confusion

```php
// Facade: simplifies a set of existing classes.
class PaymentFacade { public function pay(float $amount) { /* coordinates subsystems */ } }

// Adapter: converts one interface into another expected interface.
class LegacyPaymentAdapter implements PaymentGateway { /* wraps incompatible legacy API */ }

// Mediator: centralizes communication between peer objects.
class ChatMediator { public function notify(object $sender, string $event) { /* ... */ } }
```

Facade simplifies access to a subsystem, Adapter makes incompatible interfaces compatible, and Mediator manages communication between peer objects that would otherwise talk to each other directly. Mixing up these intents leads to misleading class names and designs.

### Pitfall 4: Business Logic Leaking Into the Facade

```php
class OrderFacade
{
    public function placeOrder(string $sku, int $qty, float $amount): string
    {
        // Tax rules, discount rules, and validation do not belong here.
        if ($qty > 10) {
            $amount *= 0.9;
        }
        // ...
    }
}
```

Calculation and validation rules belong in dedicated subsystem or domain classes, not scattered inside the coordinating facade.

---

## Variants

### Simple Facade (Stateless Wrapper)

A thin facade that only forwards calls to subsystems in the right order, holding no state of its own beyond its dependencies.

### Facade With Its Own Logic or State

A facade that tracks progress, retries failed steps, or caches intermediate results across calls, in addition to coordinating subsystems.

### Layered Facades (Facade of Facades)

A high-level facade that itself calls smaller, more focused facades, useful when a subsystem is large enough to need its own internal simplification layers.

### Facade Combined With Singleton

A facade exposed as a single globally accessible instance, similar to how some frameworks expose static-style facades (`Cache::get()`), so callers do not need to manually wire dependencies everywhere.

---

## Practice Exercises

### Exercise 1: Hotel Booking Facade

Create `RoomInventory`, `PaymentProcessor`, and `GuestNotifier` classes. Build a `BookingFacade` with a single `bookRoom()` method that coordinates all three.

### Exercise 2: Report Generation Facade

Create `DataFetcher`, `DataFormatter`, and `PdfExporter` classes. Build a `ReportFacade` with a `generateReport()` method that fetches, formats, and exports data.

### Exercise 3: Layered Facade

Build two subsystem facades, `BillingFacade` and `ShippingFacade`, then combine them behind a single `CheckoutFacade`.

### Exercise 4: Facade With Singleton Access

Implement a `Logger` facade exposed through a static `Logger::log()` method backed by a single shared instance internally.

### Exercise 5: Facade vs Adapter

Take a legacy `OldPaymentSystem` class with an incompatible interface. Write a `PaymentAdapter` to make it compatible with your `PaymentGateway` interface, then write a `PaymentFacade` that uses the adapted gateway alongside inventory and shipping subsystems. Write down how the adapter and facade roles differ in your solution.

---

## Summary

The Facade Pattern gives client code one simple entry point into a complex subsystem, hiding coordination details without removing access to the subsystem itself.

It is useful for checkout flows, media processing pipelines, framework service access, and wrapping third-party SDKs.

Use Facade when clients need a straightforward way to perform common tasks, but keep it lean so it simplifies the subsystem instead of becoming a new source of complexity.
