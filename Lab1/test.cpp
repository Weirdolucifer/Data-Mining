#include <bits/stdc++.h>
using namespace std;

int t;
vector<int>v[100005];
map<vector<int>,int>m;
map<vector<int>,int>tmp1;
map<vector<int>,int>prev_set;
vector<vector<int> >closed;
set<vector<int>>ss;
double support = 98.0; 
double confidence = 50.0; 
int count_vector(vector<int>a){
	int cnt = 0;
	int i;
	vector<int>h(100000,0);
	for(auto k: a) h[k]++;
	for(i=0;i<t;i++){
		int sze = 0;
		for(auto k: v[i]){
			if(h[k]!=0) sze++;
		}
		if(sze==(int)a.size()) cnt++;
	}
	return cnt;
}
vector<int> merge_vector(vector<int>a, vector<int>b){
	int i;
	int n = (int)a.size();
	vector<int>ans;
	set<int>s;
	for(i=0;i<n;i++){
		s.insert(a[i]);
		s.insert(b[i]);
	}
	if((int)s.size()==n+1){
		for(auto it=s.begin();it!=s.end();it++){
			ans.push_back(*it);
		}
	}
	return ans;
}
int main(){
	freopen("chess.dat","r",stdin);
	string line;
	int i,j;
	while(getline(cin,line)){
		
		int n=0;
		string tmp(5000,'\0');
		j=0;
		for(i=0;i<(int)line.size();i++){
			if(line[i]==' '){
				for(int k=0;k<j;k++){
					n=(tmp[k]-48)+(n*10);
				}
				j=0;
				v[t].push_back(n);
				n=0;
			}
			else
			{
				tmp[j] = (line[i]);
				j++;
			}
		}
		t++;
	}
	int maxcnt = (support*t)/100.0;

	for(i=0;i<t;i++){
		for(auto k:v[i]){
			vector<int>temp;
			temp.push_back(k);
			m[temp]++;
			temp.clear();
		}
	}

	int cnt = 0;
	while(1){
		cnt++;
		cout<<"Itemset"; cout<<cnt<<endl;
		for(auto it = m.begin();it!=m.end();it++){
			for(i=0;i<(int)(it->first).size();i++){
				cout<<(it->first[i]);
			}
			cout<<"-> ";
			cout<<it->second<<endl;
		}
		cout<<"\n";
		cout<<"frequent";cout<<cnt<<endl;
		int fl = 0;
		for(auto it = m.begin();it!=m.end();it++){
			if(it->second >= maxcnt){
				tmp1.insert({it->first,it->second});
				for(i=0;i<(int)(it->first).size();i++){
					cout<<(it->first[i]);
				}
				cout<<"-> ";
				cout<<it->second<<endl;
				fl = 1;
			}
		}
		cout<<"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"<<endl;
		if(fl==1){
			m.clear();
	
			ss.clear();
			prev_set.clear();
			for(auto it = tmp1.begin();it!=tmp1.end();it++){
				auto it1 = it;
				it1++;
				for(;it1!=tmp1.end();it1++){
					vector<int> ans = merge_vector(it->first,it1->first);
					if((int)ans.size()>0){
				
						ss.insert(ans);
					}
		
				}
				prev_set.insert({it->first,it->second});
			}
			
			for(auto it=ss.begin();it!=ss.end();it++){
				int cnt1 = count_vector(*it);
				m.insert({*it,cnt1});
		
			}
		
			for(auto it = tmp1.begin();it!=tmp1.end();it++){
				auto it1 = it;
				it1++;
				int fl2 = 0;
				for(;it1!=tmp1.end();it1++){
					vector<int> ans = merge_vector(it->first,it1->first);
					if((int)ans.size()>0){
				
						auto it_child = m.find(ans);
						if(it_child->second == it->second){
							fl2 = 1;
							break;
						}
					}
		
				}
				if(fl2==0){
					closed.push_back(it->first);
				}
			}
			cout<<"Closed Set:"<<endl;
			for(i=0;i<(int)closed.size();i++){
				for(auto k1: closed[i]){
					cout<<(k1);
				}
				cout<<endl;
			}
			closed.clear();
		}
		else {
			break ;
		}
		tmp1.clear();
	}
	cout<<"Final association:"<<endl;
	for(auto it=prev_set.begin();it!=prev_set.end();it++){
		for(auto k1: it->first){
			cout<<(k1);
		}
		cout<<": ";
		cout<<it->second<<endl;
	}
	for(auto it=prev_set.begin();it!=prev_set.end();it++){
		vector<int>a;
		int n = (int)(it->first).size();
		for(i=1;i<(1<<n)-1;i++){
			a.clear();
			for(j=0;j<n;j++){
				if((i&(1<<j))>0){
					a.push_back(it->first[j]);
				}
			}
			double k = (double)count_vector(it->first)/(double)count_vector(a);
	
			vector<int>h(100000,0);
			if(k>=confidence/100.0){
				for(auto k1: a){
					h[k1] = 1;
					cout<<(k1);
				}
				cout<<" =>\t";
				for(auto k1: it->first){
					if(h[k1]==0){
						cout<<(k1);
					}
				}
				cout<<" -> ";
				printf("%lf\n",k*100);
				fill(h.begin(),h.end(),0);
			}
		}
		cout<<endl;
		cout<<endl;
		cout<<endl;
		cout<<endl;
	}
}
