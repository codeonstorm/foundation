# Chain of Responsibility Pattern - Deep Dive

**Goal:** Pass a request through a chain of handlers so each handler can decide whether to process it, stop it, or forward it to the next handler.

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

You have a request that may need to be checked, modified, approved, rejected, or handled by several different objects.

Without Chain of Responsibility:
- Client code knows every possible handler
- Processing logic becomes a long `if/elseif` block
- Adding a new step requires changing existing client code
- Each step becomes tightly coupled to the next step
- Validation, authorization, logging, and business rules get mixed together

### Real Example

Imagine an order request that must go through multiple checks:

```php
if (!$user->isLoggedIn()) {
    throw new Exception('User must be logged in.');
}

if (!$user->hasVerifiedEmail()) {
    throw new Exception('Email must be verified.');
}

if (!$cart->hasItems()) {
    throw new Exception('Cart is empty.');
}

if (!$payment->isValid()) {
    throw new Exception('Payment is invalid.');
}

echo 'Order can be placed.';
```

This works for a small case, but as the workflow grows, the client becomes responsible for too much.

Chain of Responsibility moves each check into its own handler and links the handlers together.

---

## Pattern Concept

### What is Chain of Responsibility?

The Chain of Responsibility Pattern lets you build a chain of handler objects. Each handler receives a request and decides:

1. Handle the request and stop the chain
2. Handle part of the request and pass it forward
3. Ignore the request and pass it forward
4. Reject the request and stop the chain

### Why It Matters

- Decouples the sender from the receiver
- Makes processing steps reusable
- Allows flexible order of processing
- Makes it easier to add, remove, or reorder behavior
- Keeps each handler focused on one responsibility

### When to Use

Use Chain of Responsibility when:
- Multiple objects may handle a request
- The handler should be chosen at runtime
- You want to avoid large conditional blocks
- You need request pipelines, middleware, filters, or approval workflows
- You want each processing step to be independently testable

Avoid Chain of Responsibility when:
- There is only one fixed handler
- The processing order must be extremely obvious and simple
- A failed request should always be handled in one specific place
- The chain hides too much control flow from the reader

---

## Structure & Components

### Pattern Diagram

```text
Client -> HandlerA -> HandlerB -> HandlerC
             |           |           |
          handles?    handles?    handles?
```

### Key Components

| Component | Role |
|-----------|------|
| `Handler` | Defines the common interface for all handlers |
| `BaseHandler` | Optional abstract class that stores the next handler |
| `ConcreteHandler` | Performs one specific check or action |
| `Request` | Data object passed through the chain |
| `Client` | Builds the chain and sends the request |

### Typical Methods

- `setNext()` - links one handler to the next handler
- `handle()` - processes the request or forwards it
- `next()` - helper method to continue the chain

---

## PHP Implementation

### Basic Handler Chain

This example uses a string request. Each handler checks whether it can process the request.

```php
<?php

interface Handler
{
    public function setNext(Handler $handler): Handler;

    public function handle(string $request): ?string;
}

abstract class AbstractHandler implements Handler
{
    private ?Handler $nextHandler = null;

    public function setNext(Handler $handler): Handler
    {
        $this->nextHandler = $handler;
        return $handler;
    }

    public function handle(string $request): ?string
    {
        if ($this->nextHandler === null) {
            return null;
        }

        return $this->nextHandler->handle($request);
    }
}

class BasicSupportHandler extends AbstractHandler
{
    public function handle(string $request): ?string
    {
        if ($request === 'password_reset') {
            return 'Basic support handled password reset.';
        }

        return parent::handle($request);
    }
}

class BillingSupportHandler extends AbstractHandler
{
    public function handle(string $request): ?string
    {
        if ($request === 'invoice_issue') {
            return 'Billing support handled invoice issue.';
        }

        return parent::handle($request);
    }
}

class TechnicalSupportHandler extends AbstractHandler
{
    public function handle(string $request): ?string
    {
        if ($request === 'server_down') {
            return 'Technical support handled server issue.';
        }

        return parent::handle($request);
    }
}

$basic = new BasicSupportHandler();
$billing = new BillingSupportHandler();
$technical = new TechnicalSupportHandler();

$basic->setNext($billing)->setNext($technical);

echo $basic->handle('password_reset') . "\n";
echo $basic->handle('invoice_issue') . "\n";
echo $basic->handle('server_down') . "\n";
echo $basic->handle('unknown_request') ?? 'No handler found.';
```

### Request Validation Chain

This version is closer to real application code. Each handler validates one rule before passing the request forward.

```php
<?php

class OrderRequest
{
    public function __construct(
        public bool $isLoggedIn,
        public bool $emailVerified,
        public int $cartItems,
        public bool $paymentValid
    ) {
    }
}

interface OrderHandler
{
    public function setNext(OrderHandler $handler): OrderHandler;

    public function handle(OrderRequest $request): void;
}

abstract class AbstractOrderHandler implements OrderHandler
{
    private ?OrderHandler $nextHandler = null;

    public function setNext(OrderHandler $handler): OrderHandler
    {
        $this->nextHandler = $handler;
        return $handler;
    }

    public function handle(OrderRequest $request): void
    {
        if ($this->nextHandler !== null) {
            $this->nextHandler->handle($request);
        }
    }
}

class LoginCheckHandler extends AbstractOrderHandler
{
    public function handle(OrderRequest $request): void
    {
        if (!$request->isLoggedIn) {
            throw new Exception('User must be logged in.');
        }

        parent::handle($request);
    }
}

class EmailVerificationHandler extends AbstractOrderHandler
{
    public function handle(OrderRequest $request): void
    {
        if (!$request->emailVerified) {
            throw new Exception('Email must be verified.');
        }

        parent::handle($request);
    }
}

class CartCheckHandler extends AbstractOrderHandler
{
    public function handle(OrderRequest $request): void
    {
        if ($request->cartItems <= 0) {
            throw new Exception('Cart is empty.');
        }

        parent::handle($request);
    }
}

class PaymentCheckHandler extends AbstractOrderHandler
{
    public function handle(OrderRequest $request): void
    {
        if (!$request->paymentValid) {
            throw new Exception('Payment is invalid.');
        }

        parent::handle($request);
    }
}

$login = new LoginCheckHandler();
$email = new EmailVerificationHandler();
$cart = new CartCheckHandler();
$payment = new PaymentCheckHandler();

$login->setNext($email)->setNext($cart)->setNext($payment);

$request = new OrderRequest(
    isLoggedIn: true,
    emailVerified: true,
    cartItems: 3,
    paymentValid: true
);

$login->handle($request);

echo 'Order can be placed.';
```

### Middleware-Style Chain

Many PHP frameworks use a pipeline or middleware style that is closely related to Chain of Responsibility.

```php
<?php

class HttpRequest
{
    public function __construct(
        public string $path,
        public bool $authenticated,
        public array $headers = []
    ) {
    }
}

class HttpResponse
{
    public function __construct(
        public int $statusCode,
        public string $body
    ) {
    }
}

interface Middleware
{
    public function process(HttpRequest $request, callable $next): HttpResponse;
}

class AuthenticationMiddleware implements Middleware
{
    public function process(HttpRequest $request, callable $next): HttpResponse
    {
        if (!$request->authenticated) {
            return new HttpResponse(401, 'Unauthorized');
        }

        return $next($request);
    }
}

class HeaderMiddleware implements Middleware
{
    public function process(HttpRequest $request, callable $next): HttpResponse
    {
        $request->headers['X-App'] = 'DesignPatternsPHP';

        return $next($request);
    }
}

class ControllerMiddleware implements Middleware
{
    public function process(HttpRequest $request, callable $next): HttpResponse
    {
        return new HttpResponse(200, 'Handled ' . $request->path);
    }
}

function buildPipeline(array $middlewares): callable
{
    $next = fn (HttpRequest $request): HttpResponse => new HttpResponse(404, 'Not Found');

    foreach (array_reverse($middlewares) as $middleware) {
        $next = fn (HttpRequest $request): HttpResponse => $middleware->process($request, $next);
    }

    return $next;
}

$pipeline = buildPipeline([
    new AuthenticationMiddleware(),
    new HeaderMiddleware(),
    new ControllerMiddleware(),
]);

$response = $pipeline(new HttpRequest('/dashboard', true));

echo $response->statusCode . ': ' . $response->body;
```

---

## Real-World Scenarios

### Scenario 1: HTTP Middleware

Requests can pass through authentication, logging, rate limiting, CSRF validation, input trimming, and route handling.

### Scenario 2: Approval Workflow

An expense request may go through team lead approval, manager approval, finance approval, and executive approval depending on the amount.

### Scenario 3: Support Ticket Routing

Basic support handles common issues. Billing handles invoice problems. Technical support handles infrastructure issues. Escalation handles complex cases.

### Scenario 4: Validation Pipeline

Registration input can pass through required field validation, email validation, password strength validation, duplicate account checks, and risk checks.

### Scenario 5: Logging Levels

A log message can pass through debug, info, warning, error, and critical handlers depending on severity and configuration.

---

## Pros & Cons

### Advantages

- Reduces coupling between sender and receiver
- Keeps each handler focused and small
- Makes processing steps easy to reorder
- Supports runtime configuration of workflows
- Helps replace large conditional blocks
- Improves testability of individual processing rules

### Disadvantages

- Request may go unhandled if no handler accepts it
- Chain order can become a hidden source of bugs
- Debugging can be harder because flow jumps between objects
- Too many tiny handlers can make simple logic feel scattered
- Shared mutable request objects can cause side effects

---

## Best Practices

1. Give each handler one clear responsibility.
2. Keep chain order explicit where the chain is built.
3. Decide whether handlers should stop the chain or always continue.
4. Use a request object instead of passing many loose parameters.
5. Return a meaningful result when no handler can process the request.
6. Test each handler alone and test the full chain order separately.
7. Avoid mutating the request unless mutation is part of the pipeline design.
8. Use exceptions for invalid or rejected requests only when that matches your application style.

### Good Handler Design

```php
<?php

class MinimumAmountHandler extends AbstractOrderHandler
{
    public function handle(OrderRequest $request): void
    {
        if ($request->cartItems <= 0) {
            throw new Exception('Order must contain at least one item.');
        }

        parent::handle($request);
    }
}
```

This handler does one thing: it checks whether the order has items. It does not send emails, charge cards, or update inventory.

---

## Common Pitfalls

### Pitfall 1: Forgetting to Forward the Request

```php
<?php

class BadHandler extends AbstractOrderHandler
{
    public function handle(OrderRequest $request): void
    {
        // Rule passes, but the chain stops accidentally.
        if ($request->isLoggedIn) {
            return;
        }
    }
}
```

If a handler should allow the next step to run, it must call `parent::handle($request)` or otherwise forward the request.

### Pitfall 2: Unclear Stop Conditions

Some chains stop when a handler succeeds. Other chains continue through every handler. Mixing both styles in one chain makes the flow difficult to reason about.

### Pitfall 3: Handler Does Too Much

A handler that validates input, sends email, writes logs, and charges payment is no longer a focused chain step. Split it into smaller handlers.

### Pitfall 4: Order-Dependent Bugs

If `PaymentCheckHandler` runs before `CartCheckHandler`, you may validate payment for an empty cart. Build chains in a clear and intentional order.

### Pitfall 5: No Default Handler

If no handler processes the request, the system should return a clear fallback result, throw a controlled exception, or use a final default handler.

---

## Variants

### Classic Chain

Each handler either handles the request or forwards it to the next handler. Once handled, the chain usually stops.

### Pipeline Chain

Every handler gets a chance to process the request. This is common in middleware and validation systems.

### Approval Chain

Each handler approves requests up to a limit, then passes larger requests to the next approver.

```php
<?php

interface Approver
{
    public function setNext(Approver $approver): Approver;

    public function approve(float $amount): string;
}

abstract class AbstractApprover implements Approver
{
    private ?Approver $nextApprover = null;

    public function setNext(Approver $approver): Approver
    {
        $this->nextApprover = $approver;
        return $approver;
    }

    protected function next(float $amount): string
    {
        if ($this->nextApprover === null) {
            return 'No approver available.';
        }

        return $this->nextApprover->approve($amount);
    }
}

class TeamLead extends AbstractApprover
{
    public function approve(float $amount): string
    {
        if ($amount <= 500) {
            return 'Team lead approved.';
        }

        return $this->next($amount);
    }
}

class Manager extends AbstractApprover
{
    public function approve(float $amount): string
    {
        if ($amount <= 5000) {
            return 'Manager approved.';
        }

        return $this->next($amount);
    }
}

class Director extends AbstractApprover
{
    public function approve(float $amount): string
    {
        if ($amount <= 25000) {
            return 'Director approved.';
        }

        return $this->next($amount);
    }
}

$teamLead = new TeamLead();
$manager = new Manager();
$director = new Director();

$teamLead->setNext($manager)->setNext($director);

echo $teamLead->approve(300) . "\n";
echo $teamLead->approve(3500) . "\n";
echo $teamLead->approve(12000) . "\n";
echo $teamLead->approve(90000) . "\n";
```

### Event Listener Chain

Listeners receive an event in order. A listener can stop propagation if the event has been fully handled.

### Logger Chain

Each logger can write messages at a specific severity level and pass higher severity messages to the next logger.

---

## Practice Exercises

### Exercise 1: Support Ticket Chain

Create a support chain with:
- `GeneralSupportHandler`
- `BillingSupportHandler`
- `TechnicalSupportHandler`
- `EscalationHandler`

Each handler should process only the ticket types it understands.

### Exercise 2: Registration Validation

Build a registration validation chain that checks:
- Name is present
- Email is valid
- Password is strong
- Terms were accepted
- Email is not already used

Return a success message only if every handler passes.

### Exercise 3: Expense Approval

Implement an approval chain:
- Team lead approves up to 500
- Manager approves up to 5000
- Director approves up to 25000
- CFO approves anything above 25000

Try changing the order and observe how the behavior changes.

### Exercise 4: HTTP Middleware

Create a middleware pipeline with:
- Logging middleware
- Authentication middleware
- Rate limit middleware
- Controller middleware

Make unauthorized requests stop before reaching the controller.

### Exercise 5: Refactor Conditionals

Take a long `if/elseif` block from your own code and refactor each condition into a separate handler class.

---

## Summary

The Chain of Responsibility Pattern is useful when a request may pass through several processing steps and each step should stay independent.

It is especially common in PHP middleware, validation pipelines, support routing, logging systems, and approval workflows.

Use it when you want flexible request processing without hard-coding every decision into one large method. Keep handlers focused, make chain order clear, and always decide what should happen when no handler can process the request.

