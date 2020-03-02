package com.java.tech.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * created by Jason on 2020/2/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hobby extends BaseEntity {

    private List<String> hobbys;
}
