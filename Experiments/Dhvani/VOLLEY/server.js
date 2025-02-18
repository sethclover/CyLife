const express = require('express');
const app = express();
const bodyParser = require('body-parser');

app.use(bodyParser.json());

const students = [
    {email: 'dhvani', password: '321'},
    { email: 'student1@example.com', password: 'password123' },
    { email: 'student2@example.com', password: 'password456' },
    { email: 'student3@example.com', password: 'password789' },
    { email: 'student4@example.com', password: 'passwordABC' },
    { email: 'student5@example.com', password: 'passwordXYZ' },
    { email: 'student6@example.com', password: 'password321' },
];

app.post('/students', (req, res) => {
    const { email, password } = req.body;
    const student = students.find(s => s.email === email);

    if (!student) {
        return res.json({ success: false, message: "Email does not exist." });
    }
    if (student.password === password) {
        return res.json({ success: true, studentName: student.email });
    } else {
        return res.json({ success: false, message: "Incorrect password." });
    }
});

app.listen(3000, () => {
    console.log('Server is running on port 3000');
});


// INSTRUCTIONS TO GET THE SERVER UP
// RUN THE FOLLOWING COMMANDS
// npm init -y
// npm install express
// node server.js