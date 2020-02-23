package com.java.tech.lambda;

/**
 * created by Jason on 2020/2/22
 */
public interface PersonFactory<P extends Person> {

    P create(String firstName,String lastName);

}
