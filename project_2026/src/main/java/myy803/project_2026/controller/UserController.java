package myy803.project_2026.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;
import myy803.project_2026.services.UserService;
import myy803.project_2026.domainmodel.User;


@Controller
public class UserController {
	
	@Autowired
    UserService userService;

    @RequestMapping("/user/dashboard")
    public String getUserHome(Model model){
    	model.addAttribute("currentUser", userService.getCurrentUser());
        return "user/dashboard";
    }
    
    @RequestMapping("/user/profile")
    public String getProfile(Model model) {
    	model.addAttribute("user", userService.getCurrentUser()); 
        return "user/profile";
    }

    @PostMapping("/user/save_profile")
    public String saveProfile(@ModelAttribute("user") User userForm) {
        User existingUser = userService.findByUserName(userForm.getUsername());
        
        userService.updateCredentials(
            existingUser, 
            userForm.getName(), 
            userForm.getEmail(), 
            userForm.getPassword()
        );
        
        return "redirect:/user/dashboard";
    }
}