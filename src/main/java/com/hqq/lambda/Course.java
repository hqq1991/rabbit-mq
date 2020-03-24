package com.hqq.lambda;

import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 2896201730223729591L;

    /**
     * 课程 ID
     */
    private Long id;

    /**
     * 课程 name
     */
    private String name;

    public Course(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
