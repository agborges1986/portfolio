package com.kaymansoft.calories;

import com.kaymansoft.settings.UserSettings;

public class BMIUtils {
	
	public static class WeigthRange {
		public double min,max;
	}
	
	public enum BMIClassification {	
		SEVERELY_UNDERWEIGHT,
		UNDERWEIGHT,
		HEALTHY,
		OVERWEIGHT,
		OBESE,
		MORBIDLY_OBESE
	}
	
	private BMIUtils() {}
	
	/**
	 * Calcula el Índice de Masa Corporal actual del usuario
	 * @param us la configuración de información del susuario en la BD
	 * @return el BMI actual calculado
	 * La fórmula que se usa es: [Masa(en kg)]/[Altura(en m)*Altura(en m)]
	 */
	public static double getActualBMI(UserSettings us) {
		double 	heightInM 	= us.getHeightInM(),
				weightInKg	= us.getWeightInKg();
		return weightInKg/(heightInM*heightInM);
	}
	
	/**
	 * Calcula el Índice de Masa Corporal deseado por el usuario
	 * @param us la configuración de información del susuario en la BD
	 * @return el BMI actual calculado
	 * La fórmula que se usa es: [Masa(en kg)]/[Altura(en m)*Altura(en m)]
	 */
	public static double getDesiredBMI(UserSettings us) {
		double 	heightInM 			= us.getHeightInM(),
				desiredWeightInKg 	= us.getDesiredWeightInKg();
		return desiredWeightInKg/(heightInM*heightInM);
	}
	
	/**
	 * Devuelve la clasificación del Índice de Masa Corporal de acuerdo a su valor
	 * Los posibles valores de clasificación son:
	 * <ul>
	 * <li/>Bajopeso severo<li>
	 * <li/>Bajopeso<li>
	 * <li/>Saludable<li>
	 * <li/>Sobrepeso<li>
	 * <li/>Obeso<li>
	 * <li/>Morbosamente Obeso<li>
	 * </ul>
	 * @param bmi el valor del BMI que se quiere clasificar
	 * @return la clasificación del BMI
	 */
	public static BMIClassification getBMIClassification(double bmi) {
		if(bmi<16)
			return BMIClassification.SEVERELY_UNDERWEIGHT;
		if(bmi<18)
			return BMIClassification.UNDERWEIGHT;
		if(bmi<25)
			return BMIClassification.HEALTHY;
		if(bmi<30)
			return BMIClassification.OVERWEIGHT;
		if(bmi<40)
			return BMIClassification.OBESE;
		return BMIClassification.MORBIDLY_OBESE;
	}
	
	/**
	 * Devuelve la clasificación del Índice de Masa Corporal actual del usuario
	 * Los posibles valores de clasificación son:
	 * <ul>
	 * <li/>Bajopeso severo<li>
	 * <li/>Bajopeso<li>
	 * <li/>Saludable<li>
	 * <li/>Sobrepeso<li>
	 * <li/>Obeso<li>
	 * <li/>Morbosamente Obeso<li>
	 * </ul>
	 * @param us la configracion del usuario almacenada en la BD
	 * @return la clasificación del BMI
	 */
	public static BMIClassification getActualBMIClassification(UserSettings us) {
		return getBMIClassification(getActualBMI(us));
	}
	
	/**
	 * Devuelve la clasificación del Índice de Masa Corporal deseado por el usuario
	 * Los posibles valores de clasificación son:
	 * <ul>
	 * <li/>Bajopeso severo<li>
	 * <li/>Bajopeso<li>
	 * <li/>Saludable<li>
	 * <li/>Sobrepeso<li>
	 * <li/>Obeso<li>
	 * <li/>Morbosamente Obeso<li>
	 * </ul>
	 * @param us la configracion del usuario almacenada en la BD
	 * @return la clasificación del BMI
	 */
	public static BMIClassification getDesiredBMIClassification(UserSettings us) {
		return getBMIClassification(getDesiredBMI(us));
	} 	

	/**
	 * Devuelve el rango de peso ideal para el usuario, dada su altura
	 * @param heightInM altra en metros
	 * @return el rango de peso ideal
	 */
	public static WeigthRange getIdealWeightRange(double heightInM) {
		WeigthRange res = new BMIUtils.WeigthRange();
		/* bmi = w/(h*h) -> w = bmi*h*h */
		res.min = 18*heightInM*heightInM;
		res.max = 24.9*heightInM*heightInM;
		return res;		
	}
}
