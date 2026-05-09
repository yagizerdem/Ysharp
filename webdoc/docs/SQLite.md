---
sidebar_position: 42
---

# SQLite

SQLite is a built-in native module in Ysharp that provides database access through SQLite. It allows you to create, read, update, and delete data in SQLite databases using a clean, intuitive API that wraps Java's JDBC functionality.

## Getting Started

To use SQLite in your Ysharp program, you need to import and establish a connection to a database:

```ysharp
// Connect to a database file
connection = SQLite.connect("mydata.db")

// Use the connection...

// Always close the connection when done
connection.close()
```

The `connect()` function takes a path to the database file. If the file doesn't exist, it will be created automatically.

## Core Classes

SQLite provides four main classes for database operations:

### SQLite.Connection

Represents a connection to a SQLite database. This is created using `SQLite.connect()` and is your gateway to database operations.

#### Creating a Connection

```ysharp
connection = SQLite.connect("database.db")
```

#### Common Connection Methods

| Method                                  | Description                                           |
| --------------------------------------- | ----------------------------------------------------- |
| `createStatement()`                     | Creates a new Statement for executing SQL queries     |
| `prepareStatement(sql)`                 | Creates a PreparedStatement for parameterized queries |
| `setAutoCommit(boolean)`                | Enable/disable automatic transaction commits          |
| `commit()`                              | Commits the current transaction                       |
| `rollback()`                            | Rolls back the current transaction                    |
| `isValid(timeout)`                      | Validates if the connection is still active           |
| `isReadOnly()` / `setReadOnly(boolean)` | Gets/sets read-only mode                              |
| `close()`                               | Closes the connection and releases resources          |

#### Transaction Control

```ysharp
connection = SQLite.connect("database.db")

// Disable auto-commit for transaction control
connection.setAutoCommit(false)

try {
    // Your database operations here
    statement = connection.createStatement()
    statement.executeUpdate("INSERT INTO users VALUES (1, 'John')")

    // Commit if successful
    connection.commit()
} catch error {
    // Rollback on error
    connection.rollback()
    output(error)
} finally {
    connection.close()
}
```

### SQLite.Statement

Represents an SQL statement. Use this for executing queries and updates.

#### Creating a Statement

```ysharp
statement = connection.createStatement()
```

#### Common Statement Methods

| Method                     | Parameters | Returns   | Description                                             |
| -------------------------- | ---------- | --------- | ------------------------------------------------------- |
| `executeQuery(sql)`        | SQL string | ResultSet | Executes a SELECT query                                 |
| `executeUpdate(sql)`       | SQL string | Integer   | Executes INSERT/UPDATE/DELETE, returns affected rows    |
| `execute(sql)`             | SQL string | Boolean   | Executes any SQL, returns true if ResultSet is returned |
| `getResultSet()`           | -          | ResultSet | Gets the current ResultSet                              |
| `getUpdateCount()`         | -          | Integer   | Gets number of rows affected by last update             |
| `setMaxRows(max)`          | Integer    | -         | Sets maximum number of rows to fetch                    |
| `setQueryTimeout(seconds)` | Integer    | -         | Sets query timeout in seconds                           |
| `close()`                  | -          | -         | Closes the statement                                    |

#### Using Statements

```ysharp
connection = SQLite.connect("database.db")
statement = connection.createStatement()

// Execute a SELECT query
resultSet = statement.executeQuery("SELECT * FROM users WHERE age > 18")
while resultSet.next() {
    id = resultSet.getInt("id")
    name = resultSet.getString("name")
    age = resultSet.getInt("age")
    output(name, " is ", age, " years old")
}
resultSet.close()

// Execute an INSERT, UPDATE, or DELETE
rowsAffected = statement.executeUpdate("INSERT INTO users VALUES (1, 'Alice', 25)")
output("Inserted ", rowsAffected, " row(s)")

statement.close()
```

### SQLite.PreparedStatement

A more secure way to execute SQL queries with parameters. PreparedStatements prevent SQL injection attacks by separating SQL code from data.

#### Creating a PreparedStatement

```ysharp
preparedStmt = connection.prepareStatement("SELECT * FROM users WHERE name = ?")
```

#### Parameter Setting Methods

Use these methods to set parameter values. The `?` in the SQL string represents each parameter (indexed from 1).

| Method                     | Parameter Index | Value Type | Description                 |
| -------------------------- | --------------- | ---------- | --------------------------- |
| `setString(index, value)`  | 1-based         | String     | Sets a string parameter     |
| `setInt(index, value)`     | 1-based         | Integer    | Sets an integer parameter   |
| `setLong(index, value)`    | 1-based         | Long       | Sets a long parameter       |
| `setDouble(index, value)`  | 1-based         | Double     | Sets a double parameter     |
| `setFloat(index, value)`   | 1-based         | Float      | Sets a float parameter      |
| `setBoolean(index, value)` | 1-based         | Boolean    | Sets a boolean parameter    |
| `setObject(index, value)`  | 1-based         | Any        | Sets an object parameter    |
| `setNull(index, sqlType)`  | 1-based         | -          | Sets a NULL parameter       |
| `clearParameters()`        | -               | -          | Clears all parameter values |

#### Execution Methods

PreparedStatement inherits all execution methods from Statement:

| Method            | Returns   | Description                                |
| ----------------- | --------- | ------------------------------------------ |
| `executeQuery()`  | ResultSet | Executes the prepared SELECT query         |
| `executeUpdate()` | Integer   | Executes the prepared INSERT/UPDATE/DELETE |
| `execute()`       | Boolean   | Executes the prepared statement            |

#### Using PreparedStatements

```ysharp
connection = SQLite.connect("database.db")

// Create a prepared statement with a parameter placeholder
preparedStmt = connection.prepareStatement("SELECT * FROM users WHERE name = ?")

// Set the parameter value
preparedStmt.setString(1, "Alice")

// Execute the query
resultSet = preparedStmt.executeQuery()

if resultSet.next() {
    output("Found user: ", resultSet.getString("name"))
}

resultSet.close()
preparedStmt.close()
connection.close()
```

#### Batch Operations

Execute multiple statements efficiently:

```ysharp
connection = SQLite.connect("database.db")
preparedStmt = connection.prepareStatement("INSERT INTO users (name, age) VALUES (?, ?)")

// Add multiple batches
preparedStmt.setString(1, "Bob")
preparedStmt.setInt(2, 30)
preparedStmt.addBatch()

preparedStmt.setString(1, "Carol")
preparedStmt.setInt(2, 28)
preparedStmt.addBatch()

preparedStmt.setString(1, "David")
preparedStmt.setInt(2, 35)
preparedStmt.addBatch()

// Execute all batches at once
results = preparedStmt.executeBatch()
output("Inserted ", results.length, " rows")

preparedStmt.close()
connection.close()
```

### SQLite.ResultSet

Represents the results of a SELECT query. Use this to iterate through and retrieve data.

#### Cursor Navigation

| Method       | Returns | Description                                             |
| ------------ | ------- | ------------------------------------------------------- |
| `next()`     | Boolean | Moves cursor to next row, returns false if no more rows |
| `previous()` | Boolean | Moves cursor to previous row                            |
| `first()`    | Boolean | Moves cursor to first row                               |
| `last()`     | Boolean | Moves cursor to last row                                |
| `isFirst()`  | Boolean | Checks if cursor is on first row                        |
| `isLast()`   | Boolean | Checks if cursor is on last row                         |
| `close()`    | -       | Closes the ResultSet                                    |

#### Data Retrieval Methods

Get column values by index (1-based) or column name:

| Method                    | Parameters        | Returns           | Description                    |
| ------------------------- | ----------------- | ----------------- | ------------------------------ |
| `getString(index\|name)`  | Integer or String | String            | Retrieves a string value       |
| `getInt(index\|name)`     | Integer or String | Integer           | Retrieves an integer value     |
| `getLong(index\|name)`    | Integer or String | Long              | Retrieves a long value         |
| `getDouble(index\|name)`  | Integer or String | Double            | Retrieves a double value       |
| `getFloat(index\|name)`   | Integer or String | Float             | Retrieves a float value        |
| `getBoolean(index\|name)` | Integer or String | Boolean           | Retrieves a boolean value      |
| `getObject(index\|name)`  | Integer or String | Object            | Retrieves any object value     |
| `getMetaData()`           | -                 | ResultSetMetaData | Gets information about columns |

#### Iterating Through Results

```ysharp
connection = SQLite.connect("database.db")
statement = connection.createStatement()

resultSet = statement.executeQuery("SELECT id, name, email FROM users")

while resultSet.next() {
    // Access columns by index (1-based)
    id = resultSet.getInt(1)
    name = resultSet.getString(2)
    email = resultSet.getString(3)

    output("ID: ", id, ", Name: ", name, ", Email: ", email)
}

resultSet.close()
statement.close()
connection.close()
```

Alternatively, use column names:

```ysharp
while resultSet.next() {
    // Access columns by name
    id = resultSet.getInt("id")
    name = resultSet.getString("name")
    email = resultSet.getString("email")

    output("ID: ", id, ", Name: ", name, ", Email: ", email)
}
```

## Complete Examples

### Creating and Populating a Table

```ysharp
connection = SQLite.connect("myapp.db")
statement = connection.createStatement()

// Create table
statement.executeUpdate(
    "CREATE TABLE IF NOT EXISTS products (" +
    "id INTEGER PRIMARY KEY," +
    "name TEXT NOT NULL," +
    "price REAL NOT NULL," +
    "quantity INTEGER" +
    ")"
)

// Insert data
statement.executeUpdate("INSERT INTO products VALUES (1, 'Laptop', 999.99, 5)")
statement.executeUpdate("INSERT INTO products VALUES (2, 'Mouse', 29.99, 50)")
statement.executeUpdate("INSERT INTO products VALUES (3, 'Keyboard', 79.99, 30)")

output("Products created successfully")

statement.close()
connection.close()
```

### Querying with WHERE Clause

```ysharp
connection = SQLite.connect("myapp.db")
statement = connection.createStatement()

// Find all products under $100
resultSet = statement.executeQuery(
    "SELECT name, price FROM products WHERE price < 100 ORDER BY price DESC"
)

while resultSet.next() {
    name = resultSet.getString("name")
    price = resultSet.getDouble("price")
    output(name, ": $", price)
}

resultSet.close()
statement.close()
connection.close()
```

### Updating with Conditions

```ysharp
connection = SQLite.connect("myapp.db")
statement = connection.createStatement()

// Update a product's quantity
rowsAffected = statement.executeUpdate(
    "UPDATE products SET quantity = 10 WHERE name = 'Laptop'"
)

output("Updated ", rowsAffected, " row(s)")

statement.close()
connection.close()
```

### Using Parameters for Safety

```ysharp
connection = SQLite.connect("myapp.db")

// Prepare a statement with parameters
query = "SELECT * FROM products WHERE name = ? AND price > ?"
preparedStmt = connection.prepareStatement(query)

// Set parameters
preparedStmt.setString(1, "Keyboard")
preparedStmt.setDouble(2, 50.0)

// Execute
resultSet = preparedStmt.executeQuery()

if resultSet.next() {
    output("Found: ", resultSet.getString("name"))
    output("Price: $", resultSet.getDouble("price"))
}

resultSet.close()
preparedStmt.close()
connection.close()
```

### Aggregation and Grouping

```ysharp
connection = SQLite.connect("myapp.db")
statement = connection.createStatement()

// Get product count and average price
resultSet = statement.executeQuery(
    "SELECT COUNT(*) as total, AVG(price) as avg_price FROM products"
)

if resultSet.next() {
    total = resultSet.getInt("total")
    avgPrice = resultSet.getDouble("avg_price")
    output("Total products: ", total)
    output("Average price: $", avgPrice)
}

resultSet.close()
statement.close()
connection.close()
```

## Error Handling

SQLite operations can throw exceptions. Always handle errors appropriately:

```ysharp
try {
    connection = SQLite.connect("database.db")
    statement = connection.createStatement()

    resultSet = statement.executeQuery("SELECT * FROM users")

    while resultSet.next() {
        // Process results
    }

    resultSet.close()
} catch error {
    output("Database error: ", error)
} finally {
    if statement != null {
        statement.close()
    }
    if connection != null {
        connection.close()
    }
}
```

Common errors:

- **File not found**: Database path is invalid
- **SQL syntax error**: Your SQL query has incorrect syntax
- **Table/column not found**: Referenced table or column doesn't exist
- **Type mismatch**: Trying to get wrong data type from a column
- **Connection closed**: Attempting to use closed connection or statement
- **Method overload not found**: Parameter types don't match method signature

## Best Practices

### 1. Always Close Resources

Always close ResultSet, Statement, and Connection objects in the reverse order they were created:

```ysharp
try {
    // Use resources
} catch error {
    output("Error: ", error)
} finally {
    if resultSet != null { resultSet.close() }
    if statement != null { statement.close() }
    if connection != null { connection.close() }
}
```

### 2. Use PreparedStatements for User Input

Always use PreparedStatements when incorporating user input to prevent SQL injection:

```ysharp
// GOOD - Uses PreparedStatement
prepared = connection.prepareStatement("SELECT * FROM users WHERE email = ?")
prepared.setString(1, userEmail)

// DANGEROUS - String concatenation allows SQL injection
resultSet = statement.executeQuery("SELECT * FROM users WHERE email = '" + userEmail + "'")
```

### 3. Use Transactions for Multiple Operations

Wrap multiple related operations in transactions:

```ysharp
connection = SQLite.connect("database.db")
connection.setAutoCommit(false)

try {
    statement = connection.createStatement()

    statement.executeUpdate("INSERT INTO accounts (name) VALUES ('Checking')")
    statement.executeUpdate("INSERT INTO accounts (name) VALUES ('Savings')")

    connection.commit()
} catch error {
    connection.rollback()
    output("Transaction failed: ", error)
} finally {
    statement.close()
    connection.close()
}
```

### 4. Check Results Before Accessing

Always verify that a row exists before accessing data:

```ysharp
resultSet = statement.executeQuery("SELECT * FROM users WHERE id = ?")

if resultSet.next() {
    // Row exists, safe to access
    name = resultSet.getString("name")
} else {
    // Row not found
    output("User not found")
}
```

### 5. Use Connection Pooling for Applications

For applications with multiple concurrent database operations, consider implementing connection pooling (reusing connections) rather than creating new ones frequently.

### 6. Create Appropriate Indexes

For frequently queried columns, create indexes to improve performance:

```ysharp
statement.executeUpdate("CREATE INDEX idx_users_email ON users(email)")
```

## SQLite SQL Reference

SQLite supports standard SQL. Here are some common operations:

### Create Table

```sql
CREATE TABLE table_name (
    column1 TYPE,
    column2 TYPE,
    ...
)
```

### Common Data Types

- `TEXT` - Text strings
- `INTEGER` - Whole numbers
- `REAL` - Floating-point numbers
- `BLOB` - Binary data
- `NULL` - Missing values

### Insert Data

```sql
INSERT INTO table_name (col1, col2) VALUES (val1, val2)
```

### Query Data

```sql
SELECT columns FROM table_name WHERE condition ORDER BY column
```

### Update Data

```sql
UPDATE table_name SET column = value WHERE condition
```

### Delete Data

```sql
DELETE FROM table_name WHERE condition
```

### Aggregate Functions

- `COUNT()` - Number of rows
- `SUM()` - Sum of values
- `AVG()` - Average of values
- `MAX()` - Maximum value
- `MIN()` - Minimum value

## Summary

SQLite in Ysharp provides a complete, type-safe database interface. Remember to:

1. Connect with `SQLite.connect()`
2. Use PreparedStatements for parameterized queries
3. Always close resources
4. Handle errors with try-catch
5. Use transactions for related operations
6. Prefer column names over indices for clarity

For more information about SQLite and SQL syntax, visit [SQLite.org](https://www.sqlite.org/).
