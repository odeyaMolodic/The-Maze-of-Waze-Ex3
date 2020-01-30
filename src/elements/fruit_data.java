package elements;

import Server.game_service;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;




public interface fruit_data {

	
	
	   /**
     *  Constructor init fruit attributes from a JSON string input. Separates the different values
     * from jsonString and sets them to their respective values. Pos The function separates the x and y values and
     * creates the 3D point for pos.
     * @ param jsonString
     */
	public void init(String jsonString);
	
	
	
	public static LinkedList<Integer> FruitInfo(game_service game, graph p) {
		return null;
	}



	public int getType();



	public double getValue();



	public Point3D getPos();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
