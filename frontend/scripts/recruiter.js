// API Base URL - Change this if backend runs on different port
const API_BASE_URL = 'http://localhost:8080/api/recruitment';

// Global variables
let editingCandidateId = null;
let editingRequisitionId = null;
let editingInterviewId = null;
let editingOfferId = null;
let candidatesCache = [];
let currentDetailOfferId = null;

// ========== UTILITY FUNCTIONS ==========
function showToast(message) {
    const toast = document.getElementById('toastBox');
    if (!toast) return;
    toast.textContent = message;
    toast.style.display = 'block';
    setTimeout(() => { toast.style.display = 'none'; }, 3000);
}

function formatStatus(status) {
    return status.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
}

function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: 'numeric' });
}

function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return 'N/A';
    const date = new Date(dateTimeStr);
    return date.toLocaleString('en-IN', { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
}

function formatCurrency(amount) {
    if (!amount) return '0.00';
    return amount.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

// ========== CHECK BACKEND ==========
async function checkBackendConnection() {
    try {
        const response = await fetch(`${API_BASE_URL}/health`);
        const data = await response.json();
        console.log('✅ Backend connection successful:', data);
        return true;
    } catch (error) {
        console.error('❌ Backend connection failed:', error);
        showToast('⚠️ Cannot connect to backend. Please ensure the Spring Boot application is running on port 8080.');
        return false;
    }
}

// ========== DASHBOARD ==========
async function loadDashboard() {
    try {
        const response = await fetch(`${API_BASE_URL}/dashboard`);
        const data = await response.json();
        document.getElementById('candidateCount').textContent = data.candidateCount || 0;
        document.getElementById('requisitionCount').textContent = data.requisitionCount || 0;
        document.getElementById('interviewCount').textContent = data.interviewCount || 0;
        document.getElementById('offerCount').textContent = data.offerCount || 0;
    } catch (error) {
        console.error('Error loading dashboard:', error);
        showToast('Error loading dashboard data');
    }
}

// ========== CANDIDATES ==========
async function loadCandidates() {
    try {
        const response = await fetch(`${API_BASE_URL}/candidates`);
        const candidates = await response.json();
        candidatesCache = candidates;

        const tbody = document.getElementById('candidateTableBody');
        if (!tbody) return;
        if (candidates.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center">No candidates found</td></tr>';
            return;
        }

        tbody.innerHTML = candidates.map(candidate => `
            <tr>
                <td>${candidate.candidateId}</td>
                <td>${candidate.fullName}</td>
                <td>${candidate.appliedRole}</td>
                <td>${candidate.experienceYears} years</td>
                <td>${candidate.interviewStage}</td>
                <td><span class="pill pill-${candidate.candidateStatus.toLowerCase()}">${formatStatus(candidate.candidateStatus)}</span></td>
                <td>
                    <button class="btn-action" onclick="editCandidate(${candidate.candidateId})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn-action delete" onclick="deleteCandidate(${candidate.candidateId})" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading candidates:', error);
        showToast('Error loading candidates');
    }
}

function showAddCandidateForm() {
    editingCandidateId = null;
    document.getElementById('candidateFormTitle').textContent = 'Add New Candidate';
    document.getElementById('candidateForm').reset();
    document.getElementById('candidateId').value = '';
    document.getElementById('candidateFormCard').style.display = 'block';
    document.getElementById('candidateFormCard').scrollIntoView({ behavior: 'smooth' });
}

function cancelCandidateForm() {
    document.getElementById('candidateFormCard').style.display = 'none';
    document.getElementById('candidateForm').reset();
    editingCandidateId = null;
}

async function editCandidate(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/candidates/${id}`);
        const candidate = await response.json();

        editingCandidateId = id;
        document.getElementById('candidateFormTitle').textContent = 'Edit Candidate';
        document.getElementById('candidateId').value = candidate.candidateId;
        document.getElementById('fullName').value = candidate.fullName;
        document.getElementById('appliedRole').value = candidate.appliedRole;
        document.getElementById('experienceYears').value = candidate.experienceYears;
        document.getElementById('interviewStage').value = candidate.interviewStage;
        document.getElementById('candidateStatus').value = candidate.candidateStatus;

        document.getElementById('candidateFormCard').style.display = 'block';
        document.getElementById('candidateFormCard').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        console.error('Error loading candidate:', error);
        showToast('Error loading candidate details');
    }
}

async function deleteCandidate(id) {
    if (!confirm('Are you sure you want to delete this candidate?')) return;
    try {
        const response = await fetch(`${API_BASE_URL}/candidates/${id}`, { method: 'DELETE' });
        if (response.ok) { showToast('Candidate deleted successfully'); loadCandidates(); }
        else showToast('Error deleting candidate');
    } catch (error) { console.error('Error deleting candidate:', error); showToast('Error deleting candidate'); }
}

async function saveCandidateForm(e) {
    e.preventDefault();
    const candidateData = {
        fullName: document.getElementById('fullName').value,
        appliedRole: document.getElementById('appliedRole').value,
        experienceYears: parseInt(document.getElementById('experienceYears').value),
        interviewStage: document.getElementById('interviewStage').value,
        candidateStatus: document.getElementById('candidateStatus').value
    };

    try {
        let response;
        if (editingCandidateId) {
            response = await fetch(`${API_BASE_URL}/candidates/${editingCandidateId}`, {
                method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(candidateData)
            });
        } else {
            response = await fetch(`${API_BASE_URL}/candidates`, {
                method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(candidateData)
            });
        }

        if (response.ok) {
            showToast(editingCandidateId ? 'Candidate updated successfully' : 'Candidate added successfully');
            cancelCandidateForm();
            loadCandidates();
        } else {
            const error = await response.json();
            showToast(error.error || 'Error saving candidate');
        }
    } catch (error) { console.error('Error saving candidate:', error); showToast('Error saving candidate'); }
}

// Load roles from Job Requisitions for the candidate "Applied Role" dropdown
async function loadRolesForCandidateDropdown() {
    try {
        const response = await fetch(`${API_BASE_URL}/job-requisitions`);
        const requisitions = await response.json();
        const roleSelect = document.getElementById('appliedRole');
        if (!roleSelect || roleSelect.tagName !== 'SELECT') return;
        const openRoles = requisitions.filter(r => r.status === 'OPEN' || r.status === 'IN_PROGRESS');
        const options = openRoles.map(r => `<option value="${r.jobTitle}">${r.jobTitle} - ${r.department}</option>`).join('');
        roleSelect.innerHTML = '<option value="">Select Role</option>' + options;
    } catch (error) { console.error('Error loading roles for dropdown:', error); }
}

// ========== JOB REQUISITIONS ==========
async function loadRequisitions() {
    try {
        const response = await fetch(`${API_BASE_URL}/job-requisitions`);
        const requisitions = await response.json();

        const tbody = document.getElementById('requisitionTableBody');
        if (!tbody) return;
        if (requisitions.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">No requisitions found</td></tr>';
            return;
        }

        tbody.innerHTML = requisitions.map(req => `
            <tr>
                <td>${req.requisitionId}</td>
                <td>${req.jobTitle}</td>
                <td>${req.department}</td>
                <td>${req.numberOfPositions}</td>
                <td><span class="pill pill-${req.priority.toLowerCase()}">${req.priority}</span></td>
                <td><span class="pill pill-${req.status.toLowerCase()}">${formatStatus(req.status)}</span></td>
                <td>${formatDate(req.requisitionDate)}</td>
                <td>
                    <button class="btn-action" onclick="editRequisition(${req.requisitionId})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn-action delete" onclick="deleteRequisition(${req.requisitionId})" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) { console.error('Error loading requisitions:', error); showToast('Error loading requisitions'); }
}

function showAddRequisitionForm() {
    editingRequisitionId = null;
    document.getElementById('requisitionFormTitle').textContent = 'Create Job Requisition';
    document.getElementById('requisitionForm').reset();
    document.getElementById('requisitionId').value = '';
    document.getElementById('requisitionFormCard').style.display = 'block';
    document.getElementById('requisitionFormCard').scrollIntoView({ behavior: 'smooth' });
}

function cancelRequisitionForm() {
    document.getElementById('requisitionFormCard').style.display = 'none';
    document.getElementById('requisitionForm').reset();
    editingRequisitionId = null;
}

async function editRequisition(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/job-requisitions/${id}`);
        const req = await response.json();

        editingRequisitionId = id;
        document.getElementById('requisitionFormTitle').textContent = 'Edit Job Requisition';
        document.getElementById('requisitionId').value = req.requisitionId;
        document.getElementById('jobTitle').value = req.jobTitle;
        document.getElementById('department').value = req.department;
        document.getElementById('numberOfPositions').value = req.numberOfPositions;
        document.getElementById('priority').value = req.priority;
        document.getElementById('status').value = req.status;
        document.getElementById('requisitionDate').value = req.requisitionDate;
        document.getElementById('description').value = req.description || '';

        document.getElementById('requisitionFormCard').style.display = 'block';
        document.getElementById('requisitionFormCard').scrollIntoView({ behavior: 'smooth' });
    } catch (error) { console.error('Error loading requisition:', error); showToast('Error loading requisition details'); }
}

async function deleteRequisition(id) {
    if (!confirm('Are you sure you want to delete this job requisition?')) return;
    try {
        const response = await fetch(`${API_BASE_URL}/job-requisitions/${id}`, { method: 'DELETE' });
        if (response.ok) { showToast('Job requisition deleted successfully'); loadRequisitions(); }
        else showToast('Error deleting job requisition');
    } catch (error) { console.error('Error deleting requisition:', error); showToast('Error deleting job requisition'); }
}

async function saveRequisitionForm(e) {
    e.preventDefault();
    const reqData = {
        jobTitle: document.getElementById('jobTitle').value,
        department: document.getElementById('department').value,
        numberOfPositions: parseInt(document.getElementById('numberOfPositions').value),
        priority: document.getElementById('priority').value,
        status: document.getElementById('status').value,
        requisitionDate: document.getElementById('requisitionDate').value,
        description: document.getElementById('description').value
    };

    try {
        let response;
        if (editingRequisitionId) {
            response = await fetch(`${API_BASE_URL}/job-requisitions/${editingRequisitionId}`, {
                method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(reqData)
            });
        } else {
            response = await fetch(`${API_BASE_URL}/job-requisitions`, {
                method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(reqData)
            });
        }

        if (response.ok) {
            showToast(editingRequisitionId ? 'Requisition updated successfully' : 'Requisition created successfully');
            cancelRequisitionForm();
            loadRequisitions();
        } else {
            const error = await response.json();
            showToast(error.error || 'Error saving requisition');
        }
    } catch (error) { console.error('Error saving requisition:', error); showToast('Error saving requisition'); }
}

// ========== INTERVIEWS ==========
async function loadCandidatesForDropdown() {
    try {
        const response = await fetch(`${API_BASE_URL}/candidates`);
        const candidates = await response.json();
        const interviewSelect = document.getElementById('interviewCandidateId');
        if (interviewSelect) {
            const options = candidates.map(c => `<option value="${c.candidateId}">${c.fullName} - ${c.appliedRole}</option>`).join('');
            interviewSelect.innerHTML = '<option value="">Select Candidate</option>' + options;
        }
    } catch (error) { console.error('Error loading candidates for dropdown:', error); }
}

async function loadInterviews() {
    try {
        const response = await fetch(`${API_BASE_URL}/interviews`);
        const interviews = await response.json();

        const candidatesResponse = await fetch(`${API_BASE_URL}/candidates`);
        const candidates = await candidatesResponse.json();
        const candidateMap = {};
        candidates.forEach(c => candidateMap[c.candidateId] = c.fullName);

        const tbody = document.getElementById('interviewTableBody');
        if (!tbody) return;
        if (interviews.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">No interviews scheduled</td></tr>';
            return;
        }

        tbody.innerHTML = interviews.map(interview => `
            <tr>
                <td>${interview.interviewId}</td>
                <td>${candidateMap[interview.candidateId] || 'N/A'}</td>
                <td>${interview.interviewerName}</td>
                <td>${formatDateTime(interview.interviewDateTime)}</td>
                <td>${interview.interviewRound}</td>
                <td>${interview.interviewMode}</td>
                <td><span class="pill pill-${interview.interviewStatus.toLowerCase()}">${formatStatus(interview.interviewStatus)}</span></td>
                <td>
                    <button class="btn-action" onclick="editInterview(${interview.interviewId})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn-action delete" onclick="deleteInterview(${interview.interviewId})" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) { console.error('Error loading interviews:', error); showToast('Error loading interviews'); }
}

function showScheduleInterviewForm() {
    editingInterviewId = null;
    document.getElementById('interviewFormTitle').textContent = 'Schedule Interview';
    document.getElementById('interviewForm').reset();
    document.getElementById('interviewId').value = '';
    document.getElementById('interviewFormCard').style.display = 'block';
    document.getElementById('interviewFormCard').scrollIntoView({ behavior: 'smooth' });
}

function cancelInterviewForm() {
    document.getElementById('interviewFormCard').style.display = 'none';
    document.getElementById('interviewForm').reset();
    editingInterviewId = null;
}

async function editInterview(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/interviews/${id}`);
        const interview = await response.json();

        editingInterviewId = id;
        document.getElementById('interviewFormTitle').textContent = 'Edit Interview';
        document.getElementById('interviewId').value = interview.interviewId;
        document.getElementById('interviewCandidateId').value = interview.candidateId;
        document.getElementById('interviewerName').value = interview.interviewerName;
        document.getElementById('interviewDateTime').value = interview.interviewDateTime;
        document.getElementById('interviewRound').value = interview.interviewRound;
        document.getElementById('interviewMode').value = interview.interviewMode;
        document.getElementById('interviewStatus').value = interview.interviewStatus;
        document.getElementById('location').value = interview.location || '';
        document.getElementById('meetingLink').value = interview.meetingLink || '';
        document.getElementById('interviewRemarks').value = interview.remarks || '';

        document.getElementById('interviewFormCard').style.display = 'block';
        document.getElementById('interviewFormCard').scrollIntoView({ behavior: 'smooth' });
    } catch (error) { console.error('Error loading interview:', error); showToast('Error loading interview details'); }
}

async function deleteInterview(id) {
    if (!confirm('Are you sure you want to delete this interview?')) return;
    try {
        const response = await fetch(`${API_BASE_URL}/interviews/${id}`, { method: 'DELETE' });
        if (response.ok) { showToast('Interview deleted successfully'); loadInterviews(); }
        else showToast('Error deleting interview');
    } catch (error) { console.error('Error deleting interview:', error); showToast('Error deleting interview'); }
}

async function saveInterviewForm(e) {
    e.preventDefault();
    const interviewData = {
        candidateId: parseInt(document.getElementById('interviewCandidateId').value),
        interviewerName: document.getElementById('interviewerName').value,
        interviewDateTime: document.getElementById('interviewDateTime').value,
        interviewRound: document.getElementById('interviewRound').value,
        interviewMode: document.getElementById('interviewMode').value,
        interviewStatus: document.getElementById('interviewStatus').value,
        location: document.getElementById('location').value,
        meetingLink: document.getElementById('meetingLink').value,
        remarks: document.getElementById('interviewRemarks').value
    };

    try {
        let response;
        if (editingInterviewId) {
            response = await fetch(`${API_BASE_URL}/interviews/${editingInterviewId}`, {
                method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(interviewData)
            });
        } else {
            response = await fetch(`${API_BASE_URL}/interviews`, {
                method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(interviewData)
            });
        }

        if (response.ok) {
            showToast(editingInterviewId ? 'Interview updated successfully' : 'Interview scheduled successfully');
            cancelInterviewForm();
            loadInterviews();
        } else {
            const error = await response.json();
            showToast(error.error || 'Error scheduling interview');
        }
    } catch (error) { console.error('Error saving interview:', error); showToast('Error scheduling interview'); }
}

// ========== OFFERS ==========
async function loadCandidatesForOfferDropdown() {
    try {
        const response = await fetch(`${API_BASE_URL}/candidates`);
        const candidates = await response.json();

        const interviewsResponse = await fetch(`${API_BASE_URL}/interviews`);
        const interviews = await interviewsResponse.json();

        const completedCandidateIds = new Set(
            interviews.filter(i => i.interviewStatus === 'COMPLETED').map(i => i.candidateId)
        );

        const eligibleCandidates = candidates.filter(c =>
            completedCandidateIds.has(c.candidateId) &&
            c.candidateStatus !== 'OFFERED' &&
            c.candidateStatus !== 'HIRED'
        );

        const offerSelect = document.getElementById('offerCandidateId');
        if (offerSelect) {
            const options = eligibleCandidates.map(c =>
                `<option value="${c.candidateId}">${c.fullName} - ${c.appliedRole}</option>`
            ).join('');
            offerSelect.innerHTML = '<option value="">Select Candidate</option>' + options;
        }
    } catch (error) { console.error('Error loading candidates for offer dropdown:', error); }
}

async function loadOffers() {
    try {
        const response = await fetch(`${API_BASE_URL}/offers`);
        const offers = await response.json();

        const candidatesResponse = await fetch(`${API_BASE_URL}/candidates`);
        const candidates = await candidatesResponse.json();
        const candidateMap = {};
        candidates.forEach(c => candidateMap[c.candidateId] = c.fullName);

        const tbody = document.getElementById('offerTableBody');
        if (!tbody) return;
        if (offers.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" class="text-center">No offers found</td></tr>';
            return;
        }

        tbody.innerHTML = offers.map(offer => `
            <tr>
                <td>${offer.offerId}</td>
                <td>${candidateMap[offer.candidateId] || 'N/A'}</td>
                <td>${offer.positionOffered}</td>
                <td>${offer.department}</td>
                <td>₹${formatCurrency(offer.salaryOffered)}</td>
                <td>${formatDate(offer.offerDate)}</td>
                <td>${formatDate(offer.joiningDate)}</td>
                <td><span class="pill pill-${offer.offerStatus.toLowerCase()}">${formatStatus(offer.offerStatus)}</span></td>
                <td>
                    <button class="btn-action" onclick="viewOfferDetail(${offer.offerId})" title="View">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn-action" onclick="editOffer(${offer.offerId})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn-action delete" onclick="deleteOffer(${offer.offerId})" title="Delete">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) { console.error('Error loading offers:', error); showToast('Error loading offers'); }
}

function showRolloutOfferForm() {
    editingOfferId = null;
    document.getElementById('offerFormTitle').textContent = 'Rollout Offer Letter';
    document.getElementById('offerForm').reset();
    document.getElementById('offerId').value = '';
    document.getElementById('offerFormCard').style.display = 'block';
    document.getElementById('offerFormCard').scrollIntoView({ behavior: 'smooth' });
}

function cancelOfferForm() {
    document.getElementById('offerFormCard').style.display = 'none';
    document.getElementById('offerForm').reset();
    editingOfferId = null;
}

async function editOffer(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/offers/${id}`);
        const offer = await response.json();

        editingOfferId = id;
        document.getElementById('offerFormTitle').textContent = 'Edit Offer';
        document.getElementById('offerId').value = offer.offerId;
        document.getElementById('offerCandidateId').value = offer.candidateId;
        document.getElementById('positionOffered').value = offer.positionOffered;
        document.getElementById('offerDepartment').value = offer.department;
        document.getElementById('salaryOffered').value = offer.salaryOffered;
        document.getElementById('offerStatus').value = offer.offerStatus;
        document.getElementById('offerDate').value = offer.offerDate;
        document.getElementById('joiningDate').value = offer.joiningDate;
        document.getElementById('additionalBenefits').value = offer.additionalBenefits || '';
        document.getElementById('offerRemarks').value = offer.remarks || '';

        document.getElementById('offerFormCard').style.display = 'block';
        document.getElementById('offerFormCard').scrollIntoView({ behavior: 'smooth' });
    } catch (error) { console.error('Error loading offer:', error); showToast('Error loading offer details'); }
}

async function deleteOffer(id) {
    if (!confirm('Are you sure you want to delete this offer?')) return;
    try {
        const response = await fetch(`${API_BASE_URL}/offers/${id}`, { method: 'DELETE' });
        if (response.ok) { showToast('Offer deleted successfully'); loadOffers(); }
        else showToast('Error deleting offer');
    } catch (error) { console.error('Error deleting offer:', error); showToast('Error deleting offer'); }
}

async function viewOfferDetail(offerId) {
    try {
        currentDetailOfferId = offerId;
        const offerResponse = await fetch(`${API_BASE_URL}/offers/${offerId}`);
        const offer = await offerResponse.json();

        const candidateResponse = await fetch(`${API_BASE_URL}/candidates/${offer.candidateId}`);
        const candidate = await candidateResponse.json();

        document.getElementById('detail-candidateName').textContent = candidate.fullName || 'N/A';
        document.getElementById('detail-candidateEmail').textContent = candidate.email || 'N/A';
        document.getElementById('detail-candidatePhone').textContent = candidate.phone || 'N/A';
        document.getElementById('detail-appliedRole').textContent = candidate.appliedRole || 'N/A';
        document.getElementById('detail-experience').textContent = candidate.experienceYears ? candidate.experienceYears + ' years' : 'N/A';
        document.getElementById('detail-candidateStatus').innerHTML =
            `<span class="pill pill-${candidate.candidateStatus.toLowerCase()}">${formatStatus(candidate.candidateStatus)}</span>`;

        document.getElementById('detail-offerId').textContent = offer.offerId;
        document.getElementById('detail-position').textContent = offer.positionOffered;
        document.getElementById('detail-department').textContent = offer.department;
        document.getElementById('detail-salary').textContent = '₹' + formatCurrency(offer.salaryOffered);
        document.getElementById('detail-offerDate').textContent = formatDate(offer.offerDate);
        document.getElementById('detail-joiningDate').textContent = formatDate(offer.joiningDate);
        document.getElementById('detail-offerStatus').innerHTML =
            `<span class="pill pill-${offer.offerStatus.toLowerCase()}">${formatStatus(offer.offerStatus)}</span>`;
        document.getElementById('detail-benefits').textContent = offer.additionalBenefits || 'None specified';
        document.getElementById('detail-remarks').textContent = offer.remarks || 'No remarks';

        document.getElementById('offerDetailCard').style.display = 'block';
        document.getElementById('offerDetailCard').scrollIntoView({ behavior: 'smooth' });
    } catch (error) { console.error('Error loading offer details:', error); showToast('Error loading offer details'); }
}

function closeOfferDetail() {
    document.getElementById('offerDetailCard').style.display = 'none';
    currentDetailOfferId = null;
}

function editOfferFromDetail() {
    if (currentDetailOfferId) { closeOfferDetail(); editOffer(currentDetailOfferId); }
}

async function saveOfferForm(e) {
    e.preventDefault();
    const offerData = {
        candidateId: parseInt(document.getElementById('offerCandidateId').value),
        positionOffered: document.getElementById('positionOffered').value,
        department: document.getElementById('offerDepartment').value,
        salaryOffered: parseFloat(document.getElementById('salaryOffered').value),
        offerStatus: document.getElementById('offerStatus').value,
        offerDate: document.getElementById('offerDate').value,
        joiningDate: document.getElementById('joiningDate').value,
        additionalBenefits: document.getElementById('additionalBenefits').value,
        remarks: document.getElementById('offerRemarks').value
    };

    try {
        let response;
        if (editingOfferId) {
            response = await fetch(`${API_BASE_URL}/offers/${editingOfferId}`, {
                method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(offerData)
            });
        } else {
            response = await fetch(`${API_BASE_URL}/offers`, {
                method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(offerData)
            });
        }

        if (response.ok) {
            showToast(editingOfferId ? 'Offer updated successfully' : 'Offer rolled out successfully');
            cancelOfferForm();
            loadOffers();
            loadCandidatesForOfferDropdown();
        } else {
            const error = await response.json();
            showToast(error.error || 'Error rolling out offer');
        }
    } catch (error) { console.error('Error saving offer:', error); showToast('Error rolling out offer'); }
}

// ========== SET MIN DATE ON DATE INPUTS ==========
function setMinDatesOnForms() {
    const today = new Date().toISOString().split('T')[0];
    const now = new Date();
    const nowLocal = now.getFullYear() + '-' +
        String(now.getMonth() + 1).padStart(2, '0') + '-' +
        String(now.getDate()).padStart(2, '0') + 'T' +
        String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0');

    const requisitionDate = document.getElementById('requisitionDate');
    if (requisitionDate) requisitionDate.setAttribute('min', today);

    const offerDate = document.getElementById('offerDate');
    if (offerDate) offerDate.setAttribute('min', today);

    const joiningDate = document.getElementById('joiningDate');
    if (joiningDate) joiningDate.setAttribute('min', today);

    const interviewDateTime = document.getElementById('interviewDateTime');
    if (interviewDateTime) interviewDateTime.setAttribute('min', nowLocal);
}

// ========== AUTH (placeholder) ==========
function handleLogin() { console.log('Login clicked'); }
function handleLogout() { console.log('Logout clicked'); }

// ========== PAGE INITIALIZATION ==========
document.addEventListener('DOMContentLoaded', async function() {
    console.log('🚀 HRMS Page Loading...');
    const isConnected = await checkBackendConnection();
    if (!isConnected) return;

    // Auto-detect which page we're on and load appropriate data
    if (document.getElementById('candidateCount')) {
        // Dashboard page
        loadDashboard();
    }
    if (document.getElementById('candidateTableBody')) {
        // Candidates page
        loadCandidates();
        loadRolesForCandidateDropdown();
        const candidateForm = document.getElementById('candidateForm');
        if (candidateForm) candidateForm.addEventListener('submit', saveCandidateForm);
    }
    if (document.getElementById('requisitionTableBody')) {
        // Requisitions page
        loadRequisitions();
        const requisitionForm = document.getElementById('requisitionForm');
        if (requisitionForm) requisitionForm.addEventListener('submit', saveRequisitionForm);
    }
    if (document.getElementById('interviewTableBody')) {
        // Interviews page
        loadInterviews();
        loadCandidatesForDropdown();
        const interviewForm = document.getElementById('interviewForm');
        if (interviewForm) interviewForm.addEventListener('submit', saveInterviewForm);
    }
    if (document.getElementById('offerTableBody')) {
        // Offers page
        loadOffers();
        loadCandidatesForOfferDropdown();
        const offerForm = document.getElementById('offerForm');
        if (offerForm) offerForm.addEventListener('submit', saveOfferForm);
    }

    // Set min dates
    setMinDatesOnForms();
});
