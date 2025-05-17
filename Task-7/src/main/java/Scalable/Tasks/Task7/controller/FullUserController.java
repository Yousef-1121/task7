package Scalable.Tasks.Task7.controller;

import Scalable.Tasks.Task7.model.FullUser;
import Scalable.Tasks.Task7.Services.FullUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/52_14017")  // This matches your student ID
public class FullUserController {

    @Autowired
    private FullUserService fullUserService;

    // ========== POST: Create New User ==========
    @PostMapping
    public void createFullUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String address,
                               @RequestParam String theme,
                               @RequestParam boolean notificationsEnabled) {

        fullUserService.createFullUser(username, password, firstName, lastName, email,
                phone, address, theme, notificationsEnabled);
    }

    // ========== GET: Fetch User by ID ==========
    @GetMapping("/{id}")
    public FullUser getFullUser(@PathVariable Integer id) {
        return fullUserService.getFullUserInformation(id);
    }
}
