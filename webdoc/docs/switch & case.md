---
sidebar_position: 13
---


### General

Unlike if-then and if-then-else statements, 
the switch statement can have a number of possible execution paths. 

Based on a condition, switch selects one or more code blocks to be executed.
switch executes the code blocks that matches an expression.
switch is often used as a more readable alternative to 
many if...else if...else statements, especially when dealing with multiple possible values.


````
var month = 8;
var monthString;

switch month do
    case 1 : do
        monthString = "January";
        break;
    end
    case 2 : do
        monthString = "February";
        break;
    end
    case 3 : do
        monthString = "March";
        break;
    end
    case 4 : do
        monthString = "April";
        break;
    end
    case 5 : do
        monthString = "May";
        break;
    end
    case 6 : do
        monthString = "June";
        break;
    end
    case 7 : do
        monthString = "July";
        break;
    end
    case 8 : do
        monthString = "August";
        break;
    end
    case 9 : do
        monthString = "September";
        break;
    end
    case 10 : do
        monthString = "October";
        break;
    end
    case 11 : do
        monthString = "November";
        break;
    end
    case 12 : do
        monthString = "December";
        break;
    end
    default : do
        monthString = "Invalid month";
    end
end

println monthString;
````

In this case, August is printed to standard output.



You could also display the name of the month with if-then-else statements:

````
var month = 8;
var monthString;

if month == 1 then do
    monthString = "January";
end
elif month == 2 then do
    monthString = "February";
end
elif month == 3 then do
    monthString = "March";
end
elif month == 4 then do
    monthString = "April";
end
elif month == 5 then do
    monthString = "May";
end
elif month == 6 then do
    monthString = "June";
end
elif month == 7 then do
    monthString = "July";
end
elif month == 8 then do
    monthString = "August";
end
elif month == 9 then do
    monthString = "September";
end
elif month == 10 then do
    monthString = "October";
end
elif month == 11 then do
    monthString = "November";
end
elif month == 12 then do
    monthString = "December";
end
else do
    monthString = "Invalid month";
end

println monthString;
````

Deciding whether to use if-then-else statements or a switch statement is
based on readability and the expression that the statement is testing. An if-then-else statement can test expressions based on ranges of values or conditions, 
whereas a switch statement tests expressions based only on a single integer, enumerated value, or String object.


### break & fallthrough


Another point of interest is the break statement. Each break statement
terminates the enclosing switch statement. Control flow continues with the
first statement following the switch block. The break statements are
necessary because without them, statements in switch blocks fall through:
All statements after the matching case label are executed in sequence,
regardless of the expression of subsequent case labels, until a break
statement is encountered. The program SwitchDemoFallThrough shows statements 
in a switch block that fall through. The program displays the month corresponding
to the integer month and the months that follow in the year:

````ysharp
var futureMonths = [];

var month = 8;

switch month do
    case 1 : do futureMonths.add("January"); end
    case 2 : do futureMonths.add("February"); end
    case 3 : do futureMonths.add("March"); end
    case 4 : do futureMonths.add("April"); end
    case 5 : do futureMonths.add("May"); end
    case 6 : do futureMonths.add("June"); end
    case 7 : do futureMonths.add("July"); end
    case 8 : do futureMonths.add("August"); end
    case 9 : do futureMonths.add("September"); end
    case 10 : do futureMonths.add("October"); end
    case 11 : do futureMonths.add("November"); end
    case 12 : do
        futureMonths.add("December");
        break;
    end
    default : do
        break;
    end
end


if futureMonths.isEmpty() then do
    println "Invalid month number";
end
else do
    foreach var m in futureMonths do
        println m;
    end
end

/*
This is the output from the code:

August
September
October
November
December

*/
````

Technically, the final break is not required because flow falls out of the switch statement.
Using a break is recommended so that modifying the code is easier and less
error prone. The default section handles all values that are not
explicitly handled by one of the case sections.

The following code example, SwitchDemo2, shows how a statement can have multiple case labels.
The code example calculates the number of days in a particular month:

````ysharp
var month = 2;
var year = 2000;
var numDays = 0;

switch month do
    case 1 : do end
    case 3 : do end
    case 5 : do end
    case 7 : do end
    case 8 : do end
    case 10 : do end
    case 12 : do
        numDays = 31;
        break;
    end

    case 4 : do end
    case 6 : do end
    case 9 : do end
    case 11 : do
        numDays = 30;
        break;
    end

    case 2 : do
        if ((year % 4 == 0 && !(year % 100 == 0)) || (year % 400 == 0)) then do
            numDays = 29;
        end
        else do
            numDays = 28;
        end
        break;
    end

    default : do
        println "Invalid month.";
    end
end

println "Number of Days = " + numDays;

/*
This is the output from the code:

Number of Days = 29
*/
````


### Using Strings in switch Statements

In Ysharp, the switch statement supports dynamic types, including strings. 
This means that a string value can be used directly as the switch expression without any special requirements.

The following example demonstrates how a string based switch statement can be used 
to determine the numeric value of a month based on its name:

````ysharp
function getMonthNumber(month) do
    var monthNumber = 0;

    if month == null then do
        return monthNumber;
    end

    switch month.toLower() do
        case "january" : do monthNumber = 1; break; end
        case "february" : do monthNumber = 2; break; end
        case "march" : do monthNumber = 3; break; end
        case "april" : do monthNumber = 4; break; end
        case "may" : do monthNumber = 5; break; end
        case "june" : do monthNumber = 6; break; end
        case "july" : do monthNumber = 7; break; end
        case "august" : do monthNumber = 8; break; end
        case "september" : do monthNumber = 9; break; end
        case "october" : do monthNumber = 10; break; end
        case "november" : do monthNumber = 11; break; end
        case "december" : do monthNumber = 12; break; end
        default : do monthNumber = 0; end
    end

    return monthNumber;
end



var month = "August";

var returnedMonthNumber = getMonthNumber(month);

if returnedMonthNumber == 0 then do
    println "Invalid month";
end
else do
    println returnedMonthNumber;
end

/*
The output from this code is 8.
*/

````