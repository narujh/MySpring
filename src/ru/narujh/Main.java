package ru.narujh;

import ru.springframework.beans.factory.BeanFactory;

public class Main {

	public static void main(String[] args) {
		BeanFactory beanFactory = new BeanFactory();
		beanFactory.instantiate("ru.narujh");
		try {
			beanFactory.populateProperties();
		} catch (ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProductService productService = (ProductService) beanFactory.getBean("productService");
		System.out.println(productService.getPromotionsService());
	}

}
