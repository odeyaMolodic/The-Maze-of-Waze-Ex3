package gameClient;

import java.sql.*;
import java.util.LinkedList;

import static gameClient.SimpleDB.*;
/**
 * this class allows us read the information of each game
 *  a specific player played.
 *  by id user enters
 * the information is read from data base SQL
 */
class DB_Work {
    /**
     * private data types of the class
     * LinkedList<Integer> levellst
     */
    private static LinkedList<Integer> levellst;
    /**
     * simple constructor
     * add the linked list the
     * difficult stages in the game.
     * The server defines which level is difficult .
     */
    DB_Work(){
        levellst=new LinkedList<>();
        levellst.add(0);
        levellst.add(1);
        levellst.add(3);
        levellst.add(5);
        levellst.add(9);
        levellst.add(11);
        levellst.add(13);
        levellst.add(16);
        levellst.add(19);
        levellst.add(20);
        levellst.add(23);
    }
    /**
     * The function prints for the player the highest score he could finished a game with
     * @param id- for the player.
     * @return
     */
    static String printLog(int id) {
        StringBuilder str= new StringBuilder();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet=null;
            int ind =0;
            int MaxLevel=0;
            str.append("Max Score per level Played by: ").append(id).append("\n").append("\n");
            for (int i = 0; i <24 ; i++) {
                boolean have_a_score=false;
                int MaxScore=0;
                int MinMoves=Integer.MAX_VALUE;
                Timestamp time = null;
                String allCustomersQuery = "SELECT * FROM Logs where userID="+id+" AND levelID="+i;
                resultSet = statement.executeQuery(allCustomersQuery);
                str.append("level ").append(i).append(") ");
                    while (resultSet.next()) {
                        have_a_score = true;
                        ind++;
                        int level = resultSet.getInt("levelID");
                        if (level > MaxLevel) {
                            MaxLevel = level;
                        }
                        boolean Toughlevel = ToughLevels(i);
                        int score = resultSet.getInt("score");
                        int moves = resultSet.getInt("moves");
                        if (Toughlevel) {
                            if (underMaxMoves(moves, level)) {
                                if (score > MaxScore) {
                                    MaxScore = score;
                                    MinMoves = moves;
                                    time = resultSet.getTimestamp("time");
                                }
                            }
                        } else {
                            if (score > MaxScore) {
                                MaxScore = score;
                                MinMoves = moves;
                                time = resultSet.getTimestamp("time");
                            }
                        }
                    }
                if(have_a_score&&time!=null){
                    str.append("score: ").append(MaxScore).append(", moves: ").append(MinMoves).append(", at Time: ").append(time.toString()).append("\n");
                }
                else{
                    str.append("not played yet/not passed minimum requirements").append("\n");
                }
            }
            str.append("\n").append("ID: ").append(id).append(" has Played: ").append(ind).append(" games.").append("\n").append("MaxLevel Reached is:").append(MaxLevel).append("\n");
            resultSet.close();
            statement.close();
            connection.close();
        }

        catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
    /**
     * If a player chooses to play a game from the difficult stages.
     * The function will print the score for it relative to the other players
     * @param id - for the player.
     * @return
     */
    static String ToughStages(int id) {
        ResultSet resultSet = null;
        StringBuilder str = new StringBuilder();
        str.append(id).append(" Placements for 'Tough Levels' are:").append("\n");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            for (int level : levellst) {
                int place = 1;
                String Query = "SELECT MAX(score) AS score FROM Logs where UserID=" + id + " AND levelID=" + level;
                resultSet = statement.executeQuery(Query);
                resultSet.next();
                int myhigh = resultSet.getInt("score");
                Query = "SELECT * FROM Logs where LevelID=" + level + " Order by score desc";
                resultSet = statement.executeQuery(Query);
                LinkedList<Integer> lst2 = new LinkedList<>();
                while (resultSet.next()) {
                    int nextscore = resultSet.getInt("score");
                    if (!lst2.contains(nextscore)) {
                        lst2.add(nextscore);
                        if(nextscore==myhigh) break;
                        place++;
                    }
                }
                str.append(level).append(") ").append(place).append("\n");
            }
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
    /**
     *this function check for each difficult level
     * if the number of moves taken in the game
     * is lower then the maximum number of steps can be taken to pass a stage.
     *
     * @param moves- enter a number of moves taken
     * @param level - which level was played
     * @return true if lower then the maximum
     */
    private static boolean underMaxMoves(int moves,int level){
        switch(level) {
            case 0 :
            case 16:
            case 20:
                return moves <= 290;
            case 1:
            case 3:
            case 9:
            case 11:
            case 13:
            case 19:
                return moves<= 580;
            case 5:
                return moves<= 500;
            case 23:
                return moves<= 1140;
            default: return false;
        }
    }
    /**
     * boolean function to check if a level is a difficult level
     * @param level
     * @return
     */
    static boolean ToughLevels(int level){
        return levellst.contains(level);
    }
}