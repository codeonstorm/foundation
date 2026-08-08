# State Pattern - Deep Dive

**Goal:** Allow an object to change its behavior when its internal state changes, as if the object changed its class.

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

Some objects behave differently depending on their current state.

Examples:

- An order can be pending, paid, shipped, delivered, or cancelled
- A support ticket can be open, assigned, waiting, resolved, or closed
- A media player can be stopped, playing, or paused
- A document can be draft, reviewed, approved, or published
- A user account can be active, suspended, locked, or deleted

Without State, the object often fills up with conditionals that check the current status before every action.

### Real Example

```php
class Order
{
    private string $status = 'pending';

    public function pay(): void
    {
        if ($this->status !== 'pending') {
            throw new RuntimeException('Only pending orders can be paid.');
        }

        echo "Payment accepted\n";
        $this->status = 'paid';
    }

    public function ship(): void
    {
        if ($this->status !== 'paid') {
            throw new RuntimeException('Only paid orders can be shipped.');
        }

        echo "Order shipped\n";
        $this->status = 'shipped';
    }

    public function cancel(): void
    {
        if ($this->status === 'shipped') {
            throw new RuntimeException('Shipped orders cannot be cancelled.');
        }

        echo "Order cancelled\n";
        $this->status = 'cancelled';
    }
}
```

This works for a small workflow, but each new state adds more branching. Every action needs to know which states are valid.

State solves this by moving state-specific behavior into separate state classes.

---

## Pattern Concept

### What is State?

The State Pattern represents each possible state as its own object.

The main object, called the context, delegates behavior to its current state object. When the state changes, the context swaps one state object for another.

### Why It Matters

- Replaces large state conditionals
- Keeps state-specific behavior in focused classes
- Makes transitions explicit
- Makes workflows easier to extend
- Helps model finite state machines
- Keeps the context class small

### When to Use

Use State when:
- An object behaves differently in different states
- You have repeated conditionals based on status
- State transitions have rules
- New states are likely to be added
- You want invalid actions to be handled consistently
- The workflow is important enough to model directly

Avoid State when:
- There are only one or two simple states
- A small conditional is easier to read
- State transitions are not meaningful
- The extra classes would add noise
- You only need to store status, not change behavior

---

## Structure & Components

### Pattern Diagram

```text
Client -> Context -> State
                    ^
                    |
        +-----------+-----------+
        |           |           |
   PendingState  PaidState  ShippedState
```

### Key Components

| Component | Role |
|-----------|------|
| `Context` | Main object whose behavior changes with state |
| `State` | Interface shared by all concrete states |
| `ConcreteState` | Implements behavior for one specific state |
| `Transition` | Change from one state object to another |
| `Client` | Uses the context without managing all state rules directly |

### Typical Methods

- `setState()` - changes the context state
- `getStateName()` - identifies the current state
- `pay()`, `ship()`, `cancel()` - state-dependent actions
- `handle()` - generic state operation
- `transitionTo()` - moves the context to another state

---

## PHP Implementation

### Basic Order State Example

```php
<?php

interface OrderState
{
    public function pay(Order $order): void;

    public function ship(Order $order): void;

    public function cancel(Order $order): void;

    public function name(): string;
}

class Order
{
    public function __construct(private OrderState $state)
    {
    }

    public function setState(OrderState $state): void
    {
        $this->state = $state;
    }

    public function pay(): void
    {
        $this->state->pay($this);
    }

    public function ship(): void
    {
        $this->state->ship($this);
    }

    public function cancel(): void
    {
        $this->state->cancel($this);
    }

    public function status(): string
    {
        return $this->state->name();
    }
}

class PendingState implements OrderState
{
    public function pay(Order $order): void
    {
        echo "Payment accepted\n";
        $order->setState(new PaidState());
    }

    public function ship(Order $order): void
    {
        throw new RuntimeException('Pending orders cannot be shipped.');
    }

    public function cancel(Order $order): void
    {
        echo "Order cancelled\n";
        $order->setState(new CancelledState());
    }

    public function name(): string
    {
        return 'pending';
    }
}

class PaidState implements OrderState
{
    public function pay(Order $order): void
    {
        throw new RuntimeException('Order is already paid.');
    }

    public function ship(Order $order): void
    {
        echo "Order shipped\n";
        $order->setState(new ShippedState());
    }

    public function cancel(Order $order): void
    {
        echo "Refund payment and cancel order\n";
        $order->setState(new CancelledState());
    }

    public function name(): string
    {
        return 'paid';
    }
}

class ShippedState implements OrderState
{
    public function pay(Order $order): void
    {
        throw new RuntimeException('Shipped orders are already paid.');
    }

    public function ship(Order $order): void
    {
        throw new RuntimeException('Order is already shipped.');
    }

    public function cancel(Order $order): void
    {
        throw new RuntimeException('Shipped orders cannot be cancelled.');
    }

    public function name(): string
    {
        return 'shipped';
    }
}

class CancelledState implements OrderState
{
    public function pay(Order $order): void
    {
        throw new RuntimeException('Cancelled orders cannot be paid.');
    }

    public function ship(Order $order): void
    {
        throw new RuntimeException('Cancelled orders cannot be shipped.');
    }

    public function cancel(Order $order): void
    {
        throw new RuntimeException('Order is already cancelled.');
    }

    public function name(): string
    {
        return 'cancelled';
    }
}

$order = new Order(new PendingState());

echo $order->status() . "\n";
$order->pay();
echo $order->status() . "\n";
$order->ship();
echo $order->status() . "\n";
```

The `Order` class does not check status strings. It asks the current state object to handle the action.

### Proper Example: Support Ticket Workflow

```php
<?php

interface TicketState
{
    public function assign(Ticket $ticket, string $agent): void;

    public function waitForCustomer(Ticket $ticket): void;

    public function resolve(Ticket $ticket): void;

    public function close(Ticket $ticket): void;

    public function label(): string;
}

class Ticket
{
    private ?string $agent = null;

    public function __construct(private TicketState $state)
    {
    }

    public function transitionTo(TicketState $state): void
    {
        echo "Ticket moved from {$this->state->label()} to {$state->label()}\n";
        $this->state = $state;
    }

    public function setAgent(string $agent): void
    {
        $this->agent = $agent;
    }

    public function agent(): ?string
    {
        return $this->agent;
    }

    public function assign(string $agent): void
    {
        $this->state->assign($this, $agent);
    }

    public function waitForCustomer(): void
    {
        $this->state->waitForCustomer($this);
    }

    public function resolve(): void
    {
        $this->state->resolve($this);
    }

    public function close(): void
    {
        $this->state->close($this);
    }

    public function status(): string
    {
        return $this->state->label();
    }
}

class OpenTicketState implements TicketState
{
    public function assign(Ticket $ticket, string $agent): void
    {
        $ticket->setAgent($agent);
        $ticket->transitionTo(new AssignedTicketState());
    }

    public function waitForCustomer(Ticket $ticket): void
    {
        throw new RuntimeException('Open tickets must be assigned first.');
    }

    public function resolve(Ticket $ticket): void
    {
        throw new RuntimeException('Open tickets must be assigned before resolution.');
    }

    public function close(Ticket $ticket): void
    {
        $ticket->transitionTo(new ClosedTicketState());
    }

    public function label(): string
    {
        return 'open';
    }
}

class AssignedTicketState implements TicketState
{
    public function assign(Ticket $ticket, string $agent): void
    {
        $ticket->setAgent($agent);
        echo "Ticket reassigned to {$agent}\n";
    }

    public function waitForCustomer(Ticket $ticket): void
    {
        $ticket->transitionTo(new WaitingForCustomerState());
    }

    public function resolve(Ticket $ticket): void
    {
        $ticket->transitionTo(new ResolvedTicketState());
    }

    public function close(Ticket $ticket): void
    {
        throw new RuntimeException('Assigned tickets should be resolved before closing.');
    }

    public function label(): string
    {
        return 'assigned';
    }
}

class WaitingForCustomerState implements TicketState
{
    public function assign(Ticket $ticket, string $agent): void
    {
        $ticket->setAgent($agent);
        $ticket->transitionTo(new AssignedTicketState());
    }

    public function waitForCustomer(Ticket $ticket): void
    {
        echo "Ticket is already waiting for customer response\n";
    }

    public function resolve(Ticket $ticket): void
    {
        $ticket->transitionTo(new ResolvedTicketState());
    }

    public function close(Ticket $ticket): void
    {
        throw new RuntimeException('Waiting tickets cannot be closed directly.');
    }

    public function label(): string
    {
        return 'waiting_for_customer';
    }
}

class ResolvedTicketState implements TicketState
{
    public function assign(Ticket $ticket, string $agent): void
    {
        $ticket->setAgent($agent);
        $ticket->transitionTo(new AssignedTicketState());
    }

    public function waitForCustomer(Ticket $ticket): void
    {
        throw new RuntimeException('Resolved tickets are not waiting for customers.');
    }

    public function resolve(Ticket $ticket): void
    {
        echo "Ticket is already resolved\n";
    }

    public function close(Ticket $ticket): void
    {
        $ticket->transitionTo(new ClosedTicketState());
    }

    public function label(): string
    {
        return 'resolved';
    }
}

class ClosedTicketState implements TicketState
{
    public function assign(Ticket $ticket, string $agent): void
    {
        throw new RuntimeException('Closed tickets cannot be assigned.');
    }

    public function waitForCustomer(Ticket $ticket): void
    {
        throw new RuntimeException('Closed tickets cannot wait for customer response.');
    }

    public function resolve(Ticket $ticket): void
    {
        throw new RuntimeException('Closed tickets cannot be resolved again.');
    }

    public function close(Ticket $ticket): void
    {
        echo "Ticket is already closed\n";
    }

    public function label(): string
    {
        return 'closed';
    }
}

$ticket = new Ticket(new OpenTicketState());
$ticket->assign('Priya');
$ticket->waitForCustomer();
$ticket->resolve();
$ticket->close();
```

Each state owns the rules for what can happen next. The ticket delegates behavior without a long status switch.

### Media Player Example

```php
<?php

interface PlayerState
{
    public function play(MediaPlayer $player): void;

    public function pause(MediaPlayer $player): void;

    public function stop(MediaPlayer $player): void;
}

class MediaPlayer
{
    public function __construct(private PlayerState $state)
    {
    }

    public function setState(PlayerState $state): void
    {
        $this->state = $state;
    }

    public function play(): void
    {
        $this->state->play($this);
    }

    public function pause(): void
    {
        $this->state->pause($this);
    }

    public function stop(): void
    {
        $this->state->stop($this);
    }
}

class StoppedState implements PlayerState
{
    public function play(MediaPlayer $player): void
    {
        echo "Start playback\n";
        $player->setState(new PlayingState());
    }

    public function pause(MediaPlayer $player): void
    {
        throw new RuntimeException('Cannot pause while stopped.');
    }

    public function stop(MediaPlayer $player): void
    {
        echo "Already stopped\n";
    }
}

class PlayingState implements PlayerState
{
    public function play(MediaPlayer $player): void
    {
        echo "Already playing\n";
    }

    public function pause(MediaPlayer $player): void
    {
        echo "Pause playback\n";
        $player->setState(new PausedState());
    }

    public function stop(MediaPlayer $player): void
    {
        echo "Stop playback\n";
        $player->setState(new StoppedState());
    }
}

class PausedState implements PlayerState
{
    public function play(MediaPlayer $player): void
    {
        echo "Resume playback\n";
        $player->setState(new PlayingState());
    }

    public function pause(MediaPlayer $player): void
    {
        echo "Already paused\n";
    }

    public function stop(MediaPlayer $player): void
    {
        echo "Stop playback\n";
        $player->setState(new StoppedState());
    }
}

$player = new MediaPlayer(new StoppedState());
$player->play();
$player->pause();
$player->play();
$player->stop();
```

This is a small example, but it shows the core idea clearly: the same command means different things in different states.

---

## Real-World Scenarios

### Scenario 1: Order Lifecycle

Orders can move through pending, paid, packed, shipped, delivered, returned, and cancelled states. Each state decides which actions are valid.

### Scenario 2: Ticket Systems

Support tickets often have strict transitions, such as open to assigned, assigned to waiting, waiting to resolved, and resolved to closed.

### Scenario 3: Publishing Workflows

Documents can be draft, submitted, approved, published, archived, or rejected.

### Scenario 4: Account Status

User accounts may allow or reject login, password reset, billing, and profile updates depending on whether they are active, locked, suspended, or deleted.

### Scenario 5: UI Components

Buttons, forms, and screens can behave differently when loading, enabled, disabled, submitting, failed, or completed.

---

## Pros & Cons

### Advantages

- Removes status-based conditionals from the context
- Keeps state rules close to the state they belong to
- Makes invalid actions explicit
- Makes new states easier to add
- Supports state machines and workflows
- Improves readability for complex lifecycles

### Disadvantages

- Adds more classes
- Can feel heavy for simple status values
- Transitions may become scattered if not documented
- State classes may need access to context methods
- Similar states can duplicate behavior
- Persistence can require mapping stored status values back to state objects

---

## Best Practices

1. Keep the context responsible for shared data and delegation.
2. Keep state classes responsible for state-specific behavior and transitions.
3. Make invalid actions fail clearly.
4. Use meaningful state names like `PaidState` or `WaitingForCustomerState`.
5. Keep transitions easy to find.
6. Consider a factory when restoring state from a database value.
7. Avoid giving states unrestricted access to every context detail.
8. Test each state and important transition independently.

### Good State Design

```php
<?php

interface DocumentState
{
    public function submit(Document $document): void;

    public function approve(Document $document): void;

    public function publish(Document $document): void;
}
```

The interface exposes actions whose behavior depends on the document's current state.

---

## Common Pitfalls

### Pitfall 1: State Classes Become the Whole Application

State classes should model lifecycle behavior. They should not become large service objects that handle emails, payments, logs, and database writes.

### Pitfall 2: No Clear Transition Map

If developers cannot quickly see which transitions are allowed, the workflow becomes hard to maintain.

Use documentation, tests, or a transition table for important workflows.

### Pitfall 3: Storing Only Objects

State objects are useful at runtime, but databases usually store strings or enums.

Create a reliable mapping between persisted values and state classes.

### Pitfall 4: Too Much Duplication

If many states reject the same actions in the same way, consider an abstract base state with default behavior.

### Pitfall 5: Confusing State with Strategy

Strategy chooses an interchangeable algorithm. State represents a lifecycle where behavior changes because the object has moved to another state.

---

## Variants

### State with Abstract Base Class

Use a base state to define default invalid behavior.

```php
abstract class BaseOrderState implements OrderState
{
    public function pay(Order $order): void
    {
        throw new RuntimeException('Pay is not allowed in this state.');
    }
}
```

### State Factory

Use a factory to restore a state object from a persisted status.

```php
$state = $stateFactory->fromStatus($orderStatus);
```

### State Machine

For complex workflows, a state machine can define states and transitions separately from the state classes.

### Enum-Backed State

In modern PHP, enums can represent state names while state classes still handle behavior.

```php
enum OrderStatus: string
{
    case Pending = 'pending';
    case Paid = 'paid';
    case Shipped = 'shipped';
    case Cancelled = 'cancelled';
}
```

### State + Observer

Use Observer to publish events when transitions happen, such as `order.paid` or `ticket.closed`.

---

## Practice Exercises

### Exercise 1: Order Workflow

Create states for pending, paid, shipped, delivered, cancelled, and returned orders.

### Exercise 2: Document Publishing

Create a document workflow with draft, submitted, approved, rejected, published, and archived states.

### Exercise 3: Support Ticket

Extend the ticket example with reopened tickets and escalation.

### Exercise 4: Account Status

Create account states for active, suspended, locked, and deleted. Implement login, reset password, and update profile actions.

### Exercise 5: State Factory

Create a factory that converts database status strings into state objects and back again.

---

## Summary

The State Pattern lets an object change behavior when its internal state changes.

It is useful for workflows, lifecycles, and state machines where each state has different rules.

Use State when status-based conditionals are spreading through your object. Keep transitions clear, state classes focused, and persistence mapping explicit.
