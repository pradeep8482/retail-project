package com.graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Graph {

    private final int MAX_VERTS = 20;
    private Vertex[] vertexList;
    private int[][] adjMatrix;
    public int nVertex;
    private Stack<Integer> stack;
    private Queue<Integer> queue;
    public Graph(){
        vertexList = new Vertex[MAX_VERTS];
        adjMatrix = new int[MAX_VERTS][MAX_VERTS];
        nVertex = 0;
        for(int i=0;i<MAX_VERTS;i++){
            for(int k=0;k<MAX_VERTS;k++){
                adjMatrix[i][k] = 0;
            }
        }
        stack = new Stack();
        queue = new LinkedList<>();
    }

    public void dfs(){
        vertexList[0].wasVisited =true;
        stack.push(0);

        while(!stack.isEmpty()){
          int v =  getAdjacentUnvisitedVertex(stack.peek());
          if(v == -1){
              stack.pop();
          }else{
              vertexList[v].wasVisited = true;
              stack.push(v);
          }
        }

        for(int i=0;i<nVertex;i++){
          vertexList[i].wasVisited = false;
        }
    }

    public void bfs(){

        vertexList[0].wasVisited = true;
        queue.add(0);
        int v2;
        while(!queue.isEmpty()){
            int v1 = queue.remove();
            while((v2=getAdjacentUnvisitedVertex(v1))!=-1){
                vertexList[v2].wasVisited = true;
                queue.add(v2);
            }

        }

        for(int i=0;i<nVertex;i++){
            vertexList[i].wasVisited=false;
        }
    }

    public int getAdjacentUnvisitedVertex(int v){
        for(int i=0;i<nVertex;i++){
            if(adjMatrix[v][i]!=0 && vertexList[v].wasVisited==false){
                return i;
            }
        }
        return -1;
    }
    public void addVertex(char label){
      vertexList[nVertex++] = new Vertex(label);
    }
    public static class Vertex{
        public char label;
        public boolean wasVisited;

        public Vertex(char label){
            this.label = label;
        }
    }
}
