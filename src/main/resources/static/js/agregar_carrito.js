// Este script maneja la funcionalidad de agregar productos al carrito
// y es compatible con las vistas home.html y menu.html

document.addEventListener("DOMContentLoaded", function () {
    // Selecciona todos los botones que pueden ser de agregar al carrito
    const addToCartButtons = document.querySelectorAll(".add-to-cart, .add-to-cart-btn");

    // --- AÑADE ESTA PARTE PARA LA AUTENTICACIÓN ---
    // Credenciales de autenticación. Deben coincidir con las del backend.
    const username = 'admin';
    const password = 'admin123';
    // Codifica las credenciales en Base64 para el encabezado de autorización
    const authHeader = 'Basic ' + btoa(username + ':' + password);
    // ---------------------------------------------

    addToCartButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            // Usamos 'closest' para encontrar el contenedor padre de la tarjeta.
            const card = this.closest(".product-card");

            // Lógica para obtener el contenedor de datos de forma dinámica
            // Si los datos están en el propio botón, los usa.
            // Si no, busca el contenedor hijo que los tiene (como en home.html).
            const dataContainer = this.hasAttribute("data-id") ? this : card.querySelector("[data-id]");

            if (!dataContainer) {
                console.error("No se encontró contenedor de datos para este producto.");
                return;
            }

            const userId = dataContainer.getAttribute("data-user");

            if (userId === "null") {
                alert("Por favor, inicia sesión para agregar productos al carrito.");
                window.location.href = "/login";
                return;
            }

            const order = {
                userId: parseInt(userId),
                productId: parseInt(dataContainer.getAttribute("data-id")),
                productName: dataContainer.getAttribute("data-name"),
                price: parseFloat(dataContainer.getAttribute("data-price")),
                quantity: 1,
                image: dataContainer.getAttribute("data-img")
            };

            fetch("http://localhost:9092/orders", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": authHeader // <<-- AÑADIDO: Incluye el encabezado de autorización
                },
                body: JSON.stringify(order)
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(errorData => {
                            throw new Error(errorData.message || "Error desconocido.");
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    alert("¡Producto agregado al carrito exitosamente!");
                })
                .catch(error => {
                    console.error("Error en la petición:", error);
                    alert("Ocurrió un error al agregar el producto: " + error.message);
                });
        });
    });
});