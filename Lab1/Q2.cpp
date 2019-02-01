#include <bits/stdc++.h>

using namespace std;

int m, n;
vector<vector<int> > data_set;
vector<vector<int> > new_data;
int supp[100000];
int flag[100000];
int newsupp[100000];
map<long long int, int> buckhash;

int val1[10000000], val2[10000000];
long long int hash1(int i, int j)
{
    int mod = 1000009;
    val1[(i + j*1000)%mod] = i;
    val2[(i + j*1000)%mod] = j;
    return (i + j*1000)%mod;
}
void apriori()
{
    if(n == 0)
    return;
    for(int i = 0; i < data_set.size(); i++)
    {
    
        for(int j = 0; j < data_set[i].size(); j++)
        {
            for(int k = j + 1; k < data_set[i].size(); k++)
            {
                buckhash[hash1(data_set[i][j], data_set[i][k])]++;
            }
        }
    }
    //cout << buckhash.size() << endl;
    for(map<long long int, int>::iterator it = buckhash.begin(); it != buckhash.end(); it++)
    {
        if((it)->second >= 0.98*n)
        cout << val1[(it->first)] << " " << val2[(it->first)] << endl;
    }
}
map<int, int> mp;

void input()
{
    freopen("chess.dat", "r", stdin);
    string str;
    int t = 0;
    int j = 0;
    while(getline(cin, str))
    {
        vector<int> temp;
        data_set.push_back(temp);
        for(int i = 0; i < str.size(); i++)
        {
            if(str[i] == ' ')
            {
                data_set[j].push_back(t);
                mp[t]++;
                t = 0;
                continue;
            }
            t = t*10;
            t += (str[i] - '0');
        }
        if(str[str.size() - 1] != ' ')
        {
            data_set[j].push_back(t);
            mp[t]++;
            t = 0;
        }
        j++;

    }
    n = j;
}
int main()
{
    input();
    int ind = 1;
    apriori();

    return 0;
}
