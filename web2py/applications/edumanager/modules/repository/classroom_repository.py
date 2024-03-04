# This file defines the StudentRepository class, which contains the methods to perform CRUD (Create, Read, Update, Delete) operations on the classrooms table in the database.

class ClassroomRepository:
    def __init__(self, db):
        self.db = db

    def create_classroom(self, classroom):
        return self.db.insert('classrooms', **classroom.__dict__)

    def get_classroom(self, id):
        return self.db.select('classrooms', where='id=$id', vars={'id': id})

    def update_classroom(self, id, classroom):
        return self.db.update('classrooms', where='id=$id', vars={'id': id}, **classroom.__dict__)

    def delete_classroom(self, id):
        return self.db.delete('classrooms', where='id=$id', vars={'id': id})
