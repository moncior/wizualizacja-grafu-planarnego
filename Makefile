CC = gcc
CFLAGS = -Wall -Wextra -Iinclude
LDFLAGS = -lm

SRC_DIR = src
OBJ_DIR = obj
BIN = program
TEST_OUT = out

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
	rm -rf tests/images tests/output graph.png

alg ?= fruch_rein
idx ?= 1
show_plot ?= 1
format ?= txt

test: all
	mkdir -p tests/images tests/output
	./$(BIN) -va $(alg) -o $(TEST_OUT) -f $(format) tests/input/graph_$(idx).in 
	python3 scripts/visualize.py tests/input/graph_$(idx).in $(TEST_OUT) $(show_plot) $(format)
	mv graph.png tests/images/graph_$(idx)_$(alg).png; \
	mv $(TEST_OUT) tests/output/graph_$(idx)_$(alg).$(format)

test_all:
	mkdir -p tests/images tests/output
	for alg in fruch_rein eades tutte; do \
		for idx in 1 2 3 4 5 6 7 8; do \
			$(MAKE) test alg=$$alg idx=$$idx show_plot=0; \
		done; \
	done

.PHONY: all clean test test_all