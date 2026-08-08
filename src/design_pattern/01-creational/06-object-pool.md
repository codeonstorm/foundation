# Object Pool Pattern - Deep Dive

**Goal:** Reuse expensive objects instead of creating and destroying them repeatedly.

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

Some objects are expensive to create, initialize, or connect.

Examples:

- Database connections
- Network clients
- Worker objects
- Parser instances with loaded grammar
- Image processors with large buffers
- Game objects such as bullets, particles, or enemies

Without Object Pool, code may repeatedly create and destroy expensive objects.

### Real Example

```php
for ($i = 0; $i < 100; $i++) {
    $connection = new DatabaseConnection();
    $connection->connect();
    $connection->query('SELECT * FROM users');
    $connection->disconnect();
}
```

This creates a new connection every time. If connection setup is expensive, the application wastes time and resources.

Object Pool solves this by keeping reusable objects ready and lending them to clients when needed.

---

## Pattern Concept

### What is Object Pool?

The Object Pool Pattern manages a set of reusable objects.

Instead of creating a new object every time, client code asks the pool for an available object. When finished, the client returns the object to the pool so it can be reused later.

### Why It Matters

- Reduces expensive object creation
- Controls the number of active objects
- Improves performance for reusable resources
- Helps manage limited resources
- Makes acquire and release behavior explicit

### When to Use

Use Object Pool when:
- Object creation is expensive
- Objects can be safely reused
- You need to limit concurrent resource usage
- Objects require setup and cleanup
- You are working with connections, workers, buffers, or game entities

Avoid Object Pool when:
- Objects are cheap to create
- Objects hold user-specific state that is hard to reset
- Reuse creates correctness risks
- Garbage collection is enough
- The pool would add complexity without measurable benefit

---

## Structure & Components

### Pattern Diagram

```text
Client -> ObjectPool
            |
            | acquire()
            v
      ReusableObject
            |
            | release()
            v
        ObjectPool
```

### Key Components

| Component | Role |
|-----------|------|
| `ObjectPool` | Manages available and borrowed objects |
| `ReusableObject` | Object that can be reset and reused |
| `Client` | Acquires an object, uses it, and releases it |
| `Factory` | Optional object used by the pool to create new objects |
| `Reset/Cleanup` | Prepares an object before it returns to the pool |

### Typical Methods

- `acquire()` - borrow an object from the pool
- `release()` - return an object to the pool
- `create()` - create a new object when the pool has capacity
- `reset()` - clear object state before reuse
- `size()` - inspect pool capacity or availability

---

## PHP Implementation

### Basic Object Pool Example

```php
<?php

interface Resettable
{
    public function reset(): void;
}

class Worker implements Resettable
{
    private array $tasks = [];

    public function run(string $task): void
    {
        $this->tasks[] = $task;
        echo "Running task: {$task}\n";
    }

    public function reset(): void
    {
        $this->tasks = [];
    }
}

class WorkerPool
{
    private array $available = [];
    private array $borrowed = [];

    public function __construct(private int $maxSize)
    {
    }

    public function acquire(): Worker
    {
        if ($this->available !== []) {
            $worker = array_pop($this->available);
        } elseif (count($this->borrowed) < $this->maxSize) {
            $worker = new Worker();
        } else {
            throw new RuntimeException('No workers available.');
        }

        $this->borrowed[spl_object_id($worker)] = $worker;

        return $worker;
    }

    public function release(Worker $worker): void
    {
        $id = spl_object_id($worker);

        if (!isset($this->borrowed[$id])) {
            throw new InvalidArgumentException('This worker does not belong to the pool.');
        }

        unset($this->borrowed[$id]);

        $worker->reset();
        $this->available[] = $worker;
    }
}

$pool = new WorkerPool(maxSize: 2);

$worker = $pool->acquire();
$worker->run('send-email');
$pool->release($worker);

$sameWorker = $pool->acquire();
$sameWorker->run('generate-report');
$pool->release($sameWorker);
```

The pool controls how many workers can exist and resets each worker before reuse.

### Proper Example: Database Connection Pool

This example simulates database connections. In typical PHP-FPM applications, requests are short-lived, so connection pools are less common than in long-running processes. They are more relevant for workers, daemons, Swoole, RoadRunner, ReactPHP, or CLI services.

```php
<?php

class DatabaseConnection
{
    private bool $connected = false;
    private bool $inTransaction = false;

    public function connect(): void
    {
        if (!$this->connected) {
            $this->connected = true;
            echo "Connection opened\n";
        }
    }

    public function query(string $sql): void
    {
        if (!$this->connected) {
            throw new RuntimeException('Connection is not open.');
        }

        echo "Execute query: {$sql}\n";
    }

    public function beginTransaction(): void
    {
        $this->inTransaction = true;
        echo "Transaction started\n";
    }

    public function rollbackIfNeeded(): void
    {
        if ($this->inTransaction) {
            echo "Rollback unfinished transaction\n";
            $this->inTransaction = false;
        }
    }

    public function reset(): void
    {
        $this->rollbackIfNeeded();
    }

    public function close(): void
    {
        $this->connected = false;
        echo "Connection closed\n";
    }
}

class DatabaseConnectionPool
{
    private array $available = [];
    private array $borrowed = [];

    public function __construct(
        private int $maxConnections
    ) {
    }

    public function acquire(): DatabaseConnection
    {
        if ($this->available !== []) {
            $connection = array_pop($this->available);
        } elseif ($this->totalConnections() < $this->maxConnections) {
            $connection = new DatabaseConnection();
            $connection->connect();
        } else {
            throw new RuntimeException('Connection pool exhausted.');
        }

        $this->borrowed[spl_object_id($connection)] = $connection;

        return $connection;
    }

    public function release(DatabaseConnection $connection): void
    {
        $id = spl_object_id($connection);

        if (!isset($this->borrowed[$id])) {
            throw new InvalidArgumentException('Connection was not borrowed from this pool.');
        }

        unset($this->borrowed[$id]);

        $connection->reset();
        $this->available[] = $connection;
    }

    public function closeAll(): void
    {
        foreach ($this->available as $connection) {
            $connection->close();
        }

        foreach ($this->borrowed as $connection) {
            $connection->close();
        }

        $this->available = [];
        $this->borrowed = [];
    }

    private function totalConnections(): int
    {
        return count($this->available) + count($this->borrowed);
    }
}

$pool = new DatabaseConnectionPool(maxConnections: 2);

$connection = $pool->acquire();
$connection->query('SELECT * FROM users');
$pool->release($connection);

$pool->closeAll();
```

The pool limits total connections and makes sure unfinished transaction state is cleaned before reuse.

### Game Object Pool Example

Object Pool is common in games, where creating many short-lived objects can cause performance issues.

```php
<?php

class Bullet
{
    private bool $active = false;
    private int $x = 0;
    private int $y = 0;

    public function fire(int $x, int $y): void
    {
        $this->active = true;
        $this->x = $x;
        $this->y = $y;

        echo "Bullet fired from {$x}, {$y}\n";
    }

    public function reset(): void
    {
        $this->active = false;
        $this->x = 0;
        $this->y = 0;
    }
}

class BulletPool
{
    private array $available = [];
    private array $active = [];

    public function __construct(int $size)
    {
        for ($i = 0; $i < $size; $i++) {
            $this->available[] = new Bullet();
        }
    }

    public function acquire(): Bullet
    {
        if ($this->available === []) {
            throw new RuntimeException('No bullets available.');
        }

        $bullet = array_pop($this->available);
        $this->active[spl_object_id($bullet)] = $bullet;

        return $bullet;
    }

    public function release(Bullet $bullet): void
    {
        $id = spl_object_id($bullet);

        if (!isset($this->active[$id])) {
            return;
        }

        unset($this->active[$id]);

        $bullet->reset();
        $this->available[] = $bullet;
    }
}

$bullets = new BulletPool(size: 3);

$bullet = $bullets->acquire();
$bullet->fire(10, 20);
$bullets->release($bullet);
```

Instead of creating a new `Bullet` every time, the game reuses inactive bullets from the pool.

### Safe Release with `try` and `finally`

Always release borrowed objects, even when an exception happens.

```php
<?php

$connection = $pool->acquire();

try {
    $connection->query('SELECT * FROM orders');
} finally {
    $pool->release($connection);
}
```

The `finally` block prevents leaked borrowed objects.

---

## Real-World Scenarios

### Scenario 1: Database Connections

Long-running PHP processes can reuse database connections instead of opening a new connection for every operation.

### Scenario 2: HTTP or API Clients

Reusable clients may hold connection state, authentication, retry configuration, or prepared handlers.

### Scenario 3: Worker Objects

Queue workers, parser workers, or report workers can be reused after their temporary state is reset.

### Scenario 4: Game Development

Bullets, particles, enemies, and visual effects are often pooled to reduce memory churn.

### Scenario 5: Buffers and Parsers

Large buffers, XML parsers, image processors, or tokenizer objects can be expensive to allocate repeatedly.

---

## Pros & Cons

### Advantages

- Reduces expensive object creation
- Controls resource usage with a maximum pool size
- Improves performance in long-running processes
- Makes acquire and release lifecycle explicit
- Can reduce memory churn
- Useful for limited external resources

### Disadvantages

- Adds lifecycle complexity
- Objects must be carefully reset before reuse
- Borrowed objects can leak if not released
- Pool exhaustion must be handled
- Not useful for cheap objects
- Can hide stale state bugs
- Less useful in normal short-lived PHP requests

---

## Best Practices

1. Pool only objects that are expensive and safely reusable.
2. Always reset objects before returning them to the available pool.
3. Use `try` and `finally` to guarantee release.
4. Track borrowed objects so unknown objects cannot be returned.
5. Set a clear maximum pool size.
6. Decide what happens when the pool is exhausted.
7. Close external resources when the pool shuts down.
8. Avoid pooling objects with hard-to-clean user-specific state.
9. Measure performance before adding a pool.

### Good Object Pool Design

```php
<?php

$resource = $pool->acquire();

try {
    $resource->use();
} finally {
    $pool->release($resource);
}
```

The lifecycle is obvious: acquire, use, release.

---

## Common Pitfalls

### Pitfall 1: Forgetting to Release

If borrowed objects are not returned, the pool eventually runs out.

Use `finally` to release resources even when exceptions occur.

### Pitfall 2: Not Resetting State

If a reused object still contains previous user data, one request can affect another.

### Pitfall 3: Pooling Cheap Objects

Pooling simple value objects or small arrays often makes performance worse and code harder to read.

### Pitfall 4: No Exhaustion Strategy

When the pool reaches max size, should it throw, wait, create temporarily, or retry? Decide clearly.

### Pitfall 5: Treating Pool as a Global Singleton

A global pool can make tests and resource ownership harder. Prefer explicit dependencies.

### Pitfall 6: Ignoring PHP Runtime Model

Classic PHP-FPM requests are short-lived, so many pools disappear at the end of each request. Object Pool is more useful in long-running PHP processes.

---

## Variants

### Fixed-Size Pool

Create all objects upfront and never exceed the configured pool size.

### Lazy Pool

Create objects only when needed, up to a maximum size.

### Blocking Pool

Wait for an object to be returned instead of throwing immediately when the pool is exhausted.

### Expiring Pool

Close and remove objects after they are idle for too long.

### Validating Pool

Check whether an object is still healthy before lending it to a client.

### Generic Pool

Use a factory callback and reset callback so one pool class can manage different resource types.

```php
$pool = new ObjectPool(
    create: fn () => new Worker(),
    reset: fn (Worker $worker) => $worker->reset()
);
```

---

## Practice Exercises

### Exercise 1: Worker Pool

Create a `WorkerPool` with maximum size 3. Borrow workers, run tasks, and return them to the pool.

### Exercise 2: Connection Pool

Create a simulated database connection pool that opens connections lazily and closes all connections on shutdown.

### Exercise 3: Pool Exhaustion

Try to borrow more objects than the pool allows. Implement a clear exception message.

### Exercise 4: Reset Bug

Create a reusable object with temporary state. Show the bug when reset is missing, then fix it.

### Exercise 5: Generic Object Pool

Build a generic pool that accepts a factory callback and reset callback.

---

## Summary

The Object Pool Pattern reuses expensive objects instead of creating them repeatedly.

It is useful for resources such as connections, workers, buffers, parsers, and game objects, especially in long-running processes.

Use Object Pool only when reuse is safe and worthwhile. Always reset returned objects, handle exhaustion, and release borrowed objects reliably.
