// Student Registration Form

import React, { useState } from 'react';

interface Student {
  id: string;
  name: string;
  lastname: string;
  age: string;
  parentOrRepresentative: string;
  phone: string;
  email: string;
}

const initialStudent: Student = {
  id: '',
  name: '',
  lastname: '',
  age: '',
  parentOrRepresentative: '',
  phone: '',
  email: ''
};

function isValidEmail(email: string) {
  const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase());
}

function isValidAge(age: string) {
  const ageNumber = parseInt(age);
  return !isNaN(ageNumber) && ageNumber > 0 && ageNumber <= 120;
}

function isValidPhone(phone: string) {
  const re = /^\d{10}$/;
  return re.test(phone);
}

function StudentForm() {
  const [student, setStudent] = useState<Student>(initialStudent);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setStudent({
      ...student,
      [event.target.name]: event.target.value
    });
  };

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    if (!isValidEmail(student.email)) {
      alert('Please enter a valid email.');
      return;
    }
    if (!isValidAge(student.age)) {
      alert('Please enter a valid age.');
      return;
    }
    if (!isValidPhone(student.phone)) {
      alert('Please enter a valid phone number.');
      return;
    }
    // Aquí puedes manejar el envío del formulario, por ejemplo, llamando a tu API
    console.log(student);
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        ID:
        <input type="text" name="id" value={student.id} onChange={handleChange} />
      </label>
      <label>
        Name:
        <input type="text" name="name" value={student.name} onChange={handleChange} />
      </label>
      <label>
        Lastname:
        <input type="text" name="lastname" value={student.lastname} onChange={handleChange} />
      </label>
      <label>
        Age:
        <input type="number" name="age" value={student.age} onChange={handleChange} />
      </label>
      <label>
        Parent or Representative:
        <input type="text" name="parentOrRepresentative" value={student.parentOrRepresentative} onChange={handleChange} />
      </label>
      <label>
        Phone:
        <input type="tel" name="phone" value={student.phone} onChange={handleChange} />
      </label>
      <label>
        Email:
        <input type="email" name="email" value={student.email} onChange={handleChange} />
      </label>
      <button type="submit">Register</button>
    </form>
  );
}

export default StudentForm;
