package com.harrykay.smartgolems.pathfinding;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static com.harrykay.smartgolems.pathfinding.Action.getActions;


public class ScaffoldNavigator {

    public static ArrayList<Action> findSolution(Goal goal, State initialState) {

        PriorityQueue<Node> unExplored = new PriorityQueue<>(new NodeComparator());
        ArrayList<State> explored = new ArrayList<>();
        Node head = new Node(new State(initialState));
        unExplored.add(head);

        if (goal.reached(initialState.entityPosition)) {
            System.out.println("initial state == desired state");
            return new ArrayList<>();
        }

        while (!unExplored.isEmpty()) {
            Node current = unExplored.poll();
            explored.add(current.state);
            ArrayList<Action> directions = getActions(current.state);

            for (Action action : directions) {
                Node childNode = new Node(current, action, goal);

                if (explored.contains(childNode.state)) {
                    continue;
                }

                if (goal.reached(childNode.state.entityPosition)) {
                    System.out.println("Solution found.");
                    ArrayList<Action> solution = new ArrayList<>();
                    for (int i = 0, g = childNode.g; i < g; ++i) {
                        solution.add(childNode.action);
                        childNode = childNode.parent;
                    }
                    return solution;
                }

                unExplored.add(childNode);
            }
        }

        System.out.println("No solution");
        return new ArrayList<>();
    }
}
