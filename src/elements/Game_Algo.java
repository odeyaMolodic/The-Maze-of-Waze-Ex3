package elements;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.sun.jdi.Location;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;
import gameClient.*;
import utils.Point3D;

public class Game_Algo {


	public Game_Algo() {
	}

	private int prevtNode;
	private int nextNode;
	private int counter;
	
	private int idr;
	private int srcr;
	private int destr;
	private boolean folowr;

     /**
     * goes over all the fruits one by one and by the distance knows which edge the fruit is located
     * and the function will return the fruit that is on the closest edge.
     * @ param game - represent a game
     * @ param gg - represent graph
     * @ param src - represent the starting node
     */
	public  List<node_data> fruitLocation(game_service game, graph gg,int src) {
   
		LinkedList<Integer> fruitLocation= fruits.FruitInfo(game, gg);
		List<edge_data> fruitLocationList= new LinkedList<edge_data>();
		LinkedList<edge_data> fruitTypeList= new LinkedList<edge_data>();
		List<node_data> ans= new LinkedList<node_data>();

		Collection<node_data> nodeList = gg.getV();
		for(node_data node: nodeList) {
			Collection<edge_data> edgeList2 = gg.getE(node.getKey());
			for( edge_data edge: edgeList2 ) {
				int x= edge.getDest();
				double distPoints=Math.sqrt((gg.getNode(x).getLocation().y() - gg.getNode(node.getKey()).getLocation().y()) * (gg.getNode(x).getLocation().y() - gg.getNode(node.getKey()).getLocation().y()) + (gg.getNode(x).getLocation().x() - gg.getNode(node.getKey()).getLocation().x()) * (gg.getNode(x).getLocation().x() - gg.getNode(node.getKey()).getLocation().x()));

				for (int i = 0; i < fruitLocation.size()-3; i=i+4) {
					double xx=fruitLocation.get(i);
					double yy=fruitLocation.get(i+1);
					int type=fruitLocation.get(i+2);
					int value=fruitLocation.get(i+3);

					double dist1=Math.sqrt((gg.getNode(x).getLocation().y() - yy) * (gg.getNode(x).getLocation().y() - yy) + (gg.getNode(x).getLocation().x() - xx) * (gg.getNode(x).getLocation().x() - xx));
					double dist2=Math.sqrt((gg.getNode(node.getKey()).getLocation().y() - yy) * (gg.getNode(node.getKey()).getLocation().y() - yy) + (gg.getNode(node.getKey()).getLocation().x() - xx) * (gg.getNode(node.getKey()).getLocation().x() - xx));
					double cal=dist1+dist2-distPoints;
					
					if(Math.abs(cal)<Math.abs(0.2)) {
						fruitLocationList.add(edge);
						System.out.println("in in in in ");
						System.out.println("nextNode is"+edge.getDest());
					}

				}

			}
		}

		
		
		
		
		Graph_Algo ga = new Graph_Algo();
		ga.init(gg);
		double minApple= Integer.MAX_VALUE;
		int myApplekey=-1;

		for(edge_data shortes :fruitLocationList ) {
			int mysrc=shortes.getSrc();
			int mydest=shortes.getDest();
			//	if(mysrc<=mydest)

			if(src==mysrc) {
				ans=ga.shortestPath(src, mydest);
				return ans;
			}
		

			if(folowr==true) {
			
			ans=ga.shortestPath(src, fruitLocationList.get(1).getDest());
			return ans;
			
			
		}
			
			double x=ga.shortestPathDist(src, mysrc);
			double y=ga.shortestPathDist(src, mydest);
			if(x<=y) {
				if(x<minApple) {
					minApple=x;
					myApplekey=mysrc;
				}

			}
			else {
				if(y<minApple) {
					minApple=y;
					myApplekey=mydest;
				}

			}		
		}
		if(myApplekey!=-1)
			ans=ga.shortestPath(src, myApplekey);

		else { System.out.println("blat");
		ans=null;
		}
		
		
		
		return ans;
	}

     /**
     * this method is when the mode is automated and this function responsible to make the insert of the robots.
     * The function puts the robots close to the fruits, goes over all the fruits and checks what
     * is the fruit with the highest value and next to it puts in a robot.
     * @ param game - represent a game
     * @ param gg - represent graph
     * @ param NumOfRobots
     */
	public void insertRobots(game_service game, graph gg, int NumOfRobots) {


		LinkedList<Integer> fruitLocation=  fruits.FruitInfo(game, gg);
		List<edge_data> fruitLocationList= new LinkedList<edge_data>();
		List<Integer> fruitTypeList= new LinkedList<Integer>();
		List<Integer> fruitValueList= new LinkedList<Integer>();
		int minValue=Integer.MIN_VALUE;
		int mytype=-1;
		edge_data big=null;
		
		
		Collection<node_data> nodeList = gg.getV();
		for(node_data node: nodeList) {
			Collection<edge_data> edgeList2 = gg.getE(node.getKey());
			for( edge_data edge: edgeList2 ) {
				int x= edge.getDest();

				double distPoints=Math.sqrt((gg.getNode(x).getLocation().y() - gg.getNode(node.getKey()).getLocation().y()) * (gg.getNode(x).getLocation().y() - gg.getNode(node.getKey()).getLocation().y()) + (gg.getNode(x).getLocation().x() - gg.getNode(node.getKey()).getLocation().x()) * (gg.getNode(x).getLocation().x() - gg.getNode(node.getKey()).getLocation().x()));

				for (int i = 0; i < fruitLocation.size()-3; i=i+4) {
					double xx=fruitLocation.get(i);
					double yy=fruitLocation.get(i+1);
					int type=fruitLocation.get(i+2);
					int value=fruitLocation.get(i+3);

					double dist1=Math.sqrt((gg.getNode(x).getLocation().y() - yy) * (gg.getNode(x).getLocation().y() - yy) + (gg.getNode(x).getLocation().x() - xx) * (gg.getNode(x).getLocation().x() - xx));
					double dist2=Math.sqrt((gg.getNode(node.getKey()).getLocation().y() - yy) * (gg.getNode(node.getKey()).getLocation().y() - yy) + (gg.getNode(node.getKey()).getLocation().x() - xx) * (gg.getNode(node.getKey()).getLocation().x() - xx));
					double cal=dist1+dist2;
					double cal2=distPoints;

					if(Math.abs(cal-cal2)<Math.abs(0.2) && value>=minValue ) {

						
						
						big=edge;
						minValue=value;
						mytype=type;
						if(fruitLocationList.size()<NumOfRobots+1) {
							
							fruitLocationList.add(big);
							fruitTypeList.add(mytype);
							fruitValueList.add(minValue);
						}
						else {
							int min=Integer.MAX_VALUE;
							for (int j = 0; j < fruitTypeList.size(); j++) {
								if(fruitValueList.get(j)<min)
									min=fruitValueList.get(j);
							}	
							fruitLocationList.remove(1);
							fruitTypeList.remove(1);
							fruitLocationList.add(big);
							fruitTypeList.add(mytype);
						}
					}
				}
			}
		
		}
		
	if(	fruitLocationList.size()==0) {
		System.out.println("blattttt");
	}
		
		
		if(fruitLocationList.size()< NumOfRobots) {
			
			edge_data extra=fruitLocationList.get(0);
			fruitLocationList.add(extra);
			fruitTypeList.add(fruitTypeList.get(0));
		}
		
		for (int i = 0; i < NumOfRobots; i++) {
			System.out.println("NumOfRobots  "+NumOfRobots);
			big=fruitLocationList.get(i);
			if(minValue!=Integer.MIN_VALUE && fruitTypeList.get(i)==-1 && big!=null){

				int max=Math.max(big.getSrc(), big.getDest());
				game.addRobot(max);
			}
			if(minValue!=Integer.MIN_VALUE && fruitTypeList.get(i)==1  && big!=null){
				int min=Math.min(big.getSrc(), big.getDest());
				game.addRobot(min);
			}

			
		}
	}

	
     /**
     * This method gets a game,graph and a starting point and helps direct a
     * robot to his next destination with the use of fruitLocation method.
     * @ param game - represent a game
     * @ param g - represent graph
     * @ param src - represent the starting node
     */
	public int nextNode(game_service game,graph g, int src ) {
   

		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		List<node_data> fruitLocation= fruitLocation(game,g,src);
	
		if (fruitLocation!=null) {
			node_data nodeAns= fruitLocation.get(1);
			int keyInt=nodeAns.getKey();
			System.out.println("nextNode is"+keyInt);
			return keyInt;

		}


		if(fruitLocation==null)
			System.out.println("avi");

		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r)
		{itr.next();i++;}
		ans = itr.next().getDest();

		return ans;
	}



     /**
     *  this method is when the mode is manualed. In manual mode, the player enters which robot he wants
     * to move with and presses the fruit he wants the robot to collect.
     * @ param game - represent a game
     * @ param gg - represent graph
     * @ param location - represent location on the graph
     * @ param currentRobot
     */
	public void moveRobotsManual(game_service game, graph gg,Point3D location,int currentRobot) {
   


		double x=location.x();
		double y = location.y();
		int dest1=0;
		for(node_data node :gg.getV()) {
			if(Math.abs(node.getLocation().x()-x)<Math.abs(10)  ) {
				if(Math.abs(node.getLocation().y()-y)<Math.abs(10)) {
					dest1=node.getKey();
					break;
				}
			}
		}

		List<String> log = game.move();
		if(log!=null) {
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int dest = ttt.getInt("dest");

					if(rid==currentRobot) {
						if(dest==-1) {	
							game.chooseNextEdge(rid, dest1);
							//System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));

						}
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}

	}

     /**
     *   this method is when the mode is automated . The function relies on the nextNode function
     * in order to move the robot to its next destination.
     * @ param game - represent a game
     * @ param gg - represent graph
     */
	public void moveRobots(game_service game, graph gg) {
	folowr=false;
			List<String> log = game.move();
			if(log!=null) {

				for(int i=0;i<log.size();i++) {
					String robot_json = log.get(i);
					try {
						JSONObject line = new JSONObject(robot_json);
						JSONObject ttt = line.getJSONObject("Robot");
						int rid = ttt.getInt("id");
						int src = ttt.getInt("src");
						int dest = ttt.getInt("dest");

						if(rid!=idr && srcr==src && destr==dest) {
							folowr=true;
							System.out.println("hereeeeeeeeee");
						}
							idr=rid;
							srcr=src;
							destr=dest;
						
						
						
						
						if(dest==-1) {	
							dest = nextNode(game,gg, src);
							if(prevtNode==dest) {
								counter++;
								if(counter==3) {
									System.out.println("wrong");
									Collection<edge_data> edges= gg.getE(src);
									for(edge_data newNext : edges) {
										if(newNext.getDest()!=dest) {
											dest=newNext.getDest();
											counter=0;
											break;
										}
									}
								}
							}

							prevtNode=src;
							nextNode=dest;

							game.chooseNextEdge(rid, dest);

						}
					} 
					catch (JSONException e) {e.printStackTrace();}
				}
			}
	}










}
