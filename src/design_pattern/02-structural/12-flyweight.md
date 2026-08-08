# Flyweight Pattern - Deep Dive

**Goal:** Minimize memory usage by sharing common, immutable state across many similar objects instead of duplicating it in every instance.

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

Some applications need to create a huge number of nearly-identical objects.

Examples:

- Rendering millions of trees or particles in a game
- Drawing every character glyph in a text editor as an object
- Placing thousands of map icons on a GIS application
- Building large in-memory reports made of many small, repeated cells

Each of these objects usually carries a mix of two kinds of data:

- Data that is shared and identical across many instances (a texture, a font, an icon bitmap)
- Data that is unique to each instance (a position, a character index, a coordinate)

If every object stores both kinds of data itself, the shared data gets duplicated thousands or millions of times, and memory usage grows far beyond what is actually needed.

### Real Example

```php
class Tree
{
    public function __construct(
        private int $x,
        private int $y,
        private string $name,
        private string $color,
        private string $texture // Imagine this holds several KB of bitmap data.
    ) {
    }

    public function render(): void
    {
        echo "Rendering {$this->name} tree at ({$this->x}, {$this->y}) with {$this->color} color\n";
    }
}

$forest = [];

for ($i = 0; $i < 100000; $i++) {
    // Every tree duplicates the same name, color, and texture data.
    $forest[] = new Tree($i, $i * 2, 'Oak', 'Green', 'oak_texture_bitmap_data');
}

echo count($forest) . " trees created, each storing its own copy of the texture\n";
```

The problem: 100,000 trees means 100,000 copies of `name`, `color`, and `texture`, even though most trees of the same species look identical. Only the position actually differs per tree.

Flyweight solves this by splitting object state into two parts. **Intrinsic state** (shared, reusable data like texture and color) is stored once inside a shared flyweight object. **Extrinsic state** (unique data like position) stays outside the flyweight and is passed in whenever it is needed. A factory hands out shared flyweight instances instead of letting each object build its own.

---

## Pattern Concept

### What is Flyweight?

The Flyweight Pattern lets many objects share a single instance that holds common, immutable data. Instead of creating a new object per logical entity, the client keeps a small amount of unique data and borrows a shared flyweight object to do the work, supplying the unique data as parameters.

### Why It Matters

- **Intrinsic state** is the data stored inside the flyweight: shared, immutable, independent of context (font, texture, icon bitmap, color)
- **Extrinsic state** is the data kept outside the flyweight: unique per object, context-dependent, passed into methods at call time (position, character index, label text)
- Sharing intrinsic state can turn millions of heavy objects into a handful of lightweight, reusable ones
- The client becomes responsible for tracking and supplying extrinsic state
- A factory ensures the same intrinsic state is never created twice
- Reduces memory pressure, garbage collection overhead, and object construction cost

### When to Use

Use Flyweight when:
- An application needs to create a very large number of similar objects
- Object storage costs are a real problem due to volume
- Most of an object's state can be made extrinsic and passed in externally
- Objects can be grouped by shared, immutable characteristics

Avoid Flyweight when:
- The number of objects is small and memory is not a concern
- Objects do not share meaningful intrinsic state
- The added indirection and bookkeeping outweigh the memory savings
- The shared state needs to change independently per object (mutability breaks sharing)

---

## Structure & Components

### Pattern Diagram

```text
Client (holds extrinsic state)
   |
   | getFlyweight(key)
   v
FlyweightFactory
   |
   | returns shared instance
   v
Flyweight Pool
   +-- ConcreteFlyweight A (intrinsic state)
   +-- ConcreteFlyweight B (intrinsic state)
   +-- ConcreteFlyweight C (intrinsic state)

Client -> flyweight->operation(extrinsicState)
```

### Key Components

| Component | Role |
|-----------|------|
| `Flyweight` | Interface declaring the operation that accepts extrinsic state |
| `ConcreteFlyweight` | Stores intrinsic (shared) state and implements the operation |
| `FlyweightFactory` | Creates and caches flyweights, returning existing ones when possible |
| `Client` | Stores or computes extrinsic state and passes it into flyweight methods |
| Intrinsic state | Shared, immutable data stored inside the flyweight |
| Extrinsic state | Unique, context-dependent data supplied by the client at call time |

### Typical Methods

- `getFlyweight(key)` - factory method that returns a shared flyweight, creating one if needed
- `operation(extrinsicState)` - flyweight method that uses both its own intrinsic state and the passed-in extrinsic state
- `getPoolSize()` - optional factory method to inspect how many flyweights currently exist

---

## PHP Implementation

### Example 1: TreeType Flyweight Factory for a Forest

The `TreeType` is the flyweight holding shared, intrinsic data. Each `Tree` keeps only its coordinates (extrinsic state) and a reference to a shared `TreeType`.

```php
<?php

class TreeType
{
    public function __construct(
        private string $name,
        private string $color,
        private string $texture
    ) {
    }

    public function render(int $x, int $y): void
    {
        echo "Rendering {$this->name} tree ({$this->color}) at ({$x}, {$y}) using texture '{$this->texture}'\n";
    }
}

class TreeTypeFactory
{
    private array $treeTypes = [];

    public function getTreeType(string $name, string $color, string $texture): TreeType
    {
        $key = "{$name}_{$color}_{$texture}";

        if (!isset($this->treeTypes[$key])) {
            echo "Creating new TreeType: {$key}\n";
            $this->treeTypes[$key] = new TreeType($name, $color, $texture);
        }

        return $this->treeTypes[$key];
    }

    public function getCreatedCount(): int
    {
        return count($this->treeTypes);
    }
}

class Tree
{
    public function __construct(
        private int $x,
        private int $y,
        private TreeType $type
    ) {
    }

    public function render(): void
    {
        $this->type->render($this->x, $this->y);
    }
}

class Forest
{
    private array $trees = [];

    public function __construct(private TreeTypeFactory $factory)
    {
    }

    public function plantTree(int $x, int $y, string $name, string $color, string $texture): void
    {
        $type = $this->factory->getTreeType($name, $color, $texture);
        $this->trees[] = new Tree($x, $y, $type);
    }

    public function render(): void
    {
        foreach ($this->trees as $tree) {
            $tree->render();
        }
    }

    public function getTreeCount(): int
    {
        return count($this->trees);
    }
}

$factory = new TreeTypeFactory();
$forest = new Forest($factory);

$forest->plantTree(1, 2, 'Oak', 'Green', 'oak_bitmap');
$forest->plantTree(5, 8, 'Oak', 'Green', 'oak_bitmap');
$forest->plantTree(10, 3, 'Pine', 'DarkGreen', 'pine_bitmap');
$forest->plantTree(15, 20, 'Oak', 'Green', 'oak_bitmap');

$forest->render();

echo "Trees planted: {$forest->getTreeCount()}\n";
echo "Distinct TreeType objects created: {$factory->getCreatedCount()}\n";
```

### Expected Output

```text
Creating new TreeType: Oak_Green_oak_bitmap
Creating new TreeType: Pine_DarkGreen_pine_bitmap
Rendering Oak tree (Green) at (1, 2) using texture 'oak_bitmap'
Rendering Oak tree (Green) at (5, 8) using texture 'oak_bitmap'
Rendering Pine tree (DarkGreen) at (10, 3) using texture 'pine_bitmap'
Rendering Oak tree (Green) at (15, 20) using texture 'oak_bitmap'
Trees planted: 4
Distinct TreeType objects created: 2
```

Four trees were planted, but only two `TreeType` objects were created because three of the trees share the same "Oak, Green, oak_bitmap" combination.

### Example 2: Character Glyph Flyweight for a Text Editor

Each `CharacterStyle` (font, size, weight) is shared across many character positions. The `Character` only stores the extrinsic data: the letter and its position.

```php
<?php

class CharacterStyle
{
    public function __construct(
        private string $font,
        private int $size,
        private string $weight
    ) {
    }

    public function render(string $char, int $position): void
    {
        echo "Char '{$char}' at position {$position} [{$this->font}, {$this->size}pt, {$this->weight}]\n";
    }
}

class CharacterStyleFactory
{
    private array $styles = [];

    public function getStyle(string $font, int $size, string $weight): CharacterStyle
    {
        $key = "{$font}_{$size}_{$weight}";

        if (!isset($this->styles[$key])) {
            $this->styles[$key] = new CharacterStyle($font, $size, $weight);
        }

        return $this->styles[$key];
    }

    public function getStyleCount(): int
    {
        return count($this->styles);
    }
}

class Character
{
    public function __construct(
        private string $char,
        private int $position,
        private CharacterStyle $style
    ) {
    }

    public function render(): void
    {
        $this->style->render($this->char, $this->position);
    }
}

class TextDocument
{
    private array $characters = [];

    public function __construct(private CharacterStyleFactory $factory)
    {
    }

    public function addChar(string $char, int $position, string $font, int $size, string $weight): void
    {
        $style = $this->factory->getStyle($font, $size, $weight);
        $this->characters[] = new Character($char, $position, $style);
    }

    public function render(): void
    {
        foreach ($this->characters as $character) {
            $character->render();
        }
    }

    public function getCharCount(): int
    {
        return count($this->characters);
    }
}

$factory = new CharacterStyleFactory();
$document = new TextDocument($factory);

$text = 'Hi!';
foreach (str_split($text) as $index => $char) {
    $document->addChar($char, $index, 'Arial', 12, 'Regular');
}

$document->addChar('!', 3, 'Arial', 14, 'Bold');

$document->render();

echo "Characters in document: {$document->getCharCount()}\n";
echo "Distinct styles created: {$factory->getStyleCount()}\n";
```

### Expected Output

```text
Char 'H' at position 0 [Arial, 12pt, Regular]
Char 'i' at position 1 [Arial, 12pt, Regular]
Char '!' at position 2 [Arial, 12pt, Regular]
Char '!' at position 3 [Arial, 14pt, Bold]
Characters in document: 4
Distinct styles created: 2
```

Four characters were rendered, but only two distinct `CharacterStyle` flyweights were needed, since three characters share the same font/size/weight combination.

### Example 3: Game Particle Pool with Memory Savings

This example tracks how many particle "types" were actually created versus how many times a type was reused, demonstrating the memory savings directly.

```php
<?php

class ParticleType
{
    public function __construct(
        private string $sprite,
        private string $color
    ) {
    }

    public function draw(float $x, float $y, float $velocityX, float $velocityY): void
    {
        echo "Drawing {$this->color} '{$this->sprite}' particle at ({$x}, {$y}) moving ({$velocityX}, {$velocityY})\n";
    }
}

class ParticleTypeFactory
{
    private array $pool = [];
    private int $createdCount = 0;
    private int $reusedCount = 0;

    public function getParticleType(string $sprite, string $color): ParticleType
    {
        $key = "{$sprite}_{$color}";

        if (!isset($this->pool[$key])) {
            $this->pool[$key] = new ParticleType($sprite, $color);
            $this->createdCount++;
        } else {
            $this->reusedCount++;
        }

        return $this->pool[$key];
    }

    public function getStats(): array
    {
        return [
            'created' => $this->createdCount,
            'reused' => $this->reusedCount,
        ];
    }
}

class Particle
{
    public function __construct(
        private float $x,
        private float $y,
        private float $velocityX,
        private float $velocityY,
        private ParticleType $type
    ) {
    }

    public function draw(): void
    {
        $this->type->draw($this->x, $this->y, $this->velocityX, $this->velocityY);
    }
}

class ParticleSystem
{
    private array $particles = [];

    public function __construct(private ParticleTypeFactory $factory)
    {
    }

    public function emit(float $x, float $y, float $vx, float $vy, string $sprite, string $color): void
    {
        $type = $this->factory->getParticleType($sprite, $color);
        $this->particles[] = new Particle($x, $y, $vx, $vy, $type);
    }

    public function drawAll(): void
    {
        foreach ($this->particles as $particle) {
            $particle->draw();
        }
    }

    public function getParticleCount(): int
    {
        return count($this->particles);
    }
}

$factory = new ParticleTypeFactory();
$system = new ParticleSystem($factory);

$sprites = ['spark', 'smoke'];
$colors = ['red', 'gray'];

for ($i = 0; $i < 6; $i++) {
    $sprite = $sprites[$i % 2];
    $color = $colors[$i % 2];
    $system->emit((float) $i, (float) ($i * 2), 0.1, -0.2, $sprite, $color);
}

$system->drawAll();

$stats = $factory->getStats();

echo "Total particles emitted: {$system->getParticleCount()}\n";
echo "Particle types created: {$stats['created']}\n";
echo "Particle types reused: {$stats['reused']}\n";
```

### Expected Output

```text
Drawing red 'spark' particle at (0, 0) moving (0.1, -0.2)
Drawing gray 'smoke' particle at (1, 2) moving (0.1, -0.2)
Drawing red 'spark' particle at (2, 4) moving (0.1, -0.2)
Drawing gray 'smoke' particle at (3, 6) moving (0.1, -0.2)
Drawing red 'spark' particle at (4, 8) moving (0.1, -0.2)
Drawing gray 'smoke' particle at (5, 10) moving (0.1, -0.2)
Total particles emitted: 6
Particle types created: 2
Particle types reused: 4
```

Six particles were emitted, but only 2 `ParticleType` flyweights were ever constructed; the other 4 emissions reused an existing type instead of allocating a new one.

---

## Real-World Scenarios

### Scenario 1: Game Rendering

Games with forests, crowds, bullets, or particle effects use flyweights to share meshes, textures, and sprites across thousands of entities, storing only position, rotation, and velocity per instance.

### Scenario 2: Text Editors and Glyph Rendering

Rich text editors represent each character as an object during layout and formatting. Font, size, and style are shared flyweights; only the character value and its position are unique per instance.

### Scenario 3: Map and GIS Icon Rendering

Mapping applications place thousands of markers (restaurants, gas stations, pins) on a map. The icon bitmap and marker style are shared flyweights, while each marker's latitude and longitude are extrinsic.

### Scenario 4: Database Connection and String Interning Pools

String interning reuses a single instance for identical string values instead of duplicating them in memory. Similarly, some data layers pool immutable value objects (currency codes, country codes) so repeated values share one instance rather than being reconstructed.

---

## Pros & Cons

### Advantages

- Dramatically reduces memory usage when many similar objects are needed
- Centralizes creation of shared state through a factory, avoiding duplication
- Can improve performance by reducing object allocation and garbage collection
- Encourages a clean separation between shared (intrinsic) and unique (extrinsic) data
- Works well alongside other creational patterns like Factory Method

### Disadvantages

- Adds complexity by splitting state between the flyweight and the client
- Client code must manage and pass extrinsic state correctly
- Debugging is harder because shared objects are used in many different contexts
- Mutable flyweights are dangerous, since a change affects every user of that shared instance
- Not worth it when the number of objects is small or state is mostly unique

---

## Best Practices

1. Identify intrinsic (shareable) versus extrinsic (unique) state before designing the flyweight.
2. Make flyweights immutable so sharing them is always safe.
3. Always create flyweights through a factory, never directly with `new`.
4. Use a stable, unique key (e.g. concatenated attributes) to look up cached flyweights.
5. Keep extrinsic state entirely outside the flyweight and pass it as method arguments.
6. Avoid storing client-specific references inside a flyweight.
7. Measure actual memory or object-count savings before adopting the pattern.
8. Do not use Flyweight just because objects look similar; use it when volume makes duplication costly.

### Good Flyweight Design

```php
<?php

final class IconType
{
    public function __construct(
        private readonly string $bitmap,
        private readonly string $category
    ) {
    }

    public function draw(float $lat, float $lng): void
    {
        echo "Drawing {$this->category} icon at ({$lat}, {$lng}) using '{$this->bitmap}'\n";
    }
}

final class IconTypeFactory
{
    /** @var array<string, IconType> */
    private array $pool = [];

    public function get(string $bitmap, string $category): IconType
    {
        $key = "{$bitmap}_{$category}";

        return $this->pool[$key] ??= new IconType($bitmap, $category);
    }
}
```

The flyweight is `readonly`/immutable, holds only shared data, and is only ever obtained through the factory's cache. Latitude and longitude stay outside, passed in at draw time.

---

## Common Pitfalls

### Pitfall 1: Extrinsic State Leaking into the Flyweight

Storing per-instance data (like position) inside the flyweight defeats sharing, since every unique position forces a new object.

```php
class TreeType
{
    public function __construct(
        private string $name,
        private int $x,   // Wrong: x/y are extrinsic, not shared.
        private int $y
    ) {
    }
}
```

Keep `x` and `y` in the `Tree` (client-side object), not in `TreeType`.

### Pitfall 2: Thread-Safety of Shared Mutable Flyweights

If a flyweight is mutable and shared across threads or requests, one caller's change can silently affect every other caller using the same instance.

```php
class CharacterStyle
{
    public function setSize(int $size): void
    {
        $this->size = $size; // Mutates a shared object used by many characters!
    }
}
```

Prefer immutable flyweights, or clone before mutating.

### Pitfall 3: Over-Engineering When Object Counts Are Small

Introducing a factory, a pool, and a split of intrinsic/extrinsic state for a few dozen objects adds indirection without any real memory benefit.

If you are not creating thousands of similar objects, plain objects are simpler and easier to reason about.

### Pitfall 4: Confusing Flyweight with Object Pool

Flyweight shares immutable state across many logical objects at the same time. Object Pool reuses a single mutable object (like a database connection) one caller at a time, then returns it for the next caller.

```php
// Flyweight: many trees share one TreeType simultaneously.
// Object Pool: one connection is checked out, used, then checked back in.
```

Mixing these up leads to sharing mutable resources unsafely, or pooling immutable data unnecessarily.

---

## Variants

### Unshared Concrete Flyweight

Sometimes a flyweight-like object cannot be shared (its state cannot be fully separated into intrinsic/extrinsic parts), but it still implements the same interface so the client code stays uniform. It behaves like a flyweight in shape but not in sharing.

### Flyweight Combined with Factory Method

The `FlyweightFactory` is itself a Factory Method implementation: it encapsulates creation logic and decides whether to build a new instance or return a cached one, keeping that decision out of client code.

### Immutable vs Mutable Flyweights

Immutable flyweights are the safest and most common form, since shared state can never be corrupted by one user's changes. Mutable flyweights are rare and risky, requiring careful synchronization or copy-on-write strategies if truly needed.

---

## Practice Exercises

### Exercise 1: Forest Renderer

Build `TreeType`, `TreeTypeFactory`, and `Tree`. Plant 10,000 trees using only 3 distinct species and confirm only 3 `TreeType` objects are created.

### Exercise 2: Glyph Style Sharing

Extend the `CharacterStyle` example to support bold, italic, and underline flags as part of the style key. Verify identical styles are never duplicated.

### Exercise 3: Map Marker Pool

Create `IconType` and `IconTypeFactory` for a map app with categories like `restaurant`, `gas-station`, and `hospital`. Place 1,000 markers and print the count of distinct icon types created.

### Exercise 4: Mutable Flyweight Bug

Deliberately make a flyweight mutable with a public setter. Demonstrate how changing it through one client accidentally affects another client sharing the same instance. Then fix it by making the flyweight immutable.

### Exercise 5: Flyweight vs Object Pool

Implement a simple `DbConnectionPool` (Object Pool) and a `CountryCodeFactory` (Flyweight) side by side. Write down, in comments, why one manages exclusive checkout/checkin and the other manages concurrent shared access.

---

## Summary

The Flyweight Pattern reduces memory usage by sharing immutable, common state across a large number of logical objects instead of duplicating it in each one.

By splitting object data into intrinsic state (shared, stored in the flyweight) and extrinsic state (unique, supplied by the client), applications can represent millions of entities using only a handful of actual objects.

Use Flyweight when object volume is genuinely large and much of the per-object data is repeated; avoid it when object counts are small or state cannot be cleanly separated.
