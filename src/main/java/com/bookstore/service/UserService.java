package com.bookstore.service;

import com.bookstore.dto.requestDTO.UserRequestDTO;
import com.bookstore.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String getWriterName(UserRequestDTO dto) {

        return userMapper.selectWriterName(dto);
    }

}
