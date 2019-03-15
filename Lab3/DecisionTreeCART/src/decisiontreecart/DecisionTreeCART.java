package decisiontreecart;


import java.util.*;
import java.io.*;
import java.lang.*;


public class DecisionTreeCART 
{
    
    HashMap<String,Integer> attributes=new HashMap<String,Integer>();
    HashMap<String,Integer> lables=new HashMap<String,Integer>();
    ArrayList<ArrayList<String>> database = new ArrayList<ArrayList<String>>();
    ArrayList<String> StoreAtt=new ArrayList<String>();
    //int visit[]=new int[7];
    
    Queue<Node> queue = new LinkedList<Node>();
            
    double giniD=0.0,count=0;
    int cntv;   
    
    void run()
    {
        
           
           getInput("car.data.txt");
          cntv=6;
          StoreAtt.add("buying");StoreAtt.add("maint");
          StoreAtt.add("doors");StoreAtt.add("persons");
          StoreAtt.add("lug boot");StoreAtt.add("safety");
          attributes.put("buying",0);attributes.put("maint",1);
          attributes.put("doors",2);attributes.put("persons",3);
          attributes.put("lug boot",4);attributes.put("safety",5);
          
         
          Node DTree=new Node();
          DTree.SetTable(database);
          for(int i=0;i<DTree.table.size();i++)
          {
              if(lables.get(DTree.table.get(i).get(cntv))==null)
                  lables.put(DTree.table.get(i).get(cntv),1);
              else
                  lables.put(DTree.table.get(i).get(cntv),lables.get(DTree.table.get(i).get(cntv))+1);
          }
          giniD=GiniIndex(lables,count);
          InfoForAllAttributes(DTree.table,DTree);
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
                 //System.out.println("        ["+ParentNode.value.get(0)+"]     "+ParentNode.value.get(1));
                 for(Node child:ParentNode.children)
                 {
                       int ind=attributes.get(ParentNode.attribute);
                       for(int k=0;k<ParentNode.value.get(i).size();k++)
                       {
                          for(int j=0;j<ParentNode.table.size();j++)
                          {
                              if(ParentNode.value.get(i).get(k).equals(ParentNode.table.get(j).get(ind)))
                              {
                                child.table.add(ParentNode.table.get(j));
                              }
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
                           System.out.print("      "+ParentNode.value.get(i)+"   ------->   ");
                           InfoForAllAttributes(child.table,child);
                           queue.add(child);   
                       }
                       i++;
                 }
           }
    }
    
    void InfoForAllAttributes(ArrayList<ArrayList<String>> data,Node Tree)
    {
          String attri="";
          ArrayList<String> left=new ArrayList<String>();
          ArrayList<String> right=new ArrayList<String>();
          double val=500.0;
          for(int j=0;j<cntv;j++)
          {   
              ArrayList<String> spt=new ArrayList<String>();
              HashMap<String,Integer> mp=new HashMap<String,Integer>();
              int total=0;
              for(int i=0;i<data.size();i++)
              {
                   if(mp.get(data.get(i).get(j))==null)
                         mp.put(data.get(i).get(j),1);
                   else
                         mp.put(data.get(i).get(j),mp.get(data.get(i).get(j))+1);
              }
              for(String st: mp.keySet())
              {
                  spt.add(st);
                  total+=mp.get(st);
              }
              int n=spt.size();
              ArrayList<ArrayList<String>> sub=new ArrayList<ArrayList<String>>();
              ArrayList<ArrayList<String>> diff=new ArrayList<ArrayList<String>>();
              ArrayList<Integer> count=new ArrayList<Integer>();
              for(int i=1;i<(1<<n)-1;i++) 
	      { 
                 ArrayList<String> tmp=new ArrayList<String>(); 
                 ArrayList<String> tmp1=new ArrayList<String>(); 
                 HashMap<String,Integer> hsp=new HashMap<String,Integer>();
                 Integer x=0;
	         for(int k=0;k<n;k++) 
                 {
	            if ((i&(1<<k))>0) 
                    {
                        tmp.add(spt.get(k)); 
                        hsp.put(spt.get(k),1);
                        x+=mp.get(spt.get(k));
                    } 
                 }
                 sub.add(tmp);
                 count.add(x);
                 //System.out.println(" 1 "+tmp);
                 for(int k=0;k<spt.size();k++)
                 {
                      if(hsp.get(spt.get(k))==null)
                      {
                          tmp1.add(spt.get(k));
                      }
                 }
                // System.out.println(" 2 "+tmp);
                 diff.add(tmp1);
               //  tmp.clear();
	      }
              ArrayList<Double> Gini=new ArrayList<Double>();
              /*for(int i=0;i<sub.size();i++)
                System.out.println(sub.get(i)+"  "+diff.get(i));*/
              Gini=GiniForAllPartitions(sub,diff,count,data,total,mp,j);
              double xd=500;
              int index=0;
              for(int i=0;i<Gini.size();i++)
              {
                    if(xd>Gini.get(i))
                    {
                        index=i;
                        xd=Gini.get(i);
                    }
              }
              if(val>xd)
              {
                  left.clear();
                  right.clear();
                  attri="";
                  left=sub.get(index);
                  right=diff.get(index);
                  attri=StoreAtt.get(j);
                  val=xd;
              }
          }
          Tree.attribute=attri;
          Tree.value.add(left);
          Tree.value.add(right);
          Node child1=new Node();
          Node child2=new Node();
          Tree.children.add(child1);
          Tree.children.add(child2);
          System.out.println("["+attri+"]");
          return ;
    }
    ArrayList<Double>  GiniForAllPartitions(ArrayList<ArrayList<String>> sub,ArrayList<ArrayList<String>> diff,
                       ArrayList<Integer> count,ArrayList<ArrayList<String>> data,int total,HashMap<String,Integer> mp,int ind)
    {
             ArrayList<Double> Gini=new ArrayList<Double>();
             double d1=0.0,d2=0.0,d=0.0;
             for(int i=0;i<sub.size();i++)
             {
                    d1=(double)count.get(i)/(double)data.size();
                    d2=(double)(total-count.get(i))/(double)data.size();
                    d1=d1*GiniOfPartition(sub.get(i),data,ind,count.get(i));
                    d2=d2*GiniOfPartition(diff.get(i),data,ind,total-count.get(i));
                    d=d1+d2;
                    Gini.add(d);
             }
         return Gini;
    }
    double GiniOfPartition(ArrayList<String> sub,ArrayList<ArrayList<String>> data,int ind,int count)
    {
           double res=0.0;
           HashMap<String,Integer> cnt=new HashMap<String,Integer>();
           HashMap<String,Integer> stsub=new HashMap<String,Integer>();
           for(int i=0;i<sub.size();i++)
           {
                stsub.put(sub.get(i),1);
           }
           for(String st:lables.keySet())
           {
               cnt.put(st,0);
           }
           for(int i=0;i<data.size();i++)
           {
                if(stsub.get(data.get(i).get(ind))!=null)
                {
                    cnt.put(data.get(i).get(cntv),cnt.get(data.get(i).get(cntv))+1);
                }
           }
           res=GiniIndex(cnt,(double)count);   
        return res;   
    }
    double GiniIndex(HashMap<String,Integer> hs,double total)
    {
          // System.out.println("count = "+total);
           double res=0.0,val;
           for(String st : hs.keySet())
           {
               //System.out.println(st+" "+hs.get(st));
               val=hs.get(st);
               val=(val/total);
               val=val*val;
               res+=val;
           }
           res=1-res;
         return res;  
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
        new DecisionTreeCART().run();
    }
}

