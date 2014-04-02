/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

package AI;

import Board.Cell;
import Board.CellType;
import Board.FieldController;
import Board.WorkField;

import java.util.ArrayList;

/**
 * User: Lux
 * Date: 27.11.13
 * Time: 21:50
 */

public class Brain {

    private ArrayList<Node> opened;
    private Node start;
    private Node finish;
    private Node nVert;
    private WorkField workField;
    private FieldController controller;

    public Brain(WorkField workField, Cell start, Cell finish) {
        this.finish = new Node(finish);
        this.workField = workField;
        this.start = new Node(start);
        nVert = new Node(start);
        calcFitness(nVert); // Calc fitness for the initial vertex
        opened = new ArrayList<>();
        opened.add(nVert);
    }

    public boolean findSolution(){
        int maxOpenedNodesAmount = workField.getCellsAmount();
        Node nextNode;
        while(!getMinFitnessNode().equals(finish)){
            if(opened.isEmpty())
                return false;

            nextNode = getMinFitnessNode();
            nextNode.setParent(nVert);
            nVert = nextNode;
            opened.remove(nVert);
            expandNode(nVert);
            if (opened.size() > maxOpenedNodesAmount)
                opened.remove(getMaxFitnessNode());
        }
        return true;
    }

    private void expandNode (Node node) {
        Node neighbourNode;
        Cell neighbourCell;
        for (int i = node.getY() - 1; i <= node.getY() + 1; i++) {
            for (int j = node.getX() - 1; j <= node.getX() + 1; j++) {
                if (i != node.getY() || j != node.getX()) {
                    neighbourCell = workField.getCell(j, i);
                    if (neighbourCell.getType() == CellType.OBSTACLE) {
                        continue;
                    }
                    neighbourNode = new Node(neighbourCell);
                    calcFitness(neighbourNode);
                    opened.add(neighbourNode);
                }
            }
        }
    }

    private Node getMinFitnessNode(){
        Node minFitnessVertex = opened.get(0);
        for(Node vertex : opened){
            if(minFitnessVertex.getFitness() > vertex.getFitness()){
                minFitnessVertex = vertex;
            }
        }
        return minFitnessVertex;
    }

    private Node getMaxFitnessNode(){
        Node maxFitnessVertex = opened.get(0);
        for(Node vertex : opened){
            if(maxFitnessVertex.getFitness() < vertex.getFitness()){
                maxFitnessVertex = vertex;
            }
        }
        return maxFitnessVertex;
    }

    private void calcFitness(Node vertex) {
        int dx = Math.abs(finish.getX() - vertex.getX());
        int dy = Math.abs(finish.getY() - vertex.getY());
        vertex.setFitness(dx + dy);
    }

    /**
     * Restore the solution path using parent references.
     * @return sequence of nodes, representing the path from start to the end.
     */
    public ArrayList<Node> getSolution() {
        ArrayList<Node> solution = new ArrayList<>();
        solution.add(nVert);
        while(!nVert.getParent().equals(start)){
            nVert = nVert.getParent();
            solution.add(nVert);
        }
        return solution;
    }
}