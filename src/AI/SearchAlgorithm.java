package AI;

import Board.WorkField;

import java.util.ArrayList;

/**
 * Created by Lux on 05.04.2014.
 */
public abstract class SearchAlgorithm {
    protected ArrayList<Node> opened;
    protected Node start;
    protected Node finish;
    protected Node nVert;
    protected WorkField workField;
    protected float execTime;


    // package - local methods
    abstract Node getMinFitnessNode();
    abstract Node getMaxFitnessNode();
    abstract void expandNodeOD(Node node);
    abstract void calcFitness(Node node);
    //
    public abstract boolean findSolution();

    /**
     * Restore the solution path using parent references.
     * @return sequence of nodes, representing the path from start to the end.
     */
    public ArrayList<Node> getSolution(){
        ArrayList<Node> solution = new ArrayList<>();
        solution.add(nVert);
        while(!nVert.getParent().equals(start)){
            nVert = nVert.getParent();
            solution.add(nVert);
        }
        return solution;
    }

    public ArrayList<Node> getOpened() {
        return opened;
    }

    public float getExecTime() {
        return execTime / 1000000000; // 1 nanosecond is 1 * 10pow(-9)
    }
}
