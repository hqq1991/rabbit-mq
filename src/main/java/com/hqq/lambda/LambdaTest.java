package com.hqq.lambda;

import com.rabbitmq.tools.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaTest {

    private final static List<StudentDTO> students = new ArrayList<StudentDTO>(){
        {
            // 添加学生数据
            add(new StudentDTO(1L,"W199","小美","WM",100D,new ArrayList<Course>(){
                {
                    // 添加学生学习的课程
                    add(new Course(300L,"语文"));
                    add(new Course(301L,"数学"));
                    add(new Course(302L,"英语"));
                }
            }));
            add(new StudentDTO(2L,"W25","小美","WM",100D,new ArrayList()));
            add(new StudentDTO(3L,"W3","小名","M",90D,new ArrayList<Course>(){
                {
                    add(new Course(300L,"语文"));
                    add(new Course(304L,"体育"));
                }
            }));
            add(new StudentDTO(4L,"W1","小蓝","M",10D,new ArrayList<Course>(){
                {
                    add(new Course(301L,"数学"));
                    add(new Course(305L,"美术"));
                }
            }));
        }
    };


    public static void main(String[] args) {
        // 得到所有学生的学号
        // 这里 students.stream() 中的元素是 StudentDTO，通过 map 方法转化成 String 的流
        //StudentDTO::getCode 是 s->s.getCode() 的简写
        /*List<String> codes = students.stream()
                .map(s -> s.getCode())
                .collect(Collectors.toList());
        System.out.println("TestMap 所有学生的学号为 {}"+ codes);

        List<Integer> ids = students.stream()
                .mapToInt(s -> Integer.parseInt(s.getId() + ""))
                .mapToObj(s -> s)
                .collect(Collectors.toList());
        System.out.println("TestMapToInt result is {}"+ids);


        List<Course> courses = students.stream()
                .flatMap(s->s.getLearningCources().stream())
                .collect(Collectors.toList());*/

        // 计算学生所有的学习课程，map 返回两层课程嵌套格式
       /* List<List<Course>> courses2 = students.stream().map(s->s.getLearningCources())
                .collect(Collectors.toList());

        List<Stream<Course>> courses3 = students.stream().map(s->s.getLearningCources().stream())
                .collect(Collectors.toList());*/

       //计算学生总分
        Double aDouble = students.stream().map(s -> s.getScope()).reduce((s1, s2) -> s1 + s2).orElse(0d);
        System.out.println(aDouble);

        Long id2 = students.stream()
                .filter(s->s.getName().equals("小天"))
                .findFirst()
                // orElse 表示如果 findFirst 返回 null 的话，就返回 orElse 里的内容
                .orElse(new StudentDTO()).getId();

        Long id = students.stream()
                .filter(s->"小美".equals(s.getName()))
                // 同学中有两个叫小美的，这里匹配到第一个就返回
                .findFirst()
                .get().getId();


        // 统计姓名重名的学生有哪些
        Map<String, Set<String>> map2 = students.stream()
                .collect(Collectors.groupingBy(StudentDTO::getName,
                        Collectors.mapping(StudentDTO::getCode,Collectors.toSet())));


    }






}

