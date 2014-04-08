/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

package AI;

import Board.Cell;
import Board.CellType;
import Board.WorkField;

import java.util.ArrayList;

/**
 * User: Lux
 * Date: 27.11.13
 * Time: 21:50
 */

public class BeamSA extends SearchAlgorithm{

    public BeamSA(WorkField workField, Cell start, Cell finish) {
        this.finish = new Node(finish);
        this.workField = workField;
        this.start = new Node(start);
        execTime = 0;
        nVert = new Node(start);
        calcFitness(nVert); // Calc fitness for the initial vertex
        opened = new ArrayList<>();
        opened.add(nVert);
    }

    @Override
    public boolean findSolution(){
        Node nextNode, prevNode = null;
        float startTime = System.nanoTime();
        while(!getMinFitnessNode().equals(finish)){
            if(opened.isEmpty()) {
                return false;
            }
            nextNode = getMinFitnessNode();
            if (prevNode != null && nextNode.equals(prevNode.getParent())) { // can't find solution.
                return false;
            }
            prevNode = nextNode;
            nextNode.setParent(nVert);
            nVert = nextNode;
            opened.remove(nVert);
            expandNode(nVert);
        }
        execTime = System.nanoTime() - startTime;
        return true;
    }

    // package - local
    @Override
    void expandNode (Node node) {
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

    //package - local
    @Override
    Node getMinFitnessNode() {
        Node minFitnessNode = opened.get(0);
        for (Node vertex : opened) {
            if (minFitnessNode.getFitness() > vertex.getFitness()) {
                minFitnessNode = vertex;
            }
        }
        return minFitnessNode;
    }

    //package - local
    @Override
    Node getMaxFitnessNode(){
        Node maxFitnessNode = opened.get(0);
        for(Node vertex : opened){
            if(maxFitnessNode.getFitness() < vertex.getFitness()){
                maxFitnessNode = vertex;
            }
        }
        return maxFitnessNode;
    }

    @Override
    void calcFitness(Node node) {
        int dx = Math.abs(finish.getX() - node.getX());
        int dy = Math.abs(finish.getY() - node.getY());
        node.setFitness(dx + dy);
    }
}