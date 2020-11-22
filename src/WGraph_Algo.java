package ex1.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class WGraph_Algo implements weighted_graph_algorithms {
	private weighted_graph graph;
	
	public WGraph_Algo() {
		graph = new WGraph_DS();
	}
	
	public WGraph_Algo(weighted_graph graph) {
		this.graph = graph;
	}
	
	/**
	 * Function which initializes the given graph fot our graph
	 */
	@Override
	public void init(weighted_graph g) {
		this.graph = g;
	}

	/**
	 * function which returns our graph
	 */
	@Override
	public weighted_graph getGraph() {
		return graph;
	}

	/**
	 * Function which creates a deep copy of our graph and returns it.
	 * it creates the deep copy first by creating new nodes to an empty graph which contain
	 * identical keys to our graph, then connect all the edges and there weights between
	 * every two nodes which are connected in this graph
	 */
	@Override
	public weighted_graph copy() {
		weighted_graph newGraph = new WGraph_DS();
		for(node_info cur : graph.getV()) {
			newGraph.addNode(cur.getKey());
		}
		
		for(node_info run : graph.getV()) {
			for(node_info node : ((WGraph_DS)graph).getV(run.getKey())) {
				if(!newGraph.hasEdge(run.getKey(), node.getKey()))
					newGraph.connect(run.getKey(), node.getKey(), graph.getEdge(run.getKey(), node.getKey()));
			}
		}
		
		return newGraph;
	}

	/**
	 * Function which checks if this graph is connected, using the BFS method, by tagging and adding
	 * every node to a queue so that if a node has not been tagged "B" it has not been reached
	 * and therefore the graph would not be connected. Else, the graph is connected and the method
	 * would return true.
	 */
	@Override
	public boolean isConnected() {
		weighted_graph temp = copy();
		if(graph.getV()==null || graph.getV().size()==1) return true;
		Queue <node_info> q = new LinkedList<>();
		for(node_info cur : temp.getV()) {
			q.add(cur);
			cur.setInfo("G");
			break;
		}
		
		while(!q.isEmpty()) {
			node_info cur = q.poll();
			for(node_info help : temp.getV(cur.getKey())) {
				if(help.getInfo() == "W") {
					help.setInfo("G");
					q.add(help);
				}
			}
			cur.setInfo("B");
		}
		
		boolean flag = true;
		for(node_info cur : temp.getV()) {
			if(cur.getInfo() != "B") flag = false;
		}
		return flag;
	}

	/**
	 * Function which returns the shortest path distance between two given keys associated with
	 * nodes in the graph.
	 * The method calculates the shortest path using the BFS method, by calculating the weight
	 * between any two connected nodes, it adds all the nodes to a priority queue which sorts
	 * the queue according to the tags which represents the shortest path between the speific node
	 * and the source node, and every time it finds a shorter path it changes the length accordingly.
	 * The method will run until we have found the destination node or until we have reached all
	 * the connected nodes to the source node.
	 * If we have found the node, return it's tag which represents the shortest path,
	 * if we have not found him return -1.
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(graph.getNode(src) == null || graph.getNode(dest) == null) return -1;
		if(src == dest) return 0;
		weighted_graph cGraph = copy();
		PriorityQueue <node_info> q = new PriorityQueue<>(new compareTag());
		cGraph.getNode(src).setTag(0);
		q.add(cGraph.getNode(src));
		node_info help;
		while(!q.isEmpty()) {
			help = q.poll();
			for(node_info run : cGraph.getV(help.getKey())) {
				double t = help.getTag() + cGraph.getEdge(help.getKey(), run.getKey());
				if(run.getInfo() == "W" || run.getTag() > t) {
					q.removeIf(n -> n.getKey() == run.getKey());
					run.setTag(t);
					run.setInfo("G");
					q.add(run);
				}
			}
			help.setInfo("B");
			if(help.getKey() == dest) return help.getTag();
		}
		return -1;
	}

	/**
	 * Function which returns a list representing the shortest path between two given keys associated with
	 * nodes in the graph. The head of the list is the source node and the tail is the destination node.
	 * The method calculates the shortest path using the BFS method, by calculating the weight
	 * between any two connected nodes, and saving the node it came from as it's parent.
	 * It adds all the nodes to a priority queue which sorts the queue according to the tags,
	 * which represents the shortest path between the specific node and the source node.
	 * Every time it finds a shorter path it changes the length accordingly,
	 * and also saves the node it came from as the parent node.
	 * The method will run until we have found the destination node or until we have reached all
	 * the connected nodes to the source node.
	 * Finally, if we have found the node, we will run from the destination node, through the parents,
	 * until we get to the source node (his parent is null), and add all the nodes in the path to a list.
	 * return the list. If the method did not encounter the destination node return null.
	 * If we have found the node, return it's tag which represents the shortest path,
	 * if we have not found him return null.
	 */
	@Override
	public List<node_info> shortestPath(int src, int dest) {
		if(graph.getNode(src) == null || graph.getNode(dest) == null) return null;
		LinkedList <node_info> list = new LinkedList<node_info>();
		if(src == dest) {
			list.add(graph.getNode(src));
			return list;
		}
		
		PriorityQueue <node_info> q = new PriorityQueue<>(new compareTag());
		graph.getNode(src).setTag(0);
		q.add(graph.getNode(src));
		node_info help;
		while(!q.isEmpty()) {
			help = q.poll();
			for(node_info run : graph.getV(help.getKey())) {
				double t = help.getTag() + graph.getEdge(help.getKey(), run.getKey());
				if(run.getInfo() != "B") {
					if(run.getTag() > t) {
						run.setTag(t);
						((WGraph_DS)graph).setParent(run, help);
					}
					if(run.getInfo() == "W") {
						((WGraph_DS)graph).setParent(run, help);
						run.setInfo("G");
					}
					q.removeIf(n -> n.getKey() == run.getKey());
					q.add(run);
				}
			}
			help.setInfo("B");
			if(help.getKey() == dest) {
				while(((WGraph_DS)graph).getParent(help) != null) {
					list.addFirst(help);
					help = ((WGraph_DS)graph).getParent(help);
				}
				list.addFirst(help);
				return list;
			}
		}
		return null;
	}

	/**
	 * Function which saves this graph to a file using WGraph_DS's function to write to files.
	 * return true if the file was successfully written
	 */
	@Override
	public boolean save(String file) {
		return ((WGraph_DS)graph).writeFile("myGraph");
	}

	/**
	 * Function which loads a graph from a file using the read file function to read from files,
	 * and replaces it with our graph.
	 * return true if the file was successfully loaded.
	 */
	@Override
	public boolean load(String file) {
		weighted_graph g2 = readFile("myGraph");
		if(g2 == null)
			return false;
		this.graph = g2;
		return true;
	}
	
	/**
	 * Function which reads a graph from a given file and creates it, according to the way we
	 * saved it in WGraph_DS's write file function.
	 * @param fileName - the name of the file to read from
	 * @return graph which represents the graph in the file, null if the graph was not read successfully
	 */
	public weighted_graph readFile(String fileName) {
		// try read from the file
		try {
			weighted_graph graph = new WGraph_DS();
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			br.readLine();
			String str;
			str = br.readLine();
			int index = 0;
			while (index < str.length()) {
				String myNum = "";
				while (index < str.length() && str.charAt(index) != ',' && str.charAt(index) != ';') {
					myNum += str.charAt(index);
					index++;
				}
				graph.addNode(Integer.parseInt(myNum));
				index++;
			}
			br.readLine();
			str = br.readLine();
			while (str != null && str.charAt(0) != 'T') {
				index = 0;
				while (index < str.length()) {
					String myFirstNum = "";
					String mySecondNum = "";
					String myWeight = "";
					while (index < str.length() && str.charAt(index) != ';' && str.charAt(index) != ' ' && str.charAt(index) != ',') {
						while (index < str.length() && str.charAt(index) != '-') {
							myFirstNum += str.charAt(index);
							index++;
						}
						index++;

						while (index < str.length() && str.charAt(index) != '-') {
							myWeight += str.charAt(index);
							index++;
						}

						index += 2;
						while (index < str.length() && str.charAt(index) != ' ' && str.charAt(index) != ';') {
							mySecondNum += str.charAt(index);
							index++;
						}

						graph.connect(Integer.parseInt(myFirstNum), Integer.parseInt(mySecondNum),
								Double.parseDouble(myWeight));

					}
					index++;
				}
				str = br.readLine();
			}
			
			br.close();
			fr.close();
			return graph;
		} 
		catch (IOException ex) {
			return null;
		}
		catch(NumberFormatException ex) {
			return null;
		}
		catch(NullPointerException ex) {
			return null;
		}
	}
	
	
	/**
	 * class which is used to compare between two tags in a node.
	 *
	 */
	private class compareTag implements Comparator<node_info>{
		@Override
		public int compare(node_info o1, node_info o2) {
			if(o1.getTag() > o2.getTag()) return 1;
			else if(o1.getTag() < o2.getTag()) return -1;
			return 0;
		}
		
	}
	
	/**
	 * Function which restarts the state of all the nodes tags, infos and parents in the graph,
	 * to their default settings.
	 */
	public void resetartNodes() {
		for(node_info node : graph.getV()) {
			node.setInfo("W");
			node.setTag(0);
			((WGraph_DS)graph).setParent(node, null);
		}
	}
	
	

}
