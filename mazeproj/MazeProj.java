package mazeproj;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.*;


public class MazeProj {
    

    public static int START_ROW;
    public static int START_COL;
    public static int GOAL_ROW;
    public static int GOAL_COL;
    
    //Xreiazontai allagh se periptwsh xrhshs diaforetikou xarth!
    public static int MAP_ROWS = 11;
    public static int MAP_COLS = 13;
    
    public static int tree_length = 0;
    
    static char[][] map = new char[MAP_ROWS][MAP_COLS];
    
    
    public static void main(String[] args) throws Exception {
        
        //Xreiazetai allagh analoga me thn topothesia tou arxeiou ston ypologisth!
        initMap("../map.txt");
        
        initConstants();

        Problem p1 = new Problem();
        p1.setMap(map);
        p1.setStart(START_ROW, START_COL);
        p1.setGoal(GOAL_ROW, GOAL_COL);
        
        printMap();
        
        UCS(p1);
        
        IDS(p1);
        
        Astar(p1);

    }
    
    
    /*
    O UCS emfanizei tis veltistes lyseis ws pros to kostos.
    Oi lyseis mporei na diaferoun san monopatia exaitias ths tyxaiothtas se
    periptwseis isopalias, alla oles exoun ta idio kostos to opoio einai to elaxisto
    */
    public static ArrayList<int[]> UCS(Problem prob){
        
        int start[] = new int[2];
        start = prob.getStart();
        
        int goal[] = new int[2];
        goal = prob.getGoal();
        
        ArrayList<Node> path = new ArrayList<Node>();
        ArrayList<String> visited = new ArrayList<String>();
        
        PriorityQueue<Node> fringe = new PriorityQueue<Node>(4, new CostComparator());
        
        ArrayList<int[]> result = new ArrayList<int[]>();
        
        tree_length=0;
        

        Node n = new Node();
        n.setCoords(start[0], start[1]);
        n.setState(map[start[0]][start[1]]);
        n.setCost(0);
        n.setDepth(0);
        fringe.add(n);

        while(!fringe.isEmpty()){
            
            Node current_node = new Node();
            current_node = fringe.poll();

            String temp = current_node.getCoords()[0] + " " + current_node.getCoords()[1];
            
            if(!visited.contains(temp)){

                visited.add(temp);

                path.add(current_node);

                if(Arrays.equals(current_node.getCoords(), goal)){
                        
                    System.out.println("----------------------\n\nResults of UCS:\n");
                    return(goalFound(current_node));

                }

                expandUCS(current_node, fringe);


            }
            
        }
        
        return(result);
        
    }
    

    //H expand ston UCS epilegei tyxaia se periptwsh isopalias
    public static void expandUCS(Node current_node, PriorityQueue<Node> fringe){
        
        List<Integer> rand = new ArrayList<>(List.of(1,2,3,4));
    
        Collections.shuffle(rand);

        for(int i=0; i<4; i++){
            
            if(rand.get(i) == 1)
                expandNeighbour(current_node, fringe, null, "UP", "UCS");
            
            else if(rand.get(i) == 2)
                expandNeighbour(current_node, fringe, null, "DOWN", "UCS");
            
            else if(rand.get(i) == 3)
                expandNeighbour(current_node, fringe, null, "LEFT", "UCS");
            
            else if(rand.get(i) == 4)
                expandNeighbour(current_node, fringe, null, "RIGHT", "UCS");
            
        }

    }
    
    
    //Ylopoiei thn epanalipsi ayksanontas to orio
    public static void IDS(Problem prob){
        
        int max_depth = 0;
        tree_length=0;
        
        ArrayList<int[]> result = new ArrayList<int[]>();
        
        while(result.size() == 0){
            
            result = IDS_search(prob, max_depth);
            max_depth++;
            
        }

    }
    
    
    /*
    O IDS vriskei to idio apotelesma se kathe ektelesi kathws den prostethike h tyxaia epilogi
    se periptwsh isopalias, afou einai shmantikh h seira epektashs se auton ton algorithmo.
    */
    public static ArrayList<int[]> IDS_search(Problem prob, int max_depth){

        int start[] = new int[2];
        start = prob.getStart();
        
        int goal[] = new int[2];
        goal = prob.getGoal();
        
        ArrayList<Node> path = new ArrayList<Node>();
        ArrayList<String> visited = new ArrayList<String>();
        
        Stack<Node> fringe = new Stack<Node>();

        ArrayList<int[]> result = new ArrayList<int[]>();


        Node n = new Node();
        n.setCoords(start[0], start[1]);
        n.setState(map[start[0]][start[1]]);
        n.setCost(0);
        n.setDepth(0);
        fringe.push(n);

        while(!fringe.isEmpty()){
            
            Node current_node = new Node();
            current_node = fringe.pop();
            
            if(current_node.getDepth() < max_depth){

                String temp = current_node.getCoords()[0] + " " + current_node.getCoords()[1];

                if(!visited.contains(temp)){

                    visited.add(temp);

                    path.add(current_node);

                    if(Arrays.equals(current_node.getCoords(), goal)){

                        System.out.println("----------------------\n\nResults of IDS:\n");
                        
                        return(goalFound(current_node));

                    }

                    expandIDS(current_node, fringe);
                    
                }
            
            }

        }
        
        return(result);
            
    }

    
    /*
    Den yparxei tyxaia epilogh se periptwseis isopalias. Epilexthike h sygkekrimenh seira
    kathws paratirithike oti einai h pio katallhlh gia to provlhma mas.
    */
    public static void expandIDS(Node current_node, Stack<Node> fringe){

        expandNeighbour(current_node, null, fringe, "UP", "IDS");
        expandNeighbour(current_node, null, fringe, "RIGHT", "IDS");
        expandNeighbour(current_node, null, fringe, "DOWN", "IDS");
        expandNeighbour(current_node, null, fringe, "LEFT", "IDS");

    }
    
    
    /*
    O A* epilegei me vash thn Manhattan apostash kai oxi ta kosth.
    Oi lyseis mporei na diaferoun san monopatia exaitias ths tyxaiothtas se periptwseis isopalias
    twn apostasewn Manhattan, alla oles exoun ta idio athroisma se heuristics to opoio einai to elaxisto.
    */
    public static ArrayList<int[]> Astar(Problem prob){
        
        int start[] = new int[2];
        start = prob.getStart();
        
        int goal[] = new int[2];
        goal = prob.getGoal();
        
        ArrayList<Node> path = new ArrayList<Node>();
        ArrayList<String> visited = new ArrayList<String>();
        
        PriorityQueue<Node> fringe = new PriorityQueue<Node>(4, new HeuristicComparator());
        
        ArrayList<int[]> result = new ArrayList<int[]>();
        
        tree_length=0;
        

        Node n = new Node();
        n.setCoords(start[0], start[1]);
        n.setState(map[start[0]][start[1]]);
        n.setCost(0);
        n.setDepth(0);
        fringe.add(n);
        
        while(!fringe.isEmpty()){
            
            Node current_node = new Node();
            current_node = fringe.poll();

            String temp = current_node.getCoords()[0] + " " + current_node.getCoords()[1];
            
            if(!visited.contains(temp)){

                visited.add(temp);

                path.add(current_node);

                if(Arrays.equals(current_node.getCoords(), goal)){
                        
                    System.out.println("----------------------\n\nResults of A*:\n");
                    return(goalFound(current_node));

                }

                expandAstar(current_node, fringe);

            }
            
        }
        
        return(result);
        
    }
    
    
    //Kanei to expand tou Astar
    public static void expandAstar(Node current_node, PriorityQueue<Node> fringe){
        
        expandNeighbour(current_node, fringe, null, "UP", "A*");
        expandNeighbour(current_node, fringe, null, "RIGHT", "A*");
        expandNeighbour(current_node, fringe, null, "DOWN", "A*");
        expandNeighbour(current_node, fringe, null, "LEFT", "A*");

    }
    
  
    //Ypologizei to heuristic xrhsimopoiwntas thn apostash Manhattan
    public static int heuristic(Node neighbour, Node goal_node){
        
        return(Math.abs(neighbour.getCoords()[0] - goal_node.getCoords()[0])  +  Math.abs(neighbour.getCoords()[1] - goal_node.getCoords()[1]));
        
    }
    
    
    //Dhmiourgei tous geitones san antikeimena komvwn kai tous prosthetei sto fringe
    public static void expandNeighbour(Node current_node, PriorityQueue<Node> fringe_pq, Stack<Node> fringe_st, String direction, String alg){
        
        int current_node_coords[] = new int[2];
        current_node_coords = current_node.getCoords();

        Node goal_node = new Node();
        goal_node.setCoords(GOAL_ROW, GOAL_COL);
        
        boolean limit_flag = false;
        if(direction == "UP"){
            if(current_node_coords[0] > 0)
                limit_flag = true;
        }
        else if(direction == "DOWN"){
            if(current_node_coords[0] < MAP_ROWS-1)
                limit_flag = true;
        }
        else if(direction == "LEFT"){
            if(current_node_coords[1] > 0)
                limit_flag = true;
        }
        else if(direction == "RIGHT"){
            if(current_node_coords[1] < MAP_COLS-1)
                limit_flag = true;
        }

        if(limit_flag){

            int neighbour_coords[] = new int[2];
            char neighbour_state;
            
            if(direction == "UP"){
                neighbour_coords[0] = current_node_coords[0]-1;
                neighbour_coords[1] = current_node_coords[1];
            }
            else if(direction == "DOWN"){
                neighbour_coords[0] = current_node_coords[0]+1;
                neighbour_coords[1] = current_node_coords[1];
            }
            else if(direction == "LEFT"){
                neighbour_coords[0] = current_node_coords[0];
                neighbour_coords[1] = current_node_coords[1]-1;
            }
            else if(direction == "RIGHT"){
                neighbour_coords[0] = current_node_coords[0];
                neighbour_coords[1] = current_node_coords[1]+1;
            }

            neighbour_state = map[neighbour_coords[0]][neighbour_coords[1]];

            if(neighbour_state == 'O' || neighbour_state == 'D' || neighbour_state == 'G' || neighbour_state == 'S'){

                Node neighbour = new Node();

                neighbour.setCoords(neighbour_coords[0], neighbour_coords[1]);

                neighbour.setState(neighbour_state);

                neighbour.setParentNode(current_node);

                int temp_new_cost = 0;
                if(neighbour_state == 'O' || neighbour_state == 'G' || neighbour_state == 'S')
                    temp_new_cost = current_node.getCost()+1;
                else if(neighbour_state == 'D')
                    temp_new_cost = current_node.getCost()+2; 

                neighbour.setCost(temp_new_cost);

                neighbour.setDepth(current_node.getDepth()+1);
                
                if(alg == "A*")
                    neighbour.setHeuristic(heuristic(neighbour, goal_node));

                if(alg == "UCS" || alg == "A*"){
                    fringe_pq.add(neighbour);
                    tree_length++;
                }
                else if(alg == "IDS"){
                    fringe_st.push(neighbour);
                    tree_length++;
                }

            }
        
        }
        
    }
    
    
    //Epistrefei to monopati se periptwsh pou vrethei o stohos
    public static ArrayList<int[]> goalFound(Node current_node){
        
        ArrayList<int[]> result = new ArrayList<int[]>();
        int nodes_counter=0;
        
        while(current_node.getParentNode() != null){

            int[] insert_to_result = {current_node.getCoords()[0]+1, current_node.getCoords()[1]+1, current_node.getCost()};
            result.add(insert_to_result);

            current_node = current_node.getParentNode();

        }

        int[] insert_to_result = {current_node.getCoords()[0]+1, current_node.getCoords()[1]+1, current_node.getCost()};
        result.add(insert_to_result);

        Collections.reverse(result);
        
        System.out.println("[ROW,COL]   COST\n");

        for(int[] i : result){
            System.out.println("[" + String.format("%02d",i[0]) + ",  " + String.format("%02d",i[1]) + "]    " + String.format("%02d",i[2]));
            nodes_counter++;
        }
        
        System.out.println("\nPath length: " + nodes_counter + " nodes\nSearch tree: "+ tree_length +" nodes\n\n----------------------\n");
        //System.out.println(tree_length);
        
        return result;
        
    }
    
    
    //Arxikopoiei ton xarth me tis times apo to arxeio txt
    public static void initMap(String file_path) throws Exception{

        File file = new File(file_path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;

        int row = 0;
        
        while ((st = br.readLine()) != null){
 
            for(int col = 0; col < MAP_COLS; col++){
                
                map[row][col] = st.charAt(col);
                
            }
        
            row++;
            
        }
        
    }
    
    
    //Arxikopoiei constants
    public static void initConstants(){
        
        for ( int i = 0; i < MAP_ROWS; ++i ) {
            for ( int j = 0; j < MAP_COLS; ++j ) {
                if ( map[i][j] == 'S' ) {
                    START_ROW = i;
                    START_COL = j;
                } 
                if ( map[i][j] == 'G' ) {
                    GOAL_ROW = i;
                    GOAL_COL = j;
                } 
            }
        }

    }
    
    
    //Typwnei ton xarth
    public static void printMap(){
        
        String map_str = Arrays.deepToString(map).replace("], ", "]\n");
        map_str = map_str.replace("[", "");
        map_str = map_str.replace("]", "");
        map_str = map_str.replace(", ", "");
        map_str = map_str.replace("O", " ");
        System.out.println("----------------------\n\nMap:\n\n" + map_str + "\n");
        
    }
    
        
}
    

//Xrhsimopoieitai sthn priority queue tou UCS gia na katatassei me vash ta kosth
class CostComparator implements Comparator<Node>{
    
    public int compare(Node n1, Node n2) {
        if (n1.getCost() > n2.getCost())
            return 1;
        else if (n1.getCost() < n2.getCost())
            return -1;
        return 0;
    }
        
}


/*
Xrhsimopoieitai sthn priority queue tou Astar gia na katatassei me vash ta heuristics.
Se ayto to shmeio prostethike o paragontas tyxaiothtas gia ton Astar se periptwsh
pou yparxei isopalia sta heuristics
*/
class HeuristicComparator implements Comparator<Node>{
   
    Random rand = new Random();
   
    public int compare(Node n1, Node n2) {
        if (n1.getHeuristic() < n2.getHeuristic())
            return 1;
        else if (n1.getHeuristic() > n2.getHeuristic())
            return -1;
        else if (n1.getHeuristic() == n2.getHeuristic())
            return rand.nextInt()*2-1;
        return 0;
    }
       
}


