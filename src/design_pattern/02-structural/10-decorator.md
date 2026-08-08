# Decorator Pattern - Deep Dive

**Goal:** Attach new behavior to an object dynamically by wrapping it, without changing its class or creating a subclass for every combination.

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

Sometimes an object needs optional, combinable behaviors added on top of its base behavior.

Examples:

- A coffee that can optionally get milk, sugar, or whipped cream
- A notifier that can optionally send email, SMS, or Slack messages, in any combination
- A text source that can optionally be trimmed, upper-cased, or compressed
- An HTTP handler that can optionally get logging, authentication, or caching

If you try to model every combination as a subclass, the number of classes explodes. Three optional add-ons already require up to eight subclasses to cover every combination, and adding a fourth add-on doubles that again.

### Real Example

```php
class Coffee
{
    public function cost(): float
    {
        return 2.00;
    }

    public function description(): string
    {
        return 'Coffee';
    }
}

class CoffeeWithMilk extends Coffee
{
    public function cost(): float
    {
        return parent::cost() + 0.50;
    }

    public function description(): string
    {
        return parent::description() . ' + Milk';
    }
}

class CoffeeWithSugar extends Coffee
{
    public function cost(): float
    {
        return parent::cost() + 0.25;
    }

    public function description(): string
    {
        return parent::description() . ' + Sugar';
    }
}

class CoffeeWithMilkAndSugar extends Coffee
{
    public function cost(): float
    {
        return parent::cost() + 0.50 + 0.25;
    }

    public function description(): string
    {
        return parent::description() . ' + Milk + Sugar';
    }
}

class CoffeeWithMilkAndSugarAndWhip extends Coffee
{
    public function cost(): float
    {
        return parent::cost() + 0.50 + 0.25 + 0.75;
    }

    public function description(): string
    {
        return parent::description() . ' + Milk + Sugar + Whip';
    }
}

// Every new add-on doubles the number of subclasses needed.
```

The problem: each new optional add-on multiplies the number of subclasses, and every combination duplicates the pricing and description logic.

Decorator solves this by wrapping the base object in small objects that each add one behavior. Any combination becomes a matter of stacking wrappers instead of writing a new class.

---

## Pattern Concept

### What is Decorator?

The Decorator Pattern lets you wrap an object with one or more decorator objects that implement the same interface. Each decorator adds its own behavior before or after delegating to the object it wraps.

The client talks to the outermost wrapper through the same interface as the original object, without knowing how many layers are involved.

### Why It Matters

- Adds behavior at runtime instead of compile time
- Avoids a combinatorial explosion of subclasses
- Lets behaviors be combined in any order the client chooses
- Keeps each behavior in its own small, focused class
- Follows the Open/Closed Principle: new behavior without modifying existing classes
- Allows behavior to be added or removed by changing which wrappers are used

### When to Use

Use Decorator when:
- You need to add optional, combinable behaviors to an object
- Subclassing would produce too many combinations
- You want to add or remove responsibilities at runtime
- You want each added behavior isolated in its own class

Avoid Decorator when:
- There is only one fixed behavior to add — plain inheritance is simpler
- You need to control or restrict access rather than add behavior, where Proxy fits better
- The number of decorators grows so large that stacking becomes hard to read
- A simple configuration flag or strategy object would be clearer than wrapping

---

## Structure & Components

### Pattern Diagram

```text
Client -> Component Interface
              ^
              |
      +-------+---------------------+
      |                             |
ConcreteComponent              Decorator (abstract)
                                     |  holds wrapped Component
                                     |
                        +------------+------------+
                        |                         |
                ConcreteDecoratorA         ConcreteDecoratorB
```

### Key Components

| Component | Role |
|-----------|------|
| `Component` | Common interface implemented by both the real object and all decorators |
| `ConcreteComponent` | The base object that decorators wrap |
| `Decorator` | Abstract class or trait that holds a reference to a wrapped `Component` and implements the same interface |
| `ConcreteDecorator` | Adds one specific behavior before/after delegating to the wrapped component |
| `Client` | Uses the component interface without caring how many layers wrap the real object |

### Typical Methods

- `operation()` - the shared method defined by the component interface
- `__construct(Component $wrapped)` - decorator stores the wrapped component
- Additional helper methods a decorator may expose, such as `getAddedCost()` or `getExtraLabel()`

---

## PHP Implementation

### Example 1: Coffee Order Decorator

Each add-on wraps a `Beverage` and adds its own cost and description.

```php
<?php

interface Beverage
{
    public function cost(): float;

    public function description(): string;
}

class Coffee implements Beverage
{
    public function cost(): float
    {
        return 2.00;
    }

    public function description(): string
    {
        return 'Coffee';
    }
}

abstract class BeverageDecorator implements Beverage
{
    public function __construct(protected Beverage $beverage)
    {
    }
}

class MilkDecorator extends BeverageDecorator
{
    public function cost(): float
    {
        return $this->beverage->cost() + 0.50;
    }

    public function description(): string
    {
        return $this->beverage->description() . ' + Milk';
    }
}

class SugarDecorator extends BeverageDecorator
{
    public function cost(): float
    {
        return $this->beverage->cost() + 0.25;
    }

    public function description(): string
    {
        return $this->beverage->description() . ' + Sugar';
    }
}

class WhipDecorator extends BeverageDecorator
{
    public function cost(): float
    {
        return $this->beverage->cost() + 0.75;
    }

    public function description(): string
    {
        return $this->beverage->description() . ' + Whip';
    }
}

$order = new Coffee();
$order = new MilkDecorator($order);
$order = new SugarDecorator($order);
$order = new WhipDecorator($order);

echo $order->description() . "\n";
echo 'Total: $' . number_format($order->cost(), 2) . "\n";
```

### Expected Output

```text
Coffee + Milk + Sugar + Whip
Total: $3.50
```

### Example 2: Text Processing Decorator

Text decorators wrap a `TextSource` and transform its output.

```php
<?php

interface TextSource
{
    public function getText(): string;
}

class PlainText implements TextSource
{
    public function __construct(private string $text)
    {
    }

    public function getText(): string
    {
        return $this->text;
    }
}

abstract class TextDecorator implements TextSource
{
    public function __construct(protected TextSource $source)
    {
    }
}

class TrimDecorator extends TextDecorator
{
    public function getText(): string
    {
        return trim($this->source->getText());
    }
}

class UpperCaseDecorator extends TextDecorator
{
    public function getText(): string
    {
        return strtoupper($this->source->getText());
    }
}

class ExclaimDecorator extends TextDecorator
{
    public function getText(): string
    {
        return $this->source->getText() . '!!!';
    }
}

$text = new PlainText('  hello world  ');
$text = new TrimDecorator($text);
$text = new UpperCaseDecorator($text);
$text = new ExclaimDecorator($text);

echo $text->getText() . "\n";
```

### Expected Output

```text
HELLO WORLD!!!
```

### Example 3: HTTP Middleware Decorator

Middleware-style decorators wrap a request handler. The order in which they are stacked changes the observable behavior.

```php
<?php

interface RequestHandler
{
    public function handle(string $request): string;
}

class CoreHandler implements RequestHandler
{
    public function handle(string $request): string
    {
        return "Response for [{$request}]";
    }
}

abstract class HandlerDecorator implements RequestHandler
{
    public function __construct(protected RequestHandler $handler)
    {
    }
}

class LoggingMiddleware extends HandlerDecorator
{
    public function handle(string $request): string
    {
        echo "Log: incoming request [{$request}]\n";

        $response = $this->handler->handle($request);

        echo "Log: outgoing response\n";

        return $response;
    }
}

class AuthMiddleware extends HandlerDecorator
{
    public function __construct(RequestHandler $handler, private bool $authorized)
    {
        parent::__construct($handler);
    }

    public function handle(string $request): string
    {
        if (!$this->authorized) {
            return 'Response for [401 Unauthorized]';
        }

        return $this->handler->handle($request);
    }
}

// Order matters: auth check runs before logging here.
$handlerA = new LoggingMiddleware(new AuthMiddleware(new CoreHandler(), authorized: false));

echo $handlerA->handle('/dashboard') . "\n";

echo "---\n";

// Order matters: logging runs even for requests later rejected by auth.
$handlerB = new AuthMiddleware(new LoggingMiddleware(new CoreHandler()), authorized: false);

echo $handlerB->handle('/dashboard') . "\n";
```

### Expected Output

```text
Log: incoming request [/dashboard]
Log: outgoing response
Response for [401 Unauthorized]
---
Response for [401 Unauthorized]
```

Notice that in `$handlerA`, logging wraps auth, so every request is logged even when auth rejects it. In `$handlerB`, auth wraps logging, so a rejected request never reaches the logging layer at all. The stacking order changes what actually happens.

---

## Real-World Scenarios

### Scenario 1: Beverage and Order Customization

Coffee shop and food-ordering systems use decorators to price and describe add-ons such as milk, sugar, extra toppings, or size upgrades without a subclass per combination.

### Scenario 2: I/O Streams

PHP stream wrappers and libraries that buffer, compress, or encrypt data commonly layer decorators around a base stream, similar to Java's `InputStream` decorators (`BufferedInputStream`, `GZIPInputStream`).

### Scenario 3: HTTP Middleware

Frameworks such as Laravel, Slim, and PSR-15 implementations chain middleware around a request handler to add logging, authentication, rate limiting, CORS headers, or caching, each as an independent, stackable layer.

### Scenario 4: UI Widget Decoration

GUI toolkits can wrap a base widget with decorators that add a border, a scrollbar, a shadow, or a tooltip, combining any subset of visual behaviors around the same widget interface.

---

## Pros & Cons

### Advantages

- Adds behavior without modifying existing classes
- Avoids a combinatorial explosion of subclasses
- Behaviors can be combined and reordered at runtime
- Each decorator is small and focused on one responsibility
- Follows the Open/Closed Principle
- Decorators can be composed differently for different clients

### Disadvantages

- Many small decorator classes can be harder to navigate
- Stacking order can change behavior, which may confuse readers
- Debugging requires stepping through several wrapper layers
- Identity checks (`instanceof`, `===`) on the wrapped object become unreliable
- Overuse can make the object graph difficult to understand at a glance

---

## Best Practices

1. Keep the component interface small so every decorator stays easy to implement.
2. Give each decorator exactly one responsibility.
3. Use an abstract base decorator to avoid repeating the "store wrapped object" boilerplate.
4. Document when stacking order matters, especially for middleware-style decorators.
5. Prefer composition helpers or factory functions when stacking becomes repetitive.
6. Avoid decorators that silently change the return type or contract of the wrapped method.
7. Keep decorators stateless where possible, or make any added state clearly owned by that decorator.
8. Do not use Decorator when a simple configuration option would solve the problem.

### Good Decorator Design

```php
<?php

abstract class NotifierDecorator implements Notifier
{
    public function __construct(protected Notifier $wrapped)
    {
    }
}

class SmsNotifierDecorator extends NotifierDecorator
{
    public function send(string $message): void
    {
        $this->wrapped->send($message);
        echo "Sending SMS: {$message}\n";
    }
}
```

This decorator has a single responsibility (adding SMS delivery), reuses a shared abstract base for the wrapped reference, and never changes the meaning of `send()` from the client's point of view.

---

## Common Pitfalls

### Pitfall 1: Decorator Order Sensitivity

Wrapping decorators in the wrong order silently changes behavior, as shown in the middleware example.

```php
$a = new LoggingMiddleware(new AuthMiddleware($core, false));
$b = new AuthMiddleware(new LoggingMiddleware($core), false);
// $a and $b behave differently even though they use the same decorators.
```

Document the expected order, or provide a factory function that builds the stack consistently.

### Pitfall 2: Too Many Small Decorator Classes

Splitting every tiny behavior into its own class can leave you with dozens of near-identical decorators that are tedious to compose.

```php
class TrimDecorator extends TextDecorator { /* ... */ }
class LowerCaseDecorator extends TextDecorator { /* ... */ }
class UpperCaseDecorator extends TextDecorator { /* ... */ }
class ReverseDecorator extends TextDecorator { /* ... */ }
// Consider a closure-based or configurable decorator instead.
```

### Pitfall 3: Decorator vs Proxy vs Inheritance Confusion

Decorator adds behavior while preserving the interface. Proxy controls access to an object. Inheritance hard-codes one fixed combination of behavior at compile time.

```php
class CachedService extends RealService {}   // inheritance: fixed, compile-time
class ServiceProxy implements Service {}     // proxy: controls access
class LoggingService implements Service {}   // decorator: adds behavior, combinable
```

Choose based on intent: control access (Proxy), add combinable behavior (Decorator), or model a fixed is-a relationship (Inheritance).

### Pitfall 4: Breaking the Component Contract

A decorator that changes the meaning of a method (for example, returning `null` instead of throwing, or changing units) breaks client expectations.

```php
class BrokenDiscountDecorator extends PriceDecorator
{
    public function getPrice(): float
    {
        return -1; // Violates the contract that price is never negative.
    }
}
```

Keep the decorator's output within the same contract as the original component.

---

## Variants

### Transparent Decorator

Implements only the methods already defined on the component interface, so the client cannot tell a decorator apart from the original object.

```php
class UpperCaseDecorator extends TextDecorator
{
    public function getText(): string
    {
        return strtoupper($this->source->getText());
    }
}
```

### Decorator That Adds New Methods

Exposes extra methods beyond the component interface, which requires the client to know about the specific decorator type to use them.

```php
class MetadataDecorator extends TextDecorator
{
    public function getText(): string
    {
        return $this->source->getText();
    }

    public function getWordCount(): int
    {
        return str_word_count($this->getText());
    }
}
```

### Function/Closure-Based Decorators

PHP closures can wrap callables to add behavior without defining a new class for each decorator.

```php
$greet = fn (string $name): string => "Hello, {$name}";

$loud = fn (string $name) => strtoupper($greet($name));

$logged = function (string $name) use ($loud): string {
    echo "Calling greet for {$name}\n";
    return $loud($name);
};

echo $logged('Ankit') . "\n";
```

### Stacking Multiple Decorators

Any number of decorators can be layered, and a helper function can make the stacking order explicit and reusable.

```php
function buildPipeline(RequestHandler $core): RequestHandler
{
    return new LoggingMiddleware(
        new AuthMiddleware($core, authorized: true)
    );
}
```

---

## Practice Exercises

### Exercise 1: Pizza Topping Decorator

Create a `Pizza` interface with `cost()` and `description()`. Implement `Margherita` and decorators for `ExtraCheese`, `Olives`, and `Mushrooms`.

### Exercise 2: Notifier Combos

Create a `Notifier` interface with `send(string $message): void`. Implement `EmailNotifier` and decorators `SmsDecorator` and `SlackDecorator` that can be combined in any order.

### Exercise 3: Stream Compression and Encryption

Create a `DataStream` interface with `write(string $data): string`. Implement decorators `CompressDecorator` and `EncryptDecorator`, and show that stacking them in different orders produces different output.

### Exercise 4: Middleware Pipeline

Build a small middleware pipeline with `RateLimitMiddleware`, `LoggingMiddleware`, and `CorsMiddleware` around a `RequestHandler`. Show a case where reordering two middlewares changes the response.

### Exercise 5: Compare Decorator and Inheritance

Implement the same three-way beverage combination (milk, sugar, whip) twice: once using subclasses for every combination, and once using Decorator. Count the classes each approach requires.

---

## Summary

The Decorator Pattern wraps an object in one or more layers that each add a single, combinable behavior while preserving the original interface.

It replaces subclass explosions with composition, letting behaviors be mixed, matched, and reordered at runtime.

Use Decorator when you need optional, stackable behavior on top of an object, and reach for Proxy or plain inheritance instead when the goal is access control or a single fixed behavior.
