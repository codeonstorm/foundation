# Visitor Pattern - Deep Dive

**Goal:** Add new operations to a group of related classes without changing those classes.

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

You have a structure made of different object types, and you need to perform different operations on those objects.

Examples:

- Export document nodes as HTML, Markdown, or plain text
- Calculate total price for different cart items
- Generate reports from different domain objects
- Analyze an abstract syntax tree
- Validate different form field types

Without Visitor, you may keep adding methods to every class.

### Real Example

```php
class Heading
{
    public function toHtml(): string
    {
        return '<h1>Title</h1>';
    }

    public function toMarkdown(): string
    {
        return '# Title';
    }
}

class Paragraph
{
    public function toHtml(): string
    {
        return '<p>Text</p>';
    }

    public function toMarkdown(): string
    {
        return 'Text';
    }
}
```

If you later need `toPlainText()`, `wordCount()`, or `validate()`, you keep changing every element class.

Visitor solves this by moving operations into visitor classes.

---

## Pattern Concept

### What is Visitor?

The Visitor Pattern separates operations from the objects they operate on.

Each element class implements an `accept()` method. The visitor has a separate method for each concrete element type.

### Why It Matters

- Adds new operations without editing element classes
- Keeps related operation logic in one visitor
- Works well with object structures like trees
- Supports reporting, exporting, validation, and analysis
- Makes operations explicit and testable

### When to Use

Use Visitor when:
- You have a stable set of element classes
- You frequently add new operations
- Operations need different behavior per element type
- You work with trees or object structures
- You want to keep element classes focused on data structure

Avoid Visitor when:
- You frequently add new element classes
- The structure is simple
- Operations are naturally part of the element classes
- The visitor interface would become too large

---

## Structure & Components

### Pattern Diagram

```text
Client -> Element.accept(visitor)
              |
              v
         Visitor.visitElement()
```

### Key Components

| Component | Role |
|-----------|------|
| `Visitor` | Declares visit methods for each element type |
| `ConcreteVisitor` | Implements one operation across many element types |
| `Element` | Declares `accept()` |
| `ConcreteElement` | Calls the correct visitor method |
| `ObjectStructure` | Collection or tree of elements |

### Double Dispatch

Visitor uses double dispatch:

1. The client calls `$element->accept($visitor)`
2. The element calls the correct visitor method, such as `$visitor->visitHeading($this)`

This lets PHP choose behavior based on both the visitor type and the element type.

---

## PHP Implementation

### Basic Visitor Example

```php
<?php

interface DocumentElement
{
    public function accept(DocumentVisitor $visitor): void;
}

interface DocumentVisitor
{
    public function visitHeading(Heading $heading): void;

    public function visitParagraph(Paragraph $paragraph): void;
}

class Heading implements DocumentElement
{
    public function __construct(public string $text)
    {
    }

    public function accept(DocumentVisitor $visitor): void
    {
        $visitor->visitHeading($this);
    }
}

class Paragraph implements DocumentElement
{
    public function __construct(public string $text)
    {
    }

    public function accept(DocumentVisitor $visitor): void
    {
        $visitor->visitParagraph($this);
    }
}

class HtmlExportVisitor implements DocumentVisitor
{
    private string $html = '';

    public function visitHeading(Heading $heading): void
    {
        $this->html .= "<h1>{$heading->text}</h1>\n";
    }

    public function visitParagraph(Paragraph $paragraph): void
    {
        $this->html .= "<p>{$paragraph->text}</p>\n";
    }

    public function getHtml(): string
    {
        return $this->html;
    }
}

$document = [
    new Heading('Visitor Pattern'),
    new Paragraph('Visitor separates operations from object structures.'),
];

$visitor = new HtmlExportVisitor();

foreach ($document as $element) {
    $element->accept($visitor);
}

echo $visitor->getHtml();
```

### Proper Example: Document Export and Word Count

This example adds two operations to the same document structure without changing the document elements.

```php
<?php

interface Node
{
    public function accept(NodeVisitor $visitor): void;
}

interface NodeVisitor
{
    public function visitTitle(TitleNode $node): void;

    public function visitText(TextNode $node): void;

    public function visitImage(ImageNode $node): void;
}

class TitleNode implements Node
{
    public function __construct(public string $text)
    {
    }

    public function accept(NodeVisitor $visitor): void
    {
        $visitor->visitTitle($this);
    }
}

class TextNode implements Node
{
    public function __construct(public string $text)
    {
    }

    public function accept(NodeVisitor $visitor): void
    {
        $visitor->visitText($this);
    }
}

class ImageNode implements Node
{
    public function __construct(
        public string $url,
        public string $altText
    ) {
    }

    public function accept(NodeVisitor $visitor): void
    {
        $visitor->visitImage($this);
    }
}

class MarkdownVisitor implements NodeVisitor
{
    private string $output = '';

    public function visitTitle(TitleNode $node): void
    {
        $this->output .= "# {$node->text}\n\n";
    }

    public function visitText(TextNode $node): void
    {
        $this->output .= "{$node->text}\n\n";
    }

    public function visitImage(ImageNode $node): void
    {
        $this->output .= "![{$node->altText}]({$node->url})\n\n";
    }

    public function getOutput(): string
    {
        return $this->output;
    }
}

class WordCountVisitor implements NodeVisitor
{
    private int $count = 0;

    public function visitTitle(TitleNode $node): void
    {
        $this->count += str_word_count($node->text);
    }

    public function visitText(TextNode $node): void
    {
        $this->count += str_word_count($node->text);
    }

    public function visitImage(ImageNode $node): void
    {
        $this->count += str_word_count($node->altText);
    }

    public function getCount(): int
    {
        return $this->count;
    }
}

$nodes = [
    new TitleNode('Design Patterns in PHP'),
    new TextNode('Visitor is useful for adding operations to stable structures.'),
    new ImageNode('/visitor.png', 'Visitor pattern diagram'),
];

$markdown = new MarkdownVisitor();
$wordCount = new WordCountVisitor();

foreach ($nodes as $node) {
    $node->accept($markdown);
    $node->accept($wordCount);
}

echo $markdown->getOutput();
echo "Words: " . $wordCount->getCount() . "\n";
```

The document node classes do not need `toMarkdown()` or `countWords()` methods. Those operations live in visitors.

### Shopping Cart Visitor Example

```php
<?php

interface CartItem
{
    public function accept(CartVisitor $visitor): void;
}

interface CartVisitor
{
    public function visitPhysicalProduct(PhysicalProduct $product): void;

    public function visitDigitalProduct(DigitalProduct $product): void;
}

class PhysicalProduct implements CartItem
{
    public function __construct(
        public string $name,
        public float $price,
        public float $shipping
    ) {
    }

    public function accept(CartVisitor $visitor): void
    {
        $visitor->visitPhysicalProduct($this);
    }
}

class DigitalProduct implements CartItem
{
    public function __construct(
        public string $name,
        public float $price
    ) {
    }

    public function accept(CartVisitor $visitor): void
    {
        $visitor->visitDigitalProduct($this);
    }
}

class CartTotalVisitor implements CartVisitor
{
    private float $total = 0;

    public function visitPhysicalProduct(PhysicalProduct $product): void
    {
        $this->total += $product->price + $product->shipping;
    }

    public function visitDigitalProduct(DigitalProduct $product): void
    {
        $this->total += $product->price;
    }

    public function getTotal(): float
    {
        return $this->total;
    }
}
```

---

## Real-World Scenarios

### Scenario 1: Document Exporters

Export the same document structure to HTML, Markdown, plain text, PDF, or JSON.

### Scenario 2: Abstract Syntax Trees

Compilers and interpreters use visitors to evaluate, print, optimize, or validate expression trees.

### Scenario 3: Reporting

Generate reports from different domain objects without adding report logic to those objects.

### Scenario 4: Validation

Validate different form fields or UI components with visitors.

### Scenario 5: Shopping Cart Calculations

Calculate tax, shipping, discounts, or totals across different item types.

---

## Pros & Cons

### Advantages

- Adds operations without changing element classes
- Keeps operation logic grouped in one place
- Works well for stable object structures
- Helps with trees and complex structures
- Makes exporting, analysis, and reporting easier

### Disadvantages

- Adding new element types requires changing every visitor
- Visitor interfaces can become large
- Can expose element internals
- Double dispatch can feel indirect
- More boilerplate than simple method calls

---

## Best Practices

1. Use Visitor when element classes are stable.
2. Keep visitor methods focused on one operation.
3. Name visitors by operation, such as `MarkdownVisitor` or `TaxVisitor`.
4. Avoid using visitors to mutate unrelated state.
5. Keep element `accept()` methods simple.
6. Consider return values carefully. Accumulating state inside the visitor is often simpler in PHP.
7. Avoid Visitor if you add new element classes frequently.

### Good Visitor Design

```php
<?php

interface ShapeVisitor
{
    public function visitCircle(Circle $circle): void;

    public function visitRectangle(Rectangle $rectangle): void;
}
```

The visitor clearly describes which element types it supports.

---

## Common Pitfalls

### Pitfall 1: Too Many Element Types

If you constantly add new element classes, every visitor must be updated. This is the main trade-off of Visitor.

### Pitfall 2: Visitor Does Unrelated Work

A visitor should represent one operation. Do not mix exporting, validation, logging, and saving in the same visitor.

### Pitfall 3: Exposing Too Much State

Visitors may need access to element data. Keep public data minimal and intentional.

### Pitfall 4: Using Visitor for Simple Cases

If there are only one or two classes and one operation, regular methods may be clearer.

---

## Variants

### Classic Visitor

Uses `accept()` and one `visitX()` method for each element type.

### Reflective Visitor

Uses runtime type checks or method names to route visits. It reduces boilerplate but loses some explicitness.

### Acyclic Visitor

Splits visitor interfaces to reduce the need for every visitor to support every element type.

### Visitor with Return Values

Each visit method returns a value instead of storing state inside the visitor.

---

## Practice Exercises

### Exercise 1: Shape Visitor

Create `Circle`, `Rectangle`, and `Triangle` elements. Add visitors for area calculation and SVG export.

### Exercise 2: Document Export

Create document nodes for title, paragraph, quote, and image. Add HTML and Markdown visitors.

### Exercise 3: Cart Tax Visitor

Create physical and digital cart items. Add a tax visitor and total visitor.

### Exercise 4: Validation Visitor

Create form fields for text, email, password, and checkbox. Add a visitor that validates each field.

### Exercise 5: AST Visitor

Create expression nodes for number, addition, and multiplication. Add visitors for evaluation and pretty printing.

---

## Summary

The Visitor Pattern lets you add new operations to a stable object structure without changing the element classes.

It is useful for document trees, syntax trees, reports, validation, exporting, and calculations over mixed object types.

Use Visitor when operations change more often than the elements they operate on.

