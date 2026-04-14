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
	./$(BIN) tests/input/graph_$(idx).in -va $(alg) -o $(TEST_OUT)
	python3 scripts/visualize.py tests/input/graph_$(idx).in $(TEST_OUT)
	mv $(TEST_OUT) tests/output/graph_$(idx)_$(alg).txt
	mv graph.png tests/images/graph_$(idx)_$(alg).png

test_all:
	for alg in fruch_rein eades; do \
		for idx in 1 2 3 4 5 6 7 8; do \
			$(MAKE) test alg=$$alg idx=$$idx; \
		done; \
	done

.PHONY: all clean test test_all