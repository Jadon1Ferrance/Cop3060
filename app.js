'use strict';


// PA02 — Step 3: DOM refs + status helpers 
console.log('PA02 Step 3 — DOM ready, helpers wired');


const studentName = 'Jadon'; // string
let completed = 1; // number 
const isStudent = true; // boolean
const tags = ['ml', 'web', 'famu']; // array
const profile = { year: 2025, major: 'CS' }; // object
let unknown = null; // null
let notSet; // undefined


// operators
completed = completed + 0; // keep value
const isCS = profile.major === 'CS'; // strict comparison
const showWelcome = isStudent && isCS; // logical
if (showWelcome) console.log(`Welcome, ${studentName}! Assignments: ${completed}`);


// --- DOM references ---
const els = {
status: document.getElementById('status'),
results: document.getElementById('results'),
loadBtn: document.getElementById('loadBtn'),
filter: document.getElementById('filter'),
sort: document.getElementById('sort'),
form: document.getElementById('contactForm'),
email: document.getElementById('email'),
};


// --- App state ---
const state = { users: [], filtered: [] };


// --- Helpers  ---
function setStatus(msg, type = 'info') {
// why: clear user feedback surfaces app state without alerts
const emoji = { info: 'ℹ️', success: '✅', error: '⛔', empty: '🧐', loading: '⏳' }[type] || '';
els.status.textContent = `${emoji} ${msg}`;
}


function renderList(items) {
els.results.innerHTML = '';
if (!items || items.length === 0) {
setStatus('No results to show.', 'empty');
return;
}
const frag = document.createDocumentFragment();
items.forEach((u) => {
const li = document.createElement('li');
li.textContent = `${u.name} — ${u.email}`;
frag.appendChild(li);
});
els.results.appendChild(frag);
setStatus(`Showing ${items.length} user(s).`, 'success');
}


function filterData(items, query = '', sort = 'az') {
const q = query.trim().toLowerCase();
let out = Array.isArray(items) ? items.slice() : [];
if (q) out = out.filter((u) => (u.name || '').toLowerCase().includes(q));
out.sort((a, b) => {
const an = (a.name || '').toLowerCase();
const bn = (b.name || '').toLowerCase();
return sort === 'za' ? bn.localeCompare(an) : an.localeCompare(bn);
});
return out;
}
setStatus('Ready. Click “Load Users”. (Fetch comes next.)', 'info');
