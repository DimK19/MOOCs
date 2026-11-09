/**
 * Object-Oriented Library System
 * C++ Programming Fundamentals Capstone Project
 * 
 * This file provides a complete implementation of a library management system
 * using object-oriented programming, smart pointers, and exception handling in C++.
 */

#include <iostream>
#include <string>
#include <vector>
#include <memory>
#include <stdexcept>
#include <algorithm>
#include <map>
#include <chrono>
#include <iomanip>
#include <functional>
#include <sstream>
#include <unordered_map>
#include <set>

//===================================================================
// Exception classes
//===================================================================

/**
 * Base exception class for library-related errors
 */
class LibraryException : public std::runtime_error {
public:
    LibraryException(const std::string& message) : std::runtime_error(message) {}
};

/**
 * Exception thrown when an item is not found
 */
class ItemNotFoundException : public LibraryException {
public:
    ItemNotFoundException(const std::string& id) 
        : LibraryException("Item not found: " + id) {}
};

/**
 * Exception thrown when a patron is not found
 */
class PatronNotFoundException : public LibraryException {
public:
    PatronNotFoundException(const std::string& id) 
        : LibraryException("Patron not found: " + id) {}
};

/**
 * Exception thrown when a checkout operation fails
 */
class CheckoutException : public LibraryException {
public:
    CheckoutException(const std::string& message) 
        : LibraryException("Checkout error: " + message) {}
};

/**
 * Exception thrown when a return operation fails
 */
class ReturnException : public LibraryException {
public:
    ReturnException(const std::string& message) 
        : LibraryException("Return error: " + message) {}
};

/**
 * Exception thrown when validation fails
 */
class ValidationException : public LibraryException {
public:
    ValidationException(const std::string& message) 
        : LibraryException("Validation error: " + message) {}
};

//===================================================================
// Library Item Classes
//===================================================================

/**
 * Base class for all library items
 */
class LibraryItem {
private:
    std::string id_;
    std::string title_;
    bool available_;
    
protected:
    double dailyFine_;
    int maxLoanDays_;

public:
    // Constructor
    LibraryItem(std::string id, std::string title)
        : id_(std::move(id)), title_(std::move(title)), available_(true),
          dailyFine_(0.0), maxLoanDays_(0)
    {}
    
    // Virtual destructor
    virtual ~LibraryItem() = default;
    
    // Getters
    std::string getId() const { return id_; }
    std::string getTitle() const { return title_; }
    bool isAvailable() const { return available_; }
    int getMaxLoanDays() const { return maxLoanDays_; }
    double getDailyFine() const { return dailyFine_; }
    
    // Setters
    void setAvailable(bool available) { available_ = available; }
    
    // Pure virtual methods
    virtual std::string getItemType() const = 0;
    virtual double calculateFine(int daysOverdue) const = 0;
    virtual std::string getDetails() const = 0;
    
    // Common functionality
    void checkOut() {
        if (!available_) {
            throw CheckoutException("Item is not available for checkout: " + id_);
        }
        available_ = false;
    }
    
    void returnItem() {
        if (available_) {
            throw ReturnException("Item is not checked out: " + id_);
        }
        available_ = true;
    }
};

/**
 * Book class - derives from LibraryItem
 */
class Book : public LibraryItem {
private:
    std::string author_;
    std::string isbn_;
    std::string genre_;
    int pageCount_;
    int publishYear
