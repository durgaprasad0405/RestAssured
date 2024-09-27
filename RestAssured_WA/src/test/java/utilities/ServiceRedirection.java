package utilities;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import driver.DriverScript;

public class ServiceRedirection  extends Global {

	@Test
	public void serviceRedirection() {//throws NoSuchMethodException, SecurityException, ClassNotFoundException {

		ClassLoader classLoader = DriverScript.class.getClassLoader();
		Object instance;
		Method method;
		try {
			Class<?> type = classLoader.loadClass("testScripts." + serviceName);
			instance = type.getConstructor().newInstance();
			method = type.getMethod(serviceName);
			method.invoke(instance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception in serviceRedirection Method1:- " + e.getMessage());
		}
	}
	
}
