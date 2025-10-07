'use strict';


// PA02 — Step 5: fetch users + status handling
console.log('PA02 Step 5 — fetch flow ready');


// Types/variables 
const studentName = 'Jadon';
let completed = 1;
const isStudent = true;
const tags = ['ml', 'web', 'famu'];
const profile = { year: 2025, major: 'CS' };
let unknown = null; let notSet;


// Operators
const isCS = profile.major === 'CS';
const showWelcome = isStudent && isCS;
if (showWelcome) console.log(`Welcome, ${studentName}!`);


// DOM refs 
const els = {
status: document.getElementById('status'),
results: document.getElementById('results'),
loadBtn: document.getElementById('loadBtn'),
filter: document.getElementById('filter'),
sort: document.getElementById('sort'),
form: document.getElementById('contactForm'),
email: document.getElementById('email'),
};


// App state
const state = { users: [], filtered: [] };


// Helpers
function buildUrl() { return 'https://jsonplaceholder.typicode.com/users'; }
function setStatus(msg, type = 'info') {
const emoji = { info: 'ℹ️', success: '✅', error: '⛔', empty: '🧐', loading: '⏳' }[type] || '';
els.status.textContent = `${emoji} ${msg}`;
}
function renderList(items) {
els.results.innerHTML = '';
if (!items || items.length === 0) { setStatus('No results to show.', 'empty'); return; }
const frag = document.createDocumentFragment();
items.forEach(u => { const li = document.createElement('li'); li.textContent = `${u.name} — ${u.email}`; frag.appendChild(li); });
els.results.appendChild(frag);
setStatus(`Showing ${items.length} user(s).`, 'success');
}
function filterData(items, query = '', sort = 'az') {
const q = query.trim().toLowerCase();
let out = Array.isArray(items) ? items.slice() : [];
if (q) out = out.filter(u => (u.name || '').toLowerCase().includes(q));
out.sort((a, b) => { const an = (a.name || '').toLowerCase(); const bn = (b.name || '').toLowerCase(); return sort === 'za' ? bn.localeCompare(an) : an.localeCompare(bn); });
return out;
}
function handleError(err) { console.error(err); setStatus(`Error: ${err.message || err}`, 'error'); }


// Fetch flow
async function fetchUsers() {
try {
setStatus('Loading users…', 'loading');
const res = await fetch(buildUrl());
setStatus('Ready. Click “Load Users” to fetch data.', 'info');
