package com.kaymansoft.calories;

import com.kaymansoft.settings.UserSettings;

public class BMRUtils {
	
	/**
	 * Calcula el BMR actual del susuario dada la configuración en la BD
	 * @param us la configuracion de la información del usuario en la BD
	 * @return el BMR actual
	 */
	public static double getActualBMR(UserSettings us) {
		double	heightInM 	= us.getHeightInM(),
				weightInKg	= us.getWeightInKg(),
				age 		= us.getAge();
		String 	sex 		= us.getSex();
		if(sex.equalsIgnoreCase("M"))
			return 66.0 + (13.7*weightInKg) + (500.0 * heightInM) - (6.8*age);
		else
			return 655.0 + (9.6*weightInKg) + (180.0 * heightInM) - (4.7*age);
	}
	/**
	 * Devuelve el BMR deseado por el usuario
	 * @param us la configuracion de la información del usuario en la BD 
	 * @return el BMR deseado
	 */
	public static double getDesiredBMR(UserSettings us) {
		double	heightInM 			= us.getHeightInM(),
				desiredWeightInKg	= us.getDesiredWeightInKg(),
				age 				= us.getAge();
		String 	sex 				= us.getSex();
		if(sex.equalsIgnoreCase("M"))
			return 66.0 + (13.7*desiredWeightInKg) + (500.0 * heightInM) - (6.8*age);
		else
			return 655.0 + (9.6*desiredWeightInKg) + (180.0 * heightInM) - (4.7*age);
	}

}
