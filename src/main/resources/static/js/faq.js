document.addEventListener('DOMContentLoaded', () => {
    const questions = document.querySelectorAll('.accordion-question');

    questions.forEach(question => {
        question.addEventListener('click', () => {
            const parentItem = question.parentNode;
            parentItem.classList.toggle('open');
        });
    });
});