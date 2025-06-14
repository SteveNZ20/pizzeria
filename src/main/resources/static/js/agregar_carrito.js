// Este script se encarga de manejar la funcionalidad de agregar productos al carrito
// Si el usuario no está logueado (sin data-user), redirige a /login

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".add-to-cart").forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault(); // Evita cualquier comportamiento por defecto del botón

            const card = this.closest(".product-card");

            // Buscar el contenedor con los atributos data-*
            const dataContainer = card.querySelector("[data-id]");
            if (!dataContainer) {
                console.warn("No se encontró contenedor de datos en el producto");
                return;
            }

            const userId = dataContainer.getAttribute("data-user");

            if (!userId) {
                // Usuario no logueado, redirigir al login
                window.location.href = "/login";
                return;
            }

            const order = {
                userId: parseInt(dataContainer.getAttribute("data-user")),
                productId: parseInt(dataContainer.getAttribute("data-id")),
                productName: dataContainer.getAttribute("data-name"),
                price: parseFloat(dataContainer.getAttribute("data-price")),
                quantity: 1,
                image: dataContainer.getAttribute("data-img")
            };

            fetch("http://localhost:9092/orders", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(order)
            })
                .then(response => {
                    if (!response.ok) throw new Error("Error al enviar el producto");
                    return response.json();
                })
                .then(data => {
                    alert("Producto agregado al carrito");
                })
                .catch(error => {
                    console.error(error);
                    alert("Error al agregar producto");
                });
        });
    });
});
