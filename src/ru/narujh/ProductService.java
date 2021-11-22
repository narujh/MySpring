package ru.narujh;

import ru.springframework.bean.factory.stereotypes.Component;
import ru.springframework.beans.factory.annotation.Autowired;

@Component
public class ProductService {
	@Autowired
	private PromotionsService promotionService;

	public PromotionsService getPromotionService() {
		return promotionService;
	}

	public void setPromotionService(PromotionsService promotionService) {
		this.promotionService = promotionService;
	}

}
