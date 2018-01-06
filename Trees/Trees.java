/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees;

import java.util.List;

/**
 *
 * @author alokps
 */
public class Trees {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        MyTrie trie1 = new MyTrie();
        trie1.addWord("HIS");
        trie1.addWord("HISTORY");
        trie1.addWord("HISTORiCAL");
        
        List mylist = trie1.getWords("HI");
        for(int i=0;i<mylist.size();i++){
            System.out.println(mylist.get(i));
        }
            
    }
    
}
