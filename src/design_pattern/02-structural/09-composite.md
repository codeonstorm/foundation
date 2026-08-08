# Composite Pattern - Deep Dive

**Goal:** Compose objects into tree structures and let clients treat individual objects and groups of objects through the same interface.

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

Many domains are naturally tree-shaped: a filesystem has files inside folders which can hold more folders, a UI has widgets inside panels which can hold more panels, an org chart has employees under managers who report to other managers.

The difficulty is treating a single item and a group of items the same way. Client code often ends up full of type checks:

- Is this a `File` or a `Directory`?
- Is this a single `Employee` or a whole `Department`?
- Is this a `Button` or a `Panel` full of other widgets?

Without a common abstraction, every piece of client code that walks the tree must branch on the concrete type.

### Real Example

```php
class File
{
    public function __construct(private string $name, private int $size)
    {
    }

    public function getSize(): int
    {
        return $this->size;
    }
}

class Directory
{
    private array $children = [];

    public function __construct(private string $name)
    {
    }

    public function add(File|Directory $item): void
    {
        $this->children[] = $item;
    }

    public function getChildren(): array
    {
        return $this->children;
    }
}

function totalSize(File|Directory $item): int
{
    if ($item instanceof File) {
        return $item->getSize();
    }

    if ($item instanceof Directory) {
        $sum = 0;

        foreach ($item->getChildren() as $child) {
            // Recursive branching, repeated everywhere this tree is walked.
            $sum += totalSize($child);
        }

        return $sum;
    }

    return 0;
}
```

The problem: every function that operates on this tree must `instanceof`-check and branch between the single case and the collection case. Adding a new node type means touching every one of these functions.

The Composite Pattern fixes this by giving `File` and `Directory` the same interface. The client calls one method, and each node decides for itself whether to compute its own value or delegate to its children. No branching, no `instanceof`, and the tree can grow arbitrarily deep without changing client code.

---

## Pattern Concept

### What is Composite?

The Composite Pattern lets you build tree structures made of simple (leaf) objects and container (composite) objects, both implementing the same component interface. A client calls an operation on the root of the tree, and that call transparently propagates through every branch and leaf, without the client knowing which kind of node it is dealing with.

### Why It Matters

- Treats individual objects and groups of objects uniformly
- Removes `instanceof` checks and type branching from client code
- Makes recursive operations (sum, render, search) trivial to write once
- Lets you add new leaf or composite types without changing existing code
- Naturally models real hierarchies: filesystems, UI trees, org charts, categories

### When to Use

Use Composite when:
- Your domain is naturally a tree or part-whole hierarchy
- Clients should not care whether they are working with one object or many
- You want to run the same operation recursively over a whole structure
- New node types need to be added without breaking existing traversal code

Avoid Composite when:
- The structure is flat and never nests
- Leaf and container behavior are too different to share a meaningful interface
- Forcing a uniform interface would leave many methods meaningless on leaves
- A simple array or collection class already solves the problem clearly

---

## Structure & Components

### Pattern Diagram

```text
Client -> Component Interface
                ^
                |
        +-------+-------+
        |               |
      Leaf          Composite
                        |
                        v
                  Component[] (children)
                   /      |      \
                Leaf   Leaf   Composite
```

### Key Components

| Component | Role |
|-----------|------|
| `Component` | Common interface implemented by both leaves and composites |
| `Leaf` | End node with no children, implements the operation directly |
| `Composite` | Holds a list of child components and delegates the operation to them |
| `Client` | Works only through the component interface, unaware of leaf vs composite |

### Typical Methods

- `operation()` - the shared behavior every node must implement
- `add(Component $child)` - adds a child to a composite
- `remove(Component $child)` - removes a child from a composite
- `getChild(int $index)` - retrieves a specific child
- `getChildren()` - returns all children for iteration

---

## PHP Implementation

### Example 1: Filesystem (Files and Directories)

```php
<?php

interface FilesystemComponent
{
    public function getSize(): int;
    public function print(int $depth = 0): void;
}

class File implements FilesystemComponent
{
    public function __construct(
        private string $name,
        private int $size
    ) {
    }

    public function getSize(): int
    {
        return $this->size;
    }

    public function print(int $depth = 0): void
    {
        echo str_repeat('  ', $depth) . "- {$this->name} ({$this->size}KB)\n";
    }
}

class Directory implements FilesystemComponent
{
    private array $children = [];

    public function __construct(private string $name)
    {
    }

    public function add(FilesystemComponent $child): void
    {
        $this->children[] = $child;
    }

    public function remove(FilesystemComponent $child): void
    {
        $this->children = array_filter(
            $this->children,
            fn (FilesystemComponent $c) => $c !== $child
        );
    }

    public function getSize(): int
    {
        $total = 0;

        foreach ($this->children as $child) {
            $total += $child->getSize();
        }

        return $total;
    }

    public function print(int $depth = 0): void
    {
        echo str_repeat('  ', $depth) . "+ {$this->name}/\n";

        foreach ($this->children as $child) {
            $child->print($depth + 1);
        }
    }
}

$root = new Directory('project');
$src = new Directory('src');
$src->add(new File('index.php', 4));
$src->add(new File('helpers.php', 2));

$docs = new Directory('docs');
$docs->add(new File('readme.md', 1));

$root->add($src);
$root->add($docs);
$root->add(new File('composer.json', 1));

$root->print();
echo "Total size: {$root->getSize()}KB\n";
```

### Expected Output

```text
+ project/
  + src/
    - index.php (4KB)
    - helpers.php (2KB)
  + docs/
    - readme.md (1KB)
  - composer.json (1KB)
Total size: 8KB
```

### Example 2: UI Component Tree

```php
<?php

interface UiComponent
{
    public function render(int $depth = 0): void;
}

class Button implements UiComponent
{
    public function __construct(private string $label)
    {
    }

    public function render(int $depth = 0): void
    {
        echo str_repeat('  ', $depth) . "[Button: {$this->label}]\n";
    }
}

class TextField implements UiComponent
{
    public function __construct(private string $placeholder)
    {
    }

    public function render(int $depth = 0): void
    {
        echo str_repeat('  ', $depth) . "[TextField: {$this->placeholder}]\n";
    }
}

class Panel implements UiComponent
{
    private array $children = [];

    public function __construct(private string $title)
    {
    }

    public function add(UiComponent $component): void
    {
        $this->children[] = $component;
    }

    public function render(int $depth = 0): void
    {
        echo str_repeat('  ', $depth) . "Panel: {$this->title}\n";

        foreach ($this->children as $child) {
            $child->render($depth + 1);
        }
    }
}

$loginForm = new Panel('Login Form');
$loginForm->add(new TextField('Username'));
$loginForm->add(new TextField('Password'));
$loginForm->add(new Button('Sign In'));

$window = new Panel('Application Window');
$window->add($loginForm);
$window->add(new Button('Exit'));

$window->render();
```

### Expected Output

```text
Panel: Application Window
  Panel: Login Form
    [TextField: Username]
    [TextField: Password]
    [Button: Sign In]
  [Button: Exit]
```

### Example 3: Organization Chart (Child Management & Traversal Order)

```php
<?php

interface OrgUnit
{
    public function getSalaryCost(): float;
    public function getName(): string;
}

class Employee implements OrgUnit
{
    public function __construct(
        private string $name,
        private float $salary
    ) {
    }

    public function getName(): string
    {
        return $this->name;
    }

    public function getSalaryCost(): float
    {
        return $this->salary;
    }
}

class Manager implements OrgUnit
{
    private array $reports = [];

    public function __construct(
        private string $name,
        private float $salary
    ) {
    }

    public function addReport(OrgUnit $unit): void
    {
        $this->reports[] = $unit;
    }

    public function removeReport(OrgUnit $unit): void
    {
        $this->reports = array_values(array_filter(
            $this->reports,
            fn (OrgUnit $u) => $u !== $unit
        ));
    }

    public function getReports(): array
    {
        return $this->reports;
    }

    public function getName(): string
    {
        return $this->name;
    }

    public function getSalaryCost(): float
    {
        $total = $this->salary;

        // Traversal order follows insertion order of addReport() calls.
        foreach ($this->reports as $report) {
            $total += $report->getSalaryCost();
        }

        return $total;
    }
}

$cto = new Manager('Dana (CTO)', 12000);

$backendLead = new Manager('Sam (Backend Lead)', 9000);
$backendLead->addReport(new Employee('Ivy', 6000));
$backendLead->addReport(new Employee('Leo', 5800));

$frontendDev = new Employee('Mia', 6200);

$cto->addReport($backendLead);
$cto->addReport($frontendDev);

echo "Total org cost under {$cto->getName()}: {$cto->getSalaryCost()}\n";

$backendLead->removeReport($cto->getReports()[0]->getReports()[1] ?? new Employee('none', 0));
```

### Expected Output

```text
Total org cost under Dana (CTO): 39000
```

---

## Real-World Scenarios

### Scenario 1: Filesystems

Operating systems and file managers use composite trees where directories contain files and other directories, and operations like "calculate size" or "copy" apply recursively to the whole tree.

### Scenario 2: UI Component Trees

GUI and web frameworks build screens as trees of components. A `Panel` renders itself by rendering its children, whether those children are simple widgets or nested panels.

### Scenario 3: Organization Charts

HR and reporting tools model companies as a hierarchy of employees and managers, where a manager's total headcount or payroll cost is the sum of everyone below them.

### Scenario 4: Menu and Category Trees in E-Commerce

Product categories often nest (Electronics > Computers > Laptops). A composite tree lets you compute the total product count, render a nested menu, or apply a discount rule uniformly across a category and all its subcategories.

---

## Pros & Cons

### Advantages

- Uniform treatment of individual objects and groups
- Simplifies client code by removing type checks
- Makes it easy to add new leaf or composite types
- Naturally expresses recursive, tree-shaped domains
- Supports operations that work at any level of the hierarchy

### Disadvantages

- Can make the design overly general, hiding the real differences between node types
- Leaf classes may be forced to implement methods that make no sense for them (`add`, `remove`)
- Debugging deep trees can be harder because behavior is distributed across many small classes
- Overuse can turn a simple list into an unnecessarily complex tree structure

---

## Best Practices

1. Define one component interface and keep it as small as possible.
2. Put child-management methods (`add`, `remove`) only where they naturally belong, and document the tradeoff if you also expose them on the interface.
3. Let composites delegate work to children rather than duplicating logic.
4. Keep traversal order predictable and based on insertion order unless a sort is explicitly required.
5. Avoid mixing unrelated responsibilities into the component interface (rendering and persistence, for example, should stay separate).
6. Guard against cycles when building trees dynamically, since a composite added to itself will cause infinite recursion.
7. Prefer composition over deep inheritance chains when creating specialized leaf or composite variants.
8. Write recursive operations so a leaf's base case terminates the recursion cleanly.

### Good Composite Design

```php
<?php

interface Node
{
    public function getSize(): int;
}

final class FileNode implements Node
{
    public function __construct(private int $size)
    {
    }

    public function getSize(): int
    {
        return $this->size;
    }
}

final class FolderNode implements Node
{
    /** @var Node[] */
    private array $children = [];

    public function add(Node $child): void
    {
        $this->children[] = $child;
    }

    public function getSize(): int
    {
        return array_sum(array_map(
            fn (Node $child) => $child->getSize(),
            $this->children
        ));
    }
}
```

This design keeps the `Node` interface minimal (only the operation both leaf and composite genuinely share), and `add()` lives only on `FolderNode`, where it makes sense.

---

## Common Pitfalls

### Pitfall 1: Forcing Leaves to Implement `add`/`remove`

Putting child-management methods on the shared interface forces leaves to implement operations that make no sense for them.

```php
interface Component
{
    public function add(Component $child): void;
    public function operation(): void;
}

class Leaf implements Component
{
    public function add(Component $child): void
    {
        throw new \LogicException('Cannot add children to a leaf.');
    }

    public function operation(): void
    {
        // ...
    }
}
```

Handle this by either throwing a clear exception (transparent composite, shown above) or by splitting the interface so `add`/`remove` only exist on a `Composite`-specific interface (safe composite), keeping `Leaf` free of meaningless methods.

### Pitfall 2: Confusing Composite with Decorator

Both patterns wrap objects behind a shared interface, but their intent differs: Composite builds part-whole trees where a node's behavior is the aggregation of its children, while Decorator wraps a single object to add new behavior without changing its interface.

```php
// Composite: aggregates many children of the same interface.
class Folder implements Node { /* holds Node[] */ }

// Decorator: wraps exactly one object to add behavior.
class LoggingNode implements Node
{
    public function __construct(private Node $wrapped) {}
}
```

If you find yourself wrapping only a single child "for extra behavior," you likely want Decorator, not Composite.

### Pitfall 3: Unbounded or Cyclic Trees

Dynamically built trees can accidentally add a composite as its own descendant, causing infinite recursion.

```php
$folder->add($folder); // Infinite loop when getSize() recurses.
```

Validate additions, or track visited nodes during traversal, to prevent cycles.

### Pitfall 4: Overusing Composite for Simple Lists

If a collection never actually nests and never needs a "treat one like many" abstraction, a composite tree adds needless indirection over a plain array or collection class.

```php
// Overkill if $items never contains nested collections.
$total = array_sum(array_map(fn ($i) => $i->getPrice(), $items));
```

Reach for Composite only once real nesting or recursive operations appear.

---

## Variants

### Transparent Composite

`add()`, `remove()`, and `getChild()` are declared on the `Component` interface itself. Leaves inherit these methods and throw an exception if called, keeping the interface uniform at the cost of leaves exposing operations they cannot support.

### Safe Composite

Child-management methods live only on the `Composite` class, not on `Component`. Leaves never see `add`/`remove` at all, but client code that wants to add children must know it is working with a `Composite`, reintroducing some type-awareness.

### Iterator-Based Traversal

Instead of each composite manually looping over children inside every operation, a composite can expose an `Iterator` (implementing PHP's `IteratorAggregate`) so external code can traverse the tree (depth-first, breadth-first, or filtered) without the composite hardcoding the traversal strategy.

```php
class Folder implements IteratorAggregate
{
    private array $children = [];

    public function getIterator(): Iterator
    {
        return new ArrayIterator($this->children);
    }
}
```

---

## Practice Exercises

### Exercise 1: Directory Size Calculator

Build `File` and `Directory` classes implementing a shared `FilesystemComponent` interface. Compute total size recursively for a multi-level tree.

### Exercise 2: Safe vs Transparent Composite

Implement the same menu-tree example twice: once with `add`/`remove` on the shared interface (leaves throw), and once with `add`/`remove` only on the composite class. Compare the client code needed for each.

### Exercise 3: HTML Renderer

Model HTML elements (`TextNode` as leaf, `ElementNode` as composite holding child nodes and a tag name) and render a nested HTML string recursively.

### Exercise 4: Product Category Tree

Build a category tree (`Category` as composite, `Product` as leaf) and implement `countProducts()` and `getTotalValue()` that work at any level of nesting.

### Exercise 5: Cycle Guard

Add a check to your `Composite::add()` method that throws an exception if the child being added is an ancestor of the current node, preventing infinite recursion.

---

## Summary

The Composite Pattern lets individual objects and whole groups of objects be treated identically through one shared interface. It shines in naturally tree-shaped domains like filesystems, UI trees, org charts, and category trees, where recursive operations should work the same way regardless of depth.

The main tension to manage is how much of the child-management API belongs on the shared interface: putting it there (transparent composite) simplifies client code but forces leaves to reject nonsensical calls, while keeping it only on composites (safe composite) keeps leaves clean but requires clients to be type-aware when building the tree.
