package com.kaymansoft.settings;

public interface UserSettings {
	
	public boolean isFirstTime();

	public int getExpectedDietDays();
	public void setExpectedDietDays(int expectedDietDays);

	public String getLastConfigurationTime();
	public void setLastConfigurationTime(String lastConfigurationTime);

	public String getUserName();
	public void setUserName(String userName);

	public String getSex();
	public void setSex(String sex);

	public int getAge();
	public void setAge(int age);

	public double getHeightInM();
	public void setHeightInM(double heightInM);

	public double getWeightInKg();
	public void setWeightInKg(double weightInKg);
	
	public double getDesiredWeightInKg();
	public void setDesiredWeightInKg(double expectedWeightInKg);
	
	public String getDailyActivity();
	public void setDailyActivity(String dailyActivity);

	public String getDietStartDay();
	public void setDietStartDay(String dietStartDay);
	
	public String getUsualWakeUpTime();
	public void setUsualWakeUpTime(String usualWakeUpTime);

	public String getWeekendsWakeUpTime();
	public void setWeekendsWakeUpTime(String weekendsWakeUpTime);

	public String getUsualSleepTime();
	public void setUsualSleepTime(String usualSleepTime);

	public String getWeekendsSleepTime();
	public void setWeekendsSleepTime(String weekendsSleepTime);	

	public void save();
	
}