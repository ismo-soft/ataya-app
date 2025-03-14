package com.ataya.address.controller;


import com.ataya.address.dto.address.request.CreateAddressByCoordinatesRequest;
import com.ataya.address.dto.address.request.CreateAddressRequest;
import com.ataya.address.dto.address.request.UpdateAddressRequest;
import com.ataya.address.service.AddressManagementService;
import com.ataya.address.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address/management")
public class AddressManagementController {

    private final AddressManagementService addressManagementService;

    public AddressManagementController(AddressManagementService addressManagementService) {
        this.addressManagementService = addressManagementService;
    }

    @PostMapping("/create/info")
    @Operation(
            summary = "Create a new address by information",
            description = "Create a new address with the given information, be sure about providing tags on the request \n" +
                    "/address/debug/tags"
    )
    public ResponseEntity<ApiResponse> createAddress(@RequestBody CreateAddressRequest createAddressrequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressManagementService.createAddress(createAddressrequest));
    }

    @PostMapping("/create/coordinates")
    @Operation(
            summary = "Create a new address by coordinates",
            description = "Create a new address with the given coordinates, be sure about providing tags on the request \n" +
                    "/address/debug/tags"
    )
    public ResponseEntity<ApiResponse> createAddress(@RequestBody CreateAddressByCoordinatesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressManagementService.createAddress(request));
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Update an address",
            description = "Update an address with the given information, be sure about providing tags on the request \n" +
                    "/address/debug/tags"
    )
    public ResponseEntity<ApiResponse> updateAddress(@RequestBody UpdateAddressRequest request, @PathVariable String id) {
        System.out.println("update address");
        return ResponseEntity.status(HttpStatus.OK).body(addressManagementService.updateAddress(request, id));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getAddress(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(addressManagementService.getAddress(id));
    }
}
