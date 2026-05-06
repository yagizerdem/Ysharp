---
sidebar_position: 1
---

# multi threading

Ysharp provides native `Thread` and `Semaphore` classes for concurrent execution of multiple tasks. These classes wrap Java's underlying `java.lang.Thread` and `java.util.concurrent.Semaphore` mechanisms.

> **Note** \
> In Ysharp, each thread runs on its own `Interpreter` instance, but the global environment is shared. It is recommended to use a synchronization primitive such as `Semaphore` when accessing shared state.

---

## `Thread` Class

### Constructor

```ysharp
let t = Thread(callable, ...args);
```

| Parameter  | Type       | Description                                                     |
| ---------- | ---------- | --------------------------------------------------------------- |
| `callable` | `function` | The function or lambda to be called inside the new thread.      |
| `...args`  | `any`      | Arguments passed to the `callable` function at invocation time. |

> **Note** \
> `Thread(...)` only creates the thread object. You must call `start()` to begin execution.

### Static Methods

| Method    | Signature                | Return Type | Description                                                                                                                                                 |
| --------- | ------------------------ | ----------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `current` | `Thread.current()`       | `Thread`    | Returns a `Thread` object representing the currently running thread.                                                                                        |
| `yield`   | `yield()`                | `null`      | Suggests to the scheduler that the calling thread yield the CPU to other threads.                                                                           |
| `sleep`   | `Thread.sleep(ms : int)` | `null`      | Pauses the current thread for `ms` milliseconds. If the thread is interrupted while sleeping, a runtime error is thrown and the interrupt flag is restored. |

> **Warning** \
> The thread obtained via `Thread.current()` is a wrapper around the currently running Java thread; `start()` cannot be called on it.

### Instance Methods

| Method          | Signature                 | Return Type | Description                                                                                                           |
| --------------- | ------------------------- | ----------- | --------------------------------------------------------------------------------------------------------------------- |
| `start`         | `start()`                 | `null`      | Starts the thread and runs the `callable` function inside the new thread.                                             |
| `join`          | `join()`                  | `null`      | Blocks the calling thread until the target thread completes.                                                          |
| `joinTimeout`   | `joinTimeout(ms : int)`   | `null`      | Waits at most `ms` milliseconds for the thread to complete.                                                           |
| `isAlive`       | `isAlive()`               | `bool`      | Returns `true` if the thread is still running, `false` otherwise.                                                     |
| `interrupt`     | `interrupt()`             | `null`      | Sends an interrupt signal to the target thread.                                                                       |
| `isInterrupted` | `isInterrupted()`         | `bool`      | Returns `true` if the thread's interrupt flag has been set.                                                           |
| `getName`       | `getName()`               | `string`    | Returns the name of the thread.                                                                                       |
| `setName`       | `setName(name : string)`  | `null`      | Sets the name of the thread.                                                                                          |
| `getId`         | `getId()`                 | `int`       | Returns the unique ID of the thread.                                                                                  |
| `isDaemon`      | `isDaemon()`              | `bool`      | Returns `true` if the thread is a daemon thread.                                                                      |
| `setDaemon`     | `setDaemon(value : bool)` | `null`      | Sets whether the thread is a daemon. **Must be used before calling `start()`.**                                       |
| `getState`      | `getState()`              | `string`    | Returns the current state name of the thread: `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, `TERMINATED`. |

### Thread Lifecycle States

| State           | Description                                                    |
| --------------- | -------------------------------------------------------------- |
| `NEW`           | Thread has been created but `start()` has not been called yet. |
| `RUNNABLE`      | Running or ready to run.                                       |
| `BLOCKED`       | Waiting for a monitor lock.                                    |
| `WAITING`       | Waiting indefinitely for another thread.                       |
| `TIMED_WAITING` | Waiting for a specified duration.                              |
| `TERMINATED`    | Execution has completed.                                       |

---

## `Thread` Examples

### Creating a simple thread

```ysharp
function task() do
    for var i = 0; i < 5; i = i + 1 do
        println "task: " + i;
    end
end

let t = new Thread(task);
t.start();
t.join();
println "main: done";
```

### Starting a thread with arguments

```ysharp
function greet(name, count) do
    for var i = 0; i < count; i = i + 1 do
        println "Hello " + name;
    end
end

let t = new Thread(greet, "Ysharp", 3);
t.start();
t.join();
```

### Running multiple threads in parallel

```ysharp
function worker(id) do
    for var i = 0; i < 3; i = i + 1 do
        println "worker " + id + " step " + i;
        Thread.current().yield();
    end
end

let threads = [];
for var i = 0; i < 4; i = i + 1 do
    let t = new Thread(worker, i);
    t.start();
    threads.push(t);
end

foreach var t in threads do
    t.join();
end
println "all workers completed";
```

### Daemon thread

```ysharp
function background() do
    while true do
        println "background tick";
        __sleep(500);
    end
end

let t = new Thread(background);
t.setDaemon(true); // must be called before start()
t.start();

__sleep(1500);
println "main thread ended, daemon exits automatically";
```

### Interrupting a thread

```ysharp
function loop() do
    try do
        while !Thread.current().isInterrupted() do
            println "running...";
            __sleep(200);
        end
    end
    catch(e) do
        return null;
    end
    finally do
        println "interrupted, exiting";
    end
end

let t = new Thread(loop);
t.start();

__sleep(1000);
t.interrupt();
t.join();
```

### Join with timeout

```ysharp
function slow() do
    try do
        __sleep(5000);
    end
    catch(e) do
        return null;
    end
end

let t = new Thread(slow);
t.start();

t.joinTimeout(1000);
if (t.isAlive()) then do
    println "thread is still running, interrupting";
    t.interrupt();
end
```

---

## Best Practices

- Always protect access to shared variables with a `Semaphore` or similar primitive.
- Always pair `acquire()` and `release()` calls inside a `try/finally` block; otherwise the lock will never be released in case of an error.
- Always call `setDaemon(true)` **before** `start()`.
- In threads with infinite loops, use `Thread.current().isInterrupted()` to enable clean shutdown.
- To avoid leaking thread resources, always plan to call `join()` or `joinTimeout()` for every thread you create.
