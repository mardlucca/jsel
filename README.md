# JavaScritp Expression Language - JSEL
This is a JavaScript (or ECMAScript 5.1) expression language written in Java. Especifically, this allows you to parse and evaluate JavaScript expressions given an initial execution context containing the regular objects present in the language (e.g. Math, Object, String, Date, etc) as well as user supplied objects, JSON or user libraries, provided by the caller. 

The number of applications for a tool like this is pretty much limitless. This could be used, for example, to write a command-line tool to parser/processing JSON files (similar to tools like "jq"), or it could be used in JSON configuration files, allowing for a richer configuration of values, etc...

# Differences between JSEL and ECMAScript 5.1

* JSEL does not support statements!
* JSEL includes support for lambdas, as specified in EMCAScript 6. This is the only feature from ECMAScript 6 present in JSEL.
* [[Match]]" is implemented by all value types, which means that the "match" and
"test" functions in RegExp.prototype can be used with other data types too.
* ToInteger converts to a 32 bit integer in JSEL, while it's not really clear
what size to use in ECMAScript. ToInteger is typically used when dealing with
array indexes, so 32 bits should suffice.
* string conversion to numbers uses Java's Double.parseDouble method, instead of
a proper implementation of the grammar described in the spec. This in fact is true in many aspects of the built-in library, where implementation is backed by its Java equivalent counterpart, which at times may vary slightly from what is specified in the ECMAScript 5.1 spec 
* JSEL does not implement property getters and setters.
* Array indexes in ECMAScript can go up to 2^32 - 1, while in JSEL, as in Java,
they can go up to 2^31 - 1. (This may be revisited in the future)

# NOTICE
This is a work is progress. Specifically, the code is not very well documented at the moment (I have added barely any documentation, really) and the builtin libraries are not fully tested. Additionally, at this point I've done no real optimizations and/or performance testing of this, while I'm aware of at least two or three optimization areas that still need to be done. Optimization work is something I mean or may not do, depending on how serious I get about this stuff.
