---
sidebar_position: 26
---

## TUI namespace

TUI is a namespace for terminal-ui standard libraries. This namespace provides
classes and properties for configurations. 

> Note <br/>
> TUI cannot be instantiated since it is static class.


### Available Classes

| Name                           | Type         | Description                                                   |
|--------------------------------|--------------|---------------------------------------------------------------|
| `DefaultTerminal`              | Class        | Standard terminal implementation for basic TUI operations     |
| `YPFTerminal`                  | Class        | YPF-based terminal with windowed UI support                   |
| `SGR`                          | Static Class | Text styling utilities (bold, underline, reset, etc.)         |
| `TextColor`                    | Static Class | Foreground and background color definitions for terminal text |
| `KeyStroke`                    | Static Class | Represents keyboard input events (keys, modifiers, etc.)      |
| `TerminalSize`                 | Class        | Represents terminal dimensions (columns and rows)             |
| `SimpleTerminalResizeListener` | Class        | Listener for handling terminal resize events                  |


Default terminal and YPF terminal initialize panel for creating  text-based user interfaces. 
giving us similar abilities to the Curses C library. 
However, TUI is written in pure Ysharp. 
It also gives us the ability to generate a 
terminal UI even in a pure graphical environment 
by using an emulated terminal.

### Accessing the Terminal


Before we can do terminal UI work, we need a terminal. 
This might be the actual system terminal that we’re running on, or a YPF frame that emulates one.

The safest way to access such a terminal is to use the DefaultTerminal.
This will do the best thing depending on the environment in which it’s running:

````ysharp
const terminal = new TUI.DefaultTerminal();
````

Alternatively, we can create the YPF terminal that emulates the terminal behaviour

````ysharp
const terminal = new TUI.YPFTerminal();
````

> Note <br/>
> **USE YPF terminal in your applications**
> since windows-cmd like shells are buggy


---

### Static Utility Classes

The `TUI` module includes several static utility classes used for styling and input handling. These classes cannot be instantiated and are designed to be used as constant providers and helpers.

#### SGR
Provides predefined text styling options such as bold, underline, italic, reverse, and similar terminal text attributes.  
Used to modify how text is rendered in the terminal.

#### TextColor
Provides predefined color definitions for terminal output.  
Supports foreground and background coloring, typically based on ANSI color standards.

#### KeyStroke
Represents keyboard input in the TUI system.  
Encapsulates key events, including key types and modifier information (e.g., Ctrl, Alt).

#### Notes

- These classes are **static-only** and cannot be instantiated.
- They expose **constants and utility functions** used throughout the TUI system.
- They are typically used alongside terminal implementations such as `DefaultTerminal` and `YPFTerminal`.
- They form the **low-level foundation** for text rendering and input handling.

#### Design Overview

- `SGR` &rarr; handles **text styling layer** (bold, underline, etc.)
- `TextColor` &rarr; handles **color layer** (foreground/background colors)
- `KeyStroke` &rarr; handles **input layer** (keyboard events)

Together, these components form the **core utility layer** of the TUI system.




## Terminal Prototype

Default terminal and YPF terminal shares the same prototype so that you can access every method in both terminal types.

### Reference (Instance Methods)

| Method                   | Signature                                               | Return Type    | Description                                             |
|--------------------------|---------------------------------------------------------|----------------|---------------------------------------------------------|
| `putCharacter`           | `terminal.putCharacter(char : char)`                    | `null`         | Writes a single character to the terminal               |
| `clearScreen`            | `terminal.clearScreen()`                                | `null`         | Clears the terminal screen                              |
| `flush`                  | `terminal.flush()`                                      | `null`         | Flushes the output buffer                               |
| `close`                  | `terminal.close()`                                      | `null`         | Closes the terminal                                     |
| `bell`                   | `terminal.bell()`                                       | `null`         | Triggers terminal bell sound                            |
| `enterPrivateMode`       | `terminal.enterPrivateMode()`                           | `null`         | Switches terminal to private mode                       |
| `exitPrivateMode`        | `terminal.exitPrivateMode()`                            | `null`         | Exits private mode                                      |
| `setCursorPosition`      | `terminal.setCursorPosition(x : int, y : int)`          | `null`         | Sets cursor position                                    |
| `enableSgr`              | `terminal.enableSgr(sgr : SGR)`                         | `null`         | Enables a text style (bold, underline, etc.)            |
| `disableSgr`             | `terminal.disableSgr(sgr : SGR)`                        | `null`         | Disables a text style                                   |
| `resetColorAndSGR`       | `terminal.resetColorAndSGR()`                           | `null`         | Resets all styles and colors                            |
| `setBackgroundColor`     | `terminal.setBackgroundColor(color : TextColor)`        | `null`         | Sets background color                                   |
| `setForegroundColor`     | `terminal.setForegroundColor(color : TextColor)`        | `null`         | Sets foreground color                                   |
| `setCursorVisible`       | `terminal.setCursorVisible(flag : bool)`                | `null`         | Shows or hides the cursor                               |
| `write`                  | `terminal.write(value : any)`                           | `null`         | Writes value to terminal (auto converts to string)      |
| `writeLine`              | `terminal.writeLine(value : any)`                       | `null`         | Writes value and adds newline                           |
| `readKey`                | `terminal.readKey()`                                    | `KeyStroke`    | Waits and reads a key input                             |
| `pollKey`                | `terminal.pollKey()`                                    | `KeyStroke`    | null  (Non-blocking key read, returns null if no input) | 
| `clearInputBuffer`       | `terminal.clearInputBuffer()`                           | `null`         | Clears all pending input                                |
| `getTerminalSize`        | `terminal.getTerminalSize()`                            | `TerminalSize` | Returns current terminal size                           |
| `addResizeListener`      | `terminal.addResizeListener(listener : ResizeListener)` | `null`         | Adds listener for terminal resize events                |


## KeyStroke

`KeyStroke` represents a single keyboard input event in the TUI system.  
It encapsulates both the key itself and modifier states such as Ctrl, Alt, and Shift.

#### Overview

A `KeyStroke` object is typically produced by the terminal when reading input.  
It can represent:

- A character input (e.g., `'a'`, `'1'`)
- A special key (e.g., Enter, Escape, Arrow keys)
- A key combined with modifiers (Ctrl, Alt, Shift)

#### Creation

`KeyStroke` instances are created using static factory methods:

- `KeyStroke.fromKeyType(keyType)`
- `KeyStroke.fromKeyTypeMods(keyType, ctrl, alt)`
- `KeyStroke.fromKeyTypeFull(keyType, ctrl, alt, shift)`
- `KeyStroke.fromChar(char, ctrl, alt)`
- `KeyStroke.fromCharFull(char, ctrl, alt, shift)`

These allow constructing key events manually if needed. :contentReference[oaicite:0]{index=0}

#### Properties & Behavior

A `KeyStroke` instance provides access to:

- The pressed character (if applicable)
- The event timestamp
- Modifier states:
    - Ctrl
    - Alt
    - Shift

#### Methods

Common instance methods include:

- `getCharacter()` &rarr; returns the typed character (if any)
- `getEventTime()` &rarr; returns the timestamp of the event
- `isCtrlDown()` &rarr; checks if Ctrl was pressed
- `isAltDown()` &rarr; checks if Alt was pressed
- `isShiftDown()` &rarr; checks if Shift was pressed
- `toString()` &rarr; returns a string representation of the key event

#### Notes

- `KeyStroke` is a runtime object, not a static utility class.
- It wraps the underlying terminal key event system (Lanterna).
- Used primarily in input loops for handling user interaction.

#### Example

```ysharp
var key = terminal.readInput();

if (key.isCtrlDown()) do
    print("Ctrl pressed");
end

if (key.getCharacter() == 'q') do
    print("Quit");
end
````

## SGR

`SGR` (Select Graphic Rendition) provides text styling capabilities for terminal output.  
It defines visual attributes such as bold, underline, italic, and other text effects.

#### Overview

`SGR` is a static utility class that exposes predefined styling constants.  
These constants are used to control how text is rendered in the terminal.

Each style is represented as an enum-like object that can be passed to terminal APIs.

#### Available Styles

Common styling options include:

- `BOLD`
- `UNDERLINE`
- `ITALIC`
- `REVERSE`
- `BLINK`
- `CROSSED_OUT`
- `CIRCLED`
- `BORDERED`
- `FRAKTUR`

These represent standard terminal text attributes.

#### Usage

`SGR` values are typically used when writing styled text to the terminal.

They define how the text should appear visually (e.g., bold or underlined).

#### Methods

- `toString()` &rarr; returns the string representation of the style
- `SGR.valueOf(name)` &rarr; returns a style by its name
- `SGR.values()` &rarr; returns all available styles as an array

#### Notes

- `SGR` is a **static class** and cannot be instantiated.
- Each constant is internally represented as a runtime object.
- It wraps the underlying terminal styling system (Lanterna SGR).
- Used together with `TextColor` for full text rendering control.

#### Example

```ysharp
const terminal = new TUI.YPFTerminal();
terminal.enableSgr(TUI.SGR.BOLD);
terminal.enableSgr(TUI.SGR.ITALIC);
terminal.writeLine("Ysharp Presentation Foundation (YPF)");
terminal.flush();
````

## TextColor

`TextColor` provides color support for terminal output in the TUI system.  
It defines how text is colored when rendered in the terminal.

#### Overview

`TextColor` is a static utility class that exposes color definitions.  
It is primarily based on ANSI color standards and is used together with text output operations.

Colors are represented as runtime objects and can be passed directly to terminal APIs.

#### Color System

`TextColor` includes a nested `ANSI` color system which provides predefined colors such as:

- `BLACK`
- `RED`
- `GREEN`
- `YELLOW`
- `BLUE`
- `MAGENTA`
- `CYAN`
- `WHITE`
- `DEFAULT`

It also includes brighter variants:

- `BLACK_BRIGHT`
- `RED_BRIGHT`
- `GREEN_BRIGHT`
- `YELLOW_BRIGHT`
- `BLUE_BRIGHT`
- `MAGENTA_BRIGHT`
- `CYAN_BRIGHT`
- `WHITE_BRIGHT`

These correspond to standard ANSI terminal colors. :contentReference[oaicite:0]{index=0}

#### Usage

Color values are typically used when rendering text in the terminal.

They control the foreground (and potentially background) appearance of text.

#### Methods

- `toString()` &rarr; returns the string representation of the color
- `TextColor.ANSI.valueOf(name)` &rarr; returns a color by its name
- `TextColor.ANSI.values()` &rarr; returns all available colors as an array

#### Notes

- `TextColor` is a **static class** and cannot be instantiated.
- Colors are exposed via the `ANSI` namespace.
- Used together with `SGR` for full styling (color + text effects).

#### Example (Conceptual)

```ysharp
terminal.putString("Hello", TextColor.ANSI.GREEN);

terminal.putString("Error", TextColor.ANSI.RED);
````

## Terminal Resize

The TUI system provides built-in support for detecting and responding to terminal resize events.  
This is handled through two components: `TerminalSize` and `SimpleTerminalResizeListener`.

---

####  TerminalSize

`TerminalSize` represents the dimensions of a terminal window, expressed as columns (width) and rows (height).

#### Overview

A `TerminalSize` instance is typically returned when querying the current terminal dimensions.  
It can also be created manually to represent a fixed or expected size.

#### Creation

````ysharp
const size = new TUI.TerminalSize(columns : int, rows : int);
````

#### Methods

| Method        | Signature                            | Return Type    | Description                                        |
|---------------|--------------------------------------|----------------|----------------------------------------------------|
| `getColumns`  | `size.getColumns()`                  | `int`          | Returns the number of columns (terminal width)     |
| `getRows`     | `size.getRows()`                     | `int`          | Returns the number of rows (terminal height)       |
| `toString`    | `size.toString()`                    | `string`       | Returns a string representation of the size        |
| `withColumns` | `size.withColumns(columns : int)`    | `TerminalSize` | Returns a new `TerminalSize` with updated columns  |
| `withRows`    | `size.withRows(rows : int)`          | `TerminalSize` | Returns a new `TerminalSize` with updated rows     |

#### Example

````ysharp
const terminal = new TUI.YPFTerminal();

const size = terminal.getTerminalSize();

println("Columns: " + size.getColumns());
println("Rows: " + size.getRows());
````

---

####  SimpleTerminalResizeListener

`SimpleTerminalResizeListener` listens for terminal resize events and tracks the most recently known terminal size.  
It implements the resize listener interface and can be attached to any terminal instance.

#### Overview

When attached to a terminal, `SimpleTerminalResizeListener` will be notified whenever the terminal is resized.  
It stores the last known size and provides a flag to check whether a resize has occurred since the last check.

#### Creation

````ysharp
const listener = new TUI.SimpleTerminalResizeListener(initialSize);
````

The constructor takes an initial `TerminalSize` as a baseline.

#### Methods

| Method               | Signature                               | Return Type    | Description                                                             |
|----------------------|-----------------------------------------|----------------|-------------------------------------------------------------------------|
| `getLastKnownSize`   | `listener.getLastKnownSize()`           | `TerminalSize` | Returns the most recently known terminal size                           |
| `isTerminalResized`  | `listener.isTerminalResized()`          | `bool`         | Returns `true` if the terminal has been resized since the last check    |
| `onResized`          | `listener.onResized(terminal, size)`    | `null`         | Manually triggers a resize notification with the given size             |

#### Notes

- `SimpleTerminalResizeListener` must be attached to a terminal using `addResizeListener`.
- Once attached, the listener is automatically updated when the terminal is resized.
- `isTerminalResized()` is useful for polling-based resize detection in render loops.
- The `onResized` method is typically called by the terminal internally, not manually.

#### Example

````ysharp
const terminal = new TUI.YPFTerminal();

// create an initial size to bootstrap the listener
const initialSize = terminal.getTerminalSize();

// create the resize listener
const resizeListener = new TUI.SimpleTerminalResizeListener(initialSize);

// attach listener to terminal
terminal.addResizeListener(resizeListener);

// main render loop
while true do
    if resizeListener.isTerminalResized() then do
        const newSize = resizeListener.getLastKnownSize();

        println("Terminal resized!");
        println("New columns: " + newSize.getColumns());
        println("New rows: " + newSize.getRows());
    end

    // ... rest of render logic
end
````

## Auto Flush

The terminal provides an `autoFlush` property that controls how output is rendered.

#### Overview

`autoFlush` determines whether the terminal automatically updates the screen after each write operation.

#### Behavior

- `autoFlush = true`
  - The terminal automatically calls `flush()` after each write operation.
  - Output appears immediately on the screen.

- `autoFlush = false`
  - Output is buffered internally.
  - You must manually call `flush()` to render changes.

#### Notes

- `autoFlush` is useful for simple scripts and quick debugging.
- For performance-critical applications (such as games or dynamic UIs), it is recommended to disable auto flush.
- Disabling auto flush allows batching multiple write operations into a single render call.

#### Recommended Usage

#### Simple usage

```ysharp
const terminal = new TUI.YPFTerminal();

terminal.autoFlush = true;
terminal.writeLine("Hello World");
````