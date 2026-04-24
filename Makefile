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

INPUT_DIR := tests/input
INPUT_FILES := $(sort $(wildcard $(INPUT_DIR)/*))

test: all
	@mkdir -p tests/images tests/output
	$(eval IN_FILE := $(word $(idx), $(INPUT_FILES)))
	$(eval BASE_NAME := $(notdir $(basename $(IN_FILE))))

	./$(BIN) -va $(alg) -o $(TEST_OUT) -f $(format) -i $(IN_FILE)
	
	python3 scripts/visualize.py $(IN_FILE) $(TEST_OUT) $(show_plot) $(format)

	mv graph.png tests/images/$(BASE_NAME)_$(alg).png
	mv $(TEST_OUT) tests/output/$(BASE_NAME)_$(alg).$(format)

test_all:
	@mkdir -p tests/images tests/output
	@n=1; \
	for alg in fruch_rein eades tutte; do \
		for file in $(INPUT_FILES); do \
			$(MAKE) test alg=$$alg idx=$$n show_plot=0 format=$(format); \
			n=$$((n + 1)); \
		done; \
		n=1; \
	done

.PHONY: all clean test test_all