# This file defines the student_controller.py driver, which contains the functions to perform CRUD (Create, Read, Update, Delete) operations on the classrooms table in the database.

def index():
    classrooms = db(db.classrooms).select()
    return dict(classrooms=classrooms)

def create():
    form = SQLFORM(db.classrooms)
    if form.process().accepted:
        response.flash = 'Classroom created'
    elif form.errors:
        response.flash = 'The form has errors'
    else:
        response.flash = 'Please fill out the form'
    return dict(form=form)

def edit():
    record = db.classrooms(request.args(0)) or redirect(URL('index'))
    form = SQLFORM(db.classrooms, record)
    if form.process().accepted:
        response.flash = 'Classroom updated'
    elif form.errors:
        response.flash = 'The form has errors'
    else:
        response.flash = 'Please fill out the form'
    return dict(form=form)

def delete():
    db(db.classrooms.id == request.args(0)).delete()
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
        if table_name == 'classroom':
            return db.classroom.validate_and_insert(**vars)

    def PUT(table_name, record_id, **vars):
        if table_name == 'classroom':
            return db(db.classroom.id == record_id).validate_and_update(**vars)

    def DELETE(table_name, record_id):
        if table_name == 'classroom':
            return db(db.classroom.id == record_id).delete()

    return locals()
