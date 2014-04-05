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

    // package - local methods
    abstract Node getMinFitnessNode();
    abstract Node getMaxFitnessNode();
    abstract void expandNode(Node node);
    abstract void calcFitness(Node node);
    //
    public abstract boolean findSolution();
    public abstract ArrayList<Node> getSolution();
}
