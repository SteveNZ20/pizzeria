package com.steve.pizzeria.controller.admin;

import com.steve.pizzeria.dto.ProductDto;
import com.steve.pizzeria.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controlador para gestionar las vistas de administración de productos.
 * Se encarga de listar, mostrar el formulario de creación y guardar productos nuevos.
 * Accede a rutas del panel de administrador como /admin/products.
 *
 * Este controlador trabaja con {@link ProductService} y utiliza {@link ProductDto}.
 * Las imágenes se suben al sistema de archivos local.
 *
 * @author Franco
 * @version 1.0
 */
@Controller
public class ProductsController {

    @Autowired
    private ProductService productService;

    /**
     * Muestra la vista principal del listado de productos en el panel de administrador.
     *
     * @param model Modelo de la vista para inyectar la lista de productos
     * @return vista HTML correspondiente: "admin/products/index"
     */
    @GetMapping("/admin/products")
    public String adminProducts(Model model) {
        List<ProductDto> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/products/index";
    }

    /**
     * Muestra el formulario para agregar un nuevo producto.
     *
     * @param model Modelo de la vista para cargar el formulario vacío
     * @return vista HTML correspondiente: "admin/products/add"
     */
    @GetMapping("/admin/products/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new ProductDto());
        return "admin/products/add";
    }

    /**
     * Procesa el formulario para guardar un nuevo producto.
     * También maneja la carga y almacenamiento de la imagen del producto.
     *
     * @param productDto DTO con los datos del producto, incluyendo la imagen cargada
     * @return redirección a la lista de productos (/admin/products)
     */
    @PostMapping("/admin/products/add")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto) {
        try {
            MultipartFile uploadedImage = productDto.getImage();
            String category = productDto.getCategory().toLowerCase();

            String basePath = "/Users/steve/Desktop/Java Projects/Projects/pizzeria/src/main/resources/static/images";
            String categoryPath = basePath + "/" + category;

            java.nio.file.Path uploadDir = java.nio.file.Paths.get(categoryPath);
            if (!java.nio.file.Files.exists(uploadDir)) {
                java.nio.file.Files.createDirectories(uploadDir);
            }

            String fileName = uploadedImage.getOriginalFilename();
            java.nio.file.Path filePath = uploadDir.resolve(fileName);
            uploadedImage.transferTo(filePath.toFile());

            productDto.setImageUrl("/images/" + category + "/" + fileName);
            productDto.setStatus(true);

            productService.saveProduct(productDto);

        } catch (Exception e) {
            e.printStackTrace(); // Considera usar Logger en lugar de imprimir
        }

        return "redirect:/admin/products";
    }

    /**
     * Muestra la página principal del sitio web (home), listando los productos disponibles.
     *
     * @param model Modelo para inyectar los productos
     * @return vista HTML correspondiente: "index"
     */
    @GetMapping("/")
    public String home(Model model) {
        List<ProductDto> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }
}