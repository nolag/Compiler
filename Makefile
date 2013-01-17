BXX = ant												# builder

EXEC = joosc
PROJ = Lexer
BIN_PATH = ${PROJ}/bin
CLASS = cs444.lexer.Compiler

all:
	ant -buildfile Lexer/build.xml
	echo "java -cp ${BIN_PATH} ${CLASS}" > ${EXEC}
	chmod u+x joosc

clean:
	rm ${EXEC}
	ant -buildfile Lexer/build.xml cleanall
