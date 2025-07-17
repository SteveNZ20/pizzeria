document.addEventListener('DOMContentLoaded', () => {

    const addToCartButtons = document.querySelectorAll('.add-to-cart-btn');



    addToCartButtons.forEach(button => {

        button.addEventListener('click', async (event) => {

            const productCard = event.target.closest('.product-card');

            const dataDiv = productCard.getAttribute('data-id') ? productCard : productCard.querySelector('div[data-id]');



// Captura los datos del producto

            const productId = dataDiv.getAttribute('data-id');

            const productName = dataDiv.getAttribute('data-name');

            const price = dataDiv.getAttribute('data-price');

            const image = dataDiv.getAttribute('data-img');

            const userId = dataDiv.getAttribute('data-user');



            if (userId === 'null') {

                alert('Por favor, inicia sesión para agregar productos al carrito.');

                window.location.href = '/login';

                return;

            }



// Prepara los datos para la petición POST

            const requestData = {

                productId: parseInt(productId),

                productName: productName,

                price: parseFloat(price),

                image: image,

                userId: parseInt(userId),

                quantity: 1

            };



            try {

// Envía la petición POST al servidor

                const response = await fetch('/orders/add', {

                    method: 'POST',

                    headers: {

                        'Content-Type': 'application/json'

                    },

                    body: JSON.stringify(requestData)

                });



                if (response.ok) {

                    alert('Producto agregado correctamente al carrito.');

                    window.location.href = '/orders';

                } else {

                    const errorData = await response.json();

                    alert('Error al agregar el producto al carrito: ' + errorData.message);

                }

            } catch (error) {

                console.error('Error:', error);

                alert('Ocurrió un error al agregar el producto al carrito.');

            }

        });

    });

});