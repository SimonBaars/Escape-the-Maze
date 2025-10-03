package com.escapethemaze.game.pathfinding;

import java.util.ArrayList;

public class Node {
    public float x, y;
    public boolean walkable = true;
    public ArrayList<Node> neighbors = new ArrayList<Node>();
    public Node parent;
    public float g, h, f;
    
    public Node(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void calculateF() {
        f = g + h;
    }
    
    public float distanceTo(Node other) {
        float dx = x - other.x;
        float dy = y - other.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
}
