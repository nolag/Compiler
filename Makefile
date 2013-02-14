BXX = ant												# builder

PROJ = Lexer
BIN_PATH = ${PROJ}/bin
CLASS = cs444.lexer.Compiler

all: run_generator build_compiler

run_generator:
	ant -buildfile Generator/build.xml build Program

build_compiler:
	ant -buildfile Compiler/build.xml build

clean:
	ant -buildfile Lexer/build.xml cleanall
