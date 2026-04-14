import sys
import networkx as nx
import matplotlib.pyplot as plt

if len(sys.argv) != 3:
    sys.exit(1)

try:
    G = nx.Graph()
    pos = {}
    with open(sys.argv[2], 'r') as f:
        for line in f:
            if line.strip():
                parts = line.split()
                node_id = int(parts[0])
                x, y = float(parts[1]), float(parts[2])
                pos[node_id] = (x, y)
                G.add_node(node_id)

    with open(sys.argv[1], 'r') as f:
        for line in f:
            if line.strip():
                parts = line.split()
                name = parts[0]
                u, v = int(parts[1]), int(parts[2])
                G.add_edge(u, v, weight=name)
    
    nx.draw(G, pos, with_labels=True, node_color='lightblue', node_size=800, font_weight='bold')
    # nx.draw_networkx_edge_labels(G, pos, edge_labels=nx.get_edge_attributes(G, 'weight'))

    plt.axis('off')
    plt.savefig('graph.png')
    print("Graph saved to graph.png")

except Exception as e:
    print(f"Error: {e}")