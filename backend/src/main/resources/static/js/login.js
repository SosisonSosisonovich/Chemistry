const form = document.getElementById('loginForm');
const messageDiv = document.getElementById('message');

form.addEventListener('submit', function (e) {
    e.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    })
        .then(res => {
            if (!res.ok) throw new Error("Неверные данные");
            return res.text();
        })
        .then(data => {
            localStorage.setItem('authenticated', 'true');
            window.location.href = '/admin.html';
        })
        .catch(err => {
            messageDiv.textContent = 'Ошибка входа: ' + err.message;
        });
});
