package com.gep;

import java.util.*;
//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : @gep
//  @ File Name : GepProcess.java
//  @ Date : 2013/4/5
//  @ Author : @shenzhan
//
//

/**
 * gep����Ҫ����
 **/
public class GepProcess {
	/**
	 * 
	 */

	public Population Pop; // ��Ⱥ
	public FunctionSet Fun; // ������
	public List<String> Feature;

	public int MaxGeneration; //
	public int PopulationSize;
	public int HeadLength;
	public static int GeneCount;
	public static int GeneLength;
	public int ChromosomeLength;
	public double MutationRate;
	public double OnePRecomRate;
	public double TwoPRecomRate;
	public double GeneRecomRate;
	public double ISRate;
	public int[] ISElemLength;
	public double RISRate;
	public int[] RISElemLength;
	public double GeneTransRate;
	public double SelectionRange;
	public double Error;
	public double SuccessRate;

	public int FeatureNum; // ��������
	public String[] sFullSet; // ��������+��������
	public String[] sFeatureSet; // ��������

	public double[][] TrainData; // ѵ������
	public double[][] TestData; // ��������

	public double[] Fitness;

	public Individual BestIndividual;
	public int BestIndivNum;

	/**
	 * Ⱥ���ʼ��
	 **/
	public void InitialPopulation() {
		// ��ʼ��//===========================================================================
		Fitness = new double[this.PopulationSize];
		Fun=new FunctionSet();
		Pop=new Population();
        //================================================================================
		// ������Ҫ���ַ�����

		// ����
		sFeatureSet = new String[this.FeatureNum];
		for (int i = 0; i < this.FeatureNum; ++i) {
			sFeatureSet[i] = String.format("%d", i);
		}

		int nTail = this.HeadLength * (Fun.MaxParamCount - 1) + 1; // β������
		this.GeneLength = this.HeadLength + nTail; // ������򳤶�
		this.ChromosomeLength = GepProcess.GeneCount * GepProcess.GeneLength; // Ⱦɫ�峤��

		String[] sFunSet =new String[Fun.sFunction.size()]; //// ��������
		for(int i=0;i<Fun.sFunction.size();++i){
			sFunSet[i]=Fun.sFunction.get(i);
		}
		
		sFullSet = new String[sFunSet.length + sFeatureSet.length]; // ��������  +��������
																	
		int nSetLen = sFunSet.length + sFeatureSet.length;
		for (int i = 0; i < sFunSet.length; ++i) {
			sFullSet[i] = sFunSet[i];
		}
		int m=0;
		for (int i = sFunSet.length; i < nSetLen; ++i) {
			sFullSet[i] = sFeatureSet[m++];
		}

		Random random = new Random();
		String[] sGene = new String[this.GeneLength];
		for (int i = 0; i < this.PopulationSize; ++i) {

			Individual Indiv = new Individual();// �¸���
			for (int k = 0; k < this.GeneCount; ++k) {

				// ����ͷ��
				int nIndex;
				int j;
				for (j = 0; j < this.HeadLength; ++j) {
					nIndex = random.nextInt(nSetLen);
					sGene[j] = sFullSet[nIndex];
				}

				// ����β��
				for (; j < this.GeneLength; ++j) {
					nIndex = random.nextInt(sFeatureSet.length);
					sGene[j] = sFeatureSet[j];
				}

				Indiv.AddGene(sGene); // ���ӻ���
			}
			this.Pop.AddIndivdual(Indiv); // ���Ӹ���
		}

	}

	/**
	 * ÿһ������������
	 **/
	public void EvalutePopulaton() {

		int nRow = this.TrainData.length;
		int nCol = this.TrainData[0].length;
		Expression Exp = new Expression();
		int i, j, k;

		for (i = 0; i < this.PopulationSize; ++i) {
			int tp = 0, fp = 0, tn = 0, fn = 0;
			for (j = 0; j < nRow; ++j) {
				double dValue = Exp.GetValue(this.Pop.Get(i), this.TrainData[j]);
				Pop.Get(i).Value=dValue;
				// ������ 0 ��
				if (TrainData[j][nCol-1] == 1) {
					if (dValue < 0) {
						tp++;
					} else {
						fp++;
					}
				} else if (TrainData[j][nCol-1] == 2) {
					if (dValue >= 0) {
						tn++;
					} else {
						fn++;
					}
				}

			}
			Pop.Get(i).Fitness = (double)(tp + tn) / (double)nRow;
			this.Fitness[i] = (double)(tp + tn) / (double)nRow;
		}

	}

	/**
	 * �ж�gep�Ƿ�Ҫ����
	 **/
	public void IsTerminate() {

	}

	/**
	 * ѡ����Ⱥ���н���ѡ��
	 **/
	public void Select() {
		Population NewPop = new Population();
		FindBestIndividual();
		NewPop.AddIndivdual(this.BestIndividual); // ��Ӣ����

		double dTotal = 0;
		for (int i = 0; i < this.PopulationSize; ++i) {
			dTotal += this.Fitness[i];
		}

		double[] dRate = new double[this.PopulationSize];

		if (dTotal == 0) {
			for (int i = 0; i < this.PopulationSize; ++i) {
				dRate[i] = 1 / this.PopulationSize;
			}
		} else {
			for (int i = 0; i < this.PopulationSize; ++i) {
				dRate[i] = this.Fitness[i] / dTotal;
			}
		}

		// �ֶ���
		double[] dWheel = new double[this.PopulationSize];
		for (int i = 0; i < this.PopulationSize; ++i) {
			if (0 == i) {
				dWheel[i] = dRate[i];
			} else {
				dWheel[i] = dWheel[i - 1] + dRate[i];
			}
		}

		// ѡ��
		Random random = new Random();
		for (int i = 1; i < this.PopulationSize; ++i) {
			double d = random.nextDouble();
			int j = 0;
			while (d > dWheel[j]){
				++j;
			}
			NewPop.AddIndivdual(this.Pop.Get(j));
		}

		this.Pop = NewPop;

	}

	/**
	 * �������Ÿ���
	 */
	public void FindBestIndividual() {
		int nMax = 0;
		double dMaxFitness = this.Fitness[0];
		int i;
		for (i = 1; i < this.Fitness.length; ++i) {
			if (this.Fitness[i] > dMaxFitness) {
				dMaxFitness = this.Fitness[i];
				nMax = i;
			}
		}
		this.BestIndivNum = nMax;
		this.BestIndividual = this.Pop.Get(nMax);
	}

	/**
	 * ����
	 */
	public void Mutation() {

		Random random = new Random();
		for (int i = 0; i < this.PopulationSize; ++i) {
			for (int j = 0; j < this.ChromosomeLength; ++j) {

				if (random.nextDouble() < this.MutationRate) {
					int nIndex = j % this.GeneLength;
					int k;
					// ����ͷ��
					if (nIndex < this.HeadLength) {
						k = random.nextInt(this.sFullSet.length);
						Pop.Get(i).Set(j, sFullSet[k]); // ���ñ���Ļ���
					} else { // β��
						k = random.nextInt(this.sFeatureSet.length);
						Pop.Get(i).Set(j, sFeatureSet[k]); // ���ñ���Ļ���
					}
				}
			}
		}
	}

	/**
	 * IS �崮
	 */
	public void TransPosIS() {
		Random random = new Random();
		double dRate;
		for (int i = 0; i < this.PopulationSize; ++i) {
			dRate = random.nextDouble();
			if (dRate < this.ISRate) {
				int nIndivNO = random.nextInt(this.PopulationSize); // �������
				Individual Indiv = this.Pop.Get(nIndivNO);

				int nGeneNO = random.nextInt(this.GeneCount); // �������
				int nStart = nGeneNO * this.GeneLength; // ��ʼλ��

				int nSelLen = this.ISElemLength.length;
				int nLength = this.ISElemLength[random.nextInt(nSelLen)]; // �������

				// ���Դλ��
				int nSouPos = random.nextInt(this.GeneLength);
				if (nSouPos + nLength >= this.GeneLength) {
					nSouPos = this.GeneLength - nLength;
				}
				nSouPos += nStart;

				// ���Ŀ��λ��
				int nTarPos;
				do {
					nTarPos = random.nextInt(this.HeadLength);
				} while (0 != nTarPos);
				nTarPos += nStart;

				// ����IS����
				List<String> listTemp = Indiv.Chrom.subList(nSouPos, nSouPos
						+ nLength);
				Indiv.Chrom.addAll(0, listTemp);
				for (int j = this.HeadLength + nLength - 1; j >= this.HeadLength; --j) {
					Indiv.Chrom.remove(this.HeadLength);
				}

			}
		}

	}

	/**
	 * RIS �崮
	 */
	public void TransPosRIS() {
		Random random = new Random();
		double dRate;
		for (int i = 0; i < this.PopulationSize; ++i) {
			dRate = random.nextDouble();
			if (dRate < this.ISRate) {
				int nIndivNO = random.nextInt(this.PopulationSize); // �������
				Individual Indiv = this.Pop.Get(nIndivNO);

				int nGeneNO = random.nextInt(this.GeneCount); // �������
				int nStart = nGeneNO * this.GeneLength; // ��ʼλ��

				int nSelLen = this.ISElemLength.length;
				int nLength = this.ISElemLength[random.nextInt(nSelLen)]; // �������

				// ���Դλ��
				int nHeadPos;
				do {
					nHeadPos = random.nextInt(this.HeadLength);
				} while (nHeadPos == 0);
				while (nHeadPos < this.GeneLength
						&& !Fun.IsFunction(Indiv.Get(nHeadPos))) {
					++nHeadPos;
				}
				if (nHeadPos == this.GeneLength) { // �Ҳ�����������
					continue;
				}
				// �жϳ���
				if (this.GeneLength - nHeadPos < nLength) {
					nLength = this.GeneLength - nHeadPos;
				}

				// ������
				List<String> listTemp = Indiv.Chrom.subList(nHeadPos, nHeadPos
						+ nLength);
				Indiv.Chrom.addAll(0, listTemp);
				
				for (int j=0;j<nLength;++j) {
					Indiv.Chrom.remove(this.HeadLength);
				}
				
			}
		}

	}

	/**
	 * ���� �崮
	 */
	public void TransPosGene() {
		Random random = new Random();
		double dRate;

		for (int i = 0; i < this.PopulationSize; ++i) {
			dRate = random.nextDouble();
			if (dRate < this.GeneTransRate) {

				int nIndivNO = random.nextInt(this.PopulationSize); // �������
				Individual Indiv = this.Pop.Get(nIndivNO);

				int nGeneNO; // �������
				do {
					nGeneNO = random.nextInt(this.GeneCount);
				} while (nGeneNO != 0);

				// ����崮
				int nStart = nGeneNO * this.GeneLength;
				int nEnd = nStart + this.GeneLength;
				List<String> listTemp = Indiv.Chrom.subList(nStart, nEnd);
				Indiv.Chrom.addAll(0, listTemp); // �ѻ�����뵽��ʼλ��
				// ɾ��ԭλ�õĻ���
				for (int j = nStart; j < nEnd; ++j) {
					Indiv.Chrom.remove(nStart+this.GeneLength);
				}
				

			}
		}

	}

	/**
	 * ��������
	 */
	public void RecomOnePoint() {
		int i = 0;
		int nFather;
		int nMother;
		int nPos;
		Random random = new Random();
		double dRate;
		for (i = 0; i < this.PopulationSize; ++i) {

			dRate = random.nextDouble();

			if (dRate < this.OnePRecomRate) {
				// ���ѡȡ�������� �� �����

				nFather = random.nextInt(this.PopulationSize);// ���ѡȡ�������
				nMother = random.nextInt(this.PopulationSize);
				nPos = random.nextInt(this.ChromosomeLength);

				Individual Father = this.Pop.Get(nFather);
				Individual Mother = this.Pop.Get(nMother);
				String temp;
				// �������彻������
				for (int j = 0; j < nPos; ++j) {
					temp = Father.Get(j);
					Father.Set(j, Mother.Get(j));
					Mother.Set(j, temp);
				}

			}
		}
	}

	/**
	 * ��������
	 */
	public void RecomTwoPoint() {
		int i = 0;
		int nFather;
		int nMother;
		int nPosPre;
		int nPosLast;
		Random random = new Random();
		double dRate;
		for (i = 0; i < this.PopulationSize; ++i) {
			dRate = random.nextDouble();
			if (dRate < this.RISRate) {

				nFather = random.nextInt(this.PopulationSize);// ���ѡȡ�������
				nMother = random.nextInt(this.PopulationSize);
				Individual Father = this.Pop.Get(nFather);
				Individual Mother = this.Pop.Get(nMother);

				nPosPre = random.nextInt(this.ChromosomeLength);
				nPosLast = random.nextInt(this.ChromosomeLength);

				if (nPosPre > nPosLast) {
					int nTemp = nPosLast;
					nPosLast = nPosPre;
					nPosPre = nPosLast;
				}

				// ���򽻻�
				String sTemp;
				for (int j = nPosPre; j < nPosLast; ++j) {
					sTemp = Father.Get(j);
					Father.Set(j, Mother.Get(j));
					Mother.Set(j, sTemp);
				}

			}
		}

	}

	/**
	 * ��������
	 */
	public void ReComGene() {
		int i = 0;
		int nFather;
		int nMother;
		Random random = new Random();
		double dRate;

		for (i = 0; i < this.PopulationSize; ++i) {

			dRate = random.nextDouble();

			if (dRate < this.GeneTransRate) {

				nFather = random.nextInt(this.PopulationSize);// ���ѡȡ�������
				nMother = random.nextInt(this.PopulationSize);
				Individual Father = this.Pop.Get(nFather);
				Individual Mother = this.Pop.Get(nMother);

				int nGeneNo = random.nextInt(this.GeneCount);

				int nStart = nGeneNo * this.GeneLength; // �������Ŀ�ʼ ����λ��
				int nEnd = nStart + this.GeneLength;

				// ���򽻻�
				String sTemp;
				for (int j = nStart; j < nEnd; ++j) {
					sTemp = Father.Get(j);
					Father.Set(j, Mother.Get(j));
					Mother.Set(j, sTemp);
				}
			}
		}

	}

	// +===============================================================================================
	/**
	 * ���ݲ���
	 * 
	 * @return
	 */
	public double Test() {
		int nRow = this.TestData.length;
		int nCol = this.TestData[0].length;
		Expression Exp = new Expression();

		int j;
		int tp = 0, fp = 0, tn = 0, fn = 0;
		for (j = 0; j < nRow; ++j) {
			double dValue = Exp.GetValue(this.BestIndividual, this.TestData[j]);
			// ������ 0 ��
			if (TestData[j][nCol] == 0) {
				if (dValue < 0) {
					tp++;
				} else {
					fp++;
				}
			} else if (TestData[j][nCol] == 1) {
				if (dValue >= 0) {
					tn++;
				} else {
					fn++;
				}
			}
		}
		return (tp+tn)/nRow;

	}

}