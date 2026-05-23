# Strategy Pattern - Deep Dive

**Goal:** Define a family of algorithms, put each one in its own class, and make them interchangeable at runtime.

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

You have several ways to perform the same kind of task.

Examples:

- Pay with card, PayPal, UPI, or wallet
- Sort by price, rating, newest, or popularity
- Calculate shipping by weight, distance, or express delivery
- Apply different discount rules
- Validate data differently for admin, customer, or guest users

Without Strategy, the code often becomes a long conditional block.

### Real Example

```php
if ($paymentType === 'card') {
    echo "Paying {$amount} by card\n";
} elseif ($paymentType === 'paypal') {
    echo "Paying {$amount} by PayPal\n";
} elseif ($paymentType === 'upi') {
    echo "Paying {$amount} by UPI\n";
} else {
    throw new InvalidArgumentException('Unsupported payment type.');
}
```

This works at first, but each new payment type changes the same block. The payment logic grows in one place, and testing each algorithm becomes harder.

Strategy solves this by moving each algorithm into a separate class.

---

## Pattern Concept

### What is Strategy?

The Strategy Pattern defines a common interface for a set of algorithms. The client or context chooses which algorithm object to use.

The context does not know the details of the algorithm. It only calls the strategy interface.

### Why It Matters

- Replaces large conditional blocks
- Keeps algorithms isolated and testable
- Allows runtime behavior changes
- Supports open/closed principle
- Makes the context depend on abstraction instead of concrete logic

### When to Use

Use Strategy when:
- You have multiple algorithms for one task
- You switch behavior at runtime
- You want to remove conditionals from business logic
- You want each algorithm independently testable
- You expect new algorithms to be added later

Avoid Strategy when:
- There is only one algorithm
- A simple conditional is clearer
- Strategies share too much duplicated code
- The extra classes add no real flexibility

---

## Structure & Components

### Pattern Diagram

```text
Client -> Context -> Strategy
                      ^
                      |
       +--------------+--------------+
       |              |              |
 ConcreteA       ConcreteB       ConcreteC
```

### Key Components

| Component | Role |
|-----------|------|
| `Strategy` | Interface shared by all algorithms |
| `ConcreteStrategy` | One specific algorithm implementation |
| `Context` | Object that uses a strategy |
| `Client` | Chooses which strategy the context should use |

### Typical Methods

- `execute()` - generic strategy operation
- `calculate()` - common for pricing, shipping, or tax strategies
- `pay()` - common for payment strategies
- `validate()` - common for validation strategies

---

## PHP Implementation

### Basic Payment Strategy

```php
<?php

interface PaymentStrategy
{
    public function pay(float $amount): void;
}

class CardPayment implements PaymentStrategy
{
    public function pay(float $amount): void
    {
        echo "Paid {$amount} by card\n";
    }
}

class PayPalPayment implements PaymentStrategy
{
    public function pay(float $amount): void
    {
        echo "Paid {$amount} by PayPal\n";
    }
}

class UpiPayment implements PaymentStrategy
{
    public function pay(float $amount): void
    {
        echo "Paid {$amount} by UPI\n";
    }
}

class Checkout
{
    public function __construct(private PaymentStrategy $payment)
    {
    }

    public function setPaymentStrategy(PaymentStrategy $payment): void
    {
        $this->payment = $payment;
    }

    public function pay(float $amount): void
    {
        $this->payment->pay($amount);
    }
}

$checkout = new Checkout(new CardPayment());
$checkout->pay(1200);

$checkout->setPaymentStrategy(new UpiPayment());
$checkout->pay(500);
```

The `Checkout` class does not contain payment conditionals. It delegates payment behavior to a strategy.

### Proper Example: Shipping Cost Strategy

```php
<?php

class Shipment
{
    public function __construct(
        public float $weightKg,
        public float $distanceKm,
        public float $orderValue
    ) {
    }
}

interface ShippingStrategy
{
    public function calculate(Shipment $shipment): float;
}

class StandardShipping implements ShippingStrategy
{
    public function calculate(Shipment $shipment): float
    {
        return 50 + ($shipment->weightKg * 10);
    }
}

class ExpressShipping implements ShippingStrategy
{
    public function calculate(Shipment $shipment): float
    {
        return 100 + ($shipment->weightKg * 18) + ($shipment->distanceKm * 0.5);
    }
}

class FreeShipping implements ShippingStrategy
{
    public function calculate(Shipment $shipment): float
    {
        if ($shipment->orderValue >= 2000) {
            return 0;
        }

        return 75;
    }
}

class ShippingCalculator
{
    public function __construct(private ShippingStrategy $strategy)
    {
    }

    public function setStrategy(ShippingStrategy $strategy): void
    {
        $this->strategy = $strategy;
    }

    public function calculate(Shipment $shipment): float
    {
        return $this->strategy->calculate($shipment);
    }
}

$shipment = new Shipment(weightKg: 2.5, distanceKm: 40, orderValue: 1500);

$calculator = new ShippingCalculator(new StandardShipping());
echo $calculator->calculate($shipment) . "\n";

$calculator->setStrategy(new ExpressShipping());
echo $calculator->calculate($shipment) . "\n";

$calculator->setStrategy(new FreeShipping());
echo $calculator->calculate($shipment) . "\n";
```

Each shipping rule is isolated. You can add `SameDayShipping` later without changing `ShippingCalculator`.

### Discount Strategy Example

```php
<?php

interface DiscountStrategy
{
    public function apply(float $total): float;
}

class NoDiscount implements DiscountStrategy
{
    public function apply(float $total): float
    {
        return $total;
    }
}

class PercentageDiscount implements DiscountStrategy
{
    public function __construct(private float $percent)
    {
    }

    public function apply(float $total): float
    {
        return $total - ($total * $this->percent / 100);
    }
}

class FixedDiscount implements DiscountStrategy
{
    public function __construct(private float $amount)
    {
    }

    public function apply(float $total): float
    {
        return max(0, $total - $this->amount);
    }
}

class Cart
{
    public function __construct(private DiscountStrategy $discount)
    {
    }

    public function checkout(float $total): float
    {
        return $this->discount->apply($total);
    }
}

$cart = new Cart(new PercentageDiscount(10));
echo $cart->checkout(1000) . "\n";
```

---

## Real-World Scenarios

### Scenario 1: Payment Processing

Payment gateways can be selected by user choice, region, currency, or account configuration.

### Scenario 2: Shipping Rules

Ecommerce systems often calculate shipping differently for standard, express, international, and free-shipping rules.

### Scenario 3: Sorting and Filtering

Product lists can sort by price, popularity, newest, rating, or relevance.

### Scenario 4: Validation

Different user roles, forms, or workflows can use different validation strategies.

### Scenario 5: Tax Calculation

Tax rules can differ by country, state, product category, or customer type.

---

## Pros & Cons

### Advantages

- Removes complex conditionals
- Makes algorithms easy to test
- Allows runtime switching
- Keeps context classes small
- Makes new algorithms easier to add
- Improves adherence to open/closed principle

### Disadvantages

- Adds more classes
- Client must choose the right strategy
- Similar strategies can duplicate code
- May be overkill for simple conditionals
- Strategy objects can hide behavior from readers if names are unclear

---

## Best Practices

1. Keep each strategy focused on one algorithm.
2. Use clear names like `ExpressShipping` or `PercentageDiscount`.
3. Make the strategy interface small.
4. Inject strategies through constructors when possible.
5. Use a factory when strategy selection is based on config or user input.
6. Avoid making strategies depend heavily on the context internals.
7. Test strategies independently.

### Good Strategy Design

```php
<?php

interface TaxStrategy
{
    public function calculate(float $subtotal): float;
}
```

The interface is small, clear, and focused on the algorithm.

---

## Common Pitfalls

### Pitfall 1: Strategy Does Too Much

A payment strategy should process payment. It should not also send emails, update inventory, and generate invoices.

### Pitfall 2: Hidden Conditionals Move Elsewhere

If all selection logic becomes a huge conditional in another file, consider a factory, configuration map, or dependency injection container.

### Pitfall 3: Too Many Tiny Strategies

Do not create separate strategy classes for behavior that will never vary independently.

### Pitfall 4: Leaky Context

If strategies need to know too much about the context, the boundary is wrong. Pass only the data needed by the algorithm.

---

## Variants

### Strategy with Factory

Use a factory to choose the strategy from input.

```php
$strategy = $factory->create($paymentType);
```

### Strategy with Closures

For small local algorithms, a callable can act like a lightweight strategy.

```php
$sorter = fn (array $items): array => $items;
```

### Strategy with Dependency Injection

Register strategies in a container and inject the chosen one by configuration.

### Policy Object

A policy object is similar to Strategy, often used for business rules such as permissions, pricing, or eligibility.

---

## Practice Exercises

### Exercise 1: Payment Strategy

Create strategies for card, PayPal, UPI, and wallet payments.

### Exercise 2: Shipping Calculator

Add `SameDayShipping` and `InternationalShipping` strategies to the shipping example.

### Exercise 3: Sort Products

Create sorting strategies for price low to high, price high to low, newest, and rating.

### Exercise 4: Discount Rules

Build discount strategies for percentage discount, fixed discount, buy-one-get-one, and no discount.

### Exercise 5: Strategy Factory

Create a factory that returns a payment strategy based on a string from configuration.

---

## Summary

The Strategy Pattern lets you swap algorithms without changing the class that uses them.

It is useful when one task has multiple implementations, such as payment, shipping, sorting, validation, discounts, or tax calculation.

Use Strategy when behavior varies and you want clean, testable, runtime-selectable algorithms.

