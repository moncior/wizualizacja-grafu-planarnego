#include "../include/graph.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define INIT_SIZE 16

graph_t *init_graph() {
    graph_t *g = malloc(sizeof(graph_t));
    g->edge_capacity = INIT_SIZE;
    g->vertex_capacity = INIT_SIZE;

    g->edges = malloc(g->edge_capacity * sizeof(edge_t));
    g->vertices = malloc(g->vertex_capacity * sizeof(vertex_t));

    g->edge_idx = 0;
    g->vertex_idx = 0;

    return g;
}

void free_graph(graph_t *graph) {
    for (int i = 0; i < graph->edge_idx; i++) {
        free(graph->edges[i].name);
    }

    for (int i = 0; i < graph->vertex_idx; i++) {
        free(graph->vertices[i].neighbors);
    }

    free(graph->edges);
    free(graph->vertices);
    free(graph);
}

void print_graph(graph_t *graph, FILE *f) {
    for (int i = 0; i < graph->edge_idx; i++) {
        edge_t *edge = &graph->edges[i];
        int src = graph->vertices[edge->src_idx].id;
        int dest = graph->vertices[edge->dest_idx].id;
        double weight = edge->weight;
        char *name = edge->name;

        fprintf(f, "%s: %d --(%g)-> %d \n", name, src, weight, dest);
    }
}

int get_vertex_idx(graph_t *graph, int id) {
    for (int i = 0; i < graph->vertex_idx; i++) {
        if (graph->vertices[i].id == id) {
            return i;
        }
    }

    if (graph->vertex_idx >= graph->vertex_capacity) {
        graph->vertex_capacity *= 2;
        vertex_t *tmp =
            realloc(graph->vertices, sizeof(vertex_t) * graph->vertex_capacity);
        if (!tmp) { free(graph->vertices); return -1; }
        graph->vertices = tmp;
    }

    int new_idx = graph->vertex_idx;
    graph->vertices[new_idx].id = id;
    graph->vertices[new_idx].x = (double)(rand() % 100);
    graph->vertices[new_idx].y = (double)(rand() % 100);
    graph->vertices[new_idx].dx = 0;
    graph->vertices[new_idx].dy = 0;
    graph->vertices[new_idx].degree = 0;

    graph->vertex_idx++;

    return new_idx;
}

void load_graph(graph_t *graph, FILE *f) {
    char edge_name[64];
    int src, dest;
    double weight;

    while (fscanf(f, "%63s %d %d %lf", edge_name, &src, &dest, &weight) == 4) {
        int src_idx = get_vertex_idx(graph, src);
        int dest_idx = get_vertex_idx(graph, dest);

        if (graph->edge_idx >= graph->edge_capacity) {
            graph->edge_capacity *= 2;
            edge_t *tmp =
                realloc(graph->edges, sizeof(edge_t) * graph->edge_capacity);
            if (!tmp) { free(graph->edges); return; }
            graph->edges = tmp;
        }

        edge_t *edge = &graph->edges[graph->edge_idx];
        edge->name = strdup(edge_name);
        if (!edge->name) return;
        edge->src_idx = src_idx;
        edge->dest_idx = dest_idx;
        graph->vertices[src_idx].degree++;
        graph->vertices[dest_idx].degree++;
        edge->weight = weight;

        graph->edge_idx++;
    }

    for (int i = 0; i < graph->vertex_idx; i++) {
        graph->vertices[i].neighbors = malloc(graph->vertices[i].degree * sizeof(int));
        graph->vertices[i].degree = 0;
    }

    for (int i = 0; i < graph->edge_idx; i++) {
        int src_idx = graph->edges[i].src_idx;
        int dest_idx = graph->edges[i].dest_idx;

        graph->vertices[src_idx].neighbors[graph->vertices[src_idx].degree++] = dest_idx;
        graph->vertices[dest_idx].neighbors[graph->vertices[dest_idx].degree++] = src_idx;
    }
}

int is_planar(graph_t *graph) {
    int v = graph->vertex_idx;
    int e = graph->edge_idx;
    if (v < 3) return 1;
    if (e > 3 * (v - 2)) return 0;
    return 1;
}
