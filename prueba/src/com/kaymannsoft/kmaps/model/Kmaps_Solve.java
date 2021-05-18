package com.kaymannsoft.kmaps.model;

import java.util.List;

public abstract interface Kmaps_Solve {
	
	abstract public int[] GetZeros();
	abstract public int[] GetOnes();
	abstract public int[] GetDontCare();
	abstract public void SetZeros(int[] zeros);
	abstract public void SetOnes(int[] ones);
	abstract public void SetDontCare(int[] dont_care);
	abstract public List<KNodes> Solve(boolean min_term);
	abstract public int[][] getKmap();
	
	
	
	

}
