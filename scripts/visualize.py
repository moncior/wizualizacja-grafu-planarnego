import sys
import networkx as nx
import matplotlib.pyplot as plt
import matplotlib
import struct

if len(sys.argv) != 5:
    sys.exit(1)

try:
    G = nx.Graph()
    pos = {}
    read = "rb" if sys.argv[4] == "bin" else "r"
    with open(sys.argv[2], read) as f: 
        if read == "rb":
            while True:
                chunk = f.read(20)
                
                if not chunk:
                    break
                
                node_id, x, y = struct.unpack('<idd', chunk)
                pos[node_id] = (x, y)
                G.add_node(node_id)
        else:
            for line in f:
                if line.strip():
                    parts = line.split()
                    node_id = int(parts[0])
                    x, y = float(parts[1]), float(parts[2])
                    pos[node_id] = (x, y)
                    G.add_node(node_id)

    with open(sys.argv[1], "r") as f:
        for line in f:
            if line.strip():
                parts = line.split()
                name = parts[0]
                u, v = int(parts[1]), int(parts[2])
                G.add_edge(u, v, weight=name)

    nx.draw(
        G,
        pos,
        with_labels=True,
        node_color="lightblue",
        node_size=800,
        font_weight="bold",
    )
    # nx.draw_networkx_edge_labels(G, pos, edge_labels=nx.get_edge_attributes(G, 'weight'))

    plt.axis("off")
    plt.savefig("graph.png")
    if sys.argv[3] == "1":
        plt.show()

    plt.close("all")
        
    print("Graph saved to graph.png")

except Exception as e:
    print(f"Error: {e}")
