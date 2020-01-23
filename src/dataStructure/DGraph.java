package dataStructure;

import java.util.Collection;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class DGraph implements graph{
	
	private HashMap<Integer,node_data> vertexs;
	private HashMap<Integer,HashMap<Integer,edge_data>> edges;
	int modeCount;
	int numberOfEdges;
	
	public DGraph() {
		vertexs = new HashMap<>();
		edges = new HashMap<>();
		modeCount = 0;
		numberOfEdges = 0;
	}
	
	public void init(String g) {
		try {
			JSONObject line = new JSONObject(g);
			JSONArray nodesArr = line.getJSONArray("Nodes");
			JSONArray edgesArr = line.getJSONArray("Edges");
			
			for (int i = 0; i < nodesArr.length(); i++) { 
				int id = nodesArr.getJSONObject(i).getInt("id");
				
				String pos = nodesArr.getJSONObject(i).getString("pos"); //the vertex's position
				String[] location = pos.split(",");
				double x = Double.parseDouble(location[0]);
				double y = Double.parseDouble(location[1]);
				double z = Double.parseDouble(location[2]);
				utils.Point3D p = new Point3D(x,y,z);
				
				addNode(new Vertex(id, p));
			}
			for (int i = 0; i < edgesArr.length(); i++) {
				int src = edgesArr.getJSONObject(i).getInt("src");
				int dest = edgesArr.getJSONObject(i).getInt("dest");
				double w = edgesArr.getJSONObject(i).getDouble("w");
				connect(src, dest, w);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public node_data getNode(int key) {
		return this.vertexs.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(!this.edges.containsKey(src))
			return null;
		return this.edges.get(src).get(dest);
	}
 
	@Override
	public void addNode(node_data n) {
		//If already exists the old value is replaced
		this.vertexs.put(n.getKey(), n);
		modeCount++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(!this.vertexs.containsKey(src) || !this.vertexs.containsKey(dest)) {
			throw new RuntimeException("source or destination does not exist");
		}
		if(getEdge(src, dest)!=null) {
			throw new RuntimeException("the edge already exists");
		}
		if(!this.edges.containsKey(src)) {
			this.edges.put(src, new HashMap<Integer,edge_data>());
		}
		edge_data e = new Edge(src, dest, w);
		this.edges.get(src).put(dest, e);
		modeCount++;
		numberOfEdges++;
	}

	@Override
	public Collection<node_data> getV() {
		return this.vertexs.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if (this.edges.containsKey(node_id)) {
			return edges.get(node_id).values();
		}
		return null;
	}

	@Override
	public node_data removeNode(int key) {
		if(!this.vertexs.containsKey(key))
			return null;
		if (this.edges.containsKey(key)) {
			modeCount += getE(key).size();
			numberOfEdges -= getE(key).size();
			this.edges.remove(key);
		}
		for(node_data v: getV()) { // run in O(n), |vertexs|=n
			removeEdge(v.getKey(), key);
		}
		modeCount++;
		return this.vertexs.remove(key);
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if (this.edges.containsKey(src) && this.edges.get(src).containsKey(dest)) {
			modeCount++;
			numberOfEdges--;
			return this.edges.get(src).remove(dest); 
		}
		return null;
	}

	@Override
	public int nodeSize() {
		return this.vertexs.size();
	}

	@Override
	public int edgeSize() {
		return this.numberOfEdges;
	}

	@Override
	public int getMC() {
		return this.modeCount;
	}

}
