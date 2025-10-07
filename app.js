'use strict';

// PA02 — Step 2: Variables, Types, Operators
console.log('PA02 Step 2 — variables/operators loaded');


const studentName = 'Jadon';                 // string
let completed = 0;                           // number (mutable)
const isStudent = true;                      // boolean
const tags = ['ml', 'web', 'famu'];          // array
const profile = { year: 2025, major: 'CS' }; // object
let unknown = null;                          // null
let notSet;                                  // undefined


completed = completed + 1;                   // arithmetic
const isCS = profile.major === 'CS';         // strict comparison
const showWelcome = isStudent && isCS;       // logical

if (showWelcome) {
  console.log(`Welcome, ${studentName}! Assignments: ${completed}`);
}
