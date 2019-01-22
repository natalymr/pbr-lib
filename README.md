# PathMiner
A tool/library for mining of [path-based representations of code](https://arxiv.org/pdf/1803.09544.pdf).
*Work in progress.*

## About
Currently it supports extraction of path-based representations from code in Java and Python, but it is designed to be very easily extensible. 

A new programming language can be supported in a few simple steps:
1. Add the corresponding [ANTLR4 grammar file](https://github.com/antlr/grammars-v4) to the `antlr` directory;
2. Run the `antlr4` Gradle task to generate the parser;
3. Implement a very minimal wrapper around the generated parser.
See Java8Parser or PythonParser class for reference.

A few simple usage examples can be run with `./gradlew run`. 

A somewhat more verbose example of usage in Java is available as well in `examples/AllJavaFiles.java`.

The default output format is inspired by [code2vec](https://github.com/tech-srl/code2vec).

## Contribution
We believe that, thanks to extensibility, PathMiner could be valuable for many other researchers. 
However, our vision of potential applications is tunneled by our own work. 

Please help make PathMiner easier to use by sharing your potential use cases. 
We would also appreciate pull requests with code improvements, more usage examples, documentation, etc. 
