# Observer Pattern - Deep Dive

**Goal:** Define a one-to-many dependency so that when one object changes state, all interested objects are notified automatically.

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

You have one object that performs an important action, and several other objects need to react to that action.

Example: when a user registers, your application may need to:

- Send a welcome email
- Write an audit log
- Track an analytics event
- Create a starter profile
- Notify the sales team

Without Observer, the main service often becomes responsible for all side effects.

### Real Example

```php
class UserService
{
    public function register(string $email): void
    {
        echo "Create user account for {$email}\n";

        $emailService = new EmailService();
        $emailService->sendWelcomeEmail($email);

        $logger = new AuditLogger();
        $logger->log("User registered: {$email}");

        $analytics = new AnalyticsTracker();
        $analytics->track('user_registered', ['email' => $email]);
    }
}
```

This works, but `UserService` now knows too much. It is creating users, sending emails, logging activity, and tracking analytics.

If you later add SMS, coupons, or CRM sync, you must keep editing `UserService`.

Observer solves this by letting `UserService` announce that something happened. Other objects can subscribe and react independently.

---

## Pattern Concept

### What is Observer?

The Observer Pattern has two main roles:

1. **Subject** - the object that owns the state or event
2. **Observers** - objects that want to be notified when the subject changes

The subject does not need to know the concrete classes of its observers. It only knows that they follow a common observer interface.

### Why It Matters

- Decouples the object that triggers an event from the objects that react to it
- Allows multiple reactions to the same event
- Makes behavior easy to add or remove without changing the subject
- Supports event-driven application design
- Helps keep services focused on their main responsibility

### When to Use

Use Observer when:
- One change should notify many objects
- Event listeners should be added or removed dynamically
- You want to avoid hard-coded side effects inside a service
- Multiple independent parts of the system react to the same event
- You are building event systems, hooks, model events, or notification systems

Avoid Observer when:
- There is only one direct dependency and a normal method call is clearer
- The order of actions is critical and must be explicit
- The notification flow would become hard to debug
- Observers need to tightly coordinate with each other
- The event is part of the main business transaction and should not be hidden

---

## Structure & Components

### Pattern Diagram

```text
Client -> Subject
            |
            | notify()
            v
     +-------------+
     | Observers   |
     +-------------+
       |     |    |
       v     v    v
    Email   Log  Analytics
```

### Key Components

| Component | Role |
|-----------|------|
| `Subject` | Stores observers and sends notifications |
| `ConcreteSubject` | The real object that changes state or publishes events |
| `Observer` | Interface every observer must implement |
| `ConcreteObserver` | Reacts to notifications from the subject |
| `Event` | Optional object that carries event data |

### Typical Methods

- `attach()` - subscribe an observer
- `detach()` - unsubscribe an observer
- `notify()` - notify all subscribed observers
- `update()` or `handle()` - observer method called by the subject

---

## PHP Implementation

### Basic Observer Example

This example uses a custom `Subject` and `Observer` interface.

```php
<?php

interface Observer
{
    public function update(string $eventName, array $data): void;
}

interface Subject
{
    public function attach(Observer $observer): void;

    public function detach(Observer $observer): void;

    public function notify(string $eventName, array $data): void;
}

class UserRegistration implements Subject
{
    private array $observers = [];

    public function attach(Observer $observer): void
    {
        $this->observers[] = $observer;
    }

    public function detach(Observer $observer): void
    {
        foreach ($this->observers as $key => $existingObserver) {
            if ($existingObserver === $observer) {
                unset($this->observers[$key]);
            }
        }
    }

    public function notify(string $eventName, array $data): void
    {
        foreach ($this->observers as $observer) {
            $observer->update($eventName, $data);
        }
    }

    public function register(string $email): void
    {
        echo "Create user account for {$email}\n";

        $this->notify('user.registered', [
            'email' => $email,
        ]);
    }
}

class WelcomeEmailObserver implements Observer
{
    public function update(string $eventName, array $data): void
    {
        if ($eventName !== 'user.registered') {
            return;
        }

        echo "Send welcome email to {$data['email']}\n";
    }
}

class AuditLogObserver implements Observer
{
    public function update(string $eventName, array $data): void
    {
        echo "Audit log: {$eventName} for {$data['email']}\n";
    }
}

class AnalyticsObserver implements Observer
{
    public function update(string $eventName, array $data): void
    {
        echo "Track analytics event: {$eventName}\n";
    }
}

$registration = new UserRegistration();
$registration->attach(new WelcomeEmailObserver());
$registration->attach(new AuditLogObserver());
$registration->attach(new AnalyticsObserver());

$registration->register('alice@example.com');
```

### Event Dispatcher Example

In real applications, you often use an event dispatcher instead of making every subject store its own observer list.

```php
<?php

class Event
{
    public function __construct(
        public string $name,
        public array $payload = []
    ) {
    }
}

interface EventListener
{
    public function handle(Event $event): void;
}

class EventDispatcher
{
    private array $listeners = [];

    public function listen(string $eventName, EventListener $listener): void
    {
        $this->listeners[$eventName][] = $listener;
    }

    public function dispatch(Event $event): void
    {
        foreach ($this->listeners[$event->name] ?? [] as $listener) {
            $listener->handle($event);
        }
    }
}

class SendOrderConfirmation implements EventListener
{
    public function handle(Event $event): void
    {
        echo "Email confirmation for order {$event->payload['order_id']}\n";
    }
}

class UpdateInventory implements EventListener
{
    public function handle(Event $event): void
    {
        echo "Update inventory for order {$event->payload['order_id']}\n";
    }
}

class OrderService
{
    public function __construct(
        private EventDispatcher $events
    ) {
    }

    public function placeOrder(int $orderId): void
    {
        echo "Place order {$orderId}\n";

        $this->events->dispatch(new Event('order.placed', [
            'order_id' => $orderId,
        ]));
    }
}

$events = new EventDispatcher();
$events->listen('order.placed', new SendOrderConfirmation());
$events->listen('order.placed', new UpdateInventory());

$orders = new OrderService($events);
$orders->placeOrder(101);
```

### PHP SPL Observer Example

PHP includes built-in interfaces for this pattern: `SplSubject` and `SplObserver`.

```php
<?php

class NewsPublisher implements SplSubject
{
    private SplObjectStorage $observers;
    private string $latestNews = '';

    public function __construct()
    {
        $this->observers = new SplObjectStorage();
    }

    public function attach(SplObserver $observer): void
    {
        $this->observers->attach($observer);
    }

    public function detach(SplObserver $observer): void
    {
        $this->observers->detach($observer);
    }

    public function notify(): void
    {
        foreach ($this->observers as $observer) {
            $observer->update($this);
        }
    }

    public function publish(string $news): void
    {
        $this->latestNews = $news;
        $this->notify();
    }

    public function getLatestNews(): string
    {
        return $this->latestNews;
    }
}

class EmailSubscriber implements SplObserver
{
    public function update(SplSubject $subject): void
    {
        if (!$subject instanceof NewsPublisher) {
            return;
        }

        echo "Email subscriber received: " . $subject->getLatestNews() . "\n";
    }
}

class SmsSubscriber implements SplObserver
{
    public function update(SplSubject $subject): void
    {
        if (!$subject instanceof NewsPublisher) {
            return;
        }

        echo "SMS subscriber received: " . $subject->getLatestNews() . "\n";
    }
}

$publisher = new NewsPublisher();
$publisher->attach(new EmailSubscriber());
$publisher->attach(new SmsSubscriber());

$publisher->publish('New PHP design pattern lesson is available.');
```

### Model Event Example

This example shows how model-style events can keep side effects outside the model or service.

```php
<?php

class Product
{
    public function __construct(
        public int $id,
        public string $name,
        public int $stock
    ) {
    }
}

class ProductStockChanged
{
    public function __construct(
        public Product $product,
        public int $oldStock,
        public int $newStock
    ) {
    }
}

interface StockObserver
{
    public function onStockChanged(ProductStockChanged $event): void;
}

class LowStockNotifier implements StockObserver
{
    public function onStockChanged(ProductStockChanged $event): void
    {
        if ($event->newStock > 5) {
            return;
        }

        echo "Low stock alert for {$event->product->name}\n";
    }
}

class StockAuditLogger implements StockObserver
{
    public function onStockChanged(ProductStockChanged $event): void
    {
        echo "Stock changed from {$event->oldStock} to {$event->newStock}\n";
    }
}

class InventoryService
{
    private array $observers = [];

    public function attach(StockObserver $observer): void
    {
        $this->observers[] = $observer;
    }

    public function updateStock(Product $product, int $newStock): void
    {
        $oldStock = $product->stock;
        $product->stock = $newStock;

        $event = new ProductStockChanged($product, $oldStock, $newStock);

        foreach ($this->observers as $observer) {
            $observer->onStockChanged($event);
        }
    }
}

$product = new Product(1, 'Keyboard', 12);

$inventory = new InventoryService();
$inventory->attach(new LowStockNotifier());
$inventory->attach(new StockAuditLogger());

$inventory->updateStock($product, 3);
```

---

## Real-World Scenarios

### Scenario 1: User Registration

When a user registers, separate observers can send emails, assign default roles, create profile records, write logs, and track analytics.

### Scenario 2: Ecommerce Orders

When an order is placed, observers can update inventory, send confirmation emails, create invoices, notify shipping, and record metrics.

### Scenario 3: Framework Events

Frameworks use event listeners for request lifecycle events, model events, console events, queue events, and authentication events.

### Scenario 4: WordPress Hooks

WordPress actions and filters are practical observer-style systems. Code can subscribe to events without modifying WordPress core.

### Scenario 5: UI or Dashboard Updates

When data changes, multiple widgets or views can react and refresh independently.

---

## Pros & Cons

### Advantages

- Decouples event producers from event consumers
- Allows new reactions without changing the subject
- Supports one-to-many communication
- Helps organize side effects cleanly
- Makes event-driven systems easier to extend
- Works well for plugins, hooks, notifications, and model events

### Disadvantages

- Control flow can become less obvious
- Observer execution order may matter accidentally
- One failing observer can break the notification flow
- Too many observers can make behavior hard to trace
- Synchronous observers can slow down the original operation
- Hidden side effects can surprise future maintainers

---

## Best Practices

1. Name events clearly, such as `user.registered` or `order.placed`.
2. Keep observers focused on one reaction.
3. Use event objects when the payload becomes more than a few fields.
4. Avoid making observers depend on each other.
5. Decide whether observer order matters and document it if it does.
6. Handle observer failures intentionally.
7. Keep critical business logic in the main workflow, not hidden in observers.
8. Use queues for slow observers such as email, webhooks, or reports.
9. Write tests for important observers and for important event dispatches.

### Good Observer Design

```php
<?php

class SendWelcomeEmail implements EventListener
{
    public function handle(Event $event): void
    {
        if ($event->name !== 'user.registered') {
            return;
        }

        echo "Send welcome email to {$event->payload['email']}\n";
    }
}
```

This observer does one thing: it responds to a user registration by sending a welcome email.

---

## Common Pitfalls

### Pitfall 1: Hidden Business Logic

If creating an invoice is required for an order to be valid, hiding invoice creation in an observer may make the system harder to understand.

Use observers for side effects and reactions. Keep core business rules explicit.

### Pitfall 2: Too Many Generic Events

```php
$dispatcher->dispatch(new Event('changed', ['data' => $data]));
```

Generic event names force listeners to inspect payloads and guess meaning. Prefer specific names like `product.stock_changed`.

### Pitfall 3: No Failure Strategy

If one observer throws an exception, should the whole action fail? Should the system log the failure and continue? Decide this before production.

### Pitfall 4: Observer Order Dependencies

If `CreateInvoiceObserver` must run before `SendInvoiceEmailObserver`, the dependency may be too important to leave as an implicit listener order.

### Pitfall 5: Memory Leaks in Long-Running Processes

In workers, daemons, or long-running scripts, attached observers can stay in memory longer than expected. Detach observers when they are no longer needed.

---

## Variants

### Push Model

The subject sends all required data to observers.

```php
$observer->update('user.registered', ['email' => 'alice@example.com']);
```

This is simple and keeps observers from reading the subject directly.

### Pull Model

The subject sends itself to observers, and observers pull the data they need.

```php
$observer->update($subject);
```

This is how `SplObserver` works. It can be flexible, but observers become more aware of the subject.

### Event Dispatcher

A central dispatcher manages listeners by event name. This is common in frameworks.

```php
$events->listen('order.placed', new SendOrderConfirmation());
$events->dispatch(new Event('order.placed', ['order_id' => 101]));
```

### Publish-Subscribe

Publish-subscribe is similar, but the publisher and subscriber are often even more separated, sometimes through a message broker or queue.

### Queued Observers

Slow observers can be pushed to a queue so the original request does not wait for email, reports, or external API calls.

---

## Practice Exercises

### Exercise 1: Newsletter Publisher

Create a `NewsletterPublisher` that allows `EmailSubscriber`, `SmsSubscriber`, and `SlackSubscriber` observers to receive new article notifications.

### Exercise 2: User Registration Events

Build an event dispatcher for `user.registered` with listeners for:
- Sending a welcome email
- Writing an audit log
- Creating a default profile
- Tracking analytics

### Exercise 3: Order Placed Event

Create an `OrderPlaced` event and listeners for:
- Reducing stock
- Sending confirmation email
- Creating invoice
- Notifying warehouse

### Exercise 4: Stock Watcher

Create a product stock observer system. When stock falls below 5, send a low-stock alert. When stock becomes 0, send an out-of-stock alert.

### Exercise 5: Use SPL

Rebuild Exercise 1 using PHP's `SplSubject`, `SplObserver`, and `SplObjectStorage`.

---

## Summary

The Observer Pattern lets one object announce changes while many other objects react independently.

It is one of the most useful behavioral patterns in PHP because it appears in events, hooks, listeners, model events, notifications, and plugin systems.

Use Observer when you want extensible one-to-many communication. Keep event names clear, observers focused, and important business rules visible.

