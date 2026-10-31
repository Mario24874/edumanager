# This file defines the student_controller.py driver, which contains the functions to perform CRUD (Create, Read, Update, Delete) operations on the subjects table in the database.

def index():
    subjects = db(db.subjects).select()
    return dict(subjects=subjects)

def create():
    form = SQLFORM(db.subjects)
    if form.process().accepted:
        response.flash = 'Subject created'
    elif form.errors:
        response.flash = 'The form has errors'
    else:
        response.flash = 'Please fill out the form'
    return dict(form=form)

def edit():
    record = db.subjects(request.args(0)) or redirect(URL('index'))
    form = SQLFORM(db.subjects, record)
    if form.process().accepted:
        response.flash = 'Subject updated'
    elif form.errors:
        response.flash = 'The form has errors'
    else:
        response.flash = 'Please fill out the form'
    return dict(form=form)

def delete():
    db(db.subjects.id == request.args(0)).delete()
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
        if table_name == 'subject':
            return db.subject.validate_and_insert(**vars)

    def PUT(table_name, record_id, **vars):
        if table_name == 'subject':
            return db(db.subject.id == record_id).validate_and_update(**vars)

    def DELETE(table_name, record_id):
        if table_name == 'subject':
            return db(db.subject.id == record_id).delete()

    return locals()
