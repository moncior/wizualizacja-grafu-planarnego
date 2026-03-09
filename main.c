#include "graph.h"
#include <getopt.h>
#include <stdio.h>

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
    case 'v':
      verbose = 1;
      break;
    case 'h':
      help();
      return 0;
    case 'f':
      format = optarg;
      break;
    case 'o':
      output = optarg;
      break;
    case 'a':
      algorithm = optarg;
      break;
    default:
      help();
      return 1;
    }
  }
  if (optind < argc) {
    input = argv[optind];
  } else {
    fprintf(stderr, "Blad: nie podano pliku wejsciowego\n");
    help();
    return 1;
  }

  printf("verbose %d\n", verbose);
  printf("format %s\n", format);
  printf("input %s\n", input);
  printf("output %s\n", output);
  printf("algmorith %s\n", algorithm);

  graph_t *graph = init_graph();
  load_graph(graph, input);
  print_graph(graph, output);
  free_graph(graph);

  return 0;
}
