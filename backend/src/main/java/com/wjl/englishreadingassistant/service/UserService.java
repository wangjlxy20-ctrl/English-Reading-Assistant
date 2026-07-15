package com.wjl.englishreadingassistant.service;

import com.wjl.englishreadingassistant.dto.LoginDTO;
import com.wjl.englishreadingassistant.dto.RegisterDTO;
import com.wjl.englishreadingassistant.entity.User;

public interface UserService {
    void register(RegisterDTO registerDTO);
    User login(LoginDTO loginDTO);
}
