# This file defines the StudentRepository class, which contains the methods to perform CRUD (Create, Read, Update, Delete) operations on the subjects table in the database.

class SubjectRepository:
    def __init__(self, db):
        self.db = db

    def create_subject(self, subject):
        return self.db.insert('subjects', **subject.__dict__)

    def get_subject(self, id):
        return self.db.select('subjects', where='id=$id', vars={'id': id})

    def update_subject(self, id, subject):
        return self.db.update('subjects', where='id=$id', vars={'id': id}, **subject.__dict__)

    def delete_subject(self, id):
        return self.db.delete('subjects', where='id=$id', vars={'id': id})
