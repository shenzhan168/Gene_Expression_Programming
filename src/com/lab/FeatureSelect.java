package com.lab;

import java.util.LinkedList;
import java.util.List;

import com.gep.*;

/**
 * 特征选择
 * @author shenzhan
 *
 */
public class FeatureSelect {

	/**
	 * @param args
	 */
	//超级集合
	List<Individual> listIndivSet=new LinkedList<Individual>();
	FeatStru[] FeatureSta=null;
	GEPRun GepRun;
	
	//超级集合
	public void GetSuperSet(){
		int i,j,k;
		for(i=0;i<10;++i){
		    GepRun=new GEPRun();
			GepRun.RunGep();
			Population Pop=GepRun.GepPro.Pop;
			for(j=0;j<Pop.Size;++j){
				listIndivSet.add(Pop.Get(j));
			}
		}
	}
	
	public void GetOrder(){
		this.FeatureSta=new FeatStru[GepRun.GepPro.FeatureNum];
		for(int i=0;i<GepRun.GepPro.FeatureNum;++i){
			FeatureSta[i]=new FeatStru();
			FeatureSta[i].nNO=i;
		}
		
		Expression Exp=new Expression();
		FunctionSet Fun=new FunctionSet();
		
		for(int i=0;i<listIndivSet.size();++i){
			 Individual Indiv=listIndivSet.get(i);
			 int nLen=Exp.GetIndivValidLen(Indiv);
			 for(int j=0;j<nLen;++j){
				 String str=Indiv.Get(j);
				 int n=Fun.GetParamCount(str);
				 if(0==n){
					  int index=Integer.parseInt(str);
					  FeatureSta[index].nCount++;
				 }
			 }
			 
		}
		
		int i,j = 0,k;
		FeatStru Max;
		for(i=0;i<FeatureSta.length-1;++i){
			k=i;
			Max=FeatureSta[i];
			for(j=i+1;j<FeatureSta.length;++j){
				if(FeatureSta[j].nCount>Max.nCount){
					k=j;
					Max=FeatureSta[j];
				}
			}
			if(i!=k){
				FeatureSta[k]=FeatureSta[i];
				FeatureSta[i]=Max;
			}
		}
		
		
		for(i=0;i<FeatureSta.length;++i){
			String str=String.format("%3d ", FeatureSta[i].nNO);//,FeatureSta[i].nCount);
			System.out.print(str);
		}
		
	}
	
	public static void main(String[] args) {
		FeatureSelect fs=new FeatureSelect();
		fs.GetSuperSet();
		fs.GetOrder();

	}

}

class FeatStru{
	int nNO;
	int nCount;
}
