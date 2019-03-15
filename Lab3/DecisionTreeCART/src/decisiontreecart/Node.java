/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontreecart;



import java.util.*;
import java.io.*;


public class Node {
    
        String attribute,leaf;
        ArrayList<Node> children;
        ArrayList<ArrayList<String>> table;
        ArrayList<ArrayList<String>> value;
        public Node()
        {
             children=new ArrayList<Node>();
             table=new ArrayList<ArrayList<String>>();
             value=new ArrayList<ArrayList<String>>();
             leaf=null;
        }
        
        public void SetTable(ArrayList<ArrayList<String>> ta)
        {
              for(int i=0;i<ta.size();i++)
              {
                  table.add(ta.get(i));
              }
        }
        
}