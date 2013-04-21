package com.gep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GEPRun {
	
     public GepProcess GepPro;
     public static int nCheck;
     
     private double[][] ReadData(String sPath) throws IOException{
    	 
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
      * 设置参数
      */
     private void SetValue(){
    	 
    	 GepPro.FitnessFunType="ConciseClassify";  //设置适应值函数  可选 SampleClassify:简单分类函数 ,  SenSepClassify:适应度*敏感度   ConciseClassify 简洁模型
    	  GepPro.MaxGeneration=200;
    	  GepPro.HeadLength=10;
    	  GepPro.GeneCount=4;
    	  GepPro.PopulationSize=100;
    	  GepPro.FeatureNum=4;    //特征个数------------------------------------------------
    	  
   //--------------------------------------------------------- 	  
    	  try {
			GepPro.TrainData=ReadData("data/sample.txt");//--------------------------------------
	      
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	  
    	  try {
			GepPro.TestData=ReadData("data/testSample.txt");//-------------------------------------------------
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
     
     private void RunGep(){
    	 
    	 
    	 GepPro=new GepProcess();
    	 SetValue();
    	 int nGeneration=0;
    	 GepPro.InitialPopulation();
    	  
    	 do{
    		
    		 
			GepPro.EvalutePopulaton();
			
			Print();
			System.out.println("before average Fitness "+GepPro.AverageFitness());
			
			GepPro.Select();
			
			 
			GepPro.Mutation();
		
			
			GepPro.TransPosIS();
			
					
			GepPro.TransPosRIS();

			
			GepPro.TransPosGene();

			
			GepPro.RecomOnePoint();

			
			GepPro.RecomTwoPoint();
	
			
			GepPro.ReComGene();
	
			
			++nGeneration;
			System.out.println(nGeneration +":  "+GepPro.BestIndividual.Fitness +"\n");
			
			
    	 }while( ((1000-GepPro.BestIndividual.Fitness)>0.03)  && nGeneration<GepPro.MaxGeneration ) ;
   
    	 System.out.println(GepPro.BestIndividual.Fitness);
        
    	  System.out.println("测试  "+ GepPro.Test());
    	 
     }
     
     
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     public static void main(String[] args) {
    	     GEPRun gep=new GEPRun();
    	     gep.RunGep();
     }
     
     
     private void Print(){
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
