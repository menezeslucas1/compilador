PROJ_NAME = mpi
CC = javac

OBJ_DIR = obj
SRC_DIR = src
EXE_DIR = exe

OBJ = $(SRC:src/%.java=obj/%.class)
SRC = $(wildcard src/*.java) $(wildcard src/**/*.java)


$(EXE_DIR)/$(PROJ_NAME).jar: $(OBJ_DIR) $(EXE_DIR) $(OBJ)
	cd $(SRC_DIR); javac -d ../$(OBJ_DIR) $(PROJ_NAME).java
	cd $(OBJ_DIR); jar cfm ../$(EXE_DIR)/mpi.jar manifest.txt mpi.class lexical syntatic semantic

.PHONY: clean
clean:
	rm -f ./$(OBJ_DIR)/*.o $(PROJ_NAME)
 
$(EXE_DIR):
	mkdir $(EXE_DIR)

$(OBJ_DIR):
	mkdir $(OBJ_DIR)

parametros = teste.mp

run: $(EXE_DIR)/$(PROJ_NAME).jar
	echo $(SRC)
	java -jar $(EXE_DIR)/$(PROJ_NAME).jar $(parametros)

teste:
	echo $(SRC)
	echo $(OBJ)
