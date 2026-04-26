---
sidebar_position: 26
---

### Terminal Methods

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