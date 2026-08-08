# Memento Pattern - Deep Dive

**Goal:** Capture and restore an object's previous state without exposing its internal details.

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

Sometimes an object needs to return to a previous state.

Examples:

- Undo changes in a text editor
- Restore a form to its last saved values
- Roll back a wizard to an earlier step
- Save and load game checkpoints
- Keep snapshots before risky changes

Without Memento, other objects may need to read and store the object's private details.

### Real Example

```php
class TextEditor
{
    public string $content = '';
    public int $cursorPosition = 0;
}

$editor = new TextEditor();
$history[] = [
    'content' => $editor->content,
    'cursorPosition' => $editor->cursorPosition,
];
```

This exposes implementation details. If the editor changes how it stores text or cursor position, the history code breaks.

Memento solves this by letting the object create a snapshot of itself and restore from that snapshot later.

---

## Pattern Concept

### What is Memento?

The Memento Pattern captures an object's internal state in a separate snapshot object.

The object being saved is called the originator. The object managing snapshots is called the caretaker. The caretaker stores mementos but should not depend on their internal details.

### Why It Matters

- Supports undo and rollback
- Preserves encapsulation
- Keeps snapshot creation inside the object being saved
- Separates history management from business behavior
- Makes state restoration explicit

### When to Use

Use Memento when:
- You need undo or redo
- You need checkpoints or restore points
- You want rollback after failed operations
- Capturing state outside the object would break encapsulation
- You need a history of object states

Avoid Memento when:
- State is very large and snapshots are expensive
- A simple copy is enough
- Only one field needs to be restored
- The object can be rebuilt cheaply from source data
- You need audit logs rather than restorable snapshots

---

## Structure & Components

### Pattern Diagram

```text
Client -> Caretaker -> Memento
            ^
            |
        Originator
        save()
        restore()
```

### Key Components

| Component | Role |
|-----------|------|
| `Originator` | Object whose state is captured and restored |
| `Memento` | Snapshot of the originator's state |
| `Caretaker` | Stores and manages mementos |
| `Client` | Triggers save and restore operations |

### Typical Methods

- `save()` - creates a memento
- `restore()` - restores from a memento
- `backup()` - caretaker saves current state
- `undo()` - caretaker restores previous state
- `getName()` - optional label for displaying history

---

## PHP Implementation

### Basic Text Editor Memento

```php
<?php

final class EditorMemento
{
    public function __construct(
        private string $content,
        private int $cursorPosition
    ) {
    }

    public function content(): string
    {
        return $this->content;
    }

    public function cursorPosition(): int
    {
        return $this->cursorPosition;
    }
}

class TextEditor
{
    private string $content = '';
    private int $cursorPosition = 0;

    public function type(string $text): void
    {
        $this->content .= $text;
        $this->cursorPosition = strlen($this->content);
    }

    public function save(): EditorMemento
    {
        return new EditorMemento($this->content, $this->cursorPosition);
    }

    public function restore(EditorMemento $memento): void
    {
        $this->content = $memento->content();
        $this->cursorPosition = $memento->cursorPosition();
    }

    public function show(): void
    {
        echo "Content: {$this->content}\n";
        echo "Cursor: {$this->cursorPosition}\n";
    }
}

$editor = new TextEditor();

$editor->type('Hello');
$snapshot = $editor->save();

$editor->type(' world');
$editor->show();

$editor->restore($snapshot);
$editor->show();
```

The editor creates and consumes its own snapshots, so history code does not need to know how editor state is represented.

### Proper Example: Undo History

```php
<?php

final class DocumentMemento
{
    public function __construct(
        private string $title,
        private string $body,
        private DateTimeImmutable $createdAt
    ) {
    }

    public function title(): string
    {
        return $this->title;
    }

    public function body(): string
    {
        return $this->body;
    }

    public function createdAt(): DateTimeImmutable
    {
        return $this->createdAt;
    }

    public function label(): string
    {
        return $this->createdAt->format('Y-m-d H:i:s') . ' - ' . $this->title;
    }
}

class Document
{
    private string $title = 'Untitled';
    private string $body = '';

    public function rename(string $title): void
    {
        $this->title = $title;
    }

    public function write(string $text): void
    {
        $this->body .= $text;
    }

    public function save(): DocumentMemento
    {
        return new DocumentMemento(
            $this->title,
            $this->body,
            new DateTimeImmutable()
        );
    }

    public function restore(DocumentMemento $memento): void
    {
        $this->title = $memento->title();
        $this->body = $memento->body();
    }

    public function print(): void
    {
        echo "# {$this->title}\n";
        echo $this->body . "\n";
    }
}

class DocumentHistory
{
    private array $snapshots = [];

    public function __construct(private Document $document)
    {
    }

    public function backup(): void
    {
        $snapshot = $this->document->save();
        $this->snapshots[] = $snapshot;

        echo "Saved snapshot: {$snapshot->label()}\n";
    }

    public function undo(): void
    {
        $snapshot = array_pop($this->snapshots);

        if ($snapshot === null) {
            echo "Nothing to undo\n";
            return;
        }

        $this->document->restore($snapshot);
        echo "Restored snapshot: {$snapshot->label()}\n";
    }
}

$document = new Document();
$history = new DocumentHistory($document);

$document->rename('Release Notes');
$document->write("Version 1.0\n");
$history->backup();

$document->write("Version 1.1\n");
$history->backup();

$document->write("Broken draft\n");
$document->print();

$history->undo();
$document->print();
```

The `DocumentHistory` caretaker stores snapshots and performs undo, but the document controls how snapshots are created and restored.

### Form Draft Example

```php
<?php

final class FormDraft
{
    public function __construct(private array $values)
    {
    }

    public function values(): array
    {
        return $this->values;
    }
}

class RegistrationForm
{
    private array $values = [
        'name' => '',
        'email' => '',
        'plan' => 'free',
    ];

    public function fill(string $field, string $value): void
    {
        if (!array_key_exists($field, $this->values)) {
            throw new InvalidArgumentException("Unknown field: {$field}");
        }

        $this->values[$field] = $value;
    }

    public function saveDraft(): FormDraft
    {
        return new FormDraft($this->values);
    }

    public function restoreDraft(FormDraft $draft): void
    {
        $this->values = $draft->values();
    }

    public function values(): array
    {
        return $this->values;
    }
}

$form = new RegistrationForm();
$form->fill('name', 'Alice');
$draft = $form->saveDraft();

$form->fill('plan', 'enterprise');
$form->restoreDraft($draft);

print_r($form->values());
```

Memento is useful when a user can return to a saved form state without exposing form internals to the draft manager.

---

## Real-World Scenarios

### Scenario 1: Undo and Redo

Text editors, drawing tools, form builders, and admin panels often need to restore earlier states.

### Scenario 2: Wizard Steps

A multi-step wizard can save progress before moving between steps.

### Scenario 3: Game Save Points

Games use snapshots to restore player position, inventory, health, and quest state.

### Scenario 4: Transaction Rollback

An object can save its state before a risky operation and restore if validation fails.

### Scenario 5: Draft Management

Documents, emails, and forms can keep recoverable drafts.

---

## Pros & Cons

### Advantages

- Preserves encapsulation
- Supports undo, redo, and rollback
- Keeps snapshot logic inside the originator
- Separates state history from main behavior
- Makes restore points explicit
- Works well for editor-style workflows

### Disadvantages

- Snapshots can consume memory
- Large object graphs can be expensive to copy
- Caretaker logic can become complex
- Mementos may need versioning if state shape changes
- Restoring external resources can be difficult

---

## Best Practices

1. Keep mementos immutable.
2. Let the originator create and restore its own mementos.
3. Store only the state needed for restoration.
4. Limit history size when snapshots can grow large.
5. Use timestamps or labels for user-visible history.
6. Be careful with deep copies of objects and arrays.
7. Do not use mementos as audit logs.

### Good Memento Design

```php
<?php

final class CartSnapshot
{
    public function __construct(private array $items)
    {
    }

    public function items(): array
    {
        return $this->items;
    }
}
```

The snapshot is immutable from the outside and carries only the data needed to restore the cart.

---

## Common Pitfalls

### Pitfall 1: Exposing Too Much State

If the caretaker reads and modifies snapshot internals, encapsulation is lost.

### Pitfall 2: Saving Huge Snapshots

Saving entire object graphs after every small change can cause memory and performance problems.

### Pitfall 3: Confusing Memento with Logging

Memento restores state. Logs explain what happened. They solve different problems.

### Pitfall 4: Shallow Copy Surprises

If a memento stores references to mutable objects, later changes can accidentally modify old snapshots.

### Pitfall 5: No History Limit

Unlimited undo stacks can grow forever in long-running processes or large documents.

---

## Variants

### Undo Stack

Store mementos in a stack and pop the latest snapshot when undo is requested.

```php
$snapshot = array_pop($history);
```

### Redo Stack

When undo happens, push the current state into a redo stack before restoring the previous state.

### Named Checkpoints

Store snapshots by label, such as `before_import` or `draft_saved`.

### Serialized Memento

Store the snapshot as serialized JSON or another format for persistence.

### Command + Memento

Use Command for actions and Memento for restoring object state before or after those actions.

---

## Practice Exercises

### Exercise 1: Text Editor Undo

Create a text editor with `type()`, `delete()`, `save()`, and `undo()`.

### Exercise 2: Drawing Canvas

Create a canvas that stores shapes. Save snapshots before adding, moving, or deleting shapes.

### Exercise 3: Form Drafts

Build a form draft system that can save, restore, and discard drafts.

### Exercise 4: Cart Restore

Create a shopping cart that can restore a previous item list after coupon validation fails.

### Exercise 5: Undo and Redo

Extend the document example with both undo and redo stacks.

---

## Summary

The Memento Pattern captures an object's state so it can be restored later.

It is useful for undo, redo, drafts, checkpoints, and rollback behavior.

Use Memento when you need restoration without exposing internal object details. Keep snapshots focused, immutable, and reasonably small.
