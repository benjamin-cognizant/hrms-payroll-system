const API_BASE = "http://localhost:8070/api/recruitment";

// Helper to safely parse JSON responses
async function safeJson(res) {
    try {
        return await res.json();
    } catch {
        return null; // no JSON body
    }
}

// Helper to get Authorization header
function authHeaders() {
    const token = localStorage.getItem("authToken");
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };
}

// ================== Candidate ==================
document.getElementById("candidateForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const candidate = {
        fullName: document.getElementById("fullName").value,
        appliedRole: document.getElementById("appliedRole").value,
        experienceYears: parseInt(document.getElementById("experienceYears").value),
        interviewStage: document.getElementById("interviewStage").value,
        candidateStatus: document.getElementById("candidateStatus").value
    };
    try {
        const res = await fetch(`${API_BASE}/candidates`, {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify(candidate)
        });
        if (res.ok) {
            alert("Candidate saved successfully!");
            document.getElementById("candidateForm").reset();
        } else {
            const err = await safeJson(res);
            alert("Error saving candidate: " + (err?.error || res.statusText));
        }
    } catch (error) {
        alert("Network error: " + error.message);
    }
});

// ================== Job Requisition ==================
document.getElementById("requisitionForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const requisition = {
        jobTitle: document.getElementById("jobTitle").value,
        department: document.getElementById("department").value,
        description: document.getElementById("description").value
    };
    try {
        const res = await fetch(`${API_BASE}/job-requisitions`, {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify(requisition)
        });
        if (res.ok) {
            alert("Job requisition created!");
            document.getElementById("requisitionForm").reset();
        } else {
            const err = await safeJson(res);
            alert("Error creating requisition: " + (err?.error || res.statusText));
        }
    } catch (error) {
        alert("Network error: " + error.message);
    }
});

// ================== Interview ==================
document.getElementById("interviewForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const interview = {
        candidateId: parseInt(document.getElementById("interviewCandidateId").value),
        interviewerName: document.getElementById("interviewerName").value,
        interviewDateTime: document.getElementById("interviewDateTime").value,
        location: document.getElementById("location").value,
        interviewStatus: "SCHEDULED"
    };
    try {
        const res = await fetch(`${API_BASE}/interviews`, {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify(interview)
        });
        if (res.ok) {
            alert("Interview scheduled!");
            document.getElementById("interviewForm").reset();
        } else {
            const err = await safeJson(res);
            alert("Error scheduling interview: " + (err?.error || res.statusText));
        }
    } catch (error) {
        alert("Network error: " + error.message);
    }
});

// ================== Offer ==================
document.getElementById("offerForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const offer = {
        candidateId: parseInt(document.getElementById("offerCandidateId").value),
        positionOffered: document.getElementById("positionOffered").value,
        department: document.getElementById("offerDepartment").value,
        salaryOffered: parseFloat(document.getElementById("salaryOffered").value),
        offerDate: document.getElementById("offerDate").value,
        joiningDate: document.getElementById("joiningDate").value,
        offerStatus: document.getElementById("offerStatus").value
    };
    try {
        const res = await fetch(`${API_BASE}/offers`, {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify(offer)
        });
        if (res.ok) {
            alert("Offer rolled out!");
            document.getElementById("offerForm").reset();
        } else {
            const err = await safeJson(res);
            alert("Error rolling out offer: " + (err?.error || res.statusText));
        }
    } catch (error) {
        alert("Network error: " + error.message);
    }
});
