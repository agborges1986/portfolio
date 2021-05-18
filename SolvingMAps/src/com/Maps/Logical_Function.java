/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.Maps;

/**
 *
 * @author ariel
 */
public class Logical_Function extends True_Table{
    private String function;
    private static boolean min_or_max;



public Logical_Function(String log_function, int var)
    {
    super(var);
    this.function=log_function;
    

    }
/**
 *
 * @param ecuacion
 * @param ctdad_variables
 * @return
 */
public Mapa_Karnaough toMapa(int ctdad_variables )//Para convertir el string de la funcion en una tabla
   {

     String variables="ABCDEFGHIJKLMNOPQRST";
     variables=variables.substring(0, ctdad_variables);
     String sumando="";
     Mapa_Karnaough mapa=new Mapa_Karnaough(ctdad_variables);
     int [] ones;
     String signos="+'()";///Esto es para preever los terminos max
     int potencia=0;
     int[] var_values=new int[variables.length()];
     
    if(min_or_max)
    {
	    for (int i=0;i<this.function.length();i++)// actualizar sumando
	    {
	        sumando += this.function.charAt(i);
	        if (String.valueOf(this.function.charAt(i)).equals(String.valueOf("+"))|| i==this.function.length()-1)
	        //**********************************************************************************************
	        // A PARTIR DE AQUI SE CALCULA LOS UNOS DE LA TABLA DE VERDAD QUE GENERA EL SUMANDO EN CUESTION
	        //**********************************************************************************************
	        {
	            if (i!=this.function.length()-1)
	                sumando=sumando.substring(0,sumando.length()-1);
	//            System.out.println("Este es el sumando: "+ sumando);
	            for (int j=0;j<variables.length();j++)
	                var_values[j]=-1;// Pongo todas las variables como no importa el valor(-1)
	            
	            for (int pos_var=0;pos_var<variables.length();pos_var++)
	            {
	                for (int j=0; j< sumando.length();j++)
	                {
	                    if((sumando.charAt(j)!=signos.charAt(1)) && j<sumando.length()-1)// Aseguro no procesar el simbolo '
	                       if (sumando.charAt(j)== variables.charAt(pos_var))
	                        { //System.out.println("Aqui");
	                           if (sumando.charAt(j+1)!=signos.charAt(1))
	                                var_values[pos_var]=1;
	                             else
	                                var_values[pos_var]=0;
	                        }
	                    if((sumando.charAt(j)!=signos.charAt(1)) && j==sumando.length()-1 && sumando.charAt(j)== variables.charAt(pos_var))
	                        var_values[pos_var]=1;
	                }
	                     
	
	            }//LA VARIABLE var_values ALMACENA EL VALOR DE CADA VARIABLE PARA UN SUMANDO(1 SI ES VERDADERA, 0 SI ES FALSA Y -1 SI NO IMPORTA(O SEA, VARIA PARA LOS ELEMENTOS DEL NODO))
	            
	            //CREO UNA VARIABLE PARA ALMACENAR LAS DIFERENTES SOLUCIONES VERDADERAS DENTRO DE LA TABLA DE VERDAD
	            potencia=0;
	            for (int g=0;g<var_values.length;g++)
	             if (var_values[g]==-1)
	                 potencia++;
	           
	            ones=new int[(int)Math.pow(2,(double)potencia)];//Cantidad de unos que se derivan de un sumando
	            int f=0;
	            int g=0;
	            
	         //***********************************************************************
	         // EN ESTOS TRES CICLOS ANIDADOS SE BUSCAN TODOS LOS VALORES PARA LOS QUE
	         // SE CUMPLE EL SUMANDO Y SE AGREGAN EN LA TABLA DE VERDAD
	         //***********************************************************************
	            
	            int z=0;
	             {
	                 for (g=0;g<mapa.table.length;g++)
	                 {
	                    for (int k=0;k<var_values.length;k++)
	                    {
	                     potencia=(int)Math.pow(2,(double)(var_values.length-1-k));
	                     if (var_values[k]==-1)
	                         f++;
	                     if((potencia & g)==potencia && var_values[k]==1)
	                        {
	                         f++;
	                        }
	                     if((potencia & g)==0 && var_values[k]==0)
	                         f++;
	                     }
	                    if(f==var_values.length)
	                    {
	                         ones[z]=g;
	                         z++;
	                     }
	                    f=0;
	                 }
	            }
	         mapa.SetOnesFunction(ones);
	         sumando="";                                                                           // y que para el ultimo valor, si no esta negado no de un error con las siguientes operaciones
	        }
	      //************************************************************************************************
	      //AQUI TERMINA EL TRABAJO CON EL SUMANDO EN CUESTION
	      //************************************************************************************************
	
	     }
    }
    else //Para cuando la funcion es expresada en max terms
    {
    	for(int i=0;i<mapa.table.length;i++)
			mapa .table[i]=1;
    	for (int i=0;i<this.function.length();i++)
    	{
    		if(function.charAt(i)!='(' && function.charAt(i)!=')')
    			sumando+=function.charAt(i);
    		if(function.charAt(i)==')')
    		{
    			
    			 for (int j=0;j<variables.length();j++)//Inicializo el arreglo var_values con el valor no importa
 	                var_values[j]=-1;
    			 for(int j=0;j<sumando.length();j++)
    			 {
    				 if(j<sumando.length()-1)
    				 {
    					 for(int z=0;z<ctdad_variables;z++)
    						 if(variables.charAt(z)==sumando.charAt(j) && sumando.charAt(j+1)==signos.charAt(1))
    							 var_values[z]=1;
    						 else
    							 if(variables.charAt(z)==sumando.charAt(j))
    							 var_values[z]=0;
    				 }
    				 else
    				 {
    					
    					 for(int z=0;z<ctdad_variables;z++)
    						 if(variables.charAt(z)==sumando.charAt(j))
    							 var_values[z]=0;
    				}
    			 }
    			potencia=0;
 	            for (int g=0;g<var_values.length;g++)
 	            {
 	            	 
 	            	if (var_values[g]==-1)
 	            		potencia++;
 	            }
 	            int[] zeros = new int[(int)Math.pow(2,(double)potencia)];
 	           
 	            int f=0,c=0;
 	            {
    			 for(int z=0;z<mapa.table.length;z++)
    			 {
    				 for (int k=0;k<var_values.length;k++)
	                    {
	                     potencia=(int)Math.pow(2,(double)(var_values.length-1-k));
	                     if (var_values[k]==-1)
	                         f++;
	                     if((potencia & z)==potencia && var_values[k]==1)
	                        {
	                         f++;
	                        }
	                     if((potencia & z)==0 && var_values[k]==0)
	                         f++;
	                     }
	                    if(f==var_values.length)
	                    {
	                         zeros[c]=z;
	                         c++;
	                     }
	                    f=0;			 
    			 }
 	            }
    			 mapa.SetZerosFunction(zeros);
    	         sumando="";    		 
    		}
    	}
    }
     return mapa;
   }

    /**
     * @return the function
     */
    public String getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(String function) {
        this.function = function;
    }
	public static boolean isMin_or_max() {
		return min_or_max;
	}
	public static void setMin_or_max(boolean min_or_max) {
		Logical_Function.min_or_max = min_or_max;
	}

}
