package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Subject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubjectMapper {

    // 根据学生 id 查询课程（给 Student 嵌套 Select 用）
    @Select("select sub.id, sub.name, grade " +
            "from t_subject sub join t_stu_sub ss on sub.id = ss.subject_id " +
            "where ss.student_id = #{id}")
    List<Subject> selectSubjectByStudentId(Integer id);

    // 嵌套 Select 查询：先查课程，再按 id 查学生
    List<Subject> selectAllSubjects();

    // 嵌套结果映射：一次 join 查出课程+学生
    List<Subject> selectAllSubjects2();
}
