BXX = ant												# builder

PROJ = Lexer
BIN_PATH = ${PROJ}/bin
CLASS = cs444.lexer.Compiler

all:
	ant -buildfile Lexer/build.xml

clean:
	ant -buildfile Lexer/build.xml cleanall
