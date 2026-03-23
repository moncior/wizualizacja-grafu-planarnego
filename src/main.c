#include "../include/graph.h"
#include "../include/algorithms.h"
#include "../include/output_handler.h"
#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void help() {}

int main(int argc, char **argv) {
    int opt;
    char *input = NULL;
    char *output = NULL;
    char *format = "txt";
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
        fprintf(stderr, "Blad: Nie podano pliku wejsciowego\n");
        help();
        return 1;
    }

    if (verbose) printf("Info: Tryb verbose wlaczony\n");
    if (verbose) printf("Info: Otwieram plik '%s'\n", input);
    FILE *input_file = fopen(input, "r");
    if (!input_file) {
        fprintf(stderr, "Blad: Nie mozna otworzyc pliku '%s'\n", input);
        return EXIT_FAILURE;
    }

    if (verbose) printf("Info: Wczytuje graf\n");
    graph_t *graph = init_graph();
    load_graph(graph, input);

    if (algorithm == NULL) {
        fprintf(stderr, "Blad: Nie podano nazwy algorytmu\n");
        return EXIT_FAILURE;
    }
    if (!strcmp(algorithm, "fruch_rein")) {
        fruch_rein(graph, verbose);
    } else if (!strcmp(algorithm, "tutte")) {
        tutte(graph, verbose);
    } else {
        fprintf(stderr, "Blad: '%s' nie jest obslugiwanym algorytmem\n", algorithm);
        return EXIT_FAILURE;
    }

    // write_output(output, graph, format, verbose);
    
    printf("Info: Wypisuje graf\n");

    print_graph(graph, output);
    free_graph(graph);

    return EXIT_SUCCESS;
}
