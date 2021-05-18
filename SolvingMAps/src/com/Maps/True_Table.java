/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.Maps;



/**
 *
 * @author ariel
 */
public class True_Table
{
        private static int Num_Var; //Almacena la cantidad de variables que tiene la funcion
        public int[]table;//In this table if table[n]=1 then n represent (in decimal form) the binary number for each the function
        // f(a,..., Num_Var)=1
    /**
     * @return the Num_Var
     */
    public static int getNum_Var() {
        return Num_Var;
    }

    /**
     * @param aNum_Var the Num_Var to set
     */
    public static void setNum_Var(int aNum_Var) {
        Num_Var = aNum_Var;
    }


//Constructor
public True_Table(int var)
    {
    this.table=new int [(int)Math.pow((double)2,(double)var)];
    True_Table.Num_Var= var;
    }

// The array pos represent the number (in decimal) that de logical function values "1"
public void  SetOnesFunction(int[] pos)
    {
    for(int i=0; i<pos.length; i++)
        table[pos[i]]=1;
    }
public void SetZerosFunction(int[] pos)
    {
    for(int i=0; i<pos.length; i++)
        table[pos[i]]=0;
    }
public void SetDontCareFunction(int[] pos)
    {
    for(int i=0; i<pos.length; i++)
           table[pos[i]]=2; // The Integer 2 represent the values Dont Care for function
    }
public int[] GetOnes(){
    int c = 0;
    for (int i=0;i<this.table.length;i++)
        if (this.table[i]==1)
            c++;
    int[] ones=new int[c];
    c=0;
    for (int i=0;i<this.table.length;i++)
        if (this.table[i]==1)
        {
            ones[c]=i;
            c++;
        }
    return ones;

}
public int[] GetZeros(){
    int c = 0;
    for (int i=0;i<this.table.length;i++)
        if (this.table[i]==0)
            c++;
    int[] zeros=new int[c];
    c=0;
    for (int i=0;i<this.table.length;i++)
        if (this.table[i]==0)
        {
            zeros[c]=i;
            c++;
        }
    return zeros;
    }
public void PrintTable()
    {

    for(int i=0;i<table.length;i++)
        if (table[i]==2)
             System.out.println("Decimal i:"+i+" F("+i+")= *");
        else
         System.out.println("Decimal i:"+i+" F("+i+")"+ table[i]);
    }
}



    