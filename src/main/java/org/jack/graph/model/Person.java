package org.jack.graph.model;

import lombok.*;


@Data
@EqualsAndHashCode(exclude = {"age", "name"})
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private String name;

    private String email;

    private Integer age;

    public Person(String email) {
        this(null, email, null);
    }
}
