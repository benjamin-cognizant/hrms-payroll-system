const API_BASE = "http://localhost:8080/api/payroll";

// === Section switching ===
function showSection(sectionId, button) {
    document.querySelectorAll(".section").forEach(sec => sec.classList.remove("active"));
    document.getElementById(`section-${sectionId}`).classList.add("active");

    document.querySelectorAll(".nav-item").forEach(btn => btn.classList.remove("active"));
    button.classList.add("active");

    const titleMap = {
        compliance: ["Compliance & Reports", "File compliance and view payroll analytics"],
        runpayroll: ["Run Payroll", "Process employee salaries for a given period"],
        payrollview: ["Payroll View", "View pending and completed payroll records"]
    };
    document.getElementById("pageTitle").textContent = titleMap[sectionId][0];
    document.getElementById("pageSubtitle").textContent = titleMap[sectionId][1];
}

// === Toast helper ===
function toast(message) {
    const box = document.getElementById("toastBox");
    box.textContent = message;
    box.style.display = "block";
    setTimeout(() => box.style.display = "none", 3000);
}

// === Load payroll summary and table ===
async function loadPayrollData() {
    try {
        const response = await fetch(API_BASE);
        const payrolls = await response.json();

        let grossSum = 0, dedSum = 0, netSum = 0;
        payrolls.forEach(p => {
            grossSum += p.grossSalary;
            dedSum += p.totalDeductions;
            netSum += p.netSalary;
        });
        document.getElementById("sumGross").textContent = `₹${grossSum.toFixed(2)}`;
        document.getElementById("sumDed").textContent = `₹${dedSum.toFixed(2)}`;
        document.getElementById("sumNet").textContent = `₹${netSum.toFixed(2)}`;
        document.getElementById("avgSal").textContent = `₹${(grossSum / payrolls.length).toFixed(2)}`;

        const tbody = document.getElementById("payrollTableBody");
        tbody.innerHTML = "";
        payrolls.forEach(p => {
            const row = document.createElement("tr");
            row.innerHTML = `
        <td>${p.payrollId}</td>
        <td>${p.employee.name} (ID: ${p.employee.id})</td>
        <td>${p.payPeriod}</td>
        <