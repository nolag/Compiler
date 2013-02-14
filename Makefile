BXX = ant												# builder

PROJ = Lexer
BIN_PATH = ${PROJ}/bin
CLASS = cs444.lexer.Compiler

all:
	ant -buildfile Generator/build.xml build Program
	ant -buildfile Compiler/build.xml build

clean:
	ant -buildfile Lexer/build.xml cleanall
