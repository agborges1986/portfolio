package com.kaymannsoft.kmaps.model;


/**
 * @author Ariel
 *
 */

public class KNodes {
	
// Define the grade of node. (2^grade) is the number of node's elements.
	 public int grade;
	 
// Values of the node in decimal form
	 public int[] values;
	 
//	 
	 public boolean type; 

//	 Is true if the node belong to other node of major order
	 public boolean isPart;
	 
//	 Is True if the node is bridge. Brigde is a node that have all yours
//	 values is 2 or more nodes.
	 public boolean isBridge;
	 
	 
	 
	 
	 
	public KNodes(int grade, int[] values, boolean type, boolean isPart,
			boolean isBridge) {
		super();
		this.grade = grade;
		this.values = values;
		this.type = type;
		this.isPart = isPart;
		this.isBridge = isBridge;
	}
	
	
	public KNodes(int grade, int[] values, boolean type) {
		this.grade = grade;
		this.values = values;
		this.type = type;
	}







	public KNodes(int grade, int[] values) {
			super();
			this.grade = grade;
			this.values = values;
		}

	



	public KNodes(boolean type,int...values) {
			this.values = values;
			this.type = type;
		}





//****************************************************************************
// RETORNA TRUE SI EL NODO ES ADYACENTE CON EL NODO QUE SE LE PASA COMO PARAMETRO
// Y FALSE SI NO ES ADYACENTE
//****************************************************************************
	 public boolean IsJoinable(KNodes node, int num_var){
		 
	        if (node.grade==this.grade){
	           if (node.values.length==this.values.length){
	               
	        	   int ctrol=0;
	               int j=0;
	               int i=0;
	               boolean check_adjancent=false;
	               
	               for (i=0;i<node.values.length;++i){
	                   for(j=0;j<this.values.length;++j){
	                       for (int z=0;z<node.values.length;z++)
	                          if (node.values[z]==this.values[j])
	                              check_adjancent=true;
	                        if(node.values[i]!= this.values[j]&& check_adjancent==false){
		                        if (KNodes.Adyacencia(node.values[i],this.values[j], num_var))
		                            ctrol++;
	                        }
	                        check_adjancent=false;
	                   }
	                }
	               if (ctrol==this.values.length)
	                   return true;
	               else
	                   return false;
	            }
	            else
	            	return false;
	        }
	        else
	            return false;
	        
	    }
	 
//******************************************************************
// RECIBE COMO PARAMETROS DOS ENTEROS. COMPRUEBA SI EXISTE ENTRE ELLOS
// ADYACENCIA BINARIA.
//******************************************************************
	    public static boolean Adyacencia(int a, int b, int num_var){                                   
	        
			int c = 0;
			int temp =a^b;
			for (int i=num_var; i>=0; i--){
			  if ((1<<i & temp) !=0)
			      c++;
			}
	        if (c==1)
	        	return true;
	        else
	        	return false;
	    
	    }
	    
	    public boolean equal(KNodes temp){
	    	
	    	if (temp.grade == this.grade){
	    		
	    		int cont=0;
				for (int i=0;i<this.values.length;i++)
	    			for(int j=0;j<temp.values.length;j++)
	    				if(this.values[i]==temp.values[j])
	    					cont++;
				if((int)Math.pow(2, grade)==cont)
					return true;
				else
					return false;
	    	}
	    	else
	    		return false;
	    }
	    
//	    Is true if the new values of was push in the Node successful
	    
	    public boolean SetValores(int[] new_values){
	    	
	    	if (this.values.length== new_values.length){
	    		this.values=new_values;
	    		return true;
	    	}
	    	else
	    		return false;
	    }
	    
	    public int[] GetValores(){
	        return this.values;
	    }
	    
//	    Returns true is this node is brigde of to another nodes
	    public boolean IsBridge(KNodes temp1, KNodes temp2)
	    {

	    	boolean[] cont=new boolean[this.values.length];
	    	boolean sol=false;
	    	for (int i=0;i<values.length;i++)
	    		cont[i]=false;
	    	
			for (int i=0;i<this.values.length;i++)
				for(int j=0;j<temp1.values.length;j++)
					if(this.values[i]==temp1.values[j]) 
					 cont[i]=true;
			for (int i=0;i<this.values.length;i++)
				for(int j=0;j<temp2.values.length;j++)
					if(this.values[i]==temp2.values[j]) 
					 cont[i]=true;
			for (int i=0;i<values.length;i++)
	    		if(cont[i]!=true)
	    			sol=true;
			return sol;
				
	    }
	    
	    public String GetSumando(int num_var,boolean min_or_max)
	    {
	    //min_or_max is "true" for the map express en min terms y false if the map
	     //is express en max terms
	       char[]variables=new char[num_var];
	       boolean[] estado=new boolean[num_var];///para saber si la variable de grado i-esimo no cambia dentro del nodo
	       String factor=new String();
	       this.grade=num_var;
	       int orden=(int)Math.pow(2, this.grade-1);
	       
	       for(int i=0;i<num_var;i++){
	    	   
	           variables[i]=(char)('A'+i);
	           estado[i]=true;
	       }
	       for(int i=0;i<this.values.length;i++){
	    	//FOR CALC WICH VARIABLE DON'T CHANGE INSIDE NODE. IF
	        //THE VARIABLE CHANGE ESTADO[I] IS SET FALSE.
	            if(i==values.length-1){
	                int temp=values[i]^values[0];
	                for(int z=0;z<num_var;z++){
	                   if((temp&(orden>>z))!=0){
	                       estado[z]=false;
	                       }
	                   }
	                }
	            else{
	              int temp=values[i]^values[i+1];
	              for(int z=0;z<num_var;z++){
	            	  if((temp&(orden>>z))!=0)
	            		  estado[z]=false;
	            	  } 
	              }
	            }
	       if(min_or_max){
	    	   for(int z=0;z<num_var;z++)
	    		   if(estado[z]){//si la variable no cambio dentro del nodo
	    			   int primer_valor=this.values[0];
	    			   if(((int) Math.pow(2, num_var-z-1)&primer_valor)==0)//ERROR EN ESTA LINEA
	    				   factor=factor+variables[z]+"'";
	    			   else
	    				   factor=factor+variables[z];
	    			   }
	    	   }
	       else{
	            for(int z=0;z<num_var;z++)
	             if(estado[z]){//si la variable no cambio dentro del nodo
	                   int primer_valor=this.values[0];
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

}
