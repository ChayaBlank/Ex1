# Ex1
**Graphs**

Written by chaya blank.

Using interfaces node_info, weighted_graph and weighted_graph_algorithms written by proff. Boaz Ben - Moshe.

**General description**

The classes I have written execute a graph of vertices which are represented by nodes, and edges that connect between the vertices, so as every edge has a weight.
With the classes you can build a graph, add vertices and connect them.
Also, you can delete nodes or edges, and get information about number of nodes in the graph, whether a vertex is in the graph, the number of edges and the number of changes in the graph.

Moreover, you can check if there is an edge between two given vertices, get the weight of the edge, find the shortest distance and the shortest path between two given vertices, according to weighted paths between them.
Implementation by data structures
The graph is represented by a hash map in which the key is an integer that is the node's number, and the value is the node itself.
The edges are also represented by a hash map in which the key are the neighboring nodes, and the value is the weight of the edge between them. To connect two nodes by an edge, the nodes are added to each other's hash maps which represent their neighbors, and the given weight would be the value.

**Algorithms**

1. ```init(graph g)``` - function which initializes the algorithms on a given graph.
The function runs in O(1) time.

2. ```copy()``` - function which creates a new graph by deep copy
The function runs in O(vertices*edges) time.

3. ```isConnected()``` - function which checks if a graph is connected, i.e. there is a path from every vertex to every other vertex in the graph.
The function runs in O(vertices*edges) time.

4. ```shortestPathDist(int src, int dest)``` - function which retrieves the distance of the shortest path between two nodes which their keys are the given integers, i.e. the weight of edges between the two vertices.
The function runs in O(vertices*edges) time.

5. ```shortestPath(int src, int dest)``` - function which returns a list representing the shortest path between two nodes which their keys are the given integers.
The function runs in O(vertices*edges) time.
 
 
**Example**


```

public static void main(String[] args) {
	weighted_graph g = new WGraph_DS();
	g.addNode(1);
	g.addNode(2);
	g.addNode(3);
	g.addNode(4);
	g.addNode(5);
	g.connect(1, 2, 1);
	g.connect(1, 3, 2);
	g.connect(1, 4, 3);
	g.connect(1, 5, 4);
	g.connect(2, 5, 3);
	g.connect(3, 5, 2);
	System.out.println(g.hasEdge(1, 3));// true
	System.out.println(g.hasEdge(2, 3));// false
	System.out.println("number of edges: "+g.edgeSize());//6

	// Initialize the graph for the algorithms
	graph_algorithms ga = new Graph_Algo();
	ga.init(g);

	// print all the vertices
	Collection<node_data> vertices = g.getV();
	System.out.print("The vertices in the graph:");
	for (node_data node : vertices) {// print keys of: 1,2,3,4,5
		System.out.print(node.getKey() + ", ");
	}
	System.out.println();
	
	//check if a graph is connected
	System.out.println(ga.isConnected());// true
	g.addNode(6);
	System.out.println(ga.isConnected());// false
	
	//remove node
	g.removeNode(5);
	System.out.println("number of nodes: "+g.nodeSize());// print: 4
	
	//print shortest path and distance
	System.out.println("shortest path distance: " +ga.shortestPathDist(2,3));// print 3
	List<node_data> list = ga.shortestPath(2,3);
	System.out.print("the shortest path: " );
	for (node_data node : list) {// print: 2 --> 1 --> 3
		System.out.print(node.getKey() + " --> ");
	}
	System.out.println();
	

	// print the neighbors of a node
	Collection<node_data> nei1 = g.getV(1);
	System.out.print ("neighbors of 1: ");
	for (node_data node : nei1) {// print: 2,3,4,5
		System.out.print(node.getKey() + ", ");
	}
	System.out.println();
	
	// remove an edge
	g.removeEdge(1,2);
	nei1 = g.getV(1);
	System.out.print("neighbors of 1: " );
	for (node_data node : nei1) {// print keys of: 3,4,5
		System.out.print(node.getKey() + ", ");
	}
	System.out.println();
}

Output:
has edge: true
has edge: false
number of edges: 6
The vertices in the graph: 1, 2 ,3, 4, 5
connected: true
connected: false
number of nodes: 4
shortest path distance: 3
the shortest path: 2 --> 1 --> 3
neighbors of 1: 2,3,4,5
neighbors of 1: 3,4,5
```


