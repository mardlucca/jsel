# template-project
This is a template/skeleton project to be used a starting point to create basic Java projects using Gradle.

This project includes:
* TODO: Add description 

Differences between JSEL and ECMAScript 5.1
* match is implemented by all value types, which means that the "match" and
"test" functions in RegExp.prototype can be used with other data types too.
* ToInteger converts to a 32 bit integer in JSEL, while it's not really clear
what size to use in ECMAScript. ToInteger is typically used when dealing with
array indexes, so 32 bits should suffice.
* string conversion to numbers uses Java's Double.parseDouble method, instead of
a proper implementation of the grammar described in the spec.
* NaN, Infinity and undefined are properties in the global object in ECMAScript
while are not only that, they are also tokens in JSEL's grammar. This should not
make any difference, really.
* JSEL does not implement property getters and setters.