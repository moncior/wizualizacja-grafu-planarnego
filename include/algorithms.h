#ifndef ALGORITHMS_H
#define ALGORITHMS_H

#include "graph.h"

void fruch_rein(graph_t *graph, int verbose);
void eades(graph_t *graph, int verbose);
void tutte(graph_t *graph, int verbose);

typedef struct {
    int *vertices;
    int n;
} cycle_t;

#endif