package mazeproj;

public class Node {
    
    private int[] coords = new int[2];
    private char state;
    private Node parent_node;
    private int cost;
    private int depth;
    private int heuristic;
    
    public Node(){
        
        coords = new int[]{0,0};
        state = ' ';
        parent_node = null;
        cost = 0;
        depth = 0;
        heuristic = 0;
        
    }
    
    public Node(int row, int col, char s, int c, int d, int h){
        
        coords = new int[]{row,col};
        state = s;
        parent_node = new Node();
        cost = c;
        depth = d;
        heuristic = h;
        
    }
    
    public int[] getCoords(){
        
        return coords;
        
    }
    
    public void setCoords(int row, int col){
        
        coords = new int[]{row,col};
        
    }
    
    public char getState(){
        
        return state;
        
    }
    
    public void setState(char s){
        
        state = s;
        
    }
    
    public Node getParentNode(){
        
        return parent_node;
        
    }
    
    public void setParentNode(Node pn){
        
        parent_node = pn;
        
    }
    
    public int getCost(){
        
        return cost;
        
    }
    
    public void setCost(int c){
        
        cost = c;
        
    }
    
    public int getDepth(){
        
        return depth;
        
    }
    
    public void setDepth(int d){
        
        depth = d;
        
    }
    
    public int getHeuristic(){
        
        return heuristic;
        
    }
    
    public void setHeuristic(int h){
        
        heuristic = h;
        
    }
    
    public String nodeToString(){
        
        return(
                state + "\n" +
                cost + "\n" +
                depth + "\n" +
                
                "\n"+"\n"
        );
        
        
    }
      
}

