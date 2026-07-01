// Utility wrapper for fetch with JWT
async function apiFetch(url, options = {}) {
    const token = localStorage.getItem("authToken");
    const headers = {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json",
        ...(options.headers || {})
    };
    const res = await fetch(url, { ...options, headers });
    if (!res.ok) throw new Error(`Request failed: ${res.status}`);
    return res.json();
}

// ================== PAYROLL ==================

// Load payrolls for manager dashboard
function loadManagerPayrolls() {
    apiFetch('http://localhost:8070/api/payroll/view')
        .then(payrolls => {
            const tbody = document.getElementById('managerPayrollTableBody');
            tbody.innerHTML = ''; // clear existing
            payrolls.forEach(p => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
          <td>${p.payrollId}</td>
          <td>${p.employeeName} (ID: ${p.employeeId})</td>
          <td>${p.payPeriod}</td>
          <td>₹${p.salary}</td>
          <td>₹${p.deductions}</td>
          <td>₹${p.netSalary}</td>
          <td class="status ${p.status === 'PAID' ? 'status-paid' : 'status-hold'}">${p.status}</td>
          <td>
            <button class="btn btn-sm btn-success" onclick="updatePayrollStatus('${p.payrollId}', 'PAID')">Pay</button>
            <button class="btn btn-sm btn-warning" onclick="updatePayrollStatus('${p.payrollId}', 'ON_HOLD')">Hold</button>
          </td>
        `;
                tbody.appendChild(tr);
            });
        })
        .catch(err => console.error("Failed to load payrolls for manager", err));
}

// Update payroll status (Pay or Hold)
async function updatePayrollStatus(payrollId, newStatus) {
    try {
        const token = localStorage.getItem("authToken");
        const res = await fetch(`http://localhost:8070/api/payroll/${payrollId}/status`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ status: newStatus })
        });

        if (res.ok) {
            alert(`Payroll ${payrollId} updated to ${newStatus}`);
            loadManagerPayrolls(); // reload table
        } else {
            let errMsg = res.statusText;
            try {
                const err = await res.json();
                if (err && err.message) errMsg = err.message;
            } catch {
                // no JSON body
            }
            alert("Error updating payroll: " + errMsg);
        }
    } catch (err) {
        console.error("Error updating payroll:", err);
        alert("Network error while updating payroll.");
    }
}

// ================== LEAVE APPROVAL ==================

function approveLeave(event) {
    event.preventDefault();

    const form = document.getElementById('approveLeaveForm');
    const leaveId = form.leaveId.value;
    const token = localStorage.getItem("authToken");

    fetch(`http://localhost:8070/api/leave/${leaveId}/approve`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to approve leave");
            return res.json();
        })
        .then(data => {
            alert(`Leave request ${data.leaveId} approved successfully!`);
            form.reset();
            loadLeaveApplications(); // refresh table
        })
        .catch(err => {
            console.error(err);
            alert("Could not approve leave request.");
        });
}

// ================== LEAVE APPLICATIONS ==================

function loadLeaveApplications() {
    const token = localStorage.getItem("authToken");

    fetch("http://localhost:8070/api/leave/view", {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to load leave applications");
            return res.json();
        })
        .then(leaves => {
            const tbody = document.getElementById("leaveApplicationsTableBody");
            tbody.innerHTML = "";
            if (leaves.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;color:#666;">No leave applications found</td></tr>`;
                return;
            }
            leaves.forEach(l => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
          <td>${l.leaveId}</td>
          <td>${l.employeeId ?? '--'}</td>
          <td>${l.leaveType}</td>
          <td>${l.fromDate}</td>
          <td>${l.toDate}</td>
          <td>${l.reason}</td>
          <td>${l.status}</td>
        `;
                tbody.appendChild(tr);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Could not load leave applications.");
        });
}

// ================== INIT ==================

document.addEventListener("DOMContentLoaded", () => {
    loadManagerPayrolls();
    loadLeaveApplications();
});
