package com.bookstore.dto.requestDTO;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String writerNum;

    public UserRequestDTO(String writerNum) {
        this.writerNum = writerNum;
    }
}
