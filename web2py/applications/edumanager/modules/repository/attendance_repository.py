# This file defines the StudentRepository class, which contains the methods to perform CRUD (Create, Read, Update, Delete) operations on the attendances table in the database.

class AttendanceRepository:
    def __init__(self, db):
        self.db = db

    def create_attendance(self, attendance):
        return self.db.insert('attendances', **attendance.__dict__)

    def get_attendance(self, id):
        return self.db.select('attendances', where='id=$id', vars={'id': id})

    def update_attendance(self, id, attendance):
        return self.db.update('attendances', where='id=$id', vars={'id': id}, **attendance.__dict__)

    def delete_attendance(self, id):
        return self.db.delete('attendances', where='id=$id', vars={'id': id})
