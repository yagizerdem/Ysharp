# Ysharp grammar

### expression grammar
- expression &rarr; assignment
- assignment &rarr;  lvalue assignment_op assignment | ternary_conditional
- ternary_conditional &rarr; logical_or "?" expression  ":" ternary_conditional 
 | logical_or
- logical_or &rarr; logical_and ( "||" logical_and )*
- logical_and &rarr; bitwise_or ( "&&" bitwise_or )*
- bitwise_or &rarr; bitwise_xor ( "|" bitwise_xor )*
- bitwise_xor &rarr; bitwise_and ( "^" bitwise_and )*
- bitwise_and &rarr; equality ( "&" equality )*
- equality &rarr; comparison ( ( "!=" | "==" ) comparison )*
- comparison &rarr; bitwise_shift ( ( ">" | ">=" | "<" | "<=" ) bitwise_shift )*
- bitwise_shift &rarr; term ( ( ">>" | "<<" ) term )*
- term &rarr; factor ( ( "-" | "+" ) factor )*
- factor &rarr; unary ( ( "/" | "\*" | "%" ) unary )*
- unary &rarr;  ( "!" | "-" | "+" | "~" | "++" | "--" ) unary | postfix 
- postfix &rarr; call ( "++" | "--" )*
- call &rarr; primary ( "(" arguments? ")"  | "." IDENTIFIER )*
- primary &rarr; array | map | atom 
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

### declaration grammar
- declaration &rarr; classDecl | 
funDecl | 
varDecl | 
constDecl | 
statement 

- classDecl &rarr; "class" IDENTIFIER ( "extends" IDENTIFIER )?
  "{" classMember* "}"
- funDecl &rarr; "function" function
- varDecl &rarr; "var" IDENTIFIER (":" type )? ("=" expression)? ";"
- constDecl &rarr; "const" IDENTIFIER (":" type )? "=" expression ";"

### statement grammar
- statement &rarr; 
exprStmt | 
forStmt |
whileStmt |
tryStmt |
ifStmt |
switchStmt |
printStmt |
printlnStmt |
returnStmt |
breakStmt |
continueStmt |
useStmt |
block

- block &rarr; "do" declaration* "end"
- exprStmt &rarr; expression ";"
- forStmt &rarr;
"for" "("
( varDecl | exprStmt | ";" )
expression? ";"
expression?
")"
statement

- whileStmt &rarr;
"while" "(" expression ")" statement 

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
- useStmt &rarr; "use" STRING ";"

### utility
- NUMBER &rarr; INT | DOUBLE
- INT &rarr; [0-9]+
- DOUBLE &rarr; [0-9]+.[0-9]+
- function &rarr; IDENTIFIER "(" parameters? ")" block 
- parameters &rarr; IDENTIFIER ( "," IDENTIFIER )* 
- arguments &rarr; expression ( "," expression )*

- classMember &rarr;
function
| varDecl
| constDecl

- type = "int" | 
"double" | 
"string" | 
"char" |
"fun" |
IDENTIFIER