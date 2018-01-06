/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees;

import java.util.Scanner;

/**
 *
 * @author alokps
 */
public class BinaryTree {
    
    public static void main(String[] args){
        
        Scanner scan = new Scanner(System.in);
        
        //Create an Object of BT
        BT mybt = new BT();
        
        char Ch;
        
        do{
            
//            System.out.println("Binary Tree Ops");
//            System.out.println("1. Insert");
//            System.out.println("2. Search");
//            System.out.println("3. Count Nodes");
//            System.out.println("4. Empty Nodes");
//            System.out.println("5. PostOrder Traversal");
//            System.out.println("6. PreOrder Traversal");
//            System.out.println("7. InOrder Traversal");
//            
//            int choice = scan.nextInt();
            
            //switch(choice){
                
                //case 1:
                    System.out.println("Enter Integer to insert");
                    //mybt.insert(scan.nextInt());
                    mybt.insert(10);
                    mybt.insert(20);
                    mybt.insert(30);
                    mybt.insert(40);
                    mybt.insert(50);
                    mybt.insert(60);
                    mybt.insert(70);
                    mybt.insert(80);
                    mybt.insert(90);
                    
                    //break;
                //case 2:
                    System.out.println("Enter Integer to search");
                    System.out.println("Search result is :" + mybt.searchTree(scan.nextInt()));
                    //break;
                //case 3:
                    System.out.println("Count value of nodes is :" + mybt.CountNodes());
                    //break;
                //case 4:
                    System.out.println("Empty Status of nodes is :" + mybt.isEmpty());
                    //break;
                //case 5:
                    System.out.println("PostOrder Traversal");
                    mybt.PostorderTraversal();
                    //break;
                //case 6:
                    System.out.println("PreOrder Traversal");
                    mybt.PreorderTraversal();
                    //break;
                //case 7:
                    System.out.println("InOrder Traversal");
                    mybt.InorderTraversal();
                    //break;
                //default:
                    //break;
            //}
            
            System.out.println("\n\nDo you want to continue (Type y or n) \n");
           
           Ch = scan.next().charAt(0);
            
        }while(Ch == 'Y' || Ch == 'y');
           
    }
    
}   
