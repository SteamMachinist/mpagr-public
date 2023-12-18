package steammachinist.mpagrengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import steammachinist.mpagrengine.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
