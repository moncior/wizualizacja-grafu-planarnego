#include "../include/output_handler.h"
#include <string.h>

void write_output(FILE *f, graph_t *graph, char *format) {
    for (int i = 0; i < graph->vertex_idx; i++) {
        if (!strcmp(format, "txt")) {
            fprintf(f, "%d %.1f %.1f\n", graph->vertices[i].id, graph->vertices[i].x, graph->vertices[i].y);
        } else if (!strcmp(format, "bin")) {
            fwrite(&(graph->vertices[i].id), sizeof(int), 1, f);
            fwrite(&(graph->vertices[i].x), sizeof(graph->vertices[i].x), 1, f);
            fwrite(&(graph->vertices[i].y), sizeof(graph->vertices[i].y), 1, f);
        }
    }
}
