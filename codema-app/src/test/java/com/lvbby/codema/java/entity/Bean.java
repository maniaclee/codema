package com.lvbby.codema.java.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Bean {
    private int        id;
    private Date       updateTime;
    /** comment */
    private int        age;
    private String     memo;
    private BigDecimal account;
    private long       amount;
    private Long       remain;

    /**
     * Getter method for property   amount.
     *
     * @return property value of amount
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Setter method for property   amount .
     *
     * @param amount  value to be assigned to property amount
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }

    /**
     * Getter method for property   remain.
     *
     * @return property value of remain
     */
    public Long getRemain() {
        return remain;
    }

    /**
     * Setter method for property   remain .
     *
     * @param remain  value to be assigned to property remain
     */
    public void setRemain(Long remain) {
        this.remain = remain;
    }

    /**
     * Getter method for property   account.
     *
     * @return property value of account
     */
    public BigDecimal getAccount() {
        return account;
    }

    /**
     * Setter method for property   account .
     *
     * @param account  value to be assigned to property account
     */
    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    /**
    
     * Getter method for property   memo.
     *
     * @return property value of memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Setter method for property   memo .
     *
     * @param memo  value to be assigned to property memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * Getter method for property   id.
     *
     * @return property value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for property   id .
     *
     * @param id  value to be assigned to property id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for property   updateTime.
     *
     * @return property value of updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Setter method for property   updateTime .
     *
     * @param updateTime  value to be assigned to property updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Getter method for property   age.
     *
     * @return property value of age
     */
    public int getAge() {
        return age;
    }

    /**
     * Setter method for property   age .
     *
     * @param age  value to be assigned to property age
     */
    public void setAge(int age) {
        this.age = age;
    }
}