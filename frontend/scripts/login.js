async function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const role = document.getElementById("roleSelect").value;

    const btnText = document.getElementById("btnText");
    const btnSpinner = document.getElementById("btnSpinner");
    btnText.classList.add("d-none");
    btnSpinner.classList.remove("d-none");

    try {
        const response = await fetch("/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ username, password })
        });

        if (response.ok) {
            const redirectUrl = await response.text();

            // Redirect based on backend response
            if (role === "Employee") {
                window.location.href = "employee.html";
            } else if (role === "HR Recruiter") {
                window.location.href = "hr_recruiter.html";
            } else {
                window.location.href = redirectUrl; // fallback
            }
        } else {
            const errorMsg = await response.text();
            showError(errorMsg || "Invalid username or password.");
        }
    } catch (err) {
        console.error("Login error:", err);
        showError("An error occurred during login.");
    } finally {
        btnText.classList.remove("d-none");
        btnSpinner.classList.add("d-none");
    }
}

function showError(message) {
    const errorBox = document.getElementById("errorBox");
    const errorMsg = document.getElementById("errorMsg");
    errorMsg.textContent = message;
    errorBox.classList.remove("d-none");
}

function togglePassword() {
    const passwordInput = document.getElementById("password");
    const toggleIcon = document.getElementById("toggleIcon");
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        toggleIcon.classList.replace("bi-eye", "bi-eye-slash");
    } else {
        passwordInput.type = "password";
        toggleIcon.classList.replace("bi-eye-slash", "bi-eye");
    }
}
