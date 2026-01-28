select student.name, student.age, faculty.name, faculty.color
from student
         inner join faculty on student.faculty_id = faculty.id;

select student.name, student.age
from student
         inner join avatar on student.faculty_id = avatar.id;