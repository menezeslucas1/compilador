PROJ_NAME = mpi
CC = javac

OBJ_DIR = obj
SRC_DIR = src

OBJ = $(SRC:src/%.java=obj/%.class)
SRC = $(wildcard src/*.java)

LINK =
CFLAGS =
INCLUDE =


$(PROJ_NAME): $(OBJ_DIR) $(SRC)
	cd $(SRC_DIR); javac -d ../$(OBJ_DIR) $(PROJ_NAME).java
	cd $(OBJ_DIR); jar cfm mpi.jar manifest.txt mpi.class lexical syntatic semantic

.PHONY: clean
clean:
	rm -f ./$(OBJ_DIR)/*.o $(PROJ_NAME)

$(OBJ_DIR):
	mkdir $(OBJ_DIR)

parametros = teste.mp

run: $(PROJ_NAME)
	./$(PROJ_NAME) $(parametros)
