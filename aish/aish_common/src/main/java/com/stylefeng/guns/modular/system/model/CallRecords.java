package com.stylefeng.guns.modular.system.model;

import java.util.Objects;

public class CallRecords implements Comparable<CallRecords>{
    private String name;//该电话号码人的名称
    private String relationship;//与客户的关系
    private int relation;//关系等级，1：本人，2：紧急联系人，3;一般联系人
    private String phoneNumber;//拨打的电话号码
    private int callNumber;//通话次数

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(Integer callNumber) {
        this.callNumber = callNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallRecords that = (CallRecords) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(relationship, that.relationship) &&
                Objects.equals(relation, that.relation) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(callNumber, that.callNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, relationship, relation, phoneNumber, callNumber);
    }

    @Override
    public String toString() {
        return "CallRecords{" +
                "name='" + name + '\'' +
                ", relationship='" + relationship + '\'' +
                ", relation=" + relation +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", callNumber=" + callNumber +
                '}';
    }

    @Override
    public int compareTo(CallRecords o) {

        CallRecords callRecords = (CallRecords)o;
        if(this.relation > callRecords.getRelation()){
            return 1;
        }else if(this.relation < callRecords.getRelation()){
            return -1;
        }else{
            if(this.callNumber > callRecords.getCallNumber()){
                return -1;
            }else if(this.callNumber < callRecords.getCallNumber()){
                return 1;
            }else{
                return 0;
            }
        }

    }

}
