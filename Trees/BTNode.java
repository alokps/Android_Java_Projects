/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees;

/**
 *
 * @author alokps
 */
public class BTNode {
    
    BTNode LeftSubTree;
    BTNode RightSubTree;
    int data;
    
    /**
     * Constructors
     */
    public BTNode(){
        
        LeftSubTree = null;
        RightSubTree = null;
        data = 0;       
    }
    
    public BTNode(int n){
        
        LeftSubTree = null;
        RightSubTree = null;
        data = n;       
    }
    
    /**
     * Helper Methods
     */
    
    public void setLeftNode(BTNode n){
        LeftSubTree = n;
    } 
    
    public void setRightNode(BTNode n){    
        RightSubTree = n;
    }
    
    public BTNode getLeftNode(){
        return LeftSubTree;
    }
    
    public BTNode getRightNode(){
        return RightSubTree;
    }
    
    public void setData(int d){
        data = d;
    }
    
    public int getData(){
        return data;
    }
    
}
    