package com.ataya.address.controller;


import com.ataya.address.enums.AddressTag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address/debug")
public class DebugController {
    // endpoint for all users
    @GetMapping("/enabled")
    @Operation(
            summary = "Enabled",
            description = "Enabled"
    )
    public String enabled() {
        return "Hello, enabled!";
    }

    // endpoint for admin only
    @PostMapping("/hi-admin")
    @Operation(
            summary = "Hi Admin",
            description = "Hi Admin"
    )
    public String hiAdmin() {
        return "Hello, Admin!";
    }

    // return tags
    @GetMapping("/tags")
    @Operation(
            summary = "Tags",
            description = "Tags"
    )
    public List<AddressTag> tags() {
        return List.of(AddressTag.values());
    }
}
