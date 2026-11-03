/**
 * ============================================================================
 * Command Line Utility Tool - 100% Solution
 * C++ Programming Fundamentals Capstone Project
 * ============================================================================
 * 
 * DESCRIPTION:
 * This program implements a comprehensive command-line utility tool with
 * multiple functions accessible through an interactive menu system.
 * 
 * FEATURES:
 * - Menu-driven interface with robust error handling
 * - Basic Calculator with full input validation
 * - Temperature Converter with physics validation
 * - Word and Character Counter with comprehensive statistics
 * - Prime Number Checker with factorization
 * 
 * STANDARDS:
 * - Follows C++ best practices and naming conventions
 * - Comprehensive input validation throughout
 * - Modular design with clear separation of concerns
 * - Extensive documentation and error handling
 * 
 * ============================================================================
 */

#include <iostream>
#include <string>
#include <limits>
#include <cmath>
#include <algorithm>
#include <cctype>
#include <iomanip>

using namespace std;

// ============================================================================
// GLOBAL CONSTANTS
// ============================================================================

// Physical constants for temperature validation
const double ABSOLUTE_ZERO_CELSIUS = -273.15;
const double ABSOLUTE_ZERO_FAHRENHEIT = -459.67;
const double ABSOLUTE_ZERO_KELVIN = 0.0;

// Menu option boundaries
const int MIN_MENU_OPTION = 1;
const int MAX_MENU_OPTION = 5;

// ============================================================================
// FUNCTION PROTOTYPES
// ============================================================================

// Menu and navigation functions
void displayWelcomeBanner();
void displayMenu();
void displayExitMessage();
void clearInputBuffer();
void waitForEnter();

// Input validation helper functions
double getValidDouble(const string& prompt);
int getValidInt(const string& prompt, int minVal, int maxVal);
char getValidOperation();

// Utility functions
void basicCalculator();
void temperatureConverter();
void wordCounter();
void primeNumberChecker();

// Helper functions
bool validateTemperature(double temp, int unit);
bool isPrime(int n);
void displayPrimeFactorization(int n);

// ============================================================================
// MAIN FUNCTION
// ============================================================================

/**
 * Main program driver
 * 
 * PURPOSE:
 * Controls the main program flow with a menu-driven loop. Implements
 * comprehensive error handling to ensure stable user experience.
 * 
 * ALGORITHM:
 * 1. Display welcome banner
 * 2. Enter main loop
 * 3. Display menu and get validated choice
 * 4. Execute selected utility function
 * 5. Handle errors gracefully
 * 6. Continue until user exits
 * 
 * @return 0 on successful program termination
 */
int main() {
    // Display welcome message
    displayWelcomeBanner();
    
    // Main program loop
    bool programRunning = true;
    
    while (programRunning) {
        // Display menu
        displayMenu();
        
        // Get and validate menu choice
        int choice;
        if (!(cin >> choice)) {
            cout << "\n[ERROR] Invalid input. Please enter a number between " 
                 << MIN_MENU_OPTION << " and " << MAX_MENU_OPTION << "." << endl;
            clearInputBuffer();
            waitForEnter();
            continue;
        }
        
        // Validate menu choice range
        if (choice < MIN_MENU_OPTION || choice > MAX_MENU_OPTION) {
            cout << "\n[ERROR] Invalid choice. Please select option " 
                 << MIN_MENU_OPTION << "-" << MAX_MENU_OPTION << "." << endl;
            clearInputBuffer();
            waitForEnter();
            continue;
        }
        
        clearInputBuffer();
        
        // Execute selected utility
        switch (choice) {
            case 1:
                basicCalculator();
                break;
            case 2:
                temperatureConverter();
                break;
            case 3:
                wordCounter();
                break;
            case 4:
                primeNumberChecker();
                break;
            case 5:
                displayExitMessage();
                programRunning = false;
                break;
        }
    }
    
    return 0;
}

// ============================================================================
// MENU AND NAVIGATION FUNCTIONS
// ============================================================================

/**
 * Displays welcome banner with program information
 */
void displayWelcomeBanner() {
    cout << "\n╔═══════════════════════════════════════════════════════╗" << endl;
    cout << "║                                                       ║" << endl;
    cout << "║      COMMAND LINE UTILITY TOOL - C++ PROJECT          ║" << endl;
    cout << "║                                                       ║" << endl;
    cout << "╚═══════════════════════════════════════════════════════╝" << endl;
    cout << "\nWelcome! This program provides various utility functions." << endl;
    cout << "All inputs are validated for accuracy and safety." << endl;
}

/**
 * Displays main menu with available options
 */
void displayMenu() {
    cout << "\n╔═══════════════════════════════════════════════════════╗" << endl;
    cout << "║                     MAIN MENU                         ║" << endl;
    cout << "╠═══════════════════════════════════════════════════════╣" << endl;
    cout << "║  1. Basic Calculator                                  ║" << endl;
    cout << "║  2. Temperature Converter                             ║" << endl;
    cout << "║  3. Word and Character Counter                        ║" << endl;
    cout << "║  4. Prime Number Checker                              ║" << endl;
    cout << "║  5. Exit Program                                      ║" << endl;
    cout << "╚═══════════════════════════════════════════════════════╝" << endl;
    cout << "Enter your choice (1-5): ";
}

/**
 * Displays professional exit message
 */
void displayExitMessage() {
    cout << "\n╔═══════════════════════════════════════════════════════╗" << endl;
    cout << "║              THANK YOU FOR USING                      ║" << endl;
    cout << "║          COMMAND LINE UTILITY TOOL                    ║" << endl;
    cout << "╚═══════════════════════════════════════════════════════╝" << endl;
    cout << "\nProgram terminated successfully." << endl;
}

/**
 * Clears the input buffer after failed or successful input
 * Essential for preventing cascading input errors
 */
void clearInputBuffer() {
    cin.clear();
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
}

/**
 * Pauses execution until user presses Enter
 * Provides control over navigation pace
 */
void waitForEnter() {
    cout << "\nPress ENTER to return to main menu...";
    cin.get();
}

// ============================================================================
// INPUT VALIDATION FUNCTIONS
// ============================================================================

/**
 * Gets and validates a double value from user
 * 
 * @param prompt Message to display to user
 * @return Validated double value
 */
double getValidDouble(const string& prompt) {
    double value;
    
    while (true) {
        cout << prompt;
        if (cin >> value) {
            clearInputBuffer();
            return value;
        }
        cout << "[ERROR] Invalid input. Please enter a valid number." << endl;
        clearInputBuffer();
    }
}

/**
 * Gets and validates an integer within specified range
 * 
 * @param prompt Message to display to user
 * @param minVal Minimum acceptable value
 * @param maxVal Maximum acceptable value
 * @return Validated integer within range
 */
int getValidInt(const string& prompt, int minVal, int maxVal) {
    int value;
    
    while (true) {
        cout << prompt;
        if (cin >> value) {
            if (value >= minVal && value <= maxVal) {
                clearInputBuffer();
                return value;
            }
            cout << "[ERROR] Value out of range. Enter between " 
                 << minVal << " and " << maxVal << "." << endl;
        } else {
            cout << "[ERROR] Invalid input. Please enter an integer." << endl;
            clearInputBuffer();
        }
    }
}

/**
 * Gets and validates arithmetic operation character
 * 
 * @return Valid operation character (+, -, *, /)
 */
char getValidOperation() {
    char op;
    
    while (true) {
        cout << "Enter operation (+, -, *, /): ";
        cin >> op;
        clearInputBuffer();
        
        if (op == '+' || op == '-' || op == '*' || op == '/') {
            return op;
        }
        cout << "[ERROR] Invalid operation. Please use +, -, *, or /." << endl;
    }
}

// ============================================================================
// UTILITY FUNCTION 1: BASIC CALCULATOR
// ============================================================================

/**
 * Basic Calculator - Performs arithmetic operations
 * 
 * FUNCTIONALITY:
 * Accepts two numbers and an operation, performs calculation with
 * comprehensive error handling including division by zero.
 */
void basicCalculator() {
    cout << "\n╔═══════════════════════════════════════════════════════╗" << endl;
    cout << "║                 BASIC CALCULATOR                      ║" << endl;
    cout << "╚═══════════════════════════════════════════════════════╝" << endl;
    cout << "\nPerform arithmetic operations on two numbers." << endl;
    
    // Get validated input
    double num1 = getValidDouble("\nEnter first number: ");
    double num2 = getValidDouble("Enter second number: ");
    char operation = getValidOperation();
    
    // Perform calculation
    double result;
    bool validCalculation = true;
    
    cout << "\n────────────────────────────────────────────────────────" << endl;
    
    switch (operation) {
        case '+':
            result = num1 + num2;
            cout << "Operation: Addition" << endl;
            break;
        case '-':
            result = num1 - num2;
            cout << "Operation: Subtraction" << endl;
            break;
        case '*':
            result = num1 * num2;
            cout << "Operation: Multiplication" << endl;
            break;
        case '/':
            cout << "Operation: Division" << endl;
            if (num2 == 0.0) {
                cout << "\n[ERROR] Division by zero is undefined!" << endl;
                cout << "Cannot divide " << num1 << " by 0." << endl;
                validCalculation = false;
            } else {
                result = num1 / num2;
            }
            break;
    }
    
    // Display result
    if (validCalculation) {
        cout << fixed << setprecision(6);
        cout << "\nExpression: " << num1 << " " << operation << " " << num2 << endl;
        cout << "RESULT: " << result << endl;
    }
    
    cout << "────────────────────────────────────────────────────────" << endl;
    
    waitForEnter();
}

// ============================================================================
// UTILITY FUNCTION 2: TEMPERATURE CONVERTER
// ============================================================================

/**
 * Validates temperature against absolute zero
 * 
 * @param temp Temperature value to validate
 * @param unit Temperature unit (1=C, 2=F, 3=K)
 * @return true if valid, false if below absolute zero
 */
bool validateTemperature(double temp, int unit) {
    if (unit == 1 && temp < ABSOLUTE_ZERO_CELSIUS) {
        cout << "\n[ERROR] Temperature below absolute zero!" << endl;
        cout << "Absolute zero in Celsius: " << ABSOLUTE_ZERO_CELSIUS << "°C" << endl;
        cout << "Your input: " << temp << "°C" << endl;
        return false;
    } else if (unit == 2 && temp < ABSOLUTE_ZERO_FAHRENHEIT) {
        cout << "\n[ERROR] Temperature below absolute zero!" << endl;
        cout << "Absolute zero in Fahrenheit: " << ABSOLUTE_ZERO_FAHRENHEIT << "°F" << endl;
        cout << "Your input: " << temp << "°F" << endl;
        return false;
    } else if (unit == 3 && temp < ABSOLUTE_ZERO_KELVIN) {
        cout << "\n[ERROR] Kelvin temperature cannot be negative!" << endl;
        cout << "Absolute zero: 0 K" << endl;
        cout << "Your input: " << temp << " K" << endl;
        return false;
    }
    return true;
}

/**
 * Temperature Converter - Converts between temperature units
 * 
 * FUNCTIONALITY:
 * Converts temperatures between Celsius, Fahrenheit, and Kelvin
 * with physics validation against absolute zero.
 */
void temperatureConverter() {
    cout << "\n╔═══════════════════════════════════════════════════════╗" << endl;
    cout << "║             TEMPERATURE CONVERTER                     ║" << endl;
    cout << "╚═══════════════════════════════════════════════════════╝" << endl;
    cout << "\nConvert temperatures between Celsius, Fahrenheit, and Kelvin." << endl;
    
    const string unitNames[] = {"Celsius (°C)", "Fahrenheit (°F)", "Kelvin (K)"};
    
    // Get temperature value
    double temperature = getValidDouble("\nEnter temperature value: ");
    
    // Get source unit
    cout << "\nSelect current unit:" << endl;
    cout << "  1. Celsius (°C)" << endl;
    cout << "  2. Fahrenheit (°F)" << endl;
    cout << "  3. Kelvin (K)" << endl;
    int fromUnit = getValidInt("Enter choice (1-3): ", 1, 3);
    
    // Validate against absolute zero
    if (!validateTemperature(temperature, fromUnit)) {
        waitForEnter();
        return;
    }
    
    // Get target unit
    cout << "\nSelect target unit:" << endl;
    cout << "  1. Celsius (°C)" << endl;
    cout << "  2. Fahrenheit (°F)" << endl;
    cout << "  3. Kelvin (K)" << endl;
    int toUnit = getValidInt("Enter choice (1-3): ", 1, 3);
    
    // Perform conversion via Celsius intermediate
    double celsius;
    if (fromUnit == 1) {
        celsius = temperature;
    } else if (fromUnit == 2) {
        celsius = (temperature - 32.0) * 5.0 / 9.0;
    } else {
        celsius = temperature - 273.15;
    }
    
    double result;
    if (toUnit == 1) {
        result = celsius;
    } else if (toUnit == 2) {
        result = celsius * 9.0 / 5.0 + 32.0;
    } else {
        result = celsius + 273.15;
    }
    
    // Display result
    cout << "\n────────────────────────────────────────────────────────" << endl;
    cout << fixed << setprecision(2);
    cout << "Original:  " << temperature << "° " << unitNames[fromUnit - 1] << endl;
    cout << "Converted: " << result << "° " << unitNames[toUnit - 1] << endl;
    cout << "────────────────────────────────────────────────────────" << endl;
    
    waitForEnter();
}

// ============================================================================
// UTILITY FUNCTION 3: WORD COUNTER
// ============================================================================

/**
 * Word and Character Counter - Analyzes text statistics
 * 
 * FUNCTIONALITY:
 * Reads multi-line text and calculates comprehensive statistics
 * including words, characters, lines, and derived metrics.
 */
void wordCounter() {
    cout << "\n╔═══════════════════════════════════════════════════════╗" << endl;
    cout << "║          WORD AND CHARACTER COUNTER                   ║" << endl;
    cout << "╚═══════════════════════════════════════════════════════╝" << endl;
    cout << "\nAnalyze text for comprehensive statistics." << endl;
    
    cout << "\nINSTRUCTIONS:" << endl;
    cout << "  • Type or paste your text" << endl;
    cout << "  • Press ENTER after each line" << endl;
    cout << "  • Enter a blank line when finished" << endl;
    cout << "\nBegin entering text:\n" << endl;
    
    // Initialize counters
    int wordCount = 0;
    int charCountWithSpaces = 0;
    int charCountNoSpaces = 0;
    int lineCount = 0;
    
    string line;
    clearInputBuffer();
    
    // Read text line by line
    while (getline(cin, line)) {
        if (line.empty() && lineCount > 0) {
            break;
        }
        if (line.empty()) {
            continue;
        }
        
        lineCount++;
        charCountWithSpaces += line.length();
        
        // Count non-space characters
        charCountNoSpaces += count_if(line.begin(), line.end(),
            [](char c) { return !isspace(c); });
        
        // Count words
        bool inWord = false;
        for (char c : line) {
            if (isspace(c)) {
                inWord = false;
            } else if (!inWord) {
                wordCount++;
                inWord = true;
            }
        }
    }
    
    // Display comprehensive results
    cout << "\n╔═══════════════════════════════════════════════════════╗" << endl;
    cout << "║              TEXT ANALYSIS RESULTS                    ║" << endl;
    cout << "╚═══════════════════════════════════════════════════════╝" << endl;
    cout << "\n  Total Words:                  " << wordCount << endl;
    cout << "  Total Characters (w/ spaces): " << charCountWithSpaces << endl;
    cout << "  Total Characters (no spaces): " << charCountNoSpaces << endl;
    cout << "  Total Lines:                  " << lineCount << endl;
    
    // Calculate derived statistics
    if (lineCount > 0) {
        double avgWordsPerLine = static_cast<double>(wordCount) / lineCount;
        cout << fixed << setprecision(1);
        cout << "  Average Words per Line:       " << avgWordsPerLine << endl;
    }
    
    if (wordCount > 0) {
        double avgCharsPerWord = static_cast<double>(charCountNoSpaces) / wordCount;
        cout << fixed << setprecision(1);
        cout << "  Average Characters per Word:  " << avgCharsPerWord << endl;
    }
    
    cout << "───────────────────────────────────────────────────────" << endl;
    
    waitForEnter();
}

// ============================================================================
// UTILITY FUNCTION 4: PRIME NUMBER CHECKER
// ============================================================================

/**
 * Checks if a number is prime using optimized algorithm
 * 
 * @param n Number to check
 * @return true if prime, false otherwise
 */
bool isPrime(int n) {
    if (n <= 1) return false;
    if (n <= 3) return true;
    if (n % 2 == 0 || n % 3 == 0) return false;
    
    // Check divisors of form 6k±1 up to √n
    for (int i = 5; i * i <= n; i += 6) {
        if (n % i == 0 || n % (i + 2) == 0) {
            return false;
        }
    }
    return true;
}

/**
 * Displays prime factorization of composite number
 * 
 * @param n Number to factorize
 */
void displayPrimeFactorization(int n) {
    cout << "\nPrime Factorization: " << n << " = ";
    
    int remaining = n;
    bool first = true;
    
    for (int i = 2; i <= remaining; i++) {
        int exponent = 0;
        while (remaining % i == 0) {
            exponent++;
            remaining /= i;
        }
        
        if (exponent > 0) {
            if (!first) cout << " × ";
            cout << i;
            if (exponent > 1) cout << "^" << exponent;
            first = false;
        }
    }
    cout << endl;
}

/**
 * Prime Number Checker - Analyzes primality and factors
 * 
 * FUNCTIONALITY:
 * Determines if number is prime, provides factorization for
 * composite numbers, and finds the next prime number.
 */
void primeNumberChecker() {
    cout << "\n╔═══════════════════════════════════════════════════════╗" << endl;
    cout << "║              PRIME NUMBER CHECKER                     ║" << endl;
    cout << "╚═══════════════════════════════════════════════════════╝" << endl;
    cout << "\nCheck primality and explore number properties." << endl;
    
    // Get validated input
    int number = getValidInt("\nEnter a positive integer: ", 1, 2147483000);
    
    // Check primality
    bool isPrimeNumber = isPrime(number);
    
    // Display results
    cout << "\n────────────────────────────────────────────────────────" << endl;
    cout << "Number: " << number << endl;
    
    if (isPrimeNumber) {
        cout << "Classification: PRIME NUMBER" << endl;
        cout << "Properties: Only divisible by 1 and " << number << endl;
    } else {
        cout << "Classification: COMPOSITE NUMBER" << endl;
        if (number > 1) {
            displayPrimeFactorization(number);
        }
    }
    
    // Find next prime
    if (number < 2147483000) {
        int nextPrime = number + 1;
        while (!isPrime(nextPrime)) {
            nextPrime++;
        }
        cout << "\nNext Prime Number: " << nextPrime << endl;
        cout << "Prime Gap: " << (nextPrime - number) << endl;
    } else {
        cout << "\n[NOTE] Number too large to find next prime safely." << endl;
    }
    
    cout << "────────────────────────────────────────────────────────" << endl;
    
    waitForEnter();
}

// ============================================================================
// END OF PROGRAM
// ============================================================================