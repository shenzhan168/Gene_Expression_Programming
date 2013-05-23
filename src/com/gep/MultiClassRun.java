package com.gep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MultiClassRun {

	public GepProcess GepPro;
	public static int nCheck;
	
	public Individual[] BestIndivs;
	
	public int[] nAttri=null;

	/**
	 * 读取数据
	 * @param sPath
	 * @return
	 * @throws IOException
	 */
	public double[][] ReadData(String sPath)  {

		double[][] digital = new double[1000][1000];

		File file = new File(sPath);
		if (!file.exists() || file.isDirectory())
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp = null;
		String[] tempArray = null;
		int i = 0;
		try {
			temp = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (temp != null) {

			tempArray = temp.split(",");
			
			GepPro.FeatureNum=tempArray.length-1;  //==========================================================
			
			for (int j = 0; j < tempArray.length; j++) {
				digital[i][j] = Double.parseDouble(tempArray[j]);

			}
			try {
				temp = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}

		int nRow = i;
		int nCol = tempArray.length;
		
		
		

		double[][] Res = new double[nRow][nCol];
		for (i = 0; i < nRow; ++i) {
			for (int j = 0; j < nCol; ++j) {
				Res[i][j] = digital[i][j];
			}
		}
		return Res;

	}

	/**
	 * 用部分特征进行数据测试 部分特征由nAttri[] 给出
	 * 
	 * @param nAttri
	 */
	public double[][] SelectAttri(int[] nAttri, double[][] Data) {
		int nRow = Data.length;
		int nCol = Data[0].length;
		double[][] dRes = new double[nRow][nAttri.length + 1]; // 输出的数组

		int i, j, k;
		int nIndex;
		for (i = 0; i < nAttri.length; ++i) {
			nIndex = nAttri[i];
			for (j = 0; j < nRow; ++j) {
				dRes[j][i] = Data[j][nIndex];
			}
		}

		for (j = 0; j < nRow; ++j) {
			dRes[j][nAttri.length] = Data[j][nCol - 1];
		}

		return dRes;

	}

	/**
	 * 设置参数+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public void SetValue() {

		GepPro.FitnessFunType = "SampleClassify"; // 设置适应值函数 可选
													// SampleClassify:简单分类函数 ,
													// SenSepClassify:适应度*敏感度
													// ConciseClassify 简洁模型
		GepPro.MaxGeneration = 100;
		GepPro.HeadLength = 8;
		GepPro.GeneCount = 3;
		GepPro.PopulationSize = 50;
		GepPro.FeatureNum = 35; // 特征个数------------------------------------------------
		GepPro.nClassCount=3;   //分类器的 数目
		
		
		
		GepPro.MutationRate = 0.0051;
		GepPro.ISRate = 0.1;
		GepPro.RISRate = 0.1;
		int[] nLen = { 1, 2, 3 };
		GepPro.ISElemLength = nLen;
		GepPro.RISRate = 0.1;
		GepPro.RISElemLength = nLen;
		GepPro.GeneTransRate = 0.1;

		GepPro.OnePRecomRate = 0.3;
		GepPro.TwoPRecomRate = 0.31;
		GepPro.GeneRecomRate = 0.1;
		

		GepPro.TrainData = ReadData("data/WineTrain.txt");// ----设置训练集数据的路径----------------------------------

		GepPro.TestData = ReadData("data/WineTest.txt");// ----设置测试集数据的路径---------------------------------------------

		
		

	}

	
	/**
	 * GEP 运行的过程
	 * @param GepPro
	 */
	public void GEPrunning(GepProcess GepPro){
		
		int nGeneration = 0;
		
		GepPro.InitialPopulation();

		do {

			GepPro.EvalutePopulaton();
			Print();

			GepPro.Select();

			GepPro.Mutation();

			GepPro.TransPosIS();

			GepPro.TransPosRIS();

			GepPro.TransPosGene();

			GepPro.RecomOnePoint();

			GepPro.RecomTwoPoint();

			GepPro.ReComGene();

			++nGeneration;
		

		} while (((1 - GepPro.BestIndividual.Fitness) > 0.03)
				&& nGeneration < GepPro.MaxGeneration);
	}
	
	
	public void RunGep() {

		GepPro = new GepProcess();
		SetValue();
		BestIndivs=new Individual[GepPro.nClassCount];
		int i;
		//训练多个分类器
		for(i=1;i<=GepPro.nClassCount;++i){
			GepPro.nCurrentClass=i;
			GEPrunning(GepPro);
			BestIndivs[i-1]=GepPro.BestIndividual;
		}
		
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static void main(String[] args) {
		

		   MultiClassRun gep = new MultiClassRun();
		    gep.RunGep();
		    
		    for(int i=0;i<gep.BestIndivs.length;++i){
		    	System.out.println(gep.BestIndivs[i].Fitness);
		    }
		    
		    System.out.println("分类测试准确度  ："+gep.Test());

	}

	public double Test(){
		
		int nRow = GepPro.TestData.length;
		int nCol =  GepPro.TestData[0].length;
		double[] dValue=new double[GepPro.nClassCount];
		int i, j,k;
		Expression Exp = new Expression();
		int tp = 0, fp = 0;
		for (j = 0; j < nRow; ++j) {
			
			for(i=0;i<GepPro.nClassCount;++i){
				 dValue[i] = Exp.GetValue(BestIndivs[i], GepPro.TestData[j]);  //把数据放到每一个分类器中 计算出结果
			}
			
			int DataClass=(int) GepPro.TestData[j][nCol-1];   //数据所属的分类
			
		   
			if(DataClass==2){
			
			if(dValue[DataClass-1]>0){
					++tp;
				}
				else{
					++fp;
				}
			}	
	
		}
		GepPro.TestAccuracy = (tp ) / (double) (tp+fp);
		return GepPro.TestAccuracy;
	}
	
	
	public void Print() {
		for (int i = 0; i < GepPro.PopulationSize; ++i) {

			for (int j = 0; j < GepPro.GeneCount; ++j) {

				for (int k = 0; k < GepPro.GeneLength; ++k) {
					System.out.print(GepPro.Pop.Get(i).Get(
							j * GepPro.GeneLength + k)
							+ " ");
				}
				System.out.print("        ");
			}
			System.out.print("  :  " + GepPro.Fitness[i] + " value"
					+ GepPro.Pop.Get(i).Value);
			System.out.println();
		}
		System.out.println();

	}

}
