# dsl-parser

## Synopsis

This repository provides an example of how to create dynamic external DSLs for a Rules Engine. The code example is for a dynamic web form backed by a rules engine which calculates which "form" to next display to the user based on questions they have already answered. 

## How the DSL parser works

The parser uses a collection of keywords associated with rules and domain objects. When an input string matches a predefined pattern of keywords, it will call the underlying rule. The underlying rule can then be passed data from the engine (this can be some state metadata, or the data captured by the form session), which will be evaluated.
