package dataStructure;

import java.util.Collection;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gameClient.MyGameGUI;
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
			new DGraph();
			double x=0;
			double y=0;
			double xmin=Integer.MAX_VALUE;
			double ymin= Integer.MAX_VALUE;
			double xmax=Integer.MIN_VALUE;
			double ymax= Integer.MIN_VALUE;

			JSONObject graph = new JSONObject(g);
			JSONArray nodesArr = graph.getJSONArray("Nodes");
			JSONArray edgesArr = graph.getJSONArray("Edges");

			for (int i = 0; i < nodesArr.length(); ++i) {//find min x&y foe the scale func
				String pos = nodesArr.getJSONObject(i).getString("pos");
				try {
					MyGameGUI.km.addPlaceMark("node", pos);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String[] location = pos.split(",");
				x=Double.parseDouble(location[0]);
				if(x<xmin) {
					xmin=x;
				}
				if(x>xmax) {
					xmax=x;
				}

				y=Double.parseDouble(location[1]);
				if(y<ymin) {
					ymin=y;
				}		
				if(y>ymax) {
					ymax=y;
				}
			}

			for (int i = 0; i < nodesArr.length(); ++i) {
				int id = nodesArr.getJSONObject(i).getInt("id");
				String pos = nodesArr.getJSONObject(i).getString("pos");
				String[] str = pos.split(",");
				x=Double.parseDouble(str[0]);

				int xp=(int)scale(x, xmin, xmax, 0+40, 1300-40);
				y=Double.parseDouble(str[1]);
				int yp=(int)scale(y, ymin, ymax, 0+80, 700-40);
				
				Point3D p = new Point3D(xp,720-yp);

				addNode(new Vertex(id, p));
			}
			for (int i = 0; i < edgesArr.length(); ++i) {
				int src = edgesArr.getJSONObject(i).getInt("src");
				int dest = edgesArr.getJSONObject(i).getInt("dest");
				double w = edgesArr.getJSONObject(i).getDouble("w");

				String pos1 = nodesArr.getJSONObject(src).getString("pos");
				String pos2 = nodesArr.getJSONObject(dest).getString("pos");
				try {	
					MyGameGUI.km.Place_Mark_edge(pos2, pos1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				connect(src, dest, w);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
		return ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
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
