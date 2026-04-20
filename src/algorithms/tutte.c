#include "../../include/algorithms.h"
#include <limits.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

int dfs_cycles(graph_t *graph, int curr, int parent, int *visited, int *path, int depth, cycle_t **cycles,
               int *cycle_count, int *min_cycle_size) {
    visited[curr] = 1;
    path[depth] = curr;

    vertex_t v = graph->vertices[curr];
    for (int i = 0; i < v.degree; i++) {
        int next = v.neighbors[i];

        if (next == parent) continue;

        if (visited[next]) {
            int j = 0;
            while (j <= depth && path[j] != next) {
                j++;
            }

            int size = depth - j + 1;
            if (size < *min_cycle_size) {
                *min_cycle_size = size;

                for (int k = 0; k < *cycle_count; k++) {
                    free(cycles[k]->vertices);
                    free(cycles[k]);
                }
                *cycle_count = 0;

                cycles[*cycle_count] = malloc(sizeof(cycle_t));
                cycles[*cycle_count]->vertices = malloc(size * sizeof(int));
                for (int k = 0; k < size; k++) {
                    cycles[*cycle_count]->vertices[k] = path[j + k];
                }
                cycles[*cycle_count]->n = size;
                (*cycle_count)++;

                if (size == 3) return 1; 
            }
        } else {
            if (dfs_cycles(graph, next, curr, visited, path, depth + 1, cycles, cycle_count, min_cycle_size)) {
                return 1;
            }
        }
    }

    visited[curr] = 0;
    return 0;
}

cycle_t *find_shortest_cycle(graph_t *graph) {
    cycle_t **cycles = malloc(graph->vertex_idx * sizeof(cycle_t *));
    int cycle_count = 0;
    int min_cycle_size = INT_MAX;

    int *visited = calloc(graph->vertex_idx, sizeof(int));
    int *path = malloc(graph->vertex_idx * sizeof(int));

    for (int i = 0; i < graph->vertex_idx; i++) {
        if (!visited[i]) {
            if (dfs_cycles(graph, i, -1, visited, path, 0, cycles, &cycle_count, &min_cycle_size)) {
                break;
            }
        }
    }

    free(visited);
    free(path);

    if (cycle_count == 0) {
        free(cycles);
        cycle_t *result = malloc(sizeof(cycle_t));
        result->vertices = NULL;
        result->n = 0;
        return result;
    }

    cycle_t *result = cycles[0];
    free(cycles);
    return result;
}

void tutte(graph_t *graph, int verbose) {
    cycle_t *cycle = find_shortest_cycle(graph);

    if (!cycle->n) {
        printf("Info: Nie wykryto zadnego cyklu. Algorytm nie moze zadzialac \n");
        free(cycle->vertices);
        free(cycle);
        return;
    }

    if (verbose) {
        printf("Info: Uzywam algorytmu tutte z najkrotszym cyklem o dlugosci %d\n", cycle->n);
    }

    int n = graph->vertex_idx;
    int *fixed = calloc(n, sizeof(int));

    // setting border vertices to be fixed for further calculation
    for (int i = 0; i < cycle->n; i++) {
        int v = cycle->vertices[i];
        fixed[v] = 1;

        int radius = n * 10;
        double angle = 2.0 * 3.1415 / cycle->n * i;

        graph->vertices[v].x = radius * cos(angle);
        graph->vertices[v].y = radius * sin(angle);
    }

    int loops = 100;

    for (int i = 0; i < loops; i++) {
        for (int j = 0; j < n; j++) {
            // moving vertices which are inside the cycle
            if (!fixed[j]) {
                double x = 0.0;
                double y = 0.0;
                vertex_t *v = &graph->vertices[j];

                if (v->degree > 0) {
                    for (int k = 0; k < v->degree; k++) {
                        x += graph->vertices[v->neighbors[k]].x;
                        y += graph->vertices[v->neighbors[k]].y;
                    }
                    v->x = x / v->degree;
                    v->y = y / v->degree;
                }
            }
        }
    }
    if (verbose) printf("Info: zwalniam pamiec algorytmu tutte\n");

    free(cycle->vertices);
    free(cycle);
    free(fixed);
}
