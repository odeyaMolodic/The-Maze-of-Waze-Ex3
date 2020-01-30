package elements;
import Server.game_service;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;


/**
 * This interface represents the set of function needed for auto and manual version of the game
 * you may need to use EPSILON = 0.001
 * @author sarah-han
 */
public interface  algo {

	
	
	 /**
     * goes over all the fruits one by one and by the distance knows which edge the fruit is located
     * and the function will return the fruit that is on the closest edge.
     * @ param game - represent a game
     * @ param gg - represent graph
     * @ param src - represent the starting node
     */
	public  List<node_data> fruitLocation(game_service game, graph gg,int src);
    
	  /**
     * this method is when the mode is automated and this function responsible to make the insert of the robots.
     * The function puts the robots close to the fruits, goes over all the fruits and checks what
     * is the fruit with the highest value and next to it puts in a robot.
     * @ param game - represent a game
     * @ param gg - represent graph
     * @ param NumOfRobots
     */
    public void insertRobots(game_service game, graph gg, int NumOfRobots);
    
    
    /**
     * This method gets a game,graph and a starting point and helps direct a
     * robot to his next destination with the use of fruitLocation method.
     * @ param game - represent a game
     * @ param g - represent graph
     * @ param src - represent the starting node
     */
    public int nextNode(game_service game,graph g, int src );

    
    /**
     *  this method is when the mode is manualed. In manual mode, the player enters which robot he wants
     * to move with and presses the fruit he wants the robot to collect.
     * @ param game - represent a game
     * @ param gg - represent graph
     * @ param location - represent location on the graph
     * @ param currentRobot
     */
    public void moveRobotsManual(game_service game, graph gg,Point3D location,int currentRobot);
	
    /**
     *   this method is when the mode is automated . The function relies on the nextNode function
     * in order to move the robot to its next destination.
     * @ param game - represent a game
     * @ param gg - represent graph
     */
    public void moveRobots(game_service game, graph gg);
	
		
	
}
