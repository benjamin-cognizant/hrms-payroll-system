// ================== CONFIG ==================
const API_BASE_URL = "http://localhost:8070/api/employees";

// ================== TITLES ==================
const titles = {
  profile:    ['My Profile',          'Your personal details and employment information'],
  attendance: ['Attendance & Leave',  'Mark attendance, apply for leave and submit timesheets'],
  payslip:    ['My Payslips',         'View and download your monthly payslips'],
  appraisal:  ['My Appraisal',        'Track your goals and submit your self review']
};

// ================== NAVIGATION ==================
function showSection(name, el) {
  // Hide all sections and deactivate all nav items
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));

  // Show selected section and activate clicked nav item
  const section = document.getElementById('section-' + name);
  if (section) section.classList.add('active');
  el.classList.add('active');

  // Update page title/subtitle
  if (titles[name]) {
    document.getElementById('pageTitle').textContent    = titles[name][0];
    document.getElementById('pageSubtitle').textContent = titles[name][1];
  }
}

// ================== LEAVE REQUEST ==================
function submitLeaveRequest(event) {
  event.preventDefault(); // prevent page reload

  const form = document.getElementById('leaveForm');
  const payload = {
    leaveType: form.leaveType.value,
    fromDate: form.fromDate.value,
    toDate: form.toDate.value,
    reason: form.reason.value
  };

  const token = localStorage.getItem("authToken");

  fetch('http://localhost:8070/api/leave/apply', {
    method: 'POST',
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(payload)
  })
    .then(res => {
      if (!res.ok) throw new Error("Failed to submit leave request");
      return res.json();
    })
    .then(data => {
      toast(`Leave application submitted successfully. ID: ${data.leaveId}`);
      form.reset();
    })
    .catch(err => {
      console.error(err);
      toast("Could not submit leave request.");
    });
}

function submitTimesheet(event) {
  event.preventDefault(); // prevent page reload

  const form = document.getElementById('timesheetForm');
  const payload = {
    workDate: form.workDate.value,
    hoursWorked: form.hoursWorked.value,
    description: form.description.value
  };

  const token = localStorage.getItem("authToken");

  fetch('http://localhost:8070/api/leave/submitTimesheet', {
    method: 'POST',
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(payload)
  })
    .then(res => {
      if (!res.ok) throw new Error("Failed to submit timesheet");
      return res.json();
    })
    .then(data => {
      toast(`Timesheet submitted successfully. ID: ${data.timesheetId}`);
      form.reset();
    })
    .catch(err => {
      console.error(err);
      toast("Could not submit timesheet.");
    });
}

// ================== EMPLOYEE DETAILS ==================
function loadEmployeeDetails(empId) {
  const token = localStorage.getItem("authToken");

  fetch(`${API_BASE_URL}/${empId}`, {
    headers: {
      "Authorization": `Bearer ${token}`
    }
  })
    .then(res => {
      if (!res.ok) {
        return res.text().then(text => { 
          throw new Error(text || res.statusText); 
        });
      }
      return res.json();
    })
    .then(emp => {
      console.log("Employee details loaded:", emp.id);
      document.getElementById('empIdField').textContent     = emp.id ?? '--';
      document.getElementById('empNameField').textContent   = emp.name ?? '--';
      document.getElementById('empDeptField').textContent   = emp.department ?? '--';
      document.getElementById('empRoleField').textContent   = emp.role ?? '--';
      document.getElementById('empDesigField').textContent  = emp.designation ?? '--';
      document.getElementById('empStatusField').textContent = emp.status ?? '--';
    })
    .catch(err => {
      console.error(err);
      toast("Could not load employee details.");
    });
}

// ================== TOAST ==================
let toastTimer;
function toast(msg) {
  const box = document.getElementById('toastBox');
  box.textContent = msg;
  box.style.display = 'block';
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => { box.style.display = 'none'; }, 3000);
}

// ================== INIT ==================
document.addEventListener("DOMContentLoaded", () => {
  // Example: load employee with ID 3
  loadEmployeeDetails(3);
});
