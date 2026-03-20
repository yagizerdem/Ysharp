# Ysharp grammar

### expression grammar
- expression &rarr; assignment
- assignment &rarr;  lvalue assignment_op assignment | 
ternary_conditional | 
lambda 
- ternary_conditional &rarr; null_coalescing "?" expression  ":" ternary_conditional 
 | null_coalescing
- null_coalescing &rarr; logical_or ( "??" logical_or )*
- logical_or &rarr; logical_and ( "||" logical_and )*
- logical_and &rarr; bitwise_or ( "&&" bitwise_or )*
- bitwise_or &rarr; bitwise_xor ( "|" bitwise_xor )*
- bitwise_xor &rarr; bitwise_and ( "^" bitwise_and )*
- bitwise_and &rarr; equality ( "&" equality )*
- equality &rarr; comparison ( ( "!=" | "==" ) comparison )*
- comparison &rarr; bitwise_shift ( ( ">" | ">=" | "<" | "<=" ) bitwise_shift )*
- bitwise_shift &rarr; range ( ( ">>" | "<<" ) range )*
- range &rarr; term ( ".." term )?
- term &rarr; factor ( ( "-" | "+" ) factor )*
- factor &rarr; unary ( ( "/" | "\*" | "%" ) unary )*
- unary &rarr;  ( "!" | "-" | "+" | "~" | "++" | "--" ) unary | postfix 
- postfix &rarr; call ( "++" | "--" )*
- call &rarr; primary ( "(" arguments? ")"  | "." IDENTIFIER )*
- primary &rarr; array | map | atom | new_expr
- atom &rarr;
IDENTIFIER |
NUMBER |
STRING |
CHAR |
true |
false |
null |
"(" expression ")"
- array &rarr; "[" (expression ("," expression)*)? "]"
- map &rarr; "{" (STRING ":" expression ("," STRING ":" expression)*)? "}" 
- assignment_op &rarr;  "=" | "+=" | "-="
  | "*=" | "/=" | "%="
  | "<<=" | ">>="
  | "&=" | "^=" | "|="
- lvalue &rarr; postfix
- lambda &rarr; "(" parameters? ")" ( ":" type )? "=>" ( block | expr )
- new_expr &rarr; "new" qualified_name  "(" arguments? ")"
- qualified_name &rarr; IDENTIFIER ("." IDENTIFIER)*

### declaration grammar
- declaration &rarr; classDecl | 
funDecl | 
varDecl | 
constDecl |
useDecl |
exportDecl |
statement 

- classDecl &rarr; ("sealed")? "class" IDENTIFIER ( "extends" IDENTIFIER )?
  "{" classMember* "}"
- funDecl &rarr; "function" function
- varDecl &rarr; "var" IDENTIFIER (":" type )? ("=" expression)? ";"
- constDecl &rarr; "const" IDENTIFIER (":" type )? "=" expression ";"
- useDecl &rarr; "use" STRING ";"
- exportDecl &rarr; "export" ( classDecl | funDecl | varDecl | constDecl )

### statement grammar
- statement &rarr; 
exprStmt | 
forStmt |
foreachStmt |
whileStmt |
tryStmt |
ifStmt |
switchStmt |
printStmt |
printlnStmt |
returnStmt |
breakStmt |
continueStmt |
throwStmt |
block

- block &rarr; "do" declaration* "end"
- exprStmt &rarr; expression ";"
- forStmt &rarr;
( "for" ( varDecl | exprStmt | ";" ) expression? ";" expression?  statement )
| ( "for" varDecl "in" expression statement)
- foreachStmt &rarr "foreach" varDecl "in" expression statement
- whileStmt &rarr;
"while"  expression  statement 

- tryStmt &rarr;
"try" block "catch" "(" IDENTIFIER ")" block  ( "finally" block )?

- ifStmt &rarr; "if"  expression  "then" block
( "elif"  expression  "then" block )*
( "else" block )?

- switchStmt &rarr;
    "switch" expression "do"
    caseClause*
    defaultClause?
    "end"

- caseClause &rarr;
  "case" expression ":" block

- defaultClause &rarr;
  "default" ":" block

- printStmt &rarr; "print" expression ";"
- printlnStmt &rarr; "println" expression ";"
- returnStmt &rarr; "return" expression? ";"
- breakStmt &rarr; "break" ";"
- continueStmt &rarr; "continue" ";"
- throwStmt &rarr; "throw" expression ";"

### utility
- NUMBER &rarr; INT | DOUBLE
- INT &rarr; [0-9]+
- DOUBLE &rarr; [0-9]+.[0-9]+
- function &rarr; IDENTIFIER "(" parameters? ")" ( ":" type )? block 
- parameters &rarr; IDENTIFIER ( ":" type)? ( "," IDENTIFIER ( ":" type)? )* 
- arguments &rarr; expression ( "," expression )*

- classMember &rarr;
  constructor
|  ("static")? function
|  ("static")? varDecl
|  ("static")? constDecl

- constructor &rarr;
"init" "(" parameters? ")" block

- type = "int" | 
"bool" |
"double" | 
"number" |
"string" | 
"char" |
"function" |
"any" |
IDENTIFIER = [class name]

### program
``this is the start point of program``
- program  &rarr; useDecl* declaration* EOF