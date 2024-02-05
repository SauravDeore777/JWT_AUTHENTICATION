package com.restapi.assignment;

import com.restapi.assignment.entity.User;
import com.restapi.assignment.repository.UserRepository;
import com.restapi.assignment.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService theUserService;

    @Test
    public void createUserTest(){
        User user=new User("12313","test","test@gmail.com","test","testing ","test");
        theUserService.createUser(user);
        verify(userRepository,times(1)).save(user);
    }

}
