/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kaymannsoft.kmaps.util;

import com.kaymannsoft.kmaps.model.KNodes;
import com.kaymannsoft.kmaps.model.Kmaps;

/**
 *
 * @author ariel
 */
public class Solve_KMAP
{
   public static void main(String[] args)
    {
       KNodes lista;
       int number_of_variables=4;
       String solution=new String();
       Kmaps mapa=new Kmaps(number_of_variables);
//       for (int i=0; i< mapa.height;i++)
//       {
//           System.out.println();
//           for(int j=0;j<mapa.witdh;j++)
//               System.out.print(mapa.mapa[i][j]+"  ");
//        }
//       System.out.println();

       int[] vala=new int[]{12,4};
       KNodes a;
      
       a=new KNodes(4,vala,false);
//       System.out.println(a.GetSumando(number_of_variables, false));
//       System.out.println(a.GetSumando(number_of_variables, true));
        
//PARA PROBAR LA FUNCION LOGICA Y SU REDUCCION EN MIN OR MAX TERMS. PARA ESTO SOLO HAY QUE CAMBIAR EL PARAMETRO
//DEL METODO toMapa POR 0 SI SE QUIERE EN MAX Y POR 1 SI SE QUIERE EN MIN
//       Logical_Function function=new Logical_Function("A'BC'D+AB'+AD",number_of_variables);
//       mapa=function.toMapa(number_of_variables);
//       mapa.PrintTable();
//       System.out.println(" Esta es la solucion de la funcion logica EN MAX TERMS: " + mapa.Total_Solution(0));
//       for (int i=0; i< mapa.height;i++)
//       {
//           System.out.println();
//           for(int j=0;j<mapa.witdh;j++)
//               System.out.print(mapa.mapa[i][j]+"  ");
//        }
////       PARA PROBAR LA SOLUCION CON MAX TERM Y CON MIN TERMS
      int [] ones={3,6};
      int[] dont_care={};
       

       mapa.SetOnes(ones);
       mapa.SetDontCare(dont_care);
       solution=mapa.Total_Solution(true);
       System.out.println();
       mapa.PrintTable();
       System.out.println(" Esta es la solucion para min term: "+solution);
//       mapa.PrintTable();
       int[] ones1={0,1,2,4,5,6,7,8,9,10,11,12,13,14,15};
       mapa.SetOnes(ones1);
       mapa.SetZeros(ones);
       solution=mapa.Total_Solution(false);
       System.out.println(" Esta es la solucion para max term: "+solution);
//       char cad='A';
//       int bad=(int)cad;
//       bad++;
//
//       for(int i=0;i<10;i++)
//           System.out.println((char)('A'+i));
//       int or=3^2|7^6;
//      System.out.println(""+or);
   }
  
   }
  
