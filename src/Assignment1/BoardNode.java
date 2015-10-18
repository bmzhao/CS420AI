package Assignment1;

/**
 * Created by brianzhao on 10/17/15.
 */
public class BoardNode implements Comparable<BoardNode>{
    private String parentNode;
    private int cost;
    private int heuristicCost;
    private String boardState;

    public BoardNode(String parentNode, int cost, int heuristicCost, String boardState) {
        this.parentNode = parentNode;
        this.cost = cost;
        this.heuristicCost = heuristicCost;
        this.boardState = boardState;
    }


    public String getBoardState() {
        return boardState;
    }

    public void setBoardState(String boardState) {
        this.boardState = boardState;
    }

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    @Override
    public int compareTo(BoardNode o) {
        return (this.cost + this.heuristicCost) - (o.cost + o.heuristicCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardNode boardNode = (BoardNode) o;
        if (boardState == null || boardNode.boardState == null) {
            return false;
        }else {
            return boardState.equals(boardNode.boardState);
        }

    }

    @Override
    public int hashCode() {
        return boardState.hashCode();
    }
}
