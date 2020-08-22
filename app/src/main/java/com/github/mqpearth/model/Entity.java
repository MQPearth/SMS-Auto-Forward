package com.github.mqpearth.model;

/**
 * Entity
 *
 * @author mpqearth
 * @date 2020/08/21 17:17
 */
public class Entity {

    public static Entity runtimeEntity;

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String email;
    private Boolean auto;

    public Entity() {
    }

    public Entity(String host, Integer port, String username, String password, String email, Boolean auto) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.auto = auto;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
