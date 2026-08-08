# Mediator Pattern - Deep Dive

**Goal:** Reduce direct dependencies between objects by making them communicate through a central mediator.

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

Several objects need to work together, but direct communication between all of them can create a tangled system.

Examples:

- Form fields enabling and disabling each other
- Chat users sending messages through a room
- UI widgets reacting to each other
- Checkout components coordinating payment, shipping, and coupon rules
- Game objects coordinating through a match or lobby

Without Mediator, each object may know too much about the others.

### Real Example

```php
class TextBox
{
    public function changed(): void
    {
        $button = new Button();
        $button->enable();

        $label = new Label();
        $label->setText('Ready to submit');
    }
}
```

This is simple, but the `TextBox` now knows about `Button` and `Label`. If the form grows, every component can become connected to every other component.

Mediator solves this by moving coordination logic into one object.

---

## Pattern Concept

### What is Mediator?

The Mediator Pattern defines an object that coordinates communication between related objects.

Objects no longer call each other directly. They notify the mediator, and the mediator decides what should happen next.

### Why It Matters

- Reduces coupling between related objects
- Moves coordination logic into one place
- Makes components more reusable
- Avoids many-to-many dependencies
- Keeps complex workflows easier to reason about

### When to Use

Use Mediator when:
- Many objects communicate with each other directly
- A workflow has coordination rules spread across classes
- UI components need to react to each other
- You want components to be reusable outside one workflow
- Object relationships are becoming hard to trace

Avoid Mediator when:
- Objects have simple one-to-one relationships
- A direct method call is clearer
- The mediator would only forward messages without logic
- The central object would become too large

---

## Structure & Components

### Pattern Diagram

```text
ComponentA -> Mediator <- ComponentB
     ^           |           ^
     |           v           |
 ComponentC <- Coordination -> ComponentD
```

### Key Components

| Component | Role |
|-----------|------|
| `Mediator` | Interface for component communication |
| `ConcreteMediator` | Coordinates components and workflow rules |
| `Component` | Object that communicates through the mediator |
| `ConcreteComponent` | Specific participant in the workflow |
| `Client` | Creates and wires components with the mediator |

### Typical Methods

- `notify()` - component tells mediator something happened
- `send()` - chat-style communication through mediator
- `register()` - mediator tracks components
- `changed()` - UI component reports a state change
- `handle()` - mediator responds to an event

---

## PHP Implementation

### Basic UI Form Mediator

```php
<?php

interface FormMediator
{
    public function notify(object $sender, string $event): void;
}

class NullFormMediator implements FormMediator
{
    public function notify(object $sender, string $event): void
    {
    }
}

class TextBox
{
    public function __construct(private FormMediator $mediator)
    {
    }

    private string $value = '';

    public function setMediator(FormMediator $mediator): void
    {
        $this->mediator = $mediator;
    }

    public function setValue(string $value): void
    {
        $this->value = $value;
        $this->mediator->notify($this, 'text.changed');
    }

    public function value(): string
    {
        return $this->value;
    }
}

class SubmitButton
{
    private bool $enabled = false;

    public function enable(): void
    {
        $this->enabled = true;
        echo "Submit button enabled\n";
    }

    public function disable(): void
    {
        $this->enabled = false;
        echo "Submit button disabled\n";
    }
}

class StatusLabel
{
    public function setText(string $text): void
    {
        echo "Status: {$text}\n";
    }
}

class LoginFormMediator implements FormMediator
{
    public function __construct(
        private TextBox $email,
        private SubmitButton $submit,
        private StatusLabel $status
    ) {
    }

    public function notify(object $sender, string $event): void
    {
        if ($sender === $this->email && $event === 'text.changed') {
            if ($this->email->value() === '') {
                $this->submit->disable();
                $this->status->setText('Email is required');
                return;
            }

            $this->submit->enable();
            $this->status->setText('Ready to submit');
        }
    }
}

$submit = new SubmitButton();
$status = new StatusLabel();
$email = new TextBox(new NullFormMediator());

$mediator = new LoginFormMediator($email, $submit, $status);
$email->setMediator($mediator);

$email->setValue('');
$email->setValue('alice@example.com');
```

The form components do not call each other directly. The mediator decides how changes affect the whole form.

### Proper Example: Chat Room Mediator

```php
<?php

interface ChatMediator
{
    public function join(User $user): void;

    public function send(User $sender, string $message): void;
}

class User
{
    public function __construct(
        private string $name,
        private ChatMediator $chat
    ) {
    }

    public function name(): string
    {
        return $this->name;
    }

    public function send(string $message): void
    {
        echo "{$this->name} sends: {$message}\n";
        $this->chat->send($this, $message);
    }

    public function receive(string $from, string $message): void
    {
        echo "{$this->name} receives from {$from}: {$message}\n";
    }
}

class ChatRoom implements ChatMediator
{
    private array $users = [];

    public function join(User $user): void
    {
        $this->users[] = $user;
        echo "{$user->name()} joined the room\n";
    }

    public function send(User $sender, string $message): void
    {
        foreach ($this->users as $user) {
            if ($user === $sender) {
                continue;
            }

            $user->receive($sender->name(), $message);
        }
    }
}

$room = new ChatRoom();

$alice = new User('Alice', $room);
$bob = new User('Bob', $room);
$carol = new User('Carol', $room);

$room->join($alice);
$room->join($bob);
$room->join($carol);

$alice->send('Hello everyone');
```

Users do not know about each other. They only know the chat mediator.

### Checkout Coordination Example

```php
<?php

interface CheckoutMediator
{
    public function notify(object $sender, string $event): void;
}

class CartSummary
{
    public function __construct(private CheckoutMediator $mediator)
    {
    }

    private float $subtotal = 0;

    public function updateSubtotal(float $subtotal): void
    {
        $this->subtotal = $subtotal;
        $this->mediator->notify($this, 'cart.updated');
    }

    public function subtotal(): float
    {
        return $this->subtotal;
    }
}

class ShippingSelector
{
    private string $method = 'standard';

    public function setMethod(string $method): void
    {
        $this->method = $method;
        echo "Shipping method: {$method}\n";
    }

    public function method(): string
    {
        return $this->method;
    }
}

class CouponBox
{
    private ?string $coupon = null;

    public function apply(string $coupon): void
    {
        $this->coupon = $coupon;
        echo "Coupon applied: {$coupon}\n";
    }

    public function clear(): void
    {
        $this->coupon = null;
        echo "Coupon cleared\n";
    }
}

class EcommerceCheckoutMediator implements CheckoutMediator
{
    public function __construct(
        private CartSummary $cart,
        private ShippingSelector $shipping,
        private CouponBox $coupon
    ) {
    }

    public function notify(object $sender, string $event): void
    {
        if ($sender === $this->cart && $event === 'cart.updated') {
            if ($this->cart->subtotal() >= 2000) {
                $this->shipping->setMethod('free');
                $this->coupon->clear();
                return;
            }

            $this->shipping->setMethod('standard');
        }
    }
}
```

The mediator owns checkout coordination rules, so cart, shipping, and coupon components stay focused.

---

## Real-World Scenarios

### Scenario 1: Form and UI Coordination

Forms often need fields, buttons, validation messages, tabs, and sections to react to each other.

### Scenario 2: Chat Rooms

Users communicate through a room, channel, or server instead of holding references to every other user.

### Scenario 3: Checkout Workflow

Cart totals, coupons, shipping methods, payment methods, and tax calculations often influence each other.

### Scenario 4: Controller-Level Coordination

A controller or application service can mediate between domain services without forcing those services to depend on each other.

### Scenario 5: Game Lobbies

Players, teams, timers, scoreboards, and match rules can coordinate through a lobby or match mediator.

---

## Pros & Cons

### Advantages

- Reduces direct dependencies between components
- Centralizes coordination logic
- Makes components easier to reuse
- Simplifies many-to-many communication
- Makes workflow rules easier to locate
- Helps isolate UI interaction rules

### Disadvantages

- Mediator can become too large
- Control flow may be less obvious if events are vague
- Components may become too passive
- Central object can become a bottleneck
- Overuse can hide simple direct relationships

---

## Best Practices

1. Keep the mediator focused on coordination, not business logic that belongs elsewhere.
2. Use clear event names like `cart.updated` or `field.changed`.
3. Keep component interfaces small.
4. Avoid making the mediator a global service locator.
5. Split large mediators by workflow or screen.
6. Prefer direct calls when only two objects communicate simply.
7. Test important coordination rules through the mediator.

### Good Mediator Design

```php
<?php

interface DialogMediator
{
    public function notify(object $sender, string $event): void;
}
```

The interface gives components one communication channel without exposing every other component.

---

## Common Pitfalls

### Pitfall 1: God Mediator

If the mediator handles every workflow in the system, it becomes a large, hard-to-change class.

Split mediators by bounded context, screen, or workflow.

### Pitfall 2: Vague Events

```php
$mediator->notify($this, 'changed');
```

Generic events make the mediator guess what changed. Prefer specific events when behavior differs.

### Pitfall 3: Replacing Every Direct Call

Mediator is useful for complex coordination. A simple service calling a repository does not need a mediator.

### Pitfall 4: Hidden Dependencies

If a component silently depends on mediator side effects, tests and maintenance can become confusing.

### Pitfall 5: Mixing Mediator and Observer Carelessly

Observer broadcasts events to many listeners. Mediator coordinates known collaborators. They can look similar, but the intent is different.

---

## Variants

### UI Dialog Mediator

A dialog object coordinates controls such as buttons, inputs, checkboxes, and labels.

### Chat Room Mediator

A room coordinates message delivery between users.

### Application Service as Mediator

An application service coordinates domain services for a use case.

### Event-Based Mediator

Components send named events to a mediator, and the mediator routes behavior based on event type.

### Command Mediator

A command bus can act as a mediator by routing command objects to handlers.

---

## Practice Exercises

### Exercise 1: Chat Room

Build a chat room mediator with users who can join, leave, send public messages, and send private messages.

### Exercise 2: Registration Form

Create a form mediator that enables submit only when email, password, and terms checkbox are valid.

### Exercise 3: Checkout Mediator

Coordinate cart total, coupon rules, shipping method, and payment availability through a checkout mediator.

### Exercise 4: Notification Center

Create a mediator that coordinates email, SMS, and dashboard notifications for user events.

### Exercise 5: Game Lobby

Build a lobby mediator that tracks players, ready status, team assignment, and match start.

---

## Summary

The Mediator Pattern centralizes communication between related objects.

It is useful when components are becoming tightly coupled through many direct references.

Use Mediator to coordinate workflows, UI components, chat rooms, checkout flows, and other many-object collaborations. Keep the mediator focused so it does not become the system's dumping ground.
