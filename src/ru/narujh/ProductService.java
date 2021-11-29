package ru.narujh;

import ru.springframework.bean.factory.stereotypes.Component;
import ru.springframework.bean.factory.stereotypes.Resource;
import ru.springframework.beans.factory.annotation.Autowired;

@Component
public class ProductService {

//	@Autowired
	@Resource(name = "keks")
	private PromotionsService promotionsService;

	public PromotionsService getPromotionsService() {
		return promotionsService;
	}

	public void setPromotionsService(PromotionsService promotionsService) {
		this.promotionsService = promotionsService;
	}

}
