'use strict';


// PA02 — Step 4: DOM events + validation 
console.log('PA02 Step 4 — events wired, validation active');


// --- required types ---
const studentName = 'Jadon';
let completed = 1;
const isStudent = true;
const tags = ['ml', 'web', 'famu'];
const profile = { year: 2025, major: 'CS' };
let unknown = null;
let notSet;


// operators
completed = completed + 0; // keep value
const isCS = profile.major === 'CS';
const showWelcome = isStudent && isCS;
if (showWelcome) console.log(`Welcome, ${studentName}! Assignments: ${completed}`);


// --- try to grab existing hooks ---
function ensureHooks() {
const byId = (id) => document.getElementById(id);
let status = byId('status');
let results = byId('results');
let loadBtn = byId('loadBtn');
let filter = byId('filter');
let sort = byId('sort');
let form = byId('contactForm');
let email = byId('email');


// If any core elements are missing, inject a small section so JS still works
if (!status || !results || !loadBtn || !filter || !sort) {
const main = document.querySelector('main') || document.body;
const section = document.createElement('section');
section.innerHTML = `
<h2 id="dataHeading">Public Data (Demo)</h2>
<div id="controls">
<button id="loadBtn" type="button">Load Demo Users</button>
<input id="filter" type="text" placeholder="Filter by name" aria-label="Filter users by name" />
<select id="sort" aria-label="Sort users">
<option value="az">A→Z</option>
<option value="za">Z→A</option>
</select>
</div>
<div id="status" role="status" aria-live="polite"></div>
<ul id="results"></ul>
`;
main.appendChild(section);
// re-query after injection
status = byId('status');
results = byId('results');
loadBtn = byId('loadBtn');
filter = byId('filter');
sort = byId('sort');
}


return { status, results, loadBtn, filter, sort, form, email };
}


const els = ensureHooks();


// --- App state ---
const state = { users: [], filtered: [] };


// --- Helpers ---
function setStatus(msg, type = 'info') {
// why: show user-visible feedback rather than alert
const emoji = { info: 'ℹ️', success: '✅', error: '⛔', empty: '🧐', loading: '⏳' }[type] || '';
if (els.status) els.status.textContent = `${emoji} ${msg}`;
}


function renderList(items) {
setStatus('Ready. Click “Load Demo Users” (fetch comes next).', 'info');
