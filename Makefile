CC = gcc
CFLAGS = -Wall -Wextra -Iinclude
LDFLAGS = -lm

SRC_DIR = src
OBJ_DIR = obj
BIN = program
TEST_OUT = meow

SRCS = $(shell find $(SRC_DIR) -name "*.c")
OBJS = $(SRCS:$(SRC_DIR)/%.c=$(OBJ_DIR)/%.o)

all: $(BIN)

$(BIN): $(OBJS)
	$(CC) $(OBJS) -o $@ $(LDFLAGS)

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.c
	@mkdir -p $(dir $@)
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm -rf $(OBJ_DIR) $(BIN) $(TEST_OUT)

alg ?= fruch_rein
idx ?= 1

test: all
	./$(BIN) tests/graph_$(idx).in -va $(alg) -o $(TEST_OUT)
	python3 scripts/visualize.py tests/graph_$(idx).in $(TEST_OUT)

.PHONY: all clean test