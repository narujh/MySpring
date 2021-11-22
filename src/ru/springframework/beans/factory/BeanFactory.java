package ru.springframework.beans.factory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import ru.springframework.bean.factory.stereotypes.Component;
import ru.springframework.beans.factory.annotation.Autowired;

public class BeanFactory {
	private Map<String, Object> singletons = new HashMap<>();

	public Object getBean(String beanName) {
		return singletons.get(beanName);
	}

	public void populateProperties() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		for (Object object : singletons.values()) {
			for (Field field : object.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Autowired.class)) {
					for (Object dependency : singletons.values()) {
						if (dependency.getClass().equals(field.getType())) {
							String setterName = "set" + field.getName().substring(0, 1).toUpperCase()
									+ field.getName().substring(1);
							Method setter = object.getClass().getMethod(setterName, dependency.getClass());
							setter.invoke(object, dependency);
						}
					}
				}
			}
		}
	}

	public void instantiate(String basePackage) {
		String path = basePackage.replace('.', '/');
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		findBeans(path, classLoader);
	}

	/**
	 * Looking for all annotated beans
	 * 
	 * @param path        - path of package where the search will take place
	 * @param classLoader
	 */
	private void findBeans(String path, ClassLoader classLoader) {
		Enumeration<URL> resources;
		try {
			resources = classLoader.getResources(path);
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				File file = new File(resource.toURI());
				for (File classFile : file.listFiles()) {
					String fileName = classFile.getName();
					if (fileName.endsWith(".class")) {
						String className = fileName.substring(0, fileName.indexOf('.'));
						Class<?> classObject = Class.forName(path.replace('/', '.') + "." + className);
						if (classObject.isAnnotationPresent(Component.class)) {
							Object instance = classObject.newInstance();
							String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
							singletons.put(beanName, instance);
						}
					}

					if (classFile.isDirectory()) {
						String newPath = path + "/" + classFile.getName();
						findBeans(newPath, classLoader);
					}
				}
			}
		} catch (IllegalAccessException | IOException | URISyntaxException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
