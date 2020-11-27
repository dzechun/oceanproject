package com.fantechs.provider.client.controller;

import com.fantechs.provider.client.entity.User;
import com.fantechs.provider.client.server.impl.FanoutSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by lfz on 2020/11/27.
 */
@RestController
public class RabbitMQController {
    @Autowired
    FanoutSender fanoutSender;

    @GetMapping(value="/topic")
    public void topic(@ModelAttribute("user")User user) throws Exception{
        fanoutSender.send(user);
    }
}
