package com.gep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GEPRun {
	
     public GepProcess GepPro;
     public static int nCheck;
     
     public double[][] ReadData(String sPath) throws IOException{
    	 
    	 double[][] digital=new double[1000][1000];
    	 
    	 File file=new File(sPath);
         if(!file.exists()||file.isDirectory())
             throw new FileNotFoundException();
         BufferedReader br=new BufferedReader(new FileReader(file));
         String temp=null;
         String []tempArray = null;
         int i=0;
         temp=br.readLine();
         while(temp!=null){
           
         	tempArray = temp.split(",");
         	for (int j = 0; j < tempArray.length; j++) {	
 				digital[i][j] = Double.parseDouble(tempArray[j]);
 				
 			}
             temp=br.readLine();
             i++;
         }
         
         int nRow=i;
         int nCol=tempArray.length;
         
         double[][] Res=new double[nRow][nCol];
         for(i=0;i<nRow;++i){
        	  for(int j=0;j<nCol;++j){
        		  Res[i][j]=digital[i][j];
        	  }
         }
      return Res;
       
     }  
     
     /**
      * 用部分特征进行数据测试  部分特征由nAttri[] 给出
      * @param nAttri
      */
     public double[][] SelectAttri(int[] nAttri,double[][] Data){
           int nRow=Data.length;
           int nCol=Data[0].length;
           double[][] dRes=new double[nRow][nAttri.length+1]; //输出的数组
    	   
           int i,j,k;
           int nIndex;
           for(i=0;i<nAttri.length;++i){
        	   nIndex=nAttri[i];
        	   for(j=0;j<nRow;++j){
        		   dRes[j][i]=Data[j][nIndex];
        	   }
           }
           
           for(j=0;j<nRow;++j){
        	   dRes[j][nAttri.length]=Data[j][nCol-1];
           }
           
           
           return dRes;
    	 
     }
     
     
     /**
      * 设置参数
      */
     public void SetValue(){
    	 
    	 GepPro.FitnessFunType="SenSepClassify";  //设置适应值函数  可选 SampleClassify:简单分类函数 ,  SenSepClassify:适应度*敏感度   ConciseClassify 简洁模型
    	  GepPro.MaxGeneration=200;
    	  GepPro.HeadLength=10;
    	  GepPro.GeneCount=4;
    	  GepPro.PopulationSize=100;
    	  GepPro.FeatureNum=34;    //特征个数------------------------------------------------
    	  
   //--------------------------------------------------------- 	  
    	  try {
			GepPro.TrainData=ReadData("data/train.txt");//--------------------------------------
	      
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	  
    	  try {
			GepPro.TestData=ReadData("data/test.txt");//-------------------------------------------------
		} catch (IOException e) {
			e.printStackTrace();
		}
    	  
    	  GepPro.MutationRate=0.001;
    	  GepPro.ISRate=0.1;
    	  GepPro.RISRate=0.1;
    	  int[] nLen={1,2,3};
    	  GepPro.ISElemLength=nLen;
    	  GepPro.RISRate=0.1;
    	  GepPro.RISElemLength=nLen;
    	  GepPro.GeneTransRate=0.1;
    	  
    	  GepPro.OnePRecomRate=0.01;
    	  GepPro.TwoPRecomRate=0.01;
    	  GepPro.GeneRecomRate=0.01;
    	  
    	 
     }
     
     public void RunGep(){
    	 
    	 
    	 GepPro=new GepProcess();
    	 SetValue();
    	 int nGeneration=0;
    	 GepPro.InitialPopulation();
    	  
    	 do{
    		GepPro.Statictis();
    		 
			GepPro.EvalutePopulaton();
			
			//Print();
			//System.out.println("before average Fitness "+GepPro.AverageFitness());
			
			GepPro.Select();
			
			 
			GepPro.Mutation();
		
			
			GepPro.TransPosIS();
			
					
			GepPro.TransPosRIS();

			
			GepPro.TransPosGene();

			
			GepPro.RecomOnePoint();

			
			GepPro.RecomTwoPoint();
	
			
			GepPro.ReComGene();
	
			
			++nGeneration;
			//System.out.println(nGeneration +":  "+GepPro.BestIndividual.Fitness +"\n");
			
			
    	 }while( ((1000-GepPro.BestIndividual.Fitness)>0.03)  && nGeneration<GepPro.MaxGeneration ) ;
   
    	 System.out.println(GepPro.BestIndividual.Fitness);
        
    	  System.out.println("测试  "+ GepPro.Test());
    	  GepPro.Statictis();
    	 
     }
     
     
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     public static void main(String[] args) {
    	     GEPRun gep=new GEPRun();
    	     gep.RunGep();
     }
     
     
     public void Print(){
    	 for(int i=0;i<GepPro.PopulationSize;++i){
    	  		 
    		 for(int j=0;j<GepPro.GeneCount;++j){
    	
    			 for(int k=0;k<GepPro.GeneLength;++k){
    			       System.out.print(GepPro.Pop.Get(i).Get(j*GepPro.GeneLength+k) +" ");
    			 }
    			 System.out.print("        ");
    		 }
    		 System.out.print("  :  "+GepPro.Fitness[i] +" value"+GepPro.Pop.Get(i).Value);
    		 System.out.println();
    	 }
     }

}
