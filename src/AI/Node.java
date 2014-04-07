package AI;

import Board.Cell;

/**
 * Created by Lux on 02.04.2014.
 */
public class Node {

    private Cell cell;
    private Node parent;
    private int fitness;

    public Node(Cell cell) {
        this.cell = cell;
        fitness = Integer.MAX_VALUE;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Node))
            return false;

        Node node = (Node) other;
        return cell.getxPos() == node.getX() && cell.getyPos() == node.getY();
    }

    @Override
    public String toString() {
        return String.valueOf(fitness);
    }

    public boolean hasParent() {
        return !parent.equals(this);
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getX() {
        return cell.getxPos();
    }

    public int getY() {
        return cell.getyPos();
    }
}