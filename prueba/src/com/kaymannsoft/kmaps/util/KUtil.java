package com.kaymannsoft.kmaps.util;


import com.kaymannsoft.kmaps.model.KNodes;

public class KUtil {
	
	public static int GrayEncode(int g){
		switch(g){
			case 0: return 0;
			case 1: return 1;
			case 2: return 3;
			case 3: return 2;
	
			case 4: return 4;
			case 5: return 5;
			case 6: return 7;
			case 7: return 6;
	
			case 8: return 12;
			case 9: return 13;
			case 10: return 15;
			case 11: return 14;
	
			case 12: return 8;
			case 13: return 9;
			case 14: return 11;
			case 15: return 10;
			
	        default:
	                return 100000000;
        }
	}
	
	public static boolean equals(KNodes a, KNodes b)///Retorna false si son diferentes y true si son iguales
    {
    int c=0;
    if (a.grade!=b.grade)
        return false;
    else
    {
        for (int i=0;i<a.values.length;i++)
        {
            for (int j=0;j<b.values.length;j++)
            {
                if (a.values[i]==b.values[j])
                    c++;
            }
        }
       if (c==a.values.length)
           return true;
       else
           return false;
    }

    }
}
