package com.bookstore.mapper;

import com.bookstore.dto.requestDTO.UserRequestDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    String selectWriterName(UserRequestDTO userRequestDTO);
}
