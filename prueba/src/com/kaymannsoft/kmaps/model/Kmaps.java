package com.kaymannsoft.kmaps.model;

import java.util.ArrayList;
import java.util.List;




import com.kaymannsoft.kmaps.util.KUtil;

public class Kmaps implements Kmaps_Solve{

	public int 		num_var, witdh, height;
	public int[]	table;
	public int[][]  kmap;
	public int exception=0;
	
//	boolean min_terms;
	List<KNodes> solution=new ArrayList<KNodes>();
	private int num_Nodes;
	
	public Kmaps(int num_var){
		
		this.num_var=num_var;
		this.table=new int [(int)Math.pow((double)2,(double)num_var)];
//		this.min_terms=true;
		createKmap(num_var);
	}
	
	private void createKmap(int num_var) {
		// TODO Auto-generated method stub
		
	        this.witdh=(int)Math.pow(2, (Math.ceil((double)num_var/2)));
	        this.height=(int)Math.pow(2, (Math.floor((double)num_var/2)));///Copia del codigo C++
	        this.kmap=new int[this.height][this.witdh];
	        
	        if (num_var>2){
	        	for(int i=0; i<height; i++){
	        		for(int j=0; j<witdh; j++){
					// Set every 4x4 block's first 4 vars to gray code
	        			if(witdh>=4)
	        				this.kmap[i][j]=KUtil.GrayEncode(j%4+((i%4)*4));
	        			else
	                        this.kmap[i][j]=KUtil.GrayEncode(j%4+((i%4)*this.witdh));
					// Combine 4x4 blocks into map (5th and higher variables)
	                        this.kmap[i][j]+=16*(j/4+((i/4)*(witdh/4)));

	        		}
	        	}
	        }
	        else{
	        	if(num_var==2){
					this.kmap[0][0]=0;
					this.kmap[0][1]=1;
					this.kmap[1][0]=2;
					this.kmap[1][1]=3;
					}
	        	if(num_var==1){
					this.kmap[0][0]=0;
					this.kmap[1][0]=1;
					}
	        	}
	}

	public Kmaps(int num_var, int[] table){
		
		this.num_var=num_var;
		this.table=table;
		
	}
	
	public Kmaps(int num_var, int[][] kmap){
		
	}

	public int[] GetZeros() {
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

	public int[] GetOnes() {
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

	public int[] GetDontCare() {
		 int c = 0;
		    for (int i=0;i<this.table.length;i++)
		        if (this.table[i]==2)
		            c++;
		    int[] zeros=new int[c];
		    c=0;
		    for (int i=0;i<this.table.length;i++)
		        if (this.table[i]==2)
		        {
		            zeros[c]=i;
		            c++;
		        }
		    return zeros;
	}

	
	

	public void SetZeros(int[] zeros) {
		for(int i=0; i<zeros.length; i++)
	        table[zeros[i]]=0;
	}

	public void SetOnes(int[] ones) {
		for(int i=0; i<ones.length; i++)
	        table[ones[i]]=1;
	}

	public void SetDontCare(int[] dont_care) {
		for(int i=0; i<dont_care.length; i++)
	        table[dont_care[i]]=2;
	}
	
	public  List<KNodes> GetNodesPos()
    {
        List<KNodes> list_nodes=new ArrayList<KNodes>();
		
		int k;
		int j;
           
         for ( k=0;k<this.height; k++ )
            for (j=0;j<this.witdh;j++){
               if (this.table[kmap[k][j]]==1){
                list_nodes.add(new KNodes(true,this.kmap[k][j]));
                }
                if (this.table[kmap[k][j]]==2){
                    list_nodes.add(new KNodes(false, this.kmap[k][j]));
                    }
             }

        return  list_nodes;
    }
	
//2***********************RESUELVE EL MAPA DE KARNOUGH PARA UN NUMERO DE VARIABLES
    /**
     * Solve a map of Karnough. Return a array of Nodes in order.
     * min_or_max are a variable min or max term. If min_ormax are 1 the form of solution is min,
     * if it is 0, then, the form is in max terms
     * @param min_or_max
     * @return Nodes[]
     */

	public List<KNodes> Solve(boolean min_terms) {

	        if (!min_terms){
	            this.Complement_Mapa();//Hago los unos ceros
	            }
	        
	       solution=this.GetNodesPos();
	       num_Nodes=solution.size();
	       
	       int a=0;
	       KNodes temp_node;
	       int i=0,j=0,c=0;
	       

	///COMPRUEBO SI LOS ELEMENTOS DEL MAPA SON IGUALES
	        for (int l=0;l<this.height;l++) {///Compruebo si no son iguales todos los
	                                     //elementos del mapa
	            for(int k=0;k<this.witdh;k++){
	                if (this.table[kmap[l][k]]!=this.table[0])
	                    a=1;
	             }
	            }
	            if (a==0)
	             {
	              /////SI TODOS SON CEROS LA SOLUCION NO EXISTE
	             if (this.table[0]==0)
	                 {
	            	 exception=1;
	            	 solution.add(new KNodes(true,0));
	                 return solution;
	                }
	             else
	                {//SI TODOS SON UNOS LA SOLUCION ESTA EN EL NODO COMPLETO.
	            	 exception=2;
	            	 solution.add(new KNodes(false,0));
	            	 return solution;
	                }
	               }
	///c es el orden de los nodos. 2 elevado a c es el numero de casillas que abarca un nodo.
	//SE ITERA PARA CREAR LOS NODOS DE ORDEN SUPERIOR PARA SIMPLIFICAR LA ECUACION
	            
	            else{
	////********************************************************************************************
	//// ITERACION PARA AGRUPAR NODOS QUE TENGAN LA MAYOR CANTIDAD POSIBLE DE NODOS DE GRADO INFERIOR    OK
	////*********************************************************************************************
	       c=0;
	       for(int y=1;y<=this.num_var;y++){
	         for (i=0; i<solution.size()-1;i++){
	               for(j=i; j<solution.size();j++)///Compara cada nodo con todos los otros nodos restantes
	               {
////	                   if (lista.get(i).valores.length==(int)Math.pow((double)2,(double)(y-1)) && lista.get(j).valores.length==(int)Math.pow((double)2,(double)(y-1)))
	                  if (solution.get(i).IsJoinable(solution.get(j),this.num_var))
	                    {
	                        int[]temp=new int[solution.get(i).values.length*2];
	                        for (int z=0;z<((solution.get(i).values.length)*2);z++)
	                        {
	                            if (z<solution.get(i).values.length)
	                            {
	                                temp[z]=solution.get(i).values[z];
	                            }
	                            else
	                                temp[z]=solution.get(j).values[(z-solution.get(i).values.length)];
	                            
	                        ///AñADO LOS VALORES DE LOS NODOS PARA UNIRLOS EN UN NODO DE ORDEN SUPERIOR.
	                        }
	                       c=solution.get(i).grade+1;
	                       a=0;
	                       KNodes nuevo_nodo=new KNodes(c , temp,false);
	                       for(int k=0;k<solution.size();k++)
	                        if (KUtil.equals(nuevo_nodo, solution.get(k)))//compruebo que el nodo no sea igual a otro
	                             a=1;
	                       if(a!=1)
	                    	   solution.add(nuevo_nodo);

	                           
	                                 
	                      this.num_Nodes=solution.size();
	                      solution.get(i).isPart=true;
	                      solution.get(j).isPart=true;

	                    }
	 
	                }
	             
	            }
	 ////**************************************************************************************
	/////            BORRO LOS NODOS QUE ESTAN CONTENIDOS EN UN NODO DE ORDEN SUPERIOR
	////*************************************************************************************
	             for (int x=0;x<solution.size();x++)
	                 if (solution.get(x).isPart==true)
	                     solution.remove(x);

	        }/////ESTA PARTE OK
	       

	        for (int x=0;x<solution.size();x++)
	          if (solution.get(x).isPart==true)
	               solution.remove(x);

	                   
	       this.num_Nodes=solution.size();
	//////************************************************************************************
//////	                             FIN DE LA ITERACION PARA AGRUPAR NODOS
	/////*************************************************************************************
	         
	       

	////**************************************************************************************
	///                    ORGANIZO LOS NODOS SEGUN EL ORDEN DE CADA UNO
	///***************************************************************************************
	        
	        for (i=0;i<this.num_Nodes-1;i++)          ////
	            for (j=i+1;j<this.num_Nodes;j++)      //// ORGANIZO LOS NODOS SEGUN SU ORDEN
	            {   solution.get(i).type=false;                    //// Pongo todos los nodos en falso para despues marcar los que se escogeran para la solucion                                          /////
	                if (solution.get(i).grade<solution.get(j).grade)  ////
	                {
	                    temp_node=solution.get(j);
	                    solution.set(j,solution.get(i));
	                    solution.set(i,temp_node);
	                    
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
	        for( j=0;j <solution.size();j++)///Compruebo hasta cuantos nodos, escogiendo los de mayor orden, son necesarios para abarcar la solucion
	        {    
	            for ( c=0;c<solution.get(j).values.length;c++)
	            {
	                
	                for ( a=0;a<check.length;a++)
	                {
	                    if (temp_ones[a]==solution.get(j).values[c])//COMPRUEBO SI LOS VALORES DE LA TABLA ESTAN EN EL NODO.
	                    {
	                    	cont_ones_repeat[a]++;
	                    	if(check[a]!= -1)
	                    	  solution.get(j).type=true;  //MARCO EL NODO COMO NECESARIO PARA LA SOLUCION.
	                    	check[a]=-1;
	                    }
	                }
	            }
	            
	        }
	        
	        
	        
//	        *************************************
//	      BORRO LOS NODOS QUE NO ESTAN MARCADOS
//	      *************************************
//	      
	      for (c=0;c<solution.size();c++)
	          if(solution.get(c).type!=true)
	          	solution.remove(c);
	      
	        boolean isbridge=true;
	        for(j=0;j <solution.size();j++)//PARA COMPRABAR SI UN NODO ESTA CONTENIDO EN DOS NODOS A LA VEZ
	        {							//CADA NODO DEBE TENER AL MENOS UN VALOR QUE SOLO PERTENEZCA A EL
	        	isbridge=true;
	        	 for (c=0;c<solution.get(j).values.length;c++)
	        	 {
	        		 for (a=0;a<temp_ones.length;a++)
	                 {
	                     if (temp_ones[a]==solution.get(j).values[c] && cont_ones_repeat[a]==1)
	                     {
	                    	 isbridge=false;
	                     }
	                 }
	        	
	        	 }
	        	 if(isbridge)
	        		 solution.get(j).isBridge=true;
	        }
	        solution=check_solution(solution);
//	        *************************************
//	        BORRO LOS NODOS QUE NO ESTAN MARCADOS
//	        *************************************
//	        
//	        for (c=0;c<lista.size();c++)
//	            if(lista.get(c).type!=true)
//	            	lista.remove(c);
	        
//	        **********************************************
//	        BORRO LOS NODOS QUE SE COMPORTAN COMO PUENTES
//	        **********************************************
//	        for(int z=lista.size()-1;i>0;z--)
//	        {
//	        	for(int x=0;x<lista.size()-1;x++)
//	        		for(int y=x;y<lista.size()-1;y--)
//	        			if(lista.get(z).IsBridge(lista.get(x), lista.get(y)))
//	        				lista.get(z).type=false;
//	        }
////	        	
//	        for (c=0;c<lista.size();c++)
//	            if(lista.get(c).type==true)
//	            {
//	            	
//	            }
//	            else
//	            {
//	            	lista.remove(c);	
//	            }
	        i=0;
	        for (c=solution.size();c==0;c--)
	            if(solution.get(c).type!=true)
	             solution.remove(c);
	        return solution;
	       }
	        ////*************************FIN DEL CALCULO. DEVUELVO LA MEJOR SOLUCION
	}

	private List<KNodes> check_solution(List<KNodes> solution2) {
		
		int[] ones=this.GetOnes();
		int[]check=ones;
		
		
		for(int i=0;i<solution2.size();i++)//Marco como falso todos los nodos nuevamente
			solution2.get(i).type=false;
		
		
		for(int i=0;i<solution2.size();i++)
			if(solution2.get(i).isBridge==false)
			{
				for(int j=0;j<solution2.get(i).values.length;j++)
					for(int z=0;z<ones.length;z++)
						if (ones[z]==solution2.get(i).values[j])
						{
							if(check[z]!= -1)
								solution2.get(i).type=true;  //MARCO EL NODO COMO NECESARIO PARA LA SOLUCION.
		                    	check[z]=-1;
						}
			}
		for(int i=0;i<solution2.size();i++)
			if(solution2.get(i).isBridge)
			{
				for(int j=0;j<solution2.get(i).values.length;j++)
					for(int z=0;z<ones.length;z++)
						if (ones[z]==solution2.get(i).values[j])
						{
							if(check[z]!= -1)
								solution2.get(i).type=true;  //MARCO EL NODO COMO NECESARIO PARA LA SOLUCION.
		                    	check[z]=-1;
						}
			}
		
				
					
		return solution2;
}

	private void Complement_Mapa() {
	// TODO Auto-generated method stub
		 int k;
		 int j;
		 for (k=0;k<this.height; k++ )
			 for (j=0;j<this.witdh;j++){
				 if (this.table[kmap[k][j]]==1)
					 this.table[kmap[k][j]]=0;
				 else if(this.table[kmap[k][j]]==0)
					 this.table[kmap[k][j]]=1;
				 }


	
}

	public int[][] getKmap() {
		
		return this.kmap;
	}
	
	public String Total_Solution(boolean min_terms)
	{
	    List<KNodes> sol_nodes= this.Solve(min_terms);
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
			   if(min_terms)
			   {
			        for (int i=0;i<sol_nodes.size();i++)
			        {
			            solution=solution+sol_nodes.get(i).GetSumando(this.num_var, true)+"+";
			        }
			        solution=solution.substring(0,solution.length()-1);
			    }
			   else
			   {
			       for (int i=0;i<sol_nodes.size();i++)
			        {
			            solution=solution+"("+sol_nodes.get(i).GetSumando(this.num_var, false)+")";
			        }
			
			    }
		}
		exception=0;

	    return solution;


//	    String variables="ABCDEFGHIJKLMN";
//	    String signos="'+)(";
	//
//	    for (int i=0; i< sol_nodes.length;i++)  //posicion del nodo
//	    {
//	        for(int j=0;j<Mapa_Karnaough.getNum_Var();j++)//posicion de las variables
//	        {
//	            int k=0;
//	            //COMPRUEBO SI LA VARIABLE j VARIA DENTRO DE LOS VALORES DEL NODO
//	            while (k <sol_nodes[i].valores.length-1 &&  ((sol_nodes[i].valores[k]>>(Mapa_Karnaough.getNum_Var()-1-j))& 1)==((sol_nodes[i].valores[k+1]>>(Mapa_Karnaough.getNum_Var()-1-j))& 1))
//	            {
//	                temp=(sol_nodes[i].valores[k]>>j)& 1;
//	                k++;
	//
//	            }
//	            if(k==sol_nodes[i].valores.length-1)
//	            {
//	                solution=solution+""+variables.charAt(j);
//	                if (((sol_nodes[i].valores[k]>>(Mapa_Karnaough.getNum_Var()-1-j))& 1)==0)
//	                    solution += "'";
//	            }
//	        }
//	        //*************************************************************************************
//	        //AQUI HAGO EL STRING CONCATENANDO CON SOLUTION EN DEPENDENCIA DE LOS VALORES DEL NODO.
//	        //*************************************************************************************
	//
//	     if (i!=sol_nodes.length-1)
//	            solution += "+";
//	    }
//	    temp=0;
//	    if (min_or_max==1)
//	        return solution;
//	    else
	//
//	    {   String solution_max="(";
//	        for (int i=0; i<solution.length();i++)
//	        {temp=0;
//	            if(String.valueOf(solution.charAt(i)).compareTo(String.valueOf("'"))!=0 )
//	            {
	//
//	                if(i<solution.length()-1)
//	                 {
//	                     if (String.valueOf(solution.charAt(i+1)).compareTo(String.valueOf("'"))!=0 && String.valueOf(solution.charAt(i)).compareTo(String.valueOf("+"))!=0)
//	                     {
//	                        solution_max=solution_max.concat(String.valueOf(solution.charAt(i))).concat("'");
//	                        temp++;
//	                        if (String.valueOf(solution.charAt(i+1)).compareTo(String.valueOf("+"))!=0)
//	                            solution_max=solution_max+"+";
//	                     }
//	                     if (String.valueOf(solution.charAt(i+1)).compareTo(String.valueOf("'"))==0 && i+2<solution.length())
//	                     {
//	                         if(String.valueOf(solution.charAt(i+2)).compareTo(String.valueOf("+"))!=0)
//	                            solution_max=solution_max.concat(String.valueOf(solution.charAt(i)))+"+";
//	                         else
//	                            solution_max=solution_max.concat(String.valueOf(solution.charAt(i)));
//	                     }
	//
	//
//	                 }
	//
	//
//	                if (i==solution.length()-1)
//	                {
//	                    solution_max=solution_max+solution.charAt(i)+"'";
	//
//	                }
//	                if (String.valueOf(solution.charAt(i)).compareTo(String.valueOf("+"))==0)
//	                   solution_max=solution_max+")(";
	//
//	            }
//	        }
////	        AQUI REALIZO EL CAMBIO DE VARIABLES PARA EL MAX.
	//
//	        if (String.valueOf(solution_max.charAt(solution_max.length()-1)).compareTo("+")==0)
//	        {
//	            solution_max=solution_max.substring(0,solution_max.length()-1);
	//
//	        }
//	        solution_max+=")";
//	        return solution_max;
//	    }
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
	
	

