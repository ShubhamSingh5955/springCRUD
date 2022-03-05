package com.lov2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.util.SortUtils;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Customer> getCustomers() {
		
		//get current hibernate session
		Session session= sessionFactory.getCurrentSession();
		//create query ... sort by lastName
		Query<Customer> theQuery= session.createQuery("from Customer order by lastName",
														Customer.class);
		//get result from query
		List<Customer> customers=theQuery.getResultList();
	
		//return list of customers
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		
		//get current hibernate session
		//save customer to database
		Session session= sessionFactory.getCurrentSession();
		session.saveOrUpdate(theCustomer);
		
	}

	@Override
	public Customer getCustomer(int theId) {
		//get current hibernate session
		Session currentSession= sessionFactory.getCurrentSession();
		//now retrieve /read from database using the primary key
		Customer customer=currentSession.get(Customer.class, theId);
		return customer;
	}

	@Override
	public void deleteCustomer(int theId) {
		System.out.println("Running in delete DAO");
		//get current hibernate session
	Session currentSession=sessionFactory.getCurrentSession();
	//delete object with primary key
	Query theQuery=currentSession.createQuery("delete from Customer where id = :customerId");
	
	theQuery.setParameter("customerId", theId);
	
	theQuery.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		//get current hibernate session
		Session currentSession=sessionFactory.getCurrentSession();
		

        Query theQuery = null;
		
        //
        // only search by name if theSearchName is not empty
        //
        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // search for firstName or lastName ... case insensitive
            theQuery =currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
        }
        else {
            // theSearchName is empty ... so just get all customers
            theQuery =currentSession.createQuery("from Customer", Customer.class);            
        }
        
        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();
        
     // return the results        
        return customers;
	}

	@Override
	public List<Customer> getCustomers(int theSortField) {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
				
		// determine sort field
		String theFieldName = null;		
		
		switch (theSortField) {
		case SortUtils.FIRST_NAME: 
			theFieldName = "firstName";
			break;
		case SortUtils.LAST_NAME:
			theFieldName = "lastName";
			break;
		case SortUtils.EMAIL:
			theFieldName = "email";
			break;
		default:
			// if nothing matches the default to sort by lastName
			theFieldName = "lastName";
		}
		
		// create a query  
		String queryString = "from Customer order by " + theFieldName;
		Query<Customer> theQuery = 
						currentSession.createQuery(queryString, Customer.class);
				
		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();
						
		// return the results		
		return customers;
		
	}
	
	

}






















