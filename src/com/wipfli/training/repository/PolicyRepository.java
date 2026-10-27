package com.wipfli.training.repository;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;



public interface PolicyRepository extends JpaRepository<Policy, String> {


    /*
        Since we're now transitioning to database system - for data persistence using Hibernate
        Here we are extending the PolicyRepository interface from JpaRepository which provides us with CRUD operations and more.
        The need of InMemoryPolicyRepository we had would be eliminated with this addition.

        Some of the query names can be derived by the method name alone. But some of them such as
        countPoliciesGroupedByVehicleType, findCustomerNamesOrderedByPolicyCount etc cannot be derived from just the name.
        For that we can use @Query annotation

     */

    Optional<Policy> findByPolicyNumber(String policyNumber);
    List<Policy> findByPolicyHolderFirstName(String customerName);
    List<Policy> findByVehicleType(VehicleType vehicleType);
    List<Policy> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);

    // We are writing this query using JPQL - It uses Entity names and not the table names
    @Query(
            """
            SELECT p.vehicleType, COUNT(p)
            FROM Policy p
            GROUP BY p.vehicleType
            """
    )
    List<VehicleTypeCount> countPoliciesGroupedByVehicleType();

    @Query(
            """
            SELECT p.firstName, p.lastName, COUNT(po) 
FROM PolicyHolder p
LEFT JOIN Policy po 
ON po.policyHolder = p
GROUP BY p.userID, p.firstName, p.lastName
ORDER BY COUNT(po) DESC
            """
    )
    List<String> findCustomerNamesOrderedByPolicyCount(Pageable pageable);

}
