package com.kaymannsoft.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
	
//	Cantidad de preguntas del Test. Puede variar y no ser igual
//	a la cantidad de ejercicios que tenemos pero si siempre es menor que esa cantidad
	int ask_quantity;
	
//	Arreglo con los ejercicios para el test
	Exercise[] excersices;
	
//	Para saber en que pregunta se encuentra el test;
	private int ASK_COUNTER;
	
//	Para manejar el estado de cada pregunta y del test completo
	
	
	private boolean FINISH_TEST_STATUS= false;
	static private boolean GENERATED_TEST_STATUS= false;
	
//	el porciento de aprobado del examen
	public final double INDICEPASS;
	
	private int good_answers;
	private final int[] generate_test;
	
//	Constructor para hacer un Test con menor cantidad de preguntas que de ejercicios 
	public Test(int ask_quantity, double pass, Exercise[] total_asks){
				
		this.INDICEPASS=pass;
		this.excersices=total_asks;
		this.good_answers=0;
		setASK_COUNTER(1);
		setFINISH_TEST_STATUS(false);
		
		if (total_asks.length>ask_quantity){
			this.ask_quantity=ask_quantity;
			generate_test=new int[ask_quantity];
			generateTest();
			Test.setGENERATED_TEST_STATUS(true);
		}
		else{
			this.ask_quantity=total_asks.length;
			generate_test=new int[ask_quantity];
		}
	}
	
	public Test(double pass,  Exercise[] total_asks){
		
		this.ask_quantity=total_asks.length;
		this.INDICEPASS=pass;
		this.excersices=total_asks;
		this.good_answers=0;
		setASK_COUNTER(1);
		setFINISH_TEST_STATUS(false);
		generate_test=new int[ask_quantity];
	}
	
	public Test( Exercise[] total_asks){
		
		this.ask_quantity=total_asks.length;
		this.INDICEPASS=0.75;
		this.excersices=total_asks;
		this.good_answers=0;
		setASK_COUNTER(1);
		setFINISH_TEST_STATUS(false);
		generate_test=new int[ask_quantity];
	}

	public int getGood_answers() {
		return good_answers;
	}

	public void addGood_answers() {
		this.good_answers ++;
	}
	
	public double calification(){		
		return (double)good_answers/getASK_COUNTER();
	}
	
//	Para saber si se paso el Test con un indice mayor
	public boolean testIsPassed(){
		if(calification()<=INDICEPASS)
			return false;
		else
			return true;
	}
	
//	para aumentar el marcador de la preguntas
	public boolean passAsk(){
		
		if (getASK_COUNTER()<this.ask_quantity){
			setASK_COUNTER(getASK_COUNTER() + 1);
			return true;
		} else{
			this.setFINISH_TEST_STATUS(true);
			return false;
		}
	}
	
	public Exercise getNowExercise(){
		if(!isGENERATED_TEST_STATUS()){
			return excersices[getASK_COUNTER()-1];
		}
		else
			return excersices[generate_test[getASK_COUNTER()-1]];
	}
//	Para evaluar una pregunta si esta respondida correctamente
	public boolean evaluateAsk(int answer){
		if(!isGENERATED_TEST_STATUS()){
			if(excersices[getASK_COUNTER()-1].right==answer){
				addGood_answers();
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if(excersices[generate_test[getASK_COUNTER()-1]].right==answer){
				addGood_answers();
				return true;
			}
			else{
				return false;
			}
		}
		
	}
	
//	Para evaluar una respuesta de una pregunta con indice ask_index
	public boolean evaluateAsk(int answer, int ask_index){
		if(excersices[ask_index].right==answer){
			return true;
		}
		else{
			return false;
		}
		
	}
	public int getAnswerNow(){
		if(!isGENERATED_TEST_STATUS())
			return excersices[getASK_COUNTER()-1].right;
		else
			return excersices[generate_test[getASK_COUNTER()-1]].right;
	}
/*//Para generar un test aleatorio con menor cntidad de preguntas que la cantidad
//	de ejercicios
*/	
	public void generateTest(){
//		TODO Arreglar mejor esta funcion
		Random rand=new Random();
		List<Integer> ask_choose= new ArrayList<Integer>();
		int quantity=generate_test.length;
		
		for(int i=1;i<=excersices.length;i++)
		{
			ask_choose.add(i);
		}
				
		
		for(int i=0;i<quantity;i++){			
			int index=rand.nextInt(ask_choose.size());
			generate_test[i]=ask_choose.get(index);
			ask_choose.remove(index);
			
		}
	}

	public static boolean isGENERATED_TEST_STATUS() {
		return GENERATED_TEST_STATUS;
	}

	private static void setGENERATED_TEST_STATUS(boolean gENERATED_TEST_STATUS) {
		GENERATED_TEST_STATUS = gENERATED_TEST_STATUS;
	}

	public boolean isFINISH_TEST_STATUS() {
		return FINISH_TEST_STATUS;
	}

	public void setFINISH_TEST_STATUS(boolean fINISH_TEST_STATUS) {
		if(fINISH_TEST_STATUS){
			FINISH_TEST_STATUS = fINISH_TEST_STATUS;
			setASK_COUNTER(ask_quantity);
		}
	}

	public int getASK_COUNTER() {
		return ASK_COUNTER;
	}

	public void setASK_COUNTER(int aSK_COUNTER) {
		ASK_COUNTER = aSK_COUNTER;
	}
	
	public void finishTest(){
		ASK_COUNTER=ask_quantity;
		FINISH_TEST_STATUS=true;
	}

}
