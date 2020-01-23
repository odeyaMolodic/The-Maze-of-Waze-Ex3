package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * this class responsible to create 24 kml files
 * the file can be loaded to google earth
 * and view the game course in a specific level
 */
public class KML_Logger {
    /**
     * private data types of the class
     * int level
     * StringBuffer str- the string will be written there.
     */

    private int level;
    private StringBuilder info;

    /**
     * Default constructor
     */
    
    public  KML_Logger(){}
    /**
     * this method initialize the object and concat the standard start of a KML file.
     * @param level - represent the stage
     */
    public KML_Logger(int level) {
        this.level = level;
        info = new StringBuilder();
        KML_node();
    }

    /**
     * this function initialize the node icon to KML
     */
    private void KML_node(){
    	info.append(" <Style id=\"node\">\r\n" +
                "      <IconStyle>\r\n" +
                "        <Icon>\r\n" +
                "          <href>http://maps.google.com/mapfiles/kml/pal3/icon35.png</href>\r\n" +
                "        </Icon>\r\n" +
                "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                "      </IconStyle>\r\n" +
                "    </Style>"
        );
        KML_Fruit();
    }
    /**
     * this function initialize the Fruits icon to KML (Type 1 and -1)
     */
    private void KML_Fruit(){
    	info.append(
                " <Style id=\"fruit_-1\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/paddle/purple-stars.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>" +
                        " <Style id=\"fruit_1\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/paddle/red-stars.png</href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>"
        );
        KML_Robot();
    }
    /**
     * this function initialize the Robots icon to KML
     */
    private void KML_Robot(){
    	info.append(
                " <Style id=\"robot\">\r\n" +
                        "      <IconStyle>\r\n" +
                        "        <Icon>\r\n" +
                        "          <href>http://maps.google.com/mapfiles/kml/shapes/motorcycling.png></href>\r\n" +
                        "        </Icon>\r\n" +
                        "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
                        "      </IconStyle>\r\n" +
                        "    </Style>"
        );
    }

    /**
     * this methos adds placemark to the KML file.
     * Each game element has a placemark id.
     * @param id 
     * @param location 
     */
    public void addPlaceMark(String id, String location)
    {
        LocalDateTime time = LocalDateTime.now();
        info.append(
                "    <Placemark>\r\n" +
                        "      <TimeStamp>\r\n" +
                        "        <when>" + time+ "</when>\r\n" +
                        "      </TimeStamp>\r\n" +
                        "      <styleUrl>#" + id + "</styleUrl>\r\n" +
                        "      <Point>\r\n" +
                        "        <coordinates>" + location + "</coordinates>\r\n" +
                        "      </Point>\r\n" +
                        "    </Placemark>\r\n"
        );
    }

    
    
    /**
     * Concat the closing string for the KML file.
     * Creates a kml file name=stage.kml and save it to the data folder in this project.
     */
    public void kmlEnd()
    {
        info.append("  \r\n</Document>\r\n" +
                "</kml>"
        );
        kmlSave();
    }
    public void kmlSave() {
        try
        {
            File f=new File("data/"+this.level+".kml");
            PrintWriter pw=new PrintWriter(f);
            pw.write(info.toString());
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    }
