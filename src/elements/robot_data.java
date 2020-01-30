package elements;
import Server.game_service;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;




public interface robot_data {
	
	
	
	  /**
     *  The function gets a game and a graph and returns a linked list containing all the information about the robots.
     * The function converts the coordinates of the x and y values of the robots from STDraw
     * to jframe and adds to the list according to the values in the following order: first x, then y, and id
     * @param game - represent game
     * @param p - represent graph
     */
	public LinkedList<Integer> robotsInfo(game_service game, graph p);
	
	
	public void locate(double xmin, double ymin, double ymax, double xmax, LinkedList<Integer> a, game_service game);


	public double getValue();


	public int getSrc();


	public void setSrc(int i);


	public void setDest(int i);


	public int getDest();


	public void setSpeed(double s);


	public double getSpeed();


	public int getID();


	

	
}
