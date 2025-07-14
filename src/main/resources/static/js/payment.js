const confirmPaymentBtn = document.getElementById('confirmPaymentBtn');
let currentCardBrandInput = null;

const qrModal = document.getElementById('qrModal');
const qrModalImage = document.getElementById('qrModalImage');
const qrModalTitle = document.getElementById('qrModalTitle');

// Referencias a los elementos del estado para actualizarlos
const paymentStatusInput = document.getElementById('paymentStatusInput');
const paymentStatusDisplay = document.getElementById('paymentStatusDisplay');

function showPaymentForm(method) {
    document.querySelectorAll('.payment-form-section').forEach(form => {
        form.style.display = 'none';
        form.querySelectorAll('input, select').forEach(field => {
            field.disabled = true;
            field.removeAttribute('required');
        });
    });

    confirmPaymentBtn.disabled = true;
    confirmPaymentBtn.classList.remove('enabled');

    document.getElementById('cardValidationMessage').textContent = '';

    if (currentCardBrandInput) {
        currentCardBrandInput.remove();
        currentCardBrandInput = null;
    }

    // Restablecer el estado a PENDING al cambiar de método de pago
    paymentStatusInput.value = 'PENDING';
    paymentStatusDisplay.textContent = 'PENDING';

    let selectedSection = null;
    if (method === 'Visa' || method === 'MasterCard' || method === 'American Express') {
        selectedSection = document.getElementById('card-form-section');
        selectedSection.querySelectorAll('input, select').forEach(field => {
            field.disabled = false;
            field.setAttribute('required', 'required');
        });
        const cardBrandInput = document.createElement('input');
        cardBrandInput.type = 'hidden';
        cardBrandInput.name = 'cardBrand';
        cardBrandInput.value = method;
        document.getElementById('mainPaymentForm').appendChild(cardBrandInput);
        currentCardBrandInput = cardBrandInput;

    } else if (method === 'Yape') {
        selectedSection = document.getElementById('yape-qr-section');
    } else if (method === 'Plin') {
        selectedSection = document.getElementById('plin-qr-section');
    }

    if (selectedSection) {
        selectedSection.style.display = 'block';
    }
}

function validateCardForm() {
    const cardFormSection = document.getElementById('card-form-section');
    const cardInputs = cardFormSection.querySelectorAll('input[required], select[required]');
    let allFieldsValid = true;
    let validationMessage = '';

    cardInputs.forEach(input => {
        if (!input.value.trim()) {
            allFieldsValid = false;
            validationMessage = 'Por favor, rellena todos los campos de la tarjeta.';
            input.style.borderColor = 'red'; // Resaltar campo inválido
        } else if (input.hasAttribute('pattern') && !input.value.match(new RegExp(input.pattern))) {
            allFieldsValid = false;
            validationMessage = `Formato incorrecto para ${input.placeholder || input.labels[0].textContent}.`;
            input.style.borderColor = 'red';
        } else {
            input.style.borderColor = ''; // Limpiar borde si es válido
        }
    });

    const expiryMonth = document.getElementById('expiryMonth').value;
    const expiryYear = document.getElementById('expiryYear').value;
    const currentYear = new Date().getFullYear();
    const currentMonth = new Date().getMonth() + 1;

    if (allFieldsValid) {
        if (parseInt(expiryYear) < currentYear || (parseInt(expiryYear) === currentYear && parseInt(expiryMonth) < currentMonth)) {
            allFieldsValid = false;
            validationMessage = 'La fecha de vencimiento no puede ser anterior a la actual.';
            document.getElementById('expiryMonth').style.borderColor = 'red';
            document.getElementById('expiryYear').style.borderColor = 'red';
        } else {
            document.getElementById('expiryMonth').style.borderColor = '';
            document.getElementById('expiryYear').style.borderColor = '';
        }
    }

    const cardValidationMessage = document.getElementById('cardValidationMessage');
    if (allFieldsValid) {
        cardValidationMessage.textContent = 'Datos de tarjeta válidos localmente. Puedes confirmar el pago.';
        cardValidationMessage.style.color = 'green';
        confirmPaymentBtn.disabled = false;
        confirmPaymentBtn.classList.add('enabled');
        // Aquí se cambia el estado a PROCESS
        paymentStatusInput.value = 'PROCESS';
        paymentStatusDisplay.textContent = 'PROCESS';
    } else {
        cardValidationMessage.textContent = validationMessage || 'Datos de tarjeta inválidos.';
        cardValidationMessage.style.color = 'red';
        confirmPaymentBtn.disabled = true;
        confirmPaymentBtn.classList.remove('enabled');
        // Si la validación falla, el estado debe permanecer PENDING o volver a PENDING
        paymentStatusInput.value = 'PENDING';
        paymentStatusDisplay.textContent = 'PENDING';
    }
}

function enableConfirmPayment(method) {
    confirmPaymentBtn.disabled = false;
    confirmPaymentBtn.classList.add('enabled');
    document.getElementById('cardValidationMessage').textContent = `Listo para pagar con ${method}.`;
    document.getElementById('cardValidationMessage').style.color = 'green';
    // Aquí se cambia el estado a PROCESS
    paymentStatusInput.value = 'PROCESS';
    paymentStatusDisplay.textContent = 'PROCESS';
}

function showQrModal(imageSrc, title) {
    qrModalImage.src = imageSrc;
    qrModalTitle.textContent = `Escanea con ${title}`;
    qrModal.style.display = 'flex';
    document.body.style.overflow = 'hidden'; // Evitar scroll de fondo
}

function closeQrModal() {
    qrModal.style.display = 'none';
    document.body.style.overflow = ''; // Restaurar scroll de fondo
}

// Cerrar modal al presionar ESC
document.addEventListener('keydown', function(event) {
    if (event.key === "Escape" && qrModal.style.display === 'flex') {
        closeQrModal();
    }
});

// Cerrar modal al hacer clic fuera del contenido
qrModal.addEventListener('click', function(event) {
    if (event.target === qrModal) {
        closeQrModal();
    }
});

document.addEventListener('DOMContentLoaded', function() {
    showPaymentForm(''); // Llama con un valor vacío para ocultar todo al inicio
    // Asegúrate de que el estado inicial en el input hidden y el display sea PENDING
    paymentStatusInput.value = 'PENDING';
    paymentStatusDisplay.textContent = 'PENDING';
});