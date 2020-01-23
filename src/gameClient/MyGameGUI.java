package gameClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import elements.Game_Algo;
import elements.fruits;
import elements.robots;
import utils.Point3D;

/**
 * This class create gui for the game
 * extends JFrame
 */
public class MyGameGUI extends JFrame implements ActionListener, MouseListener ,Runnable{
	
	private int choose=0;
	private int type=-1;
	private int on=0;
	boolean draw;
	private Image dbImage;
	private Graphics dbg;
	private int currentRobot;
	private int prevtNode;
	private int nextNode;
	private int counter;
	private int isFollow;
	static Thread roboThread=new Thread();
	public static KML_Logger km=null;
	private int inputfrom;


	JButton Buttons;
	JButton Start;
	JButton automatic;
	JButton manual;

	int play=-1;;
	int key;
	static DGraph dgraph;
	boolean draw_level=true;

	Point3D p;
	graph graph2;
	graph graph1;
	game_service game;
	fruits myFruit = new fruits();
	robots myRobot = new robots();
	Game_Algo myAlgo = new Game_Algo();

	 /**
     * Initialize new MyGameGUI() 
     * @param g - represnt graph
     */
	public MyGameGUI(graph g) {
    
	}
     /**
     * This method make the first init for the gui,this method performs all the properties of the game. 
     * in this function selects the level in which the player wants to play, 
     * which game mode he wants to play (auto / manual), the robots selection and more.
     */
	public MyGameGUI()
    
	{
		try {

			this.setSize(1300, 700);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);
			this.addMouseListener(this);
			this.setTitle("The Robots Game");

			if(choose==0) {
				String a[]= {"manual","outomatic"};
				type=JOptionPane.showOptionDialog(null, "choose your type of game", "Click a button", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, a, null);	
				String input = JOptionPane.showInputDialog(null,"Enter level");	
				int i=0;
				inputfrom=-1;
				boolean flag=true;
				while(flag) {
					while(i<1) {

						inputfrom = Integer.parseInt(input);
						if(0<=inputfrom && 23>=inputfrom) {
							i++;
						}
						if(i==0) {
							input = JOptionPane.showInputDialog(null,"Enter level");	
						}
					}
					flag=false;

				}
				this.game = Game_Server.getServer(inputfrom); // you have [0,23] games
				km=new KML_Logger(inputfrom);

				String gr = game.getGraph(); //getGraph returns String of edges and nodes
				DGraph gg = new DGraph();
				gg.init(gr);
				this.graph2=gg;

				choose=1;
				//	frut= new fruits(game, graph2);

			}

			repaint();

			while(choose==1) {}

			if(choose==2 &&type==1) {

				String info = this.game.toString();			
				JSONObject	line = new JSONObject(info);
				JSONObject ttt = line.getJSONObject("GameServer");
				int rs = ttt.getInt("robots");
				myAlgo.insertRobots(this.game,graph2,rs);

			}

			if(choose==2 &&type==0) {

				String info = this.game.toString();			
				JSONObject	line = new JSONObject(info);
				JSONObject ttt = line.getJSONObject("GameServer");
				int rs = ttt.getInt("robots");
				int i=0;
				boolean flag=true;
				String input = JOptionPane.showInputDialog(null,"Enter were you want your robots(only nodes)");	
				while(flag) {
					while(i<rs) {


						int inputfrom = Integer.parseInt(input);
						if(graph2.getV().contains(graph2.getNode(inputfrom))) {
							this.game.addRobot(inputfrom);
							i++;
						}
						if(i<rs) {
							input = JOptionPane.showInputDialog(null,"Enter were you want your robots(only nodes)");		
						}
					}
					flag=false;
				}
				choose=3;
			}
			repaint();

			if(JOptionPane.showConfirmDialog(null, "press YES to start the game", "ready?", JOptionPane.YES_OPTION) != JOptionPane.YES_OPTION)
				System.exit(0);
			this.game.startGame();
			try {
				on=1;
				int ind=0;
				long dt=30;
				if(type==1) {
					while(this.game.isRunning()) {
						myAlgo.moveRobots(this.game, this.graph2);

						if(ind%2==0) {repaint();}
						TimeUnit.MILLISECONDS.sleep(dt);
						ind++;
					}
				}
				if(type==0) {
					repaint();
					while(this.game.isRunning()) {

						if(ind%2==0) {repaint(); game.move();}
						TimeUnit.MILLISECONDS.sleep(dt);
						ind++;
					}


				}

				km.kmlEnd();
				counter=0;
				String info = this.game.toString();			
				JSONObject	line = new JSONObject(info);

				JSONObject	ttt = line.getJSONObject("GameServer");
				int rs = ttt.getInt("grade");
				JOptionPane.showMessageDialog(null, "Game Over: your grade is "+ rs);
				String results = game.toString();
				System.out.println("Game Over: "+results);
				System.exit(0);

			}catch(Exception ex) {}



		}catch(Exception ex) {}
	}
	private void initGUI() {}


	@Override
	public void run() {

	}

     /**
     * This method draws the graph.
     * @param g - represent graph.
     */
	@Override
	public void paint (Graphics g)
	{
     
		dbImage=createImage(1300,700 );
		dbg = dbImage.getGraphics();
		paintComponents(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}

     /**
     * This method draws the whole game using the previous function and the fruits and robots using their coordinates.
     * @param g - represent graph.
     */
	@Override
	public void paintComponents(Graphics g)
	{

		try {
			if(choose==1 || on==1) {
				if(game.getFruits()!=null) {
					choose=2;
					BufferedImage apple_image;	
					BufferedImage banana_image;

					apple_image = ImageIO.read(new File("data/apple.jpg"));
					banana_image= ImageIO.read(new File("data/banana.jpg"));

					//LinkedList<Integer> a= fruit(game, graph2,"Fruit");
					LinkedList<Integer> a= myFruit.FruitInfo(game, graph2);
					for(int i=0 ; i<a.size()-3; i=i+4) {

						if(a.get(i+2)==-1) {
							g.setColor(Color.gray);	
							g.drawImage(apple_image, (int)a.get(i)-5, (int)a.get(i+1)+5,30, 30, null);
						}
						if(a.get(i+2)==1) {
							g.setColor(Color.black);
							g.drawImage(banana_image, (int)a.get(i)-5, (int)a.get(i+1)+5,30, 30, null);
						}
					}	
					int i=0;
					for( String fruit: game.getFruits())
					{
						JSONObject	ff = new JSONObject(fruit);		
						JSONObject ttt = ff.getJSONObject("Fruit");
						int value = ttt.getInt("value");
						int type = ttt.getInt("type");

						g.setColor(Color.blue);
						g.drawString("value  "+value, a.get(i)+30 ,a.get(i+1)+15);
						g.drawString("type  "+type, a.get(i)+30 ,a.get(i+1)+30);
						i=i+4;
					}
				}
			}
			if(game!=null) {

				String info = game.toString();
				JSONObject line = new JSONObject(info);
				JSONObject t = line.getJSONObject("GameServer");
				int grade = t.getInt("grade");
				g.setColor(Color.black);
				long time= game.timeToEnd();
				Graphics2D g2=(Graphics2D) g;
				g2.setStroke(new BasicStroke(2));
				g2.drawString("level: "+inputfrom+"  Time Left  00:"+time/1000+"   score is: "+grade, 300 ,630);

				BufferedImage robot_image;
				robot_image = ImageIO.read(new File("data/robot.jpg"));

				LinkedList<Integer> a= myRobot.robotsInfo(game, graph2);
				g.setColor(Color.red);
				for(int i=0 ; i<a.size()-2; i=i+3) {

					g.drawImage(robot_image, (int)a.get(i)-5, (int)a.get(i+1)+5,40, 40, null);
				}	
				int i=0;
				for( String Robot: game.getRobots())
				{

					JSONObject	ff = new JSONObject(Robot);		
					JSONObject ttt = ff.getJSONObject("Robot");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					double value=ttt.getInt("value");
					int id= ttt.getInt("id");
					g.setColor(Color.black);
					if(id==0) {
						g2.drawString("Robot id is: "+id+ "  src  "+src+"  dest  "+dest+"     value:"+value, 300 ,650);

					}
					if(id==1) {
						g2.drawString("Robot id is: "+id+ "  src  "+src+"   dest  "+dest+"     value:"+value, 300 ,660);

					}
					if(id==2) {
						g2.drawString("Robot id is: "+id+ "   src  "+src+"  dest  "+dest+"     value:"+value, 300 ,670);

					}


					g.setColor(Color.black);
					g.drawString("src  "+src, a.get(i)+30 ,a.get(i+1)+15);
					g.drawString("dest  "+dest, a.get(i)+30 ,a.get(i+1)+30);
					i=i+3;
				}
			}
			//end of robots
			if(graph2!=null ) {
				for (node_data p : graph2.getV() ) 
				{
					g.setColor(Color.BLUE);
					Point3D srcPoint = p.getLocation();
					g.fillOval((int)srcPoint.x()-7, (int)srcPoint.y()-7, 12, 12);
					g.drawString(""+p.getKey(), (int)srcPoint.x() ,(int)srcPoint.y()-10);														

					if(graph2.getE(p.getKey())!=null) {

						for(edge_data e: graph2.getE(p.getKey())) {

							g.setColor(Color.magenta);
							if(e.getInfo()=="do" ) {
								e.setInfo("");
								g.setColor(Color.black);}
							Point3D destPoint = graph2.getNode(e.getDest()).getLocation();
							g.drawLine((int)srcPoint.x(), (int)srcPoint.y(), (int)destPoint.x(), (int)destPoint.y());

							g.setColor(Color.BLACK);

							g.setColor(Color.yellow);
							g.fillOval((int)((0.1*srcPoint.x()+0.9*destPoint.x())-5), (int)((0.1*srcPoint.y()+0.9*destPoint.y())-5), 10, 10);
							g.setColor(Color.BLUE);
							g.drawString(""+e.getDest() , (int)destPoint.x(), (int)destPoint.y()-10);

						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("avar");
			e.printStackTrace();
		}
	}




	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		return;
	}
     /**
     * this method is when the mode is manualed.The player clicks on a particular robot to move it
     * in the game in manual mode and the function takes the id of this particular robot.
     * @param e - represent MouseEvent.
     */
	@Override
	public void mousePressed(MouseEvent e) {
	
		
		// TODO Auto-generated method stub
		int x = e.getX();
		int y = e.getY();
		Point3D location=new Point3D(x, y);

		//LinkedList<Integer> a= fruit(game, graph2, borot);
		LinkedList<Integer> a=myRobot.robotsInfo(game, graph2);
		for(int i=0 ; i<a.size()-2; i=i+3) {
			if(Math.abs(x- a.get(i))<20 && Math.abs(y-a.get(i+1))<20){

				currentRobot=a.get(i+2);
			}	
		}
	}
     /**
     * this method is when the mode is manualed. When the player unleashes the mouse,
     * it takes the x and y coordinates and moves the robot
     * that we took its id in the previous function to his next node according to the coordinates.
     * @param e - represent MouseEvent.
     */
	@Override
	public void mouseReleased(MouseEvent e) {

		
		// TODO Auto-generated method stub
		if(type==0) {
			draw=true;
			int x = e.getX();
			int y = e.getY();
			Point3D location=new Point3D(x, y);
			//System.out.println(x);
			//System.out.println(y);
			myAlgo.moveRobotsManual( game, graph2, location,currentRobot) ;
			repaint();
			draw=false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MyGameGUI g = new MyGameGUI();

	}
}
