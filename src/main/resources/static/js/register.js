document.addEventListener('DOMContentLoaded', function() {
    // Toggle password visibility
    const passwordInput = document.querySelector('input[type="password"]');
    if (passwordInput) {
        const passwordToggle = document.createElement('span');
        passwordToggle.innerHTML = '👁️';
        passwordToggle.classList.add('password-toggle');
        passwordToggle.addEventListener('click', function() {
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                this.innerHTML = '👁️‍🗨️';
            } else {
                passwordInput.type = 'password';
                this.innerHTML = '👁️';
            }
        });

        passwordInput.parentNode.appendChild(passwordToggle);
    }

    // Form validation
    const form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', function(e) {
            let isValid = true;
            const inputs = form.querySelectorAll('input[required]');

            inputs.forEach(input => {
                if (!input.value.trim()) {
                    input.style.borderColor = 'var(--error-color)';
                    isValid = false;
                } else {
                    input.style.borderColor = '';
                }
            });

            if (!isValid) {
                e.preventDefault();
                showMessage('Por favor, complete todos los campos requeridos', 'error');
            }
        });
    }
});

function showMessage(text, type) {
    const messageDiv = document.createElement('div');
    messageDiv.textContent = text;
    messageDiv.classList.add('status-message', `${type}-message`);

    const form = document.querySelector('form');
    if (form) {
        form.insertBefore(messageDiv, form.firstChild);
        setTimeout(() => {
            messageDiv.style.display = 'block';
        }, 10);

        setTimeout(() => {
            messageDiv.style.opacity = '0';
            setTimeout(() => {
                messageDiv.remove();
            }, 300);
        }, 5000);
    }
}