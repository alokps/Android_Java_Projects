/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees;

/**
 *Often we call it as BST, is a type of Binary tree which has a spe­cial property.
 * Nodes smaller than root goes to the left of the root and Nodes greater than root 
 * goes to the right of the root.
 * @author alokps
 */
public class BST {
    
    public static BSTNode root;
    
    public BST(){
        this.root = null;
    }
    
    /**
     * 
     *   Its very simple operation to perform.
     *   start from the root and com­pare root.data with n
     *   if root.data is greater than n that means we need to go to the left of the root.
     *   if root.data is smaller than n that means we need to go to the right of the root.
     *   if any point of time root.data is equal to the n then we have found the node, return true.
     *   if we reach to the leaves (end of the tree) return false, we didn’t find the element
     * @param id
     * @return 
     */
    
    public boolean find(int id){
    
        BSTNode current = root;
        
        while(current != null){
            
            if(current.data == id){
                return true;
            }else if(current.data > id){
                current = current.Left;
            }else{
                current = current.Right;
            }            
        }
        
        return false;
    }
    
    /**
     * Very much similar to find() operation.
     * To insert a node our first task is to find the place to insert the node.
     * Take cur­rent = root .
     * start from the cur­rent and com­pare root.data with n
     * if current.data is greater than n that means we need to go to the left of the root.
     * if current.data is smaller than n that means we need to go to the right of the root.
     * if any point of time cur­rent is null that means we have reached to the leaf node, 
     * insert your node here with the help of parent node. 
     * @param id 
     */
    
    public void insert(int id){
        
        BSTNode node = new BSTNode(id);
        
        if(root == null){ 
            root = node;
            return; 
        }
        
        BSTNode current = root;
        BSTNode parent  = null;
        
        while(true){
            parent = current;
            if(current.data > id){
                
                current = current.Left;
                if(current == null){
                    parent.Left = node;
                    return;
                }
                
            }else{
                
                current = current.Right;
                if(current == null){
                    parent.Right = node;
                    return;
                }   
            }   
        }   
    }
    
    public void display(BSTNode root){
        
        if(root != null){
            display(root.Left);
            System.out.println(" "+root.data);
            display(root.Right);
        }
    }
    
    public BSTNode getSuccessor(BSTNode deleteNode){
        
        BSTNode successor = null;
        BSTNode successorParent = null;
        
        BSTNode current = deleteNode.Right;
        
        while(current != null){
         
            successorParent = successor;
            successor = current;
            current = current.Left;   
        }
        
        //check if successor has the right child, it cannot have left child for sure
	// if it does have the right child, add it to the left of successorParent.
        //successsorParent
        if(successor != deleteNode.Right){
            successorParent.Left = successor.Right;
            successor.Right = deleteNode.Right;         
        }
        return successor;
    }
    
    public boolean delete(int id){
     
        BSTNode parent = root;
        BSTNode current = root;
        
        boolean isLeftChild = false;
        
        while(current.data != id){
            parent = current;
            
            if(current.data > id){
                isLeftChild = true;
                current = current.Left;
            }else{
                isLeftChild = false;
                current = current.Right;
            }
            
            if(current==null){
                return false;
            }  
        }
        //If I am here that means we have found Node
        //Case 1: If node to be deleted has no children
        if((current.Left == null) && (current.Right == null)){
            
            if(current == root){
                root = null;
            }
            else if(isLeftChild==true){
                parent.Left = null;
            }else{
                parent.Right = null;
            }
        }
        
        //Case 2:If Node to be deleted has only one child
        else if(current.Right == null){
            
            if(current == root){
                root = null;
            }
            else if(isLeftChild == true){
                parent.Left = current.Left;
            }else{
                parent.Right = current.Left;
            }
            
        }
        
        else if(current.Left == null){
            
            if(current == root){
                root = null;
            }
            else if(isLeftChild == true){
                
                parent.Left = current.Right;
                
            }else{
                parent.Right = current.Right;
            }
            
        }
        else if(current.Left!=null && current.Right!=null){
            
            BSTNode successor = getSuccessor(current);
            
            if(current == root){
                root = successor;
            }
            else if(isLeftChild){
                parent.Left = successor;
            }else{
                parent.Right = successor;
            }
            successor.Left = current.Left;
            
        }
        
        
        return true;
    }
    
   public static void main(String[] args){
       BST mynode = new BST();
       mynode.insert(2);
       mynode.insert(1);
       mynode.insert(0);
       mynode.insert(4);
       mynode.insert(5);
       //mynode.insert(10);
       //mynode.insert(15);
       //mynode.insert(20);
       //mynode.delete(5);
       mynode.display(root);
       System.out.println(mynode.isHeightBalanced(root));
       
   }
    /**
     * 
How to determine if a binary tree is height-balanced?
A tree where no leaf is much farther away from the root than any other leaf. 
* Different balancing schemes allow different definitions of “much farther” and 
* different amounts of work to keep them balanced.Consider a height-balancing 
* scheme where following conditions should be checked 
* to determine if a binary tree is balanced.
An empty tree is height-balanced. A non-empty binary tree T is balanced if:
1) Left subtree of T is balanced
2) Right subtree of T is balanced
3) The difference between heights of left subtree and right subtree is not more than 1.

The above height-balancing scheme is used in AVL trees.
     * @param root
     * @return 
     */
    public boolean isHeightBalanced(BSTNode root){
        
        int lt;
        int rt;
        
        //If the tree is empty return true
        if(root == null){
            return true;
        }
        
        lt = height(root.Left);
        rt = height(root.Right);
        
        if(Math.abs(lt-rt)<=1 && isHeightBalanced(root.Left) && isHeightBalanced(root.Right)){
            return true;
        }
        
        
        return false;
    }
    
    int height(BSTNode node){
        
        //If the tree is empty return 0
        if(node == null){
            return 0;
        }
        
        //If tree is not empty return 1 plus max of left sub tree
        //and right sub tree
        return 1+max(height(node.Left),height(node.Right));
        
    }
    
    int max(int a, int b){
        return(a >= b) ? a:b;
    }
    
}
