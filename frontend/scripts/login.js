async function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const btnText = document.getElementById("btnText");
    const btnSpinner = document.getElementById("btnSpinner");
    btnText.classList.add("d-none");
    btnSpinner.classList.remove("d-none");

    try {
        const response = await fetch("http://localhost:8070/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ username, password })
        });

        if (response.ok) {
            const data = await response.json();

            // Store JWT token in localStorage
            localStorage.setItem("authToken", data.token);
            localStorage.setItem("userRole", data.message);

            // Redirect based on backend response
            window.location.href = data.redirectUrl;
        } else {
            // Try to parse JSON error, fallback to plain text if not JSON
            let errorMsg = "Login failed";
            try {
                const errorResponse = await response.json();
                errorMsg = errorResponse.message || errorMsg;
            } catch {
                errorMsg = response.statusText; // fallback if no JSON body
            }
            showError(errorMsg);
        }
    } catch (err) {
        console.error("Login error:", err);
        showError("An error occurred during login.");
    } finally {
        btnText.classList.remove("d-none");
        btnSpinner.classList.add("d-none");
    }
}
