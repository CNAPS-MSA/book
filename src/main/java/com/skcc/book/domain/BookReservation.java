package com.skcc.book.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Value
public class BookReservation implements Serializable {
    @Column(name="user_id")
    private Long userId ;
    @Column(name="reserved_seq_no")
    private Long reservedSeqNo;

    public BookReservation(){
        this.userId = null;
        this.reservedSeqNo = null;
    }


    //데이터 직렬화
    @JsonCreator
    public BookReservation(@JsonProperty("userId")Long userId,
                           @JsonProperty("reservedSeqNo")Long reservedSeqNo){
        this.userId = userId;
        this.reservedSeqNo=reservedSeqNo;
    }
}
