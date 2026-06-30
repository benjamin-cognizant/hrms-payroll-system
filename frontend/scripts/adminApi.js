const API_BASE = "http://localhost:8080/api/employees";

// === Employee APIs ===
async function fetchAllEmployees() {
    const response = await fetch(API_BASE);
    if (!response.ok) throw new Error("Failed to fetch employees");
    return response.json();
}

async function fetchEmployeeDetails(id) {
    const response = await fetch(`${API_BASE}/${id}`);
    if (!response.ok) throw new Error(`Failed to fetch employee ${id}`);
    return response.json();
}

// === Payroll & Reports (demo using employee data) ===
async function loadEmployeeTable() {
    try {
        const employees = await fetchAllEmployees();
        const tbody = document.getElementById("employeeTableBody");
        tbody.innerHTML = "";

        employees.forEach(emp => {
            const row = document.createElement("tr");
            row.innerHTML = `
        <td>${emp.id}</td>
        <td>${emp.name}</td>
        <td>${emp.department}</td>
        <td>${emp.designation || emp.role}</td>
        <td><span class="pill pill-approved">${emp.status}</span></td>
      `;
            tbody.appendChild(row);
        });
    } catch (err) {
        console.error("Error loading employees:", err);
    }
}

async function loadPayrollSummary() {
    try {
        const employees = await fetchAllEmployees();
        document.getElementById("payrollSummary").textContent =
            `Total Employees: ${employees.length} | Processed: ${employees.length - 2}`;
        document.getElementById("totalPayroll").textContent = "₹8,640,000";
        document.getElementById("payrollStatus").textContent = "Processed";
    } catch (err) {
        console.error("Error loading payroll:", err);
    }
}

function generateReport() {
    document.getElementById("reportOutput").textContent =
        "Report generated successfully. (Demo data)";
}

// === Section switching ===
function showSection(sectionId, button) {
    document.querySelectorAll(".section").forEach(sec => sec.classList.remove("active"));
    document.getElementById(`section-${sectionId}`).classList.add("active");

    document.querySelectorAll(".nav-item").forEach(btn => btn.classList.remove("active"));
    button.classList.add("active");

    // Update page title/subtitle
    const titleMap = {
        employees: ["Employees", "Manage employee records and roles"],
        payroll: ["Payroll", "View payroll processing and compliance"],
        reports: ["Reports", "Generate HR analytics and insights"]
    };
    document.getElementById("pageTitle")?.textContent = titleMap[sectionId][0];
    document.getElementById("pageSubtitle")?.textContent = titleMap[sectionId][1];
}

// === Init on page load ===
window.addEventListener("DOMContentLoaded", () => {
    loadEmployeeTable();
    loadPayrollSummary();
});
