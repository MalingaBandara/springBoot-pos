package com.bitlord.pos.service.impl;


import com.bitlord.pos.dto.core.CustomerDto;
import com.bitlord.pos.dto.request.RequestCustomerDto;
import com.bitlord.pos.dto.response.ResponseCustomerDto;
import com.bitlord.pos.dto.response.paginated.model.CustomerPaginatedDto;
import com.bitlord.pos.entity.Customer;
import com.bitlord.pos.exception.EntryNotFoundException;
import com.bitlord.pos.repo.CustomerRepo;
import com.bitlord.pos.service.CustomerService;
import com.bitlord.pos.util.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, CustomerMapper customerMapper )  {
        this.customerRepo = customerRepo;
        this.customerMapper = customerMapper;
    }


    @Override
    public ResponseCustomerDto createCustomer(RequestCustomerDto dto) {

        CustomerDto customerDto = new CustomerDto(
                0,
                new Random().nextInt(100001),
                dto.getName(),
                dto.getAddress(),
                dto.getSalary(),
                true,
                null,
                null,
                null,
                null
        );

        /*Customer customer = new Customer(
                0,
                customerDto.getPublicId(),
                customerDto.getName(),
                customerDto.getAddress(),
                customerDto.getSalary(),
                customerDto.isActiveState(),
                null
        );

        customerRepo.save(customer);*/

        Customer customer = customerMapper.toCustomer(customerDto);

        customerRepo.save( customer );

        return customerMapper.toResponseCustomerDto( customer );

        /*return new ResponseCustomerDto(
                customerDto.getPublicId(),
                customerDto.getName(),
                customerDto.getAddress(),
                customerDto.getSalary(),
                customerDto.isActiveState()
        );*/
    }

    @Override
    public ResponseCustomerDto findCustomer(long id) {

        Optional< Customer > selectedCustomer = customerRepo.findByPublicId( id );

        if ( selectedCustomer.isPresent() ) {

            return  customerMapper.toResponseCustomerDto( selectedCustomer.get() );

           /* return new ResponseCustomerDto(
                    selectedCustomer.get().getPublicId(),
                    selectedCustomer.get().getName(),
                    selectedCustomer.get().getAddress(),
                    selectedCustomer.get().getSalary(),
                    selectedCustomer.get().isActiveState()
            );*/
        }

        // customerRepo.findById( id );
        throw  new EntryNotFoundException( "Hoyaganna Bariyoooo...!!" );
    }

    @Override
    public ResponseCustomerDto updateCustomer(RequestCustomerDto dto, long id) throws ClassNotFoundException {

        Optional<Customer> selectedCustomer = customerRepo.findByPublicId(id); // catch the customer

                if ( selectedCustomer.isEmpty() ) throw new ClassNotFoundException(); // check the customer

           // update details
            selectedCustomer.get().setName( dto.getName() );
            selectedCustomer.get().setAddress( dto.getAddress() );
            selectedCustomer.get().setSalary( dto.getSalary() );

        customerRepo.save( selectedCustomer.get() ); // save

        return customerMapper.toResponseCustomerDto( selectedCustomer.get() );

       /* return new ResponseCustomerDto( // return a response
                selectedCustomer.get().getPublicId(),
                selectedCustomer.get().getName(),
                selectedCustomer.get().getAddress(),
                selectedCustomer.get().getSalary(),
                selectedCustomer.get().isActiveState()
        );*/

    }

    @Override
    public void deleteCustomer(long id) {

        customerRepo.deleteByPublicId( id );

    }

    @Override
    public CustomerPaginatedDto searchAllCustomers(int page, int size, String searchText) {

        /* Page<Customer> customers = customerRepo.findAll(PageRequest.of( page, size )); // get customers */

         Page<Customer> customers = customerRepo.searchAllByAddressOrName( searchText, PageRequest.of( page, size )); // get customers

        List< ResponseCustomerDto > list = customerMapper.toResponseCustomerDtoList( customers );

            /*
            List<ResponseCustomerDto> list = new ArrayList<>(); // create array list of customer dto to response
        long recordCount = customerRepo.count(); // get count of repos */

        long recordCount = customerRepo.countDataWithSearhText( searchText );

            /*for ( Customer d: customers ) { // assign customers to the response list
                    list.add(new ResponseCustomerDto(
                            d.getPublicId(),
                            d.getName(),
                            d.getAddress(),
                            d.getSalary(),
                            d.isActiveState()
                    ));
            }*/


        return new CustomerPaginatedDto( recordCount , list ); // output / response

    }
}