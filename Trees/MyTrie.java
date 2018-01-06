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
public class MyTrie {
    
    private MyTrieNode root;
    
    public MyTrie(){
        
        root = new MyTrieNode();
    }
    
    /**
     * Adds a word to the Trie
     * @param word 
     */
    public void addWord(String word){
        
        root.AddWord(word.toLowerCase());
        
    }
    /**
     * Get the words in the
     * @param words
     * @return 
     */
    public List getWords(String prefix){
        
        //Find the Node which represnts the last node of the prefix.
        MyTrieNode lastNode = root;
        
        for(int i=0;i<prefix.length();i++){
            
            lastNode = lastNode.getNode(prefix.toLowerCase().charAt(i));
            
            if(lastNode == null){
                return new ArrayList();
            }
        }
        
        return lastNode.getWords();
        
    }
    
    
}
