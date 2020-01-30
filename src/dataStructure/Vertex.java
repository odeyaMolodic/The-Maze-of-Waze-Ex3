package dataStructure;

import static org.junit.jupiter.api.Assertions.assertThrows;

import dataStructure.node_data;
import utils.Point3D;

public class Vertex<T> implements node_data, Comparable<T>{
	
	private int _key;
	private Point3D _location;
	private double _weight;
	private String _info;
	private int _tag;
	
	public Vertex(int key, Point3D p) {
		this._key = key;
		this._location = p;
		this._weight = Double.MAX_VALUE;
		this._info = "";
		this._tag = 0;
	}
	
	public Vertex(node_data n) {
		this._key = n.getKey();
		this._location = n.getLocation();
		this._weight = n.getWeight();
		this._info = n.getInfo();
		this._tag = n.getTag();
	}
	
	public Vertex(int key, double x, double y) {
		this._key = key;
		this._location = new Point3D(x, y);
		this._weight = Double.MAX_VALUE;
		this._info = "";
		this._tag = 0;
	}

	@Override
	public int getKey() {
		return this._key;
	}

	@Override
	public Point3D getLocation() {
		return this._location;
	}

	@Override
	public void setLocation(Point3D p) {
		this._location = new Point3D(p.x(), p.y());
	}

	@Override
	public double getWeight() {
		return this._weight;
	}

	@Override
	public void setWeight(double w) {
		this._weight = w;
	}

	@Override
	public String getInfo() {
		return this._info;
	}

	@Override
	public void setInfo(String s) {
		this._info = s;
	}

	@Override
	public int getTag() {
		return this._tag;
	}

	@Override
	public void setTag(int t) {
		this._tag = t;
	}
	
	@Override
	public int compareTo(T node) {
		if(node instanceof node_data) {
			return ((node_data) node).getKey() - this.getKey();
		} else {
			throw new RuntimeException();
		}
	}

}
