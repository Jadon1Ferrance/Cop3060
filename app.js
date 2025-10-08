'use strict';


/* Setup & Console */
console.log('PA02 JS ready — repo: https://github.com/Jadon1Ferrance/Cop3060');


/* Variables, Types, Operators */
const studentName = 'Jadon'; // string
let completedAssignments = 2; // number
const isStudent = true; // boolean
const tags = ['ml', 'web', 'famu']; // array
const profile = { year: 2025, major: 'CS' }; // object
let mystery = null; // null
let notSet; // undefined


// operators
completedAssignments = completedAssignments + 1; // arithmetic
const isCS = profile.major === 'CS'; // strict equality
const showWelcome = isStudent && isCS; // logical AND
if (showWelcome) console.log(`Welcome, ${studentName}!`);


/* DOM refs */
const els = {
status: document.getElementById('status'),
results: document.getElementById('results'),
loadBtn: document.getElementById('loadBtn'),
filter: document.getElementById('filter'),
sort: document.getElementById('sort'),
form: document.getElementById('contactForm'),
email: document.getElementById('email'),
};


/* App state */
const state = { users: [], filtered: [] };


/* Step 6 core helpers */
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


/* Fetch flow */
async function fetchUsers() {
try {
setStatus('Loading users…', 'loading');
const res = await fetch(buildUrl());
if (!res.ok) throw new Error(`HTTP ${res.status}`);
const data = await res.json();
if (!Array.isArray(data)) throw new Error('Unexpected response');


state.users = data; // array of objects
const initial = data.slice(0, Math.max(10, data.length));
state.filtered = filterData(initial, els.filter.value, els.sort.value);
renderList(state.filtered);
} catch (err) {
setStatus('Ready. Click ‘Load Users’ to fetch data.');
