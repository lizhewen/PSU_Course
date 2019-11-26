# Expression Tree Evaluation Program
###### Author: Eric Zhewen Li

###### Contact: eric@zhewenli.com

## Project Description

This project was originally written as Project 2 for [CMPSC-461](https://bulletins.psu.edu/search/?scontext=courses&search=cmpsc+461) of Spring 2019 at Penn State University, taught by [Professor Mahmut Kandemir](http://www.cse.psu.edu/hpcl/kandemir/).

For this project, my program builds an expression tree and evaluate the result of the expression for a given expression. The program also supports both in-order and post-order and will automatically generate the right output.

This program contains two (virtual) functions, `build_expression_tree(args)` and `evaluate_expression_tree(args)`. The project implements the following programming language concepts:

- Pure Abstract Class / Interface
- Inheritance with Interfaces

## How to Use

The default usage is to run the executable shell file `execution.sh` and it will use the default input file `test.txt` and output the calculated results, showing the original input, its corresponding in-order (or post-order) expressions and the evaluated result (rounded to two decimal places).