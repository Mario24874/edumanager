# This file defines the StudentRepository class, which contains the methods to perform CRUD (Create, Read, Update, Delete) operations on the students table in the database.

class StudentRepository:
    def __init__(self, db):
        self.db = db

    def create_student(self, student):
        return self.db.insert('students', **student.__dict__)

    def get_student(self, id):
        return self.db.select('students', where='id=$id', vars={'id': id})

    def update_student(self, id, student):
        return self.db.update('students', where='id=$id', vars={'id': id}, **student.__dict__)

    def delete_student(self, id):
        return self.db.delete('students', where='id=$id', vars={'id': id})
