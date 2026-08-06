package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Student;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {
    // 嵌套 Select 查询：先查学生，再按 id 查课程
    List<Student> selectAllStudents();

    // 嵌套结果映射：一次 join 查出学生+课程
    List<Student> selectAllStudents2();

    // 根据课程 id 查询学生（给 Subject 嵌套 Select 用）
    @Select("select stu.id, stu.name, stu.sex " +
            "from t_student stu join t_stu_sub ss on stu.id = ss.student_id " +
            "where ss.subject_id = #{id}")
    List<Student> selectStudentBySubjectId(Integer id);
}