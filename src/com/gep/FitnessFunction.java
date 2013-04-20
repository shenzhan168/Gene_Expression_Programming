package com.gep;

//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : @gep
//  @ File Name : FitnessFunction.java
//  @ Date : 2013/4/5
//  @ Author : @shenzhan
//
//
/**
 * 适应度函数 
 * 对不同的问题采用不同的适应度
 * @author shenzhan
 *
 */
public abstract  class FitnessFunction {
	
	public FitnessFunction(){
		
	}
	
	/**
	 * 个体表达类
	 */
	protected Expression Exp=new Expression();
	/**
	 * 计算适应度
	 * @param Pop
	 * @param Data
	 * @param Fitness
	 */
	public abstract void GetFitness(Population Pop, double[][] Data,double[] Fitness);
}
