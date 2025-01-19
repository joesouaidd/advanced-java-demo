package fr.epita.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberRestController {

    @GetMapping
    public String getMembers() {
        System.out.println("hello from get!");
        return "GET method on /members is working!";
    }

    @PostMapping
    public String postMembers() {
        System.out.println("hello from post!");
        return "POST method on /members is working!";
    }
}
