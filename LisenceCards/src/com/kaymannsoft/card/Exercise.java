package com.kaymannsoft.card;

public class Exercise {
	
	final static String headTagName="head";
	final static String excersiceTagName="Exercise";
	final static String imageTagName="image";
	final static String chooseTagName="option";
	final static String commentTagName="comment";
	final static String rigthTagName="solution";
	final static String imagecommentTagName="image_comment";
	

//	para un numero de ejercicio dentro de una seccion
	public int number;
	
//	pregunta del ejercicio	
	public String exercise;

//	para el encabezado de la pregunta
	public String head;
	
//	imagen ejercicio
	int image;
	
//	imagen del comentario
	int imagecomment;
	
//	posibles respuestas
	public String[] choose;
	
//	respuesta correcta
	public int right;
	
//	comentario para la respuesta correcta
	public String comment;
	
	
//	
//	
	public Exercise(int number, String head, String excersice, int image,int imagecomment, String[] choose,
			int right, String comment){
		
		this.number=number;
		this.head=head;
		this.choose=choose;
		this.right=right;
		this.image=image;
		this.comment=comment;
		this.exercise=excersice;
		this.imagecomment=imagecomment;
	}
	
}
