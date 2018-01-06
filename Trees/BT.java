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
public class BT {
    
    private BTNode root;
    
    public BT(){
        root = null;
    }
    
    public boolean isEmpty(){
        
        return root == null;
    }
    
    public void insert(int data){
        
        root = insert(root,data);
    }
    
    private BTNode insert(BTNode node,int data){
        
        if(node == null){
            node = new BTNode(data);
        }
        else{
            
            if(node.getRightNode() == null){ 
                //node.RightSubTree = insert(node.RightSubTree,data);
                node.setRightNode(insert(node.RightSubTree,data));
            }else{
                //node.LeftSubTree = insert(node.LeftSubTree,data);
                node.setLeftNode(insert(node.LeftSubTree,data));
            }
            
        }
        
        return node;
    }
    
    /**
     * Helper Method to Count the number of Nodes in the Binary Tree
     * @return 
     */
    public int CountNodes(){
       
        return CountNodes(root);
    }
    
    private int CountNodes(BTNode r){
               
        if(r == null){
            return 0;
        }else{
            
           int n = 1;
            n += CountNodes(r.getLeftNode());
            n += CountNodes(r.getRightNode());
            return n;
        }
    }
    
    /**
     * Helper Method to Search the Binary Tree
     * @param val
     * @return 
     */
    public boolean searchTree(int val){
        
        return searchTree(root, val);
    }
    
    private boolean searchTree(BTNode r, int val){
        
        if(r.getData() == val){
            return true;
        }
        
        if(r.getLeftNode() != null){
            
            if(searchTree(r.getLeftNode(), val)){
                return true;
            }
        }
        
        if(r.getRightNode() != null){
            
            if(searchTree(r.getRightNode(), val)){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Helper Method to search the Tree Inorder
     */
    
    public void InorderTraversal(){
        
        InorderTraversal(root);
    }
    
    private void InorderTraversal(BTNode r){
        
        if(root == null){
            return;
        }else{
        
            InorderTraversal(r.getLeftNode());

            System.out.println(r.getData() + " ");

            InorderTraversal(r.getRightNode());
       }
        
    }
    
    public void PreorderTraversal(){
        
        PreorderTraversal(root);
    }
    
    private void PreorderTraversal(BTNode r){
        
        if(root == null){
            return;
        }else{
        
            System.out.println(r.getData() + " ");
            
            PreorderTraversal(r.getLeftNode());

            PreorderTraversal(r.getRightNode());
       }
        
    }
    
    public void PostorderTraversal(){
        
        PostorderTraversal(root);
    }
    
    private void PostorderTraversal(BTNode r){
        
        if(root == null){
            return;
        }else{
        
            PostorderTraversal(r.getLeftNode());

            PostorderTraversal(r.getRightNode());
            
            System.out.println(r.getData() + " ");
       }
        
    }
    
    
}
