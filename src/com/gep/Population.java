package com.gep;

import java.util.LinkedList;
import java.util.List;

//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : @gep
//  @ File Name : Population.java
//  @ Date : 2013/4/5
//  @ Author : @shenzhan
//
//

public class Population {
	public int Size; // 种群大小
	public double Fitness; // 种群的适应值
	public List<Individual> PopulationSet; // 种群的集合体

	/**
	 * 初始化种群
	 */
	public Population() {
	     PopulationSet=new LinkedList<Individual>();
	}
	
	/**
	 * 添加个体
	 */
	public void AddIndivdual(Individual Indiv){
		   PopulationSet.add(Indiv);
	}
	public int GetSize(){
		   return PopulationSet.size();
	}
	
	/**
	 * 获取某个个体
	 */
	public Individual Get(int  nIndex){
		return PopulationSet.get(nIndex);
	}
		
	/** 
	 * 设置个体
	 */
	public void Set(int nIndex, Individual Indiv){
		  PopulationSet.set(nIndex, Indiv);
	}
}
