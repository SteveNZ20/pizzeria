document.addEventListener('DOMContentLoaded', function () {
    const btnGuardar = document.getElementById('btnGuardar');
    const tableBody = document.querySelector('.cart-table tbody'); // Clase actualizada a 'cart-table'

    if (btnGuardar) {
        btnGuardar.addEventListener('click', function () {
            const inputs = document.querySelectorAll('input[type="number"]:not([disabled])');
            const updates = [];

            inputs.forEach(input => {
                const id = input.getAttribute('data-id');
                const quantity = parseInt(input.value);

                updates.push({ id: id, quantity: quantity });
            });

            const updatePromises = updates.map(update => {
                return fetch(`/orders/update/${update.id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ quantity: update.quantity })
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Error al actualizar pedido con ID ' + update.id);
                        }
                    });
            });

            Promise.all(updatePromises)
                .then(() => {
                    alert('Cambios guardados exitosamente.');
                    location.reload();
                })
                .catch(error => {
                    console.error(error);
                    alert('Ocurrió un error al guardar los cambios.');
                });
        });
    }

    if (tableBody) {
        tableBody.addEventListener('click', function (event) {
            const targetButton = event.target.closest('.delete-btn');
            if (targetButton) {
                const orderId = targetButton.getAttribute('data-id');
                if (confirm('¿Estás seguro de que deseas eliminar este producto del carrito?')) {
                    eliminarProducto(orderId);
                }
            }
        });
    }

    function eliminarProducto(id) {
        fetch(`/orders/delete/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    alert('Producto eliminado correctamente.');
                    location.reload();
                } else {
                    alert('Error al eliminar el producto del carrito.');
                    console.error('Error al eliminar el producto:', response.statusText);
                }
            })
            .catch(error => {
                alert('Ocurrió un error al intentar eliminar el producto.');
                console.error('Error:', error);
            });
    }
});

function habilitarCantidad(index) {
    const inputCantidad = document.getElementById(`cantidad-${index}`);
    const btnEditar = document.getElementById(`btnEditar-${index}`);
    const btnGuardar = document.getElementById('btnGuardar');

    if (inputCantidad && btnEditar && btnGuardar) {
        inputCantidad.removeAttribute('disabled');
        btnEditar.setAttribute('disabled', 'true');
        btnGuardar.removeAttribute('disabled');
    }
}