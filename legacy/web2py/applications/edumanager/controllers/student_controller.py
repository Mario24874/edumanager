# This file defines the student_controller.py driver, which contains the functions to perform CRUD (Create, Read, Update, Delete) operations on the students table in the database.

def index():
    students = db(db.students).select()
    return dict(students=students)

def create():
    form = SQLFORM(db.students)
    if form.process().accepted:
        response.flash = 'Student created'
    elif form.errors:
        response.flash = 'The form has errors'
    else:
        response.flash = 'Please fill out the form'
    return dict(form=form)

def edit():
    record = db.students(request.args(0)) or redirect(URL('index'))
    form = SQLFORM(db.students, record)
    if form.process().accepted:
        response.flash = 'Student updated'
    elif form.errors:
        response.flash = 'The form has errors'
    else:
        response.flash = 'Please fill out the form'
    return dict(form=form)

def delete():
    db(db.students.id == request.args(0)).delete()
    redirect(URL('index'))


# API Service.

@request.restful()
def api():
    def GET(*args, **vars):
        patterns = 'auto'
        parser = db.parse_as_rest(patterns, args, vars)
        if parser.status == 200:
            return dict(content=parser.response)
        else:
            raise HTTP(parser.status, parser.error)

    def POST(table_name, **vars):
        if table_name == 'student':
            return db.student.validate_and_insert(**vars)

    def PUT(table_name, record_id, **vars):
        if table_name == 'student':
            return db(db.student.id == record_id).validate_and_update(**vars)

    def DELETE(table_name, record_id):
        if table_name == 'student':
            return db(db.student.id == record_id).delete()

    return locals()
