package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("title", "Home Page");
        model.addAttribute("name", "SPringBoot Technology");
        model.addAttribute("githubRepo", "https://github.com/ronatkumar6084");
        System.out.println("Home Page Handler");
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage(Model model)
    {
        model.addAttribute("title", "About Page");
        System.out.println("About page loading");
        return "about";
    }

    @GetMapping("/services")
    public String sevices(Model model)
    {
        model.addAttribute("title", "Service Page");
        System.out.println("This is service page");
        return "service";
    }

    @GetMapping("/contact")
    public String contact(Model model)
    {
        model.addAttribute("title", "Contact Page");
        System.out.println("This is contact page");
        return "contact";
    }

    @GetMapping("/signin")
    public String signin(Model model)
    {
        model.addAttribute("title", "Signin Page");
        System.out.println("This is contact page");
        return "signin";
    }

    @GetMapping("/signup")
    public String signup(Model model)
    {
        model.addAttribute("title", "Signup Page");
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        System.out.println("This is contact page");
        return "signup";
    }

    

    //@PostMapping("/do_register")
    @RequestMapping(value="/do_register", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute  UserForm userForm, HttpSession session, BindingResult bindingResult)
    {
        System.out.println(" Registering User");
        //fetch formdata
        System.out.println(userForm);
       
        //validate formdata
        if(bindingResult.hasErrors()){
            return "signup";
        }


        //save to database //user service
    //     User user = User.builder()
    //     .name(userForm.getName())
    //     .email(userForm.getEmail())
    //     .password(userForm.getPassword())
    //     .about(userForm.getAbout())
    //     .phoneNumber(userForm.getPhoneNumber())
    //    .profilePic("https://scontent.fblr23-1.fna.fbcdn.net/v/t1.6435-1/40141619_1922082084759473_6921567482310295552_n.jpg?stp=c6.0.40.40a_cp0_dst-jpg_p40x40&_nc_cat=109&ccb=1-7&_nc_sid=5f2048&_nc_ohc=mTgEdk_GtfMQ7kNvgFoq8EM&_nc_ht=scontent.fblr23-1.fna&oh=00_AYB6K9qhX3yxE7dcdA3ljQULFWLFzyUtFmWI5-4RkTGBcg&oe=669DE3E1")
    //     .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);

        User savedUser = userService.saveUser(user);
        System.out.println("User Saved."+savedUser);


        //message= "Registration successful"
        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message",message);

        //redirect to loginpage
        return "redirect:/signup";
    }

}
