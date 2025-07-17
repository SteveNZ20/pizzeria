package com.steve.pizzeria.controller.admin;

import com.steve.pizzeria.dto.ProductDto;
import com.steve.pizzeria.services.ProductService;
import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private UserService userService;

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

            Path uploadDir = Paths.get(categoryPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fileName = uploadedImage.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            uploadedImage.transferTo(filePath.toFile());

            productDto.setImageUrl("/images/" + category + "/" + fileName);
            productDto.setStatus(true);

            productService.saveProduct(productDto);

        } catch (IOException e) {
            e.printStackTrace(); // Considera usar un logger
        }

        return "redirect:/admin/products";
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar.
     * @return redirección a la lista de productos.
     */
    @PostMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Producto eliminado correctamente.");
        return "redirect:/admin/products";
    }

    /**
     * Muestra la página principal del sitio web (home), listando los productos disponibles.
     *
     * @param model Modelo para inyectar los productos
     * @return vista HTML correspondiente: "index"
     */
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        // Obtener lista de productos y agregarla al modelo
        List<ProductDto> products = productService.getAllProducts();
        model.addAttribute("products", products);

        // Si el usuario está autenticado, obtener su info y agregarla al modelo
        if (principal != null) {
            String username = principal.getName();
            UserDto user = userService.getUserDetailsByUsername(username);
            model.addAttribute("user", user);
        }

        return "index";
    }

    @GetMapping("/menu")
    public String menu(Model model, Principal principal) {
        // Obtiene todos los productos de tu servicio
        List<ProductDto> allProducts = productService.getAllProducts();

        // 1. Agrupa los productos por categoría
        Map<String, List<ProductDto>> productsByCategory = allProducts.stream()
                .collect(Collectors.groupingBy(ProductDto::getCategory));

        // 2. Obtiene una lista con los nombres de las categorías
        List<String> categories = productsByCategory.keySet().stream()
                .sorted() // Ordena las categorías alfabéticamente si lo deseas
                .collect(Collectors.toList());

        // 3. Añade ambas variables al modelo
        model.addAttribute("categories", categories);
        model.addAttribute("productsByCategory", productsByCategory);

        // Si el usuario está autenticado, obtener su info y agregarla al modelo
        if (principal != null) {
            String username = principal.getName();
            UserDto user = userService.getUserDetailsByUsername(username);
            model.addAttribute("user", user);
        }

        // Renderiza la vista 'menu.html'
        return "menu";
    }

    @GetMapping("/faq")
    public String showFaqPage(Model model, Principal principal) {
        // Lógica para el FAQ, si la necesitas.
        // Por ahora, solo necesitas pasar la información del usuario al modelo.
        if (principal != null) {
            String username = principal.getName();
            UserDto user = userService.getUserDetailsByUsername(username);
            model.addAttribute("user", user);
        }
        return "faq"; // Devuelve el nombre de la vista (faq.html)
    }

    @GetMapping("/about")
    public String about(Model model, Principal principal) {
        // Si el usuario está autenticado, obtiene su información y la añade al modelo.
        if (principal != null) {
            String username = principal.getName();
            UserDto user = userService.getUserDetailsByUsername(username);
            model.addAttribute("user", user);
        }
        // Devuelve el nombre de la vista (about.html)
        return "about";
    }

    /**
     * Muestra el formulario de edición para un producto existente, precargando sus datos.
     *
     * @param id ID del producto a editar.
     * @param model Modelo para inyectar los datos del producto en la vista.
     * @return vista HTML del formulario de edición.
     */
    @GetMapping("/admin/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        try {
            ProductDto productDto = productService.getProductById(id);
            model.addAttribute("product", productDto);
            return "admin/products/edit";
        } catch (RuntimeException e) {
            // Manejar el caso si el producto no se encuentra
            return "redirect:/admin/products";
        }
    }

    /**
     * Procesa el formulario de edición para actualizar un producto.
     * También maneja la carga de la nueva imagen si se selecciona una.
     *
     * @param productDto DTO con los datos del producto editados, incluyendo la imagen (opcional).
     * @return redirección a la lista de productos.
     */
    @PostMapping("/admin/products/edit")
    public String updateProduct(@ModelAttribute("product") ProductDto productDto, RedirectAttributes redirectAttributes) {
        try {
            if (productDto.getImage() != null && !productDto.getImage().isEmpty()) {
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
            } else {
                // Si no se carga una nueva imagen, mantén la URL de la imagen existente.
                ProductDto existingProduct = productService.getProductById(productDto.getId());
                productDto.setImageUrl(existingProduct.getImageUrl());
            }

            productService.updateProduct(productDto);
            redirectAttributes.addFlashAttribute("message", "Producto actualizado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto.");
        }
        return "redirect:/admin/products";
    }
}