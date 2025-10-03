package com.escapethemaze.game.pathfinding;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Pathfinder {
    public ArrayList<Node> nodes = new ArrayList<Node>();
    public int offsetX = 0;
    public int offsetY = 0;
    
    public void setCuboidNodes(int width, int height, int spacing) {
        nodes.clear();
        
        // Create grid of nodes
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Node node = new Node(x * spacing + offsetX, y * spacing + offsetY);
                nodes.add(node);
            }
        }
        
        // Connect neighbors
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Node current = nodes.get(y * width + x);
                
                // Add 4-directional neighbors
                if (x > 0) {
                    current.neighbors.add(nodes.get(y * width + (x - 1)));
                }
                if (x < width - 1) {
                    current.neighbors.add(nodes.get(y * width + (x + 1)));
                }
                if (y > 0) {
                    current.neighbors.add((y - 1) * width + x);
                }
                if (y < height - 1) {
                    current.neighbors.add((y + 1) * width + x);
                }
            }
        }
    }
    
    public ArrayList<Node> findPath(Node start, Node goal) {
        if (start == null || goal == null) {
            return new ArrayList<Node>();
        }
        
        // Reset all nodes
        for (Node node : nodes) {
            node.g = Float.MAX_VALUE;
            node.h = 0;
            node.f = 0;
            node.parent = null;
        }
        
        PriorityQueue<Node> openSet = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node a, Node b) {
                return Float.compare(a.f, b.f);
            }
        });
        
        ArrayList<Node> closedSet = new ArrayList<Node>();
        
        start.g = 0;
        start.h = start.distanceTo(goal);
        start.calculateF();
        openSet.add(start);
        
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            
            if (current == goal) {
                return reconstructPath(current);
            }
            
            closedSet.add(current);
            
            for (Node neighbor : current.neighbors) {
                if (!neighbor.walkable || closedSet.contains(neighbor)) {
                    continue;
                }
                
                float tentativeG = current.g + current.distanceTo(neighbor);
                
                if (tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = neighbor.distanceTo(goal);
                    neighbor.calculateF();
                    
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        
        return new ArrayList<Node>(); // No path found
    }
    
    private ArrayList<Node> reconstructPath(Node current) {
        ArrayList<Node> path = new ArrayList<Node>();
        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }
        return path;
    }
}
