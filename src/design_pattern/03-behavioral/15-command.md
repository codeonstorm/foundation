# Command Pattern - Deep Dive

**Goal:** Encapsulate a request as an object so it can be passed around, queued, logged, undone, retried, or executed later.

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

Sometimes you need to represent an action as data.

Examples:

- A button should trigger an action, but the button should not know the business logic
- A text editor needs undo and redo
- A job queue needs to store work for later
- An admin panel needs to log every operation
- A remote control needs configurable buttons
- A payment operation may need retry logic

Without Command, client code often calls methods directly and loses the ability to store, undo, queue, or replay the request.

### Real Example

```php
class Button
{
    public function click(): void
    {
        $light = new Light();
        $light->turnOn();
    }
}
```

This button is tightly coupled to `Light`. If tomorrow the button should open a door, send an email, or run a backup, the button class must change.

Command solves this by turning the action into an object. The button only knows how to execute a command.

---

## Pattern Concept

### What is Command?

The Command Pattern wraps a request inside an object.

Instead of calling the receiver directly, the client creates a command object. That command knows which receiver to call and what data to pass.

### Why It Matters

- Decouples the object that triggers an action from the object that performs it
- Makes actions reusable and configurable
- Allows undo and redo
- Supports queues, retries, logs, and scheduled jobs
- Lets you store history of executed operations
- Makes business operations easier to test in isolation

### When to Use

Use Command when:
- You need undo or redo
- You need to queue or schedule work
- You want to log operations as objects
- You want to parameterize buttons, menus, routes, or shortcuts
- You need retryable actions
- You want to separate request creation from request execution

Avoid Command when:
- The action is simple and will never need history, queueing, or decoupling
- A direct method call is clearer
- The command would only wrap one line with no real benefit
- Too many small command classes would make the code harder to follow

---

## Structure & Components

### Pattern Diagram

```text
Client -> Command -> Receiver
            ^
            |
         Invoker
```

### Key Components

| Component | Role |
|-----------|------|
| `Command` | Interface that defines `execute()` |
| `ConcreteCommand` | Stores the receiver and request data |
| `Receiver` | Object that performs the actual work |
| `Invoker` | Object that triggers the command |
| `Client` | Creates commands and wires them to receivers |

### Typical Methods

- `execute()` - perform the action
- `undo()` - reverse the action, if supported
- `redo()` - execute again, often handled by a command manager
- `canExecute()` - optional validation before execution

---

## PHP Implementation

### Basic Command Example

This example decouples a remote control from the objects it controls.

```php
<?php

interface Command
{
    public function execute(): void;
}

class Light
{
    public function turnOn(): void
    {
        echo "Light is on\n";
    }

    public function turnOff(): void
    {
        echo "Light is off\n";
    }
}

class TurnLightOnCommand implements Command
{
    public function __construct(private Light $light)
    {
    }

    public function execute(): void
    {
        $this->light->turnOn();
    }
}

class TurnLightOffCommand implements Command
{
    public function __construct(private Light $light)
    {
    }

    public function execute(): void
    {
        $this->light->turnOff();
    }
}

class RemoteControl
{
    private ?Command $command = null;

    public function setCommand(Command $command): void
    {
        $this->command = $command;
    }

    public function pressButton(): void
    {
        if ($this->command === null) {
            echo "No command configured\n";
            return;
        }

        $this->command->execute();
    }
}

$light = new Light();
$remote = new RemoteControl();

$remote->setCommand(new TurnLightOnCommand($light));
$remote->pressButton();

$remote->setCommand(new TurnLightOffCommand($light));
$remote->pressButton();
```

The remote does not know about `Light`. It only knows the `Command` interface.

### Proper Example: Text Editor with Undo and Redo

This example shows why Command is powerful. Each editor action becomes an object that can execute and undo itself.

```php
<?php

interface UndoableCommand
{
    public function execute(): void;

    public function undo(): void;
}

class TextEditor
{
    private string $content = '';

    public function insert(int $position, string $text): void
    {
        $this->content =
            substr($this->content, 0, $position)
            . $text
            . substr($this->content, $position);
    }

    public function delete(int $position, int $length): string
    {
        $deletedText = substr($this->content, $position, $length);

        $this->content =
            substr($this->content, 0, $position)
            . substr($this->content, $position + $length);

        return $deletedText;
    }

    public function getContent(): string
    {
        return $this->content;
    }
}

class InsertTextCommand implements UndoableCommand
{
    public function __construct(
        private TextEditor $editor,
        private int $position,
        private string $text
    ) {
    }

    public function execute(): void
    {
        $this->editor->insert($this->position, $this->text);
    }

    public function undo(): void
    {
        $this->editor->delete($this->position, strlen($this->text));
    }
}

class DeleteTextCommand implements UndoableCommand
{
    private string $deletedText = '';

    public function __construct(
        private TextEditor $editor,
        private int $position,
        private int $length
    ) {
    }

    public function execute(): void
    {
        $this->deletedText = $this->editor->delete($this->position, $this->length);
    }

    public function undo(): void
    {
        $this->editor->insert($this->position, $this->deletedText);
    }
}

class CommandHistory
{
    private array $undoStack = [];
    private array $redoStack = [];

    public function execute(UndoableCommand $command): void
    {
        $command->execute();

        $this->undoStack[] = $command;
        $this->redoStack = [];
    }

    public function undo(): void
    {
        $command = array_pop($this->undoStack);

        if ($command === null) {
            echo "Nothing to undo\n";
            return;
        }

        $command->undo();
        $this->redoStack[] = $command;
    }

    public function redo(): void
    {
        $command = array_pop($this->redoStack);

        if ($command === null) {
            echo "Nothing to redo\n";
            return;
        }

        $command->execute();
        $this->undoStack[] = $command;
    }
}

$editor = new TextEditor();
$history = new CommandHistory();

$history->execute(new InsertTextCommand($editor, 0, 'Hello'));
$history->execute(new InsertTextCommand($editor, 5, ' World'));

echo $editor->getContent() . "\n"; // Hello World

$history->execute(new DeleteTextCommand($editor, 5, 6));

echo $editor->getContent() . "\n"; // Hello

$history->undo();

echo $editor->getContent() . "\n"; // Hello World

$history->redo();

echo $editor->getContent() . "\n"; // Hello
```

The editor only knows how to change text. The commands know how to apply and reverse each action. The history manager knows how to store and replay commands.

### Command Queue Example

Commands can be stored and executed later. This is useful for background jobs and scheduled tasks.

```php
<?php

interface QueueCommand
{
    public function execute(): void;
}

class SendEmailCommand implements QueueCommand
{
    public function __construct(
        private string $email,
        private string $message
    ) {
    }

    public function execute(): void
    {
        echo "Sending email to {$this->email}: {$this->message}\n";
    }
}

class GenerateReportCommand implements QueueCommand
{
    public function __construct(private string $reportName)
    {
    }

    public function execute(): void
    {
        echo "Generating report: {$this->reportName}\n";
    }
}

class CommandQueue
{
    private array $commands = [];

    public function add(QueueCommand $command): void
    {
        $this->commands[] = $command;
    }

    public function run(): void
    {
        while ($command = array_shift($this->commands)) {
            $command->execute();
        }
    }
}

$queue = new CommandQueue();
$queue->add(new SendEmailCommand('alice@example.com', 'Welcome!'));
$queue->add(new GenerateReportCommand('monthly-sales'));
$queue->add(new SendEmailCommand('admin@example.com', 'Report is ready.'));

$queue->run();
```

### Command Bus Example

A command bus maps command objects to handlers. This style is common in application architecture.

```php
<?php

class CreateUserCommand
{
    public function __construct(
        public string $name,
        public string $email
    ) {
    }
}

class CreateUserHandler
{
    public function handle(CreateUserCommand $command): void
    {
        echo "Create user {$command->name} with email {$command->email}\n";
    }
}

class CommandBus
{
    private array $handlers = [];

    public function register(string $commandClass, object $handler): void
    {
        $this->handlers[$commandClass] = $handler;
    }

    public function dispatch(object $command): void
    {
        $commandClass = $command::class;

        if (!isset($this->handlers[$commandClass])) {
            throw new RuntimeException("No handler registered for {$commandClass}");
        }

        $handler = $this->handlers[$commandClass];
        $handler->handle($command);
    }
}

$bus = new CommandBus();
$bus->register(CreateUserCommand::class, new CreateUserHandler());

$bus->dispatch(new CreateUserCommand('Alice', 'alice@example.com'));
```

---

## Real-World Scenarios

### Scenario 1: Undo and Redo

Text editors, drawing tools, spreadsheet apps, and workflow builders can store every operation as a command.

### Scenario 2: Job Queues

Commands can represent background jobs such as sending emails, generating PDFs, resizing images, or syncing data.

### Scenario 3: Admin Actions

Admin operations can be stored as command objects for logging, auditing, permissions, and replay.

### Scenario 4: UI Buttons and Menus

Buttons, menu items, keyboard shortcuts, and toolbar actions can execute configurable commands.

### Scenario 5: Command Bus Architecture

Applications can separate "what should happen" from "how it happens" by dispatching command objects to handlers.

---

## Pros & Cons

### Advantages

- Decouples invoker from receiver
- Encapsulates request data and behavior
- Supports undo and redo
- Supports queues and delayed execution
- Makes actions loggable and replayable
- Helps organize application use cases
- Makes buttons, menus, and shortcuts configurable

### Disadvantages

- Adds more classes
- Can feel heavy for simple method calls
- Undo logic can be complex
- Command objects can become too large
- Debugging can require jumping between command, handler, and receiver
- Serialized commands need careful versioning

---

## Best Practices

1. Give each command one clear purpose.
2. Name commands with verbs, such as `CreateUserCommand` or `SendInvoiceCommand`.
3. Keep command data simple and explicit.
4. Put business work in handlers or receivers, not necessarily inside data-only commands.
5. Add `undo()` only when the command can truly be reversed safely.
6. Log command execution when auditability matters.
7. Validate commands before running irreversible operations.
8. Avoid storing live service objects in commands that must be serialized.

### Good Command Design

```php
<?php

class SendInvoiceCommand
{
    public function __construct(
        public int $invoiceId,
        public string $recipientEmail
    ) {
    }
}
```

This command clearly describes the requested action and carries only the data needed to perform it.

---

## Common Pitfalls

### Pitfall 1: Command Does Too Much

A command should represent one action. If it creates users, sends reports, updates billing, and clears cache, split it into smaller commands.

### Pitfall 2: Undo Is Not Actually Safe

Some actions cannot be reversed cleanly. Sending an email or charging a card cannot be undone by pretending the action never happened.

Use compensating commands instead, such as `RefundPaymentCommand`.

### Pitfall 3: Overusing Commands

Not every method call needs a command class. Use Command when you need decoupling, queueing, history, undo, logging, scheduling, or retry behavior.

### Pitfall 4: Hidden Dependencies

If a command reaches into a global service locator, it becomes harder to test. Prefer injecting receivers or passing commands to handlers with dependencies.

### Pitfall 5: Serialization Problems

Queued commands may need to be serialized. Avoid storing closures, database connections, file handles, or large object graphs inside queued commands.

---

## Variants

### Simple Command

The command contains both request data and execution logic.

```php
$command->execute();
```

### Command + Handler

The command is a data object, and a handler performs the work.

```php
$handler->handle($command);
```

### Macro Command

A macro command executes multiple commands as a group.

```php
<?php

class MacroCommand implements Command
{
    public function __construct(private array $commands)
    {
    }

    public function execute(): void
    {
        foreach ($this->commands as $command) {
            $command->execute();
        }
    }
}
```

### Undoable Command

The command provides both `execute()` and `undo()`.

```php
interface UndoableCommand
{
    public function execute(): void;

    public function undo(): void;
}
```

### Queued Command

The command is stored and executed later by a worker.

---

## Practice Exercises

### Exercise 1: Remote Control

Create a remote control with commands for:
- Turn light on
- Turn light off
- Start fan
- Stop fan

The remote should not know the concrete receiver classes.

### Exercise 2: Calculator Undo

Build a calculator where add, subtract, multiply, and divide are commands. Add undo support.

### Exercise 3: Text Editor

Extend the text editor example with:
- Replace text command
- Clear all command
- Multiple undo operations
- Multiple redo operations

### Exercise 4: Job Queue

Create a command queue that runs:
- Send email command
- Resize image command
- Generate invoice command

Add logging before and after each command executes.

### Exercise 5: Command Bus

Build a command bus for:
- `CreateUserCommand`
- `DeleteUserCommand`
- `SendPasswordResetCommand`

Each command should have its own handler.

---

## Summary

The Command Pattern turns actions into objects.

It is useful when you need to pass actions around, queue them, log them, undo them, retry them, schedule them, or configure them dynamically.

In PHP, Command appears in job queues, console commands, command buses, UI actions, admin operations, and undoable workflows. Use it when the action deserves to be treated as a first-class object.

