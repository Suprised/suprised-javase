package com.suprised.utils.guava;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

/**
 * Guava提供的一个对null Object的操作
 * 
 * Optional是对可以为空的对象进行的封装
 * 
 * Optinal是给了我们一个更有意义的“空”
 */
public class OptionalTest {

    public static void main(String[] args) {
        OptionalTest option = new OptionalTest();
        Person person = option.new Person();
        Optional<Person> _person = person.find("liujinxi");
        if (_person.isPresent())
            _person.get().doSomething();
        Optional<Person> nullPerson = person.find("hehe"); // null
        nullPerson.or(person.find("manager").get()).doSomething();// 当为null时,调用manager来做
    }
    
    /**
     * 如果我们知道自己要封装的对象是一个空对象，可以用
         Optional.absent();
                        如果封装的对象是一个非空对象，则可以用
                         Optional.of(obj);
                        如果不知道对象是否为空，就这样创建创建
                         Optional.fromNullable(obj);
                        有时候，当一个对象为null的时候，我们并不是简单的忽略，而是给出一个缺省值，比如找不到这个人，任务就交给经理来做。使用Optional可以很容易地做到这一点，以上面的代码为例：
          Optional person = people.find("John Smith");
          person.or(manager).doSomething()
     */
    public class Person {
        
        private Map<String, Person> personMap;
        
        public Person() {
            personMap = new HashMap<String, Person>();
            personMap.put("liujinxi", new Person("liujinxi"));
            personMap.put("manager", new Person("manager"));
        }
        
        public Person(String name) {
            this.name = name;
        }
        
        private String name ;
        
        public Optional<Person> find(String name) {
//            Optional.of(null);//   如果封装的对象是一个非空对象
//            Optional.fromNullable(null);//如果不知道对象是否为空
//            Optional.absent();//如果封装的对象是一个非空对象
            Person person = personMap.get(name);
            return Optional.fromNullable(person);
        }
        
        public void doSomething(){
            System.out.println(this.name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
}
