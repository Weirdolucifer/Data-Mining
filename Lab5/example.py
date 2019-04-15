from k_medoids import KMedoids
import numpy as np
import matplotlib.pyplot as plt
import math; #For pow and sqrt
import sys;
from random import shuffle, uniform;

def ReadData(fileName):
    #Read the file, splitting by lines
    f = open(fileName,'r');
    lines = f.read().splitlines();
    f.close();

    items = [];

    for i in range(1,len(lines)):
        line = lines[i].split(',');
        itemFeatures = [];

        for j in range(len(line)-1):
            v = float(line[j]); #Convert feature value to float
            itemFeatures.append(v); #Add feature value to dict
    
        items.append(itemFeatures);

    shuffle(items);

    return items;

def example_distance_func(data1, data2):
    '''example distance function'''
    return np.sqrt(np.sum((data1 - data2)**2))

if __name__ == '__main__':

    items = ReadData('Absenteeism_at_work.csv')
    X = np.asarray(items, dtype=None, order=None)
    #X = np.random.normal(0,3,(500,2))
    model = KMedoids(n_clusters=5, dist_func=example_distance_func)
    model.fit(X, plotit=True, verbose=True)
    plt.show()




