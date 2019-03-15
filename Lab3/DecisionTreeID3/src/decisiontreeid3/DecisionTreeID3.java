package decisiontreeid3;

import java.util.*;
import java.io.*;
import java.lang.*;

public class DecisionTreeID3 
{
    
    HashMap<String,Integer> attributes=new HashMap<String,Integer>();
    HashMap<String,Integer> lables=new HashMap<String,Integer>();
    HashMap<String,Integer> duprmv=new HashMap<String,Integer>();
    ArrayList<ArrayList<String>> database = new ArrayList<ArrayList<String>>();
    ArrayList<String> StoreAtt=new ArrayList<String>();
 
    
    Queue<Node> queue = new LinkedList<Node>();
            
    double infoD=0.0,count=0;
    int cntv;      
    void run()
    {
          getInput("car.data.txt");
          cntv=6;
          StoreAtt.add("buying");
          StoreAtt.add("maint");
          StoreAtt.add("doors");
          StoreAtt.add("persons");
          StoreAtt.add("lug boot");
          StoreAtt.add("safety");
          
          attributes.put("buying",0);
          attributes.put("maint",1);
          attributes.put("doors",2);
          attributes.put("persons",3);
          attributes.put("lug boot",4);
          attributes.put("safety",5);
      
          
         
          Node DTree=new Node();
          DTree.SetTable(database);
          for(int i=0;i<DTree.table.size();i++)
          {
              if(lables.get(DTree.table.get(i).get(cntv))==null)
                  lables.put(DTree.table.get(i).get(cntv),1);
              else
                  lables.put(DTree.table.get(i).get(cntv),lables.get(DTree.table.get(i).get(cntv))+1);
          }
          infoD=InformationGain(lables,count);
      
          ArrayList<Double> att=new ArrayList<Double>();
          att=InfoForAllAttributes(DTree.table);
          double x=infoD-att.get(0);
       
          int ind=0;
          for(int i=1;i<cntv;i++)
          {
              double xy=infoD-att.get(i);
       
              if(x<xy)
              {
                  x=xy;
                  ind=i;
              }
          }
          DTree.attribute=StoreAtt.get(ind);

          HashMap<String,Integer> ch=new HashMap<String,Integer>();
          for(int i=0;i<DTree.table.size();i++)
          {
              if(ch.get(DTree.table.get(i).get(ind))==null)
              {
                  ch.put(DTree.table.get(i).get(ind),1);
                  Node child=new Node();
                  DTree.children.add(child);
                  DTree.value.add(DTree.table.get(i).get(ind));
              }     
          }

          queue.add(DTree);
          bfs();
          
    }
    
    void bfs()
    {
           while (queue.peek() != null) 
           {
                 Node ParentNode = queue.remove();
                 int i=0;
                 System.out.println("["+ParentNode.attribute+"]");
                 HashMap<String,Integer> tak=new HashMap<String,Integer>();
                 HashMap<Integer,ArrayList<String>> map = new HashMap<Integer,ArrayList<String>>();
                 for(Node child : ParentNode.children) 
                 {
                    
                       int ind=attributes.get(ParentNode.attribute);
                       for(int j=0;j<ParentNode.table.size();j++)
                       {
                           if(ParentNode.value.get(i).equals(ParentNode.table.get(j).get(ind)))
                           {
                                child.table.add(ParentNode.table.get(j));
                           }
                       }
                  
                       HashMap<String,Integer> hsp=new HashMap<String,Integer>();
                       for(int j=0;j<child.table.size();j++)
                       {
                            if(hsp.get(child.table.get(j).get(cntv))==null)
                            {
                                hsp.put(child.table.get(j).get(cntv),1);
                            }
                            else
                            {
                                hsp.put(child.table.get(j).get(cntv),hsp.get(child.table.get(j).get(cntv))+1);
                            }
                       }
                       if(hsp.size()<=1)
                       {
                            child.leaf=child.table.get(0).get(cntv);
                            System.out.println("      "+ParentNode.value.get(i)+"   ------->   "+"("+child.leaf+")");
                       }
                       else
                       {
                           double infoA;
                           infoA=InformationGain(hsp,(double)child.table.size());
                           ArrayList<Double> attri=new ArrayList<Double>();
                           lables=hsp;
                           attri=InfoForAllAttributes(child.table);
                           double x=-112.00;
                       
                           int index=-1;
                           for(int j=0;j<cntv;j++)
                           {
                                double xy=infoA-attri.get(j);
                   
                                if(x<xy )
                                {
                                  x=xy;
                                  index=j;
                                }
                            }
                            if(map.get(index)==null)
                            {
                                 map.put(index,new ArrayList<String>());
                            }
                            map.get(index).add(ParentNode.value.get(i));
                            child.attribute=StoreAtt.get(index);
                            tak.put(ParentNode.value.get(i), i);
                       }
            
                       i++;
                 }
                 for(Integer it : map.keySet()) 
                 {
                      int cnt=map.get(it).size(),l=1;
                      if(map.get(it).size()==i)
                      {
                          cnt=i/2;
                          l=2;
                      }
                      int m=0;
                      for(int k=0;k<l;k++)
                      {
                            int chk=0;
                            ArrayList<ArrayList<String>> ta=new ArrayList<ArrayList<String>>();
                            ArrayList<String> pr=new ArrayList<String>();
                            for(m=m;m<map.get(it).size();m++)
                            {
                                pr.add(map.get(it).get(m));
                                for(ArrayList<String> ars : ParentNode.children.get(tak.get(map.get(it).get(m))).table)
                                {
                                    ta.add(ars);
                                }
                                chk++;
                                if(chk==cnt)
                                {
                                    cnt=i-cnt;
                                    m++;
                                    break;
                                }
                            }
                
                            System.out.println("      "+pr+"   -------->   "+"["+StoreAtt.get(it)+"]");
                            Node ch=new Node();
                            ch.table=ta;
                            ch.attribute=StoreAtt.get(it);
               
                            HashMap<String,Integer> chld=new HashMap<String,Integer>();
                            for(int j=0;j<ta.size();j++)
                            {
                               if(chld.get(ta.get(j).get(it))==null)
                               {
                                  chld.put(ta.get(j).get(it),1);
                                  Node childnew=new Node();
                                  ch.children.add(childnew);
                                  ch.value.add(ta.get(j).get(it));
                               }    
                            }
                            for(String st : pr)
                            {
                                ParentNode.children.set(tak.get(st),ch);
                            }
                            queue.add(ch);
                      }
                 }
                 
           }
    }
    
    ArrayList<Double> InfoForAllAttributes(ArrayList<ArrayList<String>> data)
    {
          //System.out.println(lables);
          ArrayList<Double> res=new ArrayList<Double>();
          for(int j=0;j<cntv;j++)
          {   
               HashMap<String,Integer> mp=new HashMap<String,Integer>();
               HashMap<String,Integer> Ind=new HashMap<String,Integer>();
               HashMap<String,Integer> lab=new HashMap<String,Integer>();
               for(int i=0;i<data.size();i++)
               {
                   if(mp.get(data.get(i).get(j))==null)
                         mp.put(data.get(i).get(j),1);
                   else
                         mp.put(data.get(i).get(j),mp.get(data.get(i).get(j))+1);
               }
               //System.out.println(mp.size());
               int x=0;
               for(String st : mp.keySet())
               {
                    //System.out.println(st);
                    Ind.put(st,x);
                    x++;
               }
               int arr[][]=new int[lables.size()+1][x+1];
               for(int k=0;k<lables.size();k++)
                   Arrays.fill(arr[k],0);
               x=0;
               for(String st : lables.keySet())
               {
                   lab.put(st,x);
                   for(int i=0;i<data.size();i++)
                   {
                       if(st.equals(data.get(i).get(cntv)))
                           arr[x][Ind.get(data.get(i).get(j))]++;
                   }
                   x++;
               }
               double sum=0.0;
               for(String mt : mp.keySet())
               {
                   HashMap<String,Integer> sd=new HashMap<String,Integer>();
                   for(String st : lables.keySet())
                   {
                       sd.put(st,arr[lab.get(st)][Ind.get(mt)]);
                   }
                   double res1=0.0;
                   res1=InformationGain(sd,(double)mp.get(mt));
            
                   res1=((double)mp.get(mt)/(double)data.size())*res1;
                   sum+=res1;
               }
         
               res.add(sum);
          }
          return res;
    }
    double InformationGain(HashMap<String,Integer> hs,double total)
    {
      
           double res=0.0,val;
           for(String st : hs.keySet())
           {
          
               val=hs.get(st);
               if(val==0)
               {
                   continue;
               }
               val=(val/total);
               res+=val*(Math.log(val)/Math.log(2));
           }
         return -res;  
    }
    void fillDatabase(String input) 
    {
        String[] split = input.split(",");
        ArrayList<String> transaction = new ArrayList<String>();
        for (String item : split) {
            transaction.add(item);
        }
        database.add(transaction);
    }
    
    void getInput(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String input;
            while ((input = br.readLine()) != null) {
               count++; 
               fillDatabase(input);   
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) 
    {
        new DecisionTreeID3().run();
    }
    
}
