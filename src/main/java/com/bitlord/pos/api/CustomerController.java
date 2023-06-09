package com.bitlord.pos.api;


import com.bitlord.pos.dto.request.RequestCustomerDto;
import com.bitlord.pos.service.CustomerService;
import com.bitlord.pos.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*

        consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}
*/
 //test
@RestController
@RequestMapping( "/api/v1/customers" )
@CrossOrigin
public class CustomerController { // Customer CRUD

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


     /*
    * {
    "name":"kamal addarararchchi",
    "address":"Moratuwa",
    "salary":25000
} *
    *
    * */

    @PostMapping
    @PreAuthorize( "hasAnyRole( 'ROLE_ADMIN' )" )
    public ResponseEntity<StandardResponse> createCustomer(@RequestBody RequestCustomerDto customerDto ) {

        return new ResponseEntity<>(  new StandardResponse( 201, "cutomer saved!", customerService.createCustomer(customerDto) ),  HttpStatus.CREATED ) ;
    }


    @GetMapping( "/{id}" )
    @PreAuthorize( "hasAnyRole( 'ROLE_ADMIN', 'ROLE_USER', 'ROLE_MANAGER' )" )
    public ResponseEntity<StandardResponse> findCustomer (@PathVariable int id) {

        return new ResponseEntity<>( new StandardResponse( 200, "customer Data!", customerService.findCustomer(id)), HttpStatus.OK );
    }


    @PutMapping(params = "id")
    @PreAuthorize( "hasAnyRole( 'ROLE_ADMIN', 'ROLE_MANAGER' )" )
    public ResponseEntity<StandardResponse> updateCustomer( @RequestParam int id,  @RequestBody RequestCustomerDto customerDto ) throws ClassNotFoundException {

        return new ResponseEntity<>(
                new StandardResponse(201,"customer Updated!", customerService.updateCustomer( customerDto, id )), HttpStatus.CREATED
        );

    }


    @DeleteMapping(params = "id")
    @PreAuthorize( "hasAnyRole( 'ROLE_ADMIN' )" )
    public ResponseEntity<StandardResponse> deleteCustomer( @RequestParam int id ) throws ClassNotFoundException {

        customerService.deleteCustomer(id);

        return new ResponseEntity<>( new StandardResponse(204,"customer Deleted!",null), HttpStatus.NO_CONTENT );
    }


    @GetMapping(value = "/list", params = { "page","size","searchText" } )
    @PreAuthorize( "hasAnyRole( 'ROLE_ADMIN', 'ROLE_USER', 'ROLE_MANAGER' )" )
    public ResponseEntity<StandardResponse> getAllCustomers( @RequestParam int page, @RequestParam int size, @RequestParam String searchText )  {

        return new ResponseEntity<>( new StandardResponse(200,"customer list" ,customerService.searchAllCustomers( page, size, searchText ) ), HttpStatus.OK );
    }

}
