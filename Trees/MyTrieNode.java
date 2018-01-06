/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alokps
 */
public class MyTrieNode {
    
    private MyTrieNode Parent;
    private MyTrieNode[] children;
    private boolean isLeaf; //to check if any children exists
    private boolean isWord; //Does this node represnts the last character of a word
    private char character; //The character this node represents.
    
    //consructor for top level root node
    public MyTrieNode(){
        
        children = new MyTrieNode[26]; //For 26 Alphabets
        isLeaf = true;
        isWord = false;
    }
    
    //Construtcor for Child Node
    public MyTrieNode(char c){
        this();
        this.character = c;
    }
    
    protected void AddWord(String word){
        
        isLeaf = false;
        int charPos = word.charAt(0) - 'a';
        
        if(children[charPos] == null){
            
            children[charPos] = new MyTrieNode(word.charAt(0));
            children[charPos].Parent = this;
        }
        
        if(word.length() > 1){
            children[charPos].AddWord(word.substring(1));
        }else{
            
            children[charPos].isWord = true;
        }      
        
    }
    
    /**
    * Returns the child TrieNode representing the given char,
    * or null if no node exists.
    * @param c
    * @return
    */
   protected MyTrieNode getNode(char c)
   {
      return children[c - 'a'];
   }
   
   protected List getWords(){
      
       //Create a List to return
       List list = new ArrayList();
       
       //If this node represnts a word, add it
       if(isWord){
           list.add(toString());
       }
       
       if(!isLeaf){
           
           //Add any word beloning to any children
           for(int i=0;i<children.length;i++){
               
               if(children[i] != null){
                   list.addAll(children[i].getWords());
               }
           }
       }
       
       return list;
   }
    
   /**

* Gets the String that this node represents.
* For example, if this node represents the character t, whose parent
* represents the character a, whose parent represents the character
* c, then the String would be "cat".
* @return

*/

@Override
public String toString(){

    if (Parent == null)
    {
         return "";
    }
    else{
         return Parent.toString() + new String(new char[] {character});
    }

 } 
    
}
