#include "../include/output_handler.h"

void write_output(FILE *f, graph_t *graph, char *format, int verbose) {
    for (int i = 0; i < graph->vertex_idx; i++) {
        fprintf(f, "%d %.1f %.1f\n", graph->vertices[i].id, graph->vertices[i].x,
                graph->vertices[i].y);
    }
}
