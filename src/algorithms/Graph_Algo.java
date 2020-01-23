package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import dataStructure.*;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms{

	private graph graph;
	private final int NOT_YET = 0, VISITED = 1;
	
	public Graph_Algo() {
		
	}

	@Override
	public void init(graph g) {
		this.graph = g;
	}

	@Override
	public void init(String file_name) {
		try {
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream objIn = new ObjectInputStream(file);
			graph g = (DGraph)objIn.readObject();
			init(g);
			objIn.close(); 
			file.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

	@Override
	public void save(String file_name) {
		try {
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream objOut = new ObjectOutputStream(file);
			objOut.writeObject(this.graph); 
			objOut.close(); 
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isConnected() {
		
		for (node_data n : graph.getV()) {
			n.setTag(NOT_YET);
		}
		int randomVertex = graph.getV().iterator().next().getKey();
		DFS(graph, randomVertex);
		for (node_data n : graph.getV()) {
			if(n.getTag()==NOT_YET)
				return false;
		}
		
		graph reverseGraph = reverse();
		for (node_data n : reverseGraph.getV()) {
			n.setTag(NOT_YET);
		}
		DFS(reverseGraph, randomVertex);
		for (node_data n : reverseGraph.getV()) {
			if(n.getTag()==NOT_YET)
				return false;
		}
		
		return true;
	}
	
	public void DFS(graph g ,int src) {
		g.getNode(src).setTag(VISITED);
		for (edge_data outgoingEdges : g.getE(src)) {
			if ((g.getNode(outgoingEdges.getDest())).getTag() == NOT_YET) {
				DFS(g, outgoingEdges.getDest());
			}
		}
		
	}
	
	public graph reverse() {
		graph reverse = new DGraph();
		for (node_data n : this.graph.getV()) {
			node_data copyN = new Vertex(n);
			reverse.addNode(copyN);
		}
		for (node_data n : this.graph.getV()) {
			for (edge_data e : this.graph.getE(n.getKey())) {
				reverse.connect(e.getDest(), e.getSrc(), e.getWeight());
			}
		}
		return reverse;
		
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if(this.graph.getNode(src) == null || this.graph.getNode(dest) == null) {
			throw new RuntimeException("source or destination does not exist");
		}
		
		for (node_data n : graph.getV()) {
			n.setTag(NOT_YET);
			n.setWeight(Double.MAX_VALUE);
			n.setInfo("");
		}
		
		double verWeight;
		double edgeWeight;
		double tempWeight;
		
		PriorityQueue <node_data> unvisited = new PriorityQueue<>();
		this.graph.getNode(src).setWeight(0);
		unvisited.add(this.graph.getNode(src));
		
		while (!unvisited.isEmpty()) {
			node_data n = unvisited.poll();
			for (edge_data edge_data : this.graph.getE(n.getKey())) {
				verWeight = n.getWeight();
				node_data next = this.graph.getNode(edge_data.getDest());
				edgeWeight = edge_data.getWeight();
				if (this.graph.getNode(edge_data.getDest()).getTag() != 1)
				{
					tempWeight = edgeWeight + verWeight;
					if (tempWeight < this.graph.getNode(edge_data.getDest()).getWeight())
					{
						unvisited.remove(next);
						next.setWeight(tempWeight);
						next.setInfo("" + n.getKey());
						unvisited.add(next);
					}
				}
			}
		}
		return this.graph.getNode(dest).getWeight();
		
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {

		List<node_data> pathList = new ArrayList <>();
		if(src == dest) {
			pathList.add(this.graph.getNode(src));
			return pathList;
		}
		
		if(Double.MAX_VALUE > this.shortestPathDist(src,dest)) {
			node_data n = this.graph.getNode(dest);
			pathList.add(n);
			int prev;
			while(n.getInfo()!="") {
				prev = Integer.parseInt(n.getInfo());
				pathList.add(this.graph.getNode(prev));
				n = this.graph.getNode(prev);
			}
			Collections.reverse(pathList);
			return pathList;
		}
		
		return null;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		if (targets.isEmpty()) 
			return null;
		
		List<node_data> ans = new ArrayList<node_data>();
		ans.add(this.graph.getNode(targets.get(0)));
		if (targets.size()==1) {
			return ans;
		}
		List<node_data> tempPath = new ArrayList<node_data>();
		
		for(int i=0;i<targets.size()-1;i++) {
			tempPath = shortestPath(targets.get(i),targets.get(i+1));
			if (tempPath!=null) {
				tempPath.remove(0);
				for (node_data n : tempPath) {
					ans.add(n);
				}
				tempPath.clear();	
			}
		}
		return ans;
		
	}

	@Override
	public graph copy() {
		graph copy = new DGraph();
		for (node_data n : this.graph.getV()) {
			node_data copyN = new Vertex(n);
			copy.addNode(copyN);
		}
		for (node_data n : this.graph.getV()) {
			for (edge_data e : this.graph.getE(n.getKey())) {
				copy.connect(e.getSrc() , e.getDest() , e.getWeight());
				edge_data copyE = copy.getEdge(e.getSrc(), e.getDest());
				copyE.setInfo(e.getInfo());
				copyE.setTag(e.getTag());
			}
		}
		return copy;
	}

}
