/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.Maps;

import java.util.List;

/**
 *
 * @author ariel
 */
public class Nodes {
    public int grado;// Define la cantidad (2^grado) de elementos en el mapa que abarca el nodo.
    public int[] valores;// Arreglo para almacenar los valores en decimales que contiene el nodo.
    public boolean type; // Para el tipo, "No importa (false)" o "1(true)".
                        //Cuando se halla la solucion TYPE se utiliza para marcar a los nodos como pertenecientes a la mejor solucion.
    public boolean flag;// true para si el nodo pertenece a un nodo de mayor orden.
    public boolean isBridge;
    
    public Nodes()
    {
        this.grado = 0;
        this.flag=false;
    }
    public Nodes(int valor, boolean tipo)
    {
        valores=new int[1];
        this.grado = 0;
        valores[0]=valor;
        this.flag=false;
        this.type=tipo;
    }
    public Nodes(int[] elementos, int orden, boolean tipo)
    {
        this.valores=elementos;
        grado=orden;
        type=tipo;
        this.flag=false;
        
    }
    //****************************************************************************
    // RETORNA 1 SI EL NODO ES ADYACENTE CON EL NODO QUE SE LE PASA COMO PARAMETRO
    // Y 0 SI NO ES ADYACENTE
    //****************************************************************************
    public int IsJoinable(Nodes a) 
    {
        if (a.grado==this.grado)
        {
           if (a.valores.length==this.valores.length)
            
            {
               int ctrol=0;
               int j=0;
               int i=0;
               boolean no_check_adyacencia=false;
               for (i=0;i<a.valores.length;++i)
               {
                   for(j=0;j<this.valores.length;++j)
                   {
                       for (int z=0;z<a.valores.length;z++)
                          if (a.valores[z]==this.valores[j])
                              no_check_adyacencia=true;
                        if(a.valores[i]!= this.valores[j]&& no_check_adyacencia==false)
                       {
                        if ((this.Adyacencia(a.valores[i],this.valores[j])==1))
                            ctrol++;
                        }
                      no_check_adyacencia=false;
                   }
                }
               
               if (ctrol==this.valores.length)
               {   
                   return 1;
                }
               else
                   return 0;
            }
            else
           {
                return 0;
            }
        }
        else
            return 0;
        
    }
    //******************************************************************
    // RECIBE COMO PARAMETROS DOS ENTEROS. COMPRUEBA SI EXISTE ENTRE ELLOS
    // ADYACENCIA BINARIA.
    //******************************************************************
    public int Adyacencia(int a, int b)
    {                                   
        int c = 0;
        int temp =a^b;
         for (int i=Mapa_Karnaough.getNum_Var(); i>=0; i--)
         {
              if ((1<<i & temp) !=0)
                  c++;
        }
              if (c==1)
              {
                  return 1;
              }
              else
              {
                  return 0;
                }
    }
    public String GetSumando(int num_var,boolean min_or_max)
    {//min_or_max is "true" for the map express en min terms y false if the map
     //is express en max terms
       char[]variables=new char[num_var];
       boolean[] estado=new boolean[num_var];///para saber si la variable de grado i-esimo no cambia dentro del nodo
       String factor=new String();
       this.grado=num_var;
       int orden=(int)Math.pow(2, this.grado-1);
       for(int i=0;i<num_var;i++)
       {
           variables[i]=(char)('A'+i);
           estado[i]=true;
       }
       for(int i=0;i<this.valores.length;i++)
       {//FOR CALC WICH VARIABLE DON'T CHANGE INSIDE NODE. IF
        //THE VARIABLE CHANGE ESTADO[I] IS SET FALSE.
            if(i==valores.length-1)
            {
                int temp=valores[i]^valores[0];
                
                for(int z=0;z<num_var;z++)
                {
                   if((temp&(orden>>z))!=0)
                   {
                       estado[z]=false;
                       
                    }
                }
            }
            else
            {
              int temp=valores[i]^valores[i+1];
                
                for(int z=0;z<num_var;z++)
                {
                   if((temp&(orden>>z))!=0)
                       estado[z]=false;
                }  
            }

       }
       if(min_or_max)
       {
           for(int z=0;z<num_var;z++)
               if(estado[z])//si la variable no cambio dentro del nodo
               {
                   int primer_valor=this.valores[0];
                   if(((int) Math.pow(2, num_var-z-1)&primer_valor)==0)//ERROR EN ESTA LINEA
                       factor=factor+variables[z]+"'";
                   else
                       factor=factor+variables[z];
               }
       }
       else
       {
            for(int z=0;z<num_var;z++)
             if(estado[z])//si la variable no cambio dentro del nodo
               {
                   int primer_valor=this.valores[0];
                   if(((int) Math.pow(2, num_var-z-1)&primer_valor)==0)//ERROR EN ESTA LINEA
                       factor=factor+variables[z]+"+";
                   else
                       factor=factor+variables[z]+"'+";
               }
           
            if(factor.charAt(factor.length()-1)=='+')
                factor=factor.substring(0,factor.length()-1);
        }
       return factor;

    }
    public boolean IsBridge(Nodes temp1, Nodes temp2)
    {
//    Retorna el Nodo que es la interseccion de dos Nodos. Si no existe esa insterseccion
//    retorna un nodo sin valores
    	boolean[] cont=new boolean[this.valores.length];
    	boolean sol=false;
    	for (int i=0;i<valores.length;i++)
    		cont[i]=false;
    	
		for (int i=0;i<this.valores.length;i++)
			for(int j=0;j<temp1.valores.length;j++)
				if(this.valores[i]==temp1.valores[j]) 
				 cont[i]=true;
		for (int i=0;i<this.valores.length;i++)
			for(int j=0;j<temp2.valores.length;j++)
				if(this.valores[i]==temp2.valores[j]) 
				 cont[i]=true;
		for (int i=0;i<valores.length;i++)
    		if(cont[i]!=true)
    			sol=true;
		return sol;
		
    			
    			
		
    	
    }
    public boolean equal(Nodes temp)
    {
    	if (temp.grado == this.grado)
    	{
    		int cont=0;
			for (int i=0;i<this.valores.length;i++)
    			for(int j=0;j<temp.valores.length;j++)
    				if(this.valores[i]==temp.valores[j])
    					cont++;
			if((int)Math.pow(2, grado)==cont)
				return true;
			else
				return false;
    	}
    	else
    		return false;
		
    	
    }
    public void SetValores(int[] nuevos_valores)
    {
        this.valores=nuevos_valores;
    }
    public int[] GetValores()
    {
        return this.valores;
    }
    public void Print(){
      for (int i=0;i<this.valores.length;i++)
         System.out.println(this.valores[i]+" ,  ");
    }
}
