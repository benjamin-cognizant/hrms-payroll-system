const titles = {
  profile:    ['My Profile',          'Your personal details and employment information'],
  attendance: ['Attendance & Leave',  'Mark attendance, apply for leave and submit timesheets'],
  payslip:    ['My Payslips',         'View and download your monthly payslips'],
  appraisal:  ['My Appraisal',        'Track your goals and submit your self review']
};

function showSection(name, el) {
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  document.getElementById('section-' + name).classList.add('active');
  el.classList.add('active');
  document.getElementById('pageTitle').textContent    = titles[name][0];
  document.getElementById('pageSubtitle').textContent = titles[name][1];
}

let toastTimer;
function toast(msg) {
  const box = document.getElementById('toastBox');
  box.textContent = msg;
  box.style.display = 'block';
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => { box.style.display = 'none'; }, 3000);
}
