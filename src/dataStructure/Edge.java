package dataStructure;

import dataStructure.edge_data;

public class Edge implements edge_data{
	
	private int _src;
	private int _dest;
	private double _weight;
	private String _info;
	private int _tag;
	
	public Edge(int src, int dest, double weight) {
		this._src = src;
		this._dest = dest;
		this._weight = weight;
		this._info = "";
		this._tag = 0;
	}

	@Override
	public int getSrc() {
		return this._src;
	}

	@Override
	public int getDest() {
		return this._dest;
	}

	@Override
	public double getWeight() {
		return this._weight;
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

}
