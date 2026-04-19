#include "../../include/algorithms.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

void fruch_rein(graph_t *graph, int verbose) {
    if (verbose) printf("Info: Uzywam algorytmu Fruchterman-Reingold\n");

    int n = graph->vertex_idx;
    int m = graph->edge_idx;

    int width = n * n;
    int height = width;

    for (int i = 0; i < n; i++) {
        graph->vertices[i].x = (double)(rand() % width);
        graph->vertices[i].y = (double)(rand() % height);
    }

    double C = 1.5;
    double t = (double)width / (n + 1);
    double k = C * sqrt((double)(width * height) / (n + 1));

    double **forces = malloc(n * sizeof(double *));
    if (!forces) return;

    for (int i = 0; i < n; i++) {
        forces[i] = malloc(2 * sizeof(double));
        if (!forces[i]) { for (int j = 0; j < i; j++) free(forces[j]); free(forces); return; }
        forces[i][0] = 0.0;
        forces[i][1] = 0.0;
    }
    int loops = 100;

    for (int iteration = 0; iteration < loops; iteration++) {

        // reset to zero
        for (int i = 0; i < n; i++) {
            forces[i][0] = 0.0;
            forces[i][1] = 0.0;
        }

        // repulsion force
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                double dx = graph->vertices[i].x - graph->vertices[j].x;
                double dy = graph->vertices[i].y - graph->vertices[j].y;

                double d = sqrt(dx * dx + dy * dy) + 1e-6;
                double repulsion_force = (k * k) / d;
                forces[i][0] += (dx / d) * repulsion_force;
                forces[i][1] += (dy / d) * repulsion_force;
            }
        }

        // attractive force
        for (int i = 0; i < m; i++) {
            int source_idx = graph->edges[i].src_idx;
            int dest_idx = graph->edges[i].dest_idx;

            vertex_t *source = &graph->vertices[source_idx];
            vertex_t *dest = &graph->vertices[dest_idx];

            double dx = source->x - dest->x;
            double dy = source->y - dest->y;

            double d = sqrt(dx * dx + dy * dy) + 1e-9;
            double attraction_force = (d * d) / k;

            forces[source_idx][0] -= (dx / d) * attraction_force;
            forces[source_idx][1] -= (dy / d) * attraction_force;

            forces[dest_idx][0] += (dx / d) * attraction_force;
            forces[dest_idx][1] += (dy / d) * attraction_force;
        }

        // update position

        for (int i = 0; i < n; i++) {
            vertex_t *vertex = &graph->vertices[i];
            double jump =
                sqrt(forces[i][0] * forces[i][0] + forces[i][1] * forces[i][1]) +
                1e-9;

            double move = (jump < t) ? jump : t;
            vertex->x += (forces[i][0] / jump) * move;
            vertex->y += (forces[i][1] / jump) * move;

            if (vertex->x < 0) vertex->x = 0;
            if (vertex->y < 0) vertex->y = 0;
            if (vertex->x > width) vertex->x = width;
            if (vertex->y > height) vertex->y = height;
        }
        t *= 0.95;
    }

    if (verbose) printf("Info: zwalniam pamiec algorytmu fruchterman-reingold\n");

    for (int i = 0; i < n; i++) {
        free(forces[i]);
    }
    free(forces);
}