import sys
edges = []
with open(sys.argv[1], "r") as f:
    for line in f:
        edges.append(line.strip())
with open("output_graph.txt", "w") as f:
    for i, edge in enumerate(edges):
        f.write(f"e{i} {edge} 1.0\n")

