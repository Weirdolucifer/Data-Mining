import math; 
import sys;
from pprint import pprint
from random import shuffle, uniform;


def ReadData(fileName):

    f = open(fileName,'r');
    lines = f.read().splitlines();
    f.close();

    items = [];

    for i in range(1,len(lines)):
        line = lines[i].split(',');
        itemFeatures = [];

        for j in range(len(line)-1):
            v = float(line[j]); 
            itemFeatures.append(v); 
    
        items.append(itemFeatures);

    shuffle(items);

    return items;



def FindColMinMax(items):
    n = len(items[0]);
    minima = [sys.maxsize for i in range(n)];
    maxima = [-sys.maxsize -1 for i in range(n)];
    
    for item in items:
        for f in range(len(item)):
            if(item[f] < minima[f]):
                minima[f] = item[f];
            
            if(item[f] > maxima[f]):
                maxima[f] = item[f];

    return minima,maxima;

def EuclideanDistance(x,y):
    S = 0; 
    for i in range(len(x)):
        S += math.pow(x[i]-y[i],2);

    return math.sqrt(S); 

def InitializeMeans(items,k,cMin,cMax):

    
    f = len(items[0]); 
    means = [[0 for i in range(f)] for j in range(k)];
    
    for mean in means:
        for i in range(len(mean)):

            mean[i] = uniform(cMin[i]+1,cMax[i]-1);

    return means;

def UpdateMean(n,mean,item):
    for i in range(len(mean)):
        m = mean[i];
        m = (m*(n-1)+item[i])/float(n);
        mean[i] = round(m,3);
    
    return mean;

def FindClusters(means,items):
    clusters = [[] for i in range(len(means))]; 
    
    for item in items:
    
        index = Classify(means,item);


        clusters[index].append(item);

    return clusters;



def Classify(means,item):

    
    minimum = sys.maxsize;
    index = -1;

    for i in range(len(means)):
    
        dis = EuclideanDistance(item,means[i]);

        if(dis < minimum):
            minimum = dis;
            index = i;
    
    return index;

def CalculateMeans(k,items,maxIterations=100000):
  
    cMin, cMax = FindColMinMax(items);
    

    means = InitializeMeans(items,k,cMin,cMax);
    

    clusterSizes = [0 for i in range(len(means))];


    belongsTo = [0 for i in range(len(items))];


    for e in range(maxIterations):

        noChange = True;
        for i in range(len(items)):
            item = items[i];

        
            index = Classify(means,item);

            clusterSizes[index] += 1;
            means[index] = UpdateMean(clusterSizes[index],means[index],item);


            if(index != belongsTo[i]):
                noChange = False;

            belongsTo[i] = index;


        if(noChange):
            break;

    return means;



def main():
    items = ReadData('Absenteeism_at_work.csv');
    
    k = 5;

    means = CalculateMeans(k,items);
    clusters = FindClusters(means,items);
    print('***********MEANS*************')


    for i in means:
    	print(i)

    print('\n\n***********CLUSTERS*************\n\n')
  

    for i in clusters :
    	for j in i :
    		print(j)
    	print('\n')



if __name__ == "__main__":
    main();

