package kr.edcan.grovenue.model;

/**
 * Created by JunseokOh on 2016. 9. 24..
 */
public class User {
    private boolean isMale;
    private int age, purpose, budget;
    private String job, name, id, token;

    public User(boolean isMale, int age, int purpose, int budget, String job, String name, String id, String token) {
        this.isMale = isMale;
        this.age = age;
        this.purpose = purpose;
        this.budget = budget;
        this.job = job;
        this.name = name;
        this.id = id;
        this.token = token;
    }

    public boolean isMale() {
        return isMale;
    }

    public int getAge() {
        return age;
    }

    public int getPurpose() {
        return purpose;
    }

    public int getBudget() {
        return budget;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
