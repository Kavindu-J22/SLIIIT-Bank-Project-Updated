package com.bankingsystem.controller;

import com.bankingsystem.model.FixDeposit;
import com.bankingsystem.model.FixDepositStatus;
import com.bankingsystem.model.Role;
import com.bankingsystem.model.User;
import com.bankingsystem.service.FixDepositService;
import com.bankingsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fixdeposit")
public class FixDepositController {

    @Autowired
    private FixDepositService fixDepositService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String list(Model model, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        // Get user's fix deposits based on their role
        List<FixDeposit> fixDeposits;
        if (currentUser.getRole() == Role.CUSTOMER) {
            fixDeposits = fixDepositService.findByApplicant(currentUser);
        } else if (currentUser.getRole() == Role.STAFF || currentUser.getRole() == Role.ADMIN) {
            fixDeposits = fixDepositService.findAll();
        } else {
            return "error";
        }
        model.addAttribute("fixDeposits", fixDeposits);
        return "fixdeposit-list";
    }

    @GetMapping("/apply")
    public String applyForm(Model model) {
        model.addAttribute("fixDeposit", new FixDeposit());
        return "fixdeposit-apply";
    }

    @PostMapping("/apply")
    public String apply(@Valid @ModelAttribute FixDeposit fixDeposit, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "fixdeposit-apply";
        }
        User currentUser = getCurrentUser(authentication);
        fixDepositService.applyFixDeposit(fixDeposit, currentUser);
        return "redirect:/fixdeposit/list";
    }

    @GetMapping("/review/{id}")
    public String reviewForm(@PathVariable Long id, Model model, Authentication authentication) {
        FixDeposit fixDeposit = fixDepositService.findById(id).orElseThrow(() -> new RuntimeException("Fix Deposit not found"));
        User currentUser = getCurrentUser(authentication);
        if (currentUser.getRole() != Role.STAFF && currentUser.getRole() != Role.ADMIN) return "error";
        model.addAttribute("fixDeposit", fixDeposit);
        return "fixdeposit-review";
    }

    @PostMapping("/review/{id}")
    public String review(@PathVariable Long id, @ModelAttribute FixDeposit fixDeposit, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        fixDepositService.reviewFixDeposit(id, currentUser, FixDepositStatus.IN_REVIEW);
        return "redirect:/fixdeposit/list";
    }

    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser.getRole() != Role.ADMIN) return "error";
        fixDepositService.updateStatus(id, currentUser, FixDepositStatus.APPROVED);
        return "redirect:/fixdeposit/list";
    }

    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser.getRole() != Role.ADMIN) return "error";
        fixDepositService.updateStatus(id, currentUser, FixDepositStatus.REJECTED);
        return "redirect:/fixdeposit/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser.getRole() != Role.ADMIN) return "error";
        fixDepositService.deleteById(id);
        return "redirect:/fixdeposit/list";
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}