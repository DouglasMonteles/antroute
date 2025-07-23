# Variables
PROJECT_EXECUTABLE_JAR = agents/target/agents-3.4.5.jar

# Commands
.PHONY:
	clean
	build-and-run

build:
	@echo "Building project with maven"
	./mvnw clean install

up:
	@echo "Running project with last build created"
	java -jar $(PROJECT_EXECUTABLE_JAR)

build-up:
	@echo "Building and running project"
	make clean build up

test:
	@echo "Running tests"
	./mvnw test

test-module:
	@echo "Running tests of a specific module"
	./mvnw test -pl $(module)

clean:
	@echo "Cleaning old builds"
	./mvnw clean

help:
	@echo "============================================================================="
	@echo "Build and running"
	@echo -e "Build project:\t\t\t\t\t\t make build"
	@echo -e "Execute project:\t\t\t\t\t make up"
	@echo -e "Build and run project:\t\t\t\t\t make build-up"
	@echo -e "Clear builds (remove target directory of all modules):\t make clean"
	@echo "-----------------------------------------------------------------------------"
	@echo "Tests"
	@echo -e "Run tests in all project:\t make test"
	@echo -e "Run tests in specific module:\t make test-module module=<module-name>"
	@echo "============================================================================="