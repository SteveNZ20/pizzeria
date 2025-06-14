function habilitarCantidad(index) {
    const inputCantidad = document.getElementById(`cantidad-${index}`);
    const btnEditar = document.getElementById(`btnEditar-${index}`);
    const btnGuardar = document.getElementById('btnGuardar');

    inputCantidad.removeAttribute('disabled');
    btnEditar.setAttribute('disabled', 'true');
    btnGuardar.removeAttribute('disabled');
}

document.getElementById('btnGuardar').addEventListener('click', function () {
    const inputs = document.querySelectorAll('input[type="number"]:not([disabled])');
    const updates = [];

    inputs.forEach(input => {
        const id = input.getAttribute('data-id');
        const quantity = input.value;

        updates.push({ id: id, quantity: quantity });
    });

    updates.forEach(update => {
        fetch(`http://localhost:9092/orders/${update.id}`, {
            method: 'PUT', // o PATCH si prefieres
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ quantity: update.quantity })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error al actualizar pedido con id ' + update.id);
                }
            })
            .catch(error => {
                console.error(error);
                alert('Ocurrió un error al guardar los cambios.');
            });
    });

    alert('Cambios guardados exitosamente.');
    location.reload(); // Opcional, para recargar la vista con datos actualizados
});