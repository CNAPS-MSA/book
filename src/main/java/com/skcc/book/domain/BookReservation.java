package com.skcc.book.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class BookReservation implements Serializable {
    @Column(name="user_id")
    private Long userId;
    @Column(name="reserved_seq_no")
    private Long reservedSeqNo;

    public BookReservation() {
//        this.userId=null;
//        this.reservedSeqNo=null;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReservedSeqNo() {
        return reservedSeqNo;
    }

    public void setReservedSeqNo(Long reservedSeqNo) {
        this.reservedSeqNo = reservedSeqNo;
    }

    //데이터 직렬화
    @JsonCreator
    public BookReservation(@JsonProperty("userId")Long userId,
                           @JsonProperty("reservedSeqNo")Long reservedSeqNo){
        this.userId = userId;
        this.reservedSeqNo=reservedSeqNo;
    }
}
