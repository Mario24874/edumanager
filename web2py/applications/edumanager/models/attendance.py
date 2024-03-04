# This file corresponds to the attendance data model.

class Attendance:
    def __init__(self, id, student_id, classroom_id, subject_id, date, status):
        self.id = id
        self.student_id = student_id
        self.classroom_id = classroom_id
        self.subject_id = subject_id
        self.date = date
        self.status = status
