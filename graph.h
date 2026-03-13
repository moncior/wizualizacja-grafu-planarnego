#ifndef GRAPH_H
#define GRAPH_H

#include <stdio.h>

typedef struct {
    char *name;
    int src_idx, dest_idx;
    double weight;
} edge_t;

typedef struct {
    int id;
    double x, y;
} vertex_t;

typedef struct {
    vertex_t *vertices;
    edge_t *edges;
    int edge_idx;
    int vertex_idx;
    int edge_capacity;
    int vertex_capacity;
} graph_t;

graph_t *init_graph();
void free_graph(graph_t *graph);
void print_graph(graph_t *graph, char *filename);
void load_graph(graph_t *graph, char *filename);
int get_vertex_idx(graph_t *graph, int id);

#endif
