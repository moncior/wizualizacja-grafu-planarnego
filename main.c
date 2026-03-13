#include "graph.h"
#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>

void help() {}

int main(int argc, char **argv) {
    int opt;
    char *input = NULL;
    char *output = NULL;
    char *format = NULL;
    char *algorithm = NULL;
    int verbose = 0;

    while ((opt = getopt(argc, argv, "vhf:o:a:")) != -1) {
        switch (opt) {
        case 'v': verbose = 1; break;
        case 'h': help(); return 0;
        case 'f': format = optarg; break;
        case 'o': output = optarg; break;
        case 'a': algorithm = optarg; break;
        default: help(); return 1;
        }
    }
    if (optind < argc) {
        input = argv[optind];
    } else {
        fprintf(stderr, "Blad: nie podano pliku wejsciowego\n");
        help();
        return 1;
    }

    if (verbose) {
        printf("> Tryb verbose wlaczony\n");
        printf("> Plik wejsciowy:   %s\n", input);
        printf("> Algmorytm:        %s\n", algorithm);
        printf("> Format wyjściowy: %s\n", format);
        printf("> Plik wyjsciowy:   %s\n", output);
    }

    FILE *input_file = fopen(input, "r");
    if (!input_file) {
        fprintf(stderr, "Blad: nie mozna otworzyc pliku '%s'\n", input);
        return EXIT_FAILURE;
    }

    graph_t *graph = init_graph();
    load_graph(graph, input);
    print_graph(graph, output);
    free_graph(graph);

    return EXIT_SUCCESS;
}
