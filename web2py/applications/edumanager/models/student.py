# This file corresponds to the student data model.

class Student:
    def __init__(self, id, first_name, last_name, email, birth_date, grade):
        self.id = id
        self.first_name = first_name
        self.last_name = last_name
        self.email = email
        self.birth_date = birth_date
        self.grade = grade
