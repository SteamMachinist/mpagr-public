package steammachinist.mpagrengine.mapper;

import org.mapstruct.Mapper;
import steammachinist.mpagrengine.dto.product.Product;
import steammachinist.mpagrengine.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductEntity productEntity);
    ProductEntity toProductEntity(Product product);
}
