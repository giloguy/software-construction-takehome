package com.giloguy.examcs.payloads;

import java.util.List;

public class CreateOrderDTO {
    private List<Long> bookIds;

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
