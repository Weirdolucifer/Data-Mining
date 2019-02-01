#include <bits/stdc++.h>
using namespace std;

int m, n;
vector<vector<int> > data_set;
vector<vector<int> > new_data;
int supp[100000];
int flag[100000];
int newsupp[100000];
void apriori(vector<vector<int> > data)
{
    if(n == 0)
    return;
    for(int i = 0; i < data.size(); i++)
    {
        for(int j = i + 1; j < data.size(); j++)
        {
            int f = 0;
            for(int l = 0; l < data[i].size() - 1; l++)
            {
                if(data[i][l] != data[j][l])
                {
                    f = 1;
                    break;
                }
            }
            if(f == 1)
            break;
            vector<int> temp;
            if(data[i][data[i].size() - 1] < data[j][data[i].size() - 1])
            {
                temp = data[i];
                temp.push_back(data[j][data[i].size() - 1]);
            }
            else if(data[i][data[i].size() - 1] > data[j][data[i].size() - 1])
            {
                temp = data[j];
                temp.push_back(data[i][data[i].size() - 1]);
            }
            f = 0;
            for(int l = 0; l < temp.size(); l++)
            {
                vector<int> new_data = temp;
                new_data.erase(new_data.begin() + l);
                if(find(data.begin(), data.end(), new_data) == data.end())
                {
                    f = 1;
                    break;
                }
            }
            int c = 0;
            for(int l = 0; l < data_set.size(); l++)
            {
                if(includes(data_set[l].begin(), data_set[l].end(), temp.begin(), temp.end()))
                c++;
            }

  
            if(f == 0 && c >= 0.5*n)
            {
                new_data.push_back(temp);
                newsupp[new_data.size() - 1] = c;
            }
            if(c >= supp[i])flag[i] = 1;
            if(c >= supp[j])flag[j] = 1;
        }
    }
}
map<int, int> mp;

void input_chess()
{
    freopen("mushroom.dat", "r", stdin);
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
    input_chess();
    vector<vector<int> > data;
    for(map<int, int>::iterator i = mp.begin(); i != mp.end(); i++)
    {
        vector<int> temp;
        temp.push_back(i->first);
        int c = 0;
        for(int l = 0; l < data_set.size(); l++)
        {
            if(includes(data_set[l].begin(), data_set[l].end(), temp.begin(), temp.end()))
            c++;
        }
        if(c >= 0.6*n)
        data.push_back(temp);
    }
    
    int ind = 1;
    while(1)
    {
        
        apriori(data);
        cout << "\nClosed frequent sets\n";
        for(int l = 0; l < data.size(); l++)
        if(flag[l] == 0)
        {
            for(int j = 0; j < data[l].size(); j++)
            {
                cout << data[l][j] << " ";
            }
            cout << endl;
        }
        
        if(new_data.size() == 0)
        break;
        cout << "\nPass: " << ind << "\n";
        cout << "Final frequent set : \n";
        cout << new_data.size() << "\n";
        memset(flag, 0, sizeof flag);
        for(int i = 0; i < data.size(); i++)
        supp[i] = newsupp[i];
        for(int i = 0; i < new_data.size(); i++)
        {
            for(int j = 0; j < new_data[i].size(); j++)
            {
                cout << new_data[i][j] << " ";
            }
            cout << endl;
        }
        data = new_data;
        new_data.clear();
        ind++;
    }
    
    return 0;
}
