package steammachinist.mpagrengine.service.persistance;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.product.Product;
import steammachinist.mpagrengine.mapper.ProductMapper;
import steammachinist.mpagrengine.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Product getById(Long id) {
        return repository.findById(id).map(mapper::toProduct).orElseThrow(EntityNotFoundException::new);
    }

    public List<Product> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(mapper::toProduct).collect(Collectors.toList());
    }

    public void add(Product product) {
        repository.save(mapper.toProductEntity(product));
    }

    public void update(Product product) {
        add(product);
    }

    public void remove(Long id) {
        remove(getById(id));
    }

    public void remove(Product product) {
        repository.delete(mapper.toProductEntity(product));
    }
}
