package ex1.src;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class WGraph_DS implements weighted_graph {
	private HashMap<Integer, node_info> graph;
	private int edges;
	private int MC;
	
	//class which implements the vertices in the graph
	private class NodeInfo implements node_info {
		private int key;
		private String information;
		private double tag;
		private node_info parent;
		//Hash map which contains the vertices connected by edges to this vertex, and the weight
		//of the edge between them
		private HashMap<node_info, Double> neighbors;


		public NodeInfo(int key) {
			this.key = key;
			information = "W";
			tag = Integer.MAX_VALUE;
			parent = null;
			neighbors = new HashMap<>();
		}

		@Override
		public int getKey() {
			return key;
		}

		@Override
		public String getInfo() {
			return information;
		}

		@Override
		public void setInfo(String s) {
			information = s;
		}

		@Override
		public double getTag() {
			return tag;
		}

		@Override
		public void setTag(double t) {
			this.tag = t;
		}

		/**
		 * Function which returns the parent of a node
		 * @return node_info parent
		 */
		public node_info getParent() {
			return parent;
		}
		/**
		 * Function which sets the parent of a node
		 * @param n
		 */
		public void setParent(node_info n) {
			parent = n;
		}

		/**
		 * Function which adds a vertex to the neighboring vertices
		 * @param node - the node we are attaching by an edge
		 * @param w - the weights of the edge
		 */
		public void addNei(node_info node, double w) {
			neighbors.put(node, w);
		}

		/**
		 * Function which removes a vertex from the neighboring vertices
		 * @param node - the vertex to be removed
		 */
		public void removeNei(node_info node) {
			neighbors.remove(node);
		}

		/**
		 * Function which checks if a node is a neighbor to our node
		 * @param node - the node that is being checked
		 * @return true - if the node is a neighbor, false if he is not
		 */
		public boolean hasNei(node_info node) {
			return neighbors.containsKey(node);
		}

		/**
		 * Function which returns the wight of the edge which connects our node to the given node
		 * @param node - the neighboring node
		 * @return double - weight of the edge connecting, -1 if there is no edge
		 */
		public double getWeight(node_info node) {
			if (hasNei(node))
				return neighbors.get(node);
			else
				return -1;
		}

		/**
		 * Function which returns a collection of the neighboring vertices of our vertex
		 * @return Collection<node_info> -  of the neighboring vertices of our vertex
		 */
		public Collection<node_info> getNei() {
			return neighbors.keySet();
		}
		
		/**
		 * Function which returns true if the given node is identical to our node via his key,
		 * false if they are not identical.
		 */
		@Override
		public boolean equals(Object object) {
			if(!(object instanceof node_info)) return false;
			if(((node_info)object).getKey() != this.getKey()) return false;
			else return true;
		}

	}

	
	public WGraph_DS() {
		graph = new HashMap<>();
		edges = 0;
		MC = 0;
	}

	/**
	 * Function which returns the node which is specified to a certain given key
	 * return null if the node does not exist in the graph.
	 */
	@Override
	public node_info getNode(int key) {
		if (!graph.containsKey(key))
			return null;
		return graph.get(key);
	}

	/**
	 * Function which checks if there is an edge between the two given keys associated nodes
	 * return true if there is an edge, false if there isn't
	 */
	@Override
	public boolean hasEdge(int node1, int node2) {
		if (!graph.containsKey(node1) || !graph.containsKey(node2) || node1 == node2)
			return false;
		return ((NodeInfo) graph.get(node1)).hasNei(graph.get(node2));
	}

	/**
	 * Function which returns the weight of an edge between the two given keys associated nodes
	 * return the weight, -1 if there isn't an edge
	 */
	@Override
	public double getEdge(int node1, int node2) {
		if (!graph.containsKey(node1) || !graph.containsKey(node2) || !hasEdge(node1, node2))
			return -1;
		return ((NodeInfo) graph.get(node1)).getWeight(graph.get(node2));
	}

	/**
	 * Function which adds a node with the given key
	 * if a node with the same key exists do nothing
	 */
	@Override
	public void addNode(int key) {
		// In case of collision do nothing
		if (graph.containsKey(key) || key < 0) {
			return;
		}
		graph.put(key, new NodeInfo(key));
		MC++;
	}

	/**
	 * Function which connects two nodes associated with the given keys,
	 * 	and sets the weight of the edge weight.
	 * if there aren't nodes associated with the given keys or an edge already exists,
	 * 	the  function will do nothing
	 */
	@Override
	public void connect(int node1, int node2, double w) {
		if (!graph.containsKey(node1) || !graph.containsKey(node2) || hasEdge(node1, node2) || node1 == node2)
			return;
		((NodeInfo) graph.get(node1)).addNei(graph.get(node2), w);
		((NodeInfo) graph.get(node2)).addNei(graph.get(node1), w);
		edges++;
		MC++;
	}

	/**
	 * Function which returns a collection of the nodes in the graph
	 */
	@Override
	public Collection<node_info> getV() {
		return graph.values();
	}

	/**
	 * Function which returns all the vertices which are connected to the node associated with the given id,
	 * return null if there are'nt any neighboring vertices
	 */
	@Override
	public Collection<node_info> getV(int node_id) {
		return ((NodeInfo) graph.get(node_id)).getNei();
	}

	/**
	 * Function which removes a node from the graph.
	 * first remove our node from his neighbors's neighbors lists,
	 * then remove the node from the graph
	 * return the deleted node, id there is not such a node in the graph return null
	 */
	@Override
	public node_info removeNode(int key) {
		if (!graph.containsKey(key))
			return null;
		node_info myNode = graph.get(key);
		if (((NodeInfo) myNode).getNei() != null) {
			Iterator<node_info> iterator = ((NodeInfo) myNode).getNei().iterator();
			while (iterator.hasNext()) {
				node_info run = iterator.next();
				((NodeInfo) run).removeNei(myNode);
				iterator.remove();
				edges--;
				MC++;
			}
		}
		graph.remove(key);
		MC++;
		return myNode;
	}

	/**
	 * Function which removes an edge between to vertices
	 * if the edge does not exist, do nothing
	 */
	@Override
	public void removeEdge(int node1, int node2) {
		if (!graph.containsKey(node1) || !graph.containsKey(node2) || node1 == node2)
			return;
		((NodeInfo)graph.get(node1)).removeNei(graph.get(node2));
		((NodeInfo)graph.get(node2)).removeNei(graph.get(node1));
		edges--;
		MC++;
	}

	/**
	 * Function which returns the number of nodes in the graph
	 */
	@Override
	public int nodeSize() {
		return graph.size();
	}

	/**
	 * Function which returns the number of edges in the graph
	 */
	@Override
	public int edgeSize() {
		return edges;
	}

	/**
	 * Function which counts the number of changes made in the graph
	 */
	@Override
	public int getMC() {
		return MC;
	}

	/**
	 * Function which returns a parent of a certain node
	 * @param node - the given node
	 * @return node_info - the parent of the node
	 */
	public node_info getParent(node_info node) {
		return ((NodeInfo) node).getParent();
	}

	/**
	 * Function which sets a parent of a certain node
	 * @param node - the node we want to set his parent
	 * @param parent - the node we want to set as the parent
	 */
	public void setParent(node_info node, node_info parent) {
		if (graph.containsValue(parent))
			((NodeInfo) node).setParent(parent);
	}
	
	
	/**
	 * Function which writes the graph into a file, using print writer
	 * @param fileName - the name of the file to write the graph to.
	 * @return true - if the graph was successfully written to the file
	 */
	public boolean writeFile(String fileName) {
		// try write to the file
		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter outs = new PrintWriter(fw);
			outs.println("Vertices:");
			boolean first = true;
			for (node_info vtr : graph.values()) {
				if (first) {
					outs.print(vtr.getKey());
					first = false;
				} else
					outs.print("," + vtr.getKey());
			}
			outs.println(";");
			outs.println("Edges:");
			for (node_info num : graph.values()) {
				if(((NodeInfo)num).getNei() != null) {
					first = true;
					for (node_info num1 : ((NodeInfo)num).getNei()) {
						if (first) {
							outs.print(
									num.getKey() + "-" + getEdge(num1.getKey(), num.getKey()) + "->" + num1.getKey());
							first = false;
						} else
							outs.print(" , " + num.getKey() + "-" + getEdge(num1.getKey(), num.getKey()) + "->"
									+ num1.getKey());

					}
					outs.println(";");
				}
			}

			outs.println("There are: " + edges + " edges");
			outs.close();
			fw.close();
		}

		catch (IOException ex) {
			return false;
		}
		return true;
	}
	
	/**
	 * Function which checks if another graph is identical to this graph
	 * returns true if they are identical
	 */
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof weighted_graph)) return false;
		weighted_graph g2 = ((WGraph_DS)object);
		if(g2.edgeSize() != edgeSize() || g2.nodeSize() != nodeSize())
			return false;
		
		//check vertices
		for(node_info node : g2.getV()) {
			if(!graph.containsKey(node.getKey())) 
				return false;
			if(g2.getV(node.getKey()) != null && this.getV(node.getKey()) != null) {
				if(this.getV(node.getKey()).size() != g2.getV(node.getKey()).size())
					return false;
				for(node_info nei : g2.getV(node.getKey())) {
					if(!this.hasEdge(node.getKey(),nei.getKey()) || this.getEdge(node.getKey(), nei.getKey()) != g2.getEdge(node.getKey(), nei.getKey()))
						return false;
				}
			}
			if(g2.getV(node.getKey()) == null && this.getV(node.getKey()) != null)
				return false;
		}
		
		return true;
	}

	



}
