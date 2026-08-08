# Bridge Pattern - Deep Dive

**Goal:** Decouple an abstraction from its implementation so that both can vary and evolve independently.

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

Sometimes a class has two independent dimensions that both need to vary. For example:

- A `Shape` can be a `Circle` or a `Square`, and each shape can be drawn using `OpenGL` or `DirectX`.
- A `Notification` can be `Urgent` or `Normal`, and each notification can be sent by `Email`, `SMS`, or `Push`.

If both dimensions are combined into a single inheritance hierarchy, every new variation on either side multiplies the number of classes needed. Adding one new renderer or one new shape forces you to create a new subclass for every existing combination.

This is called **class explosion**.

### Real Example

```php
<?php

class CircleOpenGLRenderer
{
    public function render(): void
    {
        echo "Drawing a circle using OpenGL\n";
    }
}

class CircleDirectXRenderer
{
    public function render(): void
    {
        echo "Drawing a circle using DirectX\n";
    }
}

class SquareOpenGLRenderer
{
    public function render(): void
    {
        echo "Drawing a square using OpenGL\n";
    }
}

class SquareDirectXRenderer
{
    public function render(): void
    {
        echo "Drawing a square using DirectX\n";
    }
}

// Adding a Triangle means:
// TriangleOpenGLRenderer, TriangleDirectXRenderer ...
// Adding a Vulkan renderer means:
// CircleVulkanRenderer, SquareVulkanRenderer, TriangleVulkanRenderer ...
```

The problem: shapes and renderers are two independent dimensions, but they are welded together into one class per combination. Two shapes and two renderers already produce four classes. Three shapes and three renderers would produce nine. The hierarchy grows multiplicatively instead of additively.

The Bridge Pattern fixes this by splitting the class into two separate hierarchies — an **abstraction** (`Shape`) and an **implementation** (`Renderer`) — and connecting them through composition instead of inheritance. Each side can now grow on its own.

---

## Pattern Concept

### What is Bridge?

The Bridge Pattern separates an abstraction from its implementation so the two can be developed, extended, and combined independently. The abstraction holds a reference to an implementor object and delegates the low-level work to it, rather than inheriting that work directly.

Instead of `N x M` subclasses, you get `N + M` classes plus a bridge (composition) between them.

### Why It Matters

- Avoids class explosion when two dimensions vary independently
- Lets abstraction and implementation evolve without affecting each other
- Allows swapping implementations at runtime
- Keeps high-level logic separate from low-level platform or vendor details
- Makes it easier to add new abstractions or new implementations without touching existing code
- Encourages composition over deep inheritance chains

### When to Use

Use Bridge when:
- You have two (or more) independent dimensions that both need to vary
- You want to switch implementations at runtime
- You want to avoid a permanent binding between an abstraction and one specific implementation
- You expect both the abstraction hierarchy and the implementation hierarchy to grow over time
- You want to share an implementation across multiple abstractions

Avoid Bridge when:
- There is only one implementation and no realistic need for more
- The abstraction and implementation are tightly and permanently coupled by design
- The extra indirection adds complexity without a matching benefit

**Bridge vs Adapter:** Adapter is applied *after the fact* to make two already-existing, incompatible interfaces work together. Bridge is designed *upfront*, before the implementation even exists, specifically to keep an abstraction and its implementation decoupled from the start so both can vary independently.

---

## Structure & Components

### Pattern Diagram

```text
Client -> Abstraction
              |
              | has-a
              v
        Implementor Interface
              ^
              |
      +-------+-------+
      |               |
ConcreteImplementorA  ConcreteImplementorB

Abstraction
    ^
    |
RefinedAbstraction
```

### Key Components

| Component | Role |
|-----------|------|
| `Abstraction` | Defines the high-level interface and holds a reference to an `Implementor` |
| `RefinedAbstraction` | Extends `Abstraction` with additional behavior, still delegating low-level work |
| `Implementor` | Interface that declares the low-level operations `Abstraction` can call |
| `ConcreteImplementor` | Provides a specific platform/vendor/technology implementation of `Implementor` |
| `Client` | Works with an `Abstraction`, unaware of which concrete implementor is behind it |

### Typical Methods

- `operation()` - high-level method exposed by `Abstraction`
- `implementorAction()` / `drawCircle()` / `send()` - low-level method declared by `Implementor`
- `setImplementor()` - optional method to swap the implementor at runtime
- `getImplementor()` - optional accessor used internally by the abstraction

---

## PHP Implementation

### Example 1: Shape / Renderer Bridge

```php
<?php

interface Renderer
{
    public function renderCircle(float $radius): void;
    public function renderSquare(float $side): void;
}

class VectorRenderer implements Renderer
{
    public function renderCircle(float $radius): void
    {
        echo "Drawing a circle of radius {$radius} using vector lines\n";
    }

    public function renderSquare(float $side): void
    {
        echo "Drawing a square of side {$side} using vector lines\n";
    }
}

class RasterRenderer implements Renderer
{
    public function renderCircle(float $radius): void
    {
        echo "Drawing a circle of radius {$radius} using pixels\n";
    }

    public function renderSquare(float $side): void
    {
        echo "Drawing a square of side {$side} using pixels\n";
    }
}

abstract class Shape
{
    public function __construct(protected Renderer $renderer)
    {
    }

    abstract public function draw(): void;
}

class Circle extends Shape
{
    public function __construct(Renderer $renderer, private float $radius)
    {
        parent::__construct($renderer);
    }

    public function draw(): void
    {
        $this->renderer->renderCircle($this->radius);
    }
}

class Square extends Shape
{
    public function __construct(Renderer $renderer, private float $side)
    {
        parent::__construct($renderer);
    }

    public function draw(): void
    {
        $this->renderer->renderSquare($this->side);
    }
}

$vector = new VectorRenderer();
$raster = new RasterRenderer();

$shapes = [
    new Circle($vector, 5.0),
    new Circle($raster, 5.0),
    new Square($vector, 3.0),
    new Square($raster, 3.0),
];

foreach ($shapes as $shape) {
    $shape->draw();
}
```

### Expected Output

```text
Drawing a circle of radius 5 using vector lines
Drawing a circle of radius 5 using pixels
Drawing a square of side 3 using vector lines
Drawing a square of side 3 using pixels
```

Two shapes and two renderers produce four combinations using only four classes plus the interfaces, instead of four dedicated combination classes with no room to grow cleanly.

### Example 2: Notification / Channel Bridge

```php
<?php

interface Channel
{
    public function send(string $title, string $body): void;
}

class EmailChannel implements Channel
{
    public function send(string $title, string $body): void
    {
        echo "Email -> Subject: {$title} | Body: {$body}\n";
    }
}

class SmsChannel implements Channel
{
    public function send(string $title, string $body): void
    {
        echo "SMS -> {$title}: {$body}\n";
    }
}

class PushChannel implements Channel
{
    public function send(string $title, string $body): void
    {
        echo "Push -> {$title} - {$body}\n";
    }
}

class Notification
{
    public function __construct(protected Channel $channel)
    {
    }

    public function notify(string $title, string $body): void
    {
        $this->channel->send($title, $body);
    }
}

$emailNotification = new Notification(new EmailChannel());
$smsNotification = new Notification(new SmsChannel());
$pushNotification = new Notification(new PushChannel());

$emailNotification->notify('Order Shipped', 'Your order is on the way.');
$smsNotification->notify('OTP Code', 'Your code is 482913.');
$pushNotification->notify('New Message', 'You have a new message.');
```

### Expected Output

```text
Email -> Subject: Order Shipped | Body: Your order is on the way.
SMS -> OTP Code: Your code is 482913.
Push -> New Message - You have a new message.
```

The `Notification` abstraction has no idea how a message is actually delivered. The `Channel` implementor handles that detail, and new channels can be added without touching `Notification` at all.

### Example 3: Refined Abstraction — Urgent Notification

```php
<?php

interface Channel
{
    public function send(string $title, string $body): void;
}

class EmailChannel implements Channel
{
    public function send(string $title, string $body): void
    {
        echo "Email -> Subject: {$title} | Body: {$body}\n";
    }
}

class SmsChannel implements Channel
{
    public function send(string $title, string $body): void
    {
        echo "SMS -> {$title}: {$body}\n";
    }
}

class Notification
{
    public function __construct(protected Channel $channel)
    {
    }

    public function notify(string $title, string $body): void
    {
        $this->channel->send($title, $body);
    }
}

class UrgentNotification extends Notification
{
    public function notify(string $title, string $body): void
    {
        $urgentTitle = "URGENT: {$title}";

        // Refined abstraction adds retry behavior on top of the base flow.
        for ($attempt = 1; $attempt <= 2; $attempt++) {
            echo "Attempt {$attempt}: ";
            $this->channel->send($urgentTitle, $body);
        }
    }
}

$normal = new Notification(new EmailChannel());
$urgent = new UrgentNotification(new SmsChannel());

$normal->notify('Weekly Digest', 'Here is your weekly summary.');
$urgent->notify('Server Down', 'Production database is unreachable.');
```

### Expected Output

```text
Email -> Subject: Weekly Digest | Body: Here is your weekly summary.
Attempt 1: SMS -> URGENT: Server Down: Production database is unreachable.
Attempt 2: SMS -> URGENT: Server Down: Production database is unreachable.
```

`UrgentNotification` is a `RefinedAbstraction`. It extends the base abstraction with extra behavior (retrying and rewriting the title) while still delegating the actual delivery mechanism to whichever `Channel` implementor it was given.

---

## Real-World Scenarios

### Scenario 1: Cross-Platform UI Toolkits

A `Window` or `Button` abstraction can be bridged to platform-specific implementors such as `WindowsRenderer`, `MacRenderer`, or `LinuxRenderer`, so the same UI component code runs on multiple operating systems.

### Scenario 2: Database Drivers Abstracted from Query Builders

A query builder abstraction (`QueryBuilder`) can bridge to different database drivers (`MySqlDriver`, `PostgresDriver`, `SqliteDriver`), letting the same query-building API target different databases.

### Scenario 3: Device / Remote Control Abstraction

A `RemoteControl` abstraction can bridge to different `Device` implementors such as `Television` or `Radio`, so the same remote control logic (power, volume) works across different devices.

### Scenario 4: Payment Abstraction Over Multiple Processors

A `Payment` abstraction can bridge to processor implementors like `StripeProcessor`, `PayPalProcessor`, or `RazorpayProcessor`, so switching or adding a payment provider does not require rewriting the checkout logic.

### Scenario 5: Logging Abstraction Over Multiple Backends

A `Logger` abstraction can bridge to implementors such as `FileLogHandler`, `DatabaseLogHandler`, or `CloudLogHandler`, so log formatting logic stays independent of where the logs actually end up.

---

## Pros & Cons

### Advantages

- Eliminates class explosion from combining two independent hierarchies
- Abstraction and implementation can evolve separately
- Implementations can be swapped at runtime
- Encourages composition over inheritance
- New abstractions and new implementations can be added independently
- Implementation details stay hidden from the client

### Disadvantages

- Adds an extra layer of indirection
- Can be harder to understand for a codebase with only one implementation
- Requires upfront design effort to identify the correct two dimensions
- Overusing it where variation is unlikely adds unnecessary complexity

---

## Best Practices

1. Identify the two independent dimensions before introducing Bridge; forcing it onto a single dimension adds needless complexity.
2. Keep the `Implementor` interface small and focused on primitive operations.
3. Let `Abstraction` hold the implementor through composition (constructor injection), not inheritance.
4. Use refined abstractions to add behavior, not to add new implementation details.
5. Allow the implementor to be swapped at runtime when that flexibility is actually needed.
6. Avoid leaking implementor-specific details into the abstraction's public API.
7. Combine Bridge with a factory when implementor selection logic becomes complex.
8. Document which side is the "abstraction" and which is the "implementor" so future contributors extend the correct hierarchy.

### Good Bridge Design

```php
<?php

interface Channel
{
    public function send(string $title, string $body): void;
}

class Notification
{
    public function __construct(private Channel $channel)
    {
    }

    public function notify(string $title, string $body): void
    {
        $this->channel->send($title, $body);
    }

    public function setChannel(Channel $channel): void
    {
        $this->channel = $channel;
    }
}
```

This design keeps `Notification` free of any delivery-specific logic, delegates all low-level work to `Channel`, and allows the channel to be replaced at any time without changing `Notification` itself.

---

## Common Pitfalls

### Pitfall 1: Merging Abstraction and Implementation Back Together

```php
class EmailNotification
{
    public function send(string $title, string $body): void
    {
        echo "Email -> {$title}: {$body}\n";
    }
}

class SmsNotification
{
    public function send(string $title, string $body): void
    {
        echo "SMS -> {$title}: {$body}\n";
    }
}
```

This looks simple but reintroduces the class-explosion problem the moment a second dimension (for example, `Urgent` vs `Normal`) is added. Keep the abstraction and implementor as separate hierarchies connected by composition.

### Pitfall 2: Implementor Interface Too Wide

If `Implementor` exposes many high-level methods instead of a few primitive operations, every `ConcreteImplementor` becomes bloated and hard to maintain. Keep the interface narrow and let `Abstraction` compose primitives into higher-level behavior.

### Pitfall 3: Hardcoding a Concrete Implementor Inside the Abstraction

```php
class Notification
{
    private EmailChannel $channel;

    public function __construct()
    {
        $this->channel = new EmailChannel(); // defeats the purpose of Bridge
    }
}
```

This removes the flexibility Bridge is meant to provide. Always inject the implementor rather than instantiating a concrete one inside the abstraction.

### Pitfall 4: Confusing Bridge with Adapter and Strategy

- **Adapter** converts an existing incompatible interface into one the client expects; it is a retrofit.
- **Bridge** is planned upfront to keep an abstraction and its implementation independent, so both hierarchies can grow.
- **Strategy** swaps interchangeable algorithms for a single behavior (for example, sorting or discount calculation) and usually has one dimension, not two.

They can look identical in code (composition plus delegation), but their intent differs: Adapter fixes compatibility, Bridge separates two evolving hierarchies, and Strategy swaps one algorithm.

---

## Variants

### Static Bridge

The implementor is decided once at construction time and does not change afterward.

```php
$shape = new Circle(new VectorRenderer(), 5.0);
```

### Dynamic Bridge

The implementor can be swapped at runtime through a setter method.

```php
$notification = new Notification(new EmailChannel());
$notification->notify('Hello', 'First message');

$notification->setChannel(new SmsChannel());
$notification->notify('Hello', 'Second message');
```

### Bridge with Abstract Factory

An Abstract Factory can be used to select and construct the correct implementor for a given platform or configuration, keeping that selection logic out of the abstraction and the client.

```php
interface RendererFactory
{
    public function createRenderer(): Renderer;
}

class VectorRendererFactory implements RendererFactory
{
    public function createRenderer(): Renderer
    {
        return new VectorRenderer();
    }
}

$factory = new VectorRendererFactory();
$circle = new Circle($factory->createRenderer(), 5.0);
```

---

## Practice Exercises

### Exercise 1: Shape and Renderer

Build `Shape` (abstraction) with `Circle` and `Triangle`, bridged to a `Renderer` implementor with `VectorRenderer` and `RasterRenderer`. Draw all four combinations.

### Exercise 2: Remote Control and Device

Create a `RemoteControl` abstraction with `power()` and `volumeUp()`, bridged to a `Device` implementor with `Television` and `Radio` concrete implementors.

### Exercise 3: Payment Abstraction

Create a `Payment` abstraction bridged to a `PaymentProcessor` implementor with `StripeProcessor` and `PayPalProcessor`. Add a `RefundablePayment` refined abstraction that adds a `refund()` method.

### Exercise 4: Swappable Logger

Create a `Logger` abstraction bridged to a `LogHandler` implementor with `FileLogHandler` and `DatabaseLogHandler`. Add a `setHandler()` method and switch handlers at runtime.

### Exercise 5: Bridge with Factory

Extend the Shape/Renderer exercise by adding a `RendererFactory` that picks `VectorRenderer` or `RasterRenderer` based on a configuration string, so the client never instantiates a concrete renderer directly.

### Exercise 6: Compare Bridge and Adapter

Write a `LegacyLogger` with an incompatible method name (`writeLog()`), adapt it to a `Logger` interface using Adapter, then separately design a `Logger`/`LogHandler` bridge. Write down how the two solutions differ in intent.

---

## Summary

The Bridge Pattern splits an abstraction and its implementation into two separate hierarchies connected through composition instead of inheritance. This prevents the class explosion that happens when two independent dimensions are combined into one inheritance chain.

By depending on an `Implementor` interface rather than a concrete class, the abstraction stays flexible, implementations can be swapped at runtime, and both sides of the design can grow without disturbing each other.

Use Bridge whenever you can identify two independent axes of variation upfront — such as shape versus renderer, or notification versus channel — and want to keep them decoupled from the very beginning.
