/** 
e; * Project Name:elastic-example 
 * File Name:Employee.java 
 * Package Name:com.rich.elasticsearch.document 
 * Date:2015年11月5日上午10:28:25 
 * 
*/  
  
package com.rich.elasticsearch.document;  

import java.util.Date;

/** 
 * ClassName:Employee <br/> 
 * Function: Employee data model. <br/> 
 * Date:     2015年11月5日 上午10:28:25 <br/> 
 * @author   rich 
 * @version   
 * @since    
 * @see       
 */
public class Employee {

    private String name;
    private Integer age;
    private String title;
    private Date birthday;
    
    public Employee(){
        super();
    }
    
    public Employee(String name, Integer age, String title, Date birthday) {
        this();
        this.name = name;
        this.age = age;
        this.title = title;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
}
  