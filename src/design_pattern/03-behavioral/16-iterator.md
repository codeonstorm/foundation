# Iterator Pattern - Deep Dive

**Goal:** Provide sequential access to elements of an aggregate object without exposing its underlying representation.

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

You have a collection of items and need a standard way to traverse it.

Problems without Iterator:
- Clients need to know the collection internals
- Traversal code is repeated in multiple places
- Collection implementation changes break consumers
- Custom loops are required for each container type

### Real Example

```php
$books = [
    ['title' => 'Design Patterns'],
    ['title' => 'Clean Code'],
];

foreach ($books as $book) {
    echo $book['title'] . "\n";
}
```

The above works for arrays, but if the collection becomes a custom object, you need an iterator.

---

## Pattern Concept

### What is Iterator?

The Iterator Pattern defines a way to access the elements of an aggregate object sequentially without exposing its underlying structure.

### Why It Matters

- Separates traversal from collection implementation
- Allows multiple traversals with different strategies
- Supports custom collections and domain-specific containers
- Keeps collection internals encapsulated

### When to Use

✅ Use Iterator when:
- You build custom collections
- You need support for `foreach`
- You want reusable traversal logic
- You want to hide container internals

❌ Avoid when:
- You only use native arrays
- Traversal logic is trivial and local
- Performance is critical and simple loops suffice

---

## Structure & Components

### Pattern Diagram

```
Client -> Iterator
           ^
           |
        Aggregate
```

### Key Components

| Component | Role |
|-----------|------|
| `Iterator` | Interface defining traversal methods |
| `ConcreteIterator` | Implements traversal for a collection |
| `Aggregate` | Interface for creating an iterator |
| `ConcreteAggregate` | Actual collection providing iterator |

### Typical Methods

- `current()` — return current element
- `next()` — advance to next element
- `key()` — return current key
- `valid()` — check if position is valid
- `rewind()` — reset position to first element

---

## PHP Implementation

### Standard PHP Iterator

PHP provides the built-in `Iterator` interface, which makes custom collections compatible with `foreach`.

```php
<?php
class BookCollection implements Iterator
{
    private array $books = [];
    private int $position = 0;

    public function __construct(array $books = [])
    {
        $this->books = $books;
        $this->position = 0;
    }

    public function current()
    {
        return $this->books[$this->position];
    }

    public function key(): int
    {
        return $this->position;
    }

    public function next(): void
    {
        $this->position++;
    }

    public function rewind(): void
    {
        $this->position = 0;
    }

    public function valid(): bool
    {
        return isset($this->books[$this->position]);
    }
}

$collection = new BookCollection([
    ['title' => 'Design Patterns'],
    ['title' => 'Clean Code'],
]);

foreach ($collection as $key => $book) {
    echo "{$key}: {$book['title']}\n";
}
```

### Aggregate + Iterator Separation

```php
<?php
interface BookCollectionInterface
{
    public function getIterator(): Iterator;
}

class BookCollection implements BookCollectionInterface
{
    private array $books = [];

    public function __construct(array $books = [])
    {
        $this->books = $books;
    }

    public function getIterator(): Iterator
    {
        return new BookIterator($this->books);
    }
}

class BookIterator implements Iterator
{
    private array $books;
    private int $position = 0;

    public function __construct(array $books)
    {
        $this->books = array_values($books);
    }

    public function current()
    {
        return $this->books[$this->position];
    }

    public function key(): int
    {
        return $this->position;
    }

    public function next(): void
    {
        $this->position++;
    }

    public function rewind(): void
    {
        $this->position = 0;
    }

    public function valid(): bool
    {
        return isset($this->books[$this->position]);
    }
}

$books = new BookCollection([
    ['title' => 'Refactoring'],
    ['title' => 'Working Effectively with Legacy Code'],
]);

foreach ($books->getIterator() as $book) {
    echo $book['title'] . "\n";
}
```

### PHP ArrayIterator Example

```php
<?php
$books = new ArrayObject([
    ['title' => 'Patterns of Enterprise Application Architecture'],
    ['title' => 'PHP Objects, Patterns, and Practice'],
]);

$iterator = $books->getIterator();

foreach ($iterator as $book) {
    echo $book['title'] . "\n";
}
```

---

## Real-World Scenarios

### Scenario 1: Paginated Data

A paginated collection can implement `Iterator` to allow traversal page-by-page without exposing internal pagination logic.

### Scenario 2: Tree Traversal

Iterators can traverse complex structures such as trees or graphs, e.g. `RecursiveIteratorIterator` for nested categories.

### Scenario 3: Event Queue

An event queue can expose an iterator for sequential event processing while hiding queue internals.

### Scenario 4: API Result Wrappers

Wrap API responses in a custom iterator to iterate over response items and hide metadata and pagination details.

---

## Pros & Cons

### Advantages ✅

- Encapsulates traversal logic
- Makes custom containers compatible with `foreach`
- Supports multiple traversal strategies
- Decouples collection from client code
- Encourages cleaner collection APIs

### Disadvantages ❌

- More boilerplate than simple arrays
- Can be overkill for trivial collections
- Custom iterator implementation can be error-prone
- Slight performance overhead compared to native arrays

---

## Best Practices

1. Use PHP's built-in `Iterator` interface whenever possible.
2. Keep iterator state internal and private.
3. Implement `rewind()` so the iterator can be reused.
4. Use `ArrayIterator`, `ArrayObject`, `RecursiveIteratorIterator`, or `IteratorAggregate` for standard behaviors.
5. Keep collection API expressive and avoid exposing raw arrays.
6. Prefer `IteratorAggregate` for simpler traversal wrappers.

### IteratorAggregate Example

```php
<?php
class BookCollection implements IteratorAggregate
{
    private array $books;

    public function __construct(array $books)
    {
        $this->books = $books;
    }

    public function getIterator(): Traversable
    {
        return new ArrayIterator($this->books);
    }
}
```

---

## Common Pitfalls

### Pitfall 1: Invalid `valid()` Implementation

If `valid()` is incorrect, loops may iterate too far or stop early.

### Pitfall 2: Not Reseting Position

Forget to reset position in `rewind()` and repeated traversals fail.

### Pitfall 3: Exposing Internal Collection

Returning the internal array directly defeats encapsulation.

### Pitfall 4: Mixing Iterator Logic with Collection Logic

Keep traversal separate from business logic for better maintainability.

---

## Variants

### `IteratorAggregate`

Use `IteratorAggregate` when the collection can return an iterator without implementing all iterator methods.

### `RecursiveIterator`

Use `RecursiveIterator` for nested structures such as trees, folders, or XML.

### `NoRewindIterator`

Use `NoRewindIterator` when the iterator should not rewind automatically.

---

## Practice Exercises

### Exercise 1: Custom Collection

Build a `TaskCollection` that implements `IteratorAggregate` and returns an `ArrayIterator`. Include tasks with `title`, `priority`, and `completed`.

### Exercise 2: Reverse Iterator

Create a reverse iterator that traverses a collection backwards.

### Exercise 3: Filtered Iterator

Use `CallbackFilterIterator` to iterate only items that match a condition, such as completed tasks.

### Exercise 4: Recursive Category Iterator

Model nested categories and implement `RecursiveIterator` to traverse all child categories recursively.

### Exercise 5: Domain-Specific Iterator

Implement `OrderItemCollection` for an ecommerce cart. Make it return only items with quantity > 0 and hide metadata.

---

## Summary

The Iterator Pattern separates how you traverse data from the data structure itself. In PHP, it is most useful for custom collections, domain objects, and wrappers around external data where `foreach` should still work cleanly.

Use built-in interfaces like `Iterator` and `IteratorAggregate` to keep your code interoperable with PHP language features.

Happy iterating! 🚀
