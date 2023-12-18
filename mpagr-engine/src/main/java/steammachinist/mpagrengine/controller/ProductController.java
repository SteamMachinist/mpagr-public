package steammachinist.mpagrengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import steammachinist.mpagrengine.dto.product.Product;
import steammachinist.mpagrengine.service.persistance.ProductService;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService service;

    @GetMapping("/all")
    public String getProductsPage(Model model) {
        model.addAttribute("products", service.getAll());
        return "product-all";
    }

    @GetMapping("/add")
    public String getAddPage(Model model) {
        model.addAttribute("isNew", true);
        model.addAttribute("product", new Product());
        model.addAttribute("queriesAsString", "");
        return "product-form";
    }

    @PostMapping("/add")
    public String addProduct(Product product, String queriesAsString) {
        product.setQueries(Arrays.asList(queriesAsString.split("\n")));
        //sheetService.addProductToSheet(product);
        service.add(product);
        return "redirect:/product/all";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(Model model, @PathVariable Long id) {
        model.addAttribute("isNew", false);
        Product product = service.getById(id);
        model.addAttribute("product", product);
        model.addAttribute("queriesAsString", String.join("\n", product.getQueries()));
        return "product-form";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(Product product, String queriesAsString) {
        product.setQueries(Arrays.asList(queriesAsString.split("\n")));
        service.update(product);
        return "redirect:/product/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        Product product = service.getById(id);
        service.remove(product);
        return "redirect:/product/all";
    }
}
