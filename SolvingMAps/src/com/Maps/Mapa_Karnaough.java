/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.Maps;
import java.util.*;

/**
 *
 * @author ariel
 */
public class Mapa_Karnaough extends True_Table{
    public int height;
    public int witdh;
    public int[][] mapa;
    public static int Num_Nodes;
    private int exception=0; //si esto vale 0 la respuesta es calculada por la via normal
    								//vale 1 si la respuesta es 0 y 2 si la respuesta es 1
    
    
    public Mapa_Karnaough(int var)
    {
        super(var);
        True_Table.setNum_Var(var);

        this.witdh=(int)Math.pow(2, (Math.ceil((double)var/2)));
        this.height=(int)Math.pow(2, (Math.floor((double)var/2)));///Copia del codigo C++
        this.mapa=new int[this.height][this.witdh];
        if (var>2)
        {       
//        System.out.println(this.witdh+" "+this.height);
        
        for(int i=0; i<height; i++)
		{
			for(int j=0; j<witdh; j++)
			{
				// Set every 4x4 block's first 4 vars to gray code
				if(witdh>=4)
					this.mapa[i][j]=this.GrayEncode(j%4+((i%4)*4));
				else
                                	this.mapa[i][j]=this.GrayEncode(j%4+((i%4)*this.witdh));
				// Combine 4x4 blocks into map (5th and higher variables)
                                        this.mapa[i][j]+=16*(j/4+((i/4)*(witdh/4)));

			}
		}
        }
        else

	{
		if(var==2)
		{
			this.mapa[0][0]=0;
			this.mapa[0][1]=1;
			this.mapa[1][0]=2;
			this.mapa[1][1]=3;
		}
		if(var==1)
		{
			this.mapa[0][0]=0;
			this.mapa[1][0]=1;
		}
	}
    }

//2***********************RESUELVE EL MAPA DE KARNOUGH PARA UN NUMERO DE VARIABLES
    /**
     * Solve a map of Karnough. Return a array of Nodes in order.
     * min_or_max are a variable min or max term. If min_ormax are 1 the form of solution is min,
     * if it is 0, then, the form is in max terms
     * @param min_or_max
     * @return Nodes[]
     */
    private Nodes[] Solve(int min_or_max)
    {

        NodesList lista= new NodesList();
        if (min_or_max==0)
        {
            this.Complement_Mapa();//Hago los unos ceros

//            this.PrintTable();
        }
        for (int i=0;i<this.GetNodesPos().length;i++)
        {
            lista.add(this.GetNodesPos()[i]);
            
        }

        Mapa_Karnaough.Num_Nodes=lista.size();
        int a=0;
        Nodes[] solution=null;
        Nodes temp_node;
        int i=0;
        int j=0;
        int c=0;



///COMPRUEBO SI LOS ELEMENTOS DEL MAPA SON IGUALES
        for (int l=0;l<this.height;l++)///Compruebo si no son iguales todos los
            {                           //elementos del mapa
            for(int k=0;k<this.witdh;k++)
            {
                if (this.table[mapa[l][k]]!=this.table[0])
                    a=1;
             }
             }
            if (a==0)
             {
              /////SI TODOS SON CEROS LA SOLUCION NO EXISTE
            	int[] temp=new int[1];
             if (this.table[0]==0)
                 {
            	 exception=1;
            	 solution=new Nodes[1];
            	 solution[0]=new Nodes(0,true);
                 return solution;
                }
             else
                {//SI TODOS SON UNOS LA SOLUCION ESTA EN EL NODO COMPLETO.
            	 exception=2;
            	 solution=new Nodes[1];
            	 solution[0]=new Nodes(0,false);
            	 return solution;
                }
               }
///c es el orden de los nodos. 2 elevado a c es el numero de casillas que abarca un nodo.
//SE ITERA PARA CREAR LOS NODOS DE ORDEN SUPERIOR PARA SIMPLIFICAR LA ECUACION
            else
            {
       


 ////********************************************************************************************
//// ITERACION PARA AGRUPAR NODOS QUE TENGAN LA MAYOR CANTIDAD POSIBLE DE NODOS DE GRADO INFERIOR    OK
////*********************************************************************************************
       c=0;
       for(int y=1;y<=Mapa_Karnaough.getNum_Var();y++)
       {
         for (i=0; i<lista.size()-1;i++)
            {
               for(j=i; j<lista.size();j++)///Compara cada nodo con todos los otros nodos restantes
               {
////                   if (lista.get(i).valores.length==(int)Math.pow((double)2,(double)(y-1)) && lista.get(j).valores.length==(int)Math.pow((double)2,(double)(y-1)))
                  if (lista.get(i).IsJoinable(lista.get(j))==1)
                    {
                        int[]temp=new int[lista.get(i).valores.length*2];
                        for (int z=0;z<((lista.get(i).valores.length)*2);z++)
                        {
                            if (z<lista.get(i).valores.length)
                            {
                                temp[z]=lista.get(i).valores[z];
                            }
                            else
                                temp[z]=lista.get(j).valores[(z-lista.get(i).valores.length)];
                            
                        ///ANADO LOS VALORES DE LOS NODOS PARA UNIRLOS EN UN NODO DE ORDEN SUPERIOR.
                        }
                       c=lista.get(i).grado+1;
                       a=0;
                       Nodes nuevo_nodo=new Nodes(temp,c , false);
                       for(int k=0;k<lista.size();k++)
                        if (this.equals(nuevo_nodo, lista.get(k))==1)//compruebo que el nodo no sea igual a otro
                             a=1;
                                           
                       if(a!=1)
                       {
                          lista.add(nuevo_nodo);

                       }
                           
                                 
                       Mapa_Karnaough.Num_Nodes=lista.size();
                       lista.get(i).flag=true;
                       lista.get(j).flag=true;

                    }
 
                }
             
            }
 ////**************************************************************************************
/////            BORRO LOS NODOS QUE ESTAN CONTENIDOS EN UN NODO DE ORDEN SUPERIOR
////*************************************************************************************
             for (int x=0;x<lista.size();x++)
                 if (lista.get(x).flag==true)
                     lista.remove(x);

        }/////ESTA PARTE OK
       

        for (int x=0;x<lista.size();x++)
          if (lista.get(x).flag==true)
               lista.remove(x);

                   
        Mapa_Karnaough.Num_Nodes=lista.size();
//////************************************************************************************
//////                             FIN DE LA ITERACION PARA AGRUPAR NODOS
/////*************************************************************************************
         
       

////**************************************************************************************
///                    ORGANIZO LOS NODOS SEGUN EL ORDEN DE CADA UNO
///***************************************************************************************
        
        for (i=0;i<Mapa_Karnaough.Num_Nodes-1;i++)          ////
            for (j=i+1;j<Mapa_Karnaough.Num_Nodes;j++)      //// ORGANIZO LOS NODOS SEGUN SU ORDEN
            {   lista.get(i).type=false;                    //// Pongo todos los nodos en falso para despues marcar los que se escogeran para la solucion                                          /////
                if (lista.get(i).grado<lista.get(j).grado)  ////
                {
                    temp_node=lista.get(j);
                    lista.set(lista.get(i), j);
                    lista.set(temp_node, i);
                    
                }

            }
        i=0;
        j=0;
        int[] check=this.GetOnes();//GUARDO LOS UNOS EN CHECK PARA COMPROBAR E IR MARCANDO LOS QUE YA SE ENCUENTRAN EN LA SOLUCION
        int[] temp_ones=this.GetOnes();
        int[] cont_ones_repeat=new int[check.length];
        
        for(int k=0;k<cont_ones_repeat.length;k++)
        	cont_ones_repeat[k]=0;
        //*****************************************************************************************
        //COMPRUEBO CUALES NODOS, ESCOGIENDO LOS DE MAYOR ORDEN, SE DEBEN UTILIZAR PARA LA SOLUCION
        //*****************************************************************************************
        for( j=0;j <lista.size();j++)///Compruebo hasta cuantos nodos, escogiendo los de mayor orden, son necesarios para abarcar la solucion
        {    
            for ( c=0;c<lista.get(j).valores.length;c++)
            {
                
                for ( a=0;a<check.length;a++)
                {
                    if (temp_ones[a]==lista.get(j).valores[c])//COMPRUEBO SI LOS VALORES DE LA TABLA ESTAN EN EL NODO.
                    {
                    	cont_ones_repeat[a]++;
                    	if(check[a]!= -1)
                    	  lista.get(j).type=true;  //MARCO EL NODO COMO NECESARIO PARA LA SOLUCION.
                    	check[a]=-1;
                    }
                }
            }
            
        }
        
        
        
//        *************************************
//      BORRO LOS NODOS QUE NO ESTAN MARCADOS
//      *************************************
//      
      for (c=0;c<lista.size();c++)
          if(lista.get(c).type!=true)
          	lista.remove(c);
      
        boolean isbridge=true;
        for(j=0;j <lista.size();j++)//PARA COMPRABAR SI UN NODO ESTA CONTENIDO EN DOS NODOS A LA VEZ
        {							//CADA NODO DEBE TENER AL MENOS UN VALOR QUE SOLO PERTENEZCA A EL
        	isbridge=true;
        	 for (c=0;c<lista.get(j).valores.length;c++)
        	 {
        		 for (a=0;a<temp_ones.length;a++)
                 {
                     if (temp_ones[a]==lista.get(j).valores[c] && cont_ones_repeat[a]==1)
                     {
                    	 isbridge=false;
                     }
                 }
        	
        	 }
        	 if(isbridge)
        		 lista.get(j).isBridge=true;
        }
        lista=check_solution(lista);
//        *************************************
//        BORRO LOS NODOS QUE NO ESTAN MARCADOS
//        *************************************
//        
//        for (c=0;c<lista.size();c++)
//            if(lista.get(c).type!=true)
//            	lista.remove(c);
        
//        **********************************************
//        BORRO LOS NODOS QUE SE COMPORTAN COMO PUENTES
//        **********************************************
//        for(int z=lista.size()-1;i>0;z--)
//        {
//        	for(int x=0;x<lista.size()-1;x++)
//        		for(int y=x;y<lista.size()-1;y--)
//        			if(lista.get(z).IsBridge(lista.get(x), lista.get(y)))
//        				lista.get(z).type=false;
//        }
////        	
//        for (c=0;c<lista.size();c++)
//            if(lista.get(c).type==true)
//            {
//            	
//            }
//            else
//            {
//            	lista.remove(c);	
//            }
        i=0;
        for (c=0;c<lista.size();c++)
            if(lista.get(c).type==true)
             i++;

        
        solution=new Nodes[i];
        j=0;
        c=0;
        while (c<lista.size() && j<=i)
        {    
            if (lista.get(c).type==true)
            {
              
             solution[j]=new Nodes(lista.get(c).valores,lista.get(c).grado,lista.get(c).type);
             j++;
            }
            c++;

        }
        
        return solution;
       }
        ////*************************FIN DEL CALCULO. DEVUELVO LA MEJOR SOLUCION
    }
private NodesList check_solution(NodesList lista) {
	// TODO Auto-generated method stub
	
	NodesList temp=lista;
	int[] ones=this.GetOnes();
	int[]check=ones;
	
	
	for(int i=0;i<temp.size();i++)//Marco como falso todos los nodos nuevamente
		temp.get(i).type=false;
	
	
	for(int i=0;i<temp.size();i++)
		if(temp.get(i).isBridge==false)
		{
			for(int j=0;j<temp.get(i).valores.length;j++)
				for(int z=0;z<ones.length;z++)
					if (ones[z]==temp.get(i).valores[j])
					{
						if(check[z]!= -1)
	                    	  temp.get(i).type=true;  //MARCO EL NODO COMO NECESARIO PARA LA SOLUCION.
	                    	check[z]=-1;
					}
		}
	for(int i=0;i<temp.size();i++)
		if(temp.get(i).isBridge)
		{
			for(int j=0;j<temp.get(i).valores.length;j++)
				for(int z=0;z<ones.length;z++)
					if (ones[z]==temp.get(i).valores[j])
					{
						if(check[z]!= -1)
	                    	  temp.get(i).type=true;  //MARCO EL NODO COMO NECESARIO PARA LA SOLUCION.
	                    	check[z]=-1;
					}
		}
	
			
				
	return temp;
}



//2*********** FIN*********************************************************
public String Total_Solution(int min_or_max)
{
    Nodes[] sol_nodes= this.Solve(min_or_max);
    String solution = new String();
    
if(exception==1)
	{
		solution="0";
	}
else 
	if(exception==2)
	{
		solution="1";
	}
		else
	{
		   if(min_or_max==1)
		   {
		        for (int i=0;i<sol_nodes.length;i++)
		        {
		            solution=solution+sol_nodes[i].GetSumando(Mapa_Karnaough.getNum_Var(), true)+"+";
		        }
		        solution=solution.substring(0,solution.length()-1);
		    }
		   else
		   {
		       for (int i=0;i<sol_nodes.length;i++)
		        {
		            solution=solution+"("+sol_nodes[i].GetSumando(Mapa_Karnaough.getNum_Var(), false)+")";
		        }
		
		    }
	}
	exception=0;

    return solution;


//    String variables="ABCDEFGHIJKLMN";
//    String signos="'+)(";
//
//    for (int i=0; i< sol_nodes.length;i++)  //posicion del nodo
//    {
//        for(int j=0;j<Mapa_Karnaough.getNum_Var();j++)//posicion de las variables
//        {
//            int k=0;
//            //COMPRUEBO SI LA VARIABLE j VARIA DENTRO DE LOS VALORES DEL NODO
//            while (k <sol_nodes[i].valores.length-1 &&  ((sol_nodes[i].valores[k]>>(Mapa_Karnaough.getNum_Var()-1-j))& 1)==((sol_nodes[i].valores[k+1]>>(Mapa_Karnaough.getNum_Var()-1-j))& 1))
//            {
//                temp=(sol_nodes[i].valores[k]>>j)& 1;
//                k++;
//
//            }
//            if(k==sol_nodes[i].valores.length-1)
//            {
//                solution=solution+""+variables.charAt(j);
//                if (((sol_nodes[i].valores[k]>>(Mapa_Karnaough.getNum_Var()-1-j))& 1)==0)
//                    solution += "'";
//            }
//        }
//        //*************************************************************************************
//        //AQUI HAGO EL STRING CONCATENANDO CON SOLUTION EN DEPENDENCIA DE LOS VALORES DEL NODO.
//        //*************************************************************************************
//
//     if (i!=sol_nodes.length-1)
//            solution += "+";
//    }
//    temp=0;
//    if (min_or_max==1)
//        return solution;
//    else
//
//    {   String solution_max="(";
//        for (int i=0; i<solution.length();i++)
//        {temp=0;
//            if(String.valueOf(solution.charAt(i)).compareTo(String.valueOf("'"))!=0 )
//            {
//
//                if(i<solution.length()-1)
//                 {
//                     if (String.valueOf(solution.charAt(i+1)).compareTo(String.valueOf("'"))!=0 && String.valueOf(solution.charAt(i)).compareTo(String.valueOf("+"))!=0)
//                     {
//                        solution_max=solution_max.concat(String.valueOf(solution.charAt(i))).concat("'");
//                        temp++;
//                        if (String.valueOf(solution.charAt(i+1)).compareTo(String.valueOf("+"))!=0)
//                            solution_max=solution_max+"+";
//                     }
//                     if (String.valueOf(solution.charAt(i+1)).compareTo(String.valueOf("'"))==0 && i+2<solution.length())
//                     {
//                         if(String.valueOf(solution.charAt(i+2)).compareTo(String.valueOf("+"))!=0)
//                            solution_max=solution_max.concat(String.valueOf(solution.charAt(i)))+"+";
//                         else
//                            solution_max=solution_max.concat(String.valueOf(solution.charAt(i)));
//                     }
//
//
//                 }
//
//
//                if (i==solution.length()-1)
//                {
//                    solution_max=solution_max+solution.charAt(i)+"'";
//
//                }
//                if (String.valueOf(solution.charAt(i)).compareTo(String.valueOf("+"))==0)
//                   solution_max=solution_max+")(";
//
//            }
//        }
////        AQUI REALIZO EL CAMBIO DE VARIABLES PARA EL MAX.
//
//        if (String.valueOf(solution_max.charAt(solution_max.length()-1)).compareTo("+")==0)
//        {
//            solution_max=solution_max.substring(0,solution_max.length()-1);
//
//        }
//        solution_max+=")";
//        return solution_max;
//    }
   }




        ///// FIN DEL BORRADO
public final int GrayEncode(int g)
    {     
    switch(g)
	{
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
    @Override
    public void SetOnesFunction(int[] pos)
    {
        
        for(int i=0;i<this.height;i++)
            for (int j=0;j<this.witdh;j++)
                for(int k=0;k<pos.length;k++)
                  if (mapa[i][j]==pos[k])
                      this.table[mapa[i][j]]=1;


    }
    @Override
    public void SetZerosFunction(int[] pos)
    {
        
        for(int i=0;i<this.height;i++)
            for (int j=0;j<this.witdh;j++)
                for(int k=0;k<pos.length;k++)
                  if (mapa[i][j]==pos[k])
                      this.table[mapa[i][j]]=0;
    }
    @Override
public void SetDontCareFunction(int[] pos)
    {
    
        for(int i=0;i<this.height;i++)
            for (int j=0;j<this.witdh;j++)
                for(int k=0;k<pos.length;k++)
                  if (mapa[i][j]==pos[k])
                     this.table[mapa[i][j]]=2;// The Integer 2 represent the values Dont Care for function
    }

public int equals(Nodes a, Nodes b)///Retorna 0 si son diferentes y 1 si son iguales
    {
    int c=0;
    if (a.grado!=b.grado)
        return 0;
    else
    {
        for (int i=0;i<a.valores.length;i++)
        {
            for (int j=0;j<b.valores.length;j++)
            {
                if (a.valores[i]==b.valores[j])
                    c++;
            }
        }
       if (c==a.valores.length)
           return 1;
       else
           return 0;
    }

    }
public void Complement_Mapa(){
   int k;
   int j;
    for (k=0;k<this.height; k++ )
                for (j=0;j<this.witdh;j++)
                {
                   if (this.table[mapa[k][j]]==1)
                       this.table[mapa[k][j]]=0;
                   else if(this.table[mapa[k][j]]==0)
                       this.table[mapa[k][j]]=1;
                }


}
   //****************************************************************************************
 //REGRESA UN VECTOR QUE CONTIENE TODOS LOS NODOS DE GRADO CERO (LOS UNOS DE LAS FUNCIONES)
 //****************************************************************************************
    public Nodes[] GetNodesPos()
    {
        Nodes[] list_nodes=null;
        int cont=0;
        int k;
        int j;
            for (k=0;k<this.height; k++ )
                for (j=0;j<this.witdh;j++)
                   if (this.table[mapa[k][j]]==1||this.table[mapa[k][j]]==2)
                        cont++;

        list_nodes=new Nodes[cont];
             cont=0;
             for ( k=0;k<this.height; k++ )
                for (j=0;j<this.witdh;j++)
                {
                   if (this.table[mapa[k][j]]==1)
                    {
                    list_nodes[cont]=new Nodes(this.mapa[k][j],true);
                    cont++;
                    }
                    if (this.table[mapa[k][j]]==2)
                    {
                        list_nodes[cont]=new Nodes(this.mapa[k][j],false);
                        cont++;
                    }
                 }

        return  list_nodes;
    }

    void SetZerosFunction(int i) {
       this.table[i]=0;
    }
    void SetOnesFunction(int i) {
    	this.table[i]=1;
    }
    void SetDontCareFunction(int i) {
    	this.table[i]=2;
    }
    
//1**************FIN**********************************************************
 
}

