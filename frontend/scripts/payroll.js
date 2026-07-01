// ================== CONFIG ==================
const API_BASE_URL = "http://localhost:8070/api/payroll";

// ================== HELPER ==================
async function apiFetch(url, options = {}) {
  const token = localStorage.getItem("authToken");
  const response = await fetch(url, {
    ...options,
    headers: {
      ...(options.headers || {}),
      "Authorization": `Bearer ${token}`
    }
  });
  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || response.statusText);
  }
  try {
    return await response.json();
  } catch {
    return await response.text();
  }
}

// ================== SUBMIT PAYROLL ==================
function submitPayroll(event) {
  event.preventDefault();
  const id = document.getElementById('empId').value;
  const payload = {
    salary: parseFloat(document.getElementById('salary').value),
    payPeriod: document.getElementById('payPeriod').value
  };

  apiFetch(`${API_BASE_URL}/run/${id}`, {
    method: 'POST',
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  })
    .then(data => {
      alert(`Payroll updated for ${data.employeeName}, Net Salary: ₹${data.netSalary}, Status: ${data.status}`);
      addPayrollRow(data);
      loadAnalytics();
    })
    .catch(err => console.error("Error updating payroll", err));
}

// ================== ANALYTICS ==================
function loadAnalytics() {
  apiFetch(`${API_BASE_URL}/gross`)
    .then(totalGross => {
      document.getElementById('sumGross').innerText = `₹${totalGross.toFixed(2)}`;
    })
    .catch(err => console.error("Error loading gross", err));

  apiFetch(`${API_BASE_URL}/deductions`)
    .then(totalDed => {
      document.getElementById('sumDed').innerText = `₹${totalDed.toFixed(2)}`;
    })
    .catch(err => console.error("Error loading deductions", err));

  apiFetch(`${API_BASE_URL}/net`)
    .then(totalNet => {
      document.getElementById('sumNet').innerText = `₹${totalNet.toFixed(2)}`;
    })
    .catch(err => console.error("Error loading net payout", err));

  apiFetch(`${API_BASE_URL}/average`)
    .then(avgSal => {
      document.getElementById('avgSal').innerText = `₹${avgSal.toFixed(2)}`;
    })
    .catch(err => console.error("Error loading average salary", err));
}

// ================== TABLE ==================
function addPayrollRow(payroll) {
  const tbody = document.getElementById('payrollTableBody');
  const tr = document.createElement('tr');
  tr.innerHTML = `
    <td>${payroll.payrollId}</td>
    <td>${payroll.employeeName} (ID: ${payroll.employeeId})</td>
    <td>${payroll.payPeriod}</td>
    <td>${payroll.salary}</td>
    <td>${payroll.deductions}</td>
    <td>${payroll.netSalary}</td>
    <td class="status ${payroll.status === 'PAID' ? 'status-paid' : 'status-hold'}">${payroll.status}</td>
    <td>${payroll.status === 'ON_HOLD' ? 
      '<button class="btn btn-outline" onclick="markAsPaid('+payroll.payrollId+', this)">Mark as Paid</button>' 
      : '-'}</td>
  `;
  tbody.appendChild(tr);
}

// ================== MARK AS PAID ==================
function markAsPaid(id, btn) {
  apiFetch(`${API_BASE_URL}/${id}/approve`, { method: 'PUT' })
    .then(data => {
      const statusCell = btn.parentElement.previousElementSibling;
      statusCell.className = 'status status-paid';
      statusCell.innerText = 'PAID';
      btn.remove();
      loadAnalytics();
    })
    .catch(err => console.error("Error marking as paid", err));
}

// ================== COMPLIANCE REPORT ==================
function fileComplianceReport() {
  const period = document.getElementById('reportPeriod').value;
  apiFetch(`${API_BASE_URL}/compliance?period=${period}`, { method: 'POST' })
    .then(msg => alert(msg))
    .catch(err => console.error("Error filing compliance report", err));
}

// ================== LOAD PAYROLLS ==================
function loadPayrolls() {
  apiFetch(`${API_BASE_URL}/view`)
    .then(payrolls => {
      const tbody = document.getElementById('payrollTableBody');
      tbody.innerHTML = '';
      payrolls.forEach(addPayrollRow);
    })
    .catch(err => console.error("Failed to load payrolls", err));
}

// ================== INIT ==================
document.addEventListener("DOMContentLoaded", () => {
  loadPayrolls();
  loadAnalytics();
});
