# Abstract Factory Pattern - Deep Dive

**Goal:** Provide an interface for creating families of related or dependent objects without specifying their concrete classes.

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

You need to create multiple related objects that belong together, and you want to ensure the created objects are compatible without hardcoding concrete classes.

Without Abstract Factory:
- Client code is tightly coupled to concrete product classes
- Related objects may be mixed incorrectly
- Adding new product families requires modifying client code
- Object creation logic becomes scattered and hard to maintain

### Real Example

```php
$theme = 'dark';

if ($theme === 'dark') {
    $button = new DarkButton();
    $window = new DarkWindow();
} else {
    $button = new LightButton();
    $window = new LightWindow();
}
```

This should be centralized so theme-specific families are created consistently.

---

## Pattern Concept

### What is Abstract Factory?

The Abstract Factory Pattern defines an interface for creating families of related or dependent objects without specifying their concrete classes.

### Why It Matters

- Encapsulates the creation of related object families
- Enforces compatibility between created products
- Keeps client code independent of concrete classes
- Supports open/closed principle and easier extension

### When to Use

✅ Use Abstract Factory when:
- You need related objects from a family
- You must ensure objects are compatible
- You want to swap entire product families easily
- You are building a theme, platform, or component system

❌ Avoid when:
- Only one product type is needed
- Products are unrelated
- Object creation is simple and does not change often

---

## Structure & Components

### Pattern Diagram

```
Client -> AbstractFactory
            /       \
ConcreteFactoryA  ConcreteFactoryB
      |             |
ProductA1 ProductA2  ProductB1 ProductB2
```

### Key Components

| Component | Role |
|-----------|------|
| `AbstractFactory` | Defines methods to create abstract products |
| `ConcreteFactory` | Implements creation of concrete products |
| `AbstractProduct` | Interface for a product type |
| `ConcreteProduct` | Concrete implementation of a product |
| `Client` | Uses abstract factories and products |

---

## PHP Implementation

### Basic Abstract Factory Example

```php
<?php
interface Button
{
    public function render(): string;
}

interface Checkbox
{
    public function render(): string;
}

interface GUIFactory
{
    public function createButton(): Button;
    public function createCheckbox(): Checkbox;
}

class DarkButton implements Button
{
    public function render(): string
    {
        return '<button class="dark">Dark Button</button>';
    }
}

class DarkCheckbox implements Checkbox
{
    public function render(): string
    {
        return '<input type="checkbox" class="dark">';
    }
}

class LightButton implements Button
{
    public function render(): string
    {
        return '<button class="light">Light Button</button>';
    }
}

class LightCheckbox implements Checkbox
{
    public function render(): string
    {
        return '<input type="checkbox" class="light">';
    }
}

class DarkFactory implements GUIFactory
{
    public function createButton(): Button
    {
        return new DarkButton();
    }

    public function createCheckbox(): Checkbox
    {
        return new DarkCheckbox();
    }
}

class LightFactory implements GUIFactory
{
    public function createButton(): Button
    {
        return new LightButton();
    }

    public function createCheckbox(): Checkbox
    {
        return new LightCheckbox();
    }
}

function renderUI(GUIFactory $factory): void
{
    $button = $factory->createButton();
    $checkbox = $factory->createCheckbox();

    echo $button->render();
    echo $checkbox->render();
}

$factory = new DarkFactory();
renderUI($factory);

$factory = new LightFactory();
renderUI($factory);
```

### Abstract Factory with Product Variants

```php
<?php
interface TextField
{
    public function render(): string;
}

interface PasswordField
{
    public function render(): string;
}

interface FormFactory
{
    public function createTextField(): TextField;
    public function createPasswordField(): PasswordField;
}

class MobileTextField implements TextField
{
    public function render(): string
    {
        return '<input type="text" class="mobile">';
    }
}

class MobilePasswordField implements PasswordField
{
    public function render(): string
    {
        return '<input type="password" class="mobile">';
    }
}

class DesktopTextField implements TextField
{
    public function render(): string
    {
        return '<input type="text" class="desktop">';
    }
}

class DesktopPasswordField implements PasswordField
{
    public function render(): string
    {
        return '<input type="password" class="desktop">';
    }
}

class MobileFactory implements FormFactory
{
    public function createTextField(): TextField
    {
        return new MobileTextField();
    }

    public function createPasswordField(): PasswordField
    {
        return new MobilePasswordField();
    }
}

class DesktopFactory implements FormFactory
{
    public function createTextField(): TextField
    {
        return new DesktopTextField();
    }

    public function createPasswordField(): PasswordField
    {
        return new DesktopPasswordField();
    }
}

function renderForm(FormFactory $factory): void
{
    echo $factory->createTextField()->render();
    echo $factory->createPasswordField()->render();
}

renderForm(new MobileFactory());
renderForm(new DesktopFactory());
```

---

## Real-World Scenarios

### Scenario 1: UI Theme Systems

Create multiple related UI components (buttons, dialogs, menus) for different themes or platforms while ensuring consistent styling.

### Scenario 2: Cross-Platform Applications

Build platform-specific families for Windows, macOS, Linux, or mobile devices with consistent APIs.

### Scenario 3: Document Generators

Generate related output objects such as `HTMLDocument`, `PDFDocument`, and `CSVDocument` that belong to the same family.

### Scenario 4: Messaging Systems

Create producers, consumers, and formatters for SMS, email, push notifications, or webhook channels.

---

## Pros & Cons

### Advantages ✅

- Ensures families of products remain consistent
- Hides concrete classes from the client
- Supports swapping entire product families easily
- Encourages strong abstraction and separation of concerns
- Works well with open/closed principle

### Disadvantages ❌

- Leads to many interfaces and classes
- Can be overkill for small projects
- Complexity increases with product families
- Harder to understand when used unnecessarily

---

## Best Practices

1. Keep abstract product interfaces narrow and focused.
2. Use meaningful factory names that describe the family.
3. Keep creation logic inside factories only.
4. Prefer composition over forcing many relations between products.
5. Use configuration or a factory selector to choose the right concrete factory.

---

## Common Pitfalls

### Pitfall 1: Too Many Product Types

Creating too many related product interfaces makes the pattern hard to maintain.

### Pitfall 2: Unrelated Product Families

Do not group unrelated products in the same abstract factory.

### Pitfall 3: Exposing Concrete Classes

Client code should never directly instantiate concrete products if using Abstract Factory.

### Pitfall 4: Turning Factories into Service Locators

Avoid using factory classes as general registries for unrelated objects.

---

## Variants

### Factory Method

Abstract Factory often uses Factory Method internally. Factory Method creates one product, while Abstract Factory creates families of related products.

### Builder

Builder constructs complex objects step-by-step, while Abstract Factory creates related object families.

### Prototype

Abstract Factory can use Prototype for product creation when cloning is easier than `new`.

---

## Practice Exercises

### Exercise 1: Theme Factory

Implement a `ThemeFactory` that creates `Button`, `Checkbox`, and `Tooltip` for both `LightTheme` and `DarkTheme`.

### Exercise 2: Platform Factory

Build factories for `Desktop` and `Mobile` UI components with the same product family.

### Exercise 3: Report Factory

Create a factory to produce report `Header`, `Body`, and `Footer` objects for `PDF` and `HTML` output.

### Exercise 4: Messaging Factory

Implement factories for `Email`, `SMS`, and `Push` message delivery families.

### Exercise 5: Factory Selector

Create a selector class that returns the correct concrete factory based on configuration.

---

## Summary

The Abstract Factory Pattern is ideal when you need to create families of related products and want to keep client code independent of concrete implementations.

It provides a strong structure for theme systems, platform-specific components, and extensible product families. Use it when product compatibility and flexibility matter more than simplicity.

Happy designing! 🚀
