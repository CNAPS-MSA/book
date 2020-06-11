package com.skcc.book.domain;


import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class BookReservation {

    @Column(name = "user_id")
    private Long userId;

    @Column(name="reserved_seq_no")
    private Long reservedSeqNo;

    public BookReservation() {
    }

    public BookReservation(Long userId, Long reservedSeqNo) {
        this.userId = userId;
        this.reservedSeqNo = reservedSeqNo;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getReservedSeqNo() {
        return reservedSeqNo;
    }

}
