# Singleton Pattern - Deep Dive

**Goal:** Ensure a class has only one instance and provide a global point of access to it.

---

## Table of Contents

1. [Problem Statement](#problem-statement)
2. [Pattern Concept](#pattern-concept)
3. [Structure & Components](#structure--components)
4. [Implementation](#implementation)
5. [Real-World Scenarios](#real-world-scenarios)
6. [Pros & Cons](#pros--cons)
7. [Best Practices](#best-practices)
8. [Common Pitfalls](#common-pitfalls)
9. [Variants](#variants)
10. [Real-World Examples](#real-world-examples)
11. [Practice Exercises](#practice-exercises)

---

## Problem Statement

### The Challenge

Consider scenarios where you need exactly one instance of a class:

```
✓ Database Connection - Only one connection pool
✓ Logger Service - Single logging instance
✓ Configuration Manager - One config instance
✓ Cache Manager - Single cache instance
✓ File System Access - One file handler
```

**Without Singleton:**
- Multiple instances created accidentally
- Wasted resources (memory, connections)
- Inconsistent state across instances
- Hard to coordinate between instances

### Real-World Example

```php
// Without Singleton
$logger1 = new Logger();
$logger2 = new Logger();
$logger3 = new Logger();

// Now you have 3 separate instances!
// Each maintains its own state
// Logs might be written to different files
// Memory wasted
```

---

## Pattern Concept

### What is Singleton?

The Singleton Pattern ensures that:

1. **Only one instance exists** - Class cannot be instantiated more than once
2. **Global access point** - Anyone can access the single instance
3. **Lazy initialization** - Instance created when first needed
4. **Controlled creation** - Class controls its own instantiation

### When to Use

✅ **USE when:**
- Exactly one instance required
- Centralized control needed
- Shared state across application
- Resource-intensive object creation

❌ **AVOID when:**
- Multiple instances needed
- Testing requires easy mocking
- Represents application state that varies
- Simpler solution exists

### Key Characteristics

| Aspect | Details |
|--------|---------|
| **Instances** | Exactly 1 |
| **Access** | Static `getInstance()` method |
| **Creation** | Lazy (on demand) |
| **Initialization** | Private constructor |
| **Thread Safety** | Important in multi-threaded environments |

---

## Structure & Components

### Pattern Diagram

```
┌─────────────────┐
│   Singleton     │
├─────────────────┤
│ - instance      │ (static, private)
├─────────────────┤
│ - __construct() │ (private)
│ - getInstance() │ (static, public)
│ - method()      │ (public)
└─────────────────┘
```

### Key Components

| Component | Purpose |
|-----------|---------|
| `instance` | Static variable holding single instance |
| `__construct()` | Private to prevent direct instantiation |
| `getInstance()` | Static method to get/create instance |
| `__clone()` | Private to prevent cloning |
| `__wakeup()` | Private to prevent unserializing |

---

## Implementation

### Basic Implementation

```php
<?php

/**
 * Basic Singleton
 * 
 * Simplest form of the Singleton pattern.
 */
class Logger
{
    // Static instance
    private static $instance = null;

    // Private constructor prevents direct instantiation
    private function __construct()
    {
        // Initialization code here
        echo "Logger initialized\n";
    }

    // Static method to get/create instance
    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    // Prevent cloning
    private function __clone()
    {
    }

    // Prevent unserializing
    private function __wakeup()
    {
    }

    // Regular methods
    public function log($message)
    {
        echo "[LOG] {$message}\n";
    }
}

// Usage
$logger1 = Logger::getInstance();
$logger2 = Logger::getInstance();

var_dump($logger1 === $logger2); // true - same instance!
$logger1->log("Application started");
$logger2->log("This is the same logger");

// Output:
// Logger initialized
// [LOG] Application started
// [LOG] This is the same logger
// bool(true)
?>
```

### Thread-Safe Implementation (Double-Checked Locking)

```php
<?php

/**
 * Thread-Safe Singleton with Double-Checked Locking
 * 
 * Better for multi-threaded environments
 */
class ThreadSafeDatabase
{
    private static $instance = null;
    private static $lock = null;

    private function __construct()
    {
        // Simulating expensive connection
        sleep(1);
        echo "Database connection established\n";
    }

    public static function getInstance()
    {
        // First check (without lock for performance)
        if (self::$instance === null) {
            // Lock before second check
            if (self::$lock === null) {
                self::$lock = true;
            }

            // Second check (with lock)
            if (self::$instance === null) {
                self::$instance = new self();
            }
        }

        return self::$instance;
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }

    public function query($sql)
    {
        echo "Executing: {$sql}\n";
    }
}

// Usage
$db1 = ThreadSafeDatabase::getInstance();
$db2 = ThreadSafeDatabase::getInstance();

var_dump($db1 === $db2); // true
$db1->query("SELECT * FROM users");
?>
```

### Lazy Initialization

```php
<?php

/**
 * Lazy-Initialized Singleton
 * 
 * Instance created only when first accessed
 */
class Configuration
{
    private static $instance = null;
    private $settings = [];

    private function __construct()
    {
        echo "Loading configuration...\n";
        $this->loadSettings();
    }

    private function loadSettings()
    {
        // Simulate loading from file
        $this->settings = [
            'app_name' => 'MyApp',
            'debug' => true,
            'db_host' => 'localhost',
        ];
    }

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function get($key)
    {
        return $this->settings[$key] ?? null;
    }

    public function set($key, $value)
    {
        $this->settings[$key] = $value;
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Configuration not loaded yet
echo "Before getInstance\n";

// Configuration loaded on first access
$config = Configuration::getInstance();
echo "App name: " . $config->get('app_name') . "\n";

// Second access uses same instance
$config2 = Configuration::getInstance();
var_dump($config === $config2); // true

// Output:
// Before getInstance
// Loading configuration...
// App name: MyApp
// bool(true)
?>
```

### Static Initialization

```php
<?php

/**
 * Eager Initialization Singleton
 * 
 * Instance created when class is loaded
 */
class EagerLogger
{
    private static $instance = null;

    // Static initialization block (PHP 5.3+)
    public static function __static()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
    }

    private function __construct()
    {
        echo "Logger eagerly initialized\n";
    }

    public static function getInstance()
    {
        return self::$instance;
    }

    public function log($message)
    {
        echo "[LOG] {$message}\n";
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Trigger static initialization
EagerLogger::__static();

// Get instance
$logger = EagerLogger::getInstance();
$logger->log("System started");
?>
```

---

## Real-World Scenarios

### Scenario 1: Database Connection

```php
<?php

/**
 * Database Singleton
 * 
 * Single database connection shared across application
 */
class Database
{
    private static $instance = null;
    private $connection = null;
    private $host = 'localhost';
    private $database = 'myapp';
    private $user = 'root';
    private $password = '';

    private function __construct()
    {
        echo "Connecting to database...\n";
        $this->connect();
    }

    private function connect()
    {
        try {
            $this->connection = new PDO(
                "mysql:host={$this->host};dbname={$this->database}",
                $this->user,
                $this->password
            );
            echo "Database connected\n";
        } catch (PDOException $e) {
            echo "Connection failed: " . $e->getMessage() . "\n";
        }
    }

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function query($sql, $params = [])
    {
        $stmt = $this->connection->prepare($sql);
        $stmt->execute($params);
        return $stmt;
    }

    public function close()
    {
        $this->connection = null;
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Usage
$db1 = Database::getInstance();
$db1->query("SELECT * FROM users");

$db2 = Database::getInstance();
// Same connection used
var_dump($db1 === $db2); // true
?>
```

### Scenario 2: Logger Service

```php
<?php

/**
 * Logger Singleton
 * 
 * Centralized logging across application
 */
class Logger
{
    private static $instance = null;
    private $logFile = 'app.log';
    private $level = 'INFO';

    private function __construct()
    {
        echo "Logger initialized\n";
    }

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function log($message, $level = 'INFO')
    {
        $timestamp = date('Y-m-d H:i:s');
        $logMessage = "[$timestamp] [$level] $message\n";

        // Write to file
        file_put_contents($this->logFile, $logMessage, FILE_APPEND);

        // Also echo to console
        echo $logMessage;
    }

    public function info($message)
    {
        $this->log($message, 'INFO');
    }

    public function error($message)
    {
        $this->log($message, 'ERROR');
    }

    public function warning($message)
    {
        $this->log($message, 'WARNING');
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Usage in different parts of application
class UserController
{
    public function create()
    {
        Logger::getInstance()->info("Creating new user");
    }
}

class EmailService
{
    public function send()
    {
        Logger::getInstance()->info("Sending email");
    }
}

$controller = new UserController();
$controller->create();

$emailService = new EmailService();
$emailService->send();
?>
```

### Scenario 3: Configuration Manager

```php
<?php

/**
 * Configuration Singleton
 * 
 * Centralized application configuration
 */
class Config
{
    private static $instance = null;
    private $settings = [];

    private function __construct()
    {
        $this->loadConfig();
    }

    private function loadConfig()
    {
        // Load from file or environment
        $this->settings = [
            'app' => [
                'name' => 'MyApplication',
                'debug' => true,
                'timezone' => 'UTC',
            ],
            'database' => [
                'host' => 'localhost',
                'port' => 3306,
                'name' => 'myapp',
            ],
            'cache' => [
                'driver' => 'redis',
                'ttl' => 3600,
            ],
        ];
    }

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function get($key, $default = null)
    {
        $keys = explode('.', $key);
        $value = $this->settings;

        foreach ($keys as $k) {
            if (!isset($value[$k])) {
                return $default;
            }
            $value = $value[$k];
        }

        return $value;
    }

    public function set($key, $value)
    {
        $keys = explode('.', $key);
        $current = &$this->settings;

        foreach ($keys as $k) {
            $current = &$current[$k];
        }

        $current = $value;
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Usage
$config = Config::getInstance();

echo $config->get('app.name') . "\n";          // MyApplication
echo $config->get('database.host') . "\n";    // localhost
echo $config->get('cache.driver') . "\n";     // redis

$config->set('app.debug', false);
echo $config->get('app.debug') ? 'true' : 'false'; // false
?>
```

---

## Pros & Cons

### Advantages ✅

| Pro | Explanation |
|-----|-------------|
| **Single Instance** | Ensures only one instance exists |
| **Global Access** | Easy to access from anywhere |
| **Lazy Loading** | Instance created only when needed |
| **Memory Efficient** | Single instance saves resources |
| **Controlled Access** | Class controls its instantiation |
| **State Consistency** | Single source of truth |

### Disadvantages ❌

| Con | Explanation |
|-----|-------------|
| **Hard to Test** | Difficult to mock in unit tests |
| **Hidden Dependencies** | Global state hard to track |
| **Violates SRP** | Manages both logic and instantiation |
| **Thread Issues** | Not naturally thread-safe |
| **Global State** | Can lead to coupling |
| **Difficult to Debug** | Hard to trace state changes |

### Trade-offs

```
BENEFIT: Single instance
COST: Hard to test and mock

BENEFIT: Global access
COST: Hidden dependencies

BENEFIT: Memory efficient
COST: Global state issues
```

---

## Best Practices

### 1. Make Constructor Private

```php
<?php
class Singleton
{
    private function __construct()
    {
        // Prevent direct instantiation
    }
}
?>
```

### 2. Prevent Cloning

```php
<?php
class Singleton
{
    private function __clone()
    {
        // Prevent cloning the instance
    }

    // PHP 8.1+
    final public function __clone()
    {
        throw new Error("Cannot clone singleton");
    }
}
?>
```

### 3. Prevent Serialization/Unserialization

```php
<?php
class Singleton
{
    private function __wakeup()
    {
        // Prevent unserializing creating new instance
    }

    public function __sleep()
    {
        throw new Exception("Cannot serialize singleton");
    }
}
?>
```

### 4. Use Type Hints

```php
<?php
class Service
{
    private Logger $logger;

    public function __construct()
    {
        $this->logger = Logger::getInstance();
    }
}
?>
```

### 5. Document Singleton Nature

```php
<?php
/**
 * Logger - Singleton
 * 
 * @method static Logger getInstance()
 */
class Logger
{
    // Implementation
}
?>
```

### 6. Provide Setter for Testing

```php
<?php
class Logger
{
    private static $instance = null;
    private static $testInstance = null;

    public static function getInstance()
    {
        if (self::$testInstance !== null) {
            return self::$testInstance;
        }

        if (self::$instance === null) {
            self::$instance = new self();
        }

        return self::$instance;
    }

    // For testing only
    public static function setTestInstance($instance)
    {
        self::$testInstance = $instance;
    }

    // Reset for testing
    public static function reset()
    {
        self::$instance = null;
        self::$testInstance = null;
    }
}
?>
```

---

## Common Pitfalls

### Pitfall 1: Not Thread-Safe

```php
<?php
// ❌ NOT thread-safe
class Singleton
{
    private static $instance = null;

    public static function getInstance()
    {
        if (self::$instance === null) {
            // Race condition here!
            self::$instance = new self();
        }
        return self::$instance;
    }
}

// ✅ Thread-safe version
class Singleton
{
    private static $instance = null;
    private static $lock = null;

    public static function getInstance()
    {
        if (self::$instance === null) {
            if (self::$lock === null) {
                self::$lock = true;
            }
            if (self::$instance === null) {
                self::$instance = new self();
            }
        }
        return self::$instance;
    }
}
?>
```

### Pitfall 2: Forgetting to Prevent Cloning

```php
<?php
// ❌ Allows cloning
class Singleton
{
    private static $instance = null;

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }
}

$obj1 = Singleton::getInstance();
$obj2 = clone $obj1; // Creates new instance!

// ✅ Prevents cloning
class Singleton
{
    private function __clone()
    {
    }

    final public function __clone()
    {
        throw new Error("Cannot clone singleton");
    }
}
?>
```

### Pitfall 3: Hard to Test

```php
<?php
// ❌ Hard to test
class UserService
{
    public function create($data)
    {
        Logger::getInstance()->log("User created");
    }
}

// ✅ Easier to test - use dependency injection
class UserService
{
    private $logger;

    public function __construct(LoggerInterface $logger)
    {
        $this->logger = $logger;
    }

    public function create($data)
    {
        $this->logger->log("User created");
    }
}
?>
```

### Pitfall 4: Serialize/Unserialize Creating New Instance

```php
<?php
// ❌ Serialization creates new instance
class Singleton
{
    private static $instance = null;

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }
}

$obj1 = Singleton::getInstance();
$serialized = serialize($obj1);
$obj2 = unserialize($serialized); // New instance!

// ✅ Prevent serialization
class Singleton
{
    private function __wakeup()
    {
        throw new Exception("Cannot unserialize singleton");
    }

    public function __sleep()
    {
        throw new Exception("Cannot serialize singleton");
    }
}
?>
```

---

## Variants

### Variant 1: Registry Pattern (Multiple Singletons)

```php
<?php

/**
 * Registry Pattern
 * 
 * Manage multiple singleton instances
 */
class ServiceRegistry
{
    private static $instance = null;
    private $services = [];

    private function __construct()
    {
    }

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function register($name, $service)
    {
        $this->services[$name] = $service;
    }

    public function get($name)
    {
        return $this->services[$name] ?? null;
    }

    public function has($name)
    {
        return isset($this->services[$name]);
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Usage
$registry = ServiceRegistry::getInstance();
$registry->register('logger', new Logger());
$registry->register('database', new Database());

$logger = $registry->get('logger');
?>
```

### Variant 2: Multiton Pattern

```php
<?php

/**
 * Multiton Pattern
 * 
 * Allows limited number of instances per key
 */
class Multiton
{
    private static $instances = [];

    private function __construct()
    {
    }

    public static function getInstance($key)
    {
        if (!isset(self::$instances[$key])) {
            self::$instances[$key] = new self();
        }
        return self::$instances[$key];
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Usage
$instance1 = Multiton::getInstance('db_primary');
$instance2 = Multiton::getInstance('db_replica');
$instance3 = Multiton::getInstance('db_primary');

var_dump($instance1 === $instance3); // true
var_dump($instance1 === $instance2); // false
?>
```

### Variant 3: Object Pool Singleton

```php
<?php

/**
 * Object Pool
 * 
 * Singleton managing pool of reusable objects
 */
class ConnectionPool
{
    private static $instance = null;
    private $availableConnections = [];
    private $usedConnections = [];
    private $maxConnections = 5;

    private function __construct()
    {
        for ($i = 0; $i < $this->maxConnections; $i++) {
            $this->availableConnections[] = new Connection();
        }
    }

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function getConnection()
    {
        if (count($this->availableConnections) > 0) {
            $conn = array_pop($this->availableConnections);
            $this->usedConnections[] = $conn;
            return $conn;
        }
        throw new Exception("No connections available");
    }

    public function releaseConnection(Connection $conn)
    {
        $key = array_search($conn, $this->usedConnections);
        if ($key !== false) {
            unset($this->usedConnections[$key]);
            $this->availableConnections[] = $conn;
        }
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

class Connection
{
    // Connection implementation
}

// Usage
$pool = ConnectionPool::getInstance();
$conn = $pool->getConnection();
// Use connection
$pool->releaseConnection($conn);
?>
```

---

## Real-World Examples

### Laravel Framework

**Laravel Service Container** uses Singleton pattern:

```php
<?php
// Laravel Singleton
app()->singleton('logger', function () {
    return new Logger();
});

// Access from anywhere
$logger = app('logger');
?>
```

### WordPress

**WordPress global variables** as Singletons:

```php
<?php
global $wpdb;  // Database singleton
global $wp;    // WordPress singleton

$wpdb->query("SELECT ...");
?>
```

### Monolog Logger

**Monolog uses Singleton pattern:**

```php
<?php
use Monolog\Logger;
use Monolog\Handlers\StreamHandler;

class AppLogger
{
    private static $instance = null;

    public static function getInstance()
    {
        if (self::$instance === null) {
            $logger = new Logger('app');
            $logger->pushHandler(new StreamHandler('app.log'));
            self::$instance = $logger;
        }
        return self::$instance;
    }
}

$logger = AppLogger::getInstance();
?>
```

### PDO Connection

**Single database connection:**

```php
<?php
class DatabaseConnection
{
    private static $instance = null;

    public static function getInstance()
    {
        if (self::$instance === null) {
            self::$instance = new PDO('mysql:host=localhost;dbname=myapp');
        }
        return self::$instance;
    }
}

$db = DatabaseConnection::getInstance();
?>
```

---

## Practice Exercises

### Exercise 1: Basic Logger

Create a Logger Singleton with:
- `log($message, $level)` method
- Support for INFO, WARNING, ERROR levels
- Write logs to file
- Timestamp for each log

**Starter Code:**

```php
<?php
class Logger
{
    private static $instance = null;
    private $logFile = 'application.log';

    private function __construct()
    {
        // Initialize logger
    }

    public static function getInstance()
    {
        // Implement singleton logic
    }

    public function log($message, $level = 'INFO')
    {
        // Implement logging
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Test your implementation
$logger = Logger::getInstance();
$logger->log('Application started', 'INFO');
$logger->log('Error occurred', 'ERROR');
?>
```

### Exercise 2: Database Connection Pool

Create a Database Singleton that:
- Manages single database connection
- Provides query execution
- Handles connection pooling
- Prevents multiple connections

**Starter Code:**

```php
<?php
class Database
{
    private static $instance = null;
    private $connection = null;

    private function __construct()
    {
        // Connect to database
    }

    public static function getInstance()
    {
        // Implement singleton logic
    }

    public function query($sql, $params = [])
    {
        // Execute query
    }

    public function prepare($sql)
    {
        // Prepare statement
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Test your implementation
$db = Database::getInstance();
$result = $db->query("SELECT * FROM users WHERE id = ?", [1]);
?>
```

### Exercise 3: Configuration Manager

Create a Configuration Singleton that:
- Loads settings from array or file
- Supports nested key access (e.g., 'database.host')
- Allows get/set operations
- Cache configuration in memory

**Starter Code:**

```php
<?php
class Config
{
    private static $instance = null;
    private $settings = [];

    private function __construct()
    {
        // Load configuration
    }

    public static function getInstance()
    {
        // Implement singleton logic
    }

    public function get($key, $default = null)
    {
        // Get setting with dot notation
    }

    public function set($key, $value)
    {
        // Set setting with dot notation
    }

    public function all()
    {
        // Return all settings
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Test your implementation
$config = Config::getInstance();
echo $config->get('database.host', 'localhost');
?>
```

### Exercise 4: Testable Singleton

Create a Singleton that's easily testable:
- Provide way to mock instance for testing
- Allow reset for each test
- Maintain singleton behavior in production

**Starter Code:**

```php
<?php
class TestableService
{
    private static $instance = null;
    private static $mockInstance = null;

    private function __construct()
    {
    }

    public static function getInstance()
    {
        // Return mock if set, otherwise return singleton
    }

    public function doSomething()
    {
        return "real implementation";
    }

    // Testing support
    public static function setMock($mock)
    {
        // Set mock instance
    }

    public static function resetMocks()
    {
        // Reset mocks
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Test usage
class MockService extends TestableService
{
    public function doSomething()
    {
        return "mocked implementation";
    }
}

// In your tests
TestableService::setMock(new MockService());
$service = TestableService::getInstance();
echo $service->doSomething(); // "mocked implementation"
?>
```

### Exercise 5: Multi-Instance Singleton

Create a system that manages multiple Singleton instances:
- Registry for different singletons
- Easy registration and retrieval
- Type-safe access

**Starter Code:**

```php
<?php
class ServiceContainer
{
    private static $instance = null;
    private $services = [];

    private function __construct()
    {
    }

    public static function getInstance()
    {
        // Implement singleton logic
    }

    public function set($key, $callable)
    {
        // Register service factory
    }

    public function get($key)
    {
        // Get or create service instance
    }

    private function __clone()
    {
    }

    private function __wakeup()
    {
    }
}

// Test your implementation
$container = ServiceContainer::getInstance();
$container->set('logger', function () {
    return new Logger();
});

$logger = $container->get('logger');
?>
```

---

## Summary

### Key Takeaways

✅ **Singleton ensures:**
- Exactly one instance of a class
- Global access point
- Controlled instantiation
- Memory efficiency

⚠️ **Singleton challenges:**
- Hard to test
- Hidden dependencies
- Can lead to global state issues
- Not thread-safe by default

🎯 **Use when:**
- Exactly one instance needed
- Shared state across application
- Resource-intensive object
- Centralized access required

### Next Steps

1. Implement basic Logger Singleton
2. Create testable Singleton variant
3. Build Configuration Manager
4. Explore Registry Pattern
5. Compare with Dependency Injection

---

## Related Patterns

- **Factory Pattern** - Create objects (vs Singleton controlling instance)
- **Observer Pattern** - Notification system with single listeners
- **Facade Pattern** - Single interface to complex subsystem
- **Repository Pattern** - Single data access point
- **Dependency Injection** - Modern alternative to Singleton

---

## Resources

### Reading
- Design Patterns: Elements of Reusable Object-Oriented Software (Gang of Four)
- Refactoring.guru - Singleton Pattern
- PHP Design Patterns by Aaron Saray

### Online
- refactoring.guru/design-patterns/singleton
- PHP Manual - Static Properties
- DesignPatternsPHP - Creational Patterns

### Practice
- Build a Logger Singleton
- Create Configuration Manager
- Implement Database Connection Pool
- Compare with Dependency Injection

---

## Conclusion

The Singleton Pattern is a fundamental design pattern useful for controlling instantiation and providing global access. However, it should be used carefully as it can introduce global state and make testing difficult.

Modern PHP often prefers **Dependency Injection** over Singletons for better testability and loose coupling. Consider the trade-offs before applying Singleton pattern.

Happy learning! 🚀
