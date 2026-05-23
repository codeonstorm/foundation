# Interpreter Pattern - Deep Dive

**Goal:** Define a small language grammar and represent each grammar rule as a class that can interpret an expression.

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

Sometimes your application needs to understand a small language or expression format.

Examples:

- Discount rule: `cart_total > 1000 AND customer_type = vip`
- Search filter: `status = active AND role = admin`
- Math expression: `10 + 20 * 3`
- Permission rule: `is_admin OR owns_resource`
- Template placeholder: `Hello {{name}}`

Without Interpreter, logic often becomes a hard-coded set of conditionals or unsafe string evaluation.

### Real Example

```php
if ($cartTotal > 1000 && $customerType === 'vip') {
    echo "Apply discount";
}
```

This is fine for one rule. But if rules are stored in a database, configured by admins, or composed dynamically, hard-coded conditionals become limiting.

Interpreter solves this by representing the rule as an object tree.

---

## Pattern Concept

### What is Interpreter?

The Interpreter Pattern represents grammar rules as classes.

Each expression object knows how to interpret itself using a shared context.

For example, this rule:

```text
cart_total > 1000 AND customer_type = vip
```

Can become an object tree:

```text
AndExpression
  - GreaterThanExpression(cart_total, 1000)
  - EqualsExpression(customer_type, vip)
```

### Why It Matters

- Turns rules into objects
- Makes expressions composable
- Avoids unsafe `eval()`
- Allows rule trees to be tested
- Useful for small domain-specific languages
- Separates parsing from execution

### When to Use

Use Interpreter when:
- You have a small language or rule format
- Grammar is simple enough to model with classes
- Rules need to be composed dynamically
- You want to evaluate expressions against a context
- You want to avoid large conditional blocks

Avoid Interpreter when:
- The grammar is complex
- You need a full parser, compiler, or query engine
- Performance is critical for huge expression trees
- A simple Strategy or Specification pattern is enough
- Existing libraries already solve the language problem

---

## Structure & Components

### Pattern Diagram

```text
Client -> Expression -> interpret(Context)
              ^
              |
   +----------+-----------+
   |                      |
 TerminalExpression  NonTerminalExpression
```

### Key Components

| Component | Role |
|-----------|------|
| `Expression` | Common interface with `interpret()` |
| `TerminalExpression` | Represents a simple value, variable, or literal |
| `NonTerminalExpression` | Combines other expressions |
| `Context` | Holds input data used during interpretation |
| `Client` | Builds the expression tree and runs it |

### Typical Methods

- `interpret()` - evaluates the expression
- `evaluate()` - common alternative name
- `get()` - context lookup
- `parse()` - optional parser that creates expression objects

---

## PHP Implementation

### Basic Boolean Rule Interpreter

```php
<?php

interface Expression
{
    public function interpret(array $context): bool;
}

class EqualsExpression implements Expression
{
    public function __construct(
        private string $field,
        private mixed $expected
    ) {
    }

    public function interpret(array $context): bool
    {
        return ($context[$this->field] ?? null) === $this->expected;
    }
}

class GreaterThanExpression implements Expression
{
    public function __construct(
        private string $field,
        private float $value
    ) {
    }

    public function interpret(array $context): bool
    {
        return ($context[$this->field] ?? 0) > $this->value;
    }
}

class AndExpression implements Expression
{
    public function __construct(
        private Expression $left,
        private Expression $right
    ) {
    }

    public function interpret(array $context): bool
    {
        return $this->left->interpret($context) && $this->right->interpret($context);
    }
}

class OrExpression implements Expression
{
    public function __construct(
        private Expression $left,
        private Expression $right
    ) {
    }

    public function interpret(array $context): bool
    {
        return $this->left->interpret($context) || $this->right->interpret($context);
    }
}

$rule = new AndExpression(
    new GreaterThanExpression('cart_total', 1000),
    new EqualsExpression('customer_type', 'vip')
);

$context = [
    'cart_total' => 1500,
    'customer_type' => 'vip',
];

if ($rule->interpret($context)) {
    echo "Apply discount\n";
}
```

The rule is now an object tree, not a hard-coded conditional.

### Proper Example: Discount Rule Interpreter

```php
<?php

interface RuleExpression
{
    public function evaluate(array $context): bool;
}

class FieldEquals implements RuleExpression
{
    public function __construct(
        private string $field,
        private mixed $value
    ) {
    }

    public function evaluate(array $context): bool
    {
        return ($context[$this->field] ?? null) === $this->value;
    }
}

class FieldGreaterThan implements RuleExpression
{
    public function __construct(
        private string $field,
        private float $value
    ) {
    }

    public function evaluate(array $context): bool
    {
        return ($context[$this->field] ?? 0) > $this->value;
    }
}

class FieldInList implements RuleExpression
{
    public function __construct(
        private string $field,
        private array $allowedValues
    ) {
    }

    public function evaluate(array $context): bool
    {
        return in_array($context[$this->field] ?? null, $this->allowedValues, true);
    }
}

class AllOf implements RuleExpression
{
    private array $expressions;

    public function __construct(RuleExpression ...$expressions)
    {
        $this->expressions = $expressions;
    }

    public function evaluate(array $context): bool
    {
        foreach ($this->expressions as $expression) {
            if (!$expression->evaluate($context)) {
                return false;
            }
        }

        return true;
    }
}

class AnyOf implements RuleExpression
{
    private array $expressions;

    public function __construct(RuleExpression ...$expressions)
    {
        $this->expressions = $expressions;
    }

    public function evaluate(array $context): bool
    {
        foreach ($this->expressions as $expression) {
            if ($expression->evaluate($context)) {
                return true;
            }
        }

        return false;
    }
}

class NotExpression implements RuleExpression
{
    public function __construct(private RuleExpression $expression)
    {
    }

    public function evaluate(array $context): bool
    {
        return !$this->expression->evaluate($context);
    }
}

$discountRule = new AllOf(
    new FieldGreaterThan('cart_total', 2000),
    new AnyOf(
        new FieldEquals('customer_type', 'vip'),
        new FieldInList('coupon', ['FESTIVE10', 'LOYALTY20'])
    ),
    new NotExpression(new FieldEquals('blocked', true))
);

$cart = [
    'cart_total' => 2500,
    'customer_type' => 'regular',
    'coupon' => 'FESTIVE10',
    'blocked' => false,
];

if ($discountRule->evaluate($cart)) {
    echo "Discount allowed\n";
} else {
    echo "Discount not allowed\n";
}
```

This is a practical rule engine. You can compose rules without changing the evaluator.

### Math Expression Example

```php
<?php

interface MathExpression
{
    public function value(): float;
}

class NumberExpression implements MathExpression
{
    public function __construct(private float $number)
    {
    }

    public function value(): float
    {
        return $this->number;
    }
}

class AddExpression implements MathExpression
{
    public function __construct(
        private MathExpression $left,
        private MathExpression $right
    ) {
    }

    public function value(): float
    {
        return $this->left->value() + $this->right->value();
    }
}

class MultiplyExpression implements MathExpression
{
    public function __construct(
        private MathExpression $left,
        private MathExpression $right
    ) {
    }

    public function value(): float
    {
        return $this->left->value() * $this->right->value();
    }
}

// Represents: 10 + (20 * 3)
$expression = new AddExpression(
    new NumberExpression(10),
    new MultiplyExpression(
        new NumberExpression(20),
        new NumberExpression(3)
    )
);

echo $expression->value() . "\n"; // 70
```

### Small Parser Example

This tiny parser converts simple tokens into expression objects. It supports rules like `role=admin`.

```php
<?php

class SimpleRuleParser
{
    public function parse(string $rule): RuleExpression
    {
        [$field, $value] = explode('=', $rule, 2);

        return new FieldEquals(trim($field), trim($value));
    }
}

$parser = new SimpleRuleParser();
$rule = $parser->parse('role=admin');

echo $rule->evaluate(['role' => 'admin']) ? 'yes' : 'no';
```

Real parsers are more involved, but the idea is the same: parse text into an expression tree, then interpret the tree.

---

## Real-World Scenarios

### Scenario 1: Discount Rules

Admin-configured discount rules can be represented as expression objects.

### Scenario 2: Permission Rules

Rules like `is_admin OR owns_resource` can be interpreted against the current user and resource.

### Scenario 3: Search Filters

Filter expressions can be parsed into objects and converted to database queries or in-memory checks.

### Scenario 4: Template Variables

Simple template expressions such as `{{ user.name }}` can be parsed and resolved from context.

### Scenario 5: Math or Formula Evaluation

Simple formulas can be represented as expression trees and evaluated safely.

---

## Pros & Cons

### Advantages

- Represents rules as objects
- Avoids unsafe string evaluation
- Makes expressions composable
- Easy to test individual expressions
- Useful for small domain-specific languages
- Can make business rules configurable

### Disadvantages

- Many small classes
- Complex grammars become difficult quickly
- Parsing text safely can be hard
- Large expression trees may affect performance
- Existing parser or rule-engine libraries may be better for advanced needs

---

## Best Practices

1. Keep the grammar small.
2. Separate parsing from interpretation.
3. Avoid `eval()` for user-provided expressions.
4. Use clear expression class names.
5. Test each expression class independently.
6. Validate input before building expression trees.
7. Prefer existing parser libraries for complex languages.
8. Keep context data explicit and predictable.

### Good Interpreter Design

```php
<?php

interface RuleExpression
{
    public function evaluate(array $context): bool;
}
```

The expression interface is small and focused.

---

## Common Pitfalls

### Pitfall 1: Building a Full Language Accidentally

Interpreter is manageable for small languages. If you need variables, functions, precedence, errors, types, and optimization, use a parser library or existing engine.

### Pitfall 2: Mixing Parsing and Evaluation

Keep parsing text separate from evaluating expression objects. This makes both parts easier to test.

### Pitfall 3: Unsafe Evaluation

Do not use `eval()` for user input. Build an expression tree and evaluate it safely.

### Pitfall 4: Unclear Context

If expressions depend on random global state, they become hard to reason about. Pass context explicitly.

### Pitfall 5: Too Much Boilerplate

If the rule set is tiny, a Strategy or Specification pattern may be simpler.

---

## Variants

### Boolean Interpreter

Evaluates rules to true or false.

### Arithmetic Interpreter

Evaluates math expressions to numbers.

### Template Interpreter

Resolves placeholders or simple template expressions.

### Query Interpreter

Converts expressions into database query conditions.

### Interpreter + Composite

Non-terminal expressions like `AllOf` and `AnyOf` compose child expressions, making Interpreter closely related to Composite.

---

## Practice Exercises

### Exercise 1: Permission Rule

Build expressions for:
- `IsAdmin`
- `OwnsResource`
- `IsVerified`
- `AndExpression`
- `OrExpression`

Evaluate whether a user can edit a resource.

### Exercise 2: Discount Rule Engine

Create rules for cart total, coupon code, customer type, and blocked users.

### Exercise 3: Math Expressions

Add subtraction and division to the math expression example.

### Exercise 4: Search Filter

Create expressions for `Equals`, `Contains`, and `GreaterThan`. Evaluate them against an array of products.

### Exercise 5: Simple Parser

Extend the simple parser to support:
- `field=value`
- `field>number`
- `field<number`

Then convert those strings into expression objects.

---

## Summary

The Interpreter Pattern represents a small language as a set of expression classes.

It is useful for rules, filters, permissions, simple formulas, and small domain-specific languages.

Use it when the grammar is small and you want safe, composable, testable expressions. For complex languages, reach for a real parser or rule-engine library.

