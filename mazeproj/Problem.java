package mazeproj;

public class Problem {
    
    private char[][] map = new char[11][13];
    private int[] start = new int[2];
    private int[] goal = new int[2];
    
    public Problem(){
        
        map = new char[][]{};
        start = new int[]{0,0};
        goal = new int[]{0,0};
        
    }
    
    public void setMap(char arr[][]){
        
        map = arr;
        
    }
    
    public int[] getStart(){
        
        return start;
        
    }
    
    public void setStart(int row_start, int col_start){
        
        start = new int[]{row_start, col_start};
        
    }
    
    public int[] getGoal(){
        
        return goal;
        
    }
    
    public void setGoal(int row_goal, int col_goal){
        
        goal = new int[]{row_goal, col_goal};
        
    }
    
}
