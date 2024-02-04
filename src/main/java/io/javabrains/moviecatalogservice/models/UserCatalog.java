package io.javabrains.moviecatalogservice.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCatalog {
	
	private String userId;
	private List<CatalogItem> catalogItems;

}
