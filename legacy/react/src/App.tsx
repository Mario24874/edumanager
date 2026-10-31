import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './components/Home/HomePage';
import StudentPage from './components/Student/StudentPage';
import ClassroomPage from './components/Classroom/ClassroomPage';
import SubjectPage from './components/Subject/SubjectPage';
import AttendancePage from './components/Attendance/AttendancePage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/students" element={<StudentPage />} />
        <Route path="/classrooms" element={<ClassroomPage />} />
        <Route path="/subjects" element={<SubjectPage />} />
        <Route path="/attendances" element={<AttendancePage />} />
      </Routes>
    </Router>
  );
}

export default App;

