#include "../../include/algorithms.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define REP 10.0
#define ATTR 10.0

#define ITERATIONS 200
#define EPS 1e-6

void eades(graph_t *graph, int verbose) {
    if (verbose) printf("Info: Uzywam algorytmu Eades\n");
    int n = graph->vertex_idx;
    int width = n * n;
    int height = width;
    double cooling = 1.0;
    // set initial positions
    for (int i = 0; i < n; i++) {
        graph->vertices[i].x = (double)(rand() % width);
        graph->vertices[i].y = (double)(rand() % height);
    }
    for (int iter = 0; iter < ITERATIONS; iter++) {
        // repulsion
        for (int i = 0; i < graph->vertex_idx; i++) {
            for (int j = 0; j < graph->vertex_idx; j++) {
                if (i == j) continue;
                double dx = graph->vertices[i].x - graph->vertices[j].x;
                double dy = graph->vertices[i].y - graph->vertices[j].y;
                double dist2 = dx * dx + dy * dy + EPS;
                double dist = sqrt(dist2);

                double force = REP / dist2;
                graph->vertices[i].dx += (dx / dist) * force;
                graph->vertices[i].dy += (dy / dist) * force;
            }
        }
        // attraction
        for (int i = 0; i < graph->edge_idx; i++) {
            int u = graph->edges[i].src_idx;
            int v = graph->edges[i].dest_idx;

            double dx = graph->vertices[u].x - graph->vertices[v].x;
            double dy = graph->vertices[u].y - graph->vertices[v].y;
            double dist = sqrt(dx * dx + dy * dy) + EPS;

            double force = -ATTR * log(dist / graph->edges[i].weight);
            graph->vertices[u].dx += (dx / dist) * force;
            graph->vertices[u].dy += (dy / dist) * force;
            graph->vertices[v].dx -= (dx / dist) * force;
            graph->vertices[v].dy -= (dy / dist) * force;
        }
        // update
        for (int i = 0; i < graph->vertex_idx; i++) {
            graph->vertices[i].x += cooling * graph->vertices[i].dx;
            graph->vertices[i].dx = 0;
            graph->vertices[i].y += cooling * graph->vertices[i].dy;
            graph->vertices[i].dy = 0;
        }
        cooling *= 0.99;
    }

    // scale positions to 0-100
    double min_x = 1e9, max_x = -1e9, min_y = 1e9, max_y = -1e9;
    for (int i = 0; i < graph->vertex_idx; i++) {
        if (graph->vertices[i].x < min_x) min_x = graph->vertices[i].x;
        if (graph->vertices[i].x > max_x) max_x = graph->vertices[i].x;
        if (graph->vertices[i].y < min_y) min_y = graph->vertices[i].y;
        if (graph->vertices[i].y > max_y) max_y = graph->vertices[i].y;
    }
    double range_x = max_x - min_x;
    double range_y = max_y - min_y;
    if (range_x == 0) range_x = 1;
    if (range_y == 0) range_y = 1;
    for (int i = 0; i < graph->vertex_idx; i++) {
        graph->vertices[i].x = 100.0 * (graph->vertices[i].x - min_x) / range_x;
        graph->vertices[i].y = 100.0 * (graph->vertices[i].y - min_y) / range_y;
    }
}