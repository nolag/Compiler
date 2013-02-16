BXX = ant												# builder

PROJ = Lexer
BIN_PATH = ${PROJ}/bin
CLASS = cs444.lexer.Compiler

all: run_generator build_compiler

run_generator:
	ant -buildfile Generator/build.xml build-project-without-test Program

build_compiler:
	ant -buildfile Compiler/build.xml build-project-without-test

clean:
	ant -buildfile Compiler/build.xml cleanall
	ant -buildfile Generator/build.xml cleanall

submit:
	git archive --format zip --output jooscompiler.zip master
# marmosetsubmit CS444 A2 jooscompiler.zip
