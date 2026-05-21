# Proxy Pattern - Deep Dive

**Goal:** Provide a placeholder object that controls access to another object while keeping the same interface.

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

Sometimes an object is expensive, sensitive, remote, or needs extra behavior before it can be used.

Examples:

- Loading a large image from disk
- Connecting to a remote API
- Reading from a database
- Checking user permission before accessing a service
- Caching expensive method results
- Logging calls to another object

Without Proxy, client code often becomes responsible for these extra concerns.

### Real Example

```php
class Image
{
    public function __construct(private string $filename)
    {
        $this->loadFromDisk();
    }

    private function loadFromDisk(): void
    {
        echo "Loading {$this->filename} from disk\n";
    }

    public function display(): void
    {
        echo "Displaying {$this->filename}\n";
    }
}

$image = new Image('large-photo.jpg');

// The file was loaded even if we never display it.
```

The problem: the expensive object is created immediately, even if the client may not use it.

Proxy solves this by placing a lightweight object in front of the real object. The proxy delays, controls, or decorates access to the real object.

---

## Pattern Concept

### What is Proxy?

The Proxy Pattern creates an object with the same interface as the real object. The client talks to the proxy, and the proxy decides when and how to talk to the real object.

The client does not need to know whether it is using the real object or the proxy.

### Why It Matters

- Adds control without changing client code
- Delays expensive object creation
- Protects sensitive operations
- Caches repeated calls
- Logs or monitors access
- Hides remote communication details

### When to Use

Use Proxy when:
- Creating the real object is expensive
- You want lazy loading
- You need access control
- You want caching around an expensive operation
- The real object is remote and needs a local representative
- You want to add logging, metrics, or rate limiting around another object

Avoid Proxy when:
- Direct access is simple and safe
- The proxy adds no real control or value
- Extra indirection makes the code harder to understand
- You actually need to add new behavior dynamically, where Decorator may fit better

---

## Structure & Components

### Pattern Diagram

```text
Client -> Subject Interface
              ^
              |
      +-------+-------+
      |               |
  RealSubject        Proxy
                      |
                      v
                 RealSubject
```

### Key Components

| Component | Role |
|-----------|------|
| `Subject` | Common interface used by both proxy and real object |
| `RealSubject` | Actual object that performs the real work |
| `Proxy` | Controls access to the real object |
| `Client` | Uses the subject interface without caring which object is behind it |

### Typical Methods

- `request()` - method defined by the subject interface
- `checkAccess()` - optional proxy method for authorization
- `load()` or `getRealSubject()` - optional proxy method for lazy creation
- `logAccess()` - optional proxy method for logging

---

## PHP Implementation

### Proper Example: Virtual Proxy for Lazy Loading

A virtual proxy delays creating an expensive object until the object is actually needed.

```php
<?php

interface ImageInterface
{
    public function display(): void;
}

class RealImage implements ImageInterface
{
    public function __construct(private string $filename)
    {
        $this->loadFromDisk();
    }

    private function loadFromDisk(): void
    {
        echo "Loading {$this->filename} from disk\n";
    }

    public function display(): void
    {
        echo "Displaying {$this->filename}\n";
    }
}

class ImageProxy implements ImageInterface
{
    private ?RealImage $realImage = null;

    public function __construct(private string $filename)
    {
    }

    public function display(): void
    {
        if ($this->realImage === null) {
            $this->realImage = new RealImage($this->filename);
        }

        $this->realImage->display();
    }
}

function renderGallery(array $images): void
{
    echo "Gallery created\n";

    foreach ($images as $image) {
        echo "Image placeholder ready\n";
    }
}

$images = [
    new ImageProxy('large-photo-1.jpg'),
    new ImageProxy('large-photo-2.jpg'),
    new ImageProxy('large-photo-3.jpg'),
];

renderGallery($images);

echo "User opens first image\n";
$images[0]->display();

echo "User opens first image again\n";
$images[0]->display();
```

### Expected Output

```text
Gallery created
Image placeholder ready
Image placeholder ready
Image placeholder ready
User opens first image
Loading large-photo-1.jpg from disk
Displaying large-photo-1.jpg
User opens first image again
Displaying large-photo-1.jpg
```

Notice that only the first image is loaded, and it is loaded only once.

### Protection Proxy for Access Control

A protection proxy checks permission before letting the client use the real object.

```php
<?php

interface ReportService
{
    public function viewFinancialReport(): string;
}

class RealReportService implements ReportService
{
    public function viewFinancialReport(): string
    {
        return 'Financial report: revenue, expenses, and profit.';
    }
}

class User
{
    public function __construct(
        public string $name,
        public string $role
    ) {
    }
}

class ReportServiceProxy implements ReportService
{
    public function __construct(
        private RealReportService $reportService,
        private User $user
    ) {
    }

    public function viewFinancialReport(): string
    {
        if ($this->user->role !== 'admin') {
            return 'Access denied.';
        }

        return $this->reportService->viewFinancialReport();
    }
}

$admin = new User('Alice', 'admin');
$guest = new User('Bob', 'guest');

$adminProxy = new ReportServiceProxy(new RealReportService(), $admin);
$guestProxy = new ReportServiceProxy(new RealReportService(), $guest);

echo $adminProxy->viewFinancialReport() . "\n";
echo $guestProxy->viewFinancialReport() . "\n";
```

### Caching Proxy for Expensive Calls

A caching proxy stores the result of expensive calls and returns cached data on repeated requests.

```php
<?php

interface ExchangeRateProvider
{
    public function getRate(string $from, string $to): float;
}

class RealExchangeRateProvider implements ExchangeRateProvider
{
    public function getRate(string $from, string $to): float
    {
        echo "Calling external exchange-rate API\n";

        // Simulated API result.
        return 83.25;
    }
}

class CachedExchangeRateProxy implements ExchangeRateProvider
{
    private array $cache = [];

    public function __construct(
        private ExchangeRateProvider $provider
    ) {
    }

    public function getRate(string $from, string $to): float
    {
        $cacheKey = "{$from}_{$to}";

        if (!array_key_exists($cacheKey, $this->cache)) {
            $this->cache[$cacheKey] = $this->provider->getRate($from, $to);
        }

        return $this->cache[$cacheKey];
    }
}

$rates = new CachedExchangeRateProxy(new RealExchangeRateProvider());

echo $rates->getRate('USD', 'INR') . "\n";
echo $rates->getRate('USD', 'INR') . "\n";
echo $rates->getRate('USD', 'INR') . "\n";
```

The external API is called only once for `USD_INR`.

### Logging Proxy

A logging proxy records calls before or after delegating to the real object.

```php
<?php

interface PaymentGateway
{
    public function charge(float $amount): bool;
}

class StripeGateway implements PaymentGateway
{
    public function charge(float $amount): bool
    {
        echo "Charging {$amount} using Stripe\n";
        return true;
    }
}

class LoggingPaymentProxy implements PaymentGateway
{
    public function __construct(
        private PaymentGateway $gateway
    ) {
    }

    public function charge(float $amount): bool
    {
        echo "Log: payment started for {$amount}\n";

        $result = $this->gateway->charge($amount);

        echo "Log: payment " . ($result ? 'succeeded' : 'failed') . "\n";

        return $result;
    }
}

$gateway = new LoggingPaymentProxy(new StripeGateway());
$gateway->charge(99.99);
```

---

## Real-World Scenarios

### Scenario 1: Lazy Loading

ORMs often use proxies to lazy-load related objects. For example, an `Order` may hold a proxy for `Customer`, and the real customer data is fetched only when needed.

### Scenario 2: Access Control

Admin panels, billing screens, reports, and private files can be protected through a proxy that checks permissions before delegating.

### Scenario 3: API Clients

A proxy can add caching, retries, rate limiting, logging, or timeout handling around a real API client.

### Scenario 4: File Storage

A proxy can represent a file stored in S3, local disk, or remote storage. The file is downloaded only when the client requests its contents.

### Scenario 5: Framework Services

Dependency injection containers can create lazy service proxies so expensive services are not initialized until first use.

---

## Pros & Cons

### Advantages

- Controls access to another object
- Supports lazy loading
- Adds caching without changing the real object
- Adds authorization checks in one place
- Keeps client code dependent on an interface
- Can add logging, monitoring, retries, or rate limiting
- Useful for remote objects and expensive resources

### Disadvantages

- Adds another layer of indirection
- Can hide performance costs if the proxy suddenly loads real data
- May make debugging harder because calls pass through extra objects
- Proxy and real object must keep the same interface
- Can become too powerful if it takes on many responsibilities

---

## Best Practices

1. Make the proxy implement the same interface as the real object.
2. Keep the proxy focused on one concern, such as lazy loading, caching, or authorization.
3. Do not put core business logic inside the proxy.
4. Use dependency injection so the proxy can wrap different implementations.
5. Make lazy-loading behavior clear in names or documentation.
6. Add tests for both allowed and denied access in protection proxies.
7. Add cache invalidation rules when using caching proxies.
8. Avoid surprising side effects in simple getter methods.

### Good Proxy Design

```php
<?php

class CachedExchangeRateProxy implements ExchangeRateProvider
{
    private array $cache = [];

    public function __construct(
        private ExchangeRateProvider $provider
    ) {
    }

    public function getRate(string $from, string $to): float
    {
        $key = "{$from}_{$to}";

        if (!isset($this->cache[$key])) {
            $this->cache[$key] = $this->provider->getRate($from, $to);
        }

        return $this->cache[$key];
    }
}
```

This proxy does one thing: it caches exchange-rate results while preserving the same interface.

---

## Common Pitfalls

### Pitfall 1: Proxy Interface Does Not Match the Real Object

If the proxy exposes different methods than the real subject, clients can no longer swap between them easily.

```php
interface FileReader
{
    public function read(): string;
}
```

Both `RealFileReader` and `FileReaderProxy` should implement `FileReader`.

### Pitfall 2: Too Much Logic in the Proxy

A proxy should control access, not become the main business service.

If your proxy validates orders, charges cards, sends emails, and writes reports, it is doing too much.

### Pitfall 3: Cache Never Expires

Caching proxies need a strategy for stale data.

Possible strategies:
- Time-based expiration
- Manual cache clear
- Cache by version
- Cache invalidation after writes

### Pitfall 4: Hidden Remote Calls

Remote proxies can make a local method call look cheap while it actually performs network I/O.

Name and document remote proxies clearly so developers understand the cost.

### Pitfall 5: Confusing Proxy with Decorator

Proxy controls access to an object. Decorator adds new behavior while preserving the interface.

They can look similar in code, but their intent is different.

---

## Variants

### Virtual Proxy

Creates the real object only when needed.

Example: lazy-loading images, large files, or expensive services.

### Protection Proxy

Checks permissions before allowing access.

Example: admin-only reports or private file downloads.

### Caching Proxy

Stores expensive results and returns cached data for repeated calls.

Example: exchange rates, API responses, database lookups.

### Remote Proxy

Represents an object that lives somewhere else.

Example: a PHP class that wraps an HTTP service or RPC endpoint.

### Smart Proxy

Adds extra behavior around the real object.

Example: logging, metrics, reference counting, retries, or circuit breaker logic.

---

## Practice Exercises

### Exercise 1: Lazy File Reader

Create a `FileReaderInterface` with `read(): string`.

Implement:
- `RealFileReader`
- `LazyFileReaderProxy`

The real file should be read only when `read()` is called for the first time.

### Exercise 2: Admin Report Protection

Create a `ReportService` and a `ReportServiceProxy`.

Only users with the role `admin` should be allowed to view the report.

### Exercise 3: Cached Weather API

Create a `WeatherApiClient` and `CachedWeatherApiProxy`.

Cache weather results by city name.

### Exercise 4: Logging Payment Gateway

Create a `PaymentGateway` interface and wrap a real payment gateway with a logging proxy.

Log before and after each charge.

### Exercise 5: Compare Proxy and Decorator

Build two wrappers around the same `Notifier`:
- A proxy that checks whether notifications are allowed
- A decorator that adds SMS delivery alongside email delivery

Write down how their intent differs.

---

## Summary

The Proxy Pattern gives you a stand-in object that controls access to a real object.

It is useful for lazy loading, permission checks, caching, remote communication, logging, and resource management.

Use Proxy when the client should keep using the same interface, but access to the real object needs to be controlled, delayed, protected, or optimized.

