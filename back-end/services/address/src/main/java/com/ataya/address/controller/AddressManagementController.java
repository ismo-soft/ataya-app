package com.ataya.address.controller;


import com.ataya.address.dto.address.request.CreateAddressByCoordinatesRequest;
import com.ataya.address.dto.address.request.CreateAddressRequest;
import com.ataya.address.dto.address.request.UpdateAddressRequest;
import com.ataya.address.service.AddressManagementService;
import com.ataya.address.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address/management")
public class AddressManagementController {

    @Autowired
    private AddressManagementService addressManagementService;
    /*
    * needs:
    * 1. create address
    * 2. update address
    * 3. get address
    *
    * */
    @PostMapping("/create/info")
    public ResponseEntity<ApiResponse> createAddress(@RequestBody CreateAddressRequest createAddressrequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressManagementService.createAddress(createAddressrequest));
    }
    @PostMapping("/create/coordinates")
    public ResponseEntity<ApiResponse> createAddress(@RequestBody CreateAddressByCoordinatesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressManagementService.createAddress(request));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateAddress(@RequestBody UpdateAddressRequest request, @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(addressManagementService.updateAddress(request, id));
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getAddress(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(addressManagementService.getAddress(id));
    }
}
