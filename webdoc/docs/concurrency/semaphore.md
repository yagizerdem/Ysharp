---
sidebar_position: 28
---

# semaphore

A `Semaphore` is a counter-based synchronization primitive that limits how many
threads can access a shared resource at the same time. Each `acquire()` call
consumes one permit; each `release()` call returns one permit. When no permits
are available, callers either block or fail (depending on which method is used)
until another thread releases a permit.

Ysharp's `Semaphore` is a thin wrapper around Java's
`java.util.concurrent.Semaphore`.

> **Note** <br/>
> A semaphore initialized with `1` permit behaves like a mutex (mutual
> exclusion lock). Use a higher permit count to allow bounded concurrency
> (e.g. a connection pool of size `N`).

---

## Constructor

```ysharp
let s = Semaphore(permits, fair);
```

| Parameter | Type     | Description                                                                           |
|-----------|----------|---------------------------------------------------------------------------------------|
| `permits` | `int`    | Initial number of permits. Must be `>= 0`. May start at `0` for signaling patterns.   |
| `fair`    | `bool`   | If `true`, permits are granted in FIFO order; if `false`, ordering is not guaranteed. |

> **Warning** <br/>
> Passing a negative `permits` value throws a runtime error.

---

## Reference

### Acquiring permits (blocking)

| Method                    | Signature                                | Return Type | Description                                                                                              |
|---------------------------|------------------------------------------|-------------|----------------------------------------------------------------------------------------------------------|
| `acquire`                 | `acquire()`                              | `null`      | Acquires one permit, blocking until one is available. Throws if the thread is interrupted while waiting. |
| `acquireN`                | `acquireN(permits : int)`                | `null`      | Acquires the given number of permits, blocking until they are all available.                             |
| `acquireUninterruptibly`  | `acquireUninterruptibly()`               | `null`      | Acquires one permit, blocking until available. Ignores interrupt requests.                               |
| `acquireUninterruptiblyN` | `acquireUninterruptiblyN(permits : int)` | `null`      | Acquires `permits` permits, blocking until available. Ignores interrupt requests.                        |

### Acquiring permits (non-blocking / timed)

| Method               | Signature                                                            | Return Type | Description                                                                                             |
|----------------------|----------------------------------------------------------------------|-------------|---------------------------------------------------------------------------------------------------------|
| `tryAcquire`         | `tryAcquire()`                                                       | `bool`      | Acquires one permit only if one is immediately available. Returns `true` on success, `false` otherwise. |
| `tryAcquireN`        | `tryAcquireN(permits : int)`                                         | `bool`      | Acquires the given permits only if they are all immediately available.                                  |
| `tryAcquireTimeout`  | `tryAcquireTimeout(timeout : number, unit : string)`                 | `bool`      | Tries to acquire one permit, waiting up to the given timeout.                                           |
| `tryAcquireNTimeout` | `tryAcquireNTimeout(permits : int, timeout : number, unit : string)` | `bool`      | Tries to acquire `permits` permits, waiting up to the given timeout.                                    |

Valid values for `unit`:

`"NANOSECONDS"`, `"MICROSECONDS"`, `"MILLISECONDS"`, `"SECONDS"`, `"MINUTES"`, `"HOURS"`, `"DAYS"`

> **Note** <br/>
> The `unit` string is case-insensitive but must match one of the values above.
> Any other value raises a runtime error.

### Releasing permits

| Method     | Signature                 | Return Type | Description                                  |
|------------|---------------------------|-------------|----------------------------------------------|
| `release`  | `release()`               | `null`      | Releases one permit back to the semaphore.   |
| `releaseN` | `releaseN(permits : int)` | `null`      | Releases the given number of permits.        |

> **Warning** <br/>
> A semaphore does not track ownership: any thread can release permits, even
> ones it did not acquire. This is intentional and useful for signaling
> patterns, but it also means you must be careful not to release more permits
> than you acquired.

### Inspection

| Method              | Signature              | Return Type | Description                                                                  |
|---------------------|------------------------|-------------|------------------------------------------------------------------------------|
| `availablePermits`  | `availablePermits()`   | `int`       | Returns the current number of available permits.                             |
| `drainPermits`      | `drainPermits()`       | `int`       | Atomically acquires and returns all currently available permits.             |
| `getQueueLength`    | `getQueueLength()`     | `int`       | Returns an estimate of the number of threads waiting to acquire a permit.    |
| `hasQueuedThreads`  | `hasQueuedThreads()`   | `bool`      | Returns `true` if any thread is waiting to acquire a permit.                 |
| `isFair`            | `isFair()`             | `bool`      | Returns `true` if this semaphore was constructed with the fairness flag set. |

> **Note** <br/>
> `availablePermits`, `getQueueLength`, and `hasQueuedThreads` are intended for
> monitoring and debugging only. Their values may change immediately after the
> call returns.

---

## Examples

### Mutex (single permit)

A semaphore with `1` permit is the simplest way to protect a critical section:

```ysharp
let mutex = new Semaphore(1, true);
let counter = 0;

function increment() do
    for var i = 0; i < 1000; i = i + 1 do
        mutex.acquire();
        try do
            counter = counter + 1;
        end
        catch(e) do end
        finally do
            mutex.release();
        end
    end
end

let t1 = new Thread(increment);
let t2 = new Thread(increment);

t1.start();
t2.start();
t1.join();
t2.join();

println "counter = " + counter; // 2000
```

### Bounded concurrency (resource pool)

Limit the number of threads that may run a section concurrently:

```ysharp
let pool = new Semaphore(3, false);

function handle(id) do
    pool.acquire();
    try do
        println "processing #" + id;
        sleep(500);
    end
    catch(e) do end
    finally do
        pool.release();
    end
end

let workers = [];
for var i = 0; i < 10; i = i + 1 do
    let t = new Thread(handle, i);
    t.start();
    workers.push(t);
end

foreach var w in workers do
    w.join();
end
```

At most three `handle` calls are active at any moment.

### Non-blocking attempt

Use `tryAcquire()` when you do not want to wait:

```ysharp
let s = new Semaphore(1, false);

if s.tryAcquire() then do
    try do
        println "entered the critical section";
    end
    catch(e) do end
    finally do
        s.release();
    end
end
else do
    println "lock is busy, doing something else";
end
```

### Timed acquire

Wait for a permit, but give up after a deadline:

```ysharp
let s = new Semaphore(0, false);

function producer() do
    __sleep(2000);
    s.release();
end

(new Thread(producer)).start();

if s.tryAcquireTimeout(1, "SECONDS") then do
    println "got the permit within 1 second";
end
else do
    println "timed out: no permit acquired";
end
```

### Signaling between threads

A semaphore initialized with `0` permits can be used as a one-shot signal:

```ysharp
let ready = new Semaphore(0, false);
let result = null;

function compute() do
    __sleep(500);
    result = 42;
    ready.release(); // signal "result is ready"
end

(new Thread(compute)).start();

ready.acquire(); // wait for the signal
println "result = " + result;
```

### Multi-permit acquire

`acquireN` / `releaseN` are useful when a single task consumes multiple units
of a bounded resource:

```ysharp
let bandwidth = new Semaphore(10, false);

function sendLargeMessage() do
    bandwidth.acquireN(4); // this message uses 4 units
    try do
        println "sending...";
        sleep(300);
    end
    catch(e) do end
    finally do
        bandwidth.releaseN(4);
    end
end
```

### Draining a semaphore

`drainPermits()` consumes everything that is currently available, which is
useful when you want to "reset" a signaling semaphore:

```ysharp
let s = new Semaphore(5, false);
let taken = s.drainPermits();
println "drained " + taken + " permits";
println "available now = " + s.availablePermits(); // 0
```

---

## Best Practices

- Always pair `acquire()` (or `tryAcquire()` that returned `true`) with
  `release()` inside a `try / finally` block so the permit is released even
  if the protected code throws.
- Do not call `release()` unless the corresponding `acquire()` succeeded;
  otherwise you increase the permit count above its initial value.
- Prefer fair mode (`Semaphore(n, true)`) when you need predictable ordering
  and want to avoid starvation; prefer unfair mode for higher throughput.
- For simple mutual exclusion, use `Semaphore(1, ...)` and treat it as a lock.
- For producer/consumer signaling, start with `Semaphore(0, ...)` and use
  `release()` as the "signal" and `acquire()` as the "wait".
- Avoid holding a permit across long-running blocking operations whenever
  possible; doing so reduces concurrency and may cause queue buildup.

