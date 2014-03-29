/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

package AI;


import Board.WorkField;

/**
 * User: Lux
 * Date: 27.11.13
 * Time: 21:52
 */

public class GraphVertex {

    private WorkField workField;
    // metric, which represents the deepness of  vertex in graph + amount of bricks in incorrect positions
    private int fitness;
    private GraphVertex parent;
    private int level;   // vertex deepness level

    public GraphVertex(WorkField workField) {
        this.workField = workField;
        this.fitness = 0;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof GraphVertex))
            return false;
        GraphVertex vertex = (GraphVertex) other;
        int inPlace = 0;
        for (int i = 0; i < vertex.getWorkField().getRowsAmount(); i++) {
            for (int j = 0; j < vertex.getWorkField().getCollsAmount(); j++) {
                if (this.getWorkField().getCell(i, j) == vertex.getWorkField().getCell(i, j))
                    inPlace++;
            }
        }
        return inPlace == vertex.getWorkField().getBricksAmount();
    }

    @Override
    public GraphVertex clone() throws CloneNotSupportedException {
        GraphVertex target = new GraphVertex(new WorkField(workField.getCollsAmount(), workField.getRowsAmount()));
        for (int i = 0; i < target.getWorkField().getRowsAmount(); i++) {
            for (int j = 0; j < target.getWorkField().getCollsAmount(); j++) {
                target.getWorkField().setCell(i, j, workField.getCell(i, j));
            }
        }
        target.setFitness(fitness);
        target.setParent(parent);
        return target;
    }

    @Override
    public String toString() {
        return String.valueOf(this.fitness);
    }

    public boolean hasParent() {
        return !parent.equals(this);
    }

    public GraphVertex getParent() {
        return parent;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setParent(GraphVertex parent) {
        this.parent = parent;
    }

    public WorkField getWorkField() {
        return workField;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int getLevel() {
        return level;
    }
}