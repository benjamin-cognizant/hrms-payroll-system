document.getElementById("createUserForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("loginname").value;
    const password = document.getElementById("password").value;
    const role = document.getElementById("role").value;

    try {
        const response = await fetch("http://localhost:8070/api/admin/createUser", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: username,
                password: password,
                role: "ROLE_" + role, // prepend ROLE_
                enabled: true
            })
        });

        if (response.ok) {
            const data = await response.json();
            alert(`User ${data.username} created successfully with role ${data.role}`);
        } else {
            const error = await response.json();
            alert("Error creating user: " + (error.message || "Unknown error"));
        }
    } catch (err) {
        console.error("Error:", err);
        alert("Failed to create user.");
    }
});
