BXX = ant												# builder

all: run_generator build_compiler

run_generator:
	ant -buildfile Generator/build.xml build-project-without-test Program

build_compiler:
	ant -buildfile Compiler/build.xml build-project-without-test

clean:
	ant -buildfile Compiler/build.xml cleanall
	ant -buildfile Generator/build.xml cleanall

run_tests: clean
	ant -buildfile Generator/build.xml build-project-without-test Program
	ant -buildfile Compiler/build.xml build
	cd Compiler/ && ant tests && chmod u+x a5-test && all_tests=true ./a5-test

submit:
	git archive --format zip --output jooscompiler.zip master
# marmosetsubmit CS444 A2 jooscompiler.zip
